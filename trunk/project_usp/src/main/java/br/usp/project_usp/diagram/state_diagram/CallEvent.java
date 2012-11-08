/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagram.state_diagram;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pedro
 */
public class CallEvent {
    private String id;
    private String name;
    private List<Parameter> parameters;

    public CallEvent() {
        parameters = new ArrayList<Parameter>();
    }
    
    public CallEvent(String id, String name) {
        this.id = id;
        this.name = name;
        parameters = new ArrayList<Parameter>();
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

    /**
     * @return the parameters
     */
    public List<Parameter> getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(List<Parameter> parameters) {
        this.parameters = parameters;
    }
    
    public void addParameter(Parameter parameter) {
        this.parameters.add(parameter);
    }
    
    @Override
    public String toString() {
        return "Id = " + this.id + "\n" +
               "Name = " + this.name + "\n" + 
               "Parameters = " + Parameter.displayParameters(this.parameters);        
    }
}
