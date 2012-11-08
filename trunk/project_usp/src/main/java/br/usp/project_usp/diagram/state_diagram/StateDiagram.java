package br.usp.project_usp.diagram.state_diagram;

import br.usp.project_usp.diagram.Diagram;
import br.usp.project_usp.diagram.DiagramType;
import java.io.File;
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
public class StateDiagram extends Diagram {

    private List<State> states;
    private List<Transition> transitions;

    public StateDiagram(File file, Document document) {

        super(file, document, DiagramType.StateDiagram);

        states = new ArrayList<State>();
        transitions = new ArrayList<Transition>();
        constructStates(document);
        constructTransitions(document);
    }

    private void constructStates(Document document) {
        String xPathStatesLocator = "//" + StateType.UML_CompositeState.toString() + "[@xmi.id and @name]" + " | "
                + "//" + StateType.UML_FinalState.toString() + "[@xmi.id]" + " | "
                + "//" + StateType.UML_Pseudostate.toString() + "[@xmi.id]" + " | "
                + "//" + StateType.UML_SimpleState.toString() + "[@xmi.id and @name]";

        for (Iterator<Element> it = document.selectNodes(xPathStatesLocator).iterator(); it.hasNext();) {
            Element stateElement = it.next();

            State newState = new State();
            newState.setId(stateElement.attributeValue("xmi.id"));
            newState.setName(stateElement.attributeValue("name"));
            newState.setStateType(StateType.obtainStateType(stateElement.getName())); // o método setStateType já define como null os filhos dos estados que não são compostos
            newState.setStateElement(stateElement);
            
            if(newState.getStateType() == StateType.UML_Pseudostate) {
                if (stateElement.attributeValue("kind").equals("initial")) {
                    newState.setInitialState(true);
                }
            }
            
            
            Stereotype invariant = new Stereotype();
            String xPathStereotypeId = stateElement.getUniquePath() + "/descendant::UML:Stereotype/@xmi.idref";
            Attribute stereotypeIdAttribute = (Attribute) stateElement.selectSingleNode(xPathStereotypeId);
            if (stereotypeIdAttribute != null) {
                String stereotypeId = stereotypeIdAttribute.getText();
                Element stereotype = (Element) stateElement.selectSingleNode("//UML:Stereotype[@xmi.id='" + stereotypeId + "']");
                invariant.setId(stereotype.attributeValue("xmi.id"));
                invariant.setName(stereotype.attributeValue("name"));
                // UML:ModelElement.stereotype > UML:Stereotype
                newState.setInvariant(invariant);
            }
            
            states.add(newState);
        }
        
        for (Iterator<State> it = states.iterator(); it.hasNext();) {
            State state = it.next();
            System.out.println("ESTADO ATUAL = " + state.getName());
            if (state.getStateType() == StateType.UML_CompositeState) {
                System.out.println("ENTREI");
                String xPathChildrenLocator = state.getStateElement().getUniquePath() + "/UML:CompositeState.subvertex/*";
                for (Iterator<Element> it2 = document.selectNodes(xPathChildrenLocator).iterator(); it2.hasNext();) {
                    Element childElement = it2.next();
                    State child = findStateById(childElement.attributeValue("xmi.id"));
                    state.addChild(child);
                }
            }
            
        }
    }

    private void constructTransitions(Document document) {

        String xPathTransitionsLocator = "//UML:Transition[@xmi.id]"; // every UML:Transition node that has the xmi.id attribute

        for (Iterator<Element> it = document.selectNodes(xPathTransitionsLocator).iterator(); it.hasNext();) {
            Element transitionElement = it.next();

            Transition newTransition = new Transition();
            newTransition.setId(transitionElement.attributeValue("xmi.id"));

            String stateId = document.selectSingleNode(transitionElement.getUniquePath() + "/UML:Transition.source/*/@xmi.idref").getText();
            State source = findStateById(stateId);
            stateId = document.selectSingleNode(transitionElement.getUniquePath() + "/UML:Transition.target/*/@xmi.idref").getText();
            State target = findStateById(stateId);
            newTransition.setSource(source);
            newTransition.setTarget(target);
//          UML:Transition.source -> UML:SimpleState, UML:Pseudostate, UML:CompositeState, UML:FinalState
//          UML:Transition.target -> UML:SimpleState, UML:Pseudostate, UML:CompositeState, UML:FinalState            


            CallEvent callEvent = new CallEvent();
            String xPathCallEventId = transitionElement.getUniquePath() + "/descendant::UML:CallEvent/@xmi.idref";
            Attribute callEventIdAttribute = (Attribute) transitionElement.selectSingleNode(xPathCallEventId);
            if (callEventIdAttribute != null) {
                String callEventId = callEventIdAttribute.getText();
                Element callEventElement = (Element) transitionElement.selectSingleNode("//UML:CallEvent[@xmi.id='" + callEventId + "']");
                callEvent.setId(callEventElement.attributeValue("xmi.id"));
                callEvent.setName(callEventElement.attributeValue("name"));

                String xPathParameterLocator = callEventElement.getUniquePath() + "/descendant::UML:Parameter";
                for (Iterator<Element> it2 = callEventElement.selectNodes(xPathParameterLocator).iterator(); it2.hasNext();) {
                    Element parameterElement = it2.next();
                    Parameter parameter = new Parameter();
                    parameter.setId(parameterElement.attributeValue("xmi.id"));
                    parameter.setName(parameterElement.attributeValue("name"));
                    callEvent.addParameter(parameter);
                }
                newTransition.setCallEvent(callEvent);
                // TODO: O sout do parameter la na classe de teste da imprimindo alguns resultados só com o nome e sem a id. Preciso checar se não tem algum bug
//          UML:Transition.trigger -> UML:CallEvent
            }

            String xPathGuardLocator = transitionElement.getUniquePath() + "/descendant::UML:Guard/UML:Guard.expression/UML:BooleanExpression";
            Element guardElement = (Element) document.selectSingleNode(xPathGuardLocator);
            if (guardElement != null) {
                Guard newGuard = new Guard();
                newGuard.setId(guardElement.attributeValue("xmi.id"));
                newGuard.setName(guardElement.attributeValue("body"));
                newTransition.setGuard(newGuard);
            }
//          UML:Transition.guard -> UML:Guard                 

            this.transitions.add(newTransition);
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

    public State findStateById(String id) {

        State result = null;

        for (Iterator<State> it = states.iterator(); it.hasNext();) {
            State state = it.next();

            if (state.getId().equals(id)) {
                result = state;
            }
        }

        return result;
    }

    public State findStateByName(String name) {
        State result = null;

        for (Iterator<State> it = states.iterator(); it.hasNext();) {
            State state = it.next();

            if (state.getName().equals(name)) {
                result = state;
                break;
            }
        }

        return result;
    }

    public Transition findTransitionById(String id) {

        Transition result = null;

        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();

            if (transition.getId().equals(id)) {
                result = transition;
            }
        }

        return result;
    }
    
    public List<State> getInicialStates() {
        List<State> listOfInicialStates = new ArrayList<State>();
        for (Iterator<State> it = states.iterator(); it.hasNext();) {
            State state = it.next();
            if(state.isInitialState()) {
                listOfInicialStates.add(state);
            }
        }
        return listOfInicialStates;
    }
    
    public State getTopCompositeState() {
        return findStateByName("top");
    }

    /**
     * Get the states that the given states has transitions to
     * @param state the given state
     * @return 
     */
    public List<State> getTargetStates(State state) {
        List<State> targets = new ArrayList<State>();
        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();
            if(transition.getSource().getId().equals(state.getId())) {
                targets.add(transition.getTarget());
            }
        }
        return targets;
    }
}
