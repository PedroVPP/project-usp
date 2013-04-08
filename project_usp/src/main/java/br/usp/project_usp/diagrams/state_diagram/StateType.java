/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagrams.state_diagram;

/**
 *
 * @author pedro
 */
public enum StateType {

    UML_SimpleState, UML_Pseudostate, UML_CompositeState, UML_FinalState;

    @Override
    public String toString() {
        return this.name().replace('_', ':');
    }

    public static String[] getStateTypes() {
        String[] array = {"UML:SimpleState", "UML:Pseudostate", "UML:CompositeState, UML:FinalState"};
        return array;
    }

    static StateType obtainStateType(String elementName) {
        if (elementName.equals("CompositeState")) {
            return UML_CompositeState;
        } else if (elementName.equals("Pseudostate")) {
            return UML_Pseudostate;
        } else if (elementName.equals("FinalState")) {
            return UML_FinalState;
        } else if (elementName.equals("SimpleState")) {
            return UML_SimpleState;
        } else {
            return null;
        }
    }
}
