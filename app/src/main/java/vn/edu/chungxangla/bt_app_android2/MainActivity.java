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

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    AppCompatButton button;
    TextView id,iduser,title,message;
    Gson gson;
    MessageModule messageModule = new MessageModule();
    String json;
    int dem=0;
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
                // Tạo Retrofit instance
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://jsonplaceholder.typicode.com/posts/") // URL chính xác của API
                        .addConverterFactory(GsonConverterFactory.create()) // Dùng Gson để parse JSON
                        .build();

                // Tạo service từ Retrofit
                Api apiService = retrofit.create(Api.class);

                // Gọi API - Lấy danh sách MessageModule
                Call<List<MessageModule>> call = apiService.getJsonData(); // Sửa Call<List<MessageModule>>

                // Thực hiện enqueue
                call.enqueue(new Callback<List<MessageModule>>() {
                    @Override
                    public void onResponse(Call<List<MessageModule>> call, Response<List<MessageModule>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            List<MessageModule> messageList = response.body();

                            // Hiển thị dữ liệu phần tử đầu tiên nếu danh sách không rỗng
                            if (!messageList.isEmpty()) {
                                MessageModule messageModule = messageList.get(dem); // Lấy bài viết đầu tiên
                                id.setText(String.valueOf(messageModule.getId()));
                                iduser.setText(String.valueOf(messageModule.getUserId()));
                                title.setText(messageModule.getTitle());
                                message.setText(messageModule.getBody());
                                dem++;
                            }
                        } else {
                            System.err.println("Response is empty or unsuccessful");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<MessageModule>> call, Throwable t) {
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
        iduser = findViewById(R.id.iduser1);
    }

}