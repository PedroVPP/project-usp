/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagram.class_diagram;

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
public class ClassDiagramTest {
    
    public ClassDiagramTest() {
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
    public void testClassDiagramIdAndName() throws Exception {
        Parser parser = new Parser();
        File xmiFile = new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/argouml_classdiagram_test.xmi");
        Document document = parser.parse(xmiFile);
        
        ClassDiagram classDiagram = new ClassDiagram(xmiFile, document);
        
        assertEquals("127-0-1-1-626cf1bf:13a65d36f08:-8000:0000000000000D62", classDiagram.getId());
        assertEquals("diagramadeclasses", classDiagram.getName());
    }
    
    @Test
    public void testNumberOfClasses() throws Exception {
        Parser parser = new Parser();
        File xmiFile = new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/argouml_classdiagram_test.xmi");
        Document document = parser.parse(xmiFile);
        
        ClassDiagram classDiagram = new ClassDiagram(xmiFile, document);
        
        for (int i = 0; i < classDiagram.getClasses().size(); i++) {
            System.out.println("Classe = " + classDiagram.getClasses().get(i).getName());
        }
        
        assertEquals(9,classDiagram.getClasses().size());
    }
    
    @Test
    public void testClassesAttributes() throws Exception {
        Parser parser = new Parser();
        File xmiFile = new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/argouml_classdiagram_test.xmi");
        Document document = parser.parse(xmiFile);
        ClassDiagram classDiagram = new ClassDiagram(xmiFile, document);
        
        for (int i = 0; i < classDiagram.getClasses().size(); i++) {
            System.out.println("Classe = " + classDiagram.getClasses().get(i).getName());
            if(!(classDiagram.getClasses().get(i).getAttributes().isEmpty())) {
                System.out.println("Atributos: ");
                for (int j = 0; j < classDiagram.getClasses().get(i).getAttributes().size(); j++) {
                    System.out.println(classDiagram.getClasses().get(i).getAttributes().get(j).getName() + " "); 
                }
            } else {
                System.out.println("\n Não possui atributos");
            }
        }
        
    }
    
    @Test
    public void testNumberOfAssociations() throws Exception {
        Parser parser = new Parser();
        File xmiFile = new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/argouml_classdiagram_test.xmi");
        Document document = parser.parse(xmiFile);
        
        ClassDiagram classDiagram = new ClassDiagram(xmiFile, document);
                
        assertEquals(7,classDiagram.getAssociations().size());
        
        for (int i = 0; i < classDiagram.getAssociations().size(); i++) {
            System.out.println("Association "+ i +":" + "Primeiro participante: " + classDiagram.getAssociations().get(i).getFirstParticipant().getName() 
                                                      + "\n Segundo participante: " + classDiagram.getAssociations().get(i).getSecondParticipant().getName());
        }
    }
}
