package com.example.android.care2join;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by cyoo0706 on 3/2/17.
 */

public class PostAdapter extends BaseAdapter implements Filterable {
    private BrowseActivity mActivity;
    private PostFilter mPostFilter;
    private ArrayList<Post> mPostList;
    private ArrayList<Post> filteredList;

    public  PostAdapter(BrowseActivity activity, ArrayList<Post> postList){
        this.mActivity = activity;
        this.mPostList = postList;
        this.filteredList = postList;

        getFilter();
    }

    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final Post post = (Post) getItem(i);

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.course = (TextView) view.findViewById(R.id.courseTextView);
            holder.duration = (TextView) view.findViewById(R.id.locationTextView);
            holder.location = (TextView) view.findViewById(R.id.durationTextView);
            view.setTag(holder);
        } else {
            // get view holder back
            holder = (ViewHolder) view.getTag();
        }

        holder.course.setText(post.getmCourse());
        holder.duration.setText(post.getmDuration());
        holder.location.setText(post.getmLocation());

        return view;
    }

    static class ViewHolder {
        TextView course;
        TextView location;
        TextView duration;
    }

    @Override
    public Filter getFilter() {
        if (mPostFilter == null) {
            mPostFilter = new PostFilter();
        }

        return mPostFilter;
    }

    private class PostFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults filterResults = new FilterResults();
            if (constraint != null && constraint.length() > 0) {
                ArrayList<Post> tempList = new ArrayList<Post>();

                // search content in friend list
                for (Post p : mPostList) {
                    if (p.getmCourse().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        tempList.add(p);
                    }
                }

                filterResults.count = tempList.size();
                filterResults.values = tempList;
            } else {
                filterResults.count = mPostList.size();
                filterResults.values = mPostList;
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

