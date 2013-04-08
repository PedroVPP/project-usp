/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagrams.state_diagram;

import br.usp.project_usp.tests_construction.TestConstructor;
import br.usp.project_usp.tests_construction.VariablesHolder;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author pedro
 */
public class Invariant {

    private String id;
    private String name;
    private InvariantCompound[] invariantExpressions;
    public Invariant() {
    }

    public Invariant(String id, String name) {
        this.id = id;
        this.name = name;
        
        invariantExpressions = new InvariantCompound[this.getName().trim().split("&&").length];
        for (int i = 0; i < invariantExpressions.length; i++) {
            invariantExpressions[i] = new InvariantCompound(this.getName().trim().split("&&")[i]);
        }
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
        invariantExpressions = new InvariantCompound[this.getName().trim().split("&&").length];
        for (int i = 0; i < invariantExpressions.length; i++) {
            invariantExpressions[i] = new InvariantCompound(this.getName().trim().split("&&")[i]);
        }
        
    }

    @Override
    public String toString() {
        String result = "";
        result += "Invariant name: " + this.name;
//        result += this.name;
        return result;
    }

    public boolean isSatisfied() {
        return true;
    }

    public void writeEventCode(TestConstructor testConstructor) throws IOException {

        for (int i = 0; i < this.invariantExpressions.length; i++) {
            InvariantCompound invariantCompound = invariantExpressions[i];

            List<String> matchList = new ArrayList<String>();
            Pattern regex = Pattern.compile("\\{([^}]*)\\}"); //http://stackoverflow.com/questions/3684900/how-do-i-extract-words-in-braces-using-regular-expressions
            Matcher regexMatcher = regex.matcher(invariantCompound.uri);
            while (regexMatcher.find()) {
                matchList.add(regexMatcher.group());
            }
            for (Iterator<String> it = matchList.iterator(); it.hasNext();) {
                String variable = it.next();
                if(VariablesHolder.constainsVariable(variable)) {
                    invariantCompound.uri = invariantCompound.uri.replaceFirst(regex.toString(), VariablesHolder.getVariableValueByName(variable));
                } else {
                    VariablesHolder.addVariable(variable, JOptionPane.showInputDialog(null, "Variable \"" + variable + "\" found. Please insert its value."));
//                    this.getParameters().get(0).setName("http://localhost:8080/" + modifiedUri);
                    invariantCompound.uri = invariantCompound.uri.replaceFirst(regex.toString(), VariablesHolder.getVariableValueByName(variable));
                }
            }

            
            
            if(invariantCompound.methodName.equals("OK")) {
                
                testConstructor.addLine(
                "HttpClient httpClient = new DefaultHttpClient();\n"
                + "HttpGet httpget = new HttpGet(\""+ "http://localhost:8080/"+invariantCompound.uri +"\");\n"
                + "HttpResponse response = httpClient.execute(httpget);\n"
                + "assertTrue(response.getStatusLine(), 200);\n");
                
            } else if (invariantCompound.methodName.equals("NF")) {
                        testConstructor.addLine(
                "HttpClient httpClient = new DefaultHttpClient();\n"
                + "HttpGet httpget = new HttpGet(\""+ "http://localhost:8080/"+invariantCompound.uri +"\");\n"
                + "HttpResponse response = httpClient.execute(httpget);\n"
                + "assertTrue(response.getStatusLine(), 404);\n");
            }
            
            
        }




//        
//        //OK()
//        testConstructor.addLine(
//                "HttpClient httpClient = new DefaultHttpClient();\n"
//                + "HttpGet httpget = new HttpGet(\"\");\n"
//                + "HttpResponse response = httpClient.execute(httpget);\n"
//                + "assertTrue(response.getStatusLine(), 200);\n");
//
//        //NOT_FOUND()
//        testConstructor.addLine(
//                "HttpClient httpClient = new DefaultHttpClient();\n"
//                + "HttpGet httpget = new HttpGet(\"\");\n"
//                + "HttpResponse response = httpClient.execute(httpget);\n"
//                + "assertTrue(response.getStatusLine(), 404);\n");
    }

    private static class InvariantCompound {

        String methodName;
        String uri;
        
        private InvariantCompound(String invariantExpression) {
            this.methodName = invariantExpression.substring(0, 2);
            this.uri = invariantExpression.substring(3, invariantExpression.length()-1);
        }
        
        @Override
        public String toString() {
            return "Method name: " + methodName + "\n" + "URI: " + uri;
        }
    }
}
