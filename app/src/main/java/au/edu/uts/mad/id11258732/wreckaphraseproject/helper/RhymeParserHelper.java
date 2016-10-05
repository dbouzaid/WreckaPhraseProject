package au.edu.uts.mad.id11258732.wreckaphraseproject.helper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * RhymeParserHelper class
 *
 * Used to parse the XML file containing the rhymes
 */
public class RhymeParserHelper {

    public RhymeParserHelper(){

    }

    /**
     * Traverses the given input stream and retrieves the data about rhymes
     * @param inputStream
     * @return the List of rhymes
     */
    public ArrayList<String> parse(InputStream inputStream) {

        XmlPullParserFactory factory;
        XmlPullParser parser;
        ArrayList<String> words = new ArrayList<>();
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            // Give the parse the inputstream to traverse
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            String tagName;
            String currentTag = null;

            //Start traversing the XML file
            while (eventType != XmlPullParser.END_DOCUMENT) {
                tagName = parser.getName();
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        // Save the tagname to use later
                        currentTag = tagName;
                        break;
                    case XmlPullParser.TEXT:
                        // If the tag is related to rhymes, retrieve the data and split
                        if (currentTag.equals(Constants.RHYMES_TAG)){
                            words = splitRhymeWords(parser.getText());
                        }
                        break;
                    default:
                        break;
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Return the list of rhymes
        return words;
    }

    /**
     * Due to the nature of the XML file for rhymes, they must be split into an array and trimmed
     * @param rhymeWords
     * @return
     */
    private ArrayList<String> splitRhymeWords(String rhymeWords){
        String[] wordsArray = rhymeWords.split(",");
        ArrayList<String> wordsList = new ArrayList<>();
        for (String s: wordsArray) {
            wordsList.add(s.trim());
        }
        return wordsList;
    }

}
