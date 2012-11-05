/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.parser;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.w3c.dom.Document;

/**
 *
 * @author pedro
 */
public class ParserTest {
    
    static Parser parser;
    Document document;
    
    public ParserTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        parser = new Parser();
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
    public void testValidXmi() {
        document = parser.parse(new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/argouml_protocolstatemachine_test.xmi"));
        assertNotNull(document);
        
        document = parser.parse(new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/argouml_classdiagram_test.xmi"));
        assertNotNull(document);
    }
    
    @Test
    public void testInvalidXmi() {
        document = parser.parse(new File("/home/pedro/Dropbox/Documentos/workspace-eclipse-netbeans/xmi_fail_test.xmi"));
        assertNull(document);
    }
    
    @Test
    public void testInvalidFileLocation() {
        document = parser.parse(new File("incorrect location"));
        assertNull(document);
    }
    
}
