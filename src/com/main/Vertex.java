package com.main;


/**
 *
 * Jose Henriquez
 * Cosc 2p03 Assignment 4
 * 7088792
 * December, 8 2022
 *
 * This is the Vertex class, it describes all the attributes a Drug should have, and it is used by drugHeap to load the array
 * vertices. it is used to get and set information useful in DrugGraph
 */

public class Vertex {
    String genericName, Smiles, drugBankID, url, drugGroup, score;// Attributrs of a drug(vertex)
    Double inf= Double.POSITIVE_INFINITY; // Infinity variable
    public Double dist; //distance variable
    public boolean wasVisited; //boolean that sets the visited node as traversed
    public int moduleID =-1;// module id variable, used for determining which vertex belongs to which module
     public int path =-1; //variable used for finding path used for traversal

    /**
     * Constructor of Drug Class, it creates a drug taken a string array and splitting it into its separate components,
     * it also
     * @param names string array that's broken down
     */



    public Vertex(String[] names) {
        genericName = names[0];
        Smiles = names[1];
        drugBankID = names[2];
        url = names[3];
        drugGroup = names[4];
        score = names[5];
        path =-1;
        dist = inf;
        wasVisited = false;
        moduleID = -1;
    }

    /**
     * Next few methods are getters, they return the different attributes of the Drug(Vertex)
     * @return the string associated with that Drug attribute
     */
    public String getGenericName() {
        return genericName.trim();
    }

    public String getSmiles() {
        return Smiles.trim();
    }

    public String getDrugBankID() {
        return drugBankID.trim();
    }

    public String getUrl() {
        return url.trim();
    }

    public String getDrugGroup() {
        return drugGroup.trim();
    }

    public String getScore() {
        return score.trim();
    }

    /**
     * This method is used to print to the file, it takes all the parts of a Drug(Vertex) and puts them together into one string that can be used
     * @return a string with the full drug(vertex) info
     */

    public String  displayDrug(){
        return (this.getGenericName() + " " + this.getSmiles() + " " + this.getDrugBankID() + " " + this.getUrl()+ " " + this.getDrugGroup() + " " + this.getScore() + " "+ this.dist +" "+ this.path + " " + this.wasVisited );
    }//displayDrug
}

