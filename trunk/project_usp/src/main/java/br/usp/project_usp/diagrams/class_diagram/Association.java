package br.usp.project_usp.diagrams.class_diagram;

/**
 *
 * @author pedro
 */
public class Association {
    private String id;
    private String name;
    private UmlClass[] pasticipants = new UmlClass[2];
    //association ends?

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
    
    public void addParticipant(UmlClass pasticipant) throws Exception {
        if(this.pasticipants[0] == null) {
            this.pasticipants[0] = pasticipant;
        } else if(this.pasticipants[1] == null) {
            this.pasticipants[1] = pasticipant;
        } else {
            throw new Exception("Array of participants is already full");
        }   
    }
    
    @Override
    public String toString() {
        return "Id = " + this.id + "\n" +
               "Name = " + this.name + "\n" +
               "Participant1" + this.pasticipants[0] + "\n" +
               "Participant2" + this.pasticipants[1];
    }
}
