package modules;

import org.apache.commons.io.FileUtils;

import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ImageUtils {

    public static List<String> saveImage(String src, String name){
        String fullPath = "./public/images/imgApp/" + name;
        int counter = 0;
        while (Files.exists(Paths.get(fullPath))){
            counter++;
            fullPath = "./public/images/imgApp/" + "("+counter+")"+ name;
        }
        name = "("+counter+")"+ name;

        final String imageString = src.split(",")[1];
        final byte[] bytes = DatatypeConverter.parseBase64Binary(imageString);
        final File file = new File(fullPath);
        try {
            FileUtils.writeByteArrayToFile(file,  bytes);
        } catch (IOException e) {
            return null;
        }

        return Arrays.asList(name, "/images/imgApp/" + name);
    }
}
