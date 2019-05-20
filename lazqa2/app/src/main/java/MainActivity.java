import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.me.lazqaapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText dEmailField;
    private EditText dpasswordField;

    private Button dSignUp;
    private Button dLogin;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener dAuthListener ;


    protected void onCreate(Bundle savedInstanceState) {    // floating thing we will check it later start
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar =  findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //connection check
        connectionDialog();

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); //  the end of the floating thing

        fab.hide();



        dEmailField = findViewById(R.id.emailField);
        dpasswordField = findViewById(R.id.passwordField);

        dLogin = findViewById(R.id.loginBtn);
        dSignUp = findViewById(R.id.signUpBtn);

        mAuth = FirebaseAuth.getInstance();

        dSignUp.setOnClickListener(new View.OnClickListener() {  // login event start
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(MainActivity.this ,Register_Activity.class);
                MainActivity.this.startActivity(myIntent);

            }
        });






        dLogin.setOnClickListener(new View.OnClickListener() {  // login event start
            @Override
            public void onClick(View view) {

                startSignIn();

                dAuthListener = new FirebaseAuth.AuthStateListener() {
                    @Override
                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                        if(firebaseAuth.getCurrentUser() != null){

                            Intent myIntent = new Intent(MainActivity.this ,browsingActivity.class);
                            MainActivity.this.startActivity(myIntent);
                            MainActivity.this.finish();
                        }
                    }
                };
                mAuth.addAuthStateListener(dAuthListener);
            }
        }); // login event finish
    }// end on create method



    @Override
    protected void onStart() {
        super.onStart();

    }

    private void startSignIn() {

            String email = dEmailField.getText().toString();
            String password = dpasswordField.getText().toString();
            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {

                Toast.makeText(MainActivity.this, "the field is empty ", Toast.LENGTH_SHORT).show();
            }

else{
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {

                            Toast.makeText(MainActivity.this, "Sign In Problem ", Toast.LENGTH_SHORT).show();

                        }
                    }


                });

            }

        }

    public void connectionDialog() {

        Log.i("Dialog", "in Dialog");
        if (!isConnected()) {
            Log.i("Dialog", "is Online");
            AlertDialog.Builder diolag;
            diolag = new AlertDialog.Builder(this);
            diolag.setTitle("connection ");
            diolag.setMessage("Please check your connection ");
            diolag.setIcon(android.R.drawable.ic_dialog_alert);

            diolag.setPositiveButton("Restart", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    MainActivity.this.finish();
                    Intent intent=new Intent(MainActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            });
            diolag.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                }
            });

            diolag.show();

        }

    }//close fn Dialog

    public boolean isConnected() {
        String command = "ping -c 1 google.com";
        try {
            Log.i("Ping",Runtime.getRuntime().exec(command).waitFor()+"");
            return (Runtime.getRuntime().exec(command).waitFor() == 0);
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;

        }
    }

}
