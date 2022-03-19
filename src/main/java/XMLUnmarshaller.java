import database.DbConnection;
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
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;

public class XMLUnmarshaller {
    public DataLoader dataLoader = DataLoader.getInstance();

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
        } catch (JAXBException | SAXException | IOException | SQLException exception) {
            logger.info(exception.getMessage());
        }
    }

    private void writeNodes(Osm osmObject) throws IOException, SQLException {
        List<Node> nodeList = osmObject.getNode();

        DbConnection.initDatabase();
        long time = System.currentTimeMillis();
        dataLoader.loadNodeStringQuery(nodeList);
        time = System.currentTimeMillis() - time;
        logger.info("Loading nodes by STRING QUERY: " + time + " milliseconds.");

        DbConnection.initDatabase();
        time = System.currentTimeMillis();
        dataLoader.loadNode(nodeList);
        time = System.currentTimeMillis() - time;
        logger.info("Loading nodes by QUERY (with prepared statement): " + time + " milliseconds.");

        DbConnection.initDatabase();
        time = System.currentTimeMillis();
        dataLoader.loadNodeBatchQuery(nodeList);
        time = System.currentTimeMillis() - time;
        logger.info("Loading nodes by BATCH QUERY: " + time + " milliseconds");
    }
}
