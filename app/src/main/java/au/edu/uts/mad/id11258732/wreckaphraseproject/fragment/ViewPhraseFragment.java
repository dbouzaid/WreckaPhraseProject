package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
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
 * Created by Boozy on 23/05/2016.
 */
public class ViewPhraseFragment extends Fragment implements View.OnClickListener, TextToSpeech.OnInitListener {

    private Phrase mPhrase;
    private TextView mTextViewPhrase;
    private Button mButtonSend;
    private Button mButtonStartAgain;
    private FloatingActionButton mFab;
    private TextToSpeech mTts;
    private ImageView mImageViewTts;


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_phrase_fragment_favourite_fab:
                addToFavourites(mTextViewPhrase.getText().toString(), mPhrase.getCreationDate());
                mFab.hide();
                Snackbar.make(v, R.string.added_favourite, Snackbar.LENGTH_LONG).setAction(R.string.view_favourites, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ((OnChangeFromViewPhraseFragment) getActivity()).viewFavourites();
                    }
                }).show();
                break;
            case R.id.view_phrase_fragment_send_button:
                Utilities.sendPhrase(getActivity(), mPhrase);
                break;
            case R.id.view_phrase_fragment_start_again_button:
                ((OnChangeFromViewPhraseFragment) getActivity()).createNewPhrase();
                break;
            case R.id.view_phrase_fragment_tts_imageview:
                mTts.speak(mTextViewPhrase.getText().toString(), TextToSpeech.QUEUE_FLUSH, null, null);
                break;
        }
    }

    private void addToFavourites(String phrase, long date) {
        PhraseManager phraseManager = PhraseManager.getInstance(getActivity());
        phraseManager.addFavourite(new Phrase(phrase, date));
        Log.i(Constants.TAG, "Added to favourites by method");
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTts.setLanguage(Locale.ENGLISH);
        }
    }

    public interface OnChangeFromViewPhraseFragment {
        void createNewPhrase();
        void viewFavourites();
    }
}
