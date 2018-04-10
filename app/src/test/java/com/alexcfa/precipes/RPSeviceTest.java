package com.alexcfa.precipes;

import com.alexcfa.precipes.model.Recipe;
import com.alexcfa.precipes.service.ApiClient;
import com.alexcfa.precipes.service.RPService;
import com.alexcfa.precipes.service.RecipeResponse;
import com.alexcfa.precipes.service.Service;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class RPSeviceTest {

    private ApiClient apiClient;
    private Service service;

    @Before
    public void setUp() {
        apiClient = mock(ApiClient.class);
        service = new RPService(apiClient);
    }

    @Test
    public void testMaxNumberOfItems() {
        given(apiClient.searchRecipes("chicken", 1)).willReturn(stubGetRecipesResponse(createRecipes(100)));
        List<Recipe> results = service.searchRecipes("chicken", 10).blockingFirst();
        assertThat(results.size(), is(equalTo(10)));
    }

    @Test
    public void testNetworkRequests() {
        given(apiClient.searchRecipes("carrot", 1)).willReturn(stubGetRecipesResponse(createRecipes(10)));
        given(apiClient.searchRecipes("carrot", 2)).willReturn(stubGetRecipesResponse(createRecipes(5)));
        service.searchRecipes("carrot", 30).blockingFirst();
        verify(apiClient, times(2)).searchRecipes(eq("carrot"), anyInt());
    }

    private Observable<RecipeResponse> stubGetRecipesResponse(List<Recipe> recipes) {
        return Observable.just(new RecipeResponse(null, null, recipes));
    }

    private List<Recipe> createRecipes(int count) {
        List<Recipe> recipes = new ArrayList<Recipe>();
        for (int i = 0; i < count; i++) {
            recipes.add(new Recipe(String.format("Recipe %d", count), null, null, null));
        }
        return recipes;
    }

}
