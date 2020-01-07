//import com.sun.xml.internal.ws.api.streaming.XMLStreamReaderFactory;
import org.graphstream.algorithm.Toolkit;
import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.interfaces.ShortestPathAlgorithm;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class BaseDeTweets {
    //private TreeSet<Tweet> baseTweets;
    //private HashMap<String, User> listUsers;
    //private HashMap<String, String> listVertex;
    private Graph<String,DefaultWeightedEdge> directedWeightedGraph;
    //TreeSet servant à récupérer les utilisateurs les plus centraux
    /*private TreeSet<Map.Entry<String,Double>> usersCentrals = new TreeSet<>((o1, o2) -> {
        if(o1.getValue() < o2.getValue()){
            return 1;
        }
        else{
            if(o1.getValue() > o2.getValue()){
                return -1;
            }
            else{
                return o1.getKey().compareTo(o1.getKey());
            }
        }
    });*/

    public BaseDeTweets() {
        //this.baseTweets = new TreeSet<>();
        //this.listUsers = new HashMap<>();
        //this.listVertex = new HashMap<>();
    }

    public Graph<String,DefaultWeightedEdge> getDirectedWeightedGraph(){
        return directedWeightedGraph;
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

    /************************************************
     *          STATISTIQUE SUR LE GRAPHE           *
     ************************************************/

    public double getMaxDegree(Graph<String, DefaultWeightedEdge> g){
        double max = 0.0;
        for(String s : g.vertexSet()){
            if(max < g.degreeOf(s))
                max = g.degreeOf(s);
        }
        return max;
    }
    public double getDiametre(){
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
        return diametre;
    }
    public boolean isConnected(){
        ConnectivityInspector<String,DefaultWeightedEdge> cIGraph = new ConnectivityInspector<>(this.directedWeightedGraph);
        boolean connected = cIGraph.isConnected();
        return connected;
    }
    public double getMeanDegree(){
        long total = 0;
        //Le nombre moyen d'arêtes qui partent et débutent à partir d'un noeud.
        for(String s : directedWeightedGraph.vertexSet()){
            total += directedWeightedGraph.degreeOf(s);
        }
        return total / (double)getOrdre();
    }
    public double getMeanDegreeIn(){
        long total = 0;
        //Le nombre moyen d'arêtes qui partent et débutent à partir d'un noeud.
        for(String s : directedWeightedGraph.vertexSet()){
            total += directedWeightedGraph.inDegreeOf(s);
        }
        return total / (double)getOrdre();
    }
    public double getMeanDegreeOut(){
        long total = 0;
        //Le nombre moyen d'arêtes qui partent et débutent à partir d'un noeud.
        for(String s : directedWeightedGraph.vertexSet()){
            total += directedWeightedGraph.outDegreeOf(s);
        }
        return total / (double)getOrdre();
    }
    public int getOrdre(){
        return directedWeightedGraph.vertexSet().size();
    }
    public int getTaille(){
        return directedWeightedGraph.edgeSet().size();
    }
    public double getDensite(){
        float n = this.getOrdre();
        float t = this.getTaille();
        return t / (n * (n - 1));
    }

    public void ouvrir(String file) throws IOException {
        /**
         * Initialisation du buffer
         * */
        BufferedReader csv = new BufferedReader(new FileReader("resources/" + file));
        String chaine;
        new BaseDeTweets();
        long time = System.currentTimeMillis();
        directedWeightedGraph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        while ((chaine = csv.readLine()) != null) {
            String[] tabChaine = chaine.split("\t");
            //On ne parcourt que les utilisateurs qui ont retweeté
            if (tabChaine.length == 5) {
                //Récupération de l'id de l'utilisateur (sommet)
                String idUser = tabChaine[1];

                //Récupération de l'id du retweet (sommet)
                String idUserRT = tabChaine[4];

                //Ajout de l'utilisateur dans le graphe, s'il existe déjà le graphe n'est pas modifié
                String source = new String(idUser);
                directedWeightedGraph.addVertex(source);
                //Ajout de l'utilisateur retweeté dans le graphe, s'il existe déjà le graphe n'est pas modifié
                String target = new String(idUserRT);
                directedWeightedGraph.addVertex(target);

                //Ajout d'une arête entre les deux sommets
                DefaultWeightedEdge dwe = directedWeightedGraph.getEdge(source,target);
                if(dwe == null){
                    if(!idUser.equals(idUserRT)){
                        directedWeightedGraph.addEdge(source,target);
                        directedWeightedGraph.setEdgeWeight(source,target,1);
                    }
                }
                else{
                    directedWeightedGraph.setEdgeWeight(dwe,directedWeightedGraph.getEdgeWeight(dwe) + 1);
                }
            }
        }
        csv.close();
        reportPerformanceFor("graph allocation", time);
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
        new BaseDeTweets();
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
                //Création d'un objet Tweet
                Tweet t = new Tweet(1, idUser, date, texte, reTweet);
                //Ajout dans la base de tweet
                baseTweets.add(t);
                // Ajout de l'utilisateur dans la liste des utilisateurs et enregistrement du retweet de l'utilisateur
                User user = new User(idUser);
                listUsers.putIfAbsent(idUser, user);

                if(reTweet != null){
                    User userReTweet = new User(reTweet);
                    listUsers.putIfAbsent(reTweet, userReTweet);
                    userReTweet = listUsers.get(reTweet);
                    userReTweet.setEstReTweet(userReTweet.getEstReTweet() + 1);
                }

                user = listUsers.get(idUser);
                if (reTweet != null) {
                    HashMap<String, ArrayList<Tweet>> listReTweet = user.getListReTweet();
                    if (listReTweet.get(reTweet) == null) {
                        ArrayList<Tweet> arr = new ArrayList<>();
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
