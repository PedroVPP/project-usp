package br.usp.project_usp.diagrams.state_diagram;

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
    private Invariant invariant;
    private List<State> children;
    private StateType stateType;
    // the xml element that holds information about that state
    private Element stateElement;
    private boolean visited;
    private boolean initialState;
    private static int counter = 0;

    public State() {
        this.id = null;
        this.name = null;
        this.uniqueName = null;
        this.children = new ArrayList<State>();
        this.invariant = null;
        this.stateType = null;
        this.stateElement = null;
        this.visited = false;
        this.initialState = false;
        
    }
    
    /**
     * this constructor copies every attribute of the param state s into the new state.
     * @param s the state to copy the attributes from.
     */
    public State(State s) {
        this.id = Math.random()*10000+"";
        this.setName(s.getName());
        this.invariant = s.getInvariant();
        this.children = s.getChildren();
        this.setStateType(s.getStateType());
        this.stateElement = s.getStateElement();
        this.visited = s.isVisited();
        this.initialState = s.isInitialState();
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
    public Invariant getInvariant() {
        return invariant;
    }

    /**
     * @param stereotype the invariant to set
     */
    public void setInvariant(Invariant stereotype) {
        this.invariant = stereotype;
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
        if (this.uniqueName == null) {
            this.uniqueName = generateUniqueName();
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
            return "Children = null";
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
                 "Name = " + this.name + "\n"; 
        
        if(hasInvariant()) {
            result += this.invariant.toString() + "\n";
        } else {
            result += "\n";
        }
        
        result += printChildren();
        result += "Inicial State = " + this.initialState;
        return result;
    }

    public String generateUniqueName() {
        String result = null;
        
        if (this.getStateType() != null) {
            result = "" + counter;
            counter++;
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
    
    public boolean hasInvariant() {
        if (this.invariant == null) {
            return false;
        } else {
            return true;
        }
    }
    
    public String getInvariantName() {
        if(this.getInvariant() == null) {
            return "";
        } else {
            return this.getInvariant().getName();
        }
    }
    
    /**
     * @return This method returns true if the state is a simple state or a composite state, otherwise returns false.
     */
    public boolean isCommonState() {
        if (isCompositeState() || isSimpleState()) {
            return true;
        } else {
            return false;
        }
    }
}
