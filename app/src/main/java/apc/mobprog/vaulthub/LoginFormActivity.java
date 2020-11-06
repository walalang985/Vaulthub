package apc.mobprog.vaulthub;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.*;

import org.w3c.dom.Text;

import java.util.List;
public class LoginFormActivity extends AppCompatActivity implements View.OnClickListener {
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
        cancel.setOnClickListener( this );
        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()){
                    Toast.makeText( getApplicationContext(), "One field is empty", Toast.LENGTH_SHORT ).show();
                }
                Cursor cursor = db.fetch( aes128.encrypt( user.getText().toString() ) );
                if(cursor.getString( 1 ).equals(aes128.encrypt( user.getText().toString() ))  && cursor.getString( 2 ).equals( aes128.encrypt( pass.getText().toString() ) )){
                    startActivity( new Intent(getApplicationContext(), MainDisplay.class).putExtra( "status","1" ) );
                }else{
                    Toast.makeText( getApplicationContext(), "Authentication Failed", Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    @Override
    public void onClick(View v) {
        userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
        switch (v.getId()){
            case R.id.cancelBtn:
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
    }
}