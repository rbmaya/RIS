import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Stream;

public class OsmStorage {
    private static final Logger logger = LogManager.getLogger(OsmStorage.class);

    private final HashMap<String, Integer> userEdits = new HashMap<>();
    private final HashMap<String, Integer> nodeKeys = new HashMap<>();

    private static final OsmStorage instance = new OsmStorage();

    private OsmStorage(){}

    public static OsmStorage getInstance(){
        return instance;
    }

    public void saveUserEdits(String username) {
        userEdits.put(username, userEdits.getOrDefault(username, 0) + 1);
    }

    public void saveNodeKey(String key) {
        nodeKeys.put(key, nodeKeys.getOrDefault(key, 0) + 1);
    }

    public HashMap<String, Integer> getNodeKeys() {
        return nodeKeys;
    }

    public HashMap<String, Integer> getUserEdits() {
        return userEdits;
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
