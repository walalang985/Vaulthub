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
        if(!db.dbExists( getApplicationContext() )){
            //redirects the account if the database is not yet present in the file system
            new vaulthub.showDialog( "No Accounts","No accounts have been made yet.",1, RegisterActivity.class, getApplicationContext() ).show( getSupportFragmentManager(), "" );
        }
        cancel.setOnClickListener( this );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    new vaulthub.showDialog( "Empty fields","Oops, one of the fields is empty please fill it up first", 3, null, null ).show( getSupportFragmentManager(),"" );
                }
                try {

                    vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                    Cursor cursor = db.fetch( 1 );
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getLoginPublicKeydir ) );
                    PublicKey publicKey = (PublicKey) ois.readObject();
                    vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA(publicKey);
                    if(user.getText().toString().equals( rsa.decrypt( hex.getString( cursor.getString( 1 ) ) ) )&&pass.getText().toString().equals( rsa.decrypt( hex.getString( cursor.getString( 2 ) ) ) )){
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