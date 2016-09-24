package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * Created by Boozy on 22/05/2016.
 */
public class EditWordFragment extends Fragment implements View.OnClickListener {

    private TextView mTextViewWord;
    private EditText mEditTextChangeWord;
    private Button mButtonCancel;
    private Button mButtonSubmit;
    private Button mButtonSynonym;
    private Button mButtonRhyme;
    private Word mWord;
    private Phrase mPhrase;
    private int mPosition;
    private ProgressBar mProgressBarWords;
    private LinearLayout mLinearLayoutWordButtons;

    private ResponseReceiver mReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View myFragmentView = inflater.inflate(R.layout.fragment_edit_word, container, false);

        mWord = new Word(getArguments().getString(Constants.WORD));
        Log.i(Constants.TAG, "Word passed in " + mWord.getFullWord());
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

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(Constants.ACTION_RESP);
        filter.addCategory(Intent.CATEGORY_DEFAULT);
        mReceiver = new ResponseReceiver();
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_word_fragment_submit_button: {
                String word = mEditTextChangeWord.getText().toString();
                if (word.equals("")) {
                    mEditTextChangeWord.setError(getActivity().getString(R.string.empty_word_input));
                } else if (!Pattern.matches(Constants.PHRASE_REGEX, word)) {
                    mEditTextChangeWord.setError(getActivity().getString(R.string.incorrect_regex));
                } else {
                    Word newWord = new Word(word + mWord.getPunctuation());
                    mPhrase.changeWord(mPosition, newWord);
                    String newPhrase = mPhrase.buildPhrase();
                    Utilities.closeKeyboard(getActivity(), mEditTextChangeWord.getWindowToken());
                    ((OnNewWordSubmittedListener) getActivity()).newWordSubmitted(newPhrase);
                }
                break;
            }
            case R.id.edit_word_fragment_cancel_button: {
                Utilities.closeKeyboard(getActivity(), mEditTextChangeWord.getWindowToken());
                ((OnNewWordSubmittedListener) getActivity()).newWordSubmitted(mPhrase.buildPhrase());
                break;
            }
            case R.id.edit_word_fragment_synonym_button: {
                if (Utilities.isNetworkConnected(getActivity())) {
                    Intent intent = new Intent(getActivity(), WordsIntentService.class);
                    intent.putExtra(Constants.WORD_TO_CHANGE, mTextViewWord.getText().toString());
                    intent.putExtra(Constants.TYPE_OF_CHANGE, Constants.GET_SYNONYM);
                    getActivity().startService(intent);
                    mLinearLayoutWordButtons.setVisibility(View.GONE);
                    mProgressBarWords.setVisibility(View.VISIBLE);
                } else {
                    noConnectionFound();
                }
                break;
            }
            case R.id.edit_word_fragment_rhyme_button: {
                if (Utilities.isNetworkConnected(getActivity())) {
                    Intent intent = new Intent(getActivity(), WordsIntentService.class);
                    intent.putExtra(Constants.WORD_TO_CHANGE, mTextViewWord.getText().toString());
                    intent.putExtra(Constants.TYPE_OF_CHANGE, Constants.GET_RHYME);
                    getActivity().startService(intent);
                    mLinearLayoutWordButtons.setVisibility(View.GONE);
                    mProgressBarWords.setVisibility(View.VISIBLE);
                } else {
                    noConnectionFound();
                }
                break;
            }
        }
    }

    public class ResponseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra(Constants.NEW_WORD_RECEIVED);
            int typeOfWord = intent.getIntExtra(Constants.TYPE_OF_CHANGE, -1);
            mLinearLayoutWordButtons.setVisibility(View.VISIBLE);
            mProgressBarWords.setVisibility(View.GONE);
            if (text == null) {
                if (typeOfWord == Constants.GET_RHYME) {
                    Snackbar.make(getActivity().findViewById(R.id.main_activity_root_coordinatorlayout), R.string.no_rhyme, Snackbar.LENGTH_SHORT).show();
                } else if (typeOfWord == Constants.GET_SYNONYM) {
                    Snackbar.make(getActivity().findViewById(R.id.main_activity_root_coordinatorlayout), R.string.no_synonym, Snackbar.LENGTH_SHORT).show();
                }
            } else {
                mEditTextChangeWord.setText(text);
            }
        }
    }

    private void noConnectionFound() {
        Snackbar.make(getActivity().findViewById(R.id.main_activity_root_coordinatorlayout), R.string.no_connection, Snackbar.LENGTH_SHORT).show();
    }

    public interface OnNewWordSubmittedListener {
        void newWordSubmitted(String phrase);
    }
}
