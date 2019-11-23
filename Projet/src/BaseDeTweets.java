import sun.reflect.generics.tree.Tree;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BaseDeTweets {
    private TreeSet<Tweet> baseTweets;
    private HashMap<String,Integer> listUser;
    private long[][] matriceAdj;

    public BaseDeTweets() {
        this.baseTweets = new TreeSet<>();
        this.listUser = new HashMap<>();
    }

    public TreeSet<Tweet> getBaseTweets() {
        return baseTweets;
    }

    public void setBaseTweets(TreeSet<Tweet> baseTweets) {
        this.baseTweets = baseTweets;
    }

    public void ajouterTweet(Tweet n) {
        baseTweets.add(n);
    }

    public void supprimerNews(int n) {
        Iterator<Tweet> it = baseTweets.iterator();
        int nb = 1;
        while(it.hasNext() && nb != n) {
            it.next();
            nb++;
        }
        baseTweets.remove(it.next());
    }

    public void createMatrice(){//TreeSet<Tweet> tsTweet, HashMap<String,Integer> tUsers){
        /*int n = tUsers.size();
          matriceAdj = new long[n][n];
        /*Iterator<Tweet> it = tsTweet.iterator();
        Tweet t;
        int num_sommet;
        int num_lien;
        while(it.hasNext()){
            t = it.next();
            if(t.getIdReTweet()!=null){
                num_sommet = tUsers.get(t.getIdUser());
                num_lien = tUsers.get(t.getIdReTweet());
                System.out.print("L'utilisateur "+t.getIdUser()+"["+num_sommet+"] a retweeté "+t.getIdReTweet()+"["+num_lien+"]\n");
                matriceAdj[num_sommet][num_lien]++;
            }
        }
        System.out.print("La matrice est créée !\n"+matriceAdj.length);*/
    }

    public void sauvegarder(String file) throws IOException, FileNotFoundException {
        //try {
        FileOutputStream fos = new FileOutputStream("resources/"+file);
        ObjectOutputStream oos = new ObjectOutputStream( fos );
        // No buffer is required because ObjectOutputStream is automaticaly buffered.

        oos.writeObject(baseTweets);

        oos.flush();
        oos.close();

        System.out.println( "Les nouvelles sont sauvegard�es" );
    }

    @SuppressWarnings("unchecked")
    public void ouvrir(String file) throws IOException{
        BufferedReader csv = new BufferedReader(new FileReader("resources/"+file));
        String chaine;
        int i = 0;
        /** Création de la base de tweet */
        new BaseDeTweets();
            while((chaine = csv.readLine())!= null)
            {
                    String[] tabChaine = chaine.split("\t");
                    /**Récupération de l'id du tweet*/
                    long idTweet = Long.parseLong(tabChaine[0]);
                    /**Récupération de l'id de l'utilisateur*/
                    String idUser = tabChaine[1];
                    /**Ajout de l'id de l'utilisateur dans une map. L'id de l'utilisateur est la clé et le numéro du sommet de la matrice la valeur*/
                    Integer ret = listUser.putIfAbsent(idUser,i);
                    if(ret == null){
                        i++;
                    }
                    /**Récupération de la date du tweet*/
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    /**La date étant composée avec l'heure, nous la tronquons*/
                    String[] s_date = tabChaine[2].split(" ");
                    LocalDate date = LocalDate.parse(s_date[0], formatter);
                    /**Récupération du texte du tweet*/
                    String texte = tabChaine[3];
                    /**Récupération de l'id du retweet s'il existe*/
                    String reTweet = null;
                    if(tabChaine.length == 5){
                        reTweet = tabChaine[4];
                    }
                    /**Création d'un objet Tweet*/
                    Tweet t = new Tweet(idTweet, idUser, date, texte, reTweet);
                    /**Ajout dans la base de tweet*/
                    baseTweets.add(t);
            }
            csv.close();
            System.out.print(baseTweets.size());
            System.out.print("Il y a "+listUser.size()+" utilisateurs qui ont tweeté");
            //createMatrice(baseTweets,listUser);
    }
//fdsf
    @Override
    public String toString() {
        Iterator<Tweet> it = baseTweets.iterator();
        String ret = "";
        int nb = 1;
        if(!it.hasNext()) {
            ret = "Aucun article dans la base de news";
        }
        while(it.hasNext()) {
            ret += "["+nb+"]["+it.next().toString()+"]\n";
            nb++;
        }
        return ret;
    }
}
