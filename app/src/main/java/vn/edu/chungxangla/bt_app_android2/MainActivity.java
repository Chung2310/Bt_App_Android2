package vn.edu.chungxangla.bt_app_android2;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    AppCompatButton button;
    TextView id,title,message;
    Gson gson;
    MessageModule messageModule = new MessageModule();
    String json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        anhXa();

        gson = new Gson();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://tranghome.duckdns.org/api/node-red/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Api apiService = retrofit.create(Api.class);
                Call<String> call = apiService.getJsonData();

                call.enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            String jsonResponse = response.body();
                            System.out.println("JSON: " + jsonResponse);
                            Log.d("json",jsonResponse);
                            // Chuyển đổi JSON thành MessageModule
                            messageModule = gson.fromJson(jsonResponse, MessageModule.class);

                            // Cập nhật giao diện
                            if (messageModule != null) {
                                id.setText(messageModule.id);
                                title.setText(messageModule.title);
                                message.setText(messageModule.message);
                            }
                        } else {
                            System.err.println("Response is empty or unsuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable t) {
                        System.err.println("Error: " + t.getMessage());
                    }
                });
            }
        });
    }

    private void anhXa() {
        button = findViewById(R.id.button);
        id = findViewById(R.id.id1);
        title = findViewById(R.id.title1);
        message = findViewById(R.id.message1);
    }

}