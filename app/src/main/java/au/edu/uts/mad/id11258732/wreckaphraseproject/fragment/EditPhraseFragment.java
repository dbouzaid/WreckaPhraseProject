package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.adapter.WordsAdapter;

/**
 * EditPhraseFragment Class
 *
 * Used to display the View that allows the user to Edit a Phrase of the class
 * Allows the user to select one word to edit.
 */
public class EditPhraseFragment extends Fragment implements View.OnClickListener {

    // Phrase to be used to display the words to edit
    private Phrase mPhrase;
    // The RecyclerView to display the Words to edit
    private RecyclerView mWordsRecyclerView;
    //The Adapter to bind to display the Words
    private WordsAdapter mWordsAdapter;
    //Used to finish editing a phrase
    private Button mButtonFinalise;

    public EditPhraseFragment() {
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return the view to be displayed
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View myFragmentView = inflater.inflate(R.layout.fragment_edit_phrase, container, false);

        mPhrase = new Phrase(getArguments().getString(Constants.PHRASE));

        mWordsRecyclerView = (RecyclerView) myFragmentView.findViewById(R.id.edit_phrase_fragment_phrase_recyclerview);

        mWordsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mWordsAdapter = new WordsAdapter(mPhrase, getActivity());
        mWordsRecyclerView.setAdapter(mWordsAdapter);

        mButtonFinalise = (Button) myFragmentView.findViewById(R.id.edit_phrase_fragment_submit_button);
        mButtonFinalise.setOnClickListener(this);

        return myFragmentView;
    }

    /**
     * Invoked when an element in the RecyclerView is pressed
     * and determines what to do based on what view was clicked
     *
     * @param v What view was clicked
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_phrase_fragment_submit_button: {
                // Notify that the phrase is done with editting and move to view it
                mPhrase.setCreationDate(getDate());
                ((OnFinalisePhrase) getActivity()).finalisePhrase(mPhrase.buildPhrase(), mPhrase.getCreationDate());
                break;
            }
        }
    }

    /**
     * Get the current date so we know when the phrase was made
     * @return current date of phrase creation
     */
    public long getDate() {
        Calendar cal = Calendar.getInstance();
        long date = cal.getTimeInMillis();
        return date;
    }

    /**
     * Interface to communicate between Activity and Fragment
     */
    public interface OnFinalisePhrase {
        void finalisePhrase(String phrase, long date);
    }
}
