package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.PhraseManager;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.adapter.FavouritesAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;
import jp.wasabeef.recyclerview.animators.LandingAnimator;

/**
 * FavouritesFragment Class
 * <p>
 * Used to display the view of the list of their favourite phrases
 */
public class FavouritesFragment extends Fragment {

    // Recycler view to displa the favourites
    private RecyclerView mRecyclerViewFavourites;
    // Adapter to bind the data to the RecyclerView
    private FavouritesAdapter mFavouritesAdapter;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view to be displayed
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_favourites, container, false);

        mRecyclerViewFavourites = (RecyclerView) myFragmentView.findViewById(R.id.favourites_fragment_favourite_phrase_recyclerview);
        mRecyclerViewFavourites.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewFavourites.setItemAnimator(new LandingAnimator());

        // Retrieve favourites from database
        PhraseManager phraseManager = PhraseManager.getInstance(getActivity());
        mFavouritesAdapter = new FavouritesAdapter(phraseManager.getFavourites(), getActivity());
        mRecyclerViewFavourites.setAdapter(new ScaleInAnimationAdapter(mFavouritesAdapter));

        return myFragmentView;
    }
}
