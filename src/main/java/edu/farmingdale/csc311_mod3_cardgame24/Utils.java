package edu.farmingdale.csc311_mod3_cardgame24;

import javax.script.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public final class Utils {

    private static final String USER_AGENT = "Mozilla/5.0";
    private static ScriptEngineManager manager = new ScriptEngineManager();

    //private static final String GET_URL = "https://api.openai.com/v1/chat/completions";

    //private static final String POST_URL = "https://api.openai.com/v1/chat/completions";

    //private static final String POST_PARAMS = "{\"model\": \"gpt-4o-mini\",\"store\": true,\"messages\": [{\"role\": \"user\", \"content\": \"write a haiku about ai\"}]}";

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

    public static String sendGET(String GET_URL) throws IOException {
        URL obj = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("User-Agent", USER_AGENT);
        //con.setRequestProperty("Authorization", "Bearer "+"");//TODO insert bearer token from env file
        //con.setRequestProperty("content-type", "application/json");
        con.setRequestProperty("content-type", "document");
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());

            return response.toString();
        } else {
            System.out.println("GET request did not work.");
            return "ERROR";
        }

    }

    public static void sendPOST(String POST_URL, String POST_PARAMS) throws IOException {
        URL obj = new URL(POST_URL);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("content-type", "application/json");

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("POST request did not work.");
        }
    }
    public static String getScript(){
        String url = "https://frank-deng.github.io/24game-solver/dist/24game-solver.js";
        try {
            String jsScript = sendGET(url);
            return jsScript;
        } catch (IOException e) {
            System.out.println("GET DIDNT WORK");
            return "";
        }
    }
    public static String getSolutionsCount(int[] nums, String jsScript) throws ScriptException {
        String data = "solve24game(["+nums[0]+","+nums[1]+","+nums[2]+","+nums[3]+"],24)";
        ScriptEngine e = manager.getEngineByName("js");
        CompiledScript script = ((Compilable) e).compile(jsScript+data+".length");
        return script.eval().toString();
    }

    public static String getRandomSolution(int[] nums, String jsScript) throws ScriptException {
        String data = "solve24game(["+nums[0]+","+nums[1]+","+nums[2]+","+nums[3]+"],24)";
        ScriptEngine e = manager.getEngineByName("js");
        CompiledScript script = ((Compilable) e).compile("let array="+jsScript+data+"[0]");
        return script.eval().toString();
    }
    public static String getRandomSolution(int[] nums, String jsScript, int num) throws ScriptException {
        String data = "solve24game(["+nums[0]+","+nums[1]+","+nums[2]+","+nums[3]+"],24)";
        ScriptEngine e = manager.getEngineByName("js");
        CompiledScript script = ((Compilable) e).compile("let array="+jsScript+data+"["+num+"]");
        return script.eval().toString();
    }
}
