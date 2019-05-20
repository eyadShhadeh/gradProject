import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.me.lazqaapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class OrderFetch extends AppCompatActivity {
private static String showState ="f";
    private static FirebaseRecyclerOptions options;
    private RecyclerView recyclerViewTops,recyclerViewCovers,recyclerViewMugs,recyclerViewOther ;
//    private static boolean longP = false;
    private DatabaseReference retrieveDataRef;
    private FirebaseDatabase fbase;
//     private static String var ;
    private FirebaseRecyclerAdapter<OrderModel,ViewHolder> firebaseRecyclerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_fetch);

        recyclerViewTops= findViewById(R.id.topWear_fetchList);
        recyclerViewCovers= findViewById(R.id.laptop_fetchList);
        recyclerViewMugs= findViewById(R.id.mug_fetchList);
        recyclerViewOther= findViewById(R.id.other_fetchList);


        orderFill(recyclerViewTops,"Top_Wear");
        orderFill(recyclerViewCovers,"laptop_cover");
        orderFill(recyclerViewMugs,"mug");
        orderFill(recyclerViewOther,"other");





    }

    private void orderFill(RecyclerView recyclerView , final String category ){


            fbase = FirebaseDatabase.getInstance();
            retrieveDataRef = fbase.getReference().child("orders");

        options = new FirebaseRecyclerOptions.Builder<OrderModel>().setQuery( retrieveDataRef.child(category),OrderModel.class).build();


        if (category.equals("Top_Wear")){

            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrderModel, ViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final OrderModel model) {


                    if (model.getActive_statues().equals("1")) {
                        Picasso.with(OrderFetch.this).load(model.getImage()).fit().into(holder.fPost_image);
                        holder.fPrice.setText(model.getPrice());
                        holder.fName.setText(model.getBuyer_name());
                        holder.fOrder_id.setText(model.getDesignID());
                        holder.fPhone.setText(model.getBuyer_phone());
                        holder.fSize.setText(model.getSize() +"&"+model.getColor());
                    } else {

                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                    holder.fLocation.setText(model.getLocation());
                    holder.fLocation.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:"+model.getLocation()
                                    +"?q="+Uri.encode(model.getLocation())));
                            startActivity(intent);
                        }
                    });
                    // done item
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            longPress();
                            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {

                                    if(showState.equals("t") ) {
                                        //  model.setActive_statues("0");
                                        retrieveDataRef.child(category).child(model.getDesignID())
                                                .child("Active_statues").setValue("0");
                                        if(model.getActive_statues().equals("0") ){
                                            holder.itemView.setVisibility(View.GONE);
                                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                        }
                                        showState = "f";


                                    }
                                    return true;
                                }
                            });

                            return true;
                        }
                    });
                }

                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_single_order,parent,false);
                    return new ViewHolder(view);
                }


            };
        }


        else if (category.equals("laptop_cover")) {
            firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrderModel, ViewHolder>(options) {
                @Override
                protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final OrderModel model) {


                    if (model.getActive_statues().equals("1")) {
                        Picasso.with(OrderFetch.this).load(model.getImage()).fit().into(holder.fPost_image);
                        holder.fPrice.setText(model.getPrice());
                        holder.fName.setText(model.getBuyer_name());
                        holder.fOrder_id.setText(model.getDesignID());
                        holder.fPhone.setText(model.getBuyer_phone());
                        holder.fSize.setText(model.getSize());
                    } else {

                        holder.itemView.setVisibility(View.GONE);
                        holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                    }
                    holder.fLocation.setText(model.getLocation());
                    holder.fLocation.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:"+model.getLocation()
                                    +"?q="+Uri.encode(model.getLocation())));
                            startActivity(intent);
                        }
                    });

                    // done item
                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            longPress();
                            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {

                                    if(showState.equals("t") ) {
                                        //  model.setActive_statues("0");
                                        retrieveDataRef.child(category).child(model.getDesignID())
                                                .child("Active_statues").setValue("0");
                                        if(model.getActive_statues().equals("0") ){
                                            holder.itemView.setVisibility(View.GONE);
                                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                        }
                                        showState = "f";


                                    }
                                    return true;
                                }
                            });

                            return true;
                        }
                    });

                }

                @Override
                public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_single_order,parent,false);
                    return new ViewHolder(view);
                }


            };
        }
            else if (category.equals("mug")) {
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrderModel, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final OrderModel model) {


                        if(model.getActive_statues().equals("1") ){
                        Picasso.with(OrderFetch.this).load(model.getImage()).fit().into(holder.fPost_image);
                        holder.fPrice.setText(model.getPrice());
                        holder.fName.setText(model.getBuyer_name());
                        holder.fOrder_id.setText(model.getDesignID());
                        holder.fPhone.setText(model.getBuyer_phone());
                        holder.fSize.setText(model.getType());

                        }
                        else {

                            holder.itemView.setVisibility(View.GONE);
                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                        }



                            holder.fLocation.setText(model.getLocation());
                            holder.fLocation.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:"+model.getLocation()
                                            +"?q="+Uri.encode(model.getLocation())));
                                    startActivity(intent);
                                }
                            });


                        // done item
                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                longPress();
                           holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                      @Override
                          public boolean onLongClick(View v) {

                           if(showState.equals("t") ) {
                   //  model.setActive_statues("0");
                    retrieveDataRef.child(category).child(model.getDesignID())
                    .child("Active_statues").setValue("0");
                   if(model.getActive_statues().equals("0") ){
                holder.itemView.setVisibility(View.GONE);
                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
            }
            showState = "f";


        }
        return true;
    }
});return true; }
                        });

                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_single_order,parent,false);

                            return new ViewHolder(view);
                    }


                };
            }




            else if (category.equals("other")) {
                firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<OrderModel, ViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final ViewHolder holder, int position, @NonNull final OrderModel model) {


                        if (model.getActive_statues().equals("1")) {
                            Picasso.with(OrderFetch.this).load(model.getImage()).fit().into(holder.fPost_image);
                            holder.fPrice.setText(model.getPrice());
                            holder.fName.setText(model.getBuyer_name());
                            holder.fOrder_id.setText(model.getDesignID());
                            holder.fPhone.setText(model.getBuyer_phone());
                            holder.fSize.setText(model.getType());
                        }
                        else {
                            holder.itemView.setVisibility(View.GONE);
                            holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                        }

                        holder.fLocation.setText(model.getLocation());
                        holder.fLocation.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("geo:"+model.getLocation()
                                        +"?q="+Uri.encode(model.getLocation())));
                                startActivity(intent);
                            }
                        });



                        // done item
                        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(View v) {
                                longPress();
                                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                                    @Override
                                    public boolean onLongClick(View v) {

                                        if(showState.equals("t") ) {
                                            //  model.setActive_statues("0");
                                            retrieveDataRef.child(category).child(model.getDesignID())
                                                    .child("Active_statues").setValue("0");
                                            if(model.getActive_statues().equals("0") ){
                                                holder.itemView.setVisibility(View.GONE);
                                                holder.itemView.setLayoutParams(new RecyclerView.LayoutParams(0, 0));
                                            }
                                            showState = "f";


                                        }
                                        return true;
                                    }
                                });

                                return true;
                            }
                        });
                    }

                    @Override
                    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fetch_single_order,parent,false);
                        return new ViewHolder(view);
                    }


                };
            }


            LinearLayoutManager lout = new LinearLayoutManager(OrderFetch.this);
            recyclerView.setLayoutManager(lout);
            firebaseRecyclerAdapter.startListening();
            recyclerView.setAdapter(firebaseRecyclerAdapter);




    }


    public void longPress(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(OrderFetch.this);
        builder.setCancelable(true);
        builder.setMessage("Are you sure that you want to mark it as done ?\n if yes long-press the item again");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               showState = "f";
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                showState = "t" ;
            }
        });
        AlertDialog leaveDialog = builder.create();
        leaveDialog.show();
    }

    @Override
    public void onBackPressed() {
        OrderFetch.this.finish();
        super.onBackPressed();
    }
}
