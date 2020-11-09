package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.*;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.List;

public class UpdateAccountActivity extends AppCompatActivity{
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
                try{
                    //clears the edittext before putting the data from the database
                    username.setText( "" );
                    password.setText( "" );
                    usage.setText( "" );
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( RSA.publicKey1 ) );
                    ObjectInputStream ois1 = new ObjectInputStream( new FileInputStream( RSA.privateKey1 ) );
                    PublicKey publicKey = (PublicKey) ois.readObject();
                    PrivateKey privateKey = (PrivateKey) ois1.readObject();
                    Cursor cursor = db.fetch(RSA.encrypt( spinner.getSelectedItem().toString(), privateKey ));
                    cursor.moveToFirst();
                    //automatically writes to the edittext while being editable
                    username.append( RSA.decrypt( cursor.getString( 1 ), publicKey ) );
                    password.append( RSA.decrypt( cursor.getString( 2 ), publicKey ) );
                    usage.append( RSA.decrypt( cursor.getString( 3 ), publicKey ) );
                }catch (Exception e){
                    e.printStackTrace();
                }



            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        } );
        update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( RSA.privateKey1 ) );
                    PrivateKey privateKey = (PrivateKey) ois.readObject();
                    db.updateInfo( RSA.encrypt( spinner.getSelectedItem().toString(), privateKey ),
                                   RSA.encrypt( username.getText().toString(), privateKey ),
                                   RSA.encrypt( password.getText().toString(), privateKey ),
                                   RSA.encrypt( usage.getText().toString(), privateKey ));
                    startActivity( new Intent(getApplicationContext(), MainDisplay.class).putExtra( "status","2" ) );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        } );
    }
    //returns the user to the main display activity
    private View.OnClickListener cancelClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity( new Intent(getApplicationContext(), MainDisplay.class).putExtra( "status", "4" ) );
        }
    };
    //disables the back press
    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        DialogFragment dialogFragment = new DialogFragment("Invalid Action","The action you are trying to do is invalid");
        dialogFragment.show( fm, "dia" );//show a dialog about invalid action of some sort
    }
}