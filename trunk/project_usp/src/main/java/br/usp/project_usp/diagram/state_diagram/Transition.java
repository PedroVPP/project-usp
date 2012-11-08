/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagram.state_diagram;

/**
 *
 * @author pedro
 */
public class Transition {
    private String id;
    private State sourceState;
    private State targetState;
    private CallEvent callEvent;
    private Guard guard;

    public Transition() {
        this.sourceState = null;
        this.targetState = null;
        this.callEvent = null;
        this.guard = null;
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
     * @return the source
     */
    public State getSource() {
        return sourceState;
    }

    /**
     * @param source the source to set
     */
    public void setSource(State source) {
        this.sourceState = source;
    }

    /**
     * @return the target
     */
    public State getTarget() {
        return targetState;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(State target) {
        this.targetState = target;
    }

    /**
     * @return the callEvent
     */
    public CallEvent getCallEvent() {
        return callEvent;
    }

    /**
     * @param callEvent the callEvent to set
     */
    public void setCallEvent(CallEvent callEvent) {
        this.callEvent = callEvent;
    }

    /**
     * @return the guard
     */
    public Guard getGuard() {
        return guard;
    }

    /**
     * @param guard the guard to set
     */
    public void setGuard(Guard guard) {
        this.guard = guard;
    }
    
    @Override
    public String toString() {
        String result = "";
        
        result += "id = " + this.id + "\n" + 
                  "sourceStateId = " + this.sourceState + "\n" + 
                  "targetStateId = " + this.targetState + "\n";
        if(this.callEvent != null) {
            result += "callEvent = " + this.callEvent.toString() + "\n";
        }
        if(this.guard != null) {
            result += "guard = " + this.guard.toString();
        }
        
        return result;
    }
}
