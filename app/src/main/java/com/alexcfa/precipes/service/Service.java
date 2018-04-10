package com.alexcfa.precipes.service;

import com.alexcfa.precipes.model.Recipe;

import java.util.List;

import io.reactivex.Observable;

public interface Service {

    Observable<List<Recipe>> searchRecipes(String term, int maxResults);

}
