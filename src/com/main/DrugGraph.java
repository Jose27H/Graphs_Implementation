package com.main;

import java.io.*;
import java.util.*;

/**
 * Jose Henriquez
 * Cosc 2p03 Assignment 4
 * 7088792
 * December, 7 2022
 * This is the DrugGraph class, it utilizes the attributes of graphs to compare distances between Drugs and find modules, the shortest paths and cheapest paths
 * it implements all methods asked from the assignment, and has expected output when called on main class.
 *
 */
public class DrugGraph {

    File file = new File("C:\\Users\\joseh\\OneDrive\\BrockU\\Cosc 2P03\\Cosc2P03_A4_Jose_Henriquez_7088792\\Assignment5\\out\\production\\Assignment5\\com\\main\\dockedApproved.tab");// The file given from the Assignment
    File file1 = new File("C:\\Users\\joseh\\OneDrive\\BrockU\\Cosc 2P03\\Cosc2P03_A4_Jose_Henriquez_7088792\\Assignment5\\out\\production\\Assignment5\\com\\main\\sim_mat.tab");//also the file from assignment
    File out = new File("C:MSTPrimResult.txt");// the MSTPrim file Witten by the program
    FileWriter fw; // to write to the file for MSTPrim

    {
        try {
            fw = new FileWriter(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    PrintWriter pw = new PrintWriter(fw);// to print to new file

    Scanner scan, scan2,scan3;// to scan the file for vertices,to scan file for number of lines
    public Vertex[] vertices;//array to store all the drugs into (asked from instructions)

    String[] substrings = new String[6]; // string array that each line of the .txt file will be broken down into

    String[] subDoubles;// string of doubles that will be used for loading distances array W
    String line;// String that will be broken down to get vertex elements
    int number_lines = 0;// int used to keep track of number of drugs given
    public Double[][] W;// array of the "weights" of dissimilarities
    public int[][] A;// adjacency matrix (0-1 matrix)
    int modules =0;// number of modules, will be used by findModules

    Double inf= Double.POSITIVE_INFINITY; // Infinity variable
    int numVertices =1;//variable for finding vertices in BFS, must be initialized to 1
    int rand = (int)(Math.random() * 1931) + 0; //random int generator, used for finding start in MST

    {
        try {
            scan = new Scanner(file);
            scan2 = new Scanner(file);
            scan3 = new Scanner(file1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructor for the DrugGraph class, this constructor will run everytime the class is instanced
     * it gets important values for the class to work with such as the length of the files and setting array sizing
     */
    public DrugGraph(){
        number_lines = numLines(); //get number of drugs
        vertices= new Vertex[number_lines];//set array
        W = new Double[number_lines][number_lines];//set 2D array with sizing
        A = new int[number_lines][number_lines];//set 2D array with sizing
        subDoubles = new String[number_lines];//sets the array sizing

    }//constructor

    /**
     * This method reads the original file given by assignment and returns the number of lines in the file
     * @return number of lines in file
     */
    private int numLines(){
        scan2.nextLine();
        int i =0;
        while(scan2.hasNextLine()){
            i++;
            scan2.nextLine();
        }
        return i;
    }//numLines

    /**
     * This is the breadth first search method, it gets a Vertex at index i, compares the adjacent elements and continuously
     * marks the visited vertexes to wasVisited and updates the module ID of each Vertex as they are found, method will help to find modules
     * @param i The index at which we will begin the search
     */
    private void BFS(int i) {
       int modID =i;// to set the module id for the Vertex
        numVertices =1;//num of vertices has to be reset every time bfs is called
        Queue<Integer> queue = new LinkedList<>();//queue used to keep track of elements visited
        queue.offer(i);//input starting index into queue
        vertices[i].wasVisited = true;//set the visited value of that drug to true
       vertices[i].moduleID = modID; // set the module ID of that drug so that they are all same
        while (queue.size() != 0) {//while the queue is not empty
            i = queue.poll();//insert index value "i" into queue
            for (int j = 0; j < A[i].length; j++) {// for every drug adjacent to drug at i
                if (A[i][j] == 1 && !vertices[j].wasVisited) {//if the value in the array is 1 and not visited
                    queue.offer(j);// input the index into the queue
                    vertices[j].wasVisited = true; //mark that drug to visited
                   vertices[j].moduleID = modID;//set the module id of that drug
                    numVertices++;//keep count of number of vertices(drugs connected to them)
                }
            }
        }
    }//BFS

    /**
     * findmodules method, traverses entire array and groups all connected vertices into a module
     * finds number of connected vertices and then displays them in order of most to least number of
     * vertices
     */
    public void findModules(){
        int[][] modArray = new int[number_lines][2];//new array that holds the index to the drugs and number of vertices
        for(int i = 0; i< A.length; i++){ //for every drug in vertices
            if(!vertices[i].wasVisited){//if that drug has not been visited
                BFS(i);//perform bfs of that index
                vertices[i].moduleID = modules;// set the module ID
                modArray[modules][0] = i; //save the strting index of module
                modArray[modules][1] = numVertices;// set array value to number of vertices
                modules++;//increase module
            }
        }
        quickSort(modArray, 0, modules);// sort the array based on number of vertices

        for(int q =0; q< modules; q++ ) {
            System.out.println("Module:" + q +" = " +vertices[modArray[q][0]].getGenericName() + " with: " + modArray[q][1] + " Connected Vertices"); //print the list of modules
        }
        for(int q =0; q< number_lines; q++){//reset all wasVisited values
        vertices[q].wasVisited = false;
        }
    }//findModules


    /**
     * Keep a module method, this goes thru the entire array and removes all
     * the modules that do not match the moduleID
     * @param moduleID the moduleID to compare against
     */
    public void keepAModule(int moduleID){
        for(int i =0; i< number_lines; i++){
            if (vertices[i].moduleID != moduleID){
                vertices[i] =null;
            }
            else {
                vertices[i] = vertices[i];
            }
        }
    }//keepAModule

    /**
     * findShortest path will call either weighted or unweighted algorythims to find shortest path
     * @param fromDrug: Starting drug
     * @param toDrug: Ending Drug
     * @param method: which method
     */
    public void findShortestPath(String fromDrug,String toDrug, String method ) {


        int start = findDrug(fromDrug);// find index of start dug
        int finish = findDrug(toDrug);//find index of end drug
        if(method.equals("unweighted")){//do unweighted
            unweightedShortest(start,finish);

        }
        if(method.equals("weighted")){//do weighted
            weightedShortest(start,finish);
        }

    }//findShortestPath

    /**
     * Method for finding unweighted shortest path using algorithm from class
     * @param start: start drug index
     * @param end: end drug index
     */
    private void unweightedShortest(int start, int end){
        Queue<Integer> queue = new LinkedList<>(); //queue to insert traversed list
        int index=0;// setting index to start searching
        vertices[start].dist = 0.0;// the distance of starting node is zero
        queue.offer(start);// insert index of starting node
        int temp = end; //temporary index to index of end
        String traversed ="";// string that will be filled with traversed values

        while (queue.size() != 0) {// while the queue is not empty
            index = queue.poll();//remove index at start of queue
        //basically bfs to track path a drug can take to trace another drug
            for (int j = 0; j < A.length; j++) {// for every drug adjacent
                if (A[index][j] == 1 && !vertices[j].wasVisited && vertices[j] != null) { // if the value in A is 1 and was not visited
                    vertices[j].wasVisited = true;//set value to visited
                    vertices[j].path = index;// keep track of last visited node
                    vertices[j].dist++;//increase distance
                    queue.offer(j);//insert the index value into the queue so that it can be retrieved when back-tracking

                }
            }
        }

        //printing the path
        while(vertices[temp] != null && !vertices[temp].getDrugBankID().equals(vertices[start].getDrugBankID())){//if the ending is not null and is not start
            if(vertices[temp].getDrugBankID().equals(vertices[end].getDrugBankID())){//add the very last (end drug)
                traversed = vertices[end].getDrugBankID();
            }
            else {//Adding middle path drugs
                traversed = vertices[temp].getDrugBankID() +" "+ traversed;// insert the drug id from the drug into traversed
            }

            temp= vertices[temp].path;// set the temp value to the value of the path (the index value of the previously visited Drug)
        }
        if(vertices[temp].getDrugBankID() == vertices[start].getDrugBankID()){//for the start drug
            traversed = vertices[start].getDrugBankID() + " " + traversed;
        }
        System.out.println(traversed);// print the string made from the traversal
        resetWasVisited();// reset the wasVisited values and paths and dist so that it does not break other methods

    }//unweightedShortest

    /**
     * weighted shortest path, this is a method that used Dikstras algorithm from the slides
     * to find the shortest path while conmparing values from matrix "W"
     * @param start: index of starting Drug
     * @param end: index of end drug
     */
    private void weightedShortest(int start, int end){
        Queue<actualSort> pq = new PriorityQueue<actualSort>();//priority queue of items visited(Actual sort was a class made because of profesor Email

        double pathLength =0;//initial index of paths
        vertices[start].dist = 0.0;//initial distance is zero
        int temp = end;// record of end
        int index=0;//index at start is zero
        String traversed ="";// initiating string to be built
         actualSort item =new actualSort(start, vertices[start].dist);//inserting item into modified priority queue
        pq.offer(item);// insert into queue

        while (pq.size() != 0){// while the queue is not empty
            index = pq.poll().getIndex();

            if(!vertices[start].wasVisited) {//if the drug was not visited yet
                    for (int j = 0; j < A.length; j++) {//for every adjacent drug
                        if(!Objects.equals(W[index][j], inf) && vertices[start] != null && !vertices[j].wasVisited ){//if not visited and if not infinity
                            if(vertices[j].dist > vertices[index].dist +W[index][j]){//if the value in vertices is greater than the sum of the value of the drug and corresponding value in W (you found shorter path)
                                vertices[j].dist = vertices[index].dist+W[index][j];// set the distance of the drug to the smaller distance
                                vertices[j].path = index;// path value is now last visited drug
                                pq.offer(new actualSort(j, vertices[j].dist));// insert index and distance into pq
                            }
                }
            }
            }
        }

        //retracing the path
        while(vertices[temp] != null && !vertices[temp].getDrugBankID().equals(vertices[start].getDrugBankID())){//starting at end drug
            if(vertices[temp].getDrugBankID().equals(vertices[end].getDrugBankID())){
                traversed = vertices[end].getDrugBankID();// add end to string

            }
            else {
                traversed = vertices[temp].getDrugBankID() +" "+ traversed;//add middle items to string
            }

            temp= vertices[temp].path;// reset index to the last visited drug
        }
        if(vertices[temp].getDrugBankID() == vertices[start].getDrugBankID()){// at start
            traversed = vertices[start].getDrugBankID() + " " + traversed;
        }
        System.out.println(traversed);//print the path taken

        resetWasVisited();//reset the wasVisited values
    }

    /**
     * MST prim method, it finds the cheapest way to traverse a module, given a valid starting index
     * This method randomly selects starting index so my result will vary every time code is run.
     * I am aware that zero works but randomizing is more fun
     */
    public void MSTPrim(){

        Queue<actualSort> pq = new PriorityQueue<actualSort>();// using modified new priority queue
        int randomStart =rand;//random starting value
        int MSTIndex =0;
        while (vertices[randomStart] == null){ // if the value is null get another value
            randomStart = rand;
        }

        vertices[randomStart].dist = 0.0;// initial distance is zero
        pq.offer(new actualSort(randomStart, vertices[randomStart].dist));//insert start into queue

        while (pq.size() != 0){ //while the queue is not empty
            MSTIndex = pq.poll().getIndex();//remove index of front of queue
            if(!vertices[MSTIndex].wasVisited){// if it was not visited
                vertices[MSTIndex].wasVisited = true;// mark as visited
                for (int j = 0; j < A.length; j++) {// for all adjacent drugs
                    if(W[MSTIndex][j] != inf && vertices[j] != null ){// if a path exists
                        if(vertices[j].dist > W[MSTIndex][j]  && !vertices[j].wasVisited){//if the path is shorter
                            vertices[j].dist = W[MSTIndex][j];// set distance to new shorter distance
                            vertices[j].path =  MSTIndex;//set the path to last visited
                            pq.offer(new actualSort(j,vertices[j].dist));//inset index into the queue
                        }
                    }
                }
            }
        }
        double total=0.0;//for calculating total
        for(int p=0; p<number_lines;p++){//for all remaining items
            if(vertices[p] != null){
                pw.println(vertices[p].getDrugBankID()+ " " +  vertices[p].dist);//print the drugIDs of remaining drugs
                total = total+vertices[p].dist; //add up total distance
            }
        }
        pw.close();// close writer

        System.out.println(total);// display calculated total

        resetWasVisited();// reset wasVisited values
    }//MSTPrim

    /**
     * This method traverses entire array and resets wasVisited, dist and path
     */
    private void resetWasVisited(){
        for(int i=0; i<number_lines; i++){
            if (vertices[i] != null ){
                vertices[i].wasVisited = false;
                vertices[i].dist= inf;
                vertices[i].path = -1;
            }
        }
    }//resetWasVisited

    /**
     * Swap method, used for swapping 2 elements inside a 2d array(will be used for organizing modules)
     * @param arr: array to be sorted
     * @param a :value to switch from (index)
     * @param b:value to switch(index)
     */
    private static void swap(int[][] arr, int a, int b){
        int tempVertices = arr[a][1]; //save value inside array
        int tempIndex = arr[a][0];//save second value inside array
        arr[a][1] = arr[b][1];//swap the values from b to a
        arr[a][0] = arr[b][0];//swap the values from b to a
        arr[b][1]= tempVertices;// swap the temp values into b
        arr[b][0]= tempIndex;//swap the temp values into b
    }//swap

    /**
     * partition method that helps quicksort
     * will find a pivot point and then move all values higher than it to right and keep
     * lesser ones to the left. will be used to parrtition off array for quicksort to sort
     * @param arr array to sort
     * @param low lowest point in parttion
     * @param high highest point in the partition
     * @return the index at which the partition split
     */
    static int partition(int[][] arr, int low, int high){
        int pivot = arr[high][1];// pivot is highest element
        int i = low-1;// index value is one less than low
        //for every value in the array
        for(int j = low; j< high; j++){
            if(arr[j][1] > pivot){//if the value in the array is greater than pivot
                i++;//increase index so its same as low
                swap(arr, i, j);// swap value to the right
            }
        }
        i++;//increment index
        swap(arr, i, high); //swap on the index and highest value to restore pivot into list
        return i;// return the index to partition again
    }//partition

    /**
     * Quicksort algo, will parttiton values lesser than pivot to left and greater to right.
     * will be called recursively on smaller and smaller partitions until fully sorted
     * @param arr: array to sort
     * @param low: lowest point in partition
     * @param high:highest point in parttion
     */
    static void quickSort(int[][] arr, int low, int high) {
        if (low <= high) {
            // find pivot element
            // elements smaller than pivot are on the left and elements greater than pivot are on the right
            int pivot = partition(arr, low, high);
            // recursive call on the left of pivot
            quickSort(arr, low, pivot - 1);
            // recursive call on the right of pivot
            quickSort(arr, pivot + 1, high);
        }
    }//quickSort

    /**
     * method that traverses vertex array and finds the index of a drug given its drugID
     * @param drugID
     * @return
     */
    private int findDrug(String drugID){
        int index =0;
        for(int i=0; i< number_lines; i++){
            if(vertices[i] ==null ||vertices[i].getDrugBankID().compareTo(drugID) !=0){
                index++;
            } else if (vertices[i].getDrugBankID().equals(drugID)){
             return index;
            }
        }
      return index;
    }//findDrug


    /**
     * ReadData Method Specified by assignment, it reads the file and loads it into an array of Drug(vertex) instances
     * @param Vertices, sims, a, the vertices that will be loaded from the values of the arrays(A and W)
     *                  given from the .tab files from assignment
     */

    public void readData(Vertex[] Vertices, Double[][] sims, int[][] a  ){

        scan.nextLine();//ommit first line of drugs file
        int i =0 ; //index for loading the vertices array

        while(scan.hasNextLine()) {
            line = scan.nextLine();
            substrings = line.split("\t"); //sets substrings to an array of the different parts of a line from the file
            Vertex vertex = new Vertex(substrings); // creating an instance of vertex
            Vertices[i] = vertex; //setting that index of the array, to that vertex instance
            i++;

        }
        i =0;// reset index for loading array "W"

        //loading array "W"
        while (scan3.hasNextLine()){
            line = scan3.nextLine();//set line to the scanned section
            subDoubles = (line.split("\t")); //sets substrings to an array of the different parts of a line from the file
            for(int j =0; j < number_lines; j++) {
                if(j != i && ((1.00 - Double.valueOf(subDoubles[j])) <= 0.7)) {// if 1 - the value in the array is less than or equal 0.7
                    sims[i][j] = (1.00 - Double.valueOf(subDoubles[j]));// set value of array to difference of 1 and value and array
                }
                else {
                    sims[i][j] = inf;//otherwise set value to infinity
            }

        }i++;
        }

        //loading array "A"
        for (int q = 0; q < number_lines; q++){
            for (int r = 0; r<number_lines; r++){
                if(sims[q][r] == inf ){//if value in "w" is infinity
                    a[q][r] = 0;//Value in A is 0
                }else {//otherwise
                    a[q][r] = 1;//value in A is 1
                }

            }
        }

    }//readData
}
