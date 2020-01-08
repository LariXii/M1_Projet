package frontEnd;

import backEnd.CentralUser;
import backEnd.GraphTweet;
import backEnd.Importation;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class OuvertureController{
    private static GraphTweet myGraph;
    private static String folderPath;
    private static Stage mainStage;

    @FXML
    private TextField textName;
    @FXML
    private Button buttonOpen;

    @FXML
    private AnchorPane loadingPanel;
    @FXML
    private ProgressBar progressFileReader;
    @FXML
    private Button cancelButton;
    @FXML
    private Label statusLabel;

    public GraphTweet getGraph() {
        return myGraph;
    }

    public void setGraph(GraphTweet graph) {
        this.myGraph = graph;
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
        if(isFolderNameValid()){
            setFolderPath("resources/"+textName.getText());
            mainStage = (Stage)((Node) e.getSource()).getScene().getWindow();

            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Loading.fxml"));
            loader.setController(this);
            Parent loading_root = loader.load();
            Stage loading_stage = new Stage();
            Scene loading_scene = new Scene(loading_root);

            loading_stage.setScene(loading_scene);
            loading_stage.initOwner(mainStage);
            loading_stage.initModality(Modality.WINDOW_MODAL);

            loading_stage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    startLoading(loading_stage);
                }
            });
            loading_stage.show();
        }
    }

    @FXML
    private void initialize(){
        System.out.println(this.getClass()+ " est initialisé");
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

    /****************************************************
     *              CHARGEMENT DU FICHIER                *
     ****************************************************/

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

    private void startLoading(Stage loadingStage){

        progressFileReader.setProgress(0);
        cancelButton.setDisable(false);

        Importation importData = new Importation(folderPath,lengthOfFile(folderPath));
        // Unbind progress property
        progressFileReader.progressProperty().unbind();

        // Bind progress property
        progressFileReader.progressProperty().bind(importData.progressProperty());

        // Unbind text property for Label.
        statusLabel.textProperty().unbind();

        // Bind the text property of Label
        // with message property of Task
        statusLabel.textProperty().bind(importData.messageProperty());

        // When completed tasks
        importData.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        myGraph = new GraphTweet();
                        myGraph.setDirectedWeightedGraph(importData.getValue());
                        statusLabel.textProperty().unbind();
                        loadingStage.close();
                        afficheInterface();
                    }
                });

        importData.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, //
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        myGraph = null;
                        statusLabel.textProperty().unbind();
                        progressFileReader.progressProperty().unbind();
                        progressFileReader.setProgress(0);
                        loadingStage.close();
                    }
                });

        // Start the Task.
        new Thread(importData).start();

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                importData.cancel();
            }
        });
    }

    private void afficheInterface(){
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("interface.fxml"));
            Parent interface_root;
            try {
                interface_root = loader.load();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            Scene interface_scene = new Scene(interface_root);

            mainStage.setScene(interface_scene);
            mainStage.show();
    }

    /*private void execution() {
        DecimalFormat df = new DecimalFormat("0.00000000");
        double dens = bd.getDensite();
        densite.setText(String.valueOf(dens));

        int taille_var = bd.getVolume();
        taille.setText(String.valueOf(taille_var));
        int ordre_var = bd.getOrdre();
        ordre.setText(String.valueOf(ordre_var));
        double diametre_var = bd.getDiametre();
        diametre.setText(String.valueOf(diametre_var));

        double meandegree = bd.getDegreMoyen();
        degre_moy.setText(String.valueOf(meandegree));
        double meandegreein = bd.getDegreEntrantMoyen();
        degre_moy_in.setText(String.valueOf(meandegreein));
        double meandegreeout = bd.getDegreSortantMoyen();
        degre_moy_out.setText(String.valueOf(meandegreeout));

        TreeSet<CentralUser> pageR = bd.getPageRank(5);

        page_rk_user.setCellValueFactory(new PropertyValueFactory<>("UserName"));

        // Create column Email (Data type of String).
        //  TableColumn<backEnd.CentralUser, Double> score = new TableColumn<backEnd.CentralUser, Double>("score");
        page_rk_score.setCellValueFactory(new PropertyValueFactory<>("score"));
        //page_rk_tab.getColumns().addAll(userName, score);

        ObservableList<CentralUser> list = getNewsListPR();
        page_rk_tab.setItems(list);


        centr_user.setCellValueFactory(new PropertyValueFactory<>("UserName"));

        // Create column Email (Data type of String).
        //  TableColumn<backEnd.CentralUser, Double> score = new TableColumn<backEnd.CentralUser, Double>("score");
        centr_score.setCellValueFactory(new PropertyValueFactory<>("score"));
        //page_rk_tab.getColumns().addAll(userName, score);

        ObservableList<CentralUser> listCentr = getNewsListCentr();
        centr.setItems(listCentr);

    }
    private ObservableList<CentralUser> getNewsListPR(){
        List<CentralUser> list = new ArrayList<CentralUser>(bd.getPageRank(5));
        ObservableList<CentralUser> obs_list = FXCollections.observableList(list);
        return obs_list;
    }
    private ObservableList<CentralUser> getNewsListCentr(){
        List<CentralUser> list = new ArrayList<CentralUser> (bd.getDegreeCentrality(5));
        ObservableList<CentralUser> obs_list = FXCollections.observableList(list);
        return obs_list;
    }*/

}
