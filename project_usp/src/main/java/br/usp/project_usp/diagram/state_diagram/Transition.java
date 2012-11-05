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
    private String sourceStateId;
    private String targetStateId;
    private CallEvent callEvent;
    private Guard guard;

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
    public String getSource() {
        return sourceStateId;
    }

    /**
     * @param source the source to set
     */
    public void setSource(String source) {
        this.sourceStateId = source;
    }

    /**
     * @return the target
     */
    public String getTarget() {
        return targetStateId;
    }

    /**
     * @param target the target to set
     */
    public void setTarget(String target) {
        this.targetStateId = target;
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
        
        result += "id = " + this.id + "\n";
        result += "sourceStateId = " + this.sourceStateId + "\n";
        result += "targetStateId = " + this.targetStateId;
        
        return result;
    }
}
