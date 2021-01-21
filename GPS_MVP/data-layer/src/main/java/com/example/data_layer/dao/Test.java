package com.example.data_layer.dao;

import com.example.data_layer.dao.retrofit.RetrofitUserDao;
import com.example.models.Trip;
import com.example.models.UserTrip;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080/demo/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        com.example.data_layer.dao.retrofit.RetrofitUserDao retrofitUserDao = retrofit
                .create(RetrofitUserDao.class);

        Call<List<UserTrip>> call = retrofitUserDao.getAllUsers();

        final List<UserTrip>[] users = new List[]{null};

        call.enqueue(new Callback<List<UserTrip>>() {
            @Override
            public void onResponse(Call<List<UserTrip>> call, Response<List<UserTrip>> response) {

                if(!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                users[0] = response.body();
                System.out.println(users[0]);
            }

            @Override
            public void onFailure(Call<List<UserTrip>> call, Throwable t) {
                System.out.println("Request failure");
            }
        });
        Thread.sleep(2000);

        UserTrip user = users[0].get(12);

        user.getTrips().add(new Trip("Test trip"));

        Call<UserTrip> call2 = retrofitUserDao.updateUser(user.getId(), user);

        call2.enqueue(new Callback<UserTrip>() {
            @Override
            public void onResponse(Call<UserTrip> call, Response<UserTrip> response) {
                if(!response.isSuccessful()) {
                    System.out.println("Code: " + response.code());
                    return;
                }

                UserTrip postResponse = response.body();
                System.out.println(postResponse);
            }

            @Override
            public void onFailure(Call<UserTrip> call, Throwable t) {
                System.out.println("Request failure");
            }
        });
    }

}
