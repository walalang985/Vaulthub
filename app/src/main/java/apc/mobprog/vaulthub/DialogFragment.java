package apc.mobprog.vaulthub;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
public class DialogFragment extends androidx.fragment.app.DialogFragment {
    @Nullable String head, msg;
    @Nullable int numBtn;
    @Nullable Class aClass;
    @Nullable  Context context;
    public DialogFragment(String Title, String message,Class classs, Context c) {
        this.head = Title;
        this.msg = message;
        this.numBtn = 1;
        this.aClass = classs;
        this.context = c;
    }
    public DialogFragment(String Title, String message, int num,@Nullable Class classs, Context c) {
        this.head = Title;
        this.msg = message;
        this.numBtn = num;
        this.aClass = classs;
        this.context = c;
    }
    public DialogFragment(@NonNull String Title,@NonNull String message){
        this.head = Title;
        this.msg = message;
        this.numBtn = 1;
        this.aClass = null;
        this.context = null;
    }
    public void kill(){
        System.exit( 0 );
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_dialog, container, false );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Dialog dialog;
        switch (numBtn) {
            case 1:
                dialog = new AlertDialog.Builder( getActivity() )
                        .setTitle( head ).setMessage( msg ).create();
                Handler handler = new Handler();
                handler.postDelayed( new Runnable() {
                    @Override
                    public void run() {
                        if(aClass != null && context != null){
                            startActivity( new Intent(context, aClass) );
                        }
                    }
                }, 1500 );
                break;
            case 2:
                dialog = new AlertDialog.Builder( getActivity() ).setTitle( head ).setMessage( msg ).setPositiveButton( "Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity( new Intent(getContext(),aClass ) );
                    }
                } ).setNegativeButton( "No", new DialogInterface.OnClickListener() {
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
