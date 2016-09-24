package au.edu.uts.mad.id11258732.wreckaphraseproject.adapter;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.speech.tts.TextToSpeech;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
 * Created by Boozy on 31/05/2016.
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> implements TextToSpeech.OnInitListener {

    private ArrayList<Phrase> mPhrases;
    private SparseBooleanArray mExpandState = new SparseBooleanArray();
    private Activity mActivity;
    private TextToSpeech mTts;

    public FavouritesAdapter(ArrayList<Phrase> phrases, Activity activity) {
        mPhrases = phrases;
        mActivity = activity;
        for (int i = 0; i < mPhrases.size(); i++) {
            mExpandState.append(i, false);
        }
        mTts = new TextToSpeech(mActivity, this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_favourites, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Phrase phrase = mPhrases.get(position);
        Date date = new Date(phrase.getCreationDate());
        SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT);
        holder.mTextViewDate.setText(mActivity.getString(R.string.created_on) + sdf.format(date));
        holder.mTextViewPhrase.setText(phrase.getPhrase());
        holder.mButtonSend.setOnClickListener(holder);
        holder.mButtonUnfavourite.setOnClickListener(holder);
        holder.mRelativeLayoutTriangle.setOnClickListener(holder);
        holder.mRelativeLayoutTts.setOnClickListener(holder);
        holder.mExpandableLayout.setExpanded(mExpandState.get(position));
        holder.mExpandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
                createRotateAnimator(holder.mViewTriangle, 0f, 180f).start();
                mExpandState.put(position, true);
            }

            @Override
            public void onPreClose() {
                createRotateAnimator(holder.mViewTriangle, 180f, 0f).start();
                mExpandState.put(position, false);
            }
        });

        holder.mViewTriangle.setRotation(mExpandState.get(position) ? 180f : 0f);
    }

    @Override
    public int getItemCount() {
        return mPhrases.size();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            mTts.setLanguage(Locale.ENGLISH);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTextViewPhrase;
        private TextView mTextViewDate;
        private View mViewTriangle;
        private ExpandableLinearLayout mExpandableLayout;
        private Button mButtonSend;
        private Button mButtonUnfavourite;
        private RelativeLayout mRelativeLayoutTriangle;
        private RelativeLayout mRelativeLayoutTts;

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

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.adapter_favourites_triangle_relativelayout:
                    mExpandableLayout.toggle();
                    break;
                case R.id.adapter_favourites_tts_relativelayout:
                    mTts.speak(mPhrases.get(getAdapterPosition()).getPhrase(), TextToSpeech.QUEUE_FLUSH, null, null);
                    break;
                case R.id.adapter_favourites_send_button:
                    Utilities.sendPhrase(mActivity, mPhrases.get(getAdapterPosition()));
                    break;
                case R.id.adapter_favourites_unfavourite_button:
                    PhraseManager phraseManager = PhraseManager.getInstance(mActivity);
                    Log.i(Constants.TAG, "Adapter position is " + String.valueOf(getAdapterPosition()));
                    phraseManager.removeFavourite(mPhrases.get(getAdapterPosition()));
                    notifyItemRemoved(getAdapterPosition());
                    mExpandableLayout.toggle();
                    break;
            }
        }
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, mActivity.getString(R.string.rotation), from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }
}
