package com.example.gk_crud_nafrag.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;


import com.example.gk_crud_nafrag.Fragment.AddProductFragment;
import com.example.gk_crud_nafrag.Fragment.AllProductFragment;
import com.example.gk_crud_nafrag.Fragment.HomeFragment;
import com.example.gk_crud_nafrag.R;
import com.google.android.material.navigation.NavigationView;

import java.io.Serializable;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int FRAGMENT_HOME = 1;
    private static final int FRAGMENT_ALL_PRODUCT = 2;
    private static final int FRAGMENT_ADD_PRODUCT = 3;
    private DrawerLayout drawerLayout;

    private int currentFragment = FRAGMENT_HOME;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.Toolbal);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout, toolbar,R.string.open_nav
        ,R.string.close_nav);

        drawerLayout.addDrawerListener(toggle);
        toggle .syncState();
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }
    @Override
    public void onBackPressed(){
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (fragment instanceof AllProductFragment) {
            replaceFragment(new HomeFragment());
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_home);
            currentFragment = FRAGMENT_HOME;
        }else if (fragment instanceof AddProductFragment) {
            replaceFragment(new HomeFragment());
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_home);
            currentFragment = FRAGMENT_HOME;
        }
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                if (FRAGMENT_HOME != currentFragment){
                    replaceFragment(new HomeFragment());
                }
                currentFragment = FRAGMENT_HOME;
                break;
            case R.id.nav_allProduct:
                if (FRAGMENT_ALL_PRODUCT != currentFragment){
                    replaceFragment(new AllProductFragment());
                }
                currentFragment = FRAGMENT_ALL_PRODUCT;
                break;
            case R.id.nav_addProduct:
                if (FRAGMENT_ADD_PRODUCT != currentFragment){
                    replaceFragment(new AddProductFragment());

                }
                currentFragment = FRAGMENT_ADD_PRODUCT;
                break;
            case R.id.nav_logOut:
                Toast.makeText(this, "LogOut", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    private void  replaceFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container , fragment);
        fragmentTransaction.commit();
    }
}