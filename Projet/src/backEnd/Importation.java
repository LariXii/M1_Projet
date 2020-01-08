package backEnd;

import javafx.concurrent.Task;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import java.io.BufferedReader;
import java.io.FileReader;

public class Importation extends Task<Graph> {
    protected String filePath;
    protected long sizeFile;

    public Importation(String path, long size){
        this.filePath = path;
        this.sizeFile = size;
    }

    @Override
    protected Graph call() throws Exception {

        BufferedReader csv = new BufferedReader(new FileReader(filePath));
        String chaine;

        Graph<String, DefaultWeightedEdge> directedWeightedGraph = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);

        //Calcul de l'avancée
        double maxSize = sizeFile;
        double maxSizeKo = maxSize / 1024;
        double maxSizeMo = maxSizeKo / 1024;
        double currentSize = 0;

        while ((chaine = csv.readLine()) != null) {
            if(isCancelled()){
                return null;
            }
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

            currentSize += chaine.length();
            double currentSizeKo = currentSize / 1024;
            double currentSizeMo = currentSizeKo / 1024;

            updateMessage(Math.round((currentSizeMo*100)/maxSizeMo)+"%");
            updateProgress(currentSize , maxSize);
        }
        csv.close();
        updateMessage("100%");
        updateProgress(maxSize , maxSize);
        // Return null at the end of a Task of type Void
        return directedWeightedGraph;
    }

}
