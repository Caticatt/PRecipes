package com.alexcfa.precipes.service;


import com.alexcfa.precipes.model.Recipe;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RPService implements Service {

    private final int PER_PAGE = 10;

    private final ApiClient apiClient;

    public RPService(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public RPService() {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .baseUrl(ApiClient.BASE_URL)
                .build();
        this.apiClient = retrofit.create(ApiClient.class);
    }

    @Override
    public Observable<List<Recipe>> searchRecipes(final String term, final int maxResults) {
        return Observable.create(new ObservableOnSubscribe<List<Recipe>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Recipe>> e) throws Exception {
                int nextPage = 1;
                List<Recipe> recipes = new ArrayList<>();
                boolean hasMore = true;
                do {
                    List<Recipe> perPage = getPageOfSearchResults(term, nextPage).blockingFirst();
                    recipes.addAll(perPage);
                    if (lastPage(perPage) || allFilled(recipes, maxResults)) {
                        e.onNext(truncateList(recipes, maxResults));
                        e.onComplete();
                        hasMore = false;
                    } else {
                        nextPage = recipes.size() / PER_PAGE + 1;
                    }
                } while (hasMore);
            }
        });
    }

    private boolean lastPage(List<Recipe> recipes) {
        return recipes.size() < PER_PAGE;
    }

    private boolean allFilled(List<Recipe> recipes, int fill) {
        return recipes.size() >= fill;
    }

    private List<Recipe> truncateList(List<Recipe> recipes, int maxResults) {
        return recipes.size() > maxResults ? recipes.subList(0, maxResults) : recipes;
    }

    private Observable<List<Recipe>> getPageOfSearchResults(String term, int page) {
        return this.apiClient.searchRecipes(term, page)
                .map(new Function<RecipeResponse, List<Recipe>>() {
                    @Override
                    public List<Recipe> apply(RecipeResponse recipeResponse) throws Exception {
                        return recipeResponse.getRecipes();
                    }
                })
                .subscribeOn(Schedulers.io());
    }

}
