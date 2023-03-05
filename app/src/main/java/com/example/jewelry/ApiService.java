package com.example.jewelry;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/login")
    Single<UserResponse> logIn(@Body UserBody user);

    @POST("auth/register")
    Completable signUp(@Body SignUpUserBody user);

    @GET("versions")
    Single<VersionResponse> loadVersions();

    @GET("dishes")
    Single<DishResponse> loadDishes();

    @GET("dishes/{version}")
    Single<DishResponse> loadDishesByVersion(@Path("version") String version);
}
