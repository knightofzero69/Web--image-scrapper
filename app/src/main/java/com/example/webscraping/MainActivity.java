package com.example.webscraping;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button fetch;
    RecyclerView recyclerView;
    Adapter adapter;
    ArrayList<String> url_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editTextTextPersonName);
        fetch = findViewById(R.id.button);
        recyclerView = findViewById(R.id.recycler_view);

        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = editText.getText().toString();

                if (url != null){
                    Myasynck som = new Myasynck();
                    som.execute(url);

                }
                else{
                    Toast.makeText(MainActivity.this, "URL is not valid", Toast.LENGTH_SHORT).show();

                }

            }
        });


    }

    class Myasynck extends AsyncTask<String, Void,String>{


        @Override
        protected String doInBackground(String... strings) {

            url_list = new ArrayList<>();
            Document doc = null;

            try {
                doc = Jsoup.connect(strings[0]).get();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("ERROR",e.getLocalizedMessage());
            }

            if (doc !=null){

                Elements img = doc.getElementsByTag("img");
                for (Element e1 : img){
                    String src = e1.absUrl("src");
                    url_list.add(src);
                }
            }else{
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(MainActivity.this,"Can't connect to this website",Toast.LENGTH_SHORT).show();
                    }
                });

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new Adapter(url_list, MainActivity.this);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
            recyclerView.setAdapter(adapter);
        }
    }
}