package apc.mobprog.vaulthub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.Cipher;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class vaulthub {
    public vaulthub(){}
    public static class database{
        static class userInfo extends SQLiteOpenHelper {
            private static final int DB_VER = 1;
            private static final String DB_NAME = "userInfoManager";
            private static final String TBL_NAME = "userInfo";
            private static final String ID = "id";
            private static final String USER = "username";
            private static final String PASS = "password";
            private static final String USAGE = "usage";
            public userInfo(Context context){
                super(context, DB_NAME, null,DB_VER);
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
            public int getUserInfoCount(){
                String query = "SELECT * FROM " + TBL_NAME;
                SQLiteDatabase db = this.getReadableDatabase();
                Cursor cursor = db.rawQuery( query, null );
                return cursor.getCount();
            }
            public List<String> getSpinnerItems(){
                List<String> list = new ArrayList<String>();
                String selectQuery = "SELECT  * FROM " + TBL_NAME;
                SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery(selectQuery, null);
                if(cursor.moveToFirst()){
                    do{
                        try {
                            crypt.Hex hex = new crypt.Hex();
                            ObjectInputStream ois = new ObjectInputStream( new FileInputStream( getDirs.getUserPublicKey ) );
                            PublicKey publicKey = (PublicKey) ois.readObject();
                            crypt.RSA rsa = new crypt.RSA(publicKey);
                            list.add( rsa.decrypt( hex.getString( cursor.getString( 1 ) ) ) );
                            /*
                            RSA rsa = new RSA();
                            ObjectInputStream ois = new ObjectInputStream( new FileInputStream( rsa.getPublicUserKeys() ) );
                            PublicKey publicKey = (PublicKey) ois.readObject();
                            list.add( new RSA(publicKey).decrypt( new Hex().getString( cursor.getString( 1 ) ) ) );
                            */
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }while (cursor.moveToNext());
                }
                cursor.close();
                db.close();
                return list;
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
        static class userLogin extends SQLiteOpenHelper{
            private static final int DB_VER = 1;
            private static final String DB_NAME = "userLoginCredentialsHandling";
            private static final String TBL_NAME = "userLoginCredentials";
            private static final String ID = "id";
            private static final String USER = "username";
            private static final String PASS = "password";
            public userLogin(Context context){
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
                SQLiteDatabase db = getWritableDatabase();
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
            public Cursor fetch(int id){
                SQLiteDatabase db = this.getWritableDatabase();
                Cursor cursor = db.rawQuery( "SELECT * FROM " + TBL_NAME + " WHERE " + ID + " = ?", new String[]{Integer.toString( id )} );
                if(cursor != null){
                    cursor.moveToFirst();
                }
                return cursor;
            }
            public int update( String user, String pass){
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues cv = new ContentValues();
                cv.put( USER, user );
                cv.put( PASS, pass );
                return db.update( TBL_NAME, cv, ID + " = ?", new String[]{"1"} );
            }
            public void deleteDB(){
                SQLiteDatabase db = this.getWritableDatabase();
                db.execSQL( "DROP TABLE IF EXISTS " + TBL_NAME );
            }
            public boolean dbExists(Context context){
                File file = context.getDatabasePath( DB_NAME );
                return file.exists();
            }
        }
    }
    public static class crypt{

        static class RSA{
            PublicKey pubKey;
            PrivateKey privKey;
            public RSA(PublicKey publicKey){
                this.pubKey = publicKey;
            }
            public RSA(PrivateKey privateKey){
                this.privKey = privateKey;
            }
            public RSA(){}

            public String encrypt(@NonNull String text){
                String res = "";
                try{
                    Cipher cipher = Cipher.getInstance( "RSA" );
                    cipher.init( Cipher.ENCRYPT_MODE, privKey );
                    res = Base64.getEncoder().encodeToString( cipher.doFinal(text.getBytes()) );
                }catch (Exception e){
                    e.printStackTrace();
                }
                return res;
            }
            public String decrypt(@NonNull String text){
                String res = "";
                try{
                    Cipher cipher = Cipher.getInstance( "RSA" );
                    cipher.init( Cipher.DECRYPT_MODE, pubKey );
                    res = new String(cipher.doFinal(Base64.getDecoder().decode( text )));
                }catch (Exception e){
                    e.printStackTrace();
                }
                return res;
            }

        }
        static class Hex{
            public Hex(){}
            public String getHexString(@NonNull String text){
                char[] a = text.toCharArray();
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i<a.length;i++) {
                    sb.append(Integer.toHexString(a[i]));
                }
                return sb.toString();
            }
            public String getString(@NonNull String text){
                String result = "";
                char[] a = text.toCharArray();
                for(int i = 0; i<a.length;i+=2) {
                    char ch = (char) Integer.parseInt(""+a[i] + "" + a[i+1], 16);
                    result = result+ch;
                }
                return result;
            }
        }
    }
    //a static class with a constructor
    public static class showDialog extends DialogFragment{
        @NonNull String head, msg;
        int numBtn;
        @Nullable Class aClass;
        @Nullable Context context;
        //constructor for this class
        public showDialog(@NonNull String Title, @NonNull String message, int num, @Nullable Class classs, @Nullable Context c){
            this.head = Title;
            this.msg = message;
            this.numBtn = num;
            this.aClass = classs;
            this.context = c;
        }
        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Dialog dialog;
            switch (numBtn){
                case 1:
                    dialog = new AlertDialog.Builder( getActivity() )
                            .setTitle( head ).setMessage( msg ).create();
                    new Handler().postDelayed( new Runnable() {
                        @Override
                        public void run() {
                            if(aClass != null && context != null){
                                startActivity( new Intent(context, aClass) );
                            }
                            else{
                                dialog.dismiss();
                            }
                        }
                    }, 1500 );
                    break;
                case 2:
                    dialog = new AlertDialog.Builder( getActivity() ).setTitle( head ).setMessage( msg ).setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity( new Intent(context,aClass ) );
                        }
                    } ).setNegativeButton( "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Log.d("tag", String.valueOf( which ));
                        }
                    } ).create();
                    break;
                case 3:
                    dialog = new AlertDialog.Builder( getActivity() ).setTitle( head ).setMessage( msg ).setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            System.exit( 0 );
                        }
                    } ).setNegativeButton( "No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    } ).create();
                    break;
                case 4:
                    dialog = new AlertDialog.Builder( getActivity() ).setTitle( head ).setMessage( msg ).setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    } ).create();
                    break;
                default:
                    throw new IllegalStateException( "Unexpected value: " + numBtn );
            }
            return dialog;
        }
    }
    public static class getDirs{
        private static final String dir = Environment.getExternalStorageDirectory().getPath() + "/Vaulthub";
        public static final String getUserPublicKey = dir + "/userKeys/publicKey.key";
        public static final String getUserPrivateKey = dir + "/userKeys/privateKey.key";
        public static final String getLoginPublicKeydir = dir + "/loginKeys/publicKey.key";
        public static final String getLoginPrivateKey = dir + "/loginKeys/privateKey.key";
    }

}
