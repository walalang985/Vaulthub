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
        final vaulthub.database.userLogin db = new vaulthub.database.userLogin( getApplicationContext() );
        Button cancel = findViewById( R.id.cancelBtn ), login = findViewById( R.id.loginBtn );
        final EditText user = findViewById( R.id.txtUsername ), pass = findViewById( R.id.txtPassword );
        final TextView userLabel = findViewById( R.id.user ), passLabel = findViewById( R.id.pass );
        if(!db.dbExists( getApplicationContext() )){
            //redirects the account if the database is not yet present in the file system
            new vaulthub.showDialog( "No Accounts","No accounts have been made yet.",1, RegisterActivity.class, getApplicationContext() ).show( getSupportFragmentManager(), "" );
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
                    new vaulthub.showDialog( "Empty fields","Oops, one of the fields is empty please fill it up first", 3, null, null ).show( getSupportFragmentManager(),"" );
                }
                try {
                    vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA();
                    vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                    Cursor cursor = db.fetch( 1 );
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( rsa.publKey[0] ) );
                    PublicKey publicKey = (PublicKey) ois.readObject();
                    if(user.getText().toString().equals( rsa.decrypt( hex.getString( cursor.getString( 1 ) ),publicKey ) )&&pass.getText().toString().equals( rsa.decrypt( hex.getString( cursor.getString( 2 ) ),publicKey ) )){
                        new vaulthub.showDialog( "Success", "Login success", 1, MainDisplay.class, getApplicationContext() ).show( getSupportFragmentManager(),"" );
                    }
                    else{
                        new vaulthub.showDialog( "Login Failed", "Oops, the username or password does not match any data from our database",3, null, null ).show( getSupportFragmentManager(),"" );
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } );
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancelBtn:
                startActivity( new Intent( getApplicationContext(), MainActivity.class ) );
                break;
        }
    }
}