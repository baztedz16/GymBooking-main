package com.domzky.gymbooking.Sessions.GymOwner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.domzky.gymbooking.R;
import com.domzky.gymbooking.Sessions.GymOwner.pages.Account.AccountFragment;
import com.domzky.gymbooking.Sessions.GymOwner.pages.CoachesList.CoachesListFragment;
import com.domzky.gymbooking.Sessions.GymOwner.pages.Dashboard.DashboardFragment;
import com.domzky.gymbooking.Sessions.GymOwner.pages.MemberList.MembersListFragment;
import com.domzky.gymbooking.Sessions.GymOwner.pages.Membership.MembershipListFragment;
import com.domzky.gymbooking.Sessions.GymOwner.pages.StaffsList.StaffsListFragment;
import com.domzky.gymbooking.Sessions.GymOwner.pages.TasksList.TasksListFragment;
import com.domzky.gymbooking.Sessions.UsersActivity;
import com.google.android.material.navigation.NavigationView;

public class OwnerSessionActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;

    public TextView headUserName;


    private long backPressedTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_session);


        SharedPreferences preferences = this.getSharedPreferences("owner", Context.MODE_PRIVATE);

        // Setting my component hooks
        drawerLayout = findViewById(R.id.drawerlayout);
        navigationView = findViewById(R.id.nav_view);

        // This will be used when the menu is dynamic for different users
        navigationView.getMenu().clear();
        navigationView.inflateMenu(R.menu.owner_nav_menu);

        // Implemented class for the navigation functionality
        navigationView.setNavigationItemSelectedListener(this);


        // Set the toolbar title first then the bring the entire toolbar to the front of the nav menu
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(navigationView.getMenu().getItem(0).toString());
        setSupportActionBar(toolbar);
        navigationView.bringToFront();

        // Set an initialized fragment when the menu activity starts to open
        changeFragmentMenu(new DashboardFragment(),navigationView.getMenu().getItem(0).toString());

        // Initialize highlighted menu item when the menu activity starts to open
        navigationView.setCheckedItem(navigationView.getMenu().getItem(0).getItemId());

        // Set navigation header values
        View headerView = navigationView.getHeaderView(0);
        headUserName = headerView.findViewById(R.id.nav_account_name);
        TextView headUserType = headerView.findViewById(R.id.nav_account_type);
        headUserName.setText(preferences.getString("fullname",""));
        headUserType.setText("Owner");


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
//        else {
//            super.onBackPressed();
//        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.owner_menu_dashboard:
                changeFragmentMenu(new DashboardFragment(),item.toString());
                break;
            case R.id.owner_menu_tasks:
                changeFragmentMenu(new TasksListFragment(),item.toString());
                break;
            case R.id.owner_menu_membership:
                changeFragmentMenu(new MembershipListFragment(),item.toString());
                break;
            case R.id.owner_menu_coaches:
                changeFragmentMenu(new CoachesListFragment(),item.toString());
                break;
            case R.id.owner_menu_staffs:
                changeFragmentMenu(new StaffsListFragment(),item.toString());
                break;
            case R.id.owner_menu_account:
                changeFragmentMenu(new AccountFragment(),item.toString());
                break;
            case R.id.owner_menu_logout:
                logOutSessionFunction();
                break;
            case R.id.owner_member:
                changeFragmentMenu(new MembersListFragment(),item.toString());
                break;
            default:
                changeFragmentMenu(new DashboardFragment(),item.toString());
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void changeFragmentMenu(Fragment fragment, String menuTitle) {
        getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_container,fragment).commit();
        toolbar.setTitle(menuTitle);
    }


    private void logOutSessionFunction(){
//        AlertDialog.Builder builder = new AlertDialog.Builder(AdminSessionActivity.this);
//        builder.setMessage("Are You Sure to Log Out?");
//        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                startActivity(new Intent(getApplicationContext(), UsersActivity.class));
//                finishAffinity();
//            }
//        });builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                // NOTHING
//            }
//        });
//        builder.setCancelable(false);
//        builder.create().show();

        startActivity(new Intent(getApplicationContext(), UsersActivity.class));
        finishAffinity();

    }
}