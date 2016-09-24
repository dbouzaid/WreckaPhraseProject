package au.edu.uts.mad.id11258732.wreckaphraseproject.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.adapter.WordsAdapter;

public class EditPhraseFragment extends Fragment implements View.OnClickListener {

    private Phrase mPhrase;
    private RecyclerView mWordsRecyclerView;
    private WordsAdapter mWordsAdapter;
    private Button mButtonFinalise;

    public EditPhraseFragment() {
    }


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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.edit_phrase_fragment_submit_button: {
                mPhrase.setCreationDate(getDate());
                Log.i(Constants.TAG, "Got Date");
                ((OnFinalisePhrase) getActivity()).finalisePhrase(mPhrase.buildPhrase(), mPhrase.getCreationDate());
                Log.i(Constants.TAG, "Finalised Phrase");
                break;
            }
        }
    }

    public long getDate() {
        Calendar cal = Calendar.getInstance();
        long date = cal.getTimeInMillis();
        return date;
    }

    public interface OnFinalisePhrase {
        void finalisePhrase(String phrase, long date);
    }
}
