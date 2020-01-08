package frontEnd;

import backEnd.GraphTweet;
import backEnd.CentralUser;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;

public class Controller  {
    @FXML
    private Label densite;
    @FXML
    private Label taille;
    @FXML
    private Label ordre;
    @FXML
    private Label diametre;
    //@FXML
    //private Label page_rk;
    @FXML
    private Label degre_moy;
    @FXML
    private Label degre_moy_in;
    @FXML
    private Label degre_moy_out;
   // @FXML
    //private Label centralite;
    @FXML
    private TextField fichier;
    @FXML
    private Button fichier_ok;
    @FXML
    private ProgressBar temps;
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
    private AnchorPane jgraph;
    @FXML
    private Pane pane_graph;
    @FXML
    private AreaChart graph;

    @FXML
    private void ouvrir() {
        //execution();
        pane_graph.setVisible(!pane_graph.isVisible());
    }
    private ObservableList<CentralUser> userData = FXCollections.observableArrayList();

    public ObservableList<CentralUser> getPersonData() {
        return userData;
    }

    public void afficher(){
        System.out.println("Test");
    }

    //objets graphiques représentant un cercle

    public  Rectangle rectangle;
    private BooleanProperty isNotCreate = new SimpleBooleanProperty(true);
    private static GraphTweet bd;



//definir la troupe des objets graphiques


    private void execution() {
/*
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label label_fichier = new Label("Nom du fichier : ");
        grid.add(label_fichier, 0, 1);

        TextField field_fichier = new TextField();
        grid.add(field_fichier, 1, 1);

        ButtonBar buttonBar = new ButtonBar();
        Button addBut = new Button("Confirmer");

        ButtonBar.setButtonData(addBut, ButtonBar.ButtonData.OK_DONE);

        Button cancelBut = new Button("Annuler");
        ButtonBar.setButtonData(cancelBut, ButtonBar.ButtonData.CANCEL_CLOSE);

        // Add buttons to the ButtonBar
        buttonBar.getButtons().addAll(addBut, cancelBut);

        grid.add(buttonBar,1,2);

        Scene sceneForm = new Scene(grid, 350, 100);

        // New window (Stage)
        Stage windowForm = new Stage();
        if(open) {
            windowForm.setTitle("Ouvrir un fichier");
        }else {
            windowForm.setTitle("Enregistrer sous...");
        }
        windowForm.setScene(sceneForm);
*/
        // Set position of second window, related to primary window.
        //windowForm.setX(primaryStage.getX() + (primaryStage.getWidth()/2 - sceneForm.getWidth()/2));
        //windowForm.setY(primaryStage.getY() + (primaryStage.getHeight()/2 - sceneForm.getHeight()/2));
    //    windowForm.show();

                //try{
                    bd = new GraphTweet();
                    long startTime = System.currentTimeMillis();
                    //bd.ouvrir(fichier.getText());
                    long endTime = System.currentTimeMillis();
                    Graph<String, DefaultWeightedEdge> g = bd.getDirectedWeightedGraph();

                    System.out.println("Total elapsed time in execution of method callMethod() is :"+ (endTime-startTime)/1000+" secondes");
                    temps.setAccessibleText(String.valueOf((endTime-startTime)/1000));

                    DecimalFormat df = new DecimalFormat("0.00000000");
                    double dens = bd.getDensite();
                    densite.setText(String.valueOf(dens));

                    int taille_var = bd.getTaille();
                    taille.setText(String.valueOf(taille_var));
                    int ordre_var = bd.getOrdre();
                    ordre.setText(String.valueOf(ordre_var));
                    double diametre_var = bd.getDiametre();
                    diametre.setText(String.valueOf(diametre_var));


                   // Set<Map.Entry<String, Double>> page_r = bd.getPageRank(5);
                    //page_rk.setText(String.valueOf(page_r));

                   // Set<backEnd.CentralUser> page_r = bd.getPageRank(5);
                    //page_rk.setText(String.valueOf(page_r));


                    double meandegree = bd.getMeanDegree();
                    degre_moy.setText(String.valueOf(meandegree));
                    double meandegreein = bd.getMeanDegreeIn();
                    degre_moy_in.setText(String.valueOf(meandegreein));
                    double meandegreeout = bd.getMeanDegreeOut();
                    degre_moy_out.setText(String.valueOf(meandegreeout));



                    /*
                    this.page_rk_tab = new TableView<>();
                    page_r = FXCollections.observableArrayList(Map.entrySet());
                    TableColumn<Map.Entry<String, String>, String> PageRankUser = new TableColumn<Map.Entry<String, String>, String>();

                    PageRankUser.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, String>, String> vl) -> new SimpleStringProperty(vl.getValue().getValue()));
                    this.page_rk_tab.getColumns().setAll(PageRankUser);



                    // Create column UserName (Data type of String).
                    TableColumn<String, String> PageRankUser = new TableColumn<String, Double>("backEnd.User");
                    PageRankUser.setCellValueFactory(new PropertyValueFactory<>("backEnd.User"));

                    // Create column Email (Data type of String).
                    TableColumn<PageRank, Double> PageRankValue = new TableColumn<PageRank, Double>("value");
                    PageRankValue.setCellValueFactory(new PropertyValueFactory<>("Value"));

                    page_rk_tab.getColumns().addAll( PageRankUser, PageRankValue);
 */
                    TreeSet<CentralUser> pageR = bd.getPageRank(5);


                    /*
                    TableColumn<backEnd.Tweet, Long> idTweetCol = new TableColumn<backEnd.Tweet, Long>("ID");
                    idTweetCol.setCellValueFactory(new PropertyValueFactory<>("idTweet"));

                    // Create column Email (Data type of String).
                    TableColumn<backEnd.Tweet, String> userTweetCol = new TableColumn<backEnd.Tweet, String>("backEnd.User");
                    userTweetCol.setCellValueFactory(new PropertyValueFactory<>("idUser"));

                    // Create column FullName (Data type of String).
                    TableColumn<backEnd.Tweet, LocalDate> dateTweetCol = new TableColumn<backEnd.Tweet, LocalDate>("Date");
                    dateTweetCol.setCellValueFactory(new PropertyValueFactory<>("dateTweet"));
                    // Active Column
                    TableColumn<backEnd.Tweet, String> textTweetCol = new TableColumn<backEnd.Tweet, String>("Texte");
                    textTweetCol.setCellValueFactory(new PropertyValueFactory<>("textTweet"));

                    TableColumn<backEnd.Tweet, String> idReTweetCol = new TableColumn<backEnd.Tweet, String>("ReTweet");
                    idReTweetCol.setCellValueFactory(new PropertyValueFactory<>("idReTweet"));

                    page_rk_tab.getColumns().addAll(idTweetCol, userTweetCol, dateTweetCol, textTweetCol, idReTweetCol);


                     */
// Create column UserName (Data type of String).

                   // TableColumn<backEnd.CentralUser, String> userName = new TableColumn<backEnd.CentralUser, String>("userName");
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

                    // Create column Email (Data type of String).
                   // TableColumn<backEnd.CentralUser, Double> scorecol//
                     //       = new TableColumn<backEnd.CentralUser, Double>("score");



                    // Display row data
                    //ObservableList<pageRank> list = getUserList();
                    //page_rk_tab.setItems(list);



                    //System.out.println("A partir de la base de tweet :");
                   // System.out.println("Taille : "+bd.getTaille());
                    //System.out.println("Ordre : "+bd.getOrdre());
                    long time = System.currentTimeMillis();
                    GraphTweet.reportPerformanceFor("After affichage",time);
                /*}
                catch(IOException ioe) {
                    showAlert(Alert.AlertType.ERROR,"Read error","Problème de lecture du fichier"+ioe);
                }*/

        }
    private ObservableList<CentralUser> getNewsListPR(){
        List<CentralUser> list = new ArrayList<CentralUser> (bd.getPageRank(5));
        ObservableList<CentralUser> obs_list = FXCollections.observableList(list);
        return obs_list;
    }
    private ObservableList<CentralUser> getNewsListCentr(){
        List<CentralUser> list = new ArrayList<CentralUser> (bd.getDegreeCentrality(5));
        ObservableList<CentralUser> obs_list = FXCollections.observableList(list);
        return obs_list;
    }
/*

    private ObservableValue<backEnd.CentralUser> getUserList() {

        TreeSet<backEnd.CentralUser> users = bd.getPageRank(5);
        for (backEnd.CentralUser value : users){
            String user = value.getUserName();
            double score = value.getScore();
            ObservableList<? extends Serializable> list = FXCollections.observableArrayList(user, score);

    }

        return list;
            }


*/
    private static void JGraphTTOGraphStream(org.jgrapht.Graph<String, org.jgrapht.graph.DefaultWeightedEdge> dwGraph, double maxWeight){
        org.graphstream.graph.Graph g = new SingleGraph("Foot");
        g.setStrict(true);

        g.addAttribute("ui.antialias");
        g.addAttribute("ui.quality");
        g.addAttribute("ui.stylesheet", "node {size: 5px;size-mode: dyn-size;fill-color: BLUE;text-mode: hidden;z-index: 3;}edge {shape: line;fill-color: #222;arrow-size: 3px, 2px; size: 0px;}");

        double taille_max = 10;
        //Ajout des arêtes
        Set<DefaultWeightedEdge> edges = dwGraph.edgeSet();
        for(DefaultWeightedEdge dwe : edges){
            double weight = dwGraph.getEdgeWeight(dwe);
            String source = dwGraph.getEdgeSource(dwe);
            String target = dwGraph.getEdgeTarget(dwe);
            //Ajout du sommet source s'il n'est pas présent dans le graphe
            if (g.getNode(source) == null) {
                g.addNode(source);
                double sourceWeight = dwGraph.degreeOf(source);
                g.getNode(source).addAttribute("ui.size", (sourceWeight*taille_max)/maxWeight);
                System.out.println("Degré du sommet "+source+" est de : "+sourceWeight+" pixels");
            }
            //Ajout du sommet cible s'il n'est pas présent dans le graphe
            if(g.getNode(target) == null) {
                g.addNode(target);
                double targetWeight = dwGraph.degreeOf(target);
                g.getNode(target).addAttribute("ui.size", (targetWeight*taille_max)/maxWeight);
                System.out.println("Degré du sommet "+target+" est de : "+targetWeight+" pixels");
            }
            //Ajout de l'arête entre les deux sommets
            org.graphstream.graph.Edge e = g.addEdge(source+"|"+target,source,target,true);
            //Ajout du poid sur l'arête
            e.setAttribute("weight",weight);
        }
        Viewer view = g.display(true);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }


}