package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

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

        Button acc = findViewById( R.id.update ), cancel = findViewById( R.id.cancel );
        user.setFocusedByDefault( true );
        acc.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
                db.insertUserInfo( user.getText().toString(), pass.getText().toString(),use.getText().toString() );
                startActivity( new Intent(getApplicationContext(), MainDisplay.class).putExtra( "status", "0" ) );
            }
        } );
        cancel.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), MainDisplay.class).putExtra( "status", "4" ) );
            }
        } );
    }
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show( fm, "dia" );
    }
}