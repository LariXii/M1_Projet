public class Edge implements Comparable<Edge> {
    private String vertexSource;
    private String vertexTarget;
    private int weight;

    public Edge(String vertexSource, String vertexTarget, int weight) {
        this.vertexSource = vertexSource;
        this.vertexTarget = vertexTarget;
        this.weight = weight;
    }

    public String getVertexSource() {
        return vertexSource;
    }

    public void setVertexSource(String vertex_out) {
        this.vertexSource = vertex_out;
    }

    public String getVertexTarget() {
        return vertexTarget;
    }

    public void setVertexTarget(String vertex_in) {
        this.vertexTarget = vertex_in;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int compareTo(Edge e){
        if(this.getVertexSource().compareTo(e.getVertexSource()) == 0){
            if(this.getVertexTarget().compareTo(e.getVertexTarget()) == 0){
                return this.getWeight() - e.getWeight();
            }
            else{
                return this.getVertexTarget().compareTo(e.getVertexTarget());
            }
        }
        return this.getVertexSource().compareTo(e.getVertexSource());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge e = (Edge) o;
        boolean vertexOutEqual = this.getVertexSource().equals(e.getVertexSource());
        boolean vertexInEqual = this.getVertexTarget().equals(e.getVertexTarget());
        boolean weightEqual = this.getWeight() == e.getWeight();
        boolean res = vertexInEqual && vertexOutEqual && weightEqual;
        return res;
    }

    public String toString(){
        return "["+ vertexSource +" - "+weight+" > "+ vertexTarget +"]";
    }
}
