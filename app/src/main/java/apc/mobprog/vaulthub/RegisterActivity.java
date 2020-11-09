package apc.mobprog.vaulthub;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.*;
import android.util.Log;
import android.view.View;
import android.content.*;
import android.widget.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        Button cancel = findViewById( R.id.cancelBtn ), register = findViewById( R.id.registerBtn );
        final EditText user = findViewById( R.id.txtUsername ),pass = findViewById( R.id.numPassword );
        final TextView userLabel = findViewById( R.id.user ), passLabel = findViewById( R.id.pass );
        final userLoginCredentialsHandling db = new userLoginCredentialsHandling( getApplicationContext() );

        userLabel.setVisibility( View.VISIBLE );
        passLabel.setVisibility( View.VISIBLE );
        cancel.setOnClickListener(this);
        register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                    new DialogFragment( "Empty fields","Oops, one of the fields is empty please fill it up first" ).show( getSupportFragmentManager(),"dia" );
                }
                try {
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( RSA.privateKey ) );
                    PrivateKey privateKey = (PrivateKey) ois.readObject();
                    if(db.getUserLoginCount() == 0) {
                        db.insertUserLogin( RSA.encrypt( user.getText().toString(), privateKey ),RSA.encrypt( pass.getText().toString(), privateKey ) );
                        startActivity( new Intent( getApplicationContext(), LoginFormActivity.class ) );
                    }else{
                        new DialogFragment( "Account Creation Failed", "Sorry only one account per device" ).show( getSupportFragmentManager(), "dia" );
                    }
                } catch (Exception e) {
                   e.printStackTrace();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancelBtn:
                startActivity( new Intent(getApplicationContext(), MainActivity.class) );
                break;
        }
    }
}