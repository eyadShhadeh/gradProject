import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.me.lazqaapp.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Random;

public class MugDesign extends AppCompatActivity {

    private static final int ERORR_DIALOG_REQUIST = 9001;

    private static final String TAG = "MugDesign";
    private Spinner typeSpinner;
    private ProgressDialog pDialog;
    private static final int gallery_requsit = 1;
    private static Uri ImgUri;
    private StorageReference mStorage;
    private DatabaseReference refDatabase, myRef;
    private static String buyerName, buyerPhone, buyerId;
    private static FirebaseAuth mAuth;
    private static FirebaseUser mUser;
    private static String loc ;
    private static String finalPrice = "3";

    private ImageButton imgBtn;
    private Button conBtn;
    private Button btnMap;
    private CheckBox placeCheck ;

    private static final int PLACE_PICK_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lap_top_design);

        typeSpinner = findViewById(R.id.size_Select); // here we take the this spinner because we are taking
        //  same layout of laptop but we changed the adapter

        mStorage = FirebaseStorage.getInstance().getReference();
        refDatabase = FirebaseDatabase.getInstance().getReference().child("orders");
        imgBtn = findViewById(R.id.laptop_image_addBtn);
        conBtn = findViewById(R.id.laptop_confirm);
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");

        placeCheck = findViewById(R.id.loc_checkBox);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        buyerId = mUser.getUid().toString();

        if (isServicesOK()) {
            init();
        }


        placeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addCurrentLocation ();
            }
        });

        pDialog = new ProgressDialog(this);

        conBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startSubmit();
            }
        });


        imgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // to get buyer name and adding it to the order
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dc : dataSnapshot.getChildren()) {
                    if (dc.getKey().equals(buyerId)) {
                        buyerName = dc.child("name").getValue().toString();
                        buyerPhone = dc.child("phone").getValue().toString();

                        //  Toast.makeText(MugDesign.this, buyerName+buyerPhone, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        getIncomingIntent();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.mug_type_pick, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        typeSpinner.setAdapter(adapter);

    }

    private void addCurrentLocation (){

        if(placeCheck.isChecked()){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(MugDesign.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(MugDesign.this,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            loc = location.getLatitude()+","+location.getLongitude();
            Toast.makeText(this, loc, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK || resultCode == gallery_requsit)

            if (data != null) {

                ImgUri = data.getData();
                Picasso.with(this).load(ImgUri).fit().into(imgBtn);
            }

    }

    private void getIncomingIntent() {
        if (getIntent().hasExtra("img_url") && getIntent().hasExtra("img_price")) {

            String imgUrl = getIntent().getStringExtra("img_url");
            String imgPrice = getIntent().getStringExtra("img_price");
            setIntentInfo(imgUrl, imgPrice);
        }
    }

    private void setIntentInfo(String imgUrl, String imgPrice) {

        finalPrice = imgPrice;
        Picasso.with(this).load(imgUrl).fit().into(imgBtn);
        ImgUri = Uri.parse(imgUrl);
        imgBtn.setClickable(false);
    }

    private void startSubmit() {
        pDialog.setMessage("Uploading . . .");
        pDialog.show();

        StorageReference filePath = mStorage.child("orders").child("mug");
        filePath.child(random()).putFile(ImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();


                DatabaseReference newDesign = refDatabase.child("mug").push();
                newDesign.child("type").setValue(typeSpinner.getSelectedItem().toString());


                newDesign.child("buyer_phone").setValue(buyerPhone);
                newDesign.child("buyer_name").setValue(buyerName);
                newDesign.child("designID").setValue(newDesign.getKey().toString());
                newDesign.child("image").setValue(downloadUri.toString());
                newDesign.child("Uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                newDesign.child("location").setValue(loc);

                if (typeSpinner.getSelectedItem().toString().equals("Normal Mug")) {
                    finalPrice = "3";
                } else if (typeSpinner.getSelectedItem().toString().equals("Magic Mug")) {
                    finalPrice = "5";
                }

                newDesign.child("price").setValue(finalPrice);

                newDesign.child("Active_statues").setValue("1");

                pDialog.dismiss();

                MugDesign.this.finish();
                startActivity(new Intent(MugDesign.this, browsingActivity.class));

            }


        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {


                DatabaseReference newDesign = refDatabase.child("mug").push();
                newDesign.child("type").setValue(typeSpinner.getSelectedItem().toString());


                newDesign.child("image").setValue(ImgUri.toString());
                newDesign.child("designID").setValue(newDesign.getKey().toString());
                newDesign.child("buyer_phone").setValue(buyerPhone);
                newDesign.child("buyer_name").setValue(buyerName);
                newDesign.child("Uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());
                newDesign.child("location").setValue(loc);

                if (typeSpinner.getSelectedItem().toString().equals("Normal Mug")) {
                    finalPrice = "3";
                } else if (typeSpinner.getSelectedItem().toString().equals("Magic Mug")) {
                    finalPrice = "5";
                }

                newDesign.child("price").setValue(finalPrice);

                newDesign.child("Active_statues").setValue("1");

                pDialog.dismiss();
                startActivity(new Intent(MugDesign.this, browsingActivity.class));
                MugDesign.this.finish();
            }
        });
    }


    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, gallery_requsit);
    }


    public void init() {

        btnMap = findViewById(R.id.map_btn);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                if (ActivityCompat.checkSelfPermission(MugDesign.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MugDesign.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String loc = location.getLatitude()+","+location.getLongitude();

                Intent gIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:"+loc+"?q="+Uri.encode(loc)));
                int PLACE_PICKER_REQUEST = 1;
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                startActivityForResult(gIntent,PLACE_PICK_REQUEST_CODE);
                //startActivityForResult(builder.build(MugDesign.this), PLACE_PICKER_REQUEST);
*/






              /*  LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

                if (ActivityCompat.checkSelfPermission(MugDesign.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ActivityCompat.checkSelfPermission(MugDesign.this,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION)
                                != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }


                Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                String loc = location.getLatitude()+","+location.getLongitude();


                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4192?q=" + Uri.encode("1st & Pike, Seattle"));
                Intent gIntent;
                gIntent = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:"+loc+"?q="+Uri.encode(loc)));
                startActivityForResult(gIntent,PLACE_PICK_REQUEST_CODE);
                startActivity(gIntent);

//                Intent intent = new Intent(MugDesign.this,MapActivity.class);
//                startActivity(intent);*/
            }
        });

    }



    public boolean isServicesOK(){

        Log.d(TAG,"is services ok :checking google version");
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MugDesign.this);
        if(available == ConnectionResult.SUCCESS){
            //everything is fine the use can make map request
            Log.d(TAG,"isServicesOK : google play services is working");
            return true;
        }
        else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error accord but we can fix it
            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(MugDesign.this,available,ERORR_DIALOG_REQUIST);
            dialog.show();
        }
        else{
            Toast.makeText(this, "cant make map request :( ", Toast.LENGTH_SHORT).show();}
        return false;
    }


    public static String random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(20);
        char tempChar;
        for (int i = 0; i < randomLength; i++){
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
