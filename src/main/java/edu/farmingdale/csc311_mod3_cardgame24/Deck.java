package edu.farmingdale.csc311_mod3_cardgame24;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Deck {
    private List<Card> deck;
    private Random rnd;

    public Deck(){
        deck = new ArrayList<>();
        rnd = new Random();

        String[] suits = {"diamonds", "spades", "clubs", "hearts"};
        String[] faces = {"ace", "queen", "king", "jack"};

        for (int i = 2; i < 11; i++) {
            for( String suit: suits){
                String fileName = i + "_of_"+suit+".png";
                Card c = new Card(fileName);
                deck.add(c);
            }
        }
        for(String face: faces){
            for( String suit: suits){
                String fileName = face + "_of_"+suit+".png";
                Card c = new Card(fileName);
                deck.add(c);
            }
        }
    }

    public List<Card> getDeck(){
        return deck;
    }

    public Card deal(){
        int index = rnd.nextInt(deck.size());
        return deck.remove(index);
    }



}
