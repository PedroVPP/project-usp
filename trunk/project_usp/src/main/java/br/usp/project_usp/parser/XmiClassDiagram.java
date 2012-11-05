/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.parser;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author pedro
 */
public class XmiClassDiagram {
    
    private Document document;
    
    private List<Node> classes;
    private List<Node> associations;
    private List<Node> stereotypes;
    
    public XmiClassDiagram(Document document) {
        
        this.document = document;
        this.classes = new ArrayList<Node>();
        this.associations = new ArrayList<Node>();
        this.stereotypes = new ArrayList<Node>();
        
        fillClassNodes();
        fillAssociationNodes();
        fillStereotypeNodes();
    }

    private void fillClassNodes() {
        NodeList listOfClasses = document.getElementsByTagName("UML:Class");
        for (int i = 0; i < listOfClasses.getLength(); i++) {
            getClasses().add(listOfClasses.item(i));
        }
    }

    private void fillAssociationNodes() {
        NodeList listOfAssociations = document.getElementsByTagName("UML:Association");
        for (int i = 0; i < listOfAssociations.getLength(); i++) {
            getAssociations().add(listOfAssociations.item(i));
        }
    }

    private void fillStereotypeNodes() {
        NodeList listOfStereotypes = document.getElementsByTagName("UML:Stereotype");
        for (int i = 0; i < listOfStereotypes.getLength(); i++) {
            getStereotypes().add(listOfStereotypes.item(i));
        }
    }
    
    public Node findClassById(String id) {
        
        Node result = null;
        
        for (Iterator<Node> it = getClasses().iterator(); it.hasNext();) {
            Node node = it.next();
            
            if (node.getAttributes().getNamedItem("xmi.id").getNodeValue().equals(id)) {
                result = node;
            }
        }
        return result;
    }
    
    public Node findClassByName(String name) {
        
        Node result = null;
        
        for (Iterator<Node> it = getClasses().iterator(); it.hasNext();) {
            Node node = it.next();
            
            if (node.getAttributes().getNamedItem("name").getNodeValue().equals(name)) {
                result = node;
            }
        }
        return result;
    }
    
    public Node findAssociationById(String id) {
        Node result = null;
        
        for (Iterator<Node> it = getAssociations().iterator(); it.hasNext();) {
            Node node = it.next();
            
            if (node.getAttributes().getNamedItem("xmi.id").getNodeValue().equals(id)) {
                result = node;
            }
        }
        return result;
    }
    
    public Node findStereotypeById(String id) {
        Node result = null;
        
        for (Iterator<Node> it = getStereotypes().iterator(); it.hasNext();) {
            Node node = it.next();
            
            if (node.getAttributes().getNamedItem("xmi.id").getNodeValue().equals(id)) {
                result = node;
            }
        }
        return result;
    }
    
    public Node findStereotypeByName(String name) {
        Node result = null;
        
        for (Iterator<Node> it = getStereotypes().iterator(); it.hasNext();) {
            Node node = it.next();
            
            if (node.getAttributes().getNamedItem("name").getNodeValue().equals(name)) {
                result = node;
            }
        }
        return result;
    }

    /**
     * @return the classes
     */
    public List<Node> getClasses() {
        return classes;
    }

    /**
     * @param classes the classes to set
     */
    public void setClasses(List<Node> classes) {
        this.classes = classes;
    }

    /**
     * @return the associations
     */
    public List<Node> getAssociations() {
        return associations;
    }

    /**
     * @param associations the associations to set
     */
    public void setAssociations(List<Node> associations) {
        this.associations = associations;
    }

    /**
     * @return the stereotypes
     */
    public List<Node> getStereotypes() {
        return stereotypes;
    }

    /**
     * @param stereotypes the stereotypes to set
     */
    public void setStereotypes(List<Node> stereotypes) {
        this.stereotypes = stereotypes;
    }
}
