import java.io.*;
import java.util.TreeSet;

public class BaseDeTweets {
    TreeSet<Tweet> baseTweets;

    public BaseDeTweets(TreeSet<Tweet> baseTweets) {
        this.baseTweets = baseTweets;
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
        FileInputStream fis = new FileInputStream("resources/"+file );
        ObjectInputStream ois = new ObjectInputStream( fis );

        baseTweets.addAll((TreeSet<Tweet>) ois.readObject());
        for( Tweet instance : baseTweets ) {
            System.out.println( instance );
        }

        ois.close();
    }
}
