//package com.example.data_layer.dao;
//
//import com.example.models.UserTrip;
//
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.BlockingQueue;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import retrofit2.Retrofit;
//import retrofit2.converter.gson.GsonConverterFactory;
//
//public class RetrofitUserDaoImpl implements UserDao {
//
//    private RetrofitUserDao retrofitUserDao;
//
//    private final String POST_USER_ERROR = "user_has_not_been_posted_to_server";
//
//    @Override
//    public List<UserTrip> getAllUsers() {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/demo/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        retrofitUserDao = retrofit
//                .create(RetrofitUserDao.class);
//
//        Call<List<UserTrip>> call = retrofitUserDao.getAllUsers();
//
//        final List<UserTrip>[] users = new List[]{null};
//
//        call.enqueue(new Callback<List<UserTrip>>() {
//            @Override
//            public void onResponse(Call<List<UserTrip>> call, Response<List<UserTrip>> response) {
//
//                if(!response.isSuccessful()) {
//                    System.out.println("Code: " + response.code());
//                    return;
//                } else {
//                    users[0] = response.body();
//                    System.out.println(users[0]);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<UserTrip>> call, Throwable t) {
//                System.out.println("Request failure");
//            }
//        });
//
//        return users[0];
//    }
//
////    @Override
////    public UserTrip getUserByName(String username) {
////        Retrofit retrofit = new Retrofit.Builder()
////                .baseUrl("http://10.0.2.2:8080/demo/")
////                .addConverterFactory(GsonConverterFactory.create())
////                .build();
////
////        retrofitUserDao = retrofit
////                .create(RetrofitUserDao.class);
////
////        Call<UserTrip> call = retrofitUserDao.getUserByName("MVP_TEST");
////
////        final UserTrip[] user = new UserTrip[1];
////
////        call.enqueue(new Callback<UserTrip>() {
////            @Override
////            public void onResponse(Call<UserTrip> call, Response<UserTrip> response) {
////                if(!response.isSuccessful()) {
////                    System.out.println("Code: " + response.code());
////                    return;
////                } else {
////                    user[0] = response.body();
////                    System.out.println(user[0]);
////                }
////            }
////
////            @Override
////            public void onFailure(Call<UserTrip> call, Throwable t) {
////                System.out.println("Request failure");
////            }
////        });
////
////        return user[0];
////    }
//
//
//    @Override
//    public BlockingQueue<UserTrip> getUserByName(String username) {
//        final BlockingQueue<UserTrip> blockingQueue = new ArrayBlockingQueue<>(1);
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/demo/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        retrofitUserDao = retrofit
//                .create(RetrofitUserDao.class);
//
//        Call<UserTrip> call = retrofitUserDao.getUserByName("MVP_TEST");
//
//        call.enqueue(new Callback<UserTrip>() {
//            @Override
//            public void onResponse(Call<UserTrip> call, Response<UserTrip> response) {
//                if(!response.isSuccessful()) {
//                    System.out.println("Code: " + response.code());
//                    return;
//                } else {
//                    UserTrip user = response.body();
//                    blockingQueue.add(user);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<UserTrip> call, Throwable t) {
//                System.out.println("Request failure");
//            }
//        });
//
//        return blockingQueue;
//    }
//
//    @Override
//    public String postUser(UserTrip user) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/demo/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        retrofitUserDao = retrofit
//                .create(RetrofitUserDao.class);
//
//        Call<UserTrip> call = retrofitUserDao.postUser(user);
//        final String[] stringResponse = {null};
//
//        call.enqueue(new Callback<UserTrip>() {
//            @Override
//            public void onResponse(Call<UserTrip> call, Response<UserTrip> response) {
//                if(!response.isSuccessful()) {
//                    System.out.println("Code: " + response.code());
//                    return;
//                }
//
//                UserTrip postUserResponse = response.body();
//                System.out.println(postUserResponse);
//
//                stringResponse[0] = response.toString();
//            }
//
//            @Override
//            public void onFailure(Call<UserTrip> call, Throwable t) {
//                System.out.println("Request failure");
//            }
//        });
//
//        return stringResponse[0];
//    }
//
//    @Override
//    public String updateUser(UserTrip user) {
//        return null;
//    }
//
//    @Override
//    public String updateUser(long id, UserTrip user) {
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://10.0.2.2:8080/demo/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        retrofitUserDao = retrofit
//                .create(RetrofitUserDao.class);
//
//        Call<UserTrip> call = retrofitUserDao.updateUser(user.getId(), user);
//        final String[] stringResponse = {null};
//
//        call.enqueue(new Callback<UserTrip>() {
//            @Override
//            public void onResponse(Call<UserTrip> call, Response<UserTrip> response) {
//                if(!response.isSuccessful()) {
//                    System.out.println("Code: " + response.code());
//                    return;
//                }
//
//                UserTrip postResponse = response.body();
//                System.out.println(postResponse);
//
//                stringResponse[0] = response.toString();
//            }
//
//            @Override
//            public void onFailure(Call<UserTrip> call, Throwable t) {
//                System.out.println("Request failure");
//            }
//        });
//
//        return stringResponse[0];
//    }
//}
