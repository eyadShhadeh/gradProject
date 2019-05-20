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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.me.lazqaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

public class UserDesign extends AppCompatActivity {

    private Button confirmBtn ;
    private Button frontImageBtn ;
    private Button backImageBtn ;
    private Spinner colorListSpin ;
    private Spinner shirtSizeListSpin ;
    private ImageView simpleFront ;
    private static TextView printedText;
    private static EditText uEditText = null;

    private static Uri frontImgUri;
    private static Uri backImgUri;
    private static final int fgallery_requsit = 1;
    private static final int bgallery_requsit = 2;
    private static String loc ;

    private CheckBox placeCheck ;

    private static  FirebaseAuth mAuth;
    private static FirebaseUser mUser ;
    private static String buyerName,buyerPhone,buyerId;

    public static DatabaseReference imges,myRef ;
    private static StorageReference ffs ;

    public static int i;

    private static ProgressDialog pDialog;

    private StorageReference mStorage;
    private DatabaseReference refDatabase;

    private static String selectedColor ;

    private static String finalPrice = "3" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // initialization area
        setContentView(R.layout.activity_user_design);
        simpleFront = findViewById(R.id.simple_Front);
        placeCheck = findViewById(R.id.loc_checkBox_top);
        myRef = FirebaseDatabase.getInstance().getReference().child("Users");
        confirmBtn = findViewById(R.id.confirm_User_Design);
        frontImageBtn = findViewById(R.id.front_Image);
        backImageBtn = findViewById(R.id.back_Image);
        uEditText = findViewById(R.id.simple_text);
        printedText = findViewById(R.id.printed_text);

        shirtSizeListSpin = findViewById(R.id.shirt_size_spin);
        colorListSpin = findViewById(R.id.color_Select);

        backImageBtn.setVisibility(View.GONE);

        uEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                printedText.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        pDialog = new ProgressDialog(this);

        mStorage = FirebaseStorage.getInstance().getReference();
        refDatabase = FirebaseDatabase.getInstance().getReference().child("orders");
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        buyerId = mUser.getUid();

        placeCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addCurrentLocation ();
            }
        });

        frontImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               openGallery(1);
                backImageBtn.setVisibility(View.VISIBLE);
            }
        });
        backImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery(2);
            }
        });
        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startSubmit();
            }
        });

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dc : dataSnapshot.getChildren()){
                    if(dc.getKey().equals(buyerId)){
                        buyerName = dc.child("name").getValue().toString();
                        buyerPhone= dc.child("phone").getValue().toString();

                      //  Toast.makeText(UserDesign.this, buyerName+buyerPhone, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        Picasso.with(this).load(R.mipmap.t_shirt_front).fit().into(simpleFront);
        printedText.setText(uEditText.getText().toString()) ;

        getIncomingIntent ();

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter <CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.color_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        colorListSpin.setAdapter(adapter);

        // Create an ArrayAdapter using the string array and a default spinner layout for 2nd adapter
        ArrayAdapter <CharSequence> sizeAdapter = ArrayAdapter.createFromResource(this,
                R.array.shirt_size, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        shirtSizeListSpin.setAdapter(sizeAdapter);


        }
    private void addCurrentLocation (){

        if(placeCheck.isChecked()){
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(UserDesign.this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(UserDesign.this,
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
    private void getIncomingIntent (){
        if (getIntent().hasExtra("img_url")&&getIntent().hasExtra("img_price")){

            String imgUrl = getIntent().getStringExtra("img_url");
            String imgPrice = getIntent().getStringExtra("img_price");
            setIntentInfo(imgUrl,imgPrice);
        }
    }
    private void setIntentInfo(String imgUrl ,String imgPrice ){

        finalPrice = imgPrice ;
      //  Picasso.with(this).load(imgUrl).fit().into(imgBtn);

        frontImgUri = Uri.parse(imgUrl);
        Toast.makeText(this, "Design picked, choose other details", Toast.LENGTH_LONG).show();
       // frontImageBtn.setClickable(false);
        frontImageBtn.setVisibility(View.GONE);
    }



    public void startSubmit() {
        pDialog.setMessage("Uploading . . .");
       pDialog.show();

       final StorageReference filePath = mStorage.child("orders").child("Top_Wear");


       if (!filePath.equals(null))
       ffs = filePath.child(random());

       filePath.child(random()).putFile(frontImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Uri downloadUri = taskSnapshot.getDownloadUrl();


            selectedColor = colorListSpin.getSelectedItem().toString();

            DatabaseReference newDesign = refDatabase.child("Top_Wear").push();


            imges = newDesign;
                newDesign.child("back_image").setValue(downloadUri.toString());




                newDesign.child("text").setValue(uEditText.getText().toString());

            newDesign.child("size").setValue(shirtSizeListSpin.getSelectedItem().toString());

                newDesign.child("location").setValue(loc);
            newDesign.child("designID").setValue(newDesign.getKey().toString());
            newDesign.child("color").setValue(selectedColor);
            newDesign.child("buyer_phone").setValue(buyerPhone);
            newDesign.child("buyer_name").setValue(buyerName);
            newDesign.child("image").setValue(downloadUri.toString());
            newDesign.child("price").setValue("10");

            newDesign.child("Uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

            newDesign.child("Active_statues").setValue("1");

            Toast.makeText(UserDesign.this, "1st image done", Toast.LENGTH_SHORT).show();
        }

    }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
            if(task.isSuccessful()){
                putBackPic();
            }
        }
    }).addOnFailureListener(new OnFailureListener() {
        @Override
        public void onFailure(@NonNull Exception e) {



            DatabaseReference newDesign = refDatabase.child("Top_Wear").push();
            newDesign.child("size").setValue(shirtSizeListSpin.getSelectedItem().toString());

            newDesign.child("location").setValue(loc);
            newDesign.child("color").setValue(selectedColor);

            newDesign.child("image").setValue(frontImgUri.toString());
            newDesign.child("designID").setValue(newDesign.getKey().toString());
            newDesign.child("buyer_phone").setValue(buyerPhone);
            newDesign.child("buyer_name").setValue(buyerName);

            newDesign.child("Uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

            newDesign.child("price").setValue(finalPrice);
            newDesign.child("Active_statues").setValue("1");

            pDialog.dismiss();
            startActivity(new Intent(UserDesign.this,browsingActivity.class));
            UserDesign.this.finish();
        }
    });
    }

    private void putBackPic(){

        ffs.putFile(backImgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
        @Override
        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            Uri downloadUri = taskSnapshot.getDownloadUrl();
            imges.child("back_image").setValue(downloadUri.toString());



        }
    }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserDesign.this, "something wrong", Toast.LENGTH_SHORT).show();
            }
        }).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                Toast.makeText(UserDesign.this, "done of the 2nd", Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
                UserDesign.this.finish();
                startActivity(new Intent(UserDesign.this, browsingActivity.class));

            }
        });}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode ==RESULT_OK && requestCode == fgallery_requsit){
            if (data != null) {

                    frontImgUri= data.getData();

                    Toast.makeText(this, "done first", Toast.LENGTH_SHORT).show();

            }

        }
        if(resultCode ==RESULT_OK && requestCode == bgallery_requsit){
            if (data != null) {

                backImgUri= data.getData();

                Toast.makeText(this, "done 2nd", Toast.LENGTH_SHORT).show();

            }

        }




}

    public void openGallery(int i) {
        if (i == 1){
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType( "image/*");
        startActivityForResult(galleryIntent, fgallery_requsit);}
        else if (i == 2) {

            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.setType( "image/*");
            startActivityForResult(galleryIntent, bgallery_requsit);
        }
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
