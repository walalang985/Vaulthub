package apc.mobprog.vaulthub;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class LoginFormActivity extends AppCompatActivity implements View.OnClickListener {
    private String username = "", password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_form );
        final userLoginCredentialsHandling db = new userLoginCredentialsHandling( getApplicationContext() );
        Button cancel = findViewById( R.id.cancelBtn ), login = findViewById( R.id.loginBtn );
        final EditText user = findViewById( R.id.txtUsername ), pass = findViewById( R.id.numPassword );
        final TextView userLabel = findViewById( R.id.user ), passLabel = findViewById( R.id.pass );
        if(!db.dbExists( getApplicationContext() )){
            Toast.makeText( getApplicationContext(), "No accounts have been made yet", Toast.LENGTH_SHORT ).show();
            startActivity( new Intent(getApplicationContext(), RegisterActivity.class) );
        }
        user.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (user.getText().toString().equals( "" )) {
                    userLabel.setVisibility( View.VISIBLE );
                } else {
                    userLabel.setVisibility( View.INVISIBLE );
                }
            }
        } );
        pass.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                if (pass.getText().toString().equals( "" )) {
                    passLabel.setVisibility( View.VISIBLE );
                } else {
                    passLabel.setVisibility( View.INVISIBLE );
                }
            }
        } );
        cancel.setOnClickListener( this );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    Toast.makeText( getApplicationContext(), "One field is empty please try again", Toast.LENGTH_SHORT ).show();
                    startActivity( new Intent(getApplicationContext(), LoginFormActivity.class) );
                }
                try {
                    Cursor cursor = db.fetch( 1 );
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( RSA.publicKey ) );
                    PublicKey publicKey = (PublicKey) ois.readObject();
                    if (user.getText().toString().equals( RSA.decrypt( cursor.getString( 1 ), publicKey ) ) && pass.getText().toString().equals( RSA.decrypt( cursor.getString( 2 ), publicKey ) )) {
                        startActivity( new Intent( getApplicationContext(), MainDisplay.class ).putExtra( "status", "1" ) );
                    } else {
                        Toast.makeText( getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT ).show();
                    }
                    //PrivateKey privateKey = (PrivateKey) keys.get( "private" );
                    //username = RSA.encrypt( cursor.getString( 1 ), privateKey );
                    //password = RSA.encrypt( cursor.getString( 2 ), privateKey );
                }catch (Exception e){
                    e.printStackTrace();
                }


            }
        } );
    }
    @Override
    public void onClick(View v) {
        userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
        switch (v.getId()) {
            case R.id.cancelBtn:
                startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
                break;
        }
    }
}