package au.edu.uts.mad.id11258732.wreckaphraseproject.helper;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;

/**
 * Created by Boozy on 31/05/2016.
 */
public class PhraseManager {

    private static PhraseManager sInstance;

    private ArrayList<Phrase> mFavourites;

    private PhraseDatabaseHelper mDbHelper;

    public static PhraseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PhraseManager(context);
        }
        return sInstance;
    }

    private PhraseManager(Context context) {
        Log.i(Constants.TAG, "Getting helper");
        mDbHelper = new PhraseDatabaseHelper(context);
        Log.i(Constants.TAG, "Getting favourites from helper");
        mFavourites = mDbHelper.getFavourites();
    }

    public void addFavourite(Phrase phrase) {
        mFavourites.add(0, phrase);
        mDbHelper.addFavourite(phrase);
        Log.i(Constants.TAG, "Added to favourites");
    }

    public void removeFavourite(Phrase phraseToRemove) {
        for (Phrase phrase : mFavourites) {
            if (phrase.getId() == phraseToRemove.getId()){
                mFavourites.remove(phrase);
                break;
            }
        }
        mDbHelper.removeFavourite(phraseToRemove);
    }

    public ArrayList<Phrase> getFavourites() {
        return mFavourites;
    }
}
