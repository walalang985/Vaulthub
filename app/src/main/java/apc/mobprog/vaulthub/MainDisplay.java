package apc.mobprog.vaulthub;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.*;
import android.widget.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainDisplay extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main_display );
        final Spinner spinner = findViewById( R.id.spinner2 );
        Button add = findViewById( R.id.addAcc ), remove = findViewById( R.id.delete ), update = findViewById( R.id.updateBtn );
        final Button showhide = findViewById( R.id.setPassVisibility );//init seperately since it is used by itself to change its own text
        final TextView user = findViewById( R.id.un ),use = findViewById( R.id.us ), pass = findViewById( R.id.pw );
        userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );//instantiate userInfoStoreHandling to interact with the created database
        List<String> userr = db.getSpinnerItems();//gets all the items under the column username which will be used as headers in the spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>( this, R.layout.support_simple_spinner_dropdown_item,userr );//creates an adapter that the spinner needs
        spinner.setAdapter( adapter );
        add.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), AddAccountActivity.class) );
            }
        } );
        remove.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), DeleteAccountActivity.class) );
            }
        } );
        update.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(getApplicationContext(), UpdateAccountActivity.class) );
            }
        } );
        //changes the visibility of the password and the text inside the button
        showhide.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(showhide.getText().toString().equals( "Show" )&&pass.getTransformationMethod() == PasswordTransformationMethod.getInstance()){
                    pass.setTransformationMethod( HideReturnsTransformationMethod.getInstance() );
                    showhide.setText( "Hide" );
                }else{
                    pass.setTransformationMethod( PasswordTransformationMethod.getInstance() );
                    showhide.setText( "Show" );
                }
            }
        } );
        spinner.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userInfoStoreHandling db = new userInfoStoreHandling( getApplicationContext() );
                Cursor cursor = db.fetch(spinner.getSelectedItem().toString());
                cursor.moveToFirst();
                user.setText( cursor.getString( 1 ) );
                pass.setText( cursor.getString( 2 ) );
                use.setText( cursor.getString( 3 ) );
            }
            //one of the implemented methods under AdatperView
            //also required
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                user.setText( "" );
                pass.setText( "" );
                use.setText( "" );
            }
        } );

    }
    @Override
    public void onBackPressed() {
        Toast.makeText(getApplicationContext(), "Action not supported", Toast.LENGTH_SHORT).show();
    }



}