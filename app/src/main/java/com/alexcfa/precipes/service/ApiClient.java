package com.alexcfa.precipes.service;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiClient {

    String BASE_URL = "http://www.recipepuppy.com/";

    @GET("api/")
    Observable<RecipeResponse> searchRecipes(@Query("q") String searchTerm, @Query("p") int page);

}
