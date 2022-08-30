package com.winshelosl.instaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.ParseUser;
import com.winshelosl.instaapp.Fragments.AddPicFragment;
import com.winshelosl.instaapp.Fragments.HomeFragment;
import com.winshelosl.instaapp.Fragments.ProfileFragment;
import com.winshelosl.instaapp.databinding.ActivityMainBinding;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {

   ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        setContentView(binding.getRoot());


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar to act as the ActionBar for this Activity window.
        // Make sure the toolbar exists in the activity and is not null
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item->{

            switch (item.getItemId()) {


                case R.id.addPic:
                    replaceFragment(new AddPicFragment());

                    break;
                case R.id.Person:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.home:
                    default:
                    replaceFragment(new HomeFragment());
                    break;

            }
            return  true;
        });

    }
    public void replaceFragment(Fragment fragment){

        FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frameLayout, fragment);
                fragmentTransaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.logout){

            logoutUser();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.top_navmenu, menu);
        return true;
    }

    public void logoutUser(){
        ParseUser.logOut();
        ParseUser currentUser = ParseUser.getCurrentUser(); // this will now be null
        Toast.makeText(this,"Logout", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, login.class);
        startActivity(i);


    }

}
