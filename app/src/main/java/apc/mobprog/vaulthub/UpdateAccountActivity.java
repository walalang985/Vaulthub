package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.List;

public class UpdateAccountActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_update_account );
        final EditText username = findViewById( R.id.username ), password = findViewById( R.id.password ), usage = findViewById( R.id.usage );
        final Spinner spinner = findViewById( R.id.userAccContainer );
        Button cancel = findViewById( R.id.cancel ), update = findViewById( R.id.update );
        cancel.setOnClickListener( cancelClicked );
        final userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
        List<String> userr = db.getSpinnerItems();
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item,userr );
        spinner.setAdapter( adapter );
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
                Cursor cursor = db.fetch(spinner.getSelectedItem().toString());
                cursor.moveToFirst();
                username.setText( "" );
                password.setText( "" );
                usage.setText( "" );
                username.append( cursor.getString( 1 ) );
                password.append( cursor.getString( 2 ) );
                usage.append( cursor.getString( 3 ) );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        } );
        update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateInfo( spinner.getSelectedItem().toString(), username.getText().toString(),password.getText().toString(),usage.getText().toString());//updates the information inside the database
                startActivity( new Intent(getApplicationContext(), MainDisplay.class) );
            }
        } );
    }
    //returns the user to the main display activity
    private View.OnClickListener cancelClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity( new Intent(getApplicationContext(), MainDisplay.class) );
        }
    };
    //disables the back press
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Action not supported", Toast.LENGTH_SHORT).show();
    }

}