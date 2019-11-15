import java.util.Iterator;
import java.util.TreeSet;
import java.io.*;
import java.io.Serializable;

public class BaseDeNews{
	private TreeSet<News> basenews;

	BaseDeNews(){
		basenews = new TreeSet<News>();
	}

	public int ajouterNews(News n) {
		basenews.add(n);
		return 0;
	}

	public int supprimerNews(int n) {
		Iterator<News> it = basenews.iterator();
		int nb = 1;
		while(it.hasNext() && nb != n) {
			it.next();
			nb++;
		}
		basenews.remove(it.next());
		return 0;
	}
	
	public TreeSet<News> getBaseDeNews(){
		return basenews;
	}

	public int sauvegarder(String file) throws IOException,FileNotFoundException {
		//try {
		FileOutputStream fos = new FileOutputStream( file );
		ObjectOutputStream oos = new ObjectOutputStream( fos );
		// No buffer is required because ObjectOutputStream is automaticaly buffered.

		oos.writeObject(basenews);

		oos.flush();
		oos.close();

		System.out.println( "Les nouvelles sont sauvegardées" );
		/* } catch ( IOException exception ) {
            System.err.println( "Cannot write objects into the stream" );
            exception.printStackTrace();
        }*/
		return 1;
	}

	@SuppressWarnings("unchecked")
	public int ouvrir(String file) throws FileNotFoundException,IOException, ClassNotFoundException{
		FileInputStream fis = new FileInputStream( file );
		ObjectInputStream ois = new ObjectInputStream( fis );

		basenews.addAll((TreeSet<News>) ois.readObject());
		for( News instance : basenews ) {
			System.out.println( instance );
		}

		ois.close();
		return 1;
	}

	public String toString() {
		Iterator<News> it = basenews.iterator();
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
