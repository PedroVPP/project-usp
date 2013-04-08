package br.usp.project_usp.graph_construction;

import br.usp.project_usp.diagrams.state_diagram.State;
import br.usp.project_usp.diagrams.state_diagram.StateDiagram;
import java.io.File;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author pedro
 */
public class GraphConstructor {
    
    private GraphViz gv;
    private StateDiagram stateDiagram;

    /**
     * Instantiates the graphviz object with the starting sentence code ("digraph G {")
     */
    public GraphConstructor() {
        // the graphviz object responsable for making the figures (nodes and edges)
        gv = new GraphViz();
        gv.addln(gv.start_graph());
    }

    /**
     * Makes the graph based on the given state diagram
     * @param stateDiagram the given state diagram
     */
    public void buildGraph(StateDiagram stateDiagram) {
        this.stateDiagram = stateDiagram;
        State root = stateDiagram.getRoot();
        traverseGraph(root);
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
        File out = new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/project_usp/generated_graphs/out." + type);
        gv.writeGraphToFile(gv.getGraph(gv.getDotSource(), type), out);
    }

    public void traverseGraph(State state) {
        state.setVisited(true);
        gv.add(state.getUniqueName() + " [label=\"" + state.getUniqueName() + "\"];");

        List<State> targets = stateDiagram.findTargetStates(state);
        
        if (!targets.isEmpty()) {
            
            for (Iterator<State> it = targets.iterator(); it.hasNext();) {
                State target = it.next();
                gv.add(state.getUniqueName() + "->" + target.getUniqueName() + " [label=\"" + stateDiagram.findTransitionBySourceAndTargetState(state, target).getName() + "\"];");

                if (!target.isVisited()) {
                    traverseGraph(target);
                }
            }
        }
    }
}

//    public void traverseGraph(State state) {
//        
//        state.setVisited(true);
//        gv.add(state.getUniqueName() + " [label=\"" + state.getInvariantName() + "\"];");
//
//        List<State> targets = stateDiagram.findTargetStates(state);
//        if (!targets.isEmpty()) {
//            for (Iterator<State> it = targets.iterator(); it.hasNext();) {
//                State target = it.next();
//                gv.add(state.getUniqueName() + "->" + target.getUniqueName() + " [label=\"" + stateDiagram.findTransitionBySourceAndTargetState(state, target).getName() + "\"];");
//
//                if (!target.isVisited()) {
//                    traverseGraph(target);
//                }
//            }
//        }
//
//    }