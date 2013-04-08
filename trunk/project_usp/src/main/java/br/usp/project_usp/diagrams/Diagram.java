package br.usp.project_usp.diagrams;

import java.io.File;
import org.dom4j.Document;
/**
 *
 * @author pedro
 * this class was created to hold important information that I thought it would be important
 * to store. The relevance of these informations is subject to questioning.
 * In future versions maybe it could be deleted.
 */
public class Diagram {
    // the id of the diagram obtained from the xmi file
    private String id;
    // the name of the diagram obtained from the xmi file
    private String name;
    // the type of diagram (state or class diagram)
    private DiagramType type;
    // the xmi file parser by SAXREADER
    private Document document;
    private File file;
    
    public Diagram(File file, Document document, DiagramType type) {
        this.file = file;
        this.document = document;
        this.type = type;
        
        this.id = document.selectSingleNode("/XMI/descendant::UML:Model/@xmi.id").getText();
        this.name = document.selectSingleNode("/XMI/descendant::UML:Model/@name").getText();
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
