import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.me.lazqaapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.*;

import java.util.Random;

public class add_activity extends AppCompatActivity {

    private RadioGroup categories_rGroup;
    private RadioButton rBtn;

    private static String type_choice;

    private Button submitBtn ;
    private ImageButton imageBtn ;

    private EditText titleText;
    private EditText priceText;

    private static String title_val ;
    private  String price_val ;

    private static Uri imgUri;
    private ProgressDialog pDialog;

    private static DatabaseReference mDatabase;
    private StorageReference mStorage ;


    private static final int gallery_requsit = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        categories_rGroup = findViewById(R.id.caregories_select);
        submitBtn = findViewById(R.id.addSubmit);
        imageBtn = findViewById(R.id.image_addBtn);

        mStorage = FirebaseStorage.getInstance().getReference();
        mDatabase= FirebaseDatabase.getInstance().getReference().child("design");
        mDatabase.keepSynced(true);


        titleText = findViewById(R.id.add_title_feild);
        priceText = findViewById(R.id.add_price_feild);



         pDialog = new ProgressDialog(this);


        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    openGallery();
            }

        });




        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                title_val = titleText.getText().toString().trim();
                price_val = priceText.getText().toString().trim();

             //   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /* if(ContextCompat.checkSelfPermission(add_activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
                 {
                     Toast.makeText(add_activity.this, "permission Denied", Toast.LENGTH_SHORT).show();
                     ActivityCompat.requestPermissions(add_activity.this,new  String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
                 }*/
             //    else {

                     if (!TextUtils.isEmpty(title_val) && !TextUtils.isEmpty(price_val) && imgUri != null
                             && !type_choice.isEmpty()) {

                         startSubmit();

                     } else {
                         Toast.makeText(add_activity.this, "fill everything", Toast.LENGTH_SHORT).show();
                     }
                 }
             //   }

          //  }
        });


    }

    public void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType( "image/*");
        startActivityForResult(galleryIntent, gallery_requsit);
    }


    private void startSubmit() {
            pDialog.setMessage("Uploading . . .");
            pDialog.show();

        StorageReference filePath = mStorage.child("design").child(type_choice.toLowerCase()).child(random());
        filePath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Uri downloadUri = taskSnapshot.getDownloadUrl();


                mDatabase = mDatabase.child(type_choice);
                DatabaseReference newDesign = mDatabase.push();
                newDesign.child("title").setValue(title_val);
               // newDesign.child("type").setValue(type_choice);
                newDesign.child("price").setValue(price_val);
                newDesign.child("design_id").setValue(random());
                newDesign.child("image").setValue(downloadUri.toString());
                newDesign.child("Uid").setValue(FirebaseAuth.getInstance().getCurrentUser().getUid().toString());

                newDesign.child("Active_statues").setValue("1");

                pDialog.dismiss();
            startActivity(new Intent(add_activity.this,browsingActivity.class));

            }


        });



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(resultCode ==RESULT_OK || resultCode == gallery_requsit)

            if (data != null) {

            imgUri = data.getData();



       Picasso.with(this).load(imgUri).fit().into(imageBtn);
    }
}



    public void categories_radioCheck (View v){

        int radioBtnId = categories_rGroup.getCheckedRadioButtonId();
        rBtn = findViewById(radioBtnId);
        String type_val = rBtn.getText().toString().trim().toLowerCase();

        Toast.makeText(this,type_val, Toast.LENGTH_SHORT).show();

        if (type_val.equals("mugs")){
            type_choice = "mugs";
            }
        else if (type_val.equals("top wear")){
            type_choice = "Top_wear";
        }
        else if (type_val.equals("laptop cover")){
            type_choice = "laptop_cover";
        }
        else if (type_val.equals("others")){
            type_choice = "others";
        }
    }


    //random name generator
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