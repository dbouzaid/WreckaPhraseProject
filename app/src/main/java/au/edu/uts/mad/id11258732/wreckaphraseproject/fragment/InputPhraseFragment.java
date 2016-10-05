package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Utilities;

/**
 * InputPhraseFragment Class
 * <p>
 * Used to display the view associated with the process of giving a phrase to wreck
 */
public class InputPhraseFragment extends Fragment implements View.OnClickListener {

    // Button to submit phrase
    private Button mSubmitButton;
    // Where the user will type their phrase
    private EditText mPhraseInput;

    public InputPhraseFragment() {
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
        View myFragmentView = inflater.inflate(R.layout.fragment_input_phrase, container, false);

        mSubmitButton = (Button) myFragmentView.findViewById(R.id.fragment_input_phrase_submit_phrase_button);
        mSubmitButton.setOnClickListener(this);
        mPhraseInput = (EditText) myFragmentView.findViewById(R.id.fragment_input_phrase_edittext);


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
            // Submit phrase was pressed
            case R.id.fragment_input_phrase_submit_phrase_button: {
                // Get the phrase from the edittext
                String phrase = mPhraseInput.getText().toString();
                if (phrase.equals("") || Pattern.matches(Constants.EMPTY_REGEX, phrase)) {
                    // No phrase found, give error
                    mPhraseInput.setError(getActivity().getString(R.string.empty_phrase_input));
                } else if (!Pattern.matches(Constants.PHRASE_REGEX, phrase)) {
                    // Phrase didn't match regex, send error
                    mPhraseInput.setError(getActivity().getString(R.string.incorrect_regex));
                } else {
                    // Phrase was valid, send off to be edited
                    ((OnSubmitPhraseListener) getActivity()).onSubmitPhrase(phrase.trim(), Constants.FORWARD_TRANSITION);
                    Utilities.closeKeyboard(getActivity(), mPhraseInput.getWindowToken());
                }
            }
        }
    }

    /**
     * Used to communicate between Activity and this Fragment
     */
    public interface OnSubmitPhraseListener {
        void onSubmitPhrase(String phrase, int transitionType);
    }


}
