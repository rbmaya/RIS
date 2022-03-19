package model.impl;

import database.DbConnection;
import generated.Tag;
import model.TagDao;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TagDaoImpl implements TagDao {
    private static final Logger logger = LogManager.getLogger(TagDaoImpl.class);

    @Override
    public boolean insertTag(Tag tag) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "INSERT INTO tag (key, value) VALUES (?, ?) ON CONFLICT DO NOTHING";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, tag.getK());
            preparedStatement.setString(2, tag.getV());
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        return false;
    }

    @Override
    public boolean insertTagStringQuery(Tag tag) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "INSERT INTO tag (key, value) VALUES ('" +
                    tag.getK() + "','" +
                    tag.getV() + "') ON CONFLICT DO NOTHING";
            Statement statement = connection.createStatement();
            statement.executeUpdate(sql);
            return true;
        } catch (SQLException exc) {
            logger.error(exc.getMessage());
        }
        return false;
    }

    @Override
    public boolean insertTagBatchQuery(List<Tag> tagList) {
        try (Connection connection = DbConnection.getConnection()) {
            String sql = "INSERT INTO tag (key, value) VALUES (?, ?) ON CONFLICT DO NOTHING";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            AtomicInteger count = new AtomicInteger();
            tagList.forEach(tag -> {
                try {
                    preparedStatement.setString(1, tag.getK());
                    preparedStatement.setString(2, tag.getV());
                    preparedStatement.addBatch();
                    count.getAndIncrement();
                    if (count.intValue() == 10) {
                        preparedStatement.executeBatch();
                        count.set(0);
                    }
                } catch (SQLException exc) {
                    logger.error(exc.getMessage());
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
