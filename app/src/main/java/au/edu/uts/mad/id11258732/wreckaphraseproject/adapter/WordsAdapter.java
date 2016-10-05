package au.edu.uts.mad.id11258732.wreckaphraseproject.adapter;

import android.app.Activity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Word;

/**
 * WordsAdapter Class
 * <p>
 * Words Adapter provides a bing from an ArrayList of Words to the views that are displayed
 * within a RecyclerView
 */
public class WordsAdapter extends RecyclerView.Adapter<WordsAdapter.ViewHolder> {

    // The phrase that contains all the words to be displayed
    private Phrase mPhrase;
    // List of the words to be displayed
    private ArrayList<Word> mWordsList;
    // Instance of the current activity
    private Activity mActivity;

    public WordsAdapter(Phrase phrase, Activity activity) {
        mPhrase = phrase;
        mWordsList = mPhrase.getWordsInPhrase();
        mActivity = activity;
    }

    /**
     * Creates a ViewHolder that has the needed XML layout to display the content
     * in the View.
     *
     * @param parent
     * @param viewType
     * @return the View to be used to display data
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_words, parent, false);
        return new ViewHolder(view);
    }


    /**
     * Injects data into the given view into it's respective element
     *
     * @param holder   ViewHolder in the RecyclerView
     * @param position of the ViewHolder in the RecyclerView
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Word currentWord = mWordsList.get(position);
        holder.mTextViewWord.setText(currentWord.getFullWord());
    }

    /**
     * @return the current size of the ArrayList of Word classes to determine size of RecyclerView
     */
    @Override
    public int getItemCount() {
        return mWordsList.size();
    }


    /**
     * ViewHolder Class
     * Used to display the data of one word of the Phrase in one view in the RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextViewWord;
        private CardView mCardView;

        /**
         * Construct the view holder and assign the values to the pass View
         *
         * @param itemView View to populate with elements to display
         */
        public ViewHolder(View itemView) {
            super(itemView);

            mTextViewWord = (TextView) itemView.findViewById(R.id.adapter_words_textview);
            mCardView = (CardView) itemView.findViewById(R.id.adapter_words_cardview);
            mCardView.setOnClickListener(this);
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
                // A Word is selected
                case R.id.adapter_words_cardview: {
                    // Get the word from the list based on it's position
                    Word word = mWordsList.get(getAdapterPosition());
                    // Send it to the activity to be used for editing a word
                    ((OnWordChosenToEditListener) mActivity).onWordChosenToEdit(mPhrase.getPhrase(),
                            word.getFullWord(), getAdapterPosition());
                }
            }
        }
    }

    /**
     * Interface to communicate between Fragments through the activity
     */
    public interface OnWordChosenToEditListener {
        void onWordChosenToEdit(String phrase, String word, int position);
    }
}
