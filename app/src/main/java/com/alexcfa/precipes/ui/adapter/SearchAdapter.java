package com.alexcfa.precipes.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.alexcfa.precipes.R;
import com.alexcfa.precipes.model.Recipe;


public class SearchAdapter extends ArrayAdapter<Recipe> {

    public SearchAdapter(@NonNull Context context) {
        super(context, R.layout.item_search);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_search, parent, false);
        }
        TextView title = convertView.findViewById(R.id.searchResult);
        Recipe recipe = getItem(position);
        title.setText(recipe.getTitle());
        return convertView;
    }

}
