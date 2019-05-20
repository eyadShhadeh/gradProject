import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import com.example.me.lazqaapp.R;

public class Choice extends AppCompatDialogFragment {


    ImageButton shirt,mug,laptop,other;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.activity_choice,null);
        shirt = v.findViewById(R.id.shirtPick);
        mug = v.findViewById(R.id.mugPick);
        laptop = v.findViewById(R.id.coverPick);
        other = v.findViewById(R.id.other1);

        shirt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shirtIntent = new Intent(getActivity(),UserDesign.class);
                getActivity().startActivity(shirtIntent);

            }
        });
        mug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),MugDesign.class);
                getActivity().startActivity(intent);
            }
        });
        laptop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent laptopIntent = new Intent(getActivity(),LapTopDesign.class);
                getActivity().startActivity(laptopIntent);

            }
        });
        other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getContext(), "stay tuned for other product", Toast.LENGTH_SHORT).show();
                Intent laptopIntent = new Intent(getActivity(),OtherDesign.class);
                getActivity().startActivity(laptopIntent);

            }
        });




        builder.setView(v).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        return builder.create();

    }



    /* @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        shirt = findViewById(R.id.shirtPick);
        mug = findViewById(R.id.mugPick);
        laptop = findViewById(R.id.coverPick);
        other = findViewById(R.id.shirtPick);

    }*/
}
