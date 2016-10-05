package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;
import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.PhraseManager;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Utilities;

/**
 * ViewPhraseFragment Class
 * <p>
 * Used to display the View when the user has finalised their phrase and present them with
 * options to do with the prase
 */
public class ViewPhraseFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {

    // The finalised phrase
    private Phrase mPhrase;
    // Display the Phrase
    private TextView mTextViewPhrase;
    // Button to send phrase
    private Button mButtonSend;
    // Button to make another phrase
    private Button mButtonStartAgain;
    // FAB to favourite the phrase
    private FloatingActionButton mFab;
    // TTS instance to speak the phrase
    private TextToSpeech mTts;
    // Used to use the TTS
    private ImageView mImageViewTts;

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
        View myFragmentView = inflater.inflate(R.layout.fragment_view_phrase, container, false);

        mPhrase = new Phrase(getArguments().getString(Constants.PHRASE), getArguments().getLong(Constants.CREATION_DATE));
        mTextViewPhrase = (TextView) myFragmentView.findViewById(R.id.view_phrase_fragment_phrase_textview);
        mTextViewPhrase.setText(mPhrase.getPhrase());
        mButtonSend = (Button) myFragmentView.findViewById(R.id.view_phrase_fragment_send_button);
        mButtonSend.setOnClickListener(this);
        mButtonStartAgain = (Button) myFragmentView.findViewById(R.id.view_phrase_fragment_start_again_button);
        mButtonStartAgain.setOnClickListener(this);
        mFab = (FloatingActionButton) myFragmentView.findViewById(R.id.view_phrase_fragment_favourite_fab);
        mFab.setOnClickListener(this);
        mImageViewTts = (ImageView) myFragmentView.findViewById(R.id.view_phrase_fragment_tts_imageview);
        mImageViewTts.setOnClickListener(this);

        mTts = new TextToSpeech(getContext(), this);

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
            // FAB was pressed
            case R.id.view_phrase_fragment_favourite_fab:
                // Add to favourites, hide the FAB and show a snackback with the action to favourites
                addToFavourites(mTextViewPhrase.getText().toString(), mPhrase.getCreationDate());
                mFab.hide();
                Snackbar.make(v, R.string.added_favourite, Snackbar.LENGTH_LONG)
                        .setAction(R.string.view_favourites, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                ((OnChangeFromViewPhraseFragment) getActivity()).viewFavourites();
                            }
                        }).show();
                break;
            // Send was pressed
            case R.id.view_phrase_fragment_send_button:
                // Start the process to send a phrase
                Utilities.sendPhrase(getActivity(), mPhrase);
                break;
            // Start again was pressed
            case R.id.view_phrase_fragment_start_again_button:
                // Go to the beginning of the process
                ((OnChangeFromViewPhraseFragment) getActivity()).createNewPhrase();
                break;
            // TTS was pressed
            case R.id.view_phrase_fragment_tts_imageview:
                // Get the TTS to speak the phrase
                mTts.speak(mTextViewPhrase.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                break;
        }
    }

    /**
     * Add the phrase to the favourites table in the databs
     *
     * @param phrase
     * @param date   of creation
     */
    private void addToFavourites(String phrase, long date) {
        PhraseManager phraseManager = PhraseManager.getInstance(getActivity());
        phraseManager.addFavourite(new Phrase(phrase, date));
    }

    /**
     * Checks the status of the TTS when it was intialised and set it to English
     * if it was successful on intiation
     *
     * @param status
     */
    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTts.setLanguage(Locale.ENGLISH);
        }
    }

    /**
     * Interface to communicate between Activity and Fragment
     */
    public interface OnChangeFromViewPhraseFragment {
        void createNewPhrase();

        void viewFavourites();
    }
}
