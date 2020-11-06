package apc.mobprog.vaulthub;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;

public class userLoginCredentialsHandling extends SQLiteOpenHelper{
    private static final int DB_VER = 1;
    private static final String DB_NAME = "userLoginCredentialsHandling";
    private static final String TBL_NAME = "userLoginCredentials";
    private static final String ID = "id";
    private static final String USER = "username";
    private static final String PASS = "password";
    public userLoginCredentialsHandling(Context context){
        super(context, DB_NAME, null, DB_VER);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE " + TBL_NAME + "(" + ID + " INTEGER PRIMARY KEY, " + USER + " TEXT NOT NULL, " +  PASS + " TEXT NOT NULL);");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " + TBL_NAME );
        onCreate( db );
    }
    public void insertUserLogin(String user, String pass){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put( USER, user );
        cv.put( PASS, pass );
        db.insert( TBL_NAME, null, cv );
    }
    public int getUserLoginCount(){
        String query = "SELECT * FROM " + TBL_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery( query, null );
        return cursor.getCount();
    }
    public Cursor fetch(String input){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TBL_NAME + " WHERE " + USER + " = ?", new String[]{input} );
        if(cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public boolean dbExists(Context c){
        File file = c.getDatabasePath( DB_NAME );
        return file.exists();
    }
}