package com.example.piadas;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private Retrofit retrofit;
    private TextView textPiada;
    private Button btnBuscar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrofit = new Retrofit.Builder().baseUrl("https://xkcd.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        textPiada = findViewById(R.id.textPiada);
        btnBuscar = findViewById(R.id.btnBuscar);
        imageView = findViewById(R.id.imageView);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int x = new Random().nextInt(2298);
                String codigo = "/" + x;
                buscaPiada(codigo);
            }
        });
        buscaPiada("");
    }

    public void buscaPiada(String codigo) {
        PiadaService piadaService = retrofit.create(PiadaService.class);

        Call<PiadaModel> call = piadaService.buscarPiada(codigo);

        call.enqueue(new Callback<PiadaModel>() {
            @Override
            public void onResponse(Call<PiadaModel> call, Response<PiadaModel> response) {
                if (response.isSuccessful()) {
                    PiadaModel piada = response.body();
                    Log.i("TESTE", piada.getDay());
                    textPiada.setText(piada.getTitle());
                    BuscaIMG buscaIMG = new BuscaIMG();
                    buscaIMG.execute(piada.getImg());
                }
            }

            @Override
            public void onFailure(Call<PiadaModel> call, Throwable t) {
            }
        });



    }



    public class BuscaIMG extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            String urlImg = strings[0];
            Bitmap img = null;

            try {
                URL url = new URL(urlImg);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream input = connection.getInputStream();
                img = BitmapFactory.decodeStream(input);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return img;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            imageView.setImageBitmap(bitmap);
        }
    }

}
