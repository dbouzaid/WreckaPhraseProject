package au.edu.uts.mad.id11258732.wreckaphraseproject.view;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.fragment.AboutFragment;
import au.edu.uts.mad.id11258732.wreckaphraseproject.fragment.EditPhraseFragment;
import au.edu.uts.mad.id11258732.wreckaphraseproject.fragment.EditWordFragment;
import au.edu.uts.mad.id11258732.wreckaphraseproject.fragment.FavouritesFragment;
import au.edu.uts.mad.id11258732.wreckaphraseproject.fragment.InputPhraseFragment;
import au.edu.uts.mad.id11258732.wreckaphraseproject.fragment.ViewPhraseFragment;
import au.edu.uts.mad.id11258732.wreckaphraseproject.adapter.WordsAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InputPhraseFragment.OnSubmitPhraseListener, WordsAdapter.OnWordChosenToEditListener,
        EditWordFragment.OnNewWordSubmittedListener, EditPhraseFragment.OnFinalisePhrase,
        ViewPhraseFragment.OnChangeFromViewPhraseFragment {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        if (drawer != null) {
            drawer.setDrawerListener(toggle);
        }
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }


        changeToInputPhraseFragment();

    }

    public void setFragment(Fragment fragment, int animationType) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.fragment_container) == null) {
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        } else {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            setFragmentTransitionAnimation(transaction, animationType);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    public void setFragmentTransitionAnimation (FragmentTransaction transaction, int animationType){
        Log.i(Constants.TAG, "Changing animation");
        switch (animationType){
            case 0:
                transaction.setCustomAnimations(R.anim.slide_up_in, R.anim.slide_up_out);
                break;
            case 1:
                transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case 2:
                transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            default:
                transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wreck_a_phrase) {
            changeToInputPhraseFragment();
        } else if (id == R.id.nav_favourites) {
            changeToFavouriteFragment();
        } else if (id == R.id.nav_about){
            changeToAboutFragment();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer != null) {
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void changeToAboutFragment() {
        setTitle(R.string.about);
        AboutFragment aboutFragment = new AboutFragment();
        setFragment(aboutFragment, Constants.UPWARD_TRANSITION);
    }

    private void changeToFavouriteFragment() {
        setTitle(R.string.my_favourites);
        FavouritesFragment favouritesFragment = new FavouritesFragment();
        setFragment(favouritesFragment, Constants.UPWARD_TRANSITION);
    }

    private void changeToInputPhraseFragment() {
        setTitle(R.string.wreck_a_phrase);
        InputPhraseFragment inputPhraseFragment = new InputPhraseFragment();
        setFragment(inputPhraseFragment, Constants.UPWARD_TRANSITION);
    }

    @Override
    public void onSubmitPhrase(String phrase, int transitionType) {
        // Create a bundle to send the phrase to the EditPhraseFragment
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PHRASE, phrase);

        // Create the EditPhraseFragment and transaction
        EditPhraseFragment editPhraseFragment = new EditPhraseFragment();
        editPhraseFragment.setArguments(bundle);
        setFragment(editPhraseFragment, transitionType);
    }

    @Override
    public void onWordChosenToEdit(String phrase, String word, int position) {
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PHRASE, phrase);
        bundle.putString(Constants.WORD, word);
        bundle.putInt(Constants.POSITION, position);

        EditWordFragment editWordFragment = new EditWordFragment();
        editWordFragment.setArguments(bundle);
        setFragment(editWordFragment, Constants.FORWARD_TRANSITION);
    }

    @Override
    public void newWordSubmitted(String phrase) {
        onSubmitPhrase(phrase, Constants.BACKWARD_TRANSITION);
    }

    @Override
    public void finalisePhrase(String phrase, long date) {
        Log.i(Constants.TAG, "Changing to view phrase now");
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PHRASE, phrase);
        bundle.putLong(Constants.CREATION_DATE, date);

        ViewPhraseFragment viewPhraseFragment = new ViewPhraseFragment();
        viewPhraseFragment.setArguments(bundle);
        setFragment(viewPhraseFragment, Constants.FORWARD_TRANSITION);
    }

    @Override
    public void createNewPhrase() {
        Log.i(Constants.TAG, "NEW PHRASE LEGOO");
        InputPhraseFragment inputPhraseFragment = new InputPhraseFragment();
        setFragment(inputPhraseFragment, Constants.BACKWARD_TRANSITION);
    }

    @Override
    public void viewFavourites() {
        changeToFavouriteFragment();
    }
}
