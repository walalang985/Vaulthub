package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public class UpdateLoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_update_login );
        final vaulthub.database.userLogin db = new vaulthub.database.userLogin( getApplicationContext() );
        Cursor cursor = db.fetch( 1 );
        final EditText user = findViewById( R.id.UN ), pass = findViewById( R.id.PW );
        try{
            ObjectInputStream ois = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getLoginPublicKeydir ) );
            PublicKey publicKey = (PublicKey) ois.readObject();
            vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA( publicKey );
            vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
            user.append( rsa.decrypt( hex.getString( cursor.getString( 1 ) ) ) );
            pass.append( rsa.decrypt( hex.getString( cursor.getString( 2 ) ) ) );
        }catch (Exception e) {
            e.printStackTrace();
        }
        Button update = findViewById( R.id.updateBTN ), cancel = findViewById( R.id.CANCEL );
        update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getLoginPrivateKey ) );
                    PrivateKey privateKey = (PrivateKey) ois.readObject();
                    vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA( privateKey );
                    vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                    db.update( hex.getHexString( rsa.encrypt( user.getText().toString() ) ), hex.getHexString( rsa.encrypt( pass.getText().toString() ) ) );
                    new vaulthub.showDialog( "Success", "Account updated successfully", 1, MainDisplay.class, getApplicationContext() );
                }catch (Exception e){
                    e.printStackTrace();//to check at which part of the program it broke
                }
            }
        } );
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), MainDisplay.class) );
            }
        } );
    }

    @Override
    public void onBackPressed() {
        new vaulthub.showDialog("Invalid Action","The action you are trying to do is invalid",1, null, null).show( getSupportFragmentManager(), "" );//show a dialog about invalid action of some sort
    }
}