/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagram.state_diagram;

/**
 *
 * @author pedro
 */
public enum StateType {
    UML_SimpleState, UML_Pseudostate, UML_CompositeState, UML_FinalState;
    
    public static String toString(StateType type) {
        if(type == StateType.UML_SimpleState) {
            return "UML:SimpleState";
        } else if (type == StateType.UML_Pseudostate) {
            return "UML:Pseudostate";
        } else if (type == StateType.UML_CompositeState) {
            return "UML:CompositeState";
        } else if (type == StateType.UML_FinalState) {
            return "UML:FinalState";
        } else {
            return null;
        }

    }
    
    public static String[] getStateTypes() {
        String[] array = {"UML:SimpleState","UML:Pseudostate","UML:CompositeState, UML:FinalState"};
        return array;
    }
    
}
