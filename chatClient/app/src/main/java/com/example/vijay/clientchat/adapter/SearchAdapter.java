package com.example.vijay.clientchat.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vijay.clientchat.R;
import com.example.vijay.clientchat.models.SearchResponse;

import java.util.ArrayList;
import java.util.List;

import static com.example.vijay.clientchat.models.SearchResponse.*;

/**
 * Created by vijay on 31/8/17.
 */

public class SearchAdapter extends BaseAdapter implements Filterable {

    private List<SearchResponse.People> data;
    private List<SearchResponse.People> suggestions;
    private Drawable suggestionIcon;
    private LayoutInflater inflater;
    private boolean ellipsize;

    public SearchAdapter(Context context, List<SearchResponse.People> suggestions) {
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.suggestions = suggestions;
    }

    public SearchAdapter(Context context, List<SearchResponse.People> suggestions, Drawable suggestionIcon, boolean ellipsize) {
        inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        this.suggestions = suggestions;
        this.suggestionIcon = suggestionIcon;
        this.ellipsize = ellipsize;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if (!TextUtils.isEmpty(constraint)) {

                    // Retrieve the autocomplete results.
                    List<SearchResponse.People> searchData = new ArrayList<>();

                    for (People people : suggestions) {
                        if (people.getDisplayName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                            searchData.add(people);
                        }
                    }

                    // Assign the data to the FilterResults
                    filterResults.values = searchData;
                    filterResults.count = searchData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.values != null) {
                    data = (List<SearchResponse.People>) results.values;
                    notifyDataSetChanged();
                }
            }
        };
        return filter;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public People getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        SuggestionsViewHolder viewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.suggest_item, parent, false);
            viewHolder = new SuggestionsViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SuggestionsViewHolder) convertView.getTag();
        }

        String currentListData = getItem(position).getDisplayName();

        viewHolder.textView.setText(currentListData);
        if (ellipsize) {
            viewHolder.textView.setSingleLine();
            viewHolder.textView.setEllipsize(TextUtils.TruncateAt.END);
        }

        return convertView;
    }

    public void setSuggestions(List<People> suggestions) {
        if (suggestions != null) {
            this.suggestions.clear();
            this.suggestions.addAll(suggestions);
            notifyDataSetChanged();
        }
    }

    private class SuggestionsViewHolder {

        TextView textView;
        ImageView imageView;

        public SuggestionsViewHolder(View convertView) {
            textView = convertView.findViewById(R.id.suggestion_text);
            if (suggestionIcon != null) {
                imageView = convertView.findViewById(R.id.suggestion_icon);
                imageView.setImageDrawable(suggestionIcon);
            }
        }
    }
}