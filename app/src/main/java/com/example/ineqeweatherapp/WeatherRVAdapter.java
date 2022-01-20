package com.example.ineqeweatherapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Adapter class for recycler view for populate 5 day forecast.
 */
public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.ViewHolder> {


    private Context context;
    private ArrayList<RVModelWeather> modelWeatherArrayList;

    public WeatherRVAdapter(Context context, ArrayList<RVModelWeather> modelWeatherArrayList) {
        this.context = context;
        this.modelWeatherArrayList = modelWeatherArrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_card_model,parent,false);
        return new ViewHolder(view);
    }

    /**
     * Adding the values from API to each item in position.
     * Using GLide library to pull icons.
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter.ViewHolder holder, int position) {

        RVModelWeather modelWeather = modelWeatherArrayList.get(position);

        holder.tvMaxTemp.setText(String.valueOf(modelWeather.getMaxTemp()+ "\u2103"));
        holder.tvMinTemp.setText(String.valueOf(modelWeather.getMinTemp()+ "\u2103"));
        holder.tvTemp.setText(String.valueOf(modelWeather.getTemperature()+ "\u2103"));
        holder.tvDay.setText(modelWeather.getDay());
        Glide.with(context).load("https://openweathermap.org/img/wn/"+modelWeather.getIcon()+"@2x.png").into(holder.ivWeatherIcon);
        holder.tvWind.setText(String.valueOf(modelWeather.getWindSpeed() + "MPH : " + modelWeather.getWindDegree()));

    }

    /**
     * Get the total count of items in recyclerView.
     * @return
     */
    @Override
    public int getItemCount() {
        return modelWeatherArrayList.size();
    }

    /**
     *     Assign XML views to each itemView in recyclerView.
     */
    public class ViewHolder extends RecyclerView.ViewHolder {

         private TextView tvDay, tvTemp, tvMinTemp, tvMaxTemp, tvWind;
         private ImageView ivWeatherIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvDay = itemView.findViewById(R.id.tv_day);
            tvTemp = itemView.findViewById(R.id.tv_temp);
            tvMinTemp = itemView.findViewById(R.id.minTemp_value);
            tvMaxTemp = itemView.findViewById(R.id.maxTemp_value);
            tvWind = itemView.findViewById(R.id.wind_value);
            ivWeatherIcon = itemView.findViewById(R.id.ib_currentWeather);
        }
    }

}
