package com.main;

/**
 * Main class, calls the methods as asked from assignment, makes instances of other classes if needed
 *
 * Jose Henriquez
 * 7088792
 * Cosc 2p03 Assignment 4
 * December, 8 2022
 */

public class Main {
    //constructor used for calling methods

    public Main(){
        DrugGraph dg = new DrugGraph();
        dg.readData(dg.vertices,dg.W, dg.A);//load array
        dg.findModules();
        dg.keepAModule(0);
        dg.findShortestPath("DB01050", "DB00316","unweighted");
        dg.findShortestPath("DB01050", "DB00316","weighted");
        dg.MSTPrim();



    }//constructor

    public static void main(String[] args) {
        Main m = new Main();
    }
}