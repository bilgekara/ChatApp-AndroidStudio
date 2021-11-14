package com.example.firebaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class  DashboardActivity extends AppCompatActivity {

    //firebase auth
    FirebaseAuth firebaseAuth;

    ActionBar actionBar;
    //views
    //TextView mProfileTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Actionbar and its title
        actionBar =getSupportActionBar();
        actionBar.setTitle("Profile");

        //init
        firebaseAuth=firebaseAuth.getInstance();

        //init views
       // mProfileTV=findViewById(R.id.profileTv);

        //bottom navigation
        BottomNavigationView navigationView=findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);

        //home fragment transaction (default)
        actionBar.setTitle("Home");//change actionbar title
        HomeFragment fragment1=new HomeFragment();
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener selectedListener=
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
                    //handle item clicks
                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            //home fragment transaction
                            actionBar.setTitle("Home");//change actionbar title
                            HomeFragment fragment1=new HomeFragment();
                            FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                            ft1.replace(R.id.content,fragment1,"");
                            ft1.commit();
                            return  true;
                        case R.id.nav_profile:
                            //profile fragment transaction
                            actionBar.setTitle("Profile");//change actionbar title
                            ProfileFragment fragment2=new ProfileFragment();
                            FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                            ft2.replace(R.id.content,fragment2,"");
                            ft2.commit();
                            return  true;
                        case R.id.nav_users:
                            //users fragment transaction
                            actionBar.setTitle("Home");//change actionbar title
                            UsersFragment fragment3=new UsersFragment();
                            FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                            ft3.replace(R.id.content,fragment3,"");
                            ft3.commit();
                            return  true;
                        case R.id.nav_chat:
                            //users fragment transaction
                            actionBar.setTitle("Chats");//change actionbar title
                            ChatListFragment fragment4=new ChatListFragment();
                            FragmentTransaction ft4=getSupportFragmentManager().beginTransaction();
                            ft4.replace(R.id.content,fragment4,"");
                            ft4.commit();
                            return  true;
                    }
                    return  false;
                }
            };


    private  void checkUserStatus(){
        //get current user
        FirebaseUser user =firebaseAuth.getCurrentUser();
        if(user!=null){
            //user is signed in stay here
            //set email of logged in user
            //mProfileTV.setText(user.getEmail());
        }else{
            //user not signed in, go to main activity
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onStart(){
        //check on start of app
        checkUserStatus();
        super.onStart();
    }
}