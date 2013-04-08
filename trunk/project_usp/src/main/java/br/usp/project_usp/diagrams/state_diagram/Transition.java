/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagrams.state_diagram;

import java.util.Iterator;

/**
 *
 * @author pedro
 */
public class Transition {
    private String id;
    private State sourceState;
    private State targetState;
    // the transition event
    private CallEvent callEvent;
    // the precondition
    private Guard guard;
    private boolean visited;

    public Transition() {
        this.sourceState = null;
        this.targetState = null;
        this.callEvent = null;
        this.guard = null;
        this.visited = false;
    }
    
    /**
     * this constructor copies every attribute of the param transition t into the new transition.
     * @param t the transition to copy the attributes from.
     */
    public Transition(Transition t) {
        this.id = Math.random()*10000+"";
        this.sourceState = t.getSource();
        this.targetState = t.getTarget();
        this.callEvent = t.getCallEvent();
        this.guard = t.getGuard();
        this.visited = false;
    }
    
    /**
     * 
     * @param id
     * @param sourceState
     * @param targetState
     * @param callEvent
     * @param guard 
     */
    public Transition(String id, State sourceState, State targetState, CallEvent callEvent, Guard guard) {
        this.id = id;
        this.sourceState = sourceState;
        this.targetState = targetState;
        this.callEvent = callEvent;
        this.guard = guard;
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
        
        result += //"id = " + this.id + "\n" + 
                  "sourceStateId = " + this.sourceState.toString() + "\n" + 
                  "targetStateId = " + this.targetState.toString() + "\n";
        if(this.callEvent != null) {
            result += "callEvent = " + this.callEvent.toString() + "\n";
        }
        if(this.guard != null) {
            result += "guard = " + this.guard.toString();
        }
        
        return result;
    }
    
    public String toStringWithNoStatesInfo() {
        String result = "";
        
        result += "id = " + this.id + "\n";
        if(this.callEvent != null) {
            result += "callEvent = " + this.callEvent.toString() + "\n";
        }
        if(this.guard != null) {
            result += "guard = " + this.guard.toString();
        }
        
        return result;
    }
    
    public String getName() {
        String name = "";
        
        if(this.guard != null) {
            name += "[" + this.guard.getName() + "]";
        } 
        
        if(this.callEvent != null) {
            name += this.callEvent.getName();
            
            if(!this.callEvent.getParameters().isEmpty()) {
                name += "(";
                for (Iterator<Parameter> it = this.callEvent.getParameters().iterator(); it.hasNext();) {
                    Parameter parameter = it.next();
                    name += parameter.getName();
                    if(it.hasNext()) {
                        name += ", ";
                    }
                }
                name += ")";
            }
        }
        
        return name;
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
}
