package apc.mobprog.vaulthub;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.*;
import android.util.Log;
import android.view.View;
import android.content.*;
import android.widget.*;
public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        final userLoginCredentialsHandling db = new userLoginCredentialsHandling( this );
        Button cancel = findViewById( R.id.cancelBtn ), register = findViewById( R.id.registerBtn );
        final EditText user = findViewById( R.id.txtUsername ),pass = findViewById( R.id.numPassword );
        final TextView userLabel = findViewById( R.id.user ), passLabel = findViewById( R.id.pass );
        userLabel.setVisibility( View.VISIBLE );
        passLabel.setVisibility( View.VISIBLE );
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(getApplicationContext(), MainActivity.class);
                startActivity( x );
            }
        } );
        register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(db.getUserLoginCount() == 0) {
                    db.insertUserLogin( aes128.encrypt( user.getText().toString() ), aes128.encrypt( pass.getText().toString() ) );
                    startActivity( new Intent( getApplicationContext(), LoginFormActivity.class ) );
                }else{
                    Toast.makeText( getApplicationContext(), "Sorry one account per device", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
        user.addTextChangedListener( new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
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
    }
}