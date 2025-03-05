package edu.farmingdale.csc311_mod3_cardgame24;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import javax.script.ScriptException;
import java.net.URL;
import java.util.List;
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

    @FXML
    private TextField expression;

    @FXML
    private Label expressionLabel;

    @FXML
    private TextField solution;

    private ImageView[] cardViews;

    private Deck deck;
    private int[] cards;
    private int cardValue1;
    private int cardValue2;
    private int cardValue3;
    private int cardValue4;
    private String solverScript;
    private int hintCount = 0;
    private String numSolutions = "0";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deck = new Deck();
        cardViews = new ImageView[]{cardView1, cardView2, cardView3, cardView4};
        dealCards();
        solverScript = Utils.getScript();
    }
    @FXML
    public void dealCards(){
        cards = new int[]{cardValue1, cardValue2, cardValue3, cardValue4};
        if(deck.getDeck().size() >= 4) {
            for (int i = 0; i < 4; i++) {
                Card c = deck.deal();
                cardViews[i].setImage(c.getImage());
                cards[i] = c.getValue();
            }
        }else deck = new Deck();
        expression.clear();
        expression.setPromptText("");
        expression.getStyleClass().removeAll("error", "solved");
        hintCount = 0;
        numSolutions = "0";
    }

    @FXML
    void evaluateExpression() {
        expressionLabel.requestFocus();
        String expressionIn = expression.getText();
        if(!isExpression(expressionIn)) {
            handleError("Invalid expression");
            return;
        }
        List<String> exp = Utils.parseExpression(expressionIn);
        int[] cardsCopy = {cards[0], cards[1], cards[2], cards[3]};
        int count = 0;
        for(String c:exp){
            if (c.matches("\\d+")){
                count ++;
                int num = Integer.parseInt(c);
                if(!isValid(num, cardsCopy)){
                    handleError("Numbers must match cards!");
                    return;
                }
            }
        }
        System.out.println(exp);

        double val = Utils.evaluateExpression(exp);
        if (val == 24 && count == 4){
            expression.getStyleClass().remove("error");
            expression.getStyleClass().add("solved");
            expression.setText(expression.getText() + " = 24");
        } else{

            if (val != 24) {
                handleError("not equal to 24");
            }else if(count != 4){
                handleError("Use all cards!");
            }
        }
    }
    boolean isValid(int num, int[]cards){
        for (int i = 0; i < cards.length; i++) {
            if(num == cards[i]) {
                cards[i] = 0;
                return true;
            }
        }
        return false;
    }
    boolean isExpression(String expression){
        if(expression.matches("([\\d+]|[*+\\-/() ])+")){
            return true;
        }
        return false;
    }
    void handleError(String message){
        expression.getStyleClass().removeAll("error", "solved");
        expression.getStyleClass().add("error");
        expression.clear();
        expression.setPromptText(message);
    }
    @FXML
    void findSolution() {
        //Get number of solutions
        String randomSolution = "";
        if (hintCount == 0 ){
            try {
                numSolutions = Utils.getSolutionsCount(cards, solverScript);
                hintCount++;
                System.out.println(numSolutions);
            } catch (ScriptException e) {
                System.out.println("ERROR BAD SCRIPT");
            }
        } else if (hintCount == 1 && Integer.parseInt(numSolutions) > 0) {
            try {
                randomSolution = Utils.getRandomSolution(cards, solverScript);
                System.out.println(randomSolution);
            } catch (ScriptException e) {
                System.out.println("ERROR BAD SCRIPT");;
            }
        }
    }
}