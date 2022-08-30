package com.winshelosl.instaapp.Fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.winshelosl.instaapp.Post;
import com.winshelosl.instaapp.R;

import java.util.List;

public class ProfileFragment extends HomeFragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      //  return super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_person, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts.setAdapter(adapterGrid);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),3,GridLayoutManager.VERTICAL,false);
        rvPosts.setLayoutManager(layoutManager);
    }

    @Override
    protected void queryPosts() {
    // Specify which class to query
    ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
    query.include(Post.KEY_USER);
    query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
    query.setLimit(20);
    query.addDescendingOrder(Post.KEY_CREATED_KEY);
    query.findInBackground(new FindCallback<Post>() {
        @Override
        public void done(List<Post> posts, ParseException e) {
            if( e != null){
                Log.e(TAG, "Issue with getting posts");
                return;
            }

            for (Post post: posts) {
                Log.i(TAG, "Post" + post.getDescription() + "Username: " +  post.getUser().getUsername() );
            }
            allPosts.addAll(posts);
            adapterGrid.notifyDataSetChanged();

        }
    });

}


}
