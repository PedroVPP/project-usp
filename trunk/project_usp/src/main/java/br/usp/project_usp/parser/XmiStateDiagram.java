/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.parser;

import br.usp.project_usp.diagram.state_diagram.StateType;
import java.util.ArrayList;
import java.util.List;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author pedro
 */
public class XmiStateDiagram {
    private Document document;
    
    private List<Node> states;
    private List<Node> transitions;
    private List<Node> stereotypes;
    private List<Node> callEvents;

    public XmiStateDiagram(Document document) {
        this.document = document;
        this.states = new ArrayList<Node>();
        this.transitions = new ArrayList<Node>();
        this.stereotypes = new ArrayList<Node>();
        this.callEvents = new ArrayList<Node>();
        
        fillStateNodes();
        fillTransitionNodes();
        fillStereotypeNodes();
        fillCallEventNodes();
    }

    private void fillStateNodes() {
        
//        NodeList listOfSimpleStates = document.getElementsByTagName("UML:SimpleState");
//        NodeList listOfPseudoStates = document.getElementsByTagName("UML:Pseudostate");
//        NodeList listOfCompositeStates = document.getElementsByTagName("UML:CompositeState");
//        NodeList listOfCompositeStates = document.getElementsByTagName("UML:FinalState");
//        
//                
//        for (int i = 0; i < listOfSimpleStates.getLength(); i++) {
//            this.getStates().add(listOfSimpleStates.item(i));
//        }
//        for (int i = 0; i < listOfPseudoStates.getLength(); i++) {
//            this.getStates().add(listOfPseudoStates.item(i));
//        }
//        for (int i = 0; i < listOfCompositeStates.getLength(); i++) {
//            this.getStates().add(listOfCompositeStates.item(i));
//        }
        
        for(int i = 0; i < StateType.getStateTypes().length; i++) {
            NodeList listOfStates = document.getElementsByTagName(StateType.getStateTypes()[i]);
            for (int j = 0; j < listOfStates.getLength(); j++) {
                this.getStates().add(listOfStates.item(j));
            }
        }
    }

    private void fillTransitionNodes() {
        NodeList listOfTransitions = document.getElementsByTagName("UML:Transition");
        for (int i = 0; i < listOfTransitions.getLength(); i++) {
            getTransitions().add(listOfTransitions.item(i));
        }
    }

    private void fillStereotypeNodes() {
        NodeList listOfStereotypes = document.getElementsByTagName("UML:Stereotype");
        for (int i = 0; i < listOfStereotypes.getLength(); i++) {
            getStereotypes().add(listOfStereotypes.item(i));
        }
    }

    private void fillCallEventNodes() {
        NodeList listOfCallEvents = document.getElementsByTagName("UML:CallEvent");
        for (int i = 0; i < listOfCallEvents.getLength(); i++) {
            getCallEvents().add(listOfCallEvents.item(i));
        }
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
     * @return the states
     */
    public List<Node> getStates() {
        return states;
    }

    /**
     * @param states the states to set
     */
    public void setStates(List<Node> states) {
        this.states = states;
    }

    /**
     * @return the transitions
     */
    public List<Node> getTransitions() {
        return transitions;
    }

    /**
     * @param transitions the transitions to set
     */
    public void setTransitions(List<Node> transitions) {
        this.transitions = transitions;
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

    /**
     * @return the callevents
     */
    public List<Node> getCallEvents() {
        return callEvents;
    }

    /**
     * @param callevents the callevents to set
     */
    public void setCallevents(List<Node> callevents) {
        this.callEvents = callevents;
    }
}
