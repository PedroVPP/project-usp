/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagram.state_diagram;

import br.usp.project_usp.parser.Parser;
import java.io.File;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.dom4j.Document;

/**
 *
 * @author pedro
 */
public class StateDiagramTest {
    
    public StateDiagramTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testCorrectStateTypeName() {
        assertEquals("UML:CompositeState", StateType.UML_CompositeState.toString());
        assertEquals("UML:FinalState", StateType.UML_FinalState.toString());
        assertEquals("UML:Pseudostate", StateType.UML_Pseudostate.toString());
        assertEquals("UML:SimpleState", StateType.UML_SimpleState.toString());
    }
    
    @Test
    public void testNumberOfTransitions() {
        Parser parser = new Parser();
        File xmiFile = new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/argouml_protocolstatemachine_test.xmi");
        Document document = parser.parse(xmiFile);
        StateDiagram stateDiagram = new StateDiagram(xmiFile, document);
        for (int i = 0; i < stateDiagram.getTransitions().size(); i++) {
            System.out.println("Transition = " + stateDiagram.getTransitions().get(i).toString() + "\n");
        }
        
        assertEquals(14,stateDiagram.getTransitions().size());
    }
    
    @Test
    public void testNumberOfStates() {
        Parser parser = new Parser();
        File xmiFile = new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/argouml_protocolstatemachine_test.xmi");
        Document document = parser.parse(xmiFile);
        StateDiagram stateDiagram = new StateDiagram(xmiFile, document);
        
        for (int i = 0; i < stateDiagram.getStates().size(); i++) {
            System.out.println("State = " + stateDiagram.getStates().get(i).toString());
        }
        
        assertEquals(17,stateDiagram.getStates().size());
    }
}
