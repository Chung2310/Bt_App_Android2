package vn.edu.chungxangla.bt_app_android2;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Api {
    @GET("data")
    Call<String> getJsonData();

}
