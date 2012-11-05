package br.usp.project_usp.parser;

import br.usp.project_usp.diagram.Diagram;
import br.usp.project_usp.diagram.class_diagram.ClassDiagram;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

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
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();

            return document;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
