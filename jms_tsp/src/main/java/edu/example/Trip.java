package edu.example;

import java.io.Serializable;

public class Trip implements Serializable  {

    private int trip[];
    private int size;

    public Trip(int maxSize) {
        trip = new int[maxSize];
        size = 0;
    }

    public int getSize() {
        return size;
    }

    public int get(int i) {
        return trip[i];
    }

    public boolean has(int value) {
        for (int i = 0; i < size; i++)
            if (trip[i] == value)
                return true;
        return false;
    }

    public void add(int value) {
        if (size < trip.length)
            trip[size++] = value;
    }

    @Override
    public String toString() {
        String str = "";
        str += "[";
        for (int i = 0; i < size; i++)
            str += trip[i] + ", ";
        if (str.length() > 2)
            str = str.substring(0, str.length() - 2);
        str += "] ";
        return str;
    }

    @Override
    public Object clone() {
        Trip clone = new Trip(trip.length);
        for (int i = 0; i < size; i++)
            clone.add(trip[i]);
        return clone;
    }
}
