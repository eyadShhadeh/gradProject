import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.me.lazqaapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private final static String TAG = "MapActivity";
    private final static String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private final static String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private final static int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private final static float DEFULT_ZOOM = 15f;

    //vars
    private boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private EditText mSearchText ;

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "mapReady : we at mapReady");
        mMap = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();
            if (ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
              return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setZoomControlsEnabled(true);
            init();

        }

    }
    private void geoLocate(){
        Log.d(TAG, "geoLocate: geolocating");

        String searchString = mSearchText.getText().toString();

        Geocoder geocoder = new Geocoder(MapActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            Log.e(TAG, "geoLocate: IOException: " + e.getMessage() );
        }

        if(list.size() > 0){
            Address address = list.get(0);

            Log.d(TAG, "geoLocate: found a location: " + address.toString());
            //Toast.makeText(this, address.toString(), Toast.LENGTH_SHORT).show();

        }
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mSearchText = findViewById(R.id.input_search);
        getLocationPermission();
      //  init();

    }
    private void init(){
        Log.d(TAG, "init: initializing");

        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId == EditorInfo.IME_ACTION_DONE
                        || actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == event.KEYCODE_ENTER
                        || actionId == event.ACTION_DOWN){
                    Toast.makeText(MapActivity.this, "done", Toast.LENGTH_SHORT).show();
                geoLocate();}
                return false;
            }
        });

    }

    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
private void getDeviceLocation (){
    Log.d(TAG,"getDeviceLocation : getting the location of the device ");
    mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    try
    {
        if(mLocationPermissionsGranted)
        {
            Task location = mFusedLocationProviderClient.getLastLocation();

            location.addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task)
            {
                if(task.isSuccessful())
                {
                    Log.d(TAG,"getDeviceLocation (onComplete) : Location found ");
                    Location currentLocation = (Location) task.getResult();
                    moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),DEFULT_ZOOM);

                }
                else
                    {
                    Log.d(TAG,"getDeviceLocation (onComplete) : unable to get current Location ");
                    Toast.makeText(MapActivity.this, "unable to get current Location"
                            , Toast.LENGTH_SHORT).show();
                    }
            }
        });

        }
    }

    catch (SecurityException e){
        Log.d(TAG,"getDeviceLocation : SecurityException accrued"+e.getMessage());
    }

}
private void moveCamera(LatLng latLng,float zoom){
    Log.d(TAG,"moveCamera : moving to lang :"+latLng.latitude+"and lng: "+latLng.longitude);
    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
}
    public void initMap(){

        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);


    }
    @Override
    public void onRequestPermissionsResult(int requestCode
            , @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionsGranted =false;

        switch (requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                for (int i=0 ; i <grantResults.length;i++){
                if (grantResults[0]!=PackageManager.PERMISSION_GRANTED){

                    mLocationPermissionsGranted = false ;
                    return;

                    }
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }


}
