package com.example.newssurfer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements item_clicked_callback {

    RecyclerView recyclerView;
    Adapter_For_RecyclerView adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewInitializer();

    }

    private void viewInitializer(){

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_For_RecyclerView(get_data(this), this);
        recyclerView.setAdapter(adapter);

    }

    private ArrayList<News> get_data(final Activity activity){

        String url = "https://newsapi.org/v2/top-headlines?country=us&apiKey=" + API_de.API_KEY;
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

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(activity, "Something went wrong", Toast.LENGTH_LONG).show();
            }
        });

        MySingleton.getInstance(activity).addToRequestQueue(jsonObjectRequest);
        return n;
    }

        @Override
    public void on_item_click(News i) {

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(i.news_url));

    }
}