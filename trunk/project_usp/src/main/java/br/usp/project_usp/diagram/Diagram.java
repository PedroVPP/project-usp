package br.usp.project_usp.diagram;

import java.io.File;
import org.w3c.dom.Document;
/**
 *
 * @author pedro
 */
public class Diagram {
    private String id;
    private String name;
    private DiagramType type;
    private Document document;
    private File file;
    
    public Diagram(File file, Document document, DiagramType type) {
        this.file = file;
        this.document = document;
        this.type = type;
        
        this.id = document.getElementsByTagName("UML:Model").item(0).getAttributes().getNamedItem("xmi.id").getNodeValue();
        this.name = document.getElementsByTagName("UML:Model").item(0).getAttributes().getNamedItem("name").getNodeValue();
    }
    
    
    // setters and getters
    
    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the type
     */
    public DiagramType getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(DiagramType type) {
        this.type = type;
    }

    /**
     * @return the document
     */
    public Document getDocument() {
        return document;
    }

    /**
     * @param document the document to set
     */
    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

}
