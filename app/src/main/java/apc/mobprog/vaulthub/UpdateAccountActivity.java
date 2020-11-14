package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class UpdateAccountActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_update_account );
        final EditText username = findViewById( R.id.username ), password = findViewById( R.id.password ), usage = findViewById( R.id.usage );
        final Spinner spinner = findViewById( R.id.userAccContainer );
        Button cancel = findViewById( R.id.cancel ), update = findViewById( R.id.update );
        cancel.setOnClickListener( cancelClicked );
        final vaulthub.database.userInfo db = new vaulthub.database.userInfo( getApplicationContext() );
        List<String> userr = db.getSpinnerItems();//loads the items from the database
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item,userr );
        spinner.setAdapter( adapter );
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try{
                    //clears the edittext before putting the data from the database
                    username.setText( "" );
                    password.setText( "" );
                    usage.setText( "" );
                    vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                    //reads the private and public rsa keys
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getUserPublicKey ) );
                    ObjectInputStream ois1 = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getUserPrivateKey ) );
                    PublicKey publicKey = (PublicKey) ois.readObject();
                    PrivateKey privateKey = (PrivateKey) ois1.readObject();
                    vaulthub.crypt.RSA RSAencrypt = new vaulthub.crypt.RSA(privateKey);
                    vaulthub.crypt.RSA RSAdecrypt = new vaulthub.crypt.RSA(publicKey);
                    Cursor cursor = db.fetch( hex.getHexString( RSAencrypt.encrypt( spinner.getSelectedItem().toString() ) ) );
                    cursor.moveToFirst();
                    //qppends to the edittext
                    username.append( RSAdecrypt.decrypt( hex.getString( cursor.getString( 1 ) ) ) );
                    password.append( RSAdecrypt.decrypt( hex.getString( cursor.getString( 2 ) ) ) );
                    usage.append( RSAdecrypt.decrypt( hex.getString( cursor.getString( 3 ) ) ) );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        } );
        update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    vaulthub.showDialog dialog = new vaulthub.showDialog( "Action Complete", "Successfully updated the account",1 ,MainDisplay.class ,getApplicationContext() );
                    vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getUserPrivateKey ) );
                    PrivateKey privateKey = (PrivateKey) ois.readObject();
                    vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA(privateKey);
                    db.updateInfo(
                            hex.getHexString( rsa.encrypt( spinner.getSelectedItem().toString() ) ),
                            hex.getHexString( rsa.encrypt( username.getText().toString() ) ),
                            hex.getHexString( rsa.encrypt( password.getText().toString() ) ),
                            hex.getHexString( rsa.encrypt( usage.getText().toString() ) ) );
                    dialog.show( getSupportFragmentManager(),"" );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } );
    }
    //returns the user to the main display activity
    private View.OnClickListener cancelClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity( new Intent(getApplicationContext(), MainDisplay.class) );
        }
    };
    //disables the back press
    @Override
    public void onBackPressed() {
        new vaulthub.showDialog("Invalid Action","The action you are trying to do is invalid",1, null, null).show( getSupportFragmentManager(), "" );//show a dialog about invalid action of some sort
    }
}