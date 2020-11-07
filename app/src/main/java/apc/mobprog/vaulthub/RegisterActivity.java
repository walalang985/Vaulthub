package apc.mobprog.vaulthub;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.*;
import android.view.View;
import android.content.*;
import android.widget.*;
public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    private String username = "", password = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        Button cancel = findViewById( R.id.cancelBtn ), register = findViewById( R.id.registerBtn );
        final EditText user = findViewById( R.id.txtUsername ),pass = findViewById( R.id.numPassword );
        final TextView userLabel = findViewById( R.id.user ), passLabel = findViewById( R.id.pass );
        userLabel.setVisibility( View.VISIBLE );
        passLabel.setVisibility( View.VISIBLE );
        username = user.getText().toString();
        password = pass.getText().toString();
        cancel.setOnClickListener(this);
        register.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelBtn:
                startActivity( new Intent(getApplicationContext(), MainActivity.class) );
                break;
            case R.id.registerBtn:
                userLoginCredentialsHandling db = new userLoginCredentialsHandling( getApplicationContext() );
                if(db.getUserLoginCount() == 0) {
                    db.insertUserLogin( aes128.encrypt( username ), aes128.encrypt( password ) );
                    startActivity( new Intent( getApplicationContext(), LoginFormActivity.class ) );
                }else{
                    Toast.makeText( getApplicationContext(), "Sorry one account per device", Toast.LENGTH_SHORT ).show();
                }
                break;
        }
    }
}