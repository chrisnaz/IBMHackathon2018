package ibm.hackathon.a2018.sabamibmhackathon;


import android.content.Intent;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import domaine.Event;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PlaylistInfo extends AppCompatActivity {
    private ListView playlist;
    private EditText locationEditText;
    private EditText date;
    private EditText artist;
    private Button confirm;
    private Event e;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist_info);
        initVieuw();
    }


    public void initVieuw() {
        Intent intent = getIntent();
        String resultOcr = intent.getStringExtra("resultOcr");
        playlist = findViewById(R.id.playlist);
        locationEditText = findViewById(R.id.location);
        date = findViewById(R.id.date);
        artist = findViewById(R.id.artiste);
        confirm = findViewById(R.id.confirme);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendPlaylist();
            }
        });
        e = new Event();
        e.createPlaylist(resultOcr);

        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, e.getPlaylist());

        playlist.setAdapter(arrayAdapter);
        locationEditText.setText(e.getLocation());
        date.setText(e.getDate());
        artist.setText(e.getArtist());

    }

    public void sendPlaylist() {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("artist", artist.getText().toString()).add("location", locationEditText.getText().toString()).add("playlist",e.getPlaylist().toString()).add("date",e.getDate()).build();

        Request request = new Request.Builder().url("https://hacksabam.eu-gb.mybluemix.net/api/playlist").post(formBody)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (!response.isSuccessful()) {
                    Log.d("response", response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PlaylistInfo .this,"Error!",Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {

                    Log.d("response", response.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PlaylistInfo.this," Success!",Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(PlaylistInfo.this, TakePicture.class));
                        }
                    });
                }
            }
        });

    }
}

