
package frontEnd;

import backEnd.GraphTweet;
import backEnd.Importation;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Window;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.*;

public class LoadingController {
    //Elément graphique de la fenêtre de chargement
    @FXML
    private ProgressBar progressFileReader;
    @FXML
    private Button cancelButton;
    @FXML
    private Label statusLabel;

    public ProgressBar getProgressFileReader() {
        return progressFileReader;
    }

    public void setProgressFileReader(ProgressBar progressFileReader) {
        this.progressFileReader = progressFileReader;
    }

    public Button getCancelButton() {
        return cancelButton;
    }

    public void setCancelButton(Button cancelButton) {
        this.cancelButton = cancelButton;
    }

    public Label getStatusLabel() {
        return statusLabel;
    }

    public void setStatusLabel(Label statusLabel) {
        this.statusLabel = statusLabel;
    }
}
