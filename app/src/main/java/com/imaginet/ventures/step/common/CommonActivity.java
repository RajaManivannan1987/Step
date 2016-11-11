package com.imaginet.ventures.step.common;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imaginet.ventures.step.R;
import com.imaginet.ventures.step.activity.AboutStepActivity;
import com.imaginet.ventures.step.activity.DashboardActivity;
import com.imaginet.ventures.step.activity.LoginActivity;
import com.imaginet.ventures.step.activity.ProfileActivity;
import com.imaginet.ventures.step.activity.SettingsActivity;
import com.imaginet.ventures.step.adapter.baseadapter.NavigationDrawerBaseAdapter;
import com.imaginet.ventures.step.singleton.UserData;
import com.imaginet.ventures.step.utility.AlertDialogManager;
import com.imaginet.ventures.step.utility.constant.ConstantFunction;
import com.imaginet.ventures.step.utility.interfaces.VolleyResponseListener;
import com.imaginet.ventures.step.utility.sharedpreferrence.Session;
import com.imaginet.ventures.step.utility.webservice.WebServices;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by IM028 on 9/7/16.
 */
public class CommonActivity extends AppCompatActivity {
    private static String TAG = "CommonActivity";
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ListView listView;
    private FrameLayout frameLayout;
    private ActionBarDrawerToggle toggle;
    private View headerView;
    private TextView headingTextView;

    private TextView nameTextView, typeTextView;
    private ImageView profileImageView;
    private RelativeLayout relativeLayout;
    private Session session;
    private NavigationDrawerBaseAdapter adapter;
    private WebServices webServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_activity);
        session = Session.getInstance(this, TAG);
        webServices = WebServices.getInstance(this);

        toolbar = (Toolbar) findViewById(R.id.commonActivityToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        headingTextView = (TextView) findViewById(R.id.commonActivityTitleTextView);

        drawerLayout = (DrawerLayout) findViewById(R.id.commonActivityDrawerLayout);
        listView = (ListView) findViewById(R.id.commonActivityFrameLayoutLeftDrawerListView);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                // Do whatever you want here
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                nameTextView.setText(UserData.getInstance().getUserName());
                super.onDrawerOpened(drawerView);
                // Do whatever you want here
            }
        };
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        headerView = getLayoutInflater().inflate(R.layout.header_navigation_list_view, null, false);
        listView.addHeaderView(headerView);
        listView.setAdapter(adapter = new NavigationDrawerBaseAdapter(this));


        nameTextView = (TextView) headerView.findViewById(R.id.headerNavigationListViewNameTextView);
        typeTextView = (TextView) headerView.findViewById(R.id.headerNavigationListViewTypeTextView);
        profileImageView = (ImageView) headerView.findViewById(R.id.headerNavigationListViewImageView);
        relativeLayout = (RelativeLayout) headerView.findViewById(R.id.headerNavigationListViewRelativeLayout);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i != 0) {
                    switch (adapter.getItem(i).toString()) {
                        case "Dashboard":
                            startActivity(new Intent(CommonActivity.this, DashboardActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "Progress":
                            ConstantFunction.toast(CommonActivity.this, "Coming Soon");
                            break;
                        case "Profile":
//                            ConstantFunction.toast(CommonActivity.this, "Coming Soon");
                            startActivity(new Intent(CommonActivity.this, ProfileActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "Settings":
//                            ConstantFunction.toast(CommonActivity.this, "Coming Soon");
                            startActivity(new Intent(CommonActivity.this, SettingsActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "About STEP":
                            startActivity(new Intent(CommonActivity.this, AboutStepActivity.class).setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT));
                            break;
                        case "Logout":
                            webServices.logout(new VolleyResponseListener() {
                                @Override
                                public void onResponse(JSONObject response) throws JSONException {
                                    if (response.getBoolean("success") && response.getJSONObject("data").getInt("resultcode") == 200) {
                                        ConstantFunction.toast(CommonActivity.this, response.getJSONObject("data").getString("resultmessage"));
                                        Session.getInstance(CommonActivity.this, TAG).logout();
                                        UserData.getInstance().setJsonObject(new JSONObject());
                                        Intent intent = new Intent(CommonActivity.this, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                    } else {
                                        AlertDialogManager.showAlertDialog(CommonActivity.this, "Alert", response.getJSONObject("data").getString("resultmessage"), false);
                                    }
                                }

                                @Override
                                public void onError(String message, String title) {
                                    AlertDialogManager.showAlertDialog(CommonActivity.this, title, message, false);
                                }
                            }, TAG, CommonActivity.this);
                            break;
                        default:
                            ConstantFunction.toast(CommonActivity.this, "Coming Soon");
                    }
                    if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                        drawerLayout.closeDrawer(GravityCompat.START);
                    }
                }
            }
        });

    }

    public void setView(int viewLayout) {
        frameLayout = (FrameLayout) findViewById(R.id.commonActivityFrameLayout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(viewLayout, null, false);
        frameLayout.addView(activityView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        nameTextView.setText(UserData.getInstance().getUserName());
        typeTextView.setText(UserData.getInstance().getAccountType());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.common_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected void setTitle(String title) {
        headingTextView.setVisibility(View.VISIBLE);
        headingTextView.setText(title);
    }
}
