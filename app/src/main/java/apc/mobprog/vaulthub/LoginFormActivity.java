package apc.mobprog.vaulthub;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;
import java.util.List;
public class LoginFormActivity extends AppCompatActivity {
    private final userLoginCredentialsHandling a = new userLoginCredentialsHandling( this );
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_form );
        final userLoginCredentialsHandling db = new userLoginCredentialsHandling( getApplicationContext() );
        Button cancel = findViewById( R.id.cancelBtn ), login = findViewById( R.id.loginBtn );
        final EditText user = findViewById( R.id.txtUsername ), pass = findViewById( R.id.numPassword );
        final TextView userLabel = findViewById( R.id.user ), passLabel = findViewById( R.id.pass );
        user.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(user.getText().toString().equals( "" )){
                    userLabel.setVisibility( View.VISIBLE );
                }
                else{
                    userLabel.setVisibility( View.INVISIBLE );
                }
            }
        } );
        pass.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                if(pass.getText().toString().equals( "" )){
                    passLabel.setVisibility( View.VISIBLE );
                }
                else{
                    passLabel.setVisibility( View.INVISIBLE );
                }
            }
        } );
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), MainActivity.class);
                startActivity( x );
            }
        } );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getUserLoginCount() == 0){
                    Cursor cursor = db.fetch( aes128.encrypt( user.getText().toString() ) );
                    if(cursor.getString( 1 ) == aes128.encrypt( user.getText().toString() ) && cursor.getString( 2 ) == aes128.encrypt( pass.getText().toString() )){
                        startActivity( new Intent(getApplicationContext(), MainDisplay.class) );
                    }else{
                        Toast.makeText( getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT ).show();
                    }
                }
                else {
                    Toast.makeText( getApplicationContext(), "Sorry only one account per device", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Action not supported", Toast.LENGTH_SHORT).show();
    }
}