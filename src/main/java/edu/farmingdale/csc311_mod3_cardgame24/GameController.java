package edu.farmingdale.csc311_mod3_cardgame24;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.script.ScriptException;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class GameController implements Initializable {
    @FXML private ImageView cardView1;
    @FXML private ImageView cardView2;
    @FXML private ImageView cardView3;
    @FXML private ImageView cardView4;
    @FXML private TextField expression;
    @FXML private Label expressionLabel;
    @FXML private TextField solution;

    private Deck deck;
    private ImageView[] cardViews;
    private int[] cards;

    private String solverScript;

    private int hintCount ;
    private int solutionCount;
    private String numSolutions;
    private String randomSolution;


    /**
     *
     * @param url
     * The location used to resolve relative paths for the root object, or
     * {@code null} if the location is not known.
     *
     * @param resourceBundle
     * The resources used to localize the root object, or {@code null} if
     * the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        randomSolution ="";
        deck = new Deck();
        cardViews = new ImageView[]{cardView1, cardView2, cardView3, cardView4};
        cards = new int[4];
        solverScript = Utils.getScript();
        dealCards();
    }

    /**
     * This method gets 4 card objects from the deck, sets the image views to the cards image value,
     * populates the cards array with the 4 card values, and resets data members associated with a round
     */
    @FXML
    public void dealCards(){
        if(deck.getDeck().size() < 4) {
            deck = new Deck();
        }
        for (int i = 0; i < 4; i++) {
            Card c = deck.deal();
            cardViews[i].setImage(c.getImage());
            cards[i] = c.getValue();
        }

        expression.clear();
        expression.setPromptText("");
        expression.getStyleClass().removeAll("error", "solved");
        hintCount = 0;
        solutionCount = 0;
        numSolutions = "0";


    }

    /**
     * This method validates
     */
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

    /**
     * this method checks if the digits in the expression match the cards
     * @param num digit from expression
     * @param cards array containing card values
     * @return true if the value matches a card
     */
    boolean isValid(int num, int[]cards){
        for (int i = 0; i < cards.length; i++) {
            if(num == cards[i]) {
                cards[i] = 0;
                return true;
            }
        }
        return false;
    }

    /**
     * This method checks id the expression uses the correct format
     * @param expression
     * @return True if the expression has the correct format
     */
    boolean isExpression(String expression){
        if(expression.matches("([\\d+]|[*+\\-/() ])+")){
            return true;
        }
        return false;
    }

    /**
     * This method adds error styling to the expression text field
     * @param message the error message
     */
    void handleError(String message){
        expression.getStyleClass().removeAll("error", "solved");
        expression.getStyleClass().add("error");
        expression.clear();
        expression.setPromptText(message);
    }

    /**
     * This method uses a script to get the number of possible solutions.
     * if there are solutions, it will populate the text field with a solution.
     * cycles through possible solutions with each call
     */
    @FXML
    void findSolution() {
        //Get number of solutions
        expression.getStyleClass().removeAll("error", "solved");
        if(solutionCount == 0){
            try {
                numSolutions = Utils.getSolutionsCount(cards, solverScript);
                System.out.println(numSolutions);
            } catch (ScriptException e) {
                System.out.println("ERROR BAD SCRIPT");
            }
        }
        if(Integer.parseInt(numSolutions) > 0){
            try {
                randomSolution = Utils.getRandomSolution(cards, solverScript, solutionCount%Integer.parseInt(numSolutions));
                System.out.println(randomSolution);
                expression.setText(randomSolution);
                solutionCount++;
            } catch (ScriptException e) {
                System.out.println("ERROR BAD SCRIPT");;
            }
            return;
        }
        expression.setText("no solutions");
    }

    /**
     * clears the expression text field
     * @param event
     */
    @FXML
    void clearExpression(MouseEvent event) {
        expression.getStyleClass().removeAll("error", "solved");
        expression.clear();
    }

    /**
     * This method creates a new window that displays a hint.
     * The first press will show the number of solutions
     * additional presses will show the solution with the numbers removed
     * @param event
     * @throws IOException
     */
    @FXML
    void getHint(MouseEvent event) throws IOException {
        try {
            numSolutions = Utils.getSolutionsCount(cards, solverScript);
            System.out.println(numSolutions);
        } catch (ScriptException e) {
            System.out.println("ERROR BAD SCRIPT");
        }
        if (Integer.parseInt(numSolutions) > 0) {
            try {
                randomSolution = Utils.getRandomSolution(cards, solverScript);
                System.out.println(randomSolution);
            } catch (ScriptException e) {
                System.out.println("ERROR BAD SCRIPT");;
            }
        }
        String hint1 = Integer.parseInt(numSolutions) == 1 ? "There is "+numSolutions+" solution":"There are "+numSolutions+" solutions";
        String hint2 = randomSolution.replaceAll("[\\d]", "_");
        hint2 = hint2.replaceAll("(__)", "_");
        System.out.println(hint2);
        //create new window
        Stage primaryStage = (Stage) expression.getParent().getScene().getWindow();
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(primaryStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("hint-view.fxml"));
        Scene popUpScene = new Scene(fxmlLoader.load(), 300,150);
        popUp.setScene(popUpScene);
        popUp.setTitle("Hints");
        popUp.setX(primaryStage.getX() + ((primaryStage.getWidth() - 300)/2 ));
        popUp.setY(primaryStage.getY() + 30);
        popUp.show();
        HintController pc = fxmlLoader.getController();
        System.out.println(hintCount);
        if (hintCount == 0){
            pc.setHint(hint1);
            hintCount++;
        }else{
            pc.setHint(hint1, hint2);
        }

    }

    /**
     * This method creates a new window displaying the rules of the game
     * @param event
     * @throws IOException
     */
    @FXML
    void getRules(MouseEvent event) throws IOException{
        System.out.println("getting rules");
        Stage primaryStage = (Stage) expression.getParent().getScene().getWindow();
        //popup
        Stage popUp = new Stage();
        popUp.initModality(Modality.APPLICATION_MODAL);
        popUp.initOwner(primaryStage);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("rules-view.fxml"));
        Scene popUpScene = new Scene(fxmlLoader.load(), 450,350);
        popUp.setScene(popUpScene);
        popUp.setTitle("Rules");
        popUp.setX(primaryStage.getX() + ((primaryStage.getWidth() - 450)/2 ));
        popUp.setY(primaryStage.getY() + ((primaryStage.getHeight() - 350)/2 ));
        popUp.show();
    }
}