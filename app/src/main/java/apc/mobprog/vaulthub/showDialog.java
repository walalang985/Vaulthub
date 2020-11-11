package apc.mobprog.vaulthub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class showDialog  extends DialogFragment {
    @Nullable String head, msg;
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
                    }
                } ).create();
                break;
            case 3:
                dialog = new AlertDialog.Builder( getActivity() ).setTitle( head ).setMessage( msg ).setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } ).create();
            default:
                throw new IllegalStateException( "Unexpected value: " + numBtn );
        }
        return dialog;
    }
}
