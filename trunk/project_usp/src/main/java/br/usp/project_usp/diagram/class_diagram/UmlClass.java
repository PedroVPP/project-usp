/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagram.class_diagram;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author pedro
 */
public class UmlClass {
    
    private String id;
    private String name;
    private List<Attribute> attributes;

    public UmlClass() {
        attributes = new ArrayList<Attribute>();
    }
    
    public UmlClass(String id, String name) {
        this.id = id;
        this.name = name;
        attributes = new ArrayList<Attribute>();
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
     * @return the attributes
     */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /**
     * @param attributes the attributes to set
     */
    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }
    
    public void addAttribute(Attribute attribute) {
        this.attributes.add(attribute);
    }
    
    public boolean hasAttributes() {
        boolean result = false;
        
        if(this.attributes.size() > 0) {
            result = true;
        }
        
        return result;
    }
}
