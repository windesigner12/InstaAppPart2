package com.winshelosl.instaapp.Fragments;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.winshelosl.instaapp.MainActivity;
import com.winshelosl.instaapp.Post;
import com.winshelosl.instaapp.R;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AddPicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddPicFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AddPicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddPicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddPicFragment newInstance(String param1, String param2) {
        AddPicFragment fragment = new AddPicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }



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
        return inflater.inflate(R.layout.fragment_add_pic, container, false);


    }


    public String TAG="MainActivity";
    private EditText etDescription;
    private Button btTakePic;
    private Button btSubmit;
    ProgressBar pb;

    private ImageView ivPosterProfile;
    public  File photoFile;
    public static String photoFileName = "photo.jpg";
    //public String TAG="AddFragment";
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;




    // This event is triggered soon after onCreateView().
    // onViewCreated() is only called if the view returned from onCreateView() is non-null.
    // Any view setup should occur here.  E.g., view lookups and attaching view listeners.
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btTakePic = view.findViewById(R.id.btTakePic);
        etDescription = view.findViewById(R.id.etDescription);
        btSubmit = view.findViewById(R.id.btSubmit);
        ivPosterProfile = view.findViewById(R.id.ivPosterProfile);
        // on some click or some loading we need to wait for...
        pb = (ProgressBar) view.findViewById(R.id.pbLoading);

        btTakePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               launchCamera();
                Log.i("AddFragment", "Post was Successful!!");}
        });

        // queryPosts();
//
        btSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String description = etDescription.getText().toString();
                if (description.isEmpty()){
                    Toast.makeText(getContext(),"Description cannot be empty", Toast.LENGTH_LONG).show();
                    return;
                }

                if (photoFile == null || ivPosterProfile.getDrawable() == null){
                    Toast.makeText(getContext(), "There is no image", Toast.LENGTH_SHORT).show();
                return;
                }

                pb.setVisibility(ProgressBar.VISIBLE);
                ParseUser currentUser = ParseUser.getCurrentUser();
                savePost(description , currentUser, photoFile);


              }
        });


    }



    public File getPhotoFileUri(String fileName) {
        // Get safe storage directory for photos
        // Use `getExternalFilesDir` on Context to access package-specific directories.
        // This way, we don't need to request external read/write runtime permissions.
        File mediaStorageDir = new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES),"MAinActivity");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d("MainActivity", "failed to create directory");
        }

        // Return the file target for the photo based on filename
        return new File(mediaStorageDir.getPath() + File.separator + fileName);


    }

    public void launchCamera() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create a File reference for future access
        photoFile = getPhotoFileUri(photoFileName);

        // wrap File object into a content provider
        // required for API >= 24
        // See https://guides.codepath.com/android/Sharing-Content-with-Intents#sharing-files-with-api-24-or-higher
        //Uri fileProvider = FileProvider.getUriForFile(MainActivity.this, "com.codepath.fileprovider", photoFile);
        Uri fileProvider = FileProvider.getUriForFile(getContext(), "com.codepath.fileprovider", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
        // If you call startActivityForResult() using an intent that no app can handle, your app will crash.
        // So as long as the result is not null, it's safe to use the intent.
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            // Start the image capture intent to take photo
            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }

    }



    private void savePost(String description, ParseUser currentUser, File photoFile) {
        Post post = new Post();
        post.setDescription(description);
        post.setImage(new ParseFile(photoFile));
        post.setUser(currentUser);
        post.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if ( e != null){
                    Log.e(TAG, "Error While saving", e);
                    Toast.makeText(getContext(), "Error While saving", Toast.LENGTH_SHORT).show();
                    pb.setVisibility(ProgressBar.INVISIBLE);
                }
                Log.i(TAG, "Post was Successful!!");
                etDescription.setText("");
                ivPosterProfile.setImageResource(0);
                // run a background job and once complete
                pb.setVisibility(ProgressBar.INVISIBLE);


            }
        });
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
//            Bitmap takeImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
//            ImageView ivPosterProfile = (ImageView) findViewById(R.id.ivPosterProfile);
//            ivPosterProfile.setImageBitmap(takeImage);

            if(resultCode == RESULT_OK){
                Bitmap takeImage = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                ivPosterProfile.setImageBitmap(takeImage);
                btSubmit.setVisibility(VISIBLE);
                btTakePic.setVisibility(View.GONE);
            }

   } else {
            Toast.makeText(getContext(), "Picture wasn't taken", Toast.LENGTH_SHORT).show();
        }

    }






}