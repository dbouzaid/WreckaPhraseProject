package au.edu.uts.mad.id11258732.wreckaphraseproject.helper;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;

import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;

/**
 * PhraseManager Class
 * ​A singleton class class that holds the list of
 * phrases and interacts with the MyDatabaseHelper class to load
 * and store data.
 */
public class PhraseManager {

    // Instance of PhraseManager
    private static PhraseManager sInstance;

    // List of favourite phrases
    private ArrayList<Phrase> mFavourites;

    // Reference to database
    private PhraseDatabaseHelper mDbHelper;

    public static PhraseManager getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PhraseManager(context);
        }
        return sInstance;
    }

    private PhraseManager(Context context) {
        mDbHelper = new PhraseDatabaseHelper(context);
        mFavourites = mDbHelper.getFavourites();
    }

    /**
     * Add Phrase to the beginning of the list and to the database
     *
     * @param phrase
     */
    public void addFavourite(Phrase phrase) {
        mFavourites.add(0, phrase);
        mDbHelper.addFavourite(phrase);
    }

    /**
     * Delete the phrase from the list of phrases and database
     *
     * @param phraseToRemove
     */
    public void removeFavourite(Phrase phraseToRemove) {
        for (Phrase phrase : mFavourites) {
            if (phrase.getId() == phraseToRemove.getId()) {
                mFavourites.remove(phrase);
                break;
            }
        }
        mDbHelper.removeFavourite(phraseToRemove);
    }

    /**
     * @return the list of favourite phrases
     */
    public ArrayList<Phrase> getFavourites() {
        return mFavourites;
    }
}
