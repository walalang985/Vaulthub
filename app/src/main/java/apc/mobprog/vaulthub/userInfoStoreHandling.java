package apc.mobprog.vaulthub;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
//This is for storing the
public class userInfoStoreHandling extends SQLiteOpenHelper{
    private static final int DB_VER = 1;
    private static final String DB_NAME = "userInfoManager";
    private static final String TBL_NAME = "userInfo";
    private static final String ID = "id";
    private static final String USER = "username";
    private static final String PASS = "password";
    private static final String USAGE = "usage";

    public userInfoStoreHandling(Context c){
        super(c, DB_NAME, null,DB_VER);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( "CREATE TABLE " + TBL_NAME + "(" + ID + " INTEGER PRIMARY KEY, " + USER + " TEXT NOT NULL, " +  PASS + " TEXT NOT NULL, " + USAGE + " TEXT NOT NULL); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL( "DROP TABLE IF EXISTS " +TBL_NAME );
        onCreate( db );
    }
    public void insertUserInfo(String user, String pass, String usage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put( USER, user );
        cv.put( PASS, pass );
        cv.put( USAGE,usage );
        db.insert( TBL_NAME, null, cv );
        db.close();
    }
    public List<String> getSpinnerItems(){
        List<String> list = new ArrayList<String>();
        String selectQuery = "SELECT  * FROM " + TBL_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if(cursor.moveToFirst()){
            do{
                try {
                    ObjectInputStream ois = new ObjectInputStream( new FileInputStream( RSA.publicKey1 ) );
                    PublicKey publicKey = (PublicKey) ois.readObject();
                    list.add( RSA.decrypt( cursor.getString( 1 ), publicKey ) );
                }catch (Exception e){
                    e.printStackTrace();
                }
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }
    public int getUserInfoCount(){
        String query = "SELECT * FROM " + TBL_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery( query, null );
        return c.getCount();
    }
    public int deleteUserInfo(String prev){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete( TBL_NAME, USER + " = ?", new String[]{prev} );
    }
    public Cursor fetch(String prev){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery( "SELECT * FROM " + TBL_NAME + " WHERE " + USER + " = ?", new String[]{prev} );
        if (cursor != null){
            cursor.moveToFirst();
        }
        return cursor;
    }
    public int updateInfo(String prev, String user, String pass, String usage){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put( USER, user );
        cv.put( PASS, pass );
        cv.put( USAGE, usage );
        return db.update( TBL_NAME, cv, USER + " = ?", new String[]{prev} );
    }
}
