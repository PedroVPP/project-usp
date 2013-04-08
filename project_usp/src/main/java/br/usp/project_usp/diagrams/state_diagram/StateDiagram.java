package br.usp.project_usp.diagrams.state_diagram;

import br.usp.project_usp.diagrams.Diagram;
import br.usp.project_usp.diagrams.DiagramType;
import br.usp.project_usp.errors.ProtocolStateMachineModelException;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

/**
 *
 * @author pedro
 */
public class StateDiagram extends Diagram {

    private List<State> states;
    private List<Transition> transitions;
    // the main root state
    private State root;

    /**
     * The constructer who instantiates the main variable and calls the most
     * important methods
     *
     * @param xmiFile the xmi file of the diagram
     * @param document the xmi file parsed by the SAXReader parser.
     */
    public StateDiagram(File xmiFile, Document document) {

        super(xmiFile, document, DiagramType.StateDiagram);

        states = new ArrayList<State>();
        transitions = new ArrayList<Transition>();
        constructStates(document);
        constructTransitions(document);
        // modifica o diagrama para que os estados dentro do "composite state"
        // saibam a invariançia e a saida do estado composto
        applyCompositeStateBehaviourToChildren();
        try {
            // altera as transições para os estados compostos, 
            // para que essas apontem para o estado inicial do estado composto.
            // Com isso é obrigatório que um estado composto tenha um estado inicial,
            // a não ser que exista uma transição diretamente para um estado dentro do estado composto
            modifyTransitionsToCompositeStates();
        } catch (ProtocolStateMachineModelException ex) {
            Logger.getLogger(StateDiagram.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.getMessage());
        }

        // an inneficiant way to find out the main root state. It only works if
        // there is a composed state called "top" that has the main initial state of all the diagram
        State top = findStateByName("top");
        root = top.getInicialState();

        // this method gets rid of the loops, by doing so it transforms
        // the cycle graph into an acyclic graph
        convertCycleGraphToAcyclicGraph(root);
        resetVisitedStates();
        resetVisitedTransitions();
        
        eliminateSelfExecutableTransitions(root);
        resetVisitedStates();
        resetVisitedTransitions();
        
//        eliminateUnreachableStatesAndTransitions(root);
    }

    /**
     * This method creates the states according to the states it founds on the
     * XMI file.
     *
     * @param document the xmi document
     */
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
            // the method setStateType defined as null the sons of states that are not composed states
            newState.setStateType(StateType.obtainStateType(stateElement.getName()));
            newState.setStateElement(stateElement);

            if (newState.getStateType() == StateType.UML_Pseudostate) {
                if (stateElement.attributeValue("kind").equals("initial")) {
                    newState.setInitialState(true);
                }
            }

            Invariant invariant = new Invariant();
//            String xPathStereotypeId = stateElement.getUniquePath() + "/descendant::UML:Invariant/@xmi.idref";
            String xPathStereotypeId = stateElement.getUniquePath() + "/UML:ModelElement.stereotype/UML:Stereotype/@xmi.idref";
            Attribute stereotypeIdAttribute = (Attribute) stateElement.selectSingleNode(xPathStereotypeId);
            if (stereotypeIdAttribute != null) {
                String stereotypeId = stereotypeIdAttribute.getText();
                Element stereotype = (Element) stateElement.selectSingleNode("//UML:Stereotype[@xmi.id='" + stereotypeId + "']");
                invariant.setId(stereotype.attributeValue("xmi.id"));
                invariant.setName(stereotype.attributeValue("name"));
                // UML:ModelElement.stereotype > UML:Invariant
                newState.setInvariant(invariant);
            }

            states.add(newState);
        }

        for (Iterator<State> it = states.iterator(); it.hasNext();) {
            State state = it.next();
//            System.out.println("ESTADO ATUAL = " + state.getName());
            if (state.getStateType() == StateType.UML_CompositeState) {
//                System.out.println("ENTREI");
                String xPathChildrenLocator = state.getStateElement().getUniquePath() + "/UML:CompositeState.subvertex/*";
                for (Iterator<Element> it2 = document.selectNodes(xPathChildrenLocator).iterator(); it2.hasNext();) {
                    Element childElement = it2.next();
                    State child = findStateById(childElement.attributeValue("xmi.id"));
                    state.addChild(child);
                }
            }

        }
    }

    /**
     * This method creates the transitions according to the transitions it
     * founds on the XMI file.
     *
     * @param document
     */
    private void constructTransitions(Document document) {
        // every UML:Transition node that has the xmi.id attribute
        String xPathTransitionsLocator = "//UML:Transition[@xmi.id]";

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
                newGuard.setName("[" + guardElement.attributeValue("body") + "]");
                newTransition.setGuard(newGuard);
            }
//          UML:Transition.guard -> UML:Guard                 

            this.transitions.add(newTransition);
        }
    }

    /**
     * This method makes the invariant and the transitions of a composite state
     * become the invariant and the transitions of all the children, so if a
     * composite state has the invariant x, all of its children will also have
     * the invariant x, the same goes for the transitions to other states.
     */
    public void applyCompositeStateBehaviourToChildren() {
        for (Iterator<State> it = getCompositeStates().iterator(); it.hasNext();) {
            State compositeState = it.next();


            for (Iterator<State> it2 = compositeState.getChildren().iterator(); it2.hasNext();) {
                State compositeStateChild = it2.next();
                // aplicando as invariancias do composite state PAI, para todos os filhos, exceto os que não são nem simple state nem composite state
                if (compositeState.hasInvariant() && compositeStateChild.isCommonState()) {

                    if (compositeStateChild.hasInvariant()) {
                        compositeStateChild.getInvariant().setName(compositeStateChild.getInvariant().getName() + " && " + compositeState.getInvariant().getName());
                    } else {
                        Invariant invariant = new Invariant();
                        invariant.setId(compositeState.getInvariant().getId());
                        invariant.setName(compositeState.getInvariant().getName());
                        compositeStateChild.setInvariant(invariant);
                    }
                }

                // aplicando as transições que saem do composite state PAI, para todos os filhos, exceto os que não são simple state e nem composite state
                for (Iterator<Transition> it3 = this.findTransitionsBySourceState(compositeState).iterator(); it3.hasNext();) { // localizar as transições que saiem do estado pai
                    Transition transition = it3.next();
                    if (compositeStateChild.isCommonState()) {
                        Transition newTransition = new Transition(transition.getId(), compositeStateChild, transition.getTarget(), transition.getCallEvent(), transition.getGuard());
                        this.transitions.add(newTransition);
                    }
                }
            }
        }
    }

    /**
     * As transições que vão para um estado composto serão modificadas para que
     * elas apontem para o estado o qual o estado inicial está apontando dentro
     * do estado composto.
     */
    public void modifyTransitionsToCompositeStates() throws ProtocolStateMachineModelException {
        for (Iterator<Transition> it = this.transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();
            if (transition.getTarget().isCompositeState()) {
                for (Iterator<State> it2 = transition.getTarget().getChildren().iterator(); it2.hasNext();) {
                    State child = it2.next();
                    if (child.isInitialState()) {

                        List<Transition> childTransitions = findTransitionsBySourceState(child);
                        if (childTransitions.size() > 1) {
                            throw new ProtocolStateMachineModelException("Error. Inicial state " + child.toString() + "has more than one transition that goes from him");
                        }
                        transition.setTarget(childTransitions.get(0).getTarget());
                    }
                }
            }
        }
    }
    List<State> visitedStates = new ArrayList<State>(); // this list stores visited states to be used by the algorithm

    /**
     * This method finds cyclic paths and converts them to acyclic by creating a
     * new state with no transitions to replace the state that has the loop.
     *
     * @param state the root state of the graph
     */
//    public void convertCycleGraphToAcyclicGraph(State state) {
//
//        visitedStates.add(state);
//        state.setVisited(true);
//
//        List<State> targets = findTargetStates(state);
//
//        for (Iterator<State> it = targets.iterator(); it.hasNext();) {
//            State target = it.next();
//            State source = findVisitedSourceState(findSourceStates(target));
//
//            if (source != null) {
////                System.out.println("Source state(PAI) = " + source.getName());
////                System.out.println("State = " + state.getName());
//                for (Iterator<State> it1 = visitedStates.iterator(); it1.hasNext();) {
//                    State aux = it1.next();
////                    System.out.println(aux.getName());
//                }
//
//                for (Iterator<State> it2 = visitedStates.iterator(); it2.hasNext();) {
//                    State aux = it2.next();
//                    if (aux.getId().equals(source.getId())) {
//                        int indexOf = visitedStates.indexOf(aux);
//                        List<State> erasedElements = visitedStates.subList(indexOf + 1, visitedStates.size());
//                        for (Iterator<State> it1 = erasedElements.iterator(); it1.hasNext();) {
//                            State state1 = it1.next();
//                            state1.setVisited(false);
//                        }
//                        visitedStates.removeAll(erasedElements);
//                    }
//                }
//            }
//
//            if (target.isVisited()) {
////              state 15 -> target 14
////              System.out.println("States: " + state.getUniqueName() + " -> " + target.getUniqueName());
//
//                State newState = new State(target);
//                this.addState(newState);
//                Transition transition = findTransitionBySourceAndTargetState(state, target);
//                transition.setTarget(newState);
//
////                State top = findStateByName("top");
////                convertCycleGraphToAcyclicGraph(top.getInicialState());
//
//            } else {
//                convertCycleGraphToAcyclicGraph(target);
//            }
//        }
//
//    }
    public void convertCycleGraphToAcyclicGraph(State state) {
        
        List<Transition> transitions = findTransitionsBySourceState(state);
        
        if(transitions.isEmpty()) {
            resetVisitedStates();
        }
        
        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            state.setVisited(true);
            Transition transition = it.next();
            State target = transition.getTarget();
            
            if (!target.isVisited()) {
                convertCycleGraphToAcyclicGraph(target);
            } else {
                State newState = new State(target);
                this.addState(newState);
                Transition oldTransition = findTransitionBySourceAndTargetState(state, target);
                oldTransition.setTarget(newState);
            }
        }

    }

//    private void traverseStates(State state, Transition lastTransition) {
//        if (state.isVisited()) {
//            System.out.println(state.getUniqueName());
//            List<Transition> transitions = findTransitionsBySourceState(state);
//            if (!transitions.isEmpty()) {
//                for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
//                    Transition transition = it.next();
//                    if (transition.isVisited()) {
//
//                        State newState = new State(state);
//                        newState.setVisited(true);
//                        this.states.add(newState);
//                        lastTransition.setTarget(newState);
//                        state = newState;
//                    }
//                }
//            }
//        } else {
//            System.out.println(state.getUniqueName());
//            state.setVisited(true);
//
//            List<State> targets = findTargetStates(state);
//            if (!targets.isEmpty()) {
//                for (Iterator<State> it = targets.iterator(); it.hasNext();) {
//                    State target = it.next();
//                    Transition transitionFromSourcetoTarget = findTransitionBySourceAndTargetState(state, target);
//
//                    if (!transitionFromSourcetoTarget.isVisited()) {
//                        System.out.println(state.getUniqueName() + "->" + target.getUniqueName());
//                        transitionFromSourcetoTarget.setVisited(true);
//                        traverseStates(target, transitionFromSourcetoTarget);
//
//                        // loop is defined when a state is already visitedStates and posses transitions to targets that already has been visitedStates
//                    }
//                }
//            }
//        }
//    }
    public List<State> getCompositeStates() {
        List<State> compositeStates = new ArrayList<State>();
        for (Iterator<State> it = this.states.iterator(); it.hasNext();) {
            State state = it.next();
            if (state.isCompositeState()) {
                compositeStates.add(state);
            }
        }
        return compositeStates;
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

    /**
     * @return the root
     */
    public State getRoot() {
        return root;
    }

    /**
     * @param root the root to set
     */
    public void setRoot(State root) {
        this.root = root;
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
            if (state.isInitialState()) {
                listOfInicialStates.add(state);
            }
        }
        return listOfInicialStates;
    }

    /**
     * Get the states that the given state has transitions to
     *
     * @param state the given state
     * @return
     */
    public List<State> findTargetStates(State state) {

        List<State> targets = new ArrayList<State>();
        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();
//            System.out.println("ID da transition source: " + transition.getSource().getId());
//            System.out.println("ID da transition target: " + state.getId());
            if (transition.getSource().getId().equals(state.getId())) {
                targets.add(transition.getTarget());
            }
        }
        return targets;
    }

    /**
     * Get the states that has transitions to the given state
     *
     * @param state the given state
     * @return
     */
    public List<State> findSourceStates(State state) {
        List<State> sources = new ArrayList<State>();
        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();
            if (transition.getTarget().getId().equals(state.getId())) {
                sources.add(transition.getSource());
            }
        }
        return sources;
    }

    public Transition findTransitionBySourceAndTargetState(State source, State target) {
        Transition result = null;
        for (Iterator<Transition> it = this.transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();

            if (transition.getSource().getId().equals(source.getId())
                    && transition.getTarget().getId().equals(target.getId())) {
                result = transition;
            }
        }
        return result;
    }

    public List<Transition> findTransitionsBySourceState(State source) {
        List<Transition> listOfTransitions = new ArrayList<Transition>();
        for (Iterator<Transition> it = this.transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();

            if (transition.getSource().getId().equals(source.getId())) {
                listOfTransitions.add(transition);
            }
        }
        return listOfTransitions;
    }

    public List<Transition> findTransitionsByTargetState(State target) {
        List<Transition> listOfTransitions = new ArrayList<Transition>();
        for (Iterator<Transition> it = this.transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();

            if (transition.getTarget().getId().equals(target.getId())) {
                listOfTransitions.add(transition);
            }
        }
        return listOfTransitions;
    }

    // TODO: This method needs to be tested
    public List<Transition> findTransitionsByCallEvent(CallEvent event) {
        List<Transition> listOfTransitions = new ArrayList<Transition>();
        for (Iterator<Transition> it = this.transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();

            if (transition.getCallEvent() != null) {
                if (transition.getCallEvent().getId().equals(event.getId())) {
                    listOfTransitions.add(transition);
                }
            }
        }
        return listOfTransitions;
    }

    public void deleteTransitionsBySource(State source) {
        for (Iterator<Transition> it = this.transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();

            if (transition.getSource().getId().equals(source.getId())) {
                this.transitions.remove(transition);
            }
        }
    }

    public void resetVisitedStates() {
        for (Iterator<State> it = states.iterator(); it.hasNext();) {
            State state = it.next();
            state.setVisited(false);
        }
    }

    public void resetVisitedTransitions() {
        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();
            transition.setVisited(false);
        }
    }

    public Transition findVisitedTransitionBySourceState(State state) {
        Transition result = null;
        List<Transition> transitions = findTransitionsBySourceState(state);
        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();
            if (transition.isVisited()) {
                result = transition;
            }
        }

        return result;
    }

    public Transition findVisitedTransitionByTargetState(State state) {
        Transition result = null;
        List<Transition> transitions = findTransitionsByTargetState(state);
        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();
            if (transition.isVisited()) {
                result = transition;
            }
        }

        return result;
    }

    /**
     * This method returns true if the given state has some transition that
     * comes out of it (target transition) that has already been visited
     *
     * @param state
     * @return
     */
    public boolean hasVisitedTargetTransitions(State state) {
        List<Transition> transitions = findTransitionsBySourceState(state);

        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();
            if (transition.isVisited()) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    private State findVisitedSourceState(List<State> sourceStates) {
        State result = null;
        for (Iterator<State> it = sourceStates.iterator(); it.hasNext();) {
            State state = it.next();
            if (state.isVisited()) {
                result = state;
            }
        }

        return result;
    }

    private void eliminateUnreachableStatesAndTransitions(State root) {

        markVisitedStatesAndTransitions(root);
        List<State> removeStates = new ArrayList<State>();
        for (Iterator<State> it = states.iterator(); it.hasNext();) {
            State state = it.next();
            if (!state.isVisited()) {
                removeStates.add(state);
            }
        }
        states.removeAll(removeStates);

        List<Transition> removeTransitions = new ArrayList<Transition>();
        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            Transition transition = it.next();
            if (!transition.isVisited()) {
                removeTransitions.add(transition);
            }
        }
        transitions.removeAll(removeTransitions);
    }

    private void markVisitedStatesAndTransitions(State state) {
        state.setVisited(true);

        List<State> targets = findTargetStates(state);
        if (!targets.isEmpty()) {
            for (Iterator<State> it = targets.iterator(); it.hasNext();) {
                State target = it.next();

                if (!target.isVisited() || !findTransitionBySourceAndTargetState(state, target).isVisited()) {
                    Transition transition = findTransitionBySourceAndTargetState(state, target);
                    transition.setVisited(true);
                    markVisitedStatesAndTransitions(target);
                }
            }
        }
    }

    /**
     * This method eliminates the self executable transitions, i.e, the
     * transitions that does not have any condition to run, like a CallEvent or
     * a Guard. It also adapts the states as necessary, by eliminating the
     * unnecessary states. This method uses a similar traverse algorithm used by
     * the transition coverage criteria.
     *
     * @param root
     */
    private void eliminateSelfExecutableTransitions(State root) {
        // transitions from source state
        List<Transition> transitions = this.findTransitionsBySourceState(root);
        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
            // the transition
            Transition transition = it.next();
            if (!transition.isVisited()) {
                transition.setVisited(true);
                // source -> target
                State source = transition.getSource();
                State target = transition.getTarget();
                // transition that does not have a guard or callevent
                if (transition.getCallEvent() == null && transition.getGuard() == null) {
                    // make the source state identical to the other (same invariant, same transitions, name?)
                    source.setName(source.getName() + " + " + target.getName()); // necessary?

                    // copying the invariants
                    if (source.getInvariant() == null && target.getInvariant() != null) {
                        source.setInvariant(target.getInvariant());
                    } else if (source.getInvariant() != null && target.getInvariant() != null) {
                        source.getInvariant().setName(source.getInvariantName() + " && " + target.getInvariantName());
                    }

                    // create new transitions identicial to the target's transitions
                    List<Transition> targetTransitions = this.findTransitionsBySourceState(target);
                    for (Iterator<Transition> it1 = targetTransitions.iterator(); it1.hasNext();) {
                        Transition targetTransition = it1.next();
                        Transition newTransition = new Transition(targetTransition.getId(), source, targetTransition.getTarget(), targetTransition.getCallEvent(), targetTransition.getGuard());
                        this.transitions.add(newTransition);
                    }
                    this.transitions.remove(transition);
//                    target = source;
                }
                eliminateSelfExecutableTransitions(target);
            }
        }
    }
//        private void eliminateSelfExecutableTransitions(State root) {
//        // transitions from source state
//        List<Transition> transitions = this.findTransitionsBySourceState(root);
//        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
//            // the transition
//            Transition transition = it.next();
//            if (!transition.isVisited()) {
//                State source = transition.getSource();
//                State target = transition.getTarget();
//                // transition = root -> target
//                transition.setVisited(true);
//                if(transition.getCallEvent() == null  && transition.getGuard() == null) {
//                    System.out.println("Source: " + transition.getSource().getUniqueName() + " Target: " + transition.getTarget().getUniqueName());
//                    List<Transition> targetTransitions = this.findTransitionsBySourceState(target);
//                    for (Iterator<Transition> it1 = targetTransitions.iterator(); it1.hasNext();) {
//                        Transition targetTransition = it1.next();
//                        Transition newTransition = new Transition(targetTransition);
//                        newTransition.setSource(root);
//                        this.transitions.add(newTransition);
//                    }
//                    this.transitions.remove(transition);
//                    target = this.root;
//                    resetVisitedTransitions();
//                }
//                eliminateSelfExecutableTransitions(target);
//            }
//        }
//    }
//    private void eliminateSelfExecutableTransitions() {
//        // for all the transitions
//        for (Iterator<Transition> it = transitions.iterator(); it.hasNext();) {
//            // the transition
//            Transition transition = it.next();
//            // get only the ones that don't have a guard or a callevent
//            if (transition.getCallEvent() == null && transition.getGuard() == null) {
//                // the source and target states
//                State source = transition.getSource();
//                State target = transition.getTarget();
//                
//                
//                
//                List<Transition> targetTransitions = this.findTransitionsBySourceState(target);
//                for (Iterator<Transition> it1 = targetTransitions.iterator(); it1.hasNext();) {
//                    Transition targetTransition = it1.next();
//                    Transition newTransition = new Transition(targetTransition);
//                    newTransition.setSource(root);
//                    this.transitions.add(newTransition);
//                }
//                this.transitions.remove(transition);
//                target = this.root;
//                resetVisitedTransitions();
//            }
//        }
//    }
}
