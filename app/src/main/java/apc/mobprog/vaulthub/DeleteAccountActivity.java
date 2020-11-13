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
        vaulthub.database.userInfo db = new vaulthub.database.userInfo( getApplicationContext() );
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
        new vaulthub.showDialog("Invalid Action","The action you are trying to do is invalid", 1,null,null).show( getSupportFragmentManager(), "" );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.del:
                try{
                    vaulthub.crypt.RSA rsa = new vaulthub.crypt.RSA();
                    vaulthub.crypt.Hex hex = new vaulthub.crypt.Hex();
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( rsa.privaKey[1] ) );
                    PrivateKey privateKey = (PrivateKey) ois.readObject();
                    vaulthub.database.userInfo db = new vaulthub.database.userInfo( getApplicationContext() );
                    db.deleteUserInfo( hex.getHexString( rsa.encrypt( selItem,privateKey ) ) );
                    //deletes the account in the database
                    //db.deleteUserInfo( new Hex().getHexString( new RSA(privateKey).encrypt( selItem ) ) );
                    //shows a dialog and returns the user back to MainDisplay
                    new vaulthub.showDialog( "Action Complete", "Successfully deleted the account",1 , MainDisplay.class, getApplicationContext()).show( getSupportFragmentManager(), "" );
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case R.id.ret:
                new vaulthub.showDialog( "Action Not Performed", "Returning you now", 1, MainDisplay.class, getApplicationContext() ).show( getSupportFragmentManager(), "" );
                break;
        }
    }
}