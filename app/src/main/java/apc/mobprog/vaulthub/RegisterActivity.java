package apc.mobprog.vaulthub;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.*;
import android.view.View;
import android.content.*;
import android.widget.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );
        Button cancel = findViewById( R.id.cancelBtn ), register = findViewById( R.id.registerBtn );
        final EditText user = findViewById( R.id.txtUsername ),pass = findViewById( R.id.txtPassword );
        final vaulthub.database.userLogin db = new vaulthub.database.userLogin(getApplicationContext());
        cancel.setOnClickListener(this);
        register.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    try {
                        if (user.getText().toString().isEmpty() || pass.getText().toString().isEmpty()) {
                            new vaulthub.showDialog( "Empty fields","Oops, one of the fields is empty please fill it up first",1, null, null ).show( getSupportFragmentManager(),"" );
                    }else {
                        vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                        ObjectInputStream ois = new ObjectInputStream( new FileInputStream( vaulthub.getDirs.getLoginPrivateKey ) );
                        PrivateKey privateKey = (PrivateKey) ois.readObject();
                        vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA(privateKey);
                        if (db.getUserLoginCount() == 0) {
                            db.insertUserLogin( hex.getHexString( rsa.encrypt( user.getText().toString( ) ) ), hex.getHexString( rsa.encrypt( pass.getText().toString() ) ));
                            startActivity( new Intent( getApplicationContext(), LoginFormActivity.class ) );
                        } else {
                            new vaulthub.showDialog( "Account Creation Failed", "Sorry only one account per device", 1, LoginFormActivity.class, getApplicationContext() ).show( getSupportFragmentManager(), "" );
                        }
                    }
                } catch (Exception e) {
                   e.printStackTrace();
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