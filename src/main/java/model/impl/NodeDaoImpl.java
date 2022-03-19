package model.impl;

import database.DbConnection;
import generated.Node;
import model.NodeDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class NodeDaoImpl implements NodeDao {
    private static final Logger logger = LogManager.getLogger(NodeDaoImpl.class);

    @Override
    public boolean insertNode(Node node) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "INSERT INTO node (id, username, lat, lon) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setInt(1, node.getId().intValue());
            preparedStatement.setString(2, node.getUser());
            preparedStatement.setDouble(3, node.getLat());
            preparedStatement.setDouble(4, node.getLon());

            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        return false;
    }

    @Override
    public boolean insertNodeStringQuery(Node node) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "INSERT INTO node (id, username, lat, lon) VALUES (" +
                    node.getId().intValue() + ", '" +
                    node.getUser() + "'," +
                    node.getLat() + ", " +
                    node.getLon()
                    + " ON CONFLICT DO NOTHING";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        return false;
    }

    @Override
    public boolean insertNodeBatchQuery(List<Node> nodeList) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "INSERT INTO node (id, username, lat, lon) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
            AtomicInteger count = new AtomicInteger();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            nodeList.forEach(node -> {
                try {
                    preparedStatement.setInt(1, node.getId().intValue());
                    preparedStatement.setString(2, node.getUser());
                    preparedStatement.setDouble(3, node.getLat());
                    preparedStatement.setDouble(4, node.getLon());
                    count.getAndIncrement();
                    if (count.intValue() == 10) {
                        preparedStatement.executeBatch();
                        count.set(0);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
            preparedStatement.executeBatch();
            return true;
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        return false;
    }
}
