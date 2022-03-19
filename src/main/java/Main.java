import org.apache.commons.compress.compressors.bzip2.BZip2CompressorInputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) {
        try {
            XMLUnmarshaller unmarshaller = new XMLUnmarshaller();
            InputStream inputStream = archiveUncompress();
            unmarshaller.unmarshallOsmFile(inputStream);

            OsmStorage osmStorage = OsmStorage.getInstance();
            osmStorage.saveUserEditsToLogs();
            osmStorage.saveNodeKeysToLogs();
        } catch (IOException exc){
            exc.printStackTrace();
        }
    }

    private static BZip2CompressorInputStream archiveUncompress() throws IOException{
        InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/RU-NVS.osm.bz2"));
        BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
        return new BZip2CompressorInputStream(bufferedInputStream);
    }
}