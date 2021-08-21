package com.example.memetest;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.view.View;
import android.widget.*;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.*;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button share,next;
    ImageView memeimage;
    ProgressBar progressBar;
    String shareurl;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        share=findViewById(R.id.share);
        next=findViewById(R.id.next);
        memeimage=findViewById(R.id.memeimage);
        progressBar=findViewById(R.id.progressBar);
        load();

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                load();
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"dekhle meme vro "+shareurl);
                Intent chooser=Intent.createChooser(intent,"huihui.... ");
                startActivity(chooser);
            }
        });

    }

    private void load(){
        progressBar.setVisibility(View.VISIBLE);
        //RequestQueue queue = Volley.newRequestQueue(this);

        String url ="https://meme-api.herokuapp.com/gimme";
        //String url="https://api.imgflip.com/get_memes";

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null,

                        new Response.Listener<JSONObject>() {
                            public void onResponse(JSONObject response) {
                                try {
                                    String url=response.getString("url");
                                    shareurl=url;

                                    Glide
                                            .with(MainActivity.this)
                                            .load(url)
                                            .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed( GlideException e,
                                                                     Object model,
                                                                     Target<Drawable> target,
                                                                     boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource,
                                                                       Object model,
                                                                       Target<Drawable> target,
                                                                       DataSource dataSource,
                                                                       boolean isFirstResource) {
                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    }).into(memeimage);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },

                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this,"nhi hua",Toast.LENGTH_SHORT).show();
                            }
                        });
        //queue.add(jsonObjectRequest);
        singleton.getInstance(this).addToRequestQueue(jsonObjectRequest);
    }
}