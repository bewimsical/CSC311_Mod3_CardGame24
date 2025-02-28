package edu.farmingdale.csc311_mod3_cardgame24;

import javafx.scene.image.Image;

public class Card {
    private final int value;
    private final Image image;

    public Card(){
        value = 0;
        image = null;
    }
    public Card(String file){
        this.value = getCardValue(file);
        this.image = new Image(getClass().getResource("images/"+file).toExternalForm());
    }

    public int getValue(){
        return this.value;
    }

    public Image getImage(){
        return this.image;
    }

    private int getCardValue(String file){

        String[] data = file.split("_");
        String val = data[0];
        return switch(val){
            case "ace":
                yield 1;
            case "jack":
                yield 11;
            case "queen":
                yield 12;
            case "king":
                yield 13;
            default:
                if (val.matches("\\d+")){
                    yield Integer.parseInt(val);
                }else throw new IllegalArgumentException("invalid file type!");
        };

    }
}
