package apc.mobprog.vaulthub;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class DialogAboutFragment extends androidx.fragment.app.DialogFragment {


    public DialogAboutFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate( R.layout.fragment_dialog_about, container, false );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(getActivity())
                .setTitle( "About Vaulthub" )

                .setMessage( "This vault ensures that the data stored in this application would be secured since it would undergo two levels of Encryption and Decryption. This vault also features a personalized login system so that only that user could access it. This vault would not be able to enter on other application because it only serves as a storage for your usernames and passwords" )

                .setPositiveButton( "Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                } ).create();
    }
}