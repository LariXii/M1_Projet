package backEnd;
import javafx.concurrent.Task;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class GraphTweet{
    private Graph<String,DefaultWeightedEdge> directedWeightedGraph;

    private double densite;
    private double degreMax;
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

    public double getDegreMax() {
        return degreMax;
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
        Set<String> vertices = directedWeightedGraph
                .vertexSet().stream().filter(el -> directedWeightedGraph.degreeOf(el) > n).collect(Collectors.toSet());
        /***/System.out.println(vertices.size());
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

    public void calculMaxDegree(Graph<String, DefaultWeightedEdge> g){
        double max = 0.0;
        for(String s : g.vertexSet()){
            if(max < g.degreeOf(s))
                max = g.degreeOf(s);
        }
        degreMax = max;
    }
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
            reportPerformanceFor("graph diametre", time);
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

    public static void reportPerformanceFor(String msg, long refTime) {
        double time = (System.currentTimeMillis() - refTime) / 1000.0;
        double mem = usedMemory() / (1024.0 * 1024.0);
        mem = Math.round(mem * 100) / 100.0;
        System.out.println(msg + " (" + time + " sec, " + mem + "MB)");
    }
    public static long usedMemory() {
        Runtime rt = Runtime.getRuntime();

        return rt.totalMemory() - rt.freeMemory();
    }
    @Override
    public String toString() {
        return "";
    }

    //@SuppressWarnings("unchecked")
    /*public ArrayList<Integer> ouvrir_1(String file) throws IOException {
     *//**
     * Initialisation du buffer
     * *//*
        BufferedReader csv = new BufferedReader(new FileReader("resources/" + file));
        String chaine;
        int num_ligne = 0;
        //List des erreurs du fichier lu
        ArrayList<Integer> tErr = new ArrayList<>();
        //Création de la base de tweet
        new backEnd.BaseDeTweets();
        while ((chaine = csv.readLine()) != null) {
            String[] tabChaine = chaine.split("\t");
            //Variable utile afin de connaitre le numéro de la ligne erronée
            num_ligne++;
            if (tabChaine.length == 4 || tabChaine.length == 5) {
                //Récupération de l'id du tweet
                long idTweet = Long.parseLong(tabChaine[0]);
                //Récupération de l'id de l'utilisateur
                String idUser = tabChaine[1];
                //Récupération de la date du tweet
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                //La date étant composée avec l'heure, nous la tronquons
                String[] s_date = tabChaine[2].split(" ");
                LocalDate date = LocalDate.parse(s_date[0], formatter);
                //Récupération du texte du tweet
                String texte = tabChaine[3];
                //Récupération de l'id du retweet s'il existe
                String reTweet = null;
                if (tabChaine.length == 5) {
                    reTweet = tabChaine[4];
                }
                //Création d'un objet backEnd.Tweet
                backEnd.Tweet t = new backEnd.Tweet(1, idUser, date, texte, reTweet);
                //Ajout dans la base de tweet
                baseTweets.add(t);
                // Ajout de l'utilisateur dans la liste des utilisateurs et enregistrement du retweet de l'utilisateur
                backEnd.User user = new backEnd.User(idUser);
                listUsers.putIfAbsent(idUser, user);

                if(reTweet != null){
                    backEnd.User userReTweet = new backEnd.User(reTweet);
                    listUsers.putIfAbsent(reTweet, userReTweet);
                    userReTweet = listUsers.get(reTweet);
                    userReTweet.setEstReTweet(userReTweet.getEstReTweet() + 1);
                }

                user = listUsers.get(idUser);
                if (reTweet != null) {
                    HashMap<String, ArrayList<backEnd.Tweet>> listReTweet = user.getListReTweet();
                    if (listReTweet.get(reTweet) == null) {
                        ArrayList<backEnd.Tweet> arr = new ArrayList<>();
                        arr.add(t);
                        listReTweet.put(reTweet, arr);
                    } else {
                        listReTweet.get(reTweet).add(t);
                    }
                } else {
                    user.getListTweet().add(t);
                }
            }
            else{
                tErr.add(num_ligne);
            }
        }
        csv.close();
        System.out.println(baseTweets.size());
        System.out.println("Il y a " + listUsers.size() + " utilisateurs qui ont tweeté");
        return tErr;
    }*/
}
