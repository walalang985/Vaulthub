package apc.mobprog.vaulthub;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.*;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    final int Ver = Build.VERSION.SDK_INT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        todo();
        Button a = findViewById( R.id.btnLogin ), b = findViewById( R.id.btnRegister );
        a.setOnClickListener( this );
        b.setOnClickListener( this );
    }
    private void todo() {
        if(!RSA.doKeysExists()) {//checks if the keys exist in the system
            try {
                RSA.writeLoginKeys();
                RSA.writeUserInfoKeys();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(Ver > Build.VERSION_CODES.LOLLIPOP_MR1){
            if(!checkPermissions()){
                requestPermissions();
            }
            else{
                //asks the user to grant permissions first
                Toast.makeText( getApplicationContext(), "Please grant the permissions first", Toast.LENGTH_SHORT ).show();
                //kills the program while the permissions asked is not yet granted
                System.exit( 0 );
            }
        }
    }

    private void requestPermissions(){
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }
    private boolean checkPermissions(){
        int storage = ContextCompat.checkSelfPermission( this, Manifest.permission_group.STORAGE );
        if(storage == PackageManager.PERMISSION_GRANTED ){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRegister:
                startActivity( new Intent( getApplicationContext(),RegisterActivity.class ) );
                break;
            case R.id.btnLogin:
                startActivity( new Intent(getApplicationContext(), LoginFormActivity.class) );
                break;
        }
    }
}