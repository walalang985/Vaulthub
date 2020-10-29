package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.*;
import android.content.pm.PackageManager;
import android.os.*;
import android.view.View;
import android.widget.*;

public class MainActivity extends AppCompatActivity {
    final int Ver = Build.VERSION.SDK_INT;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        if(Ver > Build.VERSION_CODES.LOLLIPOP_MR1){
            if(!checkPermissions()){
                requestPermissions();
            }
        }
        final Button a = findViewById( R.id.btnLogin ), b = findViewById( R.id.btnRegister ),c = findViewById( R.id.test );
        a.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Login = new Intent( MainActivity.this,LoginFormActivity.class );
                startActivity( Login );
            }
        } );
        b.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity( Register );
            }
        } );
    }
    private void requestPermissions(){
        ActivityCompat.requestPermissions( this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
    }
    private boolean checkPermissions(){
        int storage = ContextCompat.checkSelfPermission( this, Manifest.permission_group.STORAGE );
        int fingerprint = ContextCompat.checkSelfPermission( this, Manifest.permission.USE_BIOMETRIC );
        if(storage == PackageManager.PERMISSION_GRANTED ){
            return true;
        }
        else{
            return false;
        }
    }
}