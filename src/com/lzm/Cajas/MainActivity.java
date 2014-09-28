package com.lzm.Cajas;

import android.app.*;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.util.DisplayMetrics;
import android.view.*;
import android.widget.*;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import com.lzm.Cajas.db.DbHelper;
import com.lzm.Cajas.db.Especie;
import com.lzm.Cajas.utils.Utils;

import java.util.List;

public class MainActivity extends Activity {
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;

    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String[] mOptionsArray;

    public List<Especie> especiesBusqueda;
    public int screenHeight;
    public int screenWidth;

    public int activeFragment = 0;

    public final int ENCYCLOPEDIA_POS = 0;
    public final int CAPTURA_POS = 1;
    public final int NOTEPAD_POS = 2;
    public final int NOTA_POS = 3;
    public final int TOOLS_POS = 4;
    public final int SETTINGS_POS = 5;

    public final int BUSQUEDA_POS = 18;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DbHelper helper = new DbHelper(this);
        helper.getWritableDatabase();

        Utils.checkDb(this);

        DisplayMetrics displaymetrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

        mTitle = mDrawerTitle = getTitle();
        mOptionsArray = getResources().getStringArray(R.array.options_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // set a custom shadow that overlays the main content when the drawer opens
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        // set up the drawer's list view with items and click listener
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mOptionsArray));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        // enable ActionBar app icon to behave as action to toggle nav drawer
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
//                getActionBar().setTitle(mDrawerTitle);
                getActionBar().setTitle(R.string.menu_title);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        menu.findItem(R.id.search_btn_label).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.search_btn_label:
//                Fragment fragment = new BusquedaFragment();
//                Bundle args = new Bundle();
//                Utils.openFragment(this, fragment, getString(R.string.busqueda_title), args);

                activeFragment = BUSQUEDA_POS;
//                // create intent to perform web search for this planet
//                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
//                // catch event that there's no activity to handle intent
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    startActivity(intent);
//                } else {
//                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
//                }
                return true;
            case R.id.menu_help:
                LayoutInflater inflater = getLayoutInflater();
                View v = inflater.inflate(R.layout.help_layout, null);

                final AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(v)
                        .setTitle(getString(R.string.help_help));
                builder.setPositiveButton(R.string.dialog_btn_cerrar, null);

                final AlertDialog d = builder.create();
                final TextView txt = (TextView) v.findViewById(R.id.help_container);
                switch (activeFragment) {
                    case CAPTURA_POS:
                        txt.setText(getString(R.string.help_captura));
                        break;
                    case ENCYCLOPEDIA_POS:
                        txt.setText(getString(R.string.help_enciclopedia));
                        break;
                    case NOTEPAD_POS:
                    case NOTA_POS:
                        txt.setText(getString(R.string.help_notepad));
                        break;
                    case SETTINGS_POS:
                        txt.setText(getString(R.string.help_configuracion));
                        break;
                    case BUSQUEDA_POS:
                        txt.setText(getString(R.string.help_busqueda));
                        break;
                    case TOOLS_POS:
                        txt.setText(getString(R.string.help_tools));
                        break;
                }

                d.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        Button cerrar = d.getButton(AlertDialog.BUTTON_POSITIVE);
                        cerrar.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                d.dismiss();
                            }
                        });

                    }
                });
                d.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    private void selectItem(int position) {
//        // update the main content by replacing fragments
//        Fragment fragment = new PlanetFragment();
//        Bundle args = new Bundle();
//        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
//        fragment.setArguments(args);
//
//        FragmentManager fragmentManager = getFragmentManager();
//        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
//
//        // update selected item and title, then close the drawer
//        mDrawerList.setItemChecked(position, true);
//        setTitle(mOptionsArray[position]);
//        mDrawerLayout.closeDrawer(mDrawerList);
        // update the main content by replacing fragments
        //System.out.println("pos? "+position);
        String title = "";
//        Utils.hideSoftKeyboard(this);
        Fragment fragment = null;
        Bundle args = null;

        switch (position) {
            case CAPTURA_POS:
//                System.out.println(":::::cientifico:::: " + esCientifico);
                fragment = new CapturaFragment();
                title = getString(R.string.captura_title);
                activeFragment = CAPTURA_POS;
                break;
            case ENCYCLOPEDIA_POS:
                fragment = new EnciclopediaFragment();
                title = getString(R.string.encyclopedia_title);
                activeFragment = ENCYCLOPEDIA_POS;
                break;
            case NOTEPAD_POS:
                fragment = new NotepadFragment();
                title = getString(R.string.notepad_title);
                activeFragment = NOTEPAD_POS;
                break;
            case NOTA_POS:
                fragment = new NotaCreateFrgment();
                args = new Bundle();
                args.putLong("nota", -1);
                title = getString(R.string.nota_create_title);
                activeFragment = NOTA_POS;
                break;
            case TOOLS_POS:
                fragment = new ToolsFragment();
                title = getString(R.string.tools_title);
                activeFragment = NOTA_POS;
                break;
            case SETTINGS_POS:
                fragment = new SettingsFragment();
                title = getString(R.string.settings_title);
                activeFragment = SETTINGS_POS;
                break;
            default:
                fragment = null;
                break;
        }

//        Utils.openFragment(this, fragment, title, args);
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getActionBar().setTitle(mTitle);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
}