package frontEnd;

import backEnd.AfficheGraph;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.jgrapht.Graph;

import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class OuvertureController{
    private static GraphTweet myGraph;
    private static String folderPath;
    private static Stage mainStage;

    //FENETRE DE SAISIE
    @FXML
    private TextField textName;
    @FXML
    private Button buttonOpen;

    //FENETRE DE CHARGEMENT
    @FXML
    private AnchorPane loadingPanel;
    @FXML
    private ProgressBar progressFileReader;
    @FXML
    private Button cancelButton;
    @FXML
    private Label statusLabel;

    //FENETRE D'INTERFACE
    @FXML
    private ProgressIndicator indicatorStatistique;
    @FXML
    private AnchorPane interfacePane;
    @FXML
    private Label densite;
    @FXML
    private Label taille;
    @FXML
    private Label ordre;
    @FXML
    private Label diametre;
    @FXML
    private Label degre_moy;
    @FXML
    private Label degre_moy_in;
    @FXML
    private Label degre_moy_out;
    @FXML
    private TableView<CentralUser> page_rk_tab;
    @FXML
    private TableColumn<CentralUser, String> page_rk_user;
    @FXML
    private TableColumn<CentralUser, Double> page_rk_score;
    @FXML
    private TableView<CentralUser> centr;
    @FXML
    private TableColumn<CentralUser, String> centr_user;
    @FXML
    private TableColumn<CentralUser, Double> centr_score;
    @FXML
    private TextField nbr_central_user;
    @FXML
    private Button btnDisplayGraph;

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
                loader.setController(this);
                interface_root = loader.load();
            } catch (IOException exception) {
                throw new RuntimeException(exception);
            }

            Scene interface_scene = new Scene(interface_root);

            mainStage.hide();
            mainStage.setScene(interface_scene);
            mainStage.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    execution();
                }
            });
            mainStage.show();
    }

    private void execution() {
        interfacePane.setDisable(true);

        indicatorStatistique.setProgress(0);
        indicatorStatistique.setVisible(true);

        Task<Void> calculsStats = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                myGraph.calculOrdre();
                myGraph.calculVolume();
                myGraph.calculDensite();
                myGraph.calculDiametre();
                myGraph.calculMeanDegree();
                myGraph.calculMeanDegreeIn();
                myGraph.calculMeanDegreeOut();
                return null;
            }
        };

        indicatorStatistique.progressProperty().unbind();

        // Bind progress property
        indicatorStatistique.progressProperty().bind(calculsStats.progressProperty());

        calculsStats.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {
                        try{
                            Thread.sleep(500);
                        }
                        catch(InterruptedException ie){
                            ie.printStackTrace();
                        }
                        indicatorStatistique.setVisible(false);
                        interfacePane.setDisable(false);
                        afficheStatistique();
                    }
                });

        calculsStats.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, //
                new EventHandler<WorkerStateEvent>() {
                    @Override
                    public void handle(WorkerStateEvent t) {

                    }
                });

        new Thread(calculsStats).start();
    }

    private void afficheStatistique(){
        double dens = myGraph.getDensite();
        densite.setText(String.valueOf(dens));

        int taille_var = myGraph.getVolume();
        taille.setText(String.valueOf(taille_var));

        int ordre_var = myGraph.getOrdre();
        ordre.setText(String.valueOf(ordre_var));

        double diametre_var = myGraph.getDiametre();
        diametre.setText(String.valueOf(diametre_var));

        double meandegree = myGraph.getDegreMoyen();
        degre_moy.setText(String.valueOf(meandegree));

        double meandegreein = myGraph.getDegreEntrantMoyen();
        degre_moy_in.setText(String.valueOf(meandegreein));

        double meandegreeout = myGraph.getDegreSortantMoyen();
        degre_moy_out.setText(String.valueOf(meandegreeout));

        page_rk_user.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        page_rk_score.setCellValueFactory(new PropertyValueFactory<>("score"));


        ObservableList<CentralUser> list = getNewsListPR(5);
        page_rk_tab.setItems(list);


        centr_user.setCellValueFactory(new PropertyValueFactory<>("UserName"));
        centr_score.setCellValueFactory(new PropertyValueFactory<>("score"));

        ObservableList<CentralUser> listCentr = getNewsListCentr(5);
        centr.setItems(listCentr);
    }

    private ObservableList<CentralUser> getNewsListPR(int n){
        List<CentralUser> list = new ArrayList<CentralUser>(myGraph.getPageRank(n));
        ObservableList<CentralUser> obs_list = FXCollections.observableList(list);
        return obs_list;
    }

    private ObservableList<CentralUser> getNewsListCentr(int n){
        List<CentralUser> list = new ArrayList<CentralUser> (myGraph.getDegreeCentrality(n));
        ObservableList<CentralUser> obs_list = FXCollections.observableList(list);
        return obs_list;
    }

    @FXML
    private void afficheGraphe(){
        Graph g = myGraph.getSubGraph(50);
        AfficheGraph affichage = new AfficheGraph(g);
        affichage.afficheGraphe();
    }

}
