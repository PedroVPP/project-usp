package br.usp.project_usp.diagram.state_diagram;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pedro
 */
public class State {
    private String id;
    private String name;
    private Stereotype invariant;
    private List<State> states;
//    private List<Transition> outgoingTransitions;
//    private List<Transition> incomingTransitions;

    public State() {
        states = new ArrayList<State>();
//        outgoingTransitions = new ArrayList<Transition>();
//        incomingTransitions = new ArrayList<Transition>();
    }
    
    public State(String id, String name) {
        this.id = id;
        this.name = name;
        states = new ArrayList<State>();
//        outgoingTransitions = new ArrayList<Transition>();
//        incomingTransitions = new ArrayList<Transition>();
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
    
    public void addState(State state) {
        this.states.add(state);
    }
    
//    public void addOutgoingTransition(Transition transition) {
//        this.outgoingTransitions.add(transition);
//    }
    
//    public void addIncomingTransition(Transition transition) {
//        this.incomingTransitions.add(transition);
//    }
}
