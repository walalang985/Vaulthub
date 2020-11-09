package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;
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
                try {
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( RSA.privateKey1 ) );
                    PrivateKey privateKey = (PrivateKey) ois.readObject();
                    db.insertUserInfo( RSA.encrypt( user.getText().toString(), privateKey ), RSA.encrypt( pass.getText().toString(), privateKey ), RSA.encrypt( use.getText().toString(), privateKey) );
                    startActivity( new Intent(getApplicationContext(), MainDisplay.class).putExtra( "status", "0" ) );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } );
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), MainDisplay.class).putExtra( "status", "4" ) );
            }
        } );
    }
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment dialogFragment = new DialogFragment("Invalid Action","The action you are trying to do is invalid");
        dialogFragment.show( fm, "dia" );
    }
}