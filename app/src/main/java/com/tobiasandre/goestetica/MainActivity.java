package com.tobiasandre.goestetica;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.tobiasandre.goestetica.database.GoEsteticaContract;
import com.tobiasandre.goestetica.ui.CustomerFragment;
import com.tobiasandre.goestetica.ui.HomeFragment;
import com.tobiasandre.goestetica.ui.ImportContactsActivity;
import com.tobiasandre.goestetica.ui.LoginActivity;
import com.tobiasandre.goestetica.ui.ReportFragment;
import com.tobiasandre.goestetica.ui.ScheduleFragment;
import com.tobiasandre.goestetica.ui.SettingsActivity;
import com.tobiasandre.goestetica.ui.SyncActivity;
import com.tobiasandre.goestetica.ui.TreatmentFragment;
import com.tobiasandre.goestetica.ui.drawer.DataModel;
import com.tobiasandre.goestetica.ui.drawer.DrawerItemCustomAdapter;

public class MainActivity extends AppCompatActivity {

    private static final int MY_STORAGE_WRITE_REQUEST_CODE = 102;
    final private String TAG = MainActivity.class.getSimpleName();
    private View mView;
    FirebaseAuth mAuth;
    private ListView mDrawerList;
    Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private String[] mNavigationDrawerItemTitles;
    private CharSequence mTitle;
    android.support.v7.app.ActionBarDrawerToggle mDrawerToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!checkParameters()){
            Intent intent = new Intent(this, SyncActivity.class);
            intent.putExtra("result", "S");
            startActivityForResult(intent, 2);
        }

        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){

            if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE)){

            }

            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_STORAGE_WRITE_REQUEST_CODE);
        }

        mView = (View)findViewById(R.id.drawer_layout);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            if (!user.getDisplayName().isEmpty()) {
                Snackbar.make(mView,String.format(getString(R.string.wellcome), user.getDisplayName()),Snackbar.LENGTH_LONG).show();
            }
        }else{
            CallLogin();
        }

        initMenu();

        selectItem(0);

    }

    private void initMenu(){

        mNavigationDrawerItemTitles= getResources().getStringArray(R.array.navigation_drawer_items_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        DataModel[] drawerItem = new DataModel[5];

        drawerItem[0] = new DataModel(R.mipmap.ic_launcher, getString(R.string.scheduling));
        drawerItem[1] = new DataModel(R.mipmap.ic_customers, getString(R.string.customers));
        drawerItem[2] = new DataModel(R.mipmap.ic_treatment, getString(R.string.treatments));
        drawerItem[3] = new DataModel(R.mipmap.ic_calendar, getString(R.string.schedule));
        drawerItem[4] = new DataModel(R.mipmap.ic_result, getString(R.string.reports));

        setupToolbar();

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.list_view_item_row, drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        setupDrawerToggle();
    }

    private void CallLogin(){
        Intent intentLogin = new Intent(this, LoginActivity.class);
        intentLogin.putExtra("result", "S");
        startActivityForResult(intentLogin, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2){
            CallLogin();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            startActivityForResult(new Intent(this, SettingsActivity.class),3);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }


    void setupDrawerToggle(){
        mDrawerToggle = new android.support.v7.app.ActionBarDrawerToggle(this,mDrawerLayout,toolbar,R.string.app_name, R.string.app_name);
        mDrawerToggle.syncState();
    }

    private void selectItem(int position) {

        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new HomeFragment();
                break;
            case 1:
                fragment = new CustomerFragment();
                break;
            case 2:
                fragment = new TreatmentFragment();
                break;
            case 3:
                fragment = new ScheduleFragment();
                break;
            case 4:
                fragment = new ReportFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .addToBackStack(null)
                    .commit();

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            setTitle(mNavigationDrawerItemTitles[position]);
            mDrawerLayout.closeDrawer(mDrawerList);

        } else {
            Log.e(TAG, "Error creating fragment");
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    void setupToolbar(){
        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        }catch (Exception erro){
            Log.e(TAG, getString(R.string.general_error)+erro.getMessage());
        }
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }

    }

    private boolean checkParameters(){
        Uri contentUri = GoEsteticaContract.TreatmentTypeEntry.CONTENT_URI;
        String selection = GoEsteticaContract.TreatmentTypeEntry._ID + " = ?";
        String[] selectionArguments = new String[]{String.valueOf(1)};
        Cursor c = this.getContentResolver().query(contentUri, null, selection, selectionArguments, null);
        if (c != null) {
            while (c.moveToNext()) {
                return true;
            }
            c.close();
        }
        return false;
    }
}
