import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Scanner;
import java.util.TreeSet;

public class BaseDeTweets {
    private TreeSet<Tweet> baseTweets = new TreeSet<Tweet>();;

    public BaseDeTweets() {
        this.baseTweets = new TreeSet<Tweet>();
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
    public void ouvrir(String file) throws FileNotFoundException,IOException, ClassNotFoundException{
        /*FileInputStream fis = new FileInputStream("resources/"+file );
        ObjectInputStream ois = new ObjectInputStream( fis );*/


        /*baseTweets.addAll((TreeSet<Tweet>) ois.readObject());
        for( Tweet instance : baseTweets ) {
            System.out.println( instance );
        }*/
        BufferedReader csv = new BufferedReader(new FileReader("resources/"+file));
        String chaine;
            int i = 1;
            while((chaine = csv.readLine())!= null)
            {
                if(i>1) {
                    String[] tabChaine = chaine.split("\t");
                    long idTweet = Long.parseLong(tabChaine[0]);
                    String idUser = tabChaine[1];
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String[] s_date = tabChaine[2].split(" ");
                    LocalDate date = LocalDate.parse(s_date[0], formatter);
                    String texte = tabChaine[3];
                    String reTweet = "null";
                    Tweet t = new Tweet(idTweet, idUser, date, texte, reTweet);
                    System.out.print("["+i+"]" + t + "\n");
                    baseTweets.add(t);
                }
                i++;
            }
            csv.close();
            System.out.print(baseTweets.size());
    }

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
