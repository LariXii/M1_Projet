package frontEnd;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class OuvertureController {

    @FXML
    private TextField textName;
    @FXML
    private Button buttonOpen;

    public void ouvrir(MouseEvent e) throws IOException{
        Parent loading_page_parent = FXMLLoader.load(getClass().getResource("Loading.fxml"));
        Scene loading_scene = new Scene(loading_page_parent);
        Stage window_parent = (Stage)((Node) e.getSource()).getScene().getWindow();
        System.out.println(isFolderNameValid());
        if(isFolderNameValid()){
            window_parent.hide();
            window_parent.setScene(loading_scene);
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
}
