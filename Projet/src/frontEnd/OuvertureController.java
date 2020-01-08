package frontEnd;

import backEnd.GraphTweet;
import backEnd.readingTask;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.*;

public class OuvertureController {

    @FXML
    private TextField textName;
    @FXML
    private Button buttonOpen;

    @FXML
    private ProgressBar progressFileReader;
    @FXML
    private Button cancelButton;
    @FXML
    private Label statusLabel;

    private GraphTweet graph;
    private String folderPath;
    private Task<GraphTweet> copyTask;

    public GraphTweet getGraph() {
        return graph;
    }

    public void setGraph(GraphTweet graph) {
        this.graph = graph;
    }

    public String getFolderPath() {
        return folderPath;
    }

    public void setFolderPath(String folderPath) {
        this.folderPath = folderPath;
    }

    /****************************************************
     *              OUVERTURE DU FICHIER                *
     ****************************************************/
    @FXML
    private void ouvrir(MouseEvent e) throws IOException{
        OuvertureController me = this;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Loading.fxml"));
        Parent loading_root = loader.load();

        Stage loading_stage = new Stage();
        loading_stage.setTitle("Projet Master Informatique");

        Scene loading_scene = new Scene(loading_root);
        Stage window_parent = (Stage)((Node) e.getSource()).getScene().getWindow();

        if(isFolderNameValid()){
            loading_stage.setScene(loading_scene);
            loading_stage.initOwner(window_parent);
            loading_stage.initModality(Modality.WINDOW_MODAL);

            loading_stage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    OuvertureController ctrler = loader.getController();
                    ctrler.setFolderPath("resources/" + textName.getText());
                    ctrler.startLoading();
                }
            });
            loading_stage.show();
        }
    }

    private boolean isFolderNameValid(){
        try{
            new FileReader("resources/" + textName.getText());
            return true;
        }
        catch(FileNotFoundException e){
            return false;
        }
    }

    private long lengthOfFile(String file){
        System.out.println(file);
        File f = new File(file);
        if(f.exists()){
            return f.length();
        }
        else{
            return 0L;
        }
    }

    private void startLoading(){
        progressFileReader.setProgress(0);
        cancelButton.setDisable(false);

        // Create a Task.
        GraphTweet graph = new GraphTweet();

        copyTask = new Task<GraphTweet>(){
            @Override
            protected GraphTweet call() throws Exception {

                BufferedReader csv = new BufferedReader(new FileReader(folderPath));
                String chaine;
                GraphTweet g = new GraphTweet();
                Graph<String, DefaultWeightedEdge> directedWeightedGraph = g.getDirectedWeightedGraph();

                //Calcul de l'avancée
                double maxSize = lengthOfFile(folderPath);
                double maxsizeO = maxSize * 8;
                double maxSizeKo = maxSize / 1024;
                double maxSizeMo = maxSizeKo / 1024;
                System.out.println("Taille en o du fichier "+maxSizeMo);
                double currentSize = 0;

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
                return g;
            }

        };
        // Unbind progress property
        progressFileReader.progressProperty().unbind();

        // Bind progress property
        progressFileReader.progressProperty().bind(copyTask.progressProperty());

        // Unbind text property for Label.
        statusLabel.textProperty().unbind();

        // Bind the text property of Label
        // with message property of Task
        statusLabel.textProperty().bind(copyTask.messageProperty());

        // When completed tasks
        copyTask.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        GraphTweet graph = copyTask.getValue();
                        statusLabel.textProperty().unbind();
                        System.out.println("Données chargées");
                        System.out.println("Nombre de sommets : "+graph.getOrdre());
                        System.out.println("Nombre d'arc : "+graph.getTaille());
                    }
                });

        copyTask.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, //
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        statusLabel.textProperty().unbind();
                        System.out.println("Abandon de la procédure");
                    }
                });

        // Start the Task.
        new Thread(copyTask).start();
    }

    @FXML
    private void onCancelButton(ActionEvent e){
        copyTask.cancel();
    }
}
