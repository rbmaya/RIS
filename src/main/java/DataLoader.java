import generated.Node;
import model.impl.NodeDaoImpl;
import model.impl.TagDaoImpl;

import java.util.List;

public class DataLoader {
    private static final DataLoader instance = new DataLoader();
    private static final NodeDaoImpl nodeDao = new NodeDaoImpl();
    private static final TagDaoImpl tagDao = new TagDaoImpl();

    private DataLoader(){}

    public static DataLoader getInstance(){
        return instance;
    }

    public void loadNode(List<Node> nodeList){
        nodeList.forEach(node -> {
            nodeDao.insertNode(node);
            node.getTag().forEach(tagDao::insertTag);
        });
    }

    public void loadNodeStringQuery(List<Node> nodeList){
        nodeList.forEach(node -> {
            nodeDao.insertNodeStringQuery(node);
            node.getTag().forEach(tagDao::insertTagStringQuery);
        });
    }

    public void loadNodeBatchQuery(List<Node> nodeList){
        nodeDao.insertNodeBatchQuery(nodeList);
        nodeList.forEach(node -> {
            tagDao.insertTagBatchQuery(node.getTag());
        });
    }
}
