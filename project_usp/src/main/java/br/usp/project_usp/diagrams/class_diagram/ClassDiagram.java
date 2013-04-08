package br.usp.project_usp.diagrams.class_diagram;

import br.usp.project_usp.diagrams.Diagram;
import br.usp.project_usp.diagrams.DiagramType;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 *
 * @author pedro
 * This class represents the class diagram. At first I thought that it was necessary 
 * to the algorithm, but now I'm not sure it these informations about the class diagrams are
 * really necessary. Maybe in the end, all the classes in the class_diagram package can be
 * deleted.
 * 
 * TODO: Implement the concept of inheritance
 * TODO: Implement the name and cardinality of associations
 * TODO: Implement the type of the atributes in classes
 * TODO: Implement the class stereotypes
 */
public class ClassDiagram extends Diagram {

    private List<UmlClass> classes;
    private List<Association> associations;

    public ClassDiagram(File file, Document document) throws Exception {
        super(file, document, DiagramType.ClassDiagram);
        classes = new ArrayList<UmlClass>();
        associations = new ArrayList<Association>();

        constructClasses(super.getDocument());
        constructAssociations(super.getDocument());
    }

    private void constructClasses(Document document) {
        String xPathClassesLocator = "//UML:Class[@xmi.id and @name]"; // every UML:Class node that has the xmi.id and name attribute
        
        for (Iterator<Element> it = document.selectNodes(xPathClassesLocator).iterator(); it.hasNext(); ) {
            Element umlclassElement = it.next();
            
            UmlClass newClass = new UmlClass();
            newClass.setId(umlclassElement.attributeValue("xmi.id"));
            newClass.setName(umlclassElement.attributeValue("name"));
            
            String xPathAttributeLocator = umlclassElement.getUniquePath()+"/descendant::UML:Attribute";
            for (Iterator<Element> it2 = document.selectNodes(xPathAttributeLocator).iterator(); it2.hasNext(); ) {
                Element attributeElement=  it2.next();
                
                Attribute newAttribute = new Attribute();
                newAttribute.setId(attributeElement.attributeValue("xmi.id"));
                newAttribute.setName(attributeElement.attributeValue("name"));
                newAttribute.setType(null); // TODO: falta implementar o mapeamento dos tipos dos atributos, não deveria ser nullo aqui.
                
                newClass.addAttribute(newAttribute);
            }
            this.classes.add(newClass);
        }
    }
    

    private void constructAssociations(Document document) throws Exception {
        
        // TODO: Falta implementar o mapeamento do nome e cardinalidade das associações
        String xPathAssociationsLocator = "//UML:Association"; // every UML:Association node
        
        for (Iterator<Element> it = document.selectNodes(xPathAssociationsLocator).iterator(); it.hasNext(); ) {
            Element associationElement = it.next();
            
            Association newAssociation = new Association();
            newAssociation.setId(associationElement.attributeValue("xmi.id"));
            newAssociation.setName(associationElement.attributeValue("name"));
            
            newAssociation.addParticipant(findClassById(((Element) associationElement.selectNodes(associationElement.getUniquePath()+"/descendant::UML:Class").get(0)).attributeValue("xmi.idref")));
            newAssociation.addParticipant(findClassById(((Element) associationElement.selectNodes(associationElement.getUniquePath()+"/descendant::UML:Class").get(1)).attributeValue("xmi.idref")));
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

//    private void constructParticipantsAndMultiplicity(NodeList associationsEnd) {
//        throw new UnsupportedOperationException("Not yet implemented");
//    }
}
