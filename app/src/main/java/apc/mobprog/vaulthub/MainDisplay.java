package apc.mobprog.vaulthub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class MainDisplay extends AppCompatActivity implements View.OnClickListener{
    boolean isClicked;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_display );
        final Spinner spinner = findViewById( R.id.spinner2 );
        final FloatingActionButton userAcc = findViewById( R.id.userAction ),add = findViewById( R.id.addAccount ), update = findViewById( R.id.updateAccount ), delete = findViewById( R.id.deleteAccount ), updateLogin = findViewById( R.id.changeLogin );
        final Button showhide = findViewById( R.id.setPassVisibility );//init seperately since it is used by itself to change its own text
        final TextView user = findViewById( R.id.un ),use = findViewById( R.id.us ), pass = findViewById( R.id.pw );
        final TextView labeladd = findViewById( R.id.labelAdd ), labelupd = findViewById( R.id.labelUpd ), labeldel = findViewById( R.id.labelDel ),labelLogupd = findViewById( R.id.labelChg );
        final vaulthub.database.userInfo db = new vaulthub.database.userInfo( getApplicationContext() );
        List<String> userr = db.getSpinnerItems();//gets all the items under the column username which will be used as headers in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item,userr );//creates an adapter that the spinner needs
        spinner.setAdapter( adapter );
        add.setVisibility( View.INVISIBLE );
        update.setVisibility( View.INVISIBLE );
        delete.setVisibility( View.INVISIBLE );
        updateLogin.setVisibility( View.INVISIBLE );
        labeladd.setVisibility( View.INVISIBLE );
        labeldel.setVisibility( View.INVISIBLE );
        labelupd.setVisibility( View.INVISIBLE );
        labelLogupd.setVisibility( View.INVISIBLE );
        isClicked = false;
        userAcc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isClicked){
                    add.show();
                    labeladd.setVisibility( View.VISIBLE );
                    update.show();
                    labelupd.setVisibility( View.VISIBLE );
                    delete.show();
                    labeldel.setVisibility( View.VISIBLE );
                    updateLogin.show();
                    labelLogupd.setVisibility( View.VISIBLE );
                    isClicked = true;
                }
                else{
                    updateLogin.hide();
                    labelLogupd.setVisibility( View.INVISIBLE );
                    delete.hide();
                    labeldel.setVisibility( View.INVISIBLE );
                    update.hide();
                    labelupd.setVisibility( View.INVISIBLE );
                    add.hide();
                    labeladd.setVisibility( View.INVISIBLE );
                    isClicked = false;
                }
                System.out.println(isClicked);
            }
        } );
        add.setOnClickListener(this);
        delete.setOnClickListener( this );
        update.setOnClickListener( this );
        updateLogin.setOnClickListener( this );
        //changes the visibility of the password and the text inside the button
        showhide.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pass.getText().length() != 0){
                    if(showhide.getText().toString().equals( "Show" )&&pass.getTransformationMethod() == PasswordTransformationMethod.getInstance()){
                        pass.setTransformationMethod( HideReturnsTransformationMethod.getInstance() );
                        showhide.setText( R.string.hide );
                    }else{
                        pass.setTransformationMethod( PasswordTransformationMethod.getInstance() );
                        showhide.setText( R.string.show );
                    }
                }else{
                    return;//disables the functionality
                }
                //transforms the text to either a password or plain text

            }
        } );
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                    //reads the from the local .key file created
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getUserPublicKey) );
                    ObjectInputStream ois1 = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getUserPrivateKey ) );
                    PublicKey publicKey = (PublicKey) ois.readObject();
                    PrivateKey privateKey = (PrivateKey) ois1.readObject();
                    vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA(privateKey);
                    vaulthub.crypt.RSA RSA = new vaulthub.crypt.RSA(publicKey);
                    Cursor cursor = db.fetch( hex.getHexString( rsa.encrypt( spinner.getSelectedItem().toString()) ) );
                    cursor.moveToFirst();//if this method is not invoked it could cause an Exception called CursorIndexOutOfBoundsException
                    user.setText(RSA.decrypt(hex.getString(cursor.getString(1))));
                    pass.setText( RSA.decrypt( hex.getString( cursor.getString( 2 ) )) );
                    use.setText( RSA.decrypt( hex.getString( cursor.getString( 3 ) )) );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            //one of the implemented methods under AdatperView
            //also required
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        } );

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState( outState );
        outState.putBoolean( "key", isClicked );
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState( savedInstanceState );
        isClicked = savedInstanceState.getBoolean( "key" );
    }

    @Override
    public void onBackPressed() {
        //shows a dialog confirming if the user wants to logout
        new vaulthub.showDialog("Important", "Are you sure you want to Log out", 2, MainActivity.class, getApplicationContext()).show( getSupportFragmentManager(),"" );
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addAccount:
                startActivity( new Intent(getApplicationContext(), AddAccountActivity.class) );
                break;
            case R.id.deleteAccount:
                startActivity( new Intent(getApplicationContext(), DeleteAccountActivity.class) );
                break;
            case R.id.updateAccount:
                startActivity( new Intent(getApplicationContext(), UpdateAccountActivity.class) );
                break;
            case R.id.changeLogin:
                startActivity( new Intent(getApplicationContext(), UpdateLoginActivity.class) );
                break;
        }
    }
}