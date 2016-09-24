package au.edu.uts.mad.id11258732.wreckaphraseproject.model;

import android.util.Log;

import java.util.ArrayList;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;

/**
 * Phrase Class
 * A Model class which holds all the data related to a phrase or sentence. *
 */
public class Phrase {

    // Holds the full phrase a String
    private String mPhrase;
    // Holds each word of a Phrase in an ArrayList of Word Classes
    private ArrayList<Word> mWordsInPhrase;
    // When the phrases was finalised
    private long mCreationDate;
    // ID reference for storing in the SQLiteDatabase
    private long mId;



    public Phrase() {

    }

    /**
     * Constructs a phrase with the submitted String and calls a split of the string
     * to construct Word Classes
     * @param phrase to be stored and split
     */
    public Phrase(String phrase) {
        mPhrase = phrase;
        mWordsInPhrase = new ArrayList<>();
        splitPhrase(phrase);
    }

    /**
     * Constructs a phrase with the submitted String and calls a split of the string with
     * the date created
     * Used for database retrieval and storage.
     * @param phrase to be stored and split
     * @param date of phrase creation
     */
    public Phrase(String phrase, long date) {
        mPhrase = phrase;
        mWordsInPhrase = new ArrayList<>();
        splitPhrase(phrase);
        mCreationDate = date;
    }

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getCreationDate() {
        return mCreationDate;
    }

    public void setCreationDate(long creationDate) {
        mCreationDate = creationDate;
    }

    public String getPhrase() {
        return mPhrase;
    }

    public void setPhrase(String phrase) {
        mPhrase = phrase;
    }

    public ArrayList<Word> getWordsInPhrase() {
        return mWordsInPhrase;
    }

    /**
     * Creates a String Array to split the passed phrase
     * Which each split based on spaces, it constructs a new Word Class
     * and then stored into the ArrayList of Word Classes.
     * @param phrase to be split and stored in ArrayList
     */
    public void splitPhrase(String phrase){
        String[] words = phrase.split("\\s+");
        for (String s: words) {
            Word word = new Word(s);
            mWordsInPhrase.add(word);
        }
    }

    /**
     * Iterates through the ArrayList of Word Classes to construct a String
     * to represent the phrase.
     * Called when a word is changed in the Phrase
     * @return the new Phrase that has been built
     */
    public String buildPhrase(){
        String mPhrase = "";

        for (Word word: mWordsInPhrase){
            mPhrase += word.getFullWord() + " ";
        }

        return mPhrase;
    }

    /**
     * Changes the word that the user wanted to change
     * @param position of the word to be changed
     * @param word new word to change word in the given position
     */
    public void changeWord(int position, Word word){
        mWordsInPhrase.set(position, word);
    }
}
