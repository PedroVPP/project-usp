package br.usp.project_usp.parsers;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.io.SAXReader;

/**
 *
 * @author pedro
 * This class only parses the xmi file using the SAXREADER.
 */
public class XmiParser {

    /**
     * Parses the file into a Document object
     * @param file the location of the XMI file
     * @return the Document object
     */
    public Document parse(File file) {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(file);
            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
