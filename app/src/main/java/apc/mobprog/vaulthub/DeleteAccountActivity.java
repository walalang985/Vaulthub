package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.List;

public class DeleteAccountActivity extends AppCompatActivity {
    final boolean allowBackPress = false;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_delete_account );
        final userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        final Spinner spinner = findViewById( R.id.spins );
        Button deleteBtn = findViewById( R.id.del ), returner = findViewById( R.id.ret );
        List<String> list = db.getSpinnerItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item, list );
        spinner.setAdapter( adapter );
        deleteBtn.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.deleteUserInfo(spinner.getSelectedItem().toString());
                if(db.getUserInfoCount() == 0){
                    startActivity( new Intent(getApplicationContext(),MainDisplay.class) );
                }else{
                    //refresh the activity to update the items in the spinner
                    startActivity( new Intent(getApplicationContext(),DeleteAccountActivity.class) );
                }
            }
        } );
        returner.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), MainDisplay.class).putExtra( "status", "3" ) );
            }
        } );
    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Action not supported", Toast.LENGTH_SHORT).show();
    }

}