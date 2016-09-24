package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Utilities;


public class InputPhraseFragment extends Fragment implements View.OnClickListener {

    private Button mSubmitButton;
    private EditText mPhraseInput;

    public InputPhraseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_input_phrase, container, false);

        mSubmitButton = (Button) myFragmentView.findViewById(R.id.fragment_input_phrase_submit_phrase_button);
        mSubmitButton.setOnClickListener(this);
        mPhraseInput = (EditText) myFragmentView.findViewById(R.id.fragment_input_phrase_edittext);


        return myFragmentView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_input_phrase_submit_phrase_button: {
                String phrase = mPhraseInput.getText().toString();
                if (phrase.equals("")) {
                    mPhraseInput.setError(getActivity().getString(R.string.empty_phrase_input));
                } else if (!Pattern.matches(Constants.PHRASE_REGEX, phrase)) {
                    mPhraseInput.setError(getActivity().getString(R.string.incorrect_regex));
                } else {
                    ((OnSubmitPhraseListener) getActivity()).onSubmitPhrase(phrase.trim(), Constants.FORWARD_TRANSITION);
                    Utilities.closeKeyboard(getActivity(), mPhraseInput.getWindowToken());
                    Log.i("WRECK", "Item Clicked");
                }
            }
        }
    }

    public interface OnSubmitPhraseListener {
        void onSubmitPhrase(String phrase, int transitionType);
    }


}
