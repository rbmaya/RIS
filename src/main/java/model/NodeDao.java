package model;

import generated.Node;

import java.util.List;

public interface NodeDao {
    boolean insertNode(Node node);
    boolean insertNodeStringQuery(Node node);
    boolean insertNodeBatchQuery(List<Node> nodeList);
}
