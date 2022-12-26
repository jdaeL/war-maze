
import javax.swing.JOptionPane;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class initGame {

    @FXML
    void controles(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Hello, im supposed to show the controlls here");
    }

    @FXML
    void newGame(ActionEvent event) {
        JOptionPane.showMessageDialog(null, "Hello, im supposed to start the game here");
    }

    @FXML
    void salir(ActionEvent event) {
        System.exit(0);
    }

}
