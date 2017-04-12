package com.example.android.care2join;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

/**
 * Created by cyoo0706 on 3/2/17.
 */

public class PostAdapter extends ArrayAdapter<Post> implements Filterable {
    private PostFilter mPostFilter;
    private ArrayList<Post> postList;
    private ArrayList<Post> filteredList;

    public PostAdapter(Activity context, ArrayList<Post> posts) {
        super(context, 0, posts);
        getFilter();
    }


    @Override
    public Filter getFilter() {
        if (mPostFilter == null) {
            mPostFilter = new PostFilter();
        }

        return mPostFilter;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Post currentPost = getItem(position);

        TextView userView = (TextView) listItemView.findViewById(R.id.userTextView);
        userView.setText(currentPost.getmUserID());

        TextView courseView = (TextView) listItemView.findViewById(R.id.courseTextView);
        courseView.setText(currentPost.getmCourse());

        TextView locationView = (TextView) listItemView.findViewById(R.id.locationTextView);
        locationView.setText(currentPost.getmLocation());

        TextView durationView = (TextView) listItemView.findViewById(R.id.durationTextView);
        durationView.setText(currentPost.getmDuration());

        return listItemView;
    }

    private class PostFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Post> tempList = new ArrayList<Post>();

                // search content in friend list
                for (Post p : postList) {
                    if (p.getmUserID().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(p);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = postList.size();
                filterResults.values = postList;
            }

            return filterResults;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredList = (ArrayList<Post>) results.values;
            notifyDataSetChanged();
        }
    }
}

