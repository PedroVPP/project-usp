package br.usp.project_usp.states_tree;

import br.usp.project_usp.diagram.state_diagram.StateDiagram;
import br.usp.project_usp.parser.Parser;
import java.io.File;
import org.dom4j.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author pedro
 */
public class TreeConstructorTest {
    
    public TreeConstructorTest() {
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

    /**
     * Test of buildTree method, of class TreeConstructor.
     */
    @Test
    public void testBuildTree() {
        System.out.println("buildTree");
        Parser parser = new Parser();
        File xmiFile = new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/argouml_protocolstatemachine_test.xmi");
        Document document = parser.parse(xmiFile);
        StateDiagram stateDiagram = new StateDiagram(xmiFile, document);
        TreeConstructor instance = new TreeConstructor();
        instance.buildTree(stateDiagram);
    }
}
