package com.example.foobar.webApi;

import com.example.foobar.entities.Post_Item;
import com.example.foobar.entities.TokenRes;
import com.example.foobar.entities.User_Item;
import com.google.gson.JsonObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WebServiceAPI {

    @POST("tokens")
    @FormUrlEncoded
    Call<TokenRes> createToken(
            @Field("username") String username,
            @Field("password") String password
    );


    @POST("users")
    Call<User_Item> createUser(@Body User_Item user);

    @GET("users/{id}")
    Call<User_Item> getUser(
            @Path("id")  String username,
            @Header("Authorization") String authToken );


    //that's a problem to update in this wayyyy!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @PATCH("users/{id}")
    @FormUrlEncoded
    Call<User_Item> updateUser(
            @Path("id") String username,
            @Field("fieldName") String fieldName,
            @Field("fieldValue") String fieldValue,
            @Header("Authorization") String authToken );



    @DELETE("users/{id}")
    Call<Void> deleteUser(
            @Path("id") String username,
            @Header("Authorization") String authToken);



    @GET("users/{id}/friends")
    Call<JsonObject> getUserFriends(
            @Path("id") String username,
            @Header("Authorization") String authToken);

    @GET("users/{id}/friends-requests")
    Call<JsonObject> getFriendRequests(
        @Path("id") String username,
        @Header("Authorization") String authToken);




    @POST("users/{id}/friends")
    Call<Void> addFriendReq(
            @Path("id") String username,
            @Header("Authorization") String authToken);


    @PATCH("users/{receiverId}/friends/{senderId}")
    Call<Void> acceptReq(
            @Path("senderId") String senderId,
            @Path("receiverId") String receiverId,
            @Header("Authorization") String authToken
    );


    @DELETE("users/{id}/friends/{fid}")
    Call<Void> deleteFriend(
            @Path("id") String user,
            @Path("fid") String friend,
            @Header("Authorization") String authToken
    );




    @POST("users/{id}/posts")
    @FormUrlEncoded
    Call<Post_Item> createPost(
            @Path("id") String username,
            @Field("text") String text,
            @Field("picture") String picture,
            @Header("Authorization") String authToken);

    @GET("users/{id}/posts")
    Call<List<Post_Item>> getUserPosts(
            @Path("id") String username,
            @Header("Authorization") String authToken
    );


    @DELETE("users/{id}/posts/{pid}")
    Call<Post_Item> deletePost(
            @Path("id") String username,
            @Path("pid") String postId,
            @Header("Authorization") String authToken
    );

    @PATCH("users/{id}/posts/{pid}")
    @FormUrlEncoded
    Call<Post_Item> updatePost(
            @Path("id") String username,
            @Path("pid") String postId,
            @Field("fieldName") String fieldName,
            @Field("fieldValue") String fieldValue,
            @Header("Authorization") String authToken
    );

    @GET("posts")
    Call<List<Post_Item>> getPosts(
            @Header("Authorization") String authToken);


}


