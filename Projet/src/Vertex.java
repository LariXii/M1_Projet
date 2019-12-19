import java.util.*;

public class Vertex implements Comparable<Vertex>,Comparator<Vertex>{
    private String name;
    private double score;

    public Vertex(String name) {
        this.name = name;
    }
    public Vertex(String name, double score) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int compareTo(Vertex v){
        return name.compareTo(v.getName());
    }

    public int compare(Vertex v1, Vertex v2) {
        if(v1.getScore() < v2.getScore()){
            return 1;
        }
        else{
            if(v1.getScore() > v2.getScore()){
                return -1;
            }
            else{
                return v1.compareTo(v2);
            }
        }
    }

    public static Comparator<Vertex> getComparator(){
        Comparator<Vertex> comparator = new Comparator<Vertex>() {
            @Override
            public int compare(Vertex v1, Vertex v2) {
                if(v1.getScore() < v2.getScore()){
                    return 1;
                }
                else{
                    if(v1.getScore() > v2.getScore()){
                        return -1;
                    }
                    else{
                        return v1.compareTo(v2);
                    }
                }
            }
        };
        return comparator;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(getClass() != obj.getClass()){
            return false;
        }
        Vertex other = (Vertex)obj;
        if(name.equals(other.getName()))
            return true;
        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public String toString(){
        return "["+name+"]("+score+")";
    }
}
