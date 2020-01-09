package backEnd;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.util.*;
import java.util.stream.Collectors;

public class GraphTweet{
    private Graph<String,DefaultWeightedEdge> directedWeightedGraph;

    private double densite;
    private double degreMoyen;
    private double degreEntrantMoyen;
    private double degreSortantMoyen;
    private double diametre;
    private int ordre;
    private int volume;
    /***************************************
     *          GETTER ET SETTER           *
     ****************************************/

    public double getDensite() {
        return densite;
    }

    public double getDegreMoyen() {
        return degreMoyen;
    }

    public double getDegreEntrantMoyen() {
        return degreEntrantMoyen;
    }

    public double getDegreSortantMoyen() {
        return degreSortantMoyen;
    }

    public double getDiametre() {
        return diametre;
    }

    public int getOrdre() {
        return ordre;
    }

    public int getVolume() {
        return volume;
    }

    public GraphTweet(){
        directedWeightedGraph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    }

    public Graph<String,DefaultWeightedEdge> getDirectedWeightedGraph(){
        return directedWeightedGraph;
    }
    public void setDirectedWeightedGraph(Graph g){
        directedWeightedGraph = g;
    }

    public Graph<String,DefaultWeightedEdge> getSubGraph(int n){
        Set<String> vertices = directedWeightedGraph.vertexSet();
        do {
            int finalN = n;
            vertices = directedWeightedGraph
                    .vertexSet().stream().filter(el -> directedWeightedGraph.degreeOf(el) > finalN).collect(Collectors.toSet());
            n += 10;
        }
        while(vertices.size() > 1500);

        Graph<String,DefaultWeightedEdge> subgraph = new AsSubgraph<>(directedWeightedGraph,vertices);
        return subgraph;
    }


    /*****************************************************
     *          UTILISATEURS LES PLUS CENTRAUX           *
     *****************************************************/

    public TreeSet<CentralUser> getDegreeCentrality(int n){
        TreeSet<CentralUser> usersCentrals = new TreeSet<>();
        //Le nombre moyen d'arêtes qui partent et débutent à partir d'un noeud.
        for(String s : directedWeightedGraph.vertexSet()){
            double poids = directedWeightedGraph.degreeOf(s);
            CentralUser user = new CentralUser(s,poids);
            usersCentrals.add(user);
            if(usersCentrals.size() > n){
                usersCentrals.pollLast();
            }
        }
        return usersCentrals;
    }
    public TreeSet<CentralUser> getPageRank(int n){
        TreeSet<CentralUser> usersCentrals = new TreeSet<>();
        PageRank<String,DefaultWeightedEdge> pg = new PageRank<>(this.directedWeightedGraph);
        Map<String,Double> scores = pg.getScores();

        for(Map.Entry me : scores.entrySet()){
            String name = (String)me.getKey();
            double score = (Double)me.getValue();
            CentralUser user = new CentralUser(name,score);
            usersCentrals.add(user);
            //On supprime la plus petite valeur du TreeSet
            if(usersCentrals.size() > n){
                usersCentrals.pollLast();
            }
        }
        return usersCentrals;
    }

    /*********************************************************************
     *          FONCTIONS DE CALCULS DES STATISTIQUE DU GRAPHE           *
     *********************************************************************/

    public void calculDiametre(){
        long time = System.currentTimeMillis();
        double diametre = Double.NEGATIVE_INFINITY;
        int i = 1;
        if(isConnected()){
            //Parcourt de l'ensemble des sommets du graphe
            for(String source: this.directedWeightedGraph.vertexSet()){
                System.out.println("["+source+"]#"+i);
                DijkstraShortestPath<String, DefaultWeightedEdge> dijkstraAlg = new DijkstraShortestPath<>(this.directedWeightedGraph);
                //Création d'un chemin à partir de la source
                ShortestPathAlgorithm.SingleSourcePaths<String, DefaultWeightedEdge> sourcePaths = dijkstraAlg.getPaths(source);
                //Pour chaque sommet on parcourt chacun des sommets et on récupère le plus long court chemin
                for(String target: this.directedWeightedGraph.vertexSet()) {
                    diametre = Math.max(diametre,sourcePaths.getWeight(target));
                }
                if(diametre == Double.NEGATIVE_INFINITY){
                    break;
                }
                i++;
            }
        }
        this.diametre = diametre;
    }
    public boolean isConnected(){
        ConnectivityInspector<String,DefaultWeightedEdge> cIGraph = new ConnectivityInspector<>(this.directedWeightedGraph);
        boolean connected = cIGraph.isConnected();
        return connected;
    }
    public void calculMeanDegree(){
        long total = 0;
        //Le nombre moyen d'arêtes qui partent et débutent à partir d'un noeud.
        for(String s : directedWeightedGraph.vertexSet()){
            total += directedWeightedGraph.degreeOf(s);
        }
        degreMoyen = total / (float)this.ordre;
    }
    public void calculMeanDegreeIn(){
        long total = 0;
        //Le nombre moyen d'arêtes qui partent et débutent à partir d'un noeud.
        for(String s : directedWeightedGraph.vertexSet()){
            total += directedWeightedGraph.inDegreeOf(s);
        }
        this.degreEntrantMoyen = total / (float)this.ordre;
    }
    public void calculMeanDegreeOut(){
        long total = 0;
        //Le nombre moyen d'arêtes qui partent et débutent à partir d'un noeud.
        for(String s : directedWeightedGraph.vertexSet()){
            total += directedWeightedGraph.outDegreeOf(s);
        }
        this.degreSortantMoyen = total / (float)this.ordre;
    }
    public void calculOrdre(){
        this.ordre = directedWeightedGraph.vertexSet().size();
    }
    public void calculVolume(){
        this.volume = directedWeightedGraph.edgeSet().size();
    }
    public void calculDensite(){
        float n = this.ordre;
        float t = this.volume;
        this.densite = t / (n * (n - 1));
    }
}