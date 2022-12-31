package com.main;

/**
 * Wrapper class created to solve priority queue issues, it overides the built in pq
 * to ensure that when inserting shortest and prims the priority is not affected
 */

public class actualSort implements Comparable<actualSort>{
    private int index;
    public double priority;

    public actualSort(int index, double priority){
        this.index = index;
        this.priority =priority;
    }
    public int getIndex() {// to get index in queue
        return index;
    }


    @Override
    public int compareTo(actualSort a) { // important line that overides how a regular queue compares to one that works for me
        return Double.compare(this.priority, a.priority);
    }
}
