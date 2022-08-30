package com.winshelosl.instaapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.winshelosl.instaapp.Adapter.PostsAdapter;
import com.winshelosl.instaapp.Adapter.PostsAdapterGrid;
import com.winshelosl.instaapp.EndlessRecyclerViewScrollListener;
import com.winshelosl.instaapp.Post;
import com.winshelosl.instaapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }


    public String TAG= "HomeFragmentActivity";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    protected RecyclerView rvPosts;

    protected SwipeRefreshLayout swipeContainer;
    protected PostsAdapter adapter;
    protected PostsAdapterGrid adapterGrid;
    protected List<Post> allPosts;
    EndlessRecyclerViewScrollListener scrollListener;


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rvPosts = view.findViewById(R.id.rvPosts);

        swipeContainer = view.findViewById(R.id.swipeContainer);
        allPosts = new ArrayList<>();
        adapter = new PostsAdapter(getContext(), allPosts);
        adapterGrid = new PostsAdapterGrid(getContext(), allPosts);

        rvPosts.setAdapter(adapter);



        queryPosts();

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchTimelineAsync(0);
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

                LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
              // layoutManager.setReverseLayout(true);
               layoutManager.setStackFromEnd(true);

               rvPosts.setLayoutManager(layoutManager);

                ///EndlessRecyclerView
                rvPosts.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

                scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
                    @Override
                    public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                        Log.i("HomeFragment", "Load More Data");
                        //fetchTimelineAsync(0);

                        queryPosts();
                    }

        };

                // Adds the scroll listener to RecyclerView
                rvPosts.addOnScrollListener(scrollListener);




    }

    protected void queryPosts() {
        // Specify which class to query
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include(Post.KEY_USER);
        //query.whereEqualTo(Post.KEY_USER, ParseUser.getCurrentUser());
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
                adapter.notifyDataSetChanged();


            }
        });

    }


    protected void fetchTimelineAsync(int page) {
        // Send the network request to fetch the updated data
        // getHomeProfileFragment is an example endpoint.
        adapter.clear();
        // ...the data has come back, add new items to your adapter...
        queryPosts();
        // Now we call setRefreshing(false) to signal refresh has finished
        swipeContainer.setRefreshing(false);

    }


}