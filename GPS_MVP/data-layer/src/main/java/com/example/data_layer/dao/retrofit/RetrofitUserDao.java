package com.example.data_layer.dao.retrofit;

import com.example.models.UserTrip;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RetrofitUserDao {

    @GET("allUserTrips")
    Call<List<UserTrip>> getAllUsers();

    @GET("allUserTrips/{username}")
    Call<UserTrip> getUserByName(@Path("username") String username);

    @POST("allUserTrips")
    Call<UserTrip> postUser(@Body UserTrip user);

    @PUT("allUserTrips/{id}")
    Call<UserTrip> updateUser(@Path("id") long id, @Body UserTrip user);

}
