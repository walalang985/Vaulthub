package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;

public class AddAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_account );
        final EditText user = findViewById( R.id.username ), pass = findViewById( R.id.password ), use = findViewById( R.id.usage );
        Button acc = findViewById( R.id.update ), cancel = findViewById( R.id.cancel );
        user.setFocusedByDefault( true );
        acc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vaulthub.database.userInfo db = new vaulthub.database.userInfo( getApplicationContext() );
                try{
                    vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getUserPrivateKey ) );
                    PrivateKey privateKey = (PrivateKey) ois.readObject();
                    vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA(privateKey);
                    db.insertUserInfo( hex.getHexString( rsa.encrypt( user.getText().toString() ) ), hex.getHexString( rsa.encrypt( pass.getText().toString() ) ), hex.getHexString( rsa.encrypt( use.getText().toString() ) ) );
                    new vaulthub.showDialog(  "Action Completed", "The account was added successfully", 1, MainDisplay.class, getApplicationContext() ).show( getSupportFragmentManager(), "" );
                }
                catch (Exception e){
                    e.printStackTrace();
                }
            }
        } );
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), MainDisplay.class));
            }
        } );
    }
    @Override
    public void onBackPressed() {
        new vaulthub.showDialog("Invalid Action","The action you are trying to do is invalid", 1, null,null).show( getSupportFragmentManager(), "" );
    }
}