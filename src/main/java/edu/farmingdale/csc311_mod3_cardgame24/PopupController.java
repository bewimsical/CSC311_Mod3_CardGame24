package edu.farmingdale.csc311_mod3_cardgame24;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class PopupController {

    @FXML
    private Label hint1;

    @FXML
    private Label hint2;

    public void setHint(String hint){
        hint1.setText(hint);
    }
    public void setHint(String hint, String otherHint){
        hint1.setText(hint);
        hint2.setText(otherHint);
    }

}
