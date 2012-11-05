package br.usp.project_usp.diagram.state_diagram;

import br.usp.project_usp.diagram.Diagram;
import br.usp.project_usp.diagram.DiagramType;
import br.usp.project_usp.parser.XmiStateDiagram;
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
public class StateDiagram extends Diagram{
    private List<State> states;
    private List<Transition> transitions;

    public StateDiagram(File file, Document document) {
        
        super(file, document, DiagramType.StateDiagram);
        
        states = new ArrayList<State>();
        transitions = new ArrayList<Transition>(); 
        XmiStateDiagram xmiStateDiagram = new XmiStateDiagram(document);
        
        constructTransitions(xmiStateDiagram.getTransitions());
//        constructStates(xmiStateDiagram.getStates());
        
    }
    
    private void constructStates(List<Node> states) {
        for (Iterator<Node> it = states.iterator(); it.hasNext();) {
            Node node = it.next();
            
            if(node.getAttributes().getNamedItem("xmi.id") == null || node.getAttributes().getNamedItem("name") == null) {
                continue;
            }
            
            String id = node.getAttributes().getNamedItem("xmi.id").getNodeValue();
            String name = node.getAttributes().getNamedItem("name").getNodeValue();
            
            State newState = new State(id, name);
            
            newState.setInvariant(null);
            
            
            
            
            
            
            
            newState.addState(newState);
        }
    }

    private void constructTransitions(List<Node> transitions) {
        for (Iterator<Node> it = transitions.iterator(); it.hasNext();) {
            Node node = it.next();
            if(node.getAttributes().getNamedItem("xmi.id") == null) {
                continue;
            }
            
            Transition transition = new Transition();
            transition.setId(node.getAttributes().getNamedItem("xmi.id").getNodeValue());
            
            NodeList sourceStates = ((Element) node).getElementsByTagName("UML:Transition.source");
            for(int i = 0; i < StateType.getStateTypes().length; i ++) {
                if ((((Element) sourceStates.item(0)).getElementsByTagName(StateType.getStateTypes()[i])).getLength() > 0) {
                    NodeList state = ((Element) sourceStates.item(0)).getElementsByTagName(StateType.getStateTypes()[i]);
                    transition.setSource(state.item(0).getAttributes().getNamedItem("xmi.idref").getNodeValue());
                }
            }
            
            NodeList targetStates = ((Element) node).getElementsByTagName("UML:Transition.target");
            for(int i = 0; i < StateType.getStateTypes().length; i ++) {
                if ((((Element) targetStates.item(0)).getElementsByTagName(StateType.getStateTypes()[i])).getLength() > 0) {
                    NodeList state = ((Element) targetStates.item(0)).getElementsByTagName(StateType.getStateTypes()[i]);
                    transition.setTarget(state.item(0).getAttributes().getNamedItem("xmi.idref").getNodeValue());
                }
            } 
            
            this.transitions.add(transition);
        }
    }
    
    public void addState(State state) {
        getStates().add(state);
    }
    
    public void addTransition(Transition transition) {
        getTransitions().add(transition);
    }

    /**
     * @return the states
     */
    public List<State> getStates() {
        return states;
    }

    /**
     * @param states the states to set
     */
    public void setStates(List<State> states) {
        this.states = states;
    }

    /**
     * @return the transitions
     */
    public List<Transition> getTransitions() {
        return transitions;
    }

    /**
     * @param transitions the transitions to set
     */
    public void setTransitions(List<Transition> transitions) {
        this.transitions = transitions;
    }
}
