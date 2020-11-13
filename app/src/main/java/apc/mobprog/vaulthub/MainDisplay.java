package apc.mobprog.vaulthub;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.*;
import android.widget.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class MainDisplay extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_display );
        final Spinner spinner = findViewById( R.id.spinner2 );
        Button add = findViewById( R.id.addAcc ), remove = findViewById( R.id.delete ), update = findViewById( R.id.updateBtn );
        final Button showhide = findViewById( R.id.setPassVisibility );//init seperately since it is used by itself to change its own text
        final TextView user = findViewById( R.id.un ),use = findViewById( R.id.us ), pass = findViewById( R.id.pw );
        final vaulthub.database.userInfo db = new vaulthub.database.userInfo( getApplicationContext() );
        List<String> userr = db.getSpinnerItems();//gets all the items under the column username which will be used as headers in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item,userr );//creates an adapter that the spinner needs
        spinner.setAdapter( adapter );
        add.setOnClickListener(this);
        remove.setOnClickListener( this );
        update.setOnClickListener( this );
        //changes the visibility of the password and the text inside the button
        showhide.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //transforms the text to either a password or plain text
                if(showhide.getText().toString().equals( "Show" )&&pass.getTransformationMethod() == PasswordTransformationMethod.getInstance()){
                    pass.setTransformationMethod( HideReturnsTransformationMethod.getInstance() );
                    showhide.setText( R.string.hide );
                }else{
                    pass.setTransformationMethod( PasswordTransformationMethod.getInstance() );
                    showhide.setText( R.string.show );
                }
            }
        } );
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA();
                    vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                    //reads the from the local .key file created
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( rsa.publKey[1] ) );
                    ObjectInputStream ois1 = new ObjectInputStream( new FileInputStream( rsa.privaKey[1] ) );
                    PublicKey publicKey = (PublicKey) ois.readObject();
                    PrivateKey privateKey = (PrivateKey) ois1.readObject();
                    Cursor cursor = db.fetch( hex.getHexString( rsa.encrypt( spinner.getSelectedItem().toString(), privateKey ) ) );
                    cursor.moveToFirst();
                    user.setText(rsa.decrypt(hex.getString(cursor.getString(1)),publicKey));
                    pass.setText( rsa.decrypt( hex.getString( cursor.getString( 2 ) ), publicKey ) );
                    use.setText( rsa.decrypt( hex.getString( cursor.getString( 3 ) ), publicKey ) );
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
    public void onBackPressed() {
        //shows a dialog confirming if the user wants to logout
        new vaulthub.showDialog("Important", "Are you sure you want to Log out", 2, MainActivity.class, getApplicationContext()).show( getSupportFragmentManager(),"" );
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addAcc:
                startActivity( new Intent(getApplicationContext(), AddAccountActivity.class) );
                break;
            case R.id.delete:
                startActivity( new Intent(getApplicationContext(), DeleteAccountActivity.class) );
                break;
            case R.id.updateBtn:
                startActivity( new Intent(getApplicationContext(), UpdateAccountActivity.class) );
                break;
        }
    }
}