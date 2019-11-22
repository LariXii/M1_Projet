import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
//import java.awt.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.MenuBar;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.stage.Window;

//https://o7planning.org/fr/11079/tutoriel-javafx-tableview
public class InterfaceNews extends Application {

	/**objets graphiques représentant un cercle*/
	public  Rectangle rectangle;
	private BooleanProperty isNotCreate = new SimpleBooleanProperty(true);
	private static BaseDeNews bd;

	/**definir la troupe des objets graphiques*/
	Group root;

	/**lancement de l'application*/
	public void start(Stage primaryStage) {
		construireScene(primaryStage);
	}

	/**construction des objets affichés*/
	void construireScene(Stage primaryStage) 
	{
		BorderPane root = new BorderPane();
		TableView<News> table = construireTableView();
		
		// Create MenuBar
		MenuBar menuBar = new MenuBar();

		// Create menus
		Menu fileMenu = new Menu("Fichier");
		Menu editMenu = new Menu("Edition");
		Menu helpMenu = new Menu("Aide");

		// Create MenuItems
		MenuItem newItem = new MenuItem("Créer");
		newItem.setOnAction((ActionEvent e) -> {
			if(isNotCreate.getValue()) {
				isNotCreate.set(false);
				bd = new BaseDeNews();
			}
			else {

			}
		});
		MenuItem openFileItem = new MenuItem("Ouvrir un fichier");
		MenuItem saveFileItem = new MenuItem("Enregistrer sous...");
		MenuItem exitItem = new MenuItem("Quitter");
		exitItem.setOnAction((ActionEvent t) -> {
			primaryStage.close();
		});

		MenuItem addNewItem = new MenuItem("Ajouter une nouvelle");
		addNewItem.setOnAction((ActionEvent t) -> {
			createFormAddNew(primaryStage,table);
		});

		MenuItem removeNewItem = new MenuItem("Supprimer une nouvelle");
		MenuItem searchNewItem = new MenuItem("Rechercher une nouvelle");
		MenuItem printBaseItem = new MenuItem("Afficher la base");

		// Add menuItems to the Menus
		fileMenu.getItems().add(newItem);
		fileMenu.getItems().add(openFileItem);
		fileMenu.getItems().add(saveFileItem);
		fileMenu.getItems().add(exitItem);

		editMenu.getItems().add(addNewItem);
		editMenu.getItems().add(removeNewItem);
		editMenu.getItems().add(searchNewItem);
		editMenu.getItems().add(printBaseItem);

		editMenu.disableProperty().bind(isNotCreate);
		saveFileItem.disableProperty().bind(isNotCreate);

		// Add Menus to the MenuBar
		menuBar.getMenus().addAll(fileMenu,editMenu,helpMenu);

		root.setTop(menuBar);
		root.setCenter(table);
		
		Scene scene = new Scene(root, 500, 500);

		primaryStage.setTitle("Mes nouvelles");
		primaryStage.setScene(scene);
		primaryStage.show();
		
		/****************************
		 * 		Evénements         * 
		 ***************************/
		
		openFileItem.setOnAction((ActionEvent t) -> {
			createFormOpenSaveFile(true,primaryStage,table);
		});
		
		saveFileItem.setOnAction((ActionEvent t) -> {
			createFormOpenSaveFile(false,primaryStage,table);
		});
	}
	
	private void createFormOpenSaveFile(boolean open, Stage primaryStage, TableView<News> table) {
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
		ButtonBar.setButtonData(addBut, ButtonData.OK_DONE);

		Button cancelBut = new Button("Annuler");
		ButtonBar.setButtonData(cancelBut, ButtonData.CANCEL_CLOSE);
		
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
		windowForm.setX(primaryStage.getX() + (primaryStage.getWidth()/2 - sceneForm.getWidth()/2));
		windowForm.setY(primaryStage.getY() + (primaryStage.getHeight()/2 - sceneForm.getHeight()/2));
		windowForm.show();
		
		addBut.setOnAction((ActionEvent e) -> {
			if(open) {
				try{
					bd = new BaseDeNews();
					bd.ouvrir(field_fichier.getText());
					updateDataTableView(table);
				}
				catch(FileNotFoundException fnd) {
					System.out.print("Le fichier n'a pas été trouvé"+fnd);
				}
				catch(IOException ioe) {
					System.out.print("Problème de lecture du fichier");
				}
				catch(ClassNotFoundException cnf) {
					System.out.print("Problème de lecture du fichier");
				}
			}
			else {
				try {
				bd.sauvegarder(field_fichier.getText());
				}
				catch(IOException ioe) {
					System.out.print("Le fichier n'existe pas");
				}
			}
		});
		
		cancelBut.setOnAction((ActionEvent e) -> {
			windowForm.close();
		});

	}
	
	private void createFormAddNew(Stage primaryStage,TableView<News> table) {
		
		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(25, 25, 25, 25));

		Label label_titre = new Label("Titre : ");
		grid.add(label_titre, 0, 1);

		TextField field_titre = new TextField();
		grid.add(field_titre, 1, 1);
		
		Label label_auteur = new Label("Auteur :");
		grid.add(label_auteur, 0, 2);

		TextField field_auteur = new TextField();
		grid.add(field_auteur, 1, 2);
		
		Label label_date = new Label("Date : ");
		grid.add(label_date, 0, 3);

		DatePicker field_date = new DatePicker();
		grid.add(field_date, 1, 3);
		
		Label label_url = new Label("Auteur :");
		grid.add(label_url, 0, 4);

		TextField field_url = new TextField();
		grid.add(field_url, 1, 4);

		ButtonBar buttonBar = new ButtonBar();

		Button addBut = new Button("Confirmer");
		ButtonBar.setButtonData(addBut, ButtonData.OK_DONE);

		Button cancelBut = new Button("Annuler");
		ButtonBar.setButtonData(cancelBut, ButtonData.CANCEL_CLOSE);
		
		// Add buttons to the ButtonBar
		buttonBar.getButtons().addAll(addBut, cancelBut);

		grid.add(buttonBar,1,5);
		
		Scene sceneForm = new Scene(grid, 300, 275);

		// New window (Stage)
		Stage windowForm = new Stage();
		windowForm.setTitle("Ajouter une nouvelle");
		windowForm.setScene(sceneForm);

		// Set position of second window, related to primary window.
		windowForm.setX(primaryStage.getX() + (primaryStage.getWidth()/2 - sceneForm.getWidth()/2));
		windowForm.setY(primaryStage.getY() + (primaryStage.getHeight()/2 - sceneForm.getHeight()/2));
		
		/*
		 * 				Evénements 
		 */
		addBut.setOnAction((ActionEvent e) -> {
			if(field_titre.getText().isEmpty()) {
                showAlert(Alert.AlertType.ERROR, windowForm, "Form Error!", "Veuillez renseigner un titre !");
                return;
            }
			News n = new Photo(field_titre.getText(),"Auteur",LocalDate.now(),"UneUrl","unephoto","Unformat",new Resolution(50,50),true);
			bd.ajouterNews(n);
			windowForm.close();
			updateDataTableView(table);
		});
		
		cancelBut.setOnAction((ActionEvent e) -> {
			windowForm.close();
		});

		windowForm.show();
	}

	private TableView<News> construireTableView() {
		TableView<News> table = new TableView<News>();

		// Create column UserName (Data type of String).
		TableColumn<News, String> titreCol = new TableColumn<News, String>("Titre");
		titreCol.setCellValueFactory(new PropertyValueFactory<>("titre"));

		// Create column Email (Data type of String).
		TableColumn<News, String> dateCol = new TableColumn<News, String>("Date");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

		// Create column FullName (Data type of String).
		TableColumn<News, String> auteurCol = new TableColumn<News, String>("Auteur");
		auteurCol.setCellValueFactory(new PropertyValueFactory<>("auteur"));
		// Active Column
		TableColumn<News, Boolean> urlCol = new TableColumn<News, Boolean>("URL");
		urlCol.setCellValueFactory(new PropertyValueFactory<>("url"));

		table.getColumns().addAll(auteurCol, titreCol, dateCol, urlCol);
		return table;
	}
	
	private void updateDataTableView(TableView<News> table) {
		ObservableList<News> list = getNewsList();
	    table.setItems(list);
	}
	
	private ObservableList<News> getNewsList(){
		List<News> list = new ArrayList<News> (bd.getBaseDeNews());
		ObservableList<News> obs_list = FXCollections.observableList(list);
	    return obs_list;
	}

	/**lancement du prog*/
	public static void main(String[] args) {
		launch(args);
	}
	
	private void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
