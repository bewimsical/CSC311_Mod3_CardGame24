package edu.farmingdale.csc311_mod3_cardgame24;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {
    @FXML
    private ImageView cardView1;

    @FXML
    private ImageView cardView2;

    @FXML
    private ImageView cardView3;

    @FXML
    private ImageView cardView4;

    private ImageView[] cardViews;

    private Deck deck;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deck = new Deck();
        cardViews = new ImageView[]{cardView1, cardView2, cardView3, cardView4};
        dealCards();
    }
    @FXML
    public void dealCards(){
        if(deck.getDeck().size() >= 4) {
            for (int i = 0; i < 4; i++) {
                Card c = deck.deal();
                cardViews[i].setImage(c.getImage());
            }
        }else deck = new Deck();
    }
}