import java.io.IOException;
import java.util.spi.ResourceBundleControlProvider;

import javax.swing.JOptionPane;

import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;   


public class sceneController {
    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void init(ActionEvent init) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("initGame.fxml"));
        stage = (Stage)((Node) init.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    } 

    @FXML
    public void salir(ActionEvent event) {
        int dialogButton = JOptionPane.YES_NO_OPTION;
        while (true) {
            int result = JOptionPane.showConfirmDialog (null, "¿Estas seguro que quieres salir?","WARNING", dialogButton);
            if (result == JOptionPane.YES_OPTION) {
                System.exit(0);
            } else if (result == JOptionPane.NO_OPTION) {
                break;
            }
        }
    }

    @FXML
    public void controls(ActionEvent init) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("showControls.fxml"));
        stage = (Stage)((Node) init.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    public void newGame(ActionEvent event) throws IOException {
        // Load the setDific.fxml layout
        Parent root = FXMLLoader.load(getClass().getResource("setDific.fxml"));
        stage = (Stage)((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        // Create an instance of the difficulty class and call its init() method
        difficulty dif = new difficulty();
        dif.init(event);
    }



}
