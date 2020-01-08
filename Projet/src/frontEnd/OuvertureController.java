package frontEnd;

import backEnd.GraphTweet;
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
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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

    /****************************************************
     *              OUVERTURE DU FICHIER                *
     ****************************************************/
    @FXML
    private void ouvrir(MouseEvent e) throws IOException{

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Loading.fxml"));
        Parent loading_page_parent = loader.load();

        Scene loading_scene = new Scene(loading_page_parent);
        Stage window_parent = (Stage)((Node) e.getSource()).getScene().getWindow();
        if(isFolderNameValid()){
            window_parent.hide();
            window_parent.setScene(loading_scene);
            window_parent.setOnShown(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    OuvertureController ctrler = loader.getController();
                    ctrler.startLoading();
                }
            });
            System.out.println("Show");
            window_parent.show();
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
        return new File(file).length();
    }

    private void startLoading(){
        System.out.println(progressFileReader);
        System.out.println(cancelButton);
        progressFileReader.setProgress(0);
        cancelButton.setDisable(false);

        // Create a Task.
        GraphTweet graph = new GraphTweet();

        Task<Void> copyTask= graph.ouvrir("foot.txt");
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
                        statusLabel.textProperty().unbind();
                        System.out.println("Données chargées");
                    }
                });

        // Start the Task.
        new Thread(copyTask).start();
    }

    @FXML
    private void onCancelButton(ActionEvent e){
        System.out.println(progressFileReader);
        System.out.println(cancelButton);
    }
}
