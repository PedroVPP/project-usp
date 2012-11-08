package br.usp.project_usp.parser;

import java.io.File;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

/**
 *
 * @author pedro
 */
public class Parser {

    /**
     * Parses the file into a Document object
     * @param location the location of the XMI file
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
