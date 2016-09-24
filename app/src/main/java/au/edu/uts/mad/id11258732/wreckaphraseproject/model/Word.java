package au.edu.uts.mad.id11258732.wreckaphraseproject.model;

import au.edu.uts.mad.id11258732.wreckaphraseproject.helper.Constants;

/**
 * Word Class
 * The Word Class is a Model which holds all the data related to one word in a Phrase Class
 *
 */
public class Word {

    // This will hold the current word the user wants to change to
    private String mWord;

    // Holds the punctuation mark at the end of the word if present
    private String mPunctuation;

    /**
     * Constructs a word with the passed String
     *
     * @param word String to be converted into a Word Class
     */
    public Word(String word) {
        // Send the passed word to be checked for punctuation marks
        checkPunctuation(word);
    }

    /**
     * Checks the passed String for any punctuation marks at the end of the String
     * If the word contains a punctuation mark at the end, set the Class fields based on the
     * result
     *
     * @param word  The String to be checked
     */
    private void checkPunctuation(String word) {
        // Get the substring of the end of the passed String to check for punctuation marks
        String punctuationCheck = word.substring(word.length() - 1);

        if (punctuationCheck.matches(Constants.PUNCTUATION_REGEX)){
            //Punctuation has been found, set fields with the word and punctuation mark
            mWord = word.substring(0, word.length() - 1);
            mPunctuation = punctuationCheck;
        } else {
            // No punctuation mark found, set mPunctuation to ""
            mWord = word;
            mPunctuation = "";
        }
    }

    public Word() {

    }

    public String getWord() {
        return mWord;
    }

    // Concatenates the fields together to construct the full word
    public String getFullWord() {
        return mWord + mPunctuation;
    }

    public String getPunctuation() {
        return mPunctuation;
    }
}
