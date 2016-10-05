package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.regex.Pattern;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Utilities;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Word;
import au.edu.uts.mad.id11258732.wreckaphraseproject.service.WordsIntentService;

/**
 * EditWordFragment Class
 * <p>
 * Used to display the views for doing the process of Editing a Word
 */
public class EditWordFragment extends Fragment implements View.OnClickListener {

    // The word to change is displayed
    private TextView mTextViewWord;
    // Where the user can change the word
    private EditText mEditTextChangeWord;
    // Button to cancel edit
    private Button mButtonCancel;
    // Button to submit edit
    private Button mButtonSubmit;
    // Button to get a synonym
    private Button mButtonSynonym;
    // Button to get a rhyme
    private Button mButtonRhyme;
    // Store the word to be edited
    private Word mWord;
    // The phrase the word is from
    private Phrase mPhrase;
    // Position in the phrase's word list
    private int mPosition;
    // Progress bar while IntentService is working
    private ProgressBar mProgressBarWords;
    // Layout to hide while progress bar is visible
    private LinearLayout mLinearLayoutWordButtons;

    // Used to retrieve the new word to change to
    private ResponseReceiver mReceiver;

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
        View myFragmentView = inflater.inflate(R.layout.fragment_edit_word, container, false);

        mWord = new Word(getArguments().getString(Constants.WORD));
        mPhrase = new Phrase(getArguments().getString(Constants.PHRASE));
        mPosition = getArguments().getInt(Constants.POSITION);
        mTextViewWord = (TextView) myFragmentView.findViewById(R.id.edit_word_fragment_word_textview);
        mTextViewWord.setText(mWord.getWord());
        mEditTextChangeWord = (EditText) myFragmentView.findViewById(R.id.edit_word_fragment_word_edittext);
        mButtonCancel = (Button) myFragmentView.findViewById(R.id.edit_word_fragment_cancel_button);
        mButtonCancel.setOnClickListener(this);
        mButtonSubmit = (Button) myFragmentView.findViewById(R.id.edit_word_fragment_submit_button);
        mButtonSubmit.setOnClickListener(this);
        mButtonSynonym = (Button) myFragmentView.findViewById(R.id.edit_word_fragment_synonym_button);
        mButtonSynonym.setOnClickListener(this);
        mButtonRhyme = (Button) myFragmentView.findViewById(R.id.edit_word_fragment_rhyme_button);
        mButtonRhyme.setOnClickListener(this);
        mProgressBarWords = (ProgressBar) myFragmentView.findViewById(R.id.edit_word_fragment_word_progressbar);
        mLinearLayoutWordButtons = (LinearLayout) myFragmentView.findViewById(R.id.edit_word_fragment_word_buttons_linearlayout);

        return myFragmentView;
    }

    /**
     * Called when the fragment is resumed.
     * Registers the receiver to listen for Broadcasts the new word to change to
     */
    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new ResponseReceiver();
        getActivity().registerReceiver(mReceiver, filter);
    }

    /**
     * Called to when the fragment is paused.
     * Unregisters the Receiver to make sure there are no leaks.
     */
    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
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
            // Submit word pressed
            case R.id.edit_word_fragment_submit_button: {
                //Get the word they have inputted
                String word = mEditTextChangeWord.getText().toString();
                if (word.equals("") || Pattern.matches(Constants.EMPTY_REGEX, word)) {
                    // No word found, give them an error message
                    mEditTextChangeWord.setError(getActivity().getString(R.string.empty_word_input));
                } else if (!Pattern.matches(Constants.PHRASE_REGEX, word)) {
                    // Word doesn't match regex, givem them an error message
                    mEditTextChangeWord.setError(getActivity().getString(R.string.incorrect_regex));
                } else {
                    // Create a Word containing the new word and send to be inserted in the phrase
                    Word newWord = new Word(word + mWord.getPunctuation());
                    mPhrase.changeWord(mPosition, newWord);
                    String newPhrase = mPhrase.buildPhrase();
                    // Close keyboard if it is open
                    Utilities.closeKeyboard(getActivity(), mEditTextChangeWord.getWindowToken());
                    ((OnNewWordSubmittedListener) getActivity()).newWordSubmitted(newPhrase);
                }
                break;
            }
            // Cancel was selected
            case R.id.edit_word_fragment_cancel_button: {
                // Ignore all changes and go back to phrase and close keyboard
                Utilities.closeKeyboard(getActivity(), mEditTextChangeWord.getWindowToken());
                ((OnNewWordSubmittedListener) getActivity()).newWordSubmitted(mPhrase.buildPhrase());
                break;
            }
            // Synonym pressed
            case R.id.edit_word_fragment_synonym_button: {
                // Check if connected to internet
                if (Utilities.isNetworkConnected(getActivity())) {
                    // Create an Intent to retrieve a synonym and show a progress bar
                    Intent intent = new Intent(getActivity(), WordsIntentService.class);
                    intent.putExtra(Constants.WORD_TO_CHANGE, mTextViewWord.getText().toString());
                    intent.putExtra(Constants.TYPE_OF_CHANGE, Constants.GET_SYNONYM);
                    getActivity().startService(intent);
                    mLinearLayoutWordButtons.setVisibility(View.GONE);
                    mProgressBarWords.setVisibility(View.VISIBLE);
                } else {
                    //Give a message they must be connected to the internet
                    noConnectionFound();
                }
                break;
            }
            // Rhyme pressed
            case R.id.edit_word_fragment_rhyme_button: {
                // Check if connected to internet
                if (Utilities.isNetworkConnected(getActivity())) {
                    // Create an Intent to retrieve a rhyme and show a progress bar
                    Intent intent = new Intent(getActivity(), WordsIntentService.class);
                    intent.putExtra(Constants.WORD_TO_CHANGE, mTextViewWord.getText().toString());
                    intent.putExtra(Constants.TYPE_OF_CHANGE, Constants.GET_RHYME);
                    getActivity().startService(intent);
                    mLinearLayoutWordButtons.setVisibility(View.GONE);
                    mProgressBarWords.setVisibility(View.VISIBLE);
                } else {
                    // Give a message that they must be connected to the internet
                    noConnectionFound();
                }
                break;
            }
        }
    }

    /**
     * Used to listen for the new word retrieved from the WordsIntentService
     */
    public class ResponseReceiver extends BroadcastReceiver {

        /**
         * Invoked when the Intent has a arrived with the new word
         *
         * @param context
         * @param intent
         */
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(Constants.NEW_WORD_RECEIVED);
            int typeOfWord = intent.getIntExtra(Constants.TYPE_OF_CHANGE, -1);
            //Restore visibility back to normal
            mLinearLayoutWordButtons.setVisibility(View.VISIBLE);
            mProgressBarWords.setVisibility(View.GONE);
            // Check if a word was found
            if (text == null) {
                // No word found, give message depending on what word wasn't found
                if (typeOfWord == Constants.GET_RHYME) {
                    Snackbar.make(getActivity().findViewById(R.id.main_activity_root_coordinatorlayout), R.string.no_rhyme, Snackbar.LENGTH_SHORT).show();
                } else if (typeOfWord == Constants.GET_SYNONYM) {
                    Snackbar.make(getActivity().findViewById(R.id.main_activity_root_coordinatorlayout), R.string.no_synonym, Snackbar.LENGTH_SHORT).show();
                }
            } else {
                // Word was found, insert it into EditText
                mEditTextChangeWord.setText(text);
            }
        }
    }

    /**
     * Created a snackbar to display a message that the user must be connected to the internet
     */
    private void noConnectionFound() {
        Snackbar.make(getActivity().findViewById(R.id.main_activity_root_coordinatorlayout), R.string.no_connection, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Interface to communicate between Activty and this Fragment
     */
    public interface OnNewWordSubmittedListener {
        void newWordSubmitted(String phrase);
    }
}
