package backEnd;

import org.graphstream.graph.implementations.*;
import org.graphstream.ui.layout.springbox.implementations.LinLog;
import org.graphstream.ui.view.Viewer;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Random;
import java.util.Set;


public class AfficheGraph {
    private org.jgrapht.Graph<String, org.jgrapht.graph.DefaultWeightedEdge> graphe;

    public AfficheGraph(org.jgrapht.Graph g){
        this.graphe = g;
    }

    public void afficheGraphe(){
        org.graphstream.graph.Graph g = JGraphTTOGraphStream(graphe);
        g.addAttribute("ui.stylesheet", "node {size: 4px;size-mode: dyn-size;fill-color: BLUE;text-mode: normal;z-index: 3;}edge {shape: line;fill-color: rgba(39,106,113,50);arrow-size: 1px, 0px; size: 0px;}");

        Viewer v = g.display(false);

        LinLog layout = new LinLog(false,new Random(0));
        v.enableAutoLayout(layout);
    }
    private double getMaxDegree(){
        double max = 0.0;
        for(String s : graphe.vertexSet()){
            if(max < graphe.degreeOf(s))
                max = graphe.degreeOf(s);
        }
        return max;
    }

    public org.graphstream.graph.Graph JGraphTTOGraphStream(org.jgrapht.Graph<String, org.jgrapht.graph.DefaultWeightedEdge> dwGraph){
        org.graphstream.graph.Graph g = new SingleGraph("monGraph");
        g.setStrict(true);

        double maxWeight = getMaxDegree();
        double maxSize = 30;

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
                double pourcentSize = sourceWeight * 100 / maxWeight;
                pourcentSize /= 100;
                g.getNode(source).addAttribute("layout.weight", pourcentSize * maxSize);
                g.getNode(source).addAttribute("ui.size", pourcentSize * maxSize );
            }
            //Ajout du sommet cible s'il n'est pas présent dans le graphe
            if(g.getNode(target) == null) {
                g.addNode(target);
                double targetWeight = dwGraph.degreeOf(target);
                double pourcentSize = targetWeight * 100 / maxWeight;
                pourcentSize /= 100;
                g.getNode(source).addAttribute("layout.weight", pourcentSize * maxSize);
                g.getNode(source).addAttribute("ui.size", pourcentSize * maxSize);
            }
            //Ajout de l'arête entre les deux sommets
            org.graphstream.graph.Edge e = g.addEdge(source+"|"+target,source,target,true);
            //Ajout du poid sur l'arête
            e.setAttribute("weight",weight);
        }
        return g;
    }
}