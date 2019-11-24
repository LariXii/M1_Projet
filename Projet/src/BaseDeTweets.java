import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BaseDeTweets {
    private TreeSet<Tweet> baseTweets;
    private HashMap<String, User> listUsers;

    public BaseDeTweets() {
        this.baseTweets = new TreeSet<>();
        this.listUsers = new HashMap<>();
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
        while (it.hasNext() && nb != n) {
            it.next();
            nb++;
        }
        baseTweets.remove(it.next());
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
    public void ouvrir(String file) throws IOException {
        BufferedReader csv = new BufferedReader(new FileReader("resources/" + file));
        String chaine;
        /** Création de la base de tweet */
        new BaseDeTweets();
        int i = 0;
        while ((chaine = csv.readLine()) != null) {
            String[] tabChaine = chaine.split("\t");
            /**Récupération de l'id du tweet*/
            long idTweet = Long.parseLong(tabChaine[0]);
            /**Récupération de l'id de l'utilisateur*/
            String idUser = tabChaine[1];
            /**Récupération de la date du tweet*/
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            /**La date étant composée avec l'heure, nous la tronquons*/
            String[] s_date = tabChaine[2].split(" ");
            LocalDate date = LocalDate.parse(s_date[0], formatter);
            /**Récupération du texte du tweet*/
            String texte = tabChaine[3];
            /**Récupération de l'id du retweet s'il existe*/
            String reTweet = null;
            if (tabChaine.length == 5) {
                reTweet = tabChaine[4];
            }
            /**Création d'un objet Tweet*/
            Tweet t = new Tweet(idTweet, idUser, date, texte, reTweet);
            /**Ajout dans la base de tweet*/
            baseTweets.add(t);
            /** Ajout de l'utilisateur dans la liste des utilisateurs et enregistrement du retweet de l'utilisateur */
            User user = new User(idUser);
            listUsers.putIfAbsent(idUser, user);
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
            i++;
        }
        csv.close();
        System.out.print(baseTweets.size());
        System.out.print("Il y a " + listUsers.size() + " utilisateurs qui ont tweeté");
        diametre(listUsers);
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
