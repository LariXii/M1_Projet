package main;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BaseDeTweets {
    private TreeSet<Tweet> baseTweets;
    private HashMap<String, User> listUsers;
    private HashMap<String, Vertex> listVertex;
    private Graph<String,DefaultWeightedEdge> directedWeightedGraph;

    public BaseDeTweets() {
        this.baseTweets = new TreeSet<>();
        this.listUsers = new HashMap<>();
        this.listVertex = new HashMap<>();
    }

    public TreeSet<Tweet> getBaseTweets() {
        return baseTweets;
    }

    public void setBaseTweets(TreeSet<Tweet> baseTweets) {
        this.baseTweets = baseTweets;
    }

    public HashMap<String, Vertex> getListVertex(){
        return this.listVertex;
    }

    public void ajouterTweet(Tweet n) {
        baseTweets.add(n);
    }

    public void supprimerNews(int n) {
        Iterator<Tweet> it = baseTweets.iterator();
        int nb = 1;
        while (it.hasNext() && nb != n) {
            it.next();
            nb++;
        }
        baseTweets.remove(it.next());
    }

    public TreeSet<User> listCentralUsers(HashMap<String, User> listUsers,int n){
        if(n > 0 && n < 100){
            TreeSet<User> lCentraUsers = new TreeSet<>(User.comparatorByWeightDESC());
            //Récupération d'un set des clefs-valeurs de la hashMap listUsers
            Set entrySet = listUsers.entrySet();
            Iterator it = entrySet.iterator();
            while(it.hasNext()){
                Map.Entry me = (Map.Entry) it.next();
                User curr_User = (User) me.getValue();
                lCentraUsers.add(curr_User);
                //On supprime la plus petite valeur du TreeSet
                if(lCentraUsers.size() > n){
                    lCentraUsers.pollLast();
                }
            }
            return lCentraUsers;
        }
        return null;
    }

    public TreeSet<User> listCentralUsers(HashMap<String, User> listUsers){
        return this.listCentralUsers(listUsers,5);
    }

    public void diametre(HashMap<String, User> listUsers) {
        // Getting a Set of Key-value pairs
        Set entrySet = listUsers.entrySet();
        // Obtaining an iterator for the entry set
        Iterator it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            User curr_User = (User) me.getValue();
            Set entrySetRT = curr_User.getListReTweet().entrySet();
            // Obtaining an iterator for the entry set
            Iterator itRT = entrySetRT.iterator();
            if (itRT.hasNext()) {
                System.out.println("L'utilisateur " + me.getKey() + " à retweete : " + entrySetRT.size() + " utilisateurs");
            } else {
                System.out.println("L'utilisateur " + me.getKey() + " a effectue aucun retweet");
            }
            while (itRT.hasNext()) {
                Map.Entry meRT = (Map.Entry) itRT.next();
                ArrayList<Tweet> listRT = (ArrayList) meRT.getValue();
                System.out.println("[" + meRT.getKey() + "] " + listRT.size() + " fois");
            }
        }
    }

    public void degreMoyen(HashMap<String, User> listUsers){
        //Le nombre moyen d'arêtes qui partent et débutent à partir d'un noeud.
    }

    public int getOrdre(){
        //Nombre de noeuds du graphe
        return directedWeightedGraph.vertexSet().size();
    }
    public int getTaille_(){
        //Nombre d'arête du graphe
        int nombreArete = 0;
        // Getting a Set of Key-value pairs
        Set entrySet = this.listUsers.entrySet();
        // Obtaining an iterator for the entry set
        Iterator it = entrySet.iterator();
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            User curr_User = (User) me.getValue();
            Set entrySetRT = curr_User.getListReTweet().entrySet();
            nombreArete += entrySetRT.size();
        }
        return nombreArete;
    }
    public int getTaille(){
        return directedWeightedGraph.edgeSet().size();
    }
    public double getDensite(){
        //La densité d’un graphe est le nombre d’arêtes réalisées dans le graphe par
        //rapport au nombre d’arêtes possibles
        float n = this.getOrdre();
        float t = this.getTaille();
        double n_ = n * (n - 1);
        return t / n_;
    }

    private void createGraph(){
        Graph<String, DefaultWeightedEdge> g = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
        // Getting a Set of Key-value pairs
        Set entrySet = listVertex.entrySet();
        // Obtaining an iterator for the entry set
        Iterator it = entrySet.iterator();
        //On parcourt tous les sommets
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry) it.next();
            Vertex sommet_curr = (Vertex) me.getValue();

            g.addVertex(sommet_curr.getName());

            //On récupére la liste de ses arêtes
            Set entrySuccessors = sommet_curr.getlSuccessors().entrySet();
            Iterator itSucc = entrySuccessors.iterator();
            while(itSucc.hasNext()){
                Map.Entry meSucc = (Map.Entry)itSucc.next();
                String vertexTarget = (String)meSucc.getKey();
                int weightEdge = (int)meSucc.getValue();

                if(!g.containsVertex(vertexTarget)){
                    g.addVertex(vertexTarget);
                }
                g.addEdge(sommet_curr.getName(),vertexTarget);
                g.setEdgeWeight(sommet_curr.getName(),vertexTarget,weightEdge);
            }
        }
    }

    public void sauvegarder(String file) throws IOException, FileNotFoundException {
        //try {
        FileOutputStream fos = new FileOutputStream("resources/" + file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        // No buffer is required because ObjectOutputStream is automaticaly buffered.

        oos.writeObject(baseTweets);

        oos.flush();
        oos.close();

        System.out.println("Les nouvelles sont sauvegard�es");
    }

    @SuppressWarnings("unchecked")
    public ArrayList<Integer> ouvrir_1(String file) throws IOException {
        /**
         * Initialisation du buffer
         * */
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
    }

    public void ouvrir(String file) throws IOException {
        /**
         * Initialisation du buffer
         * */
        BufferedReader csv = new BufferedReader(new FileReader("resources/" + file));
        String chaine;
        new BaseDeTweets();

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
                directedWeightedGraph.addVertex(idUser);
                //Ajout de l'utilisateur retweeté dans le graphe, s'il existe déjà le graphe n'est pas modifié
                directedWeightedGraph.addVertex(idUserRT);

                //Ajout d'une arête entre les deux sommets
                DefaultWeightedEdge dwe = directedWeightedGraph.getEdge(idUser,idUserRT);
                if(dwe == null){
                    if(!idUser.equals(idUserRT)){
                        directedWeightedGraph.addEdge(idUser,idUserRT);
                        directedWeightedGraph.setEdgeWeight(idUser,idUserRT,1);
                    }
                }
                else{
                    directedWeightedGraph.setEdgeWeight(dwe,directedWeightedGraph.getEdgeWeight(dwe));
                }

                /*// Ajout de l'id de l'utilisateur dans la liste des sommets s'il n'existe pas déjà
                Vertex sommetUser = new Vertex(idUser);
                listVertex.putIfAbsent(idUser, sommetUser);

                //Ajout de l'id de l'utilisateur retweete dans la liste des sommets s'il n'existe pas déjà
                Vertex sommetRt = new Vertex(idUserRT);
                listVertex.putIfAbsent(idUserRT, sommetRt);

                //Ajout dans la liste des successeurs de l'utilisateur qui a tweete l'utilisateur retweete
                sommetUser = listVertex.get(idUser);
                HashMap<String,Integer> successors = sommetUser.getlSuccessors();
                if(successors.get(idUserRT) == null) {
                    successors.put(idUserRT,1);
                }
                else{
                    successors.put(idUserRT,successors.get(idUserRT) + 1);
                }

                //Ajout dans la liste des predecesseurs de l'utilisateur retweete l'utilisateur qui a tweete
                sommetRt = listVertex.get(idUserRT);
                HashMap<String,Integer> predecessors = sommetRt.getlPredecessors();
                if(predecessors.get(idUser) == null) {
                    predecessors.put(idUser,1);
                }
                else{
                    predecessors.put(idUser,predecessors.get(idUser) + 1);
                }
                i++;*/
            }
        }
        csv.close();
    }

    //fdsf
    @Override
    public String toString() {
        Iterator<Tweet> it = baseTweets.iterator();
        String ret = "";
        int nb = 1;
        if (!it.hasNext()) {
            ret = "Aucun article dans la base de news";
        }
        while (it.hasNext()) {
            ret += "[" + nb + "][" + it.next().toString() + "]\n";
            nb++;
        }
        return ret;
    }
}
