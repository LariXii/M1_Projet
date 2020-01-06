import java.io.IOException;
import java.io.StringReader;

import org.graphstream.algorithm.Toolkit;
import org.graphstream.algorithm.coloring.WelshPowell;
import org.graphstream.algorithm.community.EpidemicCommunityAlgorithm;
import org.graphstream.algorithm.community.Leung;
import org.graphstream.graph.ElementNotFoundException;
import org.graphstream.graph.Graph;
import org.graphstream.graph.Node;
import org.graphstream.graph.implementations.DefaultGraph;
import org.graphstream.stream.GraphParseException;
import org.graphstream.stream.file.FileSourceDGS;

public class HelloJGraphT {

    public static void main(String[] args) throws IOException,
            ElementNotFoundException, GraphParseException {

        BaseDeTweets bd = new BaseDeTweets();
        try{
            bd.ouvrir("foot.txt");
        }
        catch(IOException ioe){
            System.out.println("Erreur : Open file");
        }
        org.graphstream.graph.Graph graph = Tutorial1.JGraphTTOGraphStream(bd.getDirectedWeightedGraph());

        System.out.println(Toolkit.averageDegree(graph));
        System.out.println(graph.getEdgeCount());
        System.out.println(graph.getNodeCount());
        int i = 0;
        for(org.graphstream.graph.Node n : Toolkit.degreeMap(graph)){
            System.out.println(n.getDegree());
            i++;
            if(i > 10){
                break;
            }
        }
    }
}