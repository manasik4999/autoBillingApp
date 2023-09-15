package com.example.qrbarcodescannerdemo;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;

public class TotalDialog extends AppCompatDialogFragment {

    private TextView Tc,Tw;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.totaldialog, null);

        Tc = view.findViewById(R.id.tcost);
        Tw = view.findViewById(R.id.tweight);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final DocumentReference docRef = db.collection("Total").document("costNWeight");

        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        int c1 = ((Long) documentSnapshot.get("Tcost")).intValue();
                        int w1 = ((Long) documentSnapshot.get("Tweight")).intValue();

                        float w3 = (float)w1/1000;
                        Log.d("flo2", w1+" w1");
                        Log.d("flo2", w3+" w3");
                        Log.d("flo2", c1+" c1");
//                        DecimalFormat decimalFormat = new DecimalFormat("#.##");
//                        float w5 = Float.valueOf(decimalFormat.format(w3));

                        String s = String.format("%.5f", w3);

                        Tc.setText(String.valueOf(c1));
                        Tw.setText(s);

                        Toast.makeText(getContext(),"DOne",Toast.LENGTH_SHORT);
                    }
                });
        builder.setView(view)
                .setTitle("Your Bill")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

        return builder.create();
    }
}
