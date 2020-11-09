package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
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
        new DialogFragment("Invalid Action","The action you are trying to do is invalid").show( getSupportFragmentManager(), "dia" );
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.del:
                try{
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( RSA.privateKey1 ) );
                    PrivateKey privateKey = (PrivateKey) ois.readObject();
                    userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
                    db.deleteUserInfo( RSA.encrypt( selItem, privateKey ) );
                    new DialogFragment( "Action Complete", "Successfully deleted the account" , MainDisplay.class, getApplicationContext()).show( getSupportFragmentManager(), "dia" );
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.ret:
                new DialogFragment( "Action Not Performed", "Returning you now", MainDisplay.class, getApplicationContext() ).show( getSupportFragmentManager(), "dia" );
                break;
        }
    }
}