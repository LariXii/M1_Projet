import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.Graph;

import java.io.IOException;
import java.util.Set;


public class Tutorial1 {
    public static void main(String args[]) {
        /*Graph graph = new SingleGraph("Tutorial 1");

        graph.addNode("A");
        graph.addNode("B");
        graph.addNode("C");
        graph.addEdge("AB", "A", "B");
        graph.addEdge("BC", "B", "C");
        graph.addEdge("CA", "C", "A");

        graph.display();*/


        /*org.graphstream.graph.Graph g = new SingleGraph("Random");
        Generator gen = new RandomGenerator(2);
        gen.addSink(g);
        gen.begin();
        for(int i=0; i<100; i++)
            gen.nextEvents();
        gen.end();
        g.addAttribute("ui.antialias");
        g.addAttribute("ui.quality");
        g.addAttribute("ui.stylesheet", "node { fill-color: red; }");
        g.display();*/
        BaseDeTweets bd = new BaseDeTweets();
        try{
            bd.ouvrir("foot.txt");
        }
        catch(IOException ioe){
            System.out.println("Erreur : Open file");
        }
        JGraphTTOGraphStream(bd.getSubGraph());
    }

    private static void JGraphTTOGraphStream(org.jgrapht.Graph<String, org.jgrapht.graph.DefaultWeightedEdge> dwGraph){
        org.graphstream.graph.Graph g = new SingleGraph("Foot");
        g.setStrict(true);

        //Ajout des arêtes
        Set<DefaultWeightedEdge> edges = dwGraph.edgeSet();
        for(DefaultWeightedEdge dwe : edges){
            double weight = dwGraph.getEdgeWeight(dwe);
            String source = dwGraph.getEdgeSource(dwe);
            String target = dwGraph.getEdgeTarget(dwe);
            //Ajout du sommet source s'il n'est pas présent dans le graphe
            if (g.getNode(source) == null)
                g.addNode(source);
            //Ajout du sommet cible s'il n'est pas présent dans le graphe
            if(g.getNode(target) == null)
                g.addNode(target);

            //Ajout de l'arête entre les deux sommets
            org.graphstream.graph.Edge e = g.addEdge(source+"|"+target,source,target,true);
            //Ajout du poid sur l'arête
            e.setAttribute("weight",weight);
        }

        g.addAttribute("ui.antialias");
        g.addAttribute("ui.quality");
        g.addAttribute("ui.stylesheet", "node { fill-color: cyan; }");
        g.display(true);
    }
}