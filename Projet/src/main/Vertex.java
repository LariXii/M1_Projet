package main;

import java.util.*;

public class Vertex {
    private String name;
    private HashMap<String,Integer> lSuccessors;
    private HashMap<String,Integer> lPredecessors;

    public Vertex(String name) {
        this.name = name;
        this.lPredecessors = new HashMap<>();
        this.lSuccessors = new HashMap<>();
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Integer> getlSuccessors() {
        return this.lSuccessors;
    }

    public HashMap<String, Integer> getlPredecessors() {
        return this.lPredecessors;
    }

    public Set<Edge> getlEdges(){
        Set<Edge> lEdges = new TreeSet<>();
        Set entrySet = lSuccessors.entrySet();
        Iterator it = entrySet.iterator();

        while(it.hasNext()){
            Map.Entry me = (Map.Entry)it.next();
            int weight = (int)me.getValue();
            String successorName = (String)me.getKey();
            lEdges.add(new Edge(this.name,successorName,weight));
        }
        return lEdges;
    }

    public String toString(){
        return name;
    }
}
