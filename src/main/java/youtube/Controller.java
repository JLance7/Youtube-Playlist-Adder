package youtube;


import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class Controller {
    @FXML
    TextField addToTextField;
    @FXML
    TextField vidFromTextField;
    @FXML
    Label statusLabel;

    Api run;

    public void initialize() throws Exception {
        run = new Api();
        run.startup();
    }

    public void runIt() throws Exception {
        statusLabel.setText("");
        String addToText = addToTextField.getText();
        String vidFromText = vidFromTextField.getText();
        run.setGetVideosPlayListID(vidFromText);
        statusLabel.setText("Running...");
        run.setAddToPlayListID(addToText);
        run.execute();
        statusLabel.setText("Finished");
        vidFromTextField.setText("");
    }
}
