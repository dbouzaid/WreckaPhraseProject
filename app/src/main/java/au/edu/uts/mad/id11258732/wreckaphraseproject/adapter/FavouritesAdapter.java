/**
 * ExpandableLayout : Copyright (C) 2015 A.Akira
 * RecyclerView-Animations: Copyright 2015 Wasabeef
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package au.edu.uts.mad.id11258732.wreckaphraseproject.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListenerAdapter;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;
import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;
import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.PhraseManager;
import au.edu.uts.mad.id11258732.wreckaphraseproject.R;
import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Utilities;

/**
 * FavouritesAdapter Class
 * <p>
 * FavouritesAdapter provides a binding from an ArrayList of Phrases to the views that are displayed
 * within a RecyclerView
 * Provides TextToSpeech capabilities to each element on the list
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> implements TextToSpeech.OnInitListener {

    // List of phrases retrieved from the database.
    private ArrayList<Phrase> mPhrases;

    // Needed to keep track of the open and closed state of each view in the recycler view.
    private SparseBooleanArray mExpandState = new SparseBooleanArray();

    // Reference to the activity.
    private Activity mActivity;

    // TextToSpeech instance used to read favourite phrases.
    private TextToSpeech mTts;

    /**
     * Constructs the adapter and sets each expanded state of phrases in the array to closed "false"
     *
     * @param phrases  that were passed through from the database
     * @param activity instance of the current activity
     */
    public FavouritesAdapter(ArrayList<Phrase> phrases, Activity activity) {
        mPhrases = phrases;
        mActivity = activity;
        // Loop through the List of phrases and set state to closed
        for (int i = 0; i < mPhrases.size(); i++) {
            mExpandState.append(i, false);
        }
        mTts = new TextToSpeech(mActivity, this);
    }

    /**
     * Creates a ViewHolder that has the needed XML layout to display
     * content in the View.
     *
     * @param parent
     * @param viewType
     * @return the ViewHolder to use in the recyclerview
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_favourites, parent, false);
        return new ViewHolder(view);
    }


    /**
     * Injects data into the given view holder into it's respective element
     *
     * @param holder   ViewHolder in the RecyclerView
     * @param position of the ViewHolder in the RecyclerView
     */
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // Get the phrase for te current position
        Phrase phrase = mPhrases.get(position);
        // Start putting the data into their views
        Date date = new Date(phrase.getCreationDate());
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        holder.mTextViewDate.setText(mActivity.getString(R.string.created_on) + sdf.format(date));
        holder.mTextViewPhrase.setText(phrase.getPhrase());
        holder.mButtonSend.setOnClickListener(holder);
        holder.mButtonUnfavourite.setOnClickListener(holder);
        holder.mRelativeLayoutTriangle.setOnClickListener(holder);
        holder.mRelativeLayoutTts.setOnClickListener(holder);
        // Check if layout should be expanded or not
        holder.mExpandableLayout.setExpanded(mExpandState.get(position));
        // Set a listener to know when to expand the view
        holder.mExpandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            /**
             * Called before the view is opened and start the animation and state to open
             */
            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.mViewTriangle, 0f, 180f).start();
                mExpandState.put(position, true);
            }

            /**
             * Called before the view is closed and start the animation and state to close
             */
            @Override
            public void onPreClose() {
                createRotateAnimator(holder.mViewTriangle, 180f, 0f).start();
                mExpandState.put(position, false);
            }
        });

        holder.mViewTriangle.setRotation(mExpandState.get(position) ? 180f : 0f);
    }

    /**
     * @return the current size of the ArrayList of Phrase classes to determine size of RecyclerView
     */
    @Override
    public int getItemCount() {
        return mPhrases.size();
    }

    /**
     * Initials the language of the TextToSpeech Class when it initialises successfully
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
     * ViewHolder Class
     * Used to display the data of the Favourited Phrase for one view in the RecyclerView
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextViewPhrase;
        private TextView mTextViewDate;
        private View mViewTriangle;
        private ExpandableLinearLayout mExpandableLayout;
        private Button mButtonSend;
        private Button mButtonUnfavourite;
        private RelativeLayout mRelativeLayoutTriangle;
        private RelativeLayout mRelativeLayoutTts;

        /**
         * Construct the view holder and assign the values to the pass View
         *
         * @param itemView View to populate with elements to display
         */
        public ViewHolder(View itemView) {
            super(itemView);

            mTextViewPhrase = (TextView) itemView.findViewById(R.id.adapter_favourites_phrase_textview);
            mTextViewDate = (TextView) itemView.findViewById(R.id.adapter_favourites_date_textview);
            mExpandableLayout = (ExpandableLinearLayout) itemView.findViewById(R.id.adapter_favourites_expandablelayout);
            mButtonSend = (Button) itemView.findViewById(R.id.adapter_favourites_send_button);
            mButtonUnfavourite = (Button) itemView.findViewById(R.id.adapter_favourites_unfavourite_button);
            mViewTriangle = itemView.findViewById(R.id.adapter_favourites_triangle_view);
            mRelativeLayoutTriangle = (RelativeLayout) itemView.findViewById(R.id.adapter_favourites_triangle_relativelayout);
            mRelativeLayoutTts = (RelativeLayout) itemView.findViewById(R.id.adapter_favourites_tts_relativelayout);
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
                // The Drop down triangle as pressed
                case R.id.adapter_favourites_triangle_relativelayout:
                    // Either open or close the expanded layout
                    mExpandableLayout.toggle();
                    break;
                // The TTS button was pressed
                case R.id.adapter_favourites_tts_relativelayout:
                    // Get the TTS to speak the associated phrase
                    mTts.speak(mPhrases.get(getAdapterPosition()).getPhrase(), TextToSpeech.QUEUE_FLUSH, null, null);
                    break;
                // Send was pressed
                case R.id.adapter_favourites_send_button:
                    // Send the associated phrase to someone
                    Utilities.sendPhrase(mActivity, mPhrases.get(getAdapterPosition()));
                    break;
                // Unfavourite was pressed
                case R.id.adapter_favourites_unfavourite_button:
                    // Access the database and remove the phrase from favourites and notify the change
                    PhraseManager phraseManager = PhraseManager.getInstance(mActivity);
                    phraseManager.removeFavourite(mPhrases.get(getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                    mExpandableLayout.toggle();
                    break;
            }
        }
    }

    /**
     * Animates the spin on the Expand and Collapse button on each view in the
     * RecyclerView
     *
     * @param target
     * @param from
     * @param to
     * @return
     */
    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, mActivity.getString(R.string.rotation), from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
