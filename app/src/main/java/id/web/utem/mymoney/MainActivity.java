package id.web.utem.mymoney;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.ImageViewCompat;
import android.util.Log;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final String apiLink = "http://mymoney-api.utem.web.id/api/v1/";
    String user_id;

    Toolbar toolbar;
    FloatingActionButton fab;
    NavigationView navigationView;
    DrawerLayout drawer;
    ActionBarDrawerToggle toggle;

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_DASHBOARD = "dashboard";
    private static final String TAG_BALANCE = "balance";
    private static final String TAG_ADD_BALANCE = "add_balance";
    private static final String TAG_INCOME = "income";
    private static final String TAG_ADD_INCOME= "add_income";
    private static final String TAG_OUTCOME = "outcome";
    private static final String TAG_ADD_OUTCOME = "add_outcome";
    private static final String TAG_TRANSFER = "transfer";
    private static final String TAG_ADD_TRANSFER = "add_transfer";
    public static String CURRENT_TAG = TAG_DASHBOARD;

    private String[] activityTitles;

    private Handler mHandler;

    TextView tvNavUsername;
    TextView tvNavEmail;
    ImageView ivNavPicture;

    // Dashboard
    TextView tvDailyIncomeAmount;
    TextView tvDailyOutcomeAmount;
    TextView tvMonthlyIncomeAmount;
    TextView tvMonthlyOutcomeAmount;

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvNavUsername = findViewById(R.id.tvNavUsername);
        tvNavEmail = findViewById(R.id.tvNavEmail);
        ivNavPicture = findViewById(R.id.ivNavPicture);

        // Dashboard
        tvDailyIncomeAmount = findViewById(R.id.tvDailyIncomeAmount);
        tvDailyOutcomeAmount = findViewById(R.id.tvDailyOutcomeAmount);
        tvMonthlyIncomeAmount = findViewById(R.id.tvMonthlyIncomeAmount);


        mHandler = new Handler();

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MainActivity.this, "Call Toast!", Toast.LENGTH_SHORT).show();
                if (navItemIndex == 1)
                {
                    CURRENT_TAG = TAG_ADD_BALANCE;
                } else if (navItemIndex == 2)
                {
                    CURRENT_TAG = TAG_ADD_INCOME;
                } else if (navItemIndex == 3)
                {
                    CURRENT_TAG = TAG_ADD_OUTCOME;
                } else if (navItemIndex == 4)
                {
                    CURRENT_TAG = TAG_ADD_TRANSFER;
                }
                loadAddFragment();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        pref = getApplicationContext().getSharedPreferences("MyMoney_Pref", 0);
        editor = pref.edit();

        user_id = pref.getString("user_id", "0");


        // load nav menu header data
        loadNavHeader();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_DASHBOARD;
            loadHomeFragment();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            editor.remove("logged_in").remove("user_id");
            editor.commit();

            finish();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.hide();
        else
            fab.show();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // Dashboard
                DashboardFragment dashboardFragment = new DashboardFragment();
                return dashboardFragment;
            case 1:
                // Balance
                BalanceFragment balanceFragment = new BalanceFragment();
                return balanceFragment;
            case 2:
                // Income
                IncomeFragment incomeFragment = new IncomeFragment();
                return incomeFragment;
            case 3:
                // Outcome
                OutcomeFragment outcomeFragment = new OutcomeFragment();
                return outcomeFragment;
            case 4:
                // Transfer
                TransferFragment transferFragment = new TransferFragment();
                return transferFragment;
            default:
                return new DashboardFragment();
        }
    }

    private Fragment getAddFragment() {
        switch (navItemIndex) {
            case 1:
                // Balance
                AddBalanceFragment addBalanceFragment = new AddBalanceFragment();
                return addBalanceFragment;
            case 2:
                // Income
                AddIncomeFragment addIncomeFragment = new AddIncomeFragment();
                return addIncomeFragment;
            case 3:
                // Outcome
                AddOutcomeFragment addOutcomeFragment = new AddOutcomeFragment();
                return addOutcomeFragment;
            case 4:
                // Transfer
                AddTransferFragment addTransferFragment = new AddTransferFragment();
                return addTransferFragment;
            default:
                return new DashboardFragment();
        }
    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
//        tvNavUsername.setText("Derick");
//        tvNavEmail.setText("user@mymoney.com");

        // showing dot next to notifications label
        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // Load the data for fragment
        loadData();

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadAddFragment() {
        Log.d("NOTICE", "Call loadAddFragment");
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            Log.d("NOTICE", "CURRENT TAG : " + CURRENT_TAG);
            Log.d("NOTICE", "NOT NULL");
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }
        Log.d("NOTICE", "Call loadAddFragment");

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getAddFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                Log.d("CURRENT_TAG", "CURRENT TAG IS " + CURRENT_TAG);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // Load the data for fragment
        loadData();

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            //Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.nav_dashboard:
                navItemIndex = 0;
                CURRENT_TAG = TAG_DASHBOARD;
                break;
            case R.id.nav_balance:
                navItemIndex = 1;
                CURRENT_TAG = TAG_BALANCE;
                break;
            case R.id.nav_income:
                navItemIndex = 2;
                CURRENT_TAG = TAG_INCOME;
                break;
            case R.id.nav_outcome:
                navItemIndex = 3;
                CURRENT_TAG = TAG_OUTCOME;
                break;
            case R.id.nav_transfer:
                navItemIndex = 4;
                CURRENT_TAG = TAG_TRANSFER;
                break;
            default:
                navItemIndex = 0;
        }

        //Checking if the item is in checked state or not, if not make it in checked state
        if (item.isChecked()) {
            item.setChecked(false);
        } else {
            item.setChecked(true);
        }

        loadHomeFragment();

        return true;
    }

    private void loadData() {
        if (navItemIndex == 0)
        {
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, apiLink +"auth.php?method=get&user_id="+user_id, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    JSONObject jsonObject = null;

                    try {
                        jsonObject = new JSONObject(response);
                        tvDailyIncomeAmount.setText(jsonObject.getString("daily_income"));
                        tvDailyOutcomeAmount.setText(jsonObject.getString("daily_outcome"));
                        tvMonthlyIncomeAmount.setText(jsonObject.getString("monthly_income"));
                        tvMonthlyOutcomeAmount.setText(jsonObject.getString("monthly_outcome"));
                        Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "There is an error!", Toast.LENGTH_SHORT).show();
                        Log.d("A", "Error!", e);
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(),"Unable to get data for dashboard", Toast.LENGTH_SHORT).show();
                }
            });

            requestQueue.add(stringRequest);
        } else if (navItemIndex == 1)
        {

        } else if (navItemIndex == 2)
        {

        } else if (navItemIndex == 3)
        {

        } else if (navItemIndex == 4)
        {

        }
    }
}
