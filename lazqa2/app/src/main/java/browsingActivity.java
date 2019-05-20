import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Toast;

import com.example.me.lazqaapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class browsingActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    public final static String TAG = "browsingActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener dAuthListener ;
    private DatabaseReference myRef;
    private FirebaseDatabase mFireDataBase;
    private static String userID;
    private FirebaseUser user;
    private static String UserType ;
    private  static String testSt ;



    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browsing);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager =  findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        setupViewPager(mViewPager);

        TabLayout tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        mAuth = FirebaseAuth.getInstance();
        mFireDataBase = FirebaseDatabase.getInstance();
        myRef = mFireDataBase.getReference();
        user = mAuth.getCurrentUser();
        userID =user.getUid().toString();

       // Toast.makeText(this, userID, Toast.LENGTH_SHORT).show();




        /*
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
*/


        final FloatingActionButton fab =  findViewById(R.id.fab);
        fab.hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Intent intent = new Intent(browsingActivity.this,OrderFetch.class);
                browsingActivity.this.startActivity(intent);


            }
        });



        setupFirebaselistener();

// getting user type
        myRef.child("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //  Toast.makeText(browsingActivity.this, "hi to snap", Toast.LENGTH_SHORT).show();
                for(DataSnapshot dc :dataSnapshot.getChildren())
                {

                    if (dc.getKey().equals(userID) )
                    {

                        if(dc.child("user_type").getValue().equals("m")){

                            testSt=dc.child("name").getValue().toString();
                            UserType = "manger";
                          //  Toast.makeText(browsingActivity.this, "hello "+testSt, Toast.LENGTH_SHORT).show();
                          //  Intent intent=new Intent(browsingActivity.this,add_activity.class);
                          //  browsingActivity.this.startActivity(intent);
                            fab.show();


                        }
                        else if (dc.child("user_type").getValue().equals("c"))
                        {
                            testSt= dc.child("name").getValue().toString();
                            UserType = "costumer";
                         //   Toast.makeText(browsingActivity.this, "hello "+testSt, Toast.LENGTH_SHORT).show();
                         //   openDialog("add");

                        }

                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(browsingActivity.this, "failed to get data", Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void setupViewPager (ViewPager viewPager){
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Tab1TopWear(),"top Wear");
        adapter.addFragment(new Tab2LaptopCovers(),"laptop covers");
        adapter.addFragment(new Tab3Mugs(),"mugs");
        adapter.addFragment(new Tab4Other(),"others");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.menu_browsing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(this, "in future update's", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (id == R.id.action_logout) {
            FirebaseAuth.getInstance().signOut();

            return true;
        }
        if (id == R.id.add_btn) {

            Toast.makeText(browsingActivity.this, "hello "+testSt, Toast.LENGTH_SHORT).show();

            if(UserType.equals("manger"))
            {

                Intent intent=new Intent(browsingActivity.this,add_activity.class);
                browsingActivity.this.startActivity(intent);

            }

            else if (UserType.equals("costumer")){


                  openDialog("add");

            }
            else {
                Toast.makeText(browsingActivity.this, "loading info ...", Toast.LENGTH_SHORT).show();
            }



            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void openDialog(String dir) {
       /* Intent intent = new Intent(browsingActivity.this, add_activity.class);
        browsingActivity.this.startActivity(intent);
*/
        Choice go = new Choice();
        if (dir.equals("add")) {
            go.show(getSupportFragmentManager(), "T-shirt");
        }
    }





    public void setupFirebaselistener(){

        dAuthListener = new FirebaseAuth.AuthStateListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
           FirebaseUser user=firebaseAuth.getCurrentUser();
           if(user!= null){
               Log.d("onAuthStateChanged: signed_in",user.getUid());
           }
           else{
               Toast.makeText(browsingActivity.this, "signed out", Toast.LENGTH_SHORT).show();
           Intent intent = new Intent(browsingActivity.this,MainActivity.class);
           intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
           browsingActivity.this.startActivity(intent);

           }

            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(dAuthListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(dAuthListener);
    }

   /* *//**
     * A placeholder fragment containing a simple view.
     *//*
    public static class PlaceholderFragment extends Fragment {
        *//**
     * The fragment argument representing the section number for this
     * fragment.
     *//*
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        *//**
     * Returns a new instance of this fragment for the given section
     * number.
     *//*
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }




        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.tab1_topwear, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    *//**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     *//*
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }
    }*/
    public void onBackPressed(){
        final AlertDialog.Builder builder = new AlertDialog.Builder(browsingActivity.this);
        builder.setCancelable(true);
        builder.setMessage("Are you sure that you want to exit ?");
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
             browsingActivity.this.finish();
            }
        });
    AlertDialog leaveDialog = builder.create();
    leaveDialog.show();
    }


}
