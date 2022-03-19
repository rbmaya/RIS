package database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    private static final Logger logger = LogManager.getLogger(DbConnection.class);
    private static final String DB_STRUCTURE_PATH = "db_init";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String DB_USERNAME = "postgres";
    private static final String DB_PASSWORD = "maya";

    private static Connection connection = null;

    public static void connect() throws SQLException {
        logger.info("Connecting to database...");
        connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
        connection.setAutoCommit(false);
    }

    public static void initDatabase() throws IOException, SQLException {
        logger.info("Init database structure...");
        if (connection != null){
            InputStream inputStream = ClassLoader.getSystemClassLoader().getResourceAsStream(DB_STRUCTURE_PATH);
            assert inputStream != null;
            String statementInit = readSchemaInitFile(inputStream);
            connection.createStatement().execute(statementInit);
            inputStream.close();
        }
    }

    private static String readSchemaInitFile(InputStream inputStream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = bufferedReader.readLine()) != null){
            stringBuilder.append(line).append(" ");
        }
        return stringBuilder.toString();
    }

    public static Connection getConnection() {
        return connection;
    }

    public static void close() throws SQLException {
        if (connection != null) {
            logger.info("Disconnect from database: started");
            connection.close();
            logger.info("Disconnect from database: complete");
        }
    }
}