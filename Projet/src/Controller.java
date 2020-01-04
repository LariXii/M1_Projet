import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.Viewer;
import org.jgrapht.Graph;
import org.jgrapht.alg.scoring.PageRank;
import org.jgrapht.graph.AsSubgraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.IOException;
import java.sql.ResultSet;
import java.time.LocalDate;
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
    @FXML
    private Label page_rk;
    @FXML
    private Label degre_moy;
    @FXML
    private Label degre_moy_in;
    @FXML
    private Label degre_moy_out;
    @FXML
    private Label centralite;
    @FXML
    private TableView<pageRank> page_rk_tab;
    @FXML
    private TableColumn<PageRank, String> page_rk_user;
    @FXML
    private AnchorPane jgraph;

    @FXML
    private void ouvrir() {

        createFormOpenSaveFile(true,canvas);
        System.out.print("ic ");

    }
    private ObservableList<PageRank> pageRankData = FXCollections.observableArrayList();


    //objets graphiques représentant un cercle

    public  Rectangle rectangle;
    private BooleanProperty isNotCreate = new SimpleBooleanProperty(true);
    private static BaseDeTweets bd;



//definir la troupe des objets graphiques

    Group root;
    Pane canvas = new Pane();
    public void start(Stage primaryStage) {
        construireScene(primaryStage);
    }

    void construireScene(Stage primaryStage) {

        Scene scene = new Scene(root, 500, 500);

        primaryStage.setScene(scene);
        primaryStage.show();
    }



    private void createFormOpenSaveFile(boolean open, Pane p) {

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

        // Set position of second window, related to primary window.
        //windowForm.setX(primaryStage.getX() + (primaryStage.getWidth()/2 - sceneForm.getWidth()/2));
        //windowForm.setY(primaryStage.getY() + (primaryStage.getHeight()/2 - sceneForm.getHeight()/2));
        windowForm.show();

        addBut.setOnAction((ActionEvent e) -> {
            if(open) {
                try{
                    bd = new BaseDeTweets();
                    long startTime = System.currentTimeMillis();
                    //ArrayList<Integer> lErr = bd.ouvrir(field_fichier.getText());
                    bd.ouvrir(field_fichier.getText());
                    long endTime = System.currentTimeMillis();
                    Graph<String, DefaultWeightedEdge> g = bd.getDirectedWeightedGraph();
                    int numVertex = 0;
                    int i =1;
                    for(String s : g.vertexSet()){
                        float total = 0;
                        for(DefaultWeightedEdge dwe :g.incomingEdgesOf(s)){
                            total += g.getEdgeWeight(dwe);
                        }
                        Circle circle = createCircle(total,s);
                        double x = 0 + i * circle.getRadius();
                        double y = 50;
                        circle.relocate(x,y);
                        numVertex++;
                        if(total > 3000 && i < 50){
                            jgraph.getChildren().add(circle);
                            i++;
                        }
                    }

                    System.out.println("Total elapsed time in execution of method callMethod() is :"+ (endTime-startTime)/1000+" secondes");
                    /*if(lErr.size() != 0){
                        showAlert(Alert.AlertType.ERROR,primaryStage,"Read error","Les lignes suivantes sont au mauvais format "+lErr);
                    }
                    updateDataTableView(table);*/

                    DecimalFormat df = new DecimalFormat("0.00000000");
                    double dens = bd.getDensite();
                    densite.setText(String.valueOf(dens));

                    int taille_var = bd.getTaille();
                    taille.setText(String.valueOf(taille_var));
                    int ordre_var = bd.getOrdre();
                    ordre.setText(String.valueOf(ordre_var));
                    double diametre_var = bd.getDiametre();
                    diametre.setText(String.valueOf(diametre_var));
                    Set<Map.Entry<String, Double>> page_r = bd.getPageRank(5);
                    page_rk.setText(String.valueOf(page_r));

                    double meandegree = bd.getMeanDegree();
                    degre_moy.setText(String.valueOf(meandegree));
                    double meandegreein = bd.getMeanDegreeIn();
                    degre_moy_in.setText(String.valueOf(meandegreein));
                    double meandegreeout = bd.getMeanDegreeOut();
                    degre_moy_out.setText(String.valueOf(meandegreeout));
                    Set<Map.Entry<String, Double>> centr = bd.getDegreeCentrality(5);
                    centralite.setText(String.valueOf(centr));


                    /*
                    this.page_rk_tab = new TableView<>();
                    page_r = FXCollections.observableArrayList(Map.entrySet());
                    TableColumn<Map.Entry<String, String>, String> PageRankUser = new TableColumn<Map.Entry<String, String>, String>();

                    PageRankUser.setCellValueFactory((TableColumn.CellDataFeatures<Map.Entry<String, String>, String> vl) -> new SimpleStringProperty(vl.getValue().getValue()));
                    this.page_rk_tab.getColumns().setAll(PageRankUser);


                    // Create column UserName (Data type of String).
                    TableColumn<String, String> PageRankUser = new TableColumn<String, Double>("User");
                    PageRankUser.setCellValueFactory(new PropertyValueFactory<>("User"));

                    // Create column Email (Data type of String).
                    TableColumn<PageRank, Double> PageRankValue = new TableColumn<PageRank, Double>("value");
                    PageRankValue.setCellValueFactory(new PropertyValueFactory<>("Value"));

                    page_rk_tab.getColumns().addAll( PageRankUser, PageRankValue);
 */


                    /*
                    TableColumn<Tweet, Long> idTweetCol = new TableColumn<Tweet, Long>("ID");
                    idTweetCol.setCellValueFactory(new PropertyValueFactory<>("idTweet"));

                    // Create column Email (Data type of String).
                    TableColumn<Tweet, String> userTweetCol = new TableColumn<Tweet, String>("User");
                    userTweetCol.setCellValueFactory(new PropertyValueFactory<>("idUser"));

                    // Create column FullName (Data type of String).
                    TableColumn<Tweet, LocalDate> dateTweetCol = new TableColumn<Tweet, LocalDate>("Date");
                    dateTweetCol.setCellValueFactory(new PropertyValueFactory<>("dateTweet"));
                    // Active Column
                    TableColumn<Tweet, String> textTweetCol = new TableColumn<Tweet, String>("Texte");
                    textTweetCol.setCellValueFactory(new PropertyValueFactory<>("textTweet"));

                    TableColumn<Tweet, String> idReTweetCol = new TableColumn<Tweet, String>("ReTweet");
                    idReTweetCol.setCellValueFactory(new PropertyValueFactory<>("idReTweet"));

                    page_rk_tab.getColumns().addAll(idTweetCol, userTweetCol, dateTweetCol, textTweetCol, idReTweetCol);


                     */
// Create column UserName (Data type of String).


                    TableColumn<pageRank, String> userNameCol //
                            = new TableColumn<pageRank, String>("User Name");

                    // Create column Email (Data type of String).
                    TableColumn<pageRank, Double> scorecol//
                            = new TableColumn<pageRank, Double>("score");



                    // Add sub columns to the FullName
                    page_rk_tab.getColumns().addAll(userNameCol, scorecol);



                    // Defines how to fill data for each cell.
                    // Get value from property of UserAccount. .
                    userNameCol.setCellValueFactory(new PropertyValueFactory<>("userName"));
                    scorecol.setCellValueFactory(new PropertyValueFactory<>("score"));


                    // Set Sort type for userName column
                    userNameCol.setSortType(TableColumn.SortType.DESCENDING);
                    scorecol.setSortable(false);

                    // Display row data
                    //ObservableList<pageRank> list = getUserList();
                    //page_rk_tab.setItems(list);

                    page_rk_tab.getColumns().addAll(userNameCol, scorecol);






                    Set<Map.Entry<String, Double>> pageR = bd.getPageRank(5);
                   // page_rk_user.setCellValueFactory();





                    for (Map.Entry<String, Double> it: pageR) {
                        String user = it.getKey();
                        double info = it.getValue();


                    }






                    //System.out.println("A partir de la base de tweet :");
                   // System.out.println("Taille : "+bd.getTaille());
                    //System.out.println("Ordre : "+bd.getOrdre());
                    long time = System.currentTimeMillis();
                  /*  System.out.println("A partir de la base du graph :");
                    System.out.println("Densité : "+bd.getDensite());
                    System.out.println("Taille : "+bd.getTaille());
                    System.out.println("Ordre : "+bd.getOrdre());
                    System.out.println("Diamètre : "+bd.getDiametre());
                    System.out.println("Page Rank : "+bd.getPageRank(5));
                    System.out.println("Degre Moyen : "+bd.getMeanDegree());
                    System.out.println("Degre Moyen In : "+bd.getMeanDegreeIn());
                    System.out.println("Degre Moyen Out : "+bd.getMeanDegreeOut());
                    System.out.println("Centralité par degré : "+bd.getDegreeCentrality(5));*/
                    BaseDeTweets.reportPerformanceFor("After affichage",time);
                }
                catch(IOException ioe) {
                    showAlert(Alert.AlertType.ERROR,"Read error","Problème de lecture du fichier"+ioe);
                }
            }
            windowForm.close();
        });

        cancelBut.setOnAction((ActionEvent e) -> {
            windowForm.close();
        });

    }
/*
    private ObservableList<pageRank> getUserList() {

        Set<Map.Entry<String, Double>> users = bd.getPageRank(5);

        for (Map.Entry<String, Double> it: users) {
            String user = it.getKey();
            double info = it.getValue();
            ObservableList<pageRank> list = FXCollections.observableArrayList(user,info);

        }

        return list;
            }

 */

    private Circle createCircle(float poids, String name){
        float radius = 1.0f;
        Circle circle = new Circle();
        if(poids > 0){
            radius *= Math.sqrt(poids)/3;
        }
        circle.setRadius(radius);
        circle.setFill(Color.BLUE);
        circle.setAccessibleText(name);
        return circle;
    }

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