package com.example.data_layer.dao;

import com.example.models.UserTrip;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImplRealDevice implements UserDao {

    private static HttpURLConnection connection;

    private final String IPv4 = "192.168.100.101";

    private final String POST_USER_ERROR = "user_has_not_been_posted_to_server";

//    public static void main(String[] args) {
//        try {
//            String ip = String.valueOf(InetAddress.getLocalHost()).split("/")[1];
//            System.out.println(ip);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }
//    }

    @Override
    public List<UserTrip> getAllUsers() {
        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();
        List<UserTrip> userTripList = new ArrayList<>();

        try {

//            String IPv4 = String.valueOf(InetAddress.getLocalHost()).split("/")[1];
            URL url = new URL("http://" + IPv4 + ":8080/demo/allUserTrips");
            connection = (HttpURLConnection) url.openConnection();

            // Request Setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);

            int status = connection.getResponseCode();

            if(status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            while((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

            ObjectMapper mapper = new ObjectMapper();
            CollectionType listType = mapper.getTypeFactory().constructCollectionType(ArrayList.class, UserTrip.class);
            userTripList = mapper.readValue(responseContent.toString(), listType);
            return userTripList;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("First catch");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Second catch");


            return userTripList;
        } finally {
            connection.disconnect();
        }

        return userTripList;
    }

    @Override
    public UserTrip getUserByName(String username) {

        BufferedReader reader;
        String line;
        StringBuilder responseContent = new StringBuilder();
        UserTrip userTrip = null;

        try {

//            String IPv4 = String.valueOf(InetAddress.getLocalHost()).split("/")[1];
            URL url = new URL("http://" + IPv4 + ":8080/demo/allUserTrips/" + username);
            connection = (HttpURLConnection) url.openConnection();

            // Request Setup
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);

            int status = connection.getResponseCode();

            if(status > 299) {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            while((line = reader.readLine()) != null) {
                responseContent.append(line);
            }
            reader.close();

            userTrip = new ObjectMapper().readValue(responseContent.toString(), UserTrip.class);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("First catch");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Second catch");

            return new UserTrip("error");
        } finally {
            connection.disconnect();
        }
        return userTrip;
    }

    @Override
    public String postUser(UserTrip user) {

        StringBuilder response = new StringBuilder();

        try {

//            String IPv4 = String.valueOf(InetAddress.getLocalHost()).split("/")[1];
            URL url = new URL("http://" + IPv4 + ":8080/demo/allUserTrips");
            connection = (HttpURLConnection) url.openConnection();

            // Request Setup
            connection.setRequestMethod("POST");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            // ?
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String requestBody = new ObjectMapper().writeValueAsString(user);

            OutputStream os = connection.getOutputStream();
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("First catch");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Second catch");

            return POST_USER_ERROR;
        } finally {
            connection.disconnect();
        }

        return response.toString();
    }

    @Override
    public String updateUser(long id, UserTrip user) {
        return null;
    }

    @Override
    public String updateUser(UserTrip user) {

        StringBuilder response = new StringBuilder();

        try {

//            String IPv4 = String.valueOf(InetAddress.getLocalHost()).split("/")[1];
            URL url = new URL("http://" + IPv4 + ":8080/demo/allUserTrips/" + user.getId());
            connection = (HttpURLConnection) url.openConnection();

            // Request Setup
            connection.setRequestMethod("PUT");
            connection.setConnectTimeout(20000);
            connection.setReadTimeout(20000);
            connection.setRequestProperty("Content-Type", "application/json; utf-8");
            // ?
            connection.setRequestProperty("Accept", "application/json");
            connection.setDoOutput(true);

            String requestBody = new ObjectMapper().writeValueAsString(user);

            OutputStream os = connection.getOutputStream();
            byte[] input = requestBody.getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));
            String responseLine;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            System.out.println(response.toString());

        } catch (MalformedURLException e) {
            e.printStackTrace();
            System.out.println("First catch");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Second catch");
        } finally {
            connection.disconnect();
        }

        return response.toString();
    }
}

