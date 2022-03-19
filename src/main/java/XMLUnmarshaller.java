import generated.Node;
import generated.Osm;
import generated.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;
import utils.NamespaceFilter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.sax.SAXSource;
import java.io.InputStream;
import java.util.List;

public class XMLUnmarshaller {
    public OsmStorage osmStorage = OsmStorage.getInstance();

    private static final Logger logger = LogManager.getLogger(XMLUnmarshaller.class);

    public void unmarshallOsmFile(InputStream inputStream) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Osm.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            XMLReader reader = XMLReaderFactory.createXMLReader();

            NamespaceFilter inFilter = new NamespaceFilter("http://openstreetmap.org/osm/0.6", true);
            inFilter.setParent(reader);

            InputSource inputSource = new InputSource(inputStream);

            SAXSource source = new SAXSource(inFilter, inputSource);

            Osm osmObject = (Osm) unmarshaller.unmarshal(source);
            writeNodes(osmObject);
        } catch (JAXBException | SAXException exception) {
            logger.info(exception.getMessage());
        }
    }

    private void writeNodes(Osm osmObject){
        List<Node> nodeList = osmObject.getNode();
        for (Node node: nodeList){
            String username = node.getUser();
            osmStorage.saveUserEdits(username);
            List<Tag> tagList = node.getTag();
            for (Tag tag: tagList){
                String key = tag.getK();
                osmStorage.saveNodeKey(key);
            }
        }
    }
}
