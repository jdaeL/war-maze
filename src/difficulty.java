import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class difficulty implements Initializable {

    @FXML
    private Label dific;

    @FXML
    private ChoiceBox<String> dificultad;
    private String[] difics = { "Fácil", "Medio", "Dificil" };

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    public void init(ActionEvent init) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("initGame.fxml"));
        stage = (Stage) ((Node) init.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void jugar(ActionEvent event) {
        // Change to GAME
        // --> CODE HERE <--
        Game game = new Game(); // EDIT THIS

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dificultad.getItems().addAll(difics);
        dificultad.setOnAction(this::getDific);
        // after this, the selected value is used and should init the game
        // <-- CODE HERE -->
    }

    public void getDific(ActionEvent event) {
        String diff = dificultad.getValue();
        dific.setText(diff);
    }

}
