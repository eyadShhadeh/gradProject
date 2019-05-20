import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.me.lazqaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register_Activity extends AppCompatActivity {


    private EditText dName ;
    private EditText dEmail;
    private EditText dPassword;
    private EditText dPhoneNum;

    private ProgressDialog dProgress ;

    private Button dRegisterBtn;

    private RadioGroup rGroup ;
    private  RadioButton rBtn;


    private FirebaseAuth mAuth;
    private DatabaseReference dBaseRef ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_);


        // implementing the variables
        mAuth = FirebaseAuth.getInstance();

        dName = findViewById(R.id.nameField);
        dEmail = findViewById(R.id.emailAddressField);
        dPassword = findViewById(R.id.passwordField);
        dPhoneNum = findViewById(R.id.phoneNumFeild);

        dRegisterBtn = findViewById(R.id.registerBtn);

        rGroup = findViewById(R.id.radioGroup);

        dProgress = new ProgressDialog(this);


        dBaseRef = FirebaseDatabase.getInstance().getReference().child("Users");


        // click listener for the register button
        dRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Toast.makeText(Register_Activity.this, "hi", Toast.LENGTH_SHORT).show();
                startRegister();
            }
        });


    }


    //the method that get the gender value
    public void radioCheck (View v){
        int radioBtnId = rGroup.getCheckedRadioButtonId();
        rBtn = findViewById(radioBtnId);
//        Toast.makeText(this, rBtn.getText(), Toast.LENGTH_SHORT).show();

    }



    // the precess that start when register Button is pressed
    private void startRegister() {

        final String userName = dName.getText().toString().trim();
        String email = dEmail.getText().toString().trim();
        String password = dPassword.getText().toString().trim();
        final String phoneNum= dPhoneNum.getText().toString().trim();


        // to check fields
        if(!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(email)&& !TextUtils.isEmpty(phoneNum)&& !TextUtils.isEmpty(password) )
        {

            dProgress.setMessage("registering . . . .");
            dProgress.show();

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {


                    //to check registering if it successful or not
                    if(task.isSuccessful()){

                        String user_id = mAuth.getCurrentUser().getUid();

                        DatabaseReference current_user_db = dBaseRef.child(user_id);

                        current_user_db.child("name").setValue(userName);
                        current_user_db.child("phone").setValue(phoneNum);
                        current_user_db.child("user_type").setValue("c");
                        current_user_db.child("gender").setValue(rBtn.getText());

                        dProgress.dismiss();
                        Intent myIntent = new Intent(Register_Activity.this ,browsingActivity.class);
                        startActivity(myIntent);
                        Register_Activity.this.finish();
                    }

                    else{
                        dProgress.dismiss();
                        Toast.makeText(Register_Activity.this,
                                "the email already registered or the password is too short", Toast.LENGTH_SHORT).show();

                    }
                }
            });



        }
        else{
            Toast.makeText(this, "some fields are empty", Toast.LENGTH_SHORT).show();
        }



    }
}
