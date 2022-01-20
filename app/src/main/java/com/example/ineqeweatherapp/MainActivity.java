package com.example.ineqeweatherapp;

/**
 * Author David Houston.
 */

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * The purpose of this class is to act as the starting point for the application.
 * When called this app will launch to an empty field, where the user can the enter an area of interest
 * This data is then captured & sent to the API to retrieve the relevant results.
 */
public class MainActivity extends AppCompatActivity {

    //Variables
    private TextView tvLocation, tvDate, tvTime, tvCurrentTemp, tv_maxTemp, tv_lowTemp, tv_sunRise, tv_sunSet;
    private RecyclerView rvWeather;
    private Calendar calendar;
    private SimpleDateFormat dateFormat, timeFormat;
    private String date, time, city, currIcon;
    private ArrayList<RVModelWeather> weatherRVArrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private RequestQueue requestQueue;
    private EditText searchData;
    private ImageView currentWeatherIcon;

    //API key for connecting to OpenWeatherMap API.
    private final String apiKey = "b6c77678b24354bebc2dbf01e6ce5b43";

    //Location access Permission Code.
    private final int PERMISSION_CODE = 101;
    DecimalFormat df = new DecimalFormat("#.##");

    /**
     * Start method, this method is called upon the Activity launching.
     * Assign variables to XML id's,
     * Format Date & time responses.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");
        timeFormat = new SimpleDateFormat("h:mm a");

        //Get the current date & time.
        date = dateFormat.format(calendar.getTime());
        time = timeFormat.format(calendar.getTime());

        //Assign UI id's to Vars.
        tvLocation = findViewById(R.id.tv_location);
        tvDate = findViewById(R.id.tv_date);
        tvTime = findViewById(R.id.tv_time);
        rvWeather = findViewById(R.id.rv_fiveDayWeather);
        tvCurrentTemp = findViewById(R.id.tv_currentTemp);
        searchData = findViewById(R.id.et_dataInput);
        tv_maxTemp = findViewById(R.id.grids_highTempValue);
        tv_lowTemp = findViewById(R.id.grids_lowTempValue);
        tv_sunRise = findViewById(R.id.grids_sunriseValue);
        tv_sunSet = findViewById(R.id.grids_sunsetValue);
        currentWeatherIcon = findViewById(R.id.ib_currentWeather);

        //Instantiating new Arraylist of Weather models 'RVModelWeather.class'
        weatherRVArrayList= new ArrayList();

        //Creating adapter using the ArrayList & Inflating before setting.
        weatherRVAdapter = new WeatherRVAdapter(MainActivity.this, weatherRVArrayList);
        rvWeather.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rvWeather.setAdapter(weatherRVAdapter);

        //Set current date & time to UI elements.
        tvDate.setText(date);
        tvTime.setText(time);

        //Creating the Volley Request queue
        requestQueue =  Volley.newRequestQueue(this);


        //When the user enters text into the edit text at the top of the application, upon
        //Pressing the Tick, run the 'getWeatherInfo' method.
        //This could be better implemented.
        searchData.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    getWeatherInfo();
                }
                return false;
            }
        });

    }


    /**
     * This method is used to connect to the API and retrieve the data in JSON format.
     */
   public void getWeatherInfo() {

       //Api URL, this could be implemented to be more dynamic and not stored within a public class.
       String url = "https://api.openweathermap.org/data/2.5/forecast?q=" +searchData.getText().toString()+ "&appid="+apiKey+"&cnt=40&units=metric";

       //JSON Request
       JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
           @Override
           public void onResponse(JSONObject response) {
               try {
                   //Clear the Arraylist so not to reload on top of existing results.
                   weatherRVArrayList.clear();

                   //Pull data for current day
                   JSONArray array = response.getJSONArray("list");
                   JSONObject mainObj = array.getJSONObject(0);
                   JSONObject weatherDetails = mainObj.getJSONObject("main");
                   JSONArray weatherIcon = mainObj.getJSONArray("weather");
                   JSONObject weaIcn = weatherIcon.getJSONObject(0);
                   String weatherIcn = weaIcn.getString("icon");
                   String iconUrl = "https://openweathermap.org/img/wn/"+weatherIcn+"@2x.png";

                   //Pull data for 5 day forecast (Api pulls data for every 3 hours (8 per day * 5 = 40 / 5 = 8)
                   for (int i = 0; i < array.length(); i+=8) {

                       JSONObject object = array.getJSONObject(i);
                       JSONObject jArr = object.getJSONObject("main");
                       JSONArray wArr = object.getJSONArray("weather");
                       JSONObject weeIco = wArr.getJSONObject(0);

                       JSONObject windObj = object.getJSONObject("wind");
                       int windSpeed = windObj.getInt("speed");
                       int windDir = windObj.getInt("deg");
                       String windDeg = calculateWindDirection(windDir);

                       int newDate = object.getInt("dt");
                       Date dayDate = new Date (newDate* 1000L);
                       SimpleDateFormat sdfrDay = new SimpleDateFormat("EEE");
                       String dayDateStr = sdfrDay.format(dayDate);
                       String date = dayDateStr;

                       int temp = jArr.getInt("temp");
                       int min = jArr.getInt("temp_min");
                       int max = jArr.getInt("temp_max");
                       String ico = weeIco.getString("icon");

                       //Add data to new weather model & put into ArrayList.
                       weatherRVArrayList.add(new RVModelWeather(date,max,min,temp,ico,windSpeed,windDeg));
                   }

                   int currTemp = weatherDetails.getInt("temp");
                   int minTemp = weatherDetails.getInt("temp_min");
                   int maxTemp = weatherDetails.getInt("temp_max");
                   Log.d("JSONRESP", "TEMP: "+String.valueOf(maxTemp));
                   tvCurrentTemp.setText(String.valueOf(currTemp + "\u2103"));
                   tv_lowTemp.setText(String.valueOf(minTemp + "\u2103"));
                   tv_maxTemp.setText(String.valueOf(maxTemp + "\u2103"));
                   Glide.with(MainActivity.this).load(iconUrl).into(currentWeatherIcon);

                   JSONObject cityArray = response.getJSONObject("city");
                   String cityName = cityArray.getString("name");
                   int sunrise = cityArray.getInt("sunrise");
                   int sunset = cityArray.getInt("sunset");
                   tvLocation.setText(cityName);

                   Date sunriseDate = new Date(sunrise* 1000L);
                   SimpleDateFormat sdfr = new SimpleDateFormat("HH:mm:a");
                   String formattedSunRise = sdfr.format(sunriseDate);
                   tv_sunRise.setText(formattedSunRise);

                   Date sunsetDate = new Date(sunset* 1000L);
                   SimpleDateFormat sdfs = new SimpleDateFormat("HH:mm:a");
                   String formattedSunSet = sdfs.format(sunsetDate);
                   tv_sunSet.setText(String.valueOf(formattedSunSet));

               } catch (JSONException e) {
                   e.printStackTrace();
               }

               weatherRVAdapter = new WeatherRVAdapter(MainActivity.this, weatherRVArrayList);
               rvWeather.setLayoutManager(new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false));
               rvWeather.setAdapter(weatherRVAdapter);
           }
       }, new Response.ErrorListener() {
           @Override
           public void onErrorResponse(VolleyError error) {
               error.printStackTrace();
           }
       });

       //Add to queue.
       requestQueue.add(request);

   }

    /**
     * Calculate the direction of the Wind (N S E W etc)
     * Based on degree value.
     * @param windDegree the value passed in to assign a direction to the Wind.
     */
   public String calculateWindDirection(int windDegree) {

        String[] directions = {"N", "NE", "E", "SE", "S", "SW", "W", "NW" };

        windDegree = windDegree * 8 / 360;
        windDegree = Math.round(windDegree);

        windDegree = (windDegree + 8) % 8;

       return directions[windDegree];


   }

    /**
     * UNUSED METHOD
     * To get current city from Lat & long from current location
     * Using Geocoder.
     * @param lat - Latitude of given location
     * @param lng - Longitude of given location.
     */
   public void getCityName(double lat, double lng) {
        String cityName = "";
       Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
       try {
           List<Address> addresses = geocoder.getFromLocation(lat,lng, 10);

           for (Address adr : addresses) {
               if (adr != null) {
                   String city = adr.getLocality();
                   if(city != null && city.equals("")) {
                       cityName = city;
                   } else {
                       Toast.makeText(this, "City Not Found", Toast.LENGTH_SHORT).show();
                   }
               }
           }
       } catch (IOException e) {
           e.printStackTrace();
       }

   }

}