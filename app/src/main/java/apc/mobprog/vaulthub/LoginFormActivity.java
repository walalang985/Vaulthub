package apc.mobprog.vaulthub;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PublicKey;
public class LoginFormActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_form );
        final userLoginCredentialsHandling db = new userLoginCredentialsHandling( getApplicationContext() );
        Button cancel = findViewById( R.id.cancelBtn ), login = findViewById( R.id.loginBtn );
        final EditText user = findViewById( R.id.txtUsername ), pass = findViewById( R.id.numPassword );
        final TextView userLabel = findViewById( R.id.user ), passLabel = findViewById( R.id.pass );
        if(!db.dbExists( getApplicationContext() )){
            new DialogFragment( "No Accounts","No accounts have been made yet.", RegisterActivity.class, getApplicationContext() ).show( getSupportFragmentManager(), "dia" );
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
                    new DialogFragment( "Empty fields","Oops, one of the fields is empty please fill it up first" ).show( getSupportFragmentManager(),"dia" );
                }
                try {
                    Cursor cursor = db.fetch( 1 );
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( RSA.publicKey ) );
                    PublicKey publicKey = (PublicKey) ois.readObject();
                    if (user.getText().toString().equals( RSA.decrypt( cursor.getString( 1 ), publicKey ) ) && pass.getText().toString().equals( RSA.decrypt( cursor.getString( 2 ), publicKey ) )) {
                        startActivity( new Intent( getApplicationContext(), MainDisplay.class ).putExtra( "status", "1" ) );
                    } else {
                        new DialogFragment( "Login Failed", "Oops, the username or password does not match any data from our database" ).show( getSupportFragmentManager(), "dia" );
                    }
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