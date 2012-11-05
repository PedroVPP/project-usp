/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.usp.project_usp.diagram.class_diagram;

/**
 *
 * @author pedro
 */
public class Association {
    private String id;
    private String name;
    private UmlClass[] pasticipants = new UmlClass[2];

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
     * @return the pasticipants
     */
    public UmlClass[] getPasticipants() {
        return pasticipants;
    }

    /**
     * @param pasticipants the pasticipants to set
     */
    public void setPasticipants(UmlClass[] pasticipants) {
        this.pasticipants = pasticipants;
    }
    
    
    public UmlClass getFirstParticipant() {
        return this.pasticipants[0];
    }
    
    public UmlClass getSecondParticipant() {
        return this.pasticipants[1];
    }
}
