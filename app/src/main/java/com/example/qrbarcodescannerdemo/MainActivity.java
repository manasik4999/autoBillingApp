package com.example.qrbarcodescannerdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.zxing.Result;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

import static android.Manifest.permission.CAMERA;
import static java.lang.reflect.Array.set;

public class MainActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int REQUEST_CAMERA =1;
    private ZXingScannerView scannerView;
    private static final String number="code";
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String s;
    private Activity mActivity;

//    public void displayResult (String scanResult)
//    {
////        String code = scanResult;
//
////        Map<String,Object> note = new HashMap<>();
////
////        db.collection("ProductList").add(note)
////                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
////                    @Override
////                    public void onSuccess(DocumentReference documentReference) {
////
////                        Log.d("TAG", "DocumentSnapshot added with ID: " + documentReference.getId());
////                    }
////                })
////                .addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////                        Log.w("TAG", "Error adding document", e);
////                    }
////                });;
////            note.put("Barcode", scanResult);
////            Toast.makeText(MainActivity.this, "Entered fn", Toast.LENGTH_LONG).show();
//
//
//        DocumentReference scan = db.collection("Barcode_Num").document();
//        Map<String, Object> note1 = new HashMap<>();
//        note1.put("b_num", scanResult);
//        scan.set(note1);
////                .addOnSuccessListener(new OnSuccessListener<Void>()
////                {
////                    @Override
////                    public void onSuccess(Void aVoid) {
////                        Toast.makeText(MainActivity.this, "Saved", Toast.LENGTH_LONG).show();
////                    }
////                })
////                .addOnFailureListener(new OnFailureListener() {
////                    @Override
////                    public void onFailure(@NonNull Exception e) {
////                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_LONG).show();
////                        Log.w("ERROR!!!", e.toString());
////                    }
////                });
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);


        if(checkPermission())
        {
            Toast.makeText(getApplicationContext(), "Permission already granted!", Toast.LENGTH_LONG).show();
        }
        else
        {
            requestPermission();
        }

    }

    private boolean checkPermission()
    {
        return (ContextCompat.checkSelfPermission(MainActivity.this, CAMERA) == PackageManager.PERMISSION_GRANTED);
    }

    private void requestPermission()
    {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, REQUEST_CAMERA);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA:
                if (grantResults.length > 0) {

                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted){
                        Toast.makeText(MainActivity.this, "Permission Granted, Now you can access camera", Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(MainActivity.this, "Permission Denied, You cannot access and camera", Toast.LENGTH_LONG).show();
                        if (shouldShowRequestPermissionRationale(CAMERA))
                        {
                            displayAlertMessage("You need to allow access for both permissions",new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    requestPermissions(new String[]{CAMERA}, REQUEST_CAMERA);
                                }
                            });
                            return;
                        }
                    }
                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()) {
                if (scannerView == null) {
                    scannerView = new ZXingScannerView(this);
                    setContentView(scannerView);
                }
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                requestPermission();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        scannerView.stopCamera();
    }





    public void displayAlertMessage(String message, DialogInterface.OnClickListener listener)
    {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", listener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }



    @Override
    public void handleResult(Result result) {
        final String scanResult = result.getText();
//        displayResult(scanResult);
//        Log.i(scanResult);

//        final Integer[] total_cost = {0};
//        final Integer[] total_weight = {0};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Scan Result");
        builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                scannerView.resumeCameraPreview(MainActivity.this);
//                displayResult(scanResult);

            }
        });

//         builder.setNeutralButton("Visit", new DialogInterface.OnClickListener() {
//           @Override
//          public void onClick(DialogInterface dialogInterface, int i) {
//              Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(scanResult));
//              startActivity(intent);
//          }
//         });
        builder.setNeutralButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Hard-coded URL for testing purposes
                //Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://google.com"));
               // displayResult(scanResult);
               // startActivity(browserIntent);
//                Log.i("Error: " + scanResult);

                DocumentReference scan = db.collection("Barcode_Num").document();
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                final DocumentReference docRef = db.collection("Total").document("costNWeight");
                Map<String, Object> note1 = new HashMap<>();
                note1.put("b_num", scanResult);

                scan.set(note1);
                //Integer total_cost = 0;
//                Number total_weight;
                CollectionReference collre_barcode = FirebaseFirestore.getInstance().collection("ProductList1");
                collre_barcode.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                        if(e!=null){
                            return;
                        }

                        for (final QueryDocumentSnapshot documentSnapshots : queryDocumentSnapshots){
                            Log.d("cost", "into for loop");
                            Log.d("cost", scanResult+ "sc");
                            Log.d("cost", documentSnapshots.getString("barcode_no"));
                            if(scanResult.equals(documentSnapshots.getString("barcode_no"))){
                                final int c = ((Long) documentSnapshots.get("cost")).intValue();
                                final int w = ((Long) documentSnapshots.get("weight")).intValue();
//                                total_cost[0] += c;
//                                total_weight[0] += w;

                                docRef.get()
                                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                            @Override
                                            public void onSuccess(DocumentSnapshot documentSnapshot) {
                                                int c1 = ((Long) documentSnapshot.get("Tcost")).intValue();
                                                int w1 = ((Long) documentSnapshot.get("Tweight")).intValue();

//                                                NumberFormat formatter = NumberFormat.getNumberInstance();
//                                                formatter.setMinimumFractionDigits(2);
//                                                formatter.setMaximumFractionDigits(2);

                                                int c2 = c1+c;
                                                int w2 = w1+w;

//                                                float w3 = roundTo2Decs(w2);
//                                                float w4  = Float.parseFloat(String.valueOf(w2));
//                                                DecimalFormat decimalFormat = new DecimalFormat("#.##");
//                                                float w5 = Float.valueOf(decimalFormat.format(w2));

                                               // Log.d("twght", String.valueOf(Float.valueOf(w2)));
                                                docRef.update("Tcost", c2);
                                                docRef.update("Tweight", w2);
                                            }
                                        });

//                                Log.d("cost", String.valueOf(total_cost[0])+" cost");
//                                Log.d("cost", String.valueOf(total_weight[0])+" weight");

                            }
                        }
//                        Log.d("cost", String.valueOf(total_cost[0])+" costttt");
                    }


                });

                //Log.d("cost", String.valueOf(total_cost[0])+" costttt");
                //Toast.makeText(getApplicationContext(), total_cost[0],Toast.LENGTH_SHORT).show();
                //Log.d("cost", String.valueOf(total_cost[0]));
                //restartActivity(mActivity);
                Intent intent = getIntent();
                finish();
                startActivity(intent);

            }
        });

        builder.setMessage(scanResult);
        AlertDialog alert = builder.create();
        alert.show();

    }

    public static void restartActivity(Activity activity) {
        if (Build.VERSION.SDK_INT >= 11) {
            activity.recreate();
        } else {
            activity.finish();
            activity.startActivity(activity.getIntent());
        }
    }

//    private float roundTo2Decs(float value) {
//        BigDecimal bd = new BigDecimal(value);
//        bd = bd.setScale(2, RoundingMode.HALF_UP);
//        return bd.floatValue();
//    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
        finish();

    }
}

