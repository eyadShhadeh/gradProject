import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.me.lazqaapp.R;


public class ViewHolder extends RecyclerView.ViewHolder {

    // design components view holder
    public TextView post_price;
    public TextView post_title;
    public ImageView post_image;
    public ProgressBar progress_Bar;


    // order components view holder
    public TextView fOrder_id;
    public TextView fPrice;
    public TextView fSize;
    public TextView fName;
    public TextView fPhone;
    public TextView fLocation;
    public ImageView fPost_image;
    public ProgressBar fprogress_Bar;

    public ViewHolder(View itemView) {
        super(itemView);


        //design part
        post_price= itemView.findViewById(R.id.price);
        post_title = itemView.findViewById(R.id.cardTitle);
        post_image = itemView.findViewById(R.id.cardImage);
        progress_Bar= itemView.findViewById(R.id.cardProgressDialog);







        //order fetch part
        fLocation = itemView.findViewById(R.id.fetch_location);
        fName = itemView.findViewById(R.id.fetch_name);
        fOrder_id = itemView.findViewById(R.id.fetch_id);
        fPhone = itemView.findViewById(R.id.fetch_phone);
        fPrice = itemView.findViewById(R.id.fetch_price);
        fSize = itemView.findViewById(R.id.fetch_size);
        fprogress_Bar = itemView.findViewById(R.id.orderFetchProgressCard);
        fPost_image = itemView.findViewById(R.id.fetchCardImage);



       // fragmetPick(pick,itemView);

    }


    // set detail for RecyclerView row
    /*public void setDetail (Context ctx, String image,String title ,String price){



        post_price.setText(price);
        post_title.setText(title);
        Picasso.with(ctx).load(image).fit().into(post_image);


    }*/
}
