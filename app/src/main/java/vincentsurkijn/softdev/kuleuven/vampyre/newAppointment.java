package vincentsurkijn.softdev.kuleuven.vampyre;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

public class newAppointment extends AppCompatActivity implements OnMapReadyCallback{
    private MapView mapView;
    private GoogleMap gmap;

    private static final String MAP_VIEW_BUNDLE_KEY = "MapViewBundleKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAP_VIEW_BUNDLE_KEY);
        }

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(mapViewBundle);
        mapView.getMapAsync(this);

        final Button dateButton = findViewById(R.id.timeButton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openagenda();
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAP_VIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAP_VIEW_BUNDLE_KEY, mapViewBundle);
        }

        mapView.onSaveInstanceState(mapViewBundle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        gmap = googleMap;
        //focus on belgium
        gmap.setMinZoomPreference(7);
        LatLng belgium = new LatLng(50.645922, 4.635408);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(belgium));

        //import locations from server
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "https://studev.groept.be/api/a18_sd209/APP_importLocations";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            JSONObject location = null;
                            try {
                                location = response.getJSONObject(i);
                                String locationName = location.getString("name");
                                double lattitude = location.getDouble("lattitude");
                                double longitude = location.getDouble("longitude");
                                LatLng coordinate = new LatLng(lattitude, longitude);
                                gmap.addMarker(new MarkerOptions()
                                        .position(coordinate).title(locationName)
                                        .draggable(false).visible(true));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                final TextView errorText= findViewById(R.id.textView6);
                errorText.setText(getResources().getText(R.string.nointernet));
                errorText.setVisibility(View.VISIBLE);
                final ScrollView scr = findViewById(R.id.scrv);
                scr.setVisibility(View.GONE);
            }
        });
        mQueue.add(request);

        gmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                final TextView location = findViewById(R.id.locationDisplay);
                location.setText(marker.getTitle());
                return true;
            }
        });
    }

    private void openagenda(){
        //instantiate the popup.xml layout file
        LayoutInflater layoutInflater = (LayoutInflater) newAppointment.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupview = layoutInflater.inflate(R.layout.popup_agenda,null);

        ArrayList<String> hourArray = new ArrayList<>();
        hourArray.add("12:00");
        hourArray.add("13:00");

        ListView hourlist = popupview.findViewById(R.id.hourList);
        ArrayAdapter arrayAdapter = new ArrayAdapter(newAppointment.this, android.R.layout.simple_list_item_1, hourArray);
        hourlist.setAdapter(arrayAdapter);

        //instantiate popup window
        final PopupWindow popupWindow = new PopupWindow(popupview, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

        //display the popup window
        ConstraintLayout constrain = findViewById(R.id.constrain);
        popupWindow.showAtLocation(constrain, Gravity.CENTER, 0, 0);

        hourlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                popupWindow.dismiss();
            }
        });
    }
}
