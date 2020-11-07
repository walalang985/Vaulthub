package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AboutActivity extends AppCompatActivity implements View.OnClickListener{
    private final Uri url = Uri.parse( "https://github.com/walalang985/Vaulthub" );
    private final String action = "android.intent.action.VIEW";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_about );
        Button git = findViewById( R.id.openGit );
        git.setOnClickListener( this );
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.openGit:
                startActivity( new Intent(action, url) );
                break;
        }
    }
}