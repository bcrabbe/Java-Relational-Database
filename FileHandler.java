
import java.util.*;
import java.nio.file
import java.nio.charset.StandardCharsets


class FileHandler {

    
    FileHandler() {
        
    }
    
    void saveToFile(Table t)
    {
        String s = ...;
        try (BufferedWriter writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            writer.write(s, 0, s.length());
        } catch (IOException x) {
            System.err.format("IOException: %s%n", x);
        }
    }
}