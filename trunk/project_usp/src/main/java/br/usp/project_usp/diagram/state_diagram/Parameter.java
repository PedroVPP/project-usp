/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagram.state_diagram;

import java.util.Iterator;
import java.util.List;

/**
 *
 * @author pedro
 */
public class Parameter {
    private String id;
    private String name;
    
    public Parameter() {
        
    }
    
    public Parameter(String id, String name) {
        this.id = id;
        this.name = name;
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
    }
    
    @Override
    public String toString() {
        return "Id = " + this.id + "\n" +
               "Name = " + this.name;
    }
    
    public static String displayParameters(List<Parameter> parametersList) {
        String result = "";
        for (Iterator<Parameter> it = parametersList.iterator(); it.hasNext();) {
            Parameter parameter = it.next();
            result += "Parameter = " + parameter.toString();
        }
        return result;
    }
}
