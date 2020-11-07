package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.List;

public class DeleteAccountActivity extends AppCompatActivity implements View.OnClickListener{
    private String selItem = "";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_delete_account );
        setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
        userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
        final Spinner spinner = findViewById( R.id.spins );
        Button deleteBtn = findViewById( R.id.del ), returner = findViewById( R.id.ret );
        List<String> list = db.getSpinnerItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item, list );
        spinner.setAdapter( adapter );
        selItem = spinner.getSelectedItem().toString();
        deleteBtn.setOnClickListener( this );
        returner.setOnClickListener( this );
    }
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment dialogFragment = new DialogFragment();
        dialogFragment.show( fm, "dia" );
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.del:
                userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
                db.deleteUserInfo( selItem );
                if(db.getUserInfoCount() == 0){
                    //if there is no more account return to MainDisplay.class
                    startActivity( new Intent(getApplicationContext(),MainDisplay.class).putExtra( "status", "3" ) );
                }else{
                    //refresh the activity to update the items in the spinner
                    startActivity( new Intent(getApplicationContext(),DeleteAccountActivity.class) );
                }
                break;
            case R.id.ret:
                startActivity( new Intent(getApplicationContext(), MainDisplay.class).putExtra( "status", "3" ) );
                break;
        }
    }
}