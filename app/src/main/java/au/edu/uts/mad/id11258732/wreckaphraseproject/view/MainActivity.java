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

/**
 * MainActivity Class
 * This class acts as the main activity on startup for the Wreck a Phrase application
 * It handles what is displayed on the screen, implements listeners to
 * act upon to invoke certain methods, manages fragment transitions
 * and implements methods used to communicate between fragments
 */
public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        InputPhraseFragment.OnSubmitPhraseListener, WordsAdapter.OnWordChosenToEditListener,
        EditWordFragment.OnNewWordSubmittedListener, EditPhraseFragment.OnFinalisePhrase,
        ViewPhraseFragment.OnChangeFromViewPhraseFragment {

    /**
     * Called when the activity has been created
     * Sets up the toolbar and navigation drawer and the first fragment to be viewed.
     * @param savedInstanceState
     */
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

        //Show the inputphrasefragment on startup
        changeToInputPhraseFragment();

    }

    /**
     * Used to commit the fragment transactions
     * @param fragment to be displayed
     * @param animationType type of animation to use
     */
    public void setFragment(Fragment fragment, int animationType) {
        FragmentManager fm = getSupportFragmentManager();
        if (fm.findFragmentById(R.id.fragment_container) == null) {
            // No fragment was found in the container, so just commit
            fm.beginTransaction().add(R.id.fragment_container, fragment).commit();
        } else {
            // Fragment was found, replace the old fragment and add the animation
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            setFragmentTransitionAnimation(transaction, animationType);
            transaction.replace(R.id.fragment_container, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
        }

    }

    /**
     * Sets the animation based on what animation type was passed through
     * @param transaction
     * @param animationType
     */
    public void setFragmentTransitionAnimation (FragmentTransaction transaction, int animationType){
        switch (animationType){
            case Constants.UPWARD_TRANSITION:
                transaction.setCustomAnimations(R.anim.slide_up_in, R.anim.slide_up_out);
                break;
            case Constants.FORWARD_TRANSITION:
                transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
            case Constants.BACKWARD_TRANSITION:
                transaction.setCustomAnimations(R.anim.slide_right_in, R.anim.slide_right_out);
                break;
            default:
                transaction.setCustomAnimations(R.anim.slide_left_in, R.anim.slide_left_out);
                break;
        }
    }

    /**
     * Handles if the back button was pressed while the drawer was open
     */
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

    /**
     * Performs the methods based on which menu item was selected
     * @param item
     * @return
     */
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

    /**
     * Used to change to the About fragment and change title
     */
    private void changeToAboutFragment() {
        setTitle(R.string.about);
        AboutFragment aboutFragment = new AboutFragment();
        setFragment(aboutFragment, Constants.UPWARD_TRANSITION);
    }

    /**
     * Used to change to view My Favourites and change title
     */
    private void changeToFavouriteFragment() {
        setTitle(R.string.my_favourites);
        FavouritesFragment favouritesFragment = new FavouritesFragment();
        setFragment(favouritesFragment, Constants.UPWARD_TRANSITION);
    }

    /**
     * Used to change the view to the InputPhraseFragment and change title
     */
    private void changeToInputPhraseFragment() {
        setTitle(R.string.wreck_a_phrase);
        InputPhraseFragment inputPhraseFragment = new InputPhraseFragment();
        setFragment(inputPhraseFragment, Constants.UPWARD_TRANSITION);
    }

    /**
     * Bundles the data passed back by the inputphrasefragment to be sent to EditPhraseFragment
     * to edit the passed phrase
     * @param phrase submitted to be edited
     * @param transitionType animation type
     */
    @Override
    public void onSubmitPhrase(String phrase, int transitionType) {
        // Create a bundle to send the phrase to the EditPhraseFragment
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PHRASE, phrase);

        // Create the EditPhraseFragment and start transaction
        EditPhraseFragment editPhraseFragment = new EditPhraseFragment();
        editPhraseFragment.setArguments(bundle);
        setFragment(editPhraseFragment, transitionType);
    }

    /**
     * Bundles the data passed to send to the EditWordFragment
     * @param phrase
     * @param word
     * @param position
     */
    @Override
    public void onWordChosenToEdit(String phrase, String word, int position) {
        // Create a bundle to send the phrase to the EditWordFragment
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PHRASE, phrase);
        bundle.putString(Constants.WORD, word);
        bundle.putInt(Constants.POSITION, position);

        // Create the EditWordFragment and start transaction
        EditWordFragment editWordFragment = new EditWordFragment();
        editWordFragment.setArguments(bundle);
        setFragment(editWordFragment, Constants.FORWARD_TRANSITION);
    }

    /**
     * Calls the onSubmitPhrase method to swap to EditPhraseFragment with the current edited phrase
     * @param phrase
     */
    @Override
    public void newWordSubmitted(String phrase) {
        onSubmitPhrase(phrase, Constants.BACKWARD_TRANSITION);
    }

    /**
     * Bundles the data passed to start the ViewPhraseFragment
     * @param phrase
     * @param date
     */
    @Override
    public void finalisePhrase(String phrase, long date) {
        // Create a bundle to send the phrase to the ViewPhraseFragment
        Bundle bundle = new Bundle();
        bundle.putString(Constants.PHRASE, phrase);
        bundle.putLong(Constants.CREATION_DATE, date);

        // Create the ViewPhraseFragment and start transaction
        ViewPhraseFragment viewPhraseFragment = new ViewPhraseFragment();
        viewPhraseFragment.setArguments(bundle);
        setFragment(viewPhraseFragment, Constants.FORWARD_TRANSITION);
    }

    /**
     * Creates a new input phrase fragment to with the backward transition
     */
    @Override
    public void createNewPhrase() {
        // Create the InputPhraseFragment and start transaction
        InputPhraseFragment inputPhraseFragment = new InputPhraseFragment();
        setFragment(inputPhraseFragment, Constants.BACKWARD_TRANSITION);
    }

    /**
     * Opens up the FavouritesFragment
     */
    @Override
    public void viewFavourites() {
        changeToFavouriteFragment();
    }
}
