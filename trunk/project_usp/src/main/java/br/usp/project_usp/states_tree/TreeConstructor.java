package br.usp.project_usp.states_tree;

import br.usp.project_usp.diagram.state_diagram.State;
import br.usp.project_usp.diagram.state_diagram.StateDiagram;
import br.usp.project_usp.diagram.state_diagram.StateType;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author pedro
 */
public class TreeConstructor {
    //TODO: Seria um construtor que irá percorrer os estados e as transições e irá construir o objeto Tree

    private GraphViz gv;
    private StateDiagram stateDiagram;

    public TreeConstructor() {
        gv = new GraphViz();
        gv.addln(gv.start_graph());
    }

    public void buildTree(StateDiagram diagram) {
        this.stateDiagram = diagram;
        State top = stateDiagram.getTopCompositeState();
        traverseTree(top);
        gv.addln(gv.end_graph());
//        System.out.println(gv.getDotSource());

//        digraph g {
//            pessoa1 [label="João Sem Dono", shape=box, style=filled, color=blue, height=0.5] ;
//            pessoa2 [label="Maria Preá", style=filled, color=pink];
//            pessoa1 -> pessoa2 [label="Gosta de"]
//        }

        String type = "gif";
//      String type = "dot";
//      String type = "fig";    // open with xfig
//      String type = "pdf";
//      String type = "ps";
//      String type = "svg";    // open with inkscape
//      String type = "png";
//      String type = "plain";
//      File out = new File("/tmp/out." + type);   // Linux
//      File out = new File("c:/eclipse.ws/graphviz-java-api/out." + type);    // Windows        
        File out = new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/project_usp/out." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }

//    public void traverseTree(State state) {
//        if (state.getStateType() == StateType.UML_CompositeState) {
//            state.setVisited(true);
//            State root = state.getInicialState();
//            if(root != null) {
//                traverseTree(root);
//            }
//        } else {
//            state.setVisited(true);
//            gv.add(state.getUniqueName() + "[label=\"" + state.getUniqueName() + "\"];");
//            
//            List<State> rootTargets = stateDiagram.getTargetStates(state);
//            if (!rootTargets.isEmpty()) {
//                for (Iterator<State> it = rootTargets.iterator(); it.hasNext();) {
//                    State target = it.next();
//                    if (!target.isVisited()) {
//                        gv.add(state.getUniqueName() + "->" + target.getUniqueName()+";");
//                        System.out.println(state.getUniqueName() + "->" + target.getUniqueName());
//                        traverseTree(target);
//                    }
//                }
//            }
//        }
//    }
    public void traverseTree(State state) {


        if (state.isCompositeState()) {
            state.setVisited(true);
            
            State root = state.getInicialState();
            if (root != null) {
                List<State> targets = stateDiagram.getTargetStates(state);
                if (!targets.isEmpty()) {
                    for (Iterator<State> it = targets.iterator(); it.hasNext();) {
                        State target = it.next();
                        if (!target.isVisited()) {
                            gv.add(state.getUniqueName() + "->" + target.getUniqueName() + ";");
                            traverseTree(target);
                        } else {
                            gv.add(state.getUniqueName() + "->" + target.getUniqueName() + ";");
                        }
                    }
                }
                traverseTree(root);
            }
        } else {
            state.setVisited(true);
            gv.add(state.getUniqueName() + "[label=\"" + state.getUniqueName() + "\"];");

            List<State> targets = stateDiagram.getTargetStates(state);
            if (!targets.isEmpty()) {
                for (Iterator<State> it = targets.iterator(); it.hasNext();) {
                    State target = it.next();
                    
                    if(target.getStateType() == StateType.UML_CompositeState) {
                        State initialState = target.getInicialState();
                        if (initialState != null) {
                            gv.add(state.getUniqueName() + "->" + initialState.getUniqueName() + ";");
                        }
                        
                    }
                    
                    if (!target.isVisited()) {
                        gv.add(state.getUniqueName() + "->" + target.getUniqueName() + ";");
                        traverseTree(target);
                    } else {
                        gv.add(state.getUniqueName() + "->" + target.getUniqueName() + ";");
                    }
                }
            }


        }
    }
}
