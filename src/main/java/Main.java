import database.DbConnection;
import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {
        try {
            DbConnection.connect();

            XMLUnmarshaller unmarshaller = new XMLUnmarshaller();
            InputStream inputStream = archiveUncompress();
            unmarshaller.unmarshallOsmFile(inputStream);

            OsmStorage osmStorage = OsmStorage.getInstance();
            osmStorage.saveUserEditsToLogs();
            osmStorage.saveNodeKeysToLogs();

            DbConnection.close();
        } catch (IOException | SQLException exc){
            exc.printStackTrace();
        }
    }

    private static BZip2CompressorInputStream archiveUncompress() throws IOException{
        InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/RU-NVS.osm.bz2"));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        return new BZip2CompressorInputStream(bufferedInputStream);
    }
}