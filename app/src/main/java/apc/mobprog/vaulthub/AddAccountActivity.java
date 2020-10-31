package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
public class AddAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_add_account );
        final userInfoStoreHandling ww = new userInfoStoreHandling(getApplicationContext());
        final EditText user = findViewById( R.id.username ), pass = findViewById( R.id.password ), use = findViewById( R.id.usage );
        final Intent q = new Intent(getApplicationContext(), MainDisplay.class);
        Button acc = findViewById( R.id.update ), cancel = findViewById( R.id.cancel );
        user.setFocusedByDefault( true );
        acc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
                db.insertUserInfo( user.getText().toString(), pass.getText().toString(),use.getText().toString() );
                q.putExtra( "status", "0" );
                startActivity( q );
            }
        } );
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                q.putExtra( "status", "0");
                startActivity( q );
            }
        } );
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Action not supported", Toast.LENGTH_SHORT).show();
    }
}