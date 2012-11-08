package br.usp.project_usp.diagram.state_diagram;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

/**
 *
 * @author pedro
 */
public class State {
    private String id;
    private String name;
    private String uniqueName;
    private Stereotype invariant;
    private List<State> children;
    private StateType stateType;
    private Element stateElement;
    private boolean visited;
    private boolean initialState;
    private static int counter = 0;

    public State() {
        this.children = new ArrayList<State>();
        this.invariant = null;
        this.stateType = null;
        this.stateElement = null;
        this.visited = false;
        this.initialState = false;
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
        this.uniqueName = generateUniqueName();
    }

    /**
     * @return the invariant
     */
    public Stereotype getInvariant() {
        return invariant;
    }

    /**
     * @param invariant the invariant to set
     */
    public void setInvariant(Stereotype invariant) {
        this.invariant = invariant;
    }

    /**
     * @return the states
     */
    public List<State> getChildren() {
        return children;
    }

    /**
     * @param states the states to set
     */
    public void setChildren(List<State> states) {
        this.children = states;
    }

    /**
     * @return the outgoing
     */
//    public List<Transition> getOutgoing() {
//        return outgoingTransitions;
//    }

    /**
     * @param outgoing the outgoing to set
     */
//    public void setOutgoing(List<Transition> outgoing) {
//        this.outgoingTransitions = outgoing;
//    }

    /**
     * @return the incoming
     */
//    public List<Transition> getIncoming() {
//        return incomingTransitions;
//    }

    /**
     * @param incoming the incoming to set
     */
//    public void setIncoming(List<Transition> incoming) {
//        this.incomingTransitions = incoming;
//    }
    
    public void addChild(State state) {
        this.children.add(state);
    }

    /**
     * @return the stateType
     */
    public StateType getStateType() {
        return stateType;
    }

    /**
     * @param stateType the stateType to set
     */
    public void setStateType(StateType stateType) {
        this.stateType = stateType;
        
        if (this.stateType != StateType.UML_CompositeState) {
            this.children = null;
        }
    }

    /**
     * @return the stateElement
     */
    public Element getStateElement() {
        return stateElement;
    }

    /**
     * @param stateElement the stateElement to set
     */
    public void setStateElement(Element stateElement) {
        this.stateElement = stateElement;
    }

    /**
     * @return the visited
     */
    public boolean isVisited() {
        return visited;
    }

    /**
     * @param visited the visited to set
     */
    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    /**
     * @return the initialState
     */
    public boolean isInitialState() {
        return initialState;
    }

    /**
     * @param initialState the initialState to set
     */
    public void setInitialState(boolean initialState) {
        this.initialState = initialState;
    }
    
    public String printChildren() {
        String result = "";
        
        if (children == null) {
            return result;
        }
        
        for (Iterator<State> it = children.iterator(); it.hasNext();) {
            State child = it.next();
            result += "Child = " + child.getName() + "\n";          
        }
        return result;
    }
    
    public State getInicialState() {
        State result = null;
        if(this.stateType != StateType.UML_CompositeState) {
            return result;
        }
        
        for (Iterator<State> it = children.iterator(); it.hasNext();) {
            State state = it.next();
            if(state.isInitialState()) {
                result = state;
            }
        }
        return result;
    }

    /**
     * @return the uniqueName
     */
    public String getUniqueName() {
        return uniqueName;
    }
    
    @Override
    public String toString() {
        String result = "";
        result = "Id = " + this.id + "\n" +
                 "Name = " + this.name; 
        
        if(this.invariant != null) {
            result += "Invariant = " + this.invariant;
        }
        
        result += printChildren();
        result += "\nInicial State = " + this.initialState;
        return result;
    }

    private String generateUniqueName() {
        String result = "";
        if (this.name == null || this.name.equals("")) {
            result += ""+counter;
            counter++;
        } else {
            result = this.name;
        }
        
        return result;
    }
    
    public boolean isCompositeState() {
        boolean result = false;
        if(this.stateType == StateType.UML_CompositeState) {
            result = true;
        }
        return result;
    }
    
    public boolean isSimpleState() {
        boolean result = false;
        if(this.stateType == StateType.UML_SimpleState) {
            result = true;
        }
        return result;
    }
    
    public boolean isPseudoState() {
        boolean result = false;
        if(this.stateType == StateType.UML_Pseudostate) {
            result = true;
        }
        return result;
    }
    
    public boolean isFinalState() {
        boolean result = false;
        if(this.stateType == StateType.UML_FinalState) {
            result = true;
        }
        return result;
    }
}
