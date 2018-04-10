package com.alexcfa.precipes.ui;

import android.annotation.SuppressLint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SearchView;

import com.alexcfa.precipes.R;
import com.alexcfa.precipes.model.Recipe;
import com.alexcfa.precipes.service.RPService;
import com.alexcfa.precipes.service.Service;
import com.alexcfa.precipes.ui.adapter.SearchAdapter;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.Subject;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private Service service;
    private SearchAdapter adapter;
    private SearchView searchView;
    private ListView resultList;
    private Subject<String> searchTextSubject;
    private Observable<String> onSearchTextChanged;
    private final int MAX_RECIPES = 20;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initApi();
        initComponents();
        subscribeToTextChanges();
    }

    private void initApi() {
        this.service = new RPService();
    }

    private void initComponents() {
        searchView = findViewById(R.id.searchBar);
        resultList = findViewById(R.id.list);
        adapter = new SearchAdapter(this);
        resultList.setAdapter(this.adapter);
        searchView.setOnQueryTextListener(this);
        searchTextSubject = BehaviorSubject.create();
        onSearchTextChanged = searchTextSubject.debounce(300, TimeUnit.MILLISECONDS);
    }

    @SuppressLint("CheckResult")
    private void subscribeToTextChanges() {
        onSearchTextChanged.subscribe(new Consumer<String>() {
            @Override
            public void accept(final String text) throws Exception {
                service.searchRecipes(text, MAX_RECIPES)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .doOnNext(new Consumer<List<Recipe>>() {
                            @Override
                            public void accept(List<Recipe> recipes) throws Exception {
                                adapter.clear();
                                adapter.addAll(recipes);
                            }
                        })
                        .subscribe();
            }
        });
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return true;
    }

    @Override
    public boolean onQueryTextChange(final String newText) {
        if (newText.trim().length() == 0) {
            adapter.clear();
        }
        searchTextSubject.onNext(newText);
        return true;
    }
}
