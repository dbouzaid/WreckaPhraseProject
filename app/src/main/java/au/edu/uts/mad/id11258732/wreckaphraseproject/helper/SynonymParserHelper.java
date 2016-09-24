package au.edu.uts.mad.id11258732.wreckaphraseproject.helper;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * SynonymParserHelper Class
 * A Helper class to Parse the specific schema of the Synonym API XML
 */
public class SynonymParserHelper {

    public SynonymParserHelper() {

    }

    /**
     * Takes the response of the Synonym API and parses it to get the synonyms from the XML
     * Uses the XmlPullParser and Factory to traverse through the XML file to get the data
     * @param inputStream of the Synonym API response
     * @return All the Synonyms in an ArrayList Format
     */
    public ArrayList<String> parse(InputStream inputStream){
        //
        XmlPullParserFactory factory;
        XmlPullParser parser;
        ArrayList<String> words = new ArrayList<>();
        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            parser = factory.newPullParser();

            // give the parser the insput stream to start traversing
            parser.setInput(inputStream, null);

            // Store the type of event the parser has come across
            int eventType = parser.getEventType();

            // While not at the end of document, traverse the XML and add the synonyms to the list
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.TEXT:
                        words.add(parser.getText());
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

        // Return the words from the XML to the service
        return words;
    }
}
