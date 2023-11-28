package compiler.lexical_analysis;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

@Slf4j
public class SourceFile {
    private final String fileName;

    private BufferedReader reader;
    private boolean fileOpened = false;
    @Getter
    private int lineNumber = 0;

    public SourceFile(String fileName) {
        this.fileName = fileName;
    }

    public String nextLine() {
        String line;
        if (!fileOpened) {
            try {
                reader = new BufferedReader(new FileReader(fileName));
            }
            catch (FileNotFoundException e) {
                log.error("File not found {}", fileName);
                return null;
            }
            fileOpened = true;
        }
        try {
            line = reader.readLine();
            lineNumber++;
        }
        catch (IOException e) {
            log.error("IOException reading line from source file {}", fileName);
            return null;
        }
        return line;
    }
}
