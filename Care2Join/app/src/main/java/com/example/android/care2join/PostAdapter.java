package com.example.android.care2join;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import javax.xml.datatype.Duration;

/**
 * Created by cyoo0706 on 3/2/17.
 */

public class PostAdapter extends BaseAdapter implements Filterable {
    private FragmentActivity mActivity;
    private PostFilter mPostFilter;
    private ArrayList<Post> mPostList;
    private ArrayList<Post> filteredList;

    public PostAdapter(FragmentActivity activity, ArrayList<Post> postList){
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

    public void add(Post p){
        mPostList.add(p);
    }

    public void clear(){
        mPostList.clear();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;
        final Post post = (Post) getItem(i);

        if(view == null){
            LayoutInflater layoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.list_item, viewGroup, false);
            holder = new ViewHolder();
            holder.user = (TextView) view.findViewById(R.id.userTextView);
            holder.course = (TextView) view.findViewById(R.id.courseTextView);
            holder.duration = (TextView) view.findViewById(R.id.durationTextView);
            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
        }

        holder.course.setText(post.getmCourse());
        holder.duration.setText(post.getmDuration());
        holder.user.setText(post.getmEmail());

        return view;
    }

    static class ViewHolder {
        TextView course;
        TextView user;
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

