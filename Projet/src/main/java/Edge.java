public class Edge implements Comparable<Edge> {
    private String vertexOut;
    private String vertexIn;
    private int weight;

    public Edge(String vertexOut, String vertexIn, int weight) {
        this.vertexOut = vertexOut;
        this.vertexIn = vertexIn;
        this.weight = weight;
    }

    public String getVertexOut() {
        return vertexOut;
    }

    public void setVertexOut(String vertex_out) {
        this.vertexOut = vertex_out;
    }

    public String getVertexIn() {
        return vertexIn;
    }

    public void setVertexIn(String vertex_in) {
        this.vertexIn = vertex_in;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int compareTo(Edge e){
        if(this.getVertexOut().compareTo(e.getVertexOut()) == 0){
            if(this.getVertexIn().compareTo(e.getVertexIn()) == 0){
                return this.getWeight() - e.getWeight();
            }
            else{
                return this.getVertexIn().compareTo(e.getVertexIn());
            }
        }
        return this.getVertexOut().compareTo(e.getVertexOut());
    }

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge e = (Edge) o;
        boolean vertexOutEqual = this.getVertexOut().equals(e.getVertexOut());
        boolean vertexInEqual = this.getVertexIn().equals(e.getVertexIn());
        boolean weightEqual = this.getWeight() == e.getWeight();
        boolean res = vertexInEqual && vertexOutEqual && weightEqual;
        return res;
    }

    public String toString(){
        return "["+vertexOut+" - "+weight+" > "+vertexIn+"]";
    }
}
