package au.edu.uts.mad.id11258732.wreckaphraseproject.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Word;

/**
 * Created by Daniel on 18/05/2016.
 */
public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    private Phrase mPhrase;
    private ArrayList<Word> mWordsList;
    private Activity mActivity;

    public WordsAdapter(Phrase phrase, Activity activity) {
        mPhrase = phrase;
        mWordsList = mPhrase.getWordsInPhrase();
        mActivity = activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_words, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Word currentWord = mWordsList.get(position);
        holder.mTextViewWord.setText(currentWord.getFullWord());
        Log.i(Constants.TAG, "View text set at " + position + " with the word of " + currentWord.getWord());
    }

    @Override
    public int getItemCount() {
        return mWordsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextViewWord;
        private CardView mCardView;

        public ViewHolder(View itemView) {
            super(itemView);

            mTextViewWord = (TextView) itemView.findViewById(R.id.adapter_words_textview);
            mCardView = (CardView) itemView.findViewById(R.id.adapter_words_cardview);
            mCardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.adapter_words_cardview: {
                    Word word = mWordsList.get(getAdapterPosition());
                    Log.i(Constants.TAG, "This word was pressed: " + word.getFullWord());
                    ((OnWordChosenToEditListener) mActivity).onWordChosenToEdit(mPhrase.getPhrase(),
                            word.getFullWord(), getAdapterPosition());
                }
            }
        }
    }

    public interface OnWordChosenToEditListener {
        void onWordChosenToEdit(String phrase, String word, int position);
    }
}
