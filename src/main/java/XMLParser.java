import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.EndElement;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class XMLParser {

    private static final Logger logger = LogManager.getLogger(XMLParser.class);

    private static final String TAG_NAME_NODE = "node";
    private static final String TAG_NAME_TAG = "tag";
    private static final QName USER_ATTR_NAME = new QName("user");
    private static final QName KEY_ATTR_NAME = new QName("k");

    private final HashMap<String, Integer> userEdits = new HashMap<>();
    private final HashMap<String, Integer> nodeKeys = new HashMap<>();


    public void parseXmlFile(InputStream inputStream){
        XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
        try {
            XMLEventReader xmlEventReader = xmlInputFactory.createXMLEventReader(inputStream);
            while (xmlEventReader.hasNext()){
                XMLEvent event = xmlEventReader.nextEvent();
                if (event.isStartElement()){
                    StartElement startElement = event.asStartElement();
                    if (startElement.getName().getLocalPart().equals(TAG_NAME_NODE)){
                        String username = startElement.getAttributeByName(USER_ATTR_NAME).getValue();
                        saveUserEdits(username);
                        countNumberKeys(xmlEventReader);
                    }
                }
            }
        } catch (XMLStreamException exc){
            logger.error("Error parsing!");
            logger.error(exc.getMessage());
        }
    }

    private void countNumberKeys(XMLEventReader xmlEventReader) throws XMLStreamException {
        while (xmlEventReader.hasNext()){
            XMLEvent event = xmlEventReader.nextEvent();
            if (event.isEndElement()){
                EndElement endElement = event.asEndElement();
                if (endElement.getName().getLocalPart().equals(TAG_NAME_NODE)){
                    return;
                }
            }
            if (event.isStartElement()){
                StartElement startElement = event.asStartElement();
                if (startElement.getName().getLocalPart().equals(TAG_NAME_TAG)){
                    String key = startElement.getAttributeByName(KEY_ATTR_NAME).getValue();
                    saveNodeKey(key);
                }
            }
        }
    }

    private void saveUserEdits(String username){
        userEdits.put(username, userEdits.getOrDefault(username, 0) + 1);
    }

    private void saveNodeKey(String key){
        nodeKeys.put(key, nodeKeys.getOrDefault(key, 0) + 1);
    }

    public void saveUserEditsToLogs(){
        Stream<Map.Entry<String, Integer>> stream = userEdits.entrySet().stream().sorted(
                (o1, o2) -> o2.getValue().compareTo(o1.getValue())
        );

        Iterator<Map.Entry<String, Integer>> iterator = stream.iterator();
        logger.info("USER EDITS:");
        while (iterator.hasNext()){
            Map.Entry<String, Integer> element = iterator.next();
            logger.info(element);
        }
    }

    public void saveNodeKeysToLogs(){
        logger.info("NODE KEYS:");
        for (Map.Entry<String, Integer> element: nodeKeys.entrySet()){
            logger.info(element);
        }
    }
}
