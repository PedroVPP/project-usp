/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagram.class_diagram;

import br.usp.project_usp.diagram.Diagram;
import br.usp.project_usp.diagram.DiagramType;
import br.usp.project_usp.parser.XmiClassDiagram;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author pedro
 */
public class ClassDiagram extends Diagram {

    private List<UmlClass> classes;
    private List<Association> associations;

    public ClassDiagram(File file, Document document) {
        super(file, document, DiagramType.ClassDiagram);
        classes = new ArrayList<UmlClass>();
        associations = new ArrayList<Association>();
        XmiClassDiagram xmiClassDiagram = new XmiClassDiagram(document);

        constructClasses(xmiClassDiagram.getClasses());
        constructAssociations(xmiClassDiagram.getAssociations());
    }

    private void constructClasses(List<Node> classes) {
        for (Iterator<Node> it = classes.iterator(); it.hasNext();) {
            Node node = it.next();
            UmlClass newClass = new UmlClass();

            Node id = node.getAttributes().getNamedItem("xmi.id");
            Node name = node.getAttributes().getNamedItem("name");

            if (id != null && name != null && !name.equals("")) {

                newClass.setId(id.getNodeValue());
                newClass.setName(name.getNodeValue());

                NodeList attributesNodes = ((Element) node).getElementsByTagName("UML:Attribute");
//                System.out.println("A classe " + node.getAttributes().getNamedItem("name") + "possui " + attributesNodes.getLength() + " atributos");
                if (attributesNodes.getLength() > 0) {

                    for (int i = 0; i < attributesNodes.getLength(); i++) {
                        
                        Node attributeNode = attributesNodes.item(i);
                        
                        Attribute newAttribute = new Attribute();

                        newAttribute.setId(attributeNode.getAttributes().getNamedItem("xmi.id").getNodeValue());
                        newAttribute.setName(attributeNode.getAttributes().getNamedItem("name").getNodeValue());
                        newAttribute.setType(null); // TODO: falta implementar o mapeamento dos tipos dos atributos, não deveria ser nullo aqui.

                        newClass.addAttribute(newAttribute);
                    }

                }

                this.classes.add(newClass);
            }
        }
    }

    private void constructAssociations(List<Node> associations) {
        
        // TODO: Falta implementar o mapeamento do nome e cardinalidade das associações
        
        for (Iterator<Node> it = associations.iterator(); it.hasNext();) {
            Node node = it.next();

            String id = node.getAttributes().getNamedItem("xmi.id").getNodeValue();
            String name = node.getAttributes().getNamedItem("name").getNodeValue();
            UmlClass participant1 = null, participant2 = null;

            NodeList participantClasses = ((Element) node).getElementsByTagName("UML:Class");

            participant1 = findClassById(participantClasses.item(0).getAttributes().getNamedItem("xmi.idref").getNodeValue());
            participant2 = findClassById(participantClasses.item(1).getAttributes().getNamedItem("xmi.idref").getNodeValue());

            UmlClass[] participants = {participant1, participant2};

            Association newAssociation = new Association();
            newAssociation.setId(id);
            newAssociation.setName(name);
            newAssociation.setPasticipants(participants);

            this.associations.add(newAssociation);
        }
    }

    public UmlClass findClassById(String id) {

        UmlClass result = null;

        for (Iterator<UmlClass> it = classes.iterator(); it.hasNext();) {
            UmlClass umlClass = it.next();

            if (umlClass.getId().equals(id)) {
                result = umlClass;
            }
        }

        return result;
    }

    public UmlClass findClassByName(String name) {
        UmlClass result = null;

        for (Iterator<UmlClass> it = classes.iterator(); it.hasNext();) {
            UmlClass umlClass = it.next();

            if (umlClass.getName().equals(name)) {
                result = umlClass;
            }
        }

        return result;
    }

    public Association findAssociationById(String id) {

        Association result = null;

        for (Iterator<Association> it = associations.iterator(); it.hasNext();) {
            Association association = it.next();

            if (association.getId().equals(id)) {
                result = association;
            }
        }

        return result;
    }

    // setters and getters
    /**
     * @return the classes
     */
    public List<UmlClass> getClasses() {
        return classes;
    }

    /**
     * @param classes the classes to set
     */
    public void setClasses(List<UmlClass> classes) {
        this.classes = classes;
    }

    /**
     * @return the associations
     */
    public List<Association> getAssociations() {
        return associations;
    }

    /**
     * @param associations the associations to set
     */
    public void setAssociations(List<Association> associations) {
        this.associations = associations;
    }

    private void constructParticipantsAndMultiplicity(NodeList associationsEnd) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
