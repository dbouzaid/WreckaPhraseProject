package au.edu.uts.mad.id11258732.wreckaphraseproject.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import au.edu.uts.mad.id11258732.wreckaphraseproject.model.Phrase;

/**
 * MyDatabaseHelper
 * A helper class to manage user's phrases database creation and version management.
 * This class takes care of opening the database if it exists, creating it if it does not,
 * and upgrading it as necessary.
 */
public class PhraseDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Phrases";
    private static final int DATABASE_VERSION = 1;

    // Used to create the favourites table in the database
    private static final String TABLE_FAVOURITES_CREATE =
            "CREATE TABLE " + Constants.TABLE_FAVOURITES + " (" +
                    Constants.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    Constants.COLUMN_PHRASE + " TEXT, " +
                    Constants.COLUMN_CREATION_DATE + " INTEGER " +
                    ")";

    public PhraseDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creates a favourites tables for the database when the database
     * is created
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_FAVOURITES_CREATE);
    }

    /**
     * Deletes the current table and called onCreate to
     * recreate the database with the new table information
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_FAVOURITES);

        onCreate(db);
    }

    /**
     * Returns a full List of favourite phrases
     *
     * @return
     */
    public ArrayList<Phrase> getFavourites() {
        ArrayList<Phrase> phrases = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_FAVOURITES, Constants.GET_FAVOURITE_COLUMNS, null, null, null, null, Constants.COLUMN_CREATION_DATE + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Phrase phrase = new Phrase();
                phrase.setId(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_ID)));
                phrase.setPhrase(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_PHRASE)));
                phrase.setCreationDate(cursor.getLong(cursor.getColumnIndex(Constants.COLUMN_CREATION_DATE)));
                phrases.add(phrase);
            } while (cursor.moveToNext());
        }
        db.close();
        return phrases;
    }


    /**
     * Creates a new row in the database to contain the information of the new
     * phrase and to insert the data to their respective columns
     *
     * @param phrase to be added
     */
    public boolean addFavourite(Phrase phrase) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Constants.COLUMN_PHRASE, phrase.getPhrase());
        values.put(Constants.COLUMN_CREATION_DATE, phrase.getCreationDate());
        long insertId = db.insert(Constants.TABLE_FAVOURITES, null, values);
        phrase.setId(insertId);
        db.close();
        return true;
    }

    /**
     * Removes a phrase from the database by it's ID reference
     *
     * @param phraseToRemove
     */
    public void removeFavourite(Phrase phraseToRemove) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_FAVOURITES, Constants.COLUMN_ID + "=" + phraseToRemove.getId(), null);
        db.close();
    }
}
