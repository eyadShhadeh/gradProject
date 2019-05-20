import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.me.lazqaapp.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;


public class Tab2LaptopCovers extends Fragment {

    private RecyclerView recyclerView ;
    private DatabaseReference retrieveDataRef;
    private FirebaseDatabase fbase;

    private FirebaseRecyclerAdapter<cardItem,ViewHolder> firebaseRecyclerAdapter;
    private FirebaseRecyclerOptions options;


    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container,
                              Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab1_topwear, container, false);

        recyclerView= rootView.findViewById(R.id.list_topWear);

        fbase = FirebaseDatabase.getInstance();
        retrieveDataRef = fbase.getReference().child("design").child("laptop_cover");

        options = new FirebaseRecyclerOptions.Builder<cardItem>().setQuery( retrieveDataRef,cardItem.class).build();

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<cardItem, ViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull final cardItem model) {



                holder.post_title.setText(model.getTitle());
                holder.post_price.setText(model.getPrice());
                Picasso.with(getContext()).load(model.getImage()).fit().into(holder.post_image);

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(getContext(), "Bingo "+position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),LapTopDesign.class);
                        intent.putExtra("img_url",model.getImage());
                        intent.putExtra("img_price",model.getPrice());
                        getContext().startActivity(intent);
                        getActivity().finish();

                    }
                });

            }

            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_item,parent,false);
                return new ViewHolder(view);
            }


        };

        LinearLayoutManager lout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(lout);
        firebaseRecyclerAdapter.startListening();
        recyclerView.setAdapter(firebaseRecyclerAdapter);

        return rootView;
    }
}