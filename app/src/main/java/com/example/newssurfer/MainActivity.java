package com.example.newssurfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Header;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements item_clicked_callback {

    RecyclerView recyclerView;
    Adapter_For_RecyclerView adapter;
    Spinner add_filter;
    ArrayAdapter<String> arrayAdapter;
    ProgressBar loading;
    Activity a = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInitializer();

    }

    private void viewInitializer(){

        loading = (ProgressBar) findViewById(R.id.loading);

        add_filter = findViewById(R.id.add_filter);
        arrayAdapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, API_de.CATEGORY);
        add_filter.setAdapter(arrayAdapter);
        add_filter.setSelection(arrayAdapter.getPosition("business"));

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_For_RecyclerView(this);
        recyclerView.setAdapter(adapter);

        add_filter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                get_data(a, i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                get_data(a, 0);

            }
        });

    }


    private void get_data(final Activity activity, int category){

        loading.setVisibility(View.VISIBLE);

        String url = "https://newsapi.org/v2/top-headlines?country=us&category=" + API_de.CATEGORY[category] + "&apiKey=" + API_de.API_KEY;
        ArrayList<News> n = new ArrayList<>();

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {

                    JSONArray jsonArray = response.getJSONArray("articles");
                    for (int i = 0; i < jsonArray.length(); i++){

                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        News news = new News();
                        news.title = jsonObject.getString("title");
                        news.author = jsonObject.getString("author");
                        news.news_url = jsonObject.getString("url");
                        news.image_url = jsonObject.getString("urlToImage");
                        n.add(news);

                    }

                    adapter.update(n);
                    loading.setVisibility(View.INVISIBLE);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError{
                HashMap<String, String> headers = new HashMap<>();
                //headers.put("Content-Type", "application/json");
                headers.put("User-Agent", "Chrome/5.0");
                return headers;
            }
        };

        MySingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);

    }



    @Override
    public void on_item_click(News i) {

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(i.news_url));

    }
}