package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.edu.uts.mad.id11258732.wreckaphraseproject.R;

/**
 * AboutFragment Class
 *
 * A Simple fragment to display information about the author of the program
 * and credits
 */
public class AboutFragment extends Fragment {

    /**
     * Load up the fragment inflated with the about layout to display the content
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_about, container, false);

        return myFragmentView;
    }
}
