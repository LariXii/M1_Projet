import org.graphstream.algorithm.generator.Generator;
import org.graphstream.algorithm.generator.GridGenerator;
import org.graphstream.algorithm.generator.RandomGenerator;
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.*;
import org.graphstream.ui.layout.Layout;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.layout.springbox.implementations.SpringBox;
import org.graphstream.ui.view.Viewer;
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
        JGraphTTOGraphStream(bd.getSubGraph(),bd.getMaxDegree(bd.getSubGraph()));
    }

    private static void JGraphTTOGraphStream(org.jgrapht.Graph<String, org.jgrapht.graph.DefaultWeightedEdge> dwGraph, double maxWeight){
        org.graphstream.graph.Graph g = new SingleGraph("Foot");
        g.setStrict(true);

        g.addAttribute("ui.antialias");
        g.addAttribute("ui.quality");
        g.addAttribute("ui.stylesheet", "node {size: 4px;fill-color: BLUE;text-mode: hidden;z-index: 3;}edge {shape: line;fill-color: #222;arrow-size: 3px, 2px; size: 0px;}");

        double taille_max = 50;
        //Ajout des arêtes
        Set<DefaultWeightedEdge> edges = dwGraph.edgeSet();
        for(DefaultWeightedEdge dwe : edges){
            double weight = dwGraph.getEdgeWeight(dwe);
            String source = dwGraph.getEdgeSource(dwe);
            String target = dwGraph.getEdgeTarget(dwe);
            //Ajout du sommet source s'il n'est pas présent dans le graphe
            if (g.getNode(source) == null) {
                g.addNode(source);
                double sourceWeight = dwGraph.degreeOf(source);
                g.getNode(source).addAttribute("layout.weight", sourceWeight);
                //g.getNode(source).addAttribute("ui.size", (sourceWeight*taille_max)/maxWeight);
                //System.out.println("Degré du sommet "+source+" est de : "+sourceWeight+" pixels");
            }
            //Ajout du sommet cible s'il n'est pas présent dans le graphe
            if(g.getNode(target) == null) {
                g.addNode(target);
                double targetWeight = dwGraph.degreeOf(target);
                g.getNode(source).addAttribute("layout.weight", targetWeight);
                //g.getNode(target).addAttribute("ui.size", (targetWeight*taille_max)/maxWeight);
                //System.out.println("Degré du sommet "+target+" est de : "+targetWeight+" pixels");
            }
            //Ajout de l'arête entre les deux sommets
            org.graphstream.graph.Edge e = g.addEdge(source+"|"+target,source,target,true);
            //Ajout du poid sur l'arête
            e.setAttribute("weight",weight);

        }
        Layout layout = new LinLog();
        //g.addSink(layout);
        //layout.addAttributeSink(g);
        Viewer v = g.display(true);
        v.enableAutoLayout(layout);
        layout.setForce(200);
        layout.setQuality(0);
        while(layout.getStabilization() < 0.9){
            layout.compute();
        }
        System.out.println(layout.getStabilization());
    }

    public static org.graphstream.graph.Graph JGraphTTOGraphStream(org.jgrapht.Graph<String, org.jgrapht.graph.DefaultWeightedEdge> dwGraph){
        org.graphstream.graph.Graph g = new SingleGraph("Foot");
        g.setStrict(true);

        //Ajout des arêtes
        Set<DefaultWeightedEdge> edges = dwGraph.edgeSet();
        for(DefaultWeightedEdge dwe : edges){
            double weight = dwGraph.getEdgeWeight(dwe);
            String source = dwGraph.getEdgeSource(dwe);
            String target = dwGraph.getEdgeTarget(dwe);
            //Ajout du sommet source s'il n'est pas présent dans le graphe
            if (g.getNode(source) == null) {
                g.addNode(source);
            }
            //Ajout du sommet cible s'il n'est pas présent dans le graphe
            if(g.getNode(target) == null) {
                g.addNode(target);
            }
            //Ajout de l'arête entre les deux sommets
            org.graphstream.graph.Edge e = g.addEdge(source+"|"+target,source,target,true);
            //Ajout du poid sur l'arête
            e.setAttribute("weight",weight);
        }

        //Ajout des sommets
        Set<String> vertices = dwGraph.vertexSet();
        for(String v : vertices){
            if (g.getNode(v) == null) {
                g.addNode(v);
            }
        }
        return g;
    }
}