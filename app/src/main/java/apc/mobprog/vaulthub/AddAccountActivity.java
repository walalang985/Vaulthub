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
                userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
                try{
                    RSA rsa = new RSA();
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( rsa.getPrivateUserKeys() ) );
                    PrivateKey privateKey = (PrivateKey) ois.readObject();
                    db.insertUserInfo( new RSA(privateKey).encrypt( user.getText().toString() ), new RSA(privateKey).encrypt( pass.getText().toString() ),new RSA(privateKey).encrypt( use.getText().toString() ) );
                    new showDialog(  "Action Completed", "The account was added successfully", 1, MainDisplay.class, getApplicationContext() ).show( getSupportFragmentManager(), "" );
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
        new showDialog("Invalid Action","The action you are trying to do is invalid", 1, null,null).show( getSupportFragmentManager(), "" );
    }
}