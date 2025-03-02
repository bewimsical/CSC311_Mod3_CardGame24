package edu.farmingdale.csc311_mod3_cardgame24;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class Utils {

    private Utils(){}

    // this method is used to parse the infix expression into a postfix expression.
    public static List<String> parseExpression(String expression){
        List<String> outputList = new ArrayList<>(); //create empty string for output
        String output = "";
        Stack<Character> operators = new Stack<>(); //create stack for operators
        char c;
        //iterate through the expression
        for (int i = 0; i < expression.length(); i++){
            c = expression.charAt(i);
            //check if the char is a letter. add letter to the output
            if(c == ' ') continue;
            else if (Character.isLetterOrDigit(c)){
                output += c;
            }
            // check if char is '('. add to the operators stack
            else if (c == '('){
                if(!output.isEmpty()) {
                    outputList.add(output);
                    output = "";
                }
                operators.push(c);
            }
            //check if char is ')'. pop from the operators stack and append to the output until '('
            else if (c == ')'){
                if(!output.isEmpty()) {
                    outputList.add(output);
                    output = "";
                }
                while(!operators.isEmpty() && operators.peek() != '('){
                    outputList.add(String.valueOf(operators.pop()));
                }
                operators.pop();
            }
            // check precedence of operators - pop off higher precedence ones into the output
            else{
                if(!output.isEmpty()) {
                    outputList.add(output);
                    output = "";
                }
                while (!operators.isEmpty() && getPrecedence(c) <= getPrecedence(operators.peek())){
                    outputList.add(String.valueOf(operators.pop()));
                }
                //add lower precedence operators to the stack
                operators.push(c);
            }
        }
        if(!output.isEmpty()) {
            outputList.add(output);
            output = "";
        }
        //pop all remaining operators from the operators stack into the output
        while(!operators.isEmpty()){
            outputList.add(String.valueOf(operators.pop()));
        }
        return outputList;
    }

    public static int getPrecedence(char o){
        if (o == '+' || o == '-')
            return 1;
        else if (o == '*' || o == '/')
            return 2;
        else
            return -1;
    }
    public static double evaluateExpression(List<String> expression){
        Stack<Double> stack  = new Stack<>();
        double o;
        for(String c: expression){
            if (c.matches("-?\\d+")){
                o = Double.parseDouble(c);
                stack.push(o);
            }
            else{
                System.out.println(stack);
                double val1 = stack.pop();
                double val2 = stack.pop();
                switch (c){
                    case "+":
                        System.out.println(val2 +"+"+val1);
                        System.out.println(val2+val1);
                        stack.push(val2 + val1);
                        System.out.println(stack);


                        break;
                    case "-":
                        System.out.println(val2 +"-"+val1);
                        System.out.println(val2-val1);
                        stack.push(val2 - val1);
                        System.out.println(stack);
                        break;
                    case "*":
                        System.out.println(val2 +"*"+val1);
                        System.out.println(val2*val1);
                        stack.push(val2 * val1);
                        System.out.println(stack);
                        break;
                    case "/":
                        System.out.println(val2 +"/"+val1);
                        System.out.println(val2/val1);

                        stack.push(val2 / val1);
                        System.out.println(stack);
                        break;
                    default:
                        System.out.println("invalid operator");
                        break;
                }
            }
        }
        return stack.pop();
    }

    public static String getSolution(){
        return "";
    }
}
