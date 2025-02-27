module edu.farmingdale.csc311_mod3_cardgame24 {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.farmingdale.csc311_mod3_cardgame24 to javafx.fxml;
    exports edu.farmingdale.csc311_mod3_cardgame24;
}