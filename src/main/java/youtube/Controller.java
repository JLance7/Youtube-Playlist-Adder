package youtube;


import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.io.IOException;

public class Controller {
    @FXML
    TextField addToTextField;
    @FXML
    TextField vidFromTextField;
    @FXML
    Label statusLabel;
    @FXML
    Button runButton;

    Api run;
    private boolean running = false;

    public void initialize() throws Exception {
        run = new Api();
        run.startup();
        vidFromTextField.requestFocus();
    }

    public void runIt() throws Exception {
        if (running){
            return;
        }
        running = true;
        runButton.getStyleClass().remove("notClicked");
        runButton.getStyleClass().add("clicked");
        //get data and run
        String addToText = addToTextField.getText();
        String vidFromText = vidFromTextField.getText();
        statusLabel.setText("Running...");
        try{
            run.setGetVideosPlayListID(vidFromText);
            run.setAddToPlayListID(addToText);
            run.execute();
            statusLabel.setText("Finished");
        } catch (Exception e){
            statusLabel.setText("An error occurred");
            e.printStackTrace();
        } finally{
            vidFromTextField.setText("");
            runButton.setDisable(false);
            running = false;
            runButton.setOpacity(1);
            runButton.getStyleClass().remove("clicked");
            runButton.getStyleClass().add("notClicked");

        }

    }

    public void handle (KeyEvent e) throws Exception {
        if (e.getCode() == KeyCode.ENTER){
            runIt();
        }
    }


}
