import java.util.ArrayList;
import java.io.*;

public class BottomUpParser{
    static ArrayList<String> stack = new ArrayList<>();
    static String[][] arrTable = {{"id", "+", "*", "(", ")", "$", "E", "T", "F"},
            {"S5", "", "", "S4", "", "", "1", "2", "3"},
            {"", "S6", "", "", "", "accept", "", "", ""},
            {"", "R2", "S7", "", "R2", "R2", "", "", ""},
            {"", "R4", "R4", "", "R4", "R4", "", "", ""},
            {"S5", "", "", "S4", "", "", "8", "2", "3"},
            {"", "R6", "R6", "", "R6", "R6", "", "", ""},
            {"S5", "", "", "S4", "", "", "", "9", "3"},
            {"S5", "", "", "S4", "", "", "", "", "10"},
            {"", "S6", "", "", "S11", "", "", "", ""},
            {"", "R1", "S7", "", "R1", "R1", "", "", ""},
            {"", "R3", "R3", "", "R3", "R3", "", "", ""},
            {"", "R5", "R5", "", "R5", "R5", "", "", ""},};
    static String[][] arrGrammer = {{"E", "E+T"},
            {"E", "T"},
            {"T", "T*F"},
            {"T", "F"},
            {"F", "(E)"},
            {"F", "id"},
    };

    public static void main(String[] args) {
        String formatStr = "%-30s %-30s %-30s%n";

        String input = args[0];
        String action = "";

        PrintWriter outputStream = null;
        try {

            outputStream = new PrintWriter(args[1]);

        } catch (FileNotFoundException e) {
            System.out.println("hata");
            System.exit(0);
        }
        push("0");
        boolean flag = false;
        String act = "";
        outputStream.println(String.format(formatStr, "Stack", "Input", "Action"));
        while (true) {
            flag = false;
            if (input.charAt(0) == 'i' && input.charAt(1) == 'd') {
                action = arrTable[top() + 1][0];
                flag = true;
            } else 
                action = arrTable[top() + 1][indexFound("" + input.charAt(0))];
            
            if (action.toUpperCase().equals("ACCEPT")) {
	System.out.println("The input has been parsed successfully.");
                outputStream.println(String.format(formatStr, stackToString(), input, "Accept"));
                break;
            }
            if (action.equals("")) {
	System.out.println("Error occurred.");
                outputStream.println(String.format(formatStr, stackToString(), input, "ERROR"));
                break;
            }
            if (action.contains("S")) {
                act = "Shift ";
                act += action.substring(1);
            } else if (action.contains("R")) {
                act = "Reduce ";
                act += action.substring(1);
            }
            outputStream.println(String.format(formatStr, stackToString(), input, act));
            if (action.contains("S")) {
                if (flag) {
                    push("id");
                    push(action.substring(1));
                    input = input.substring(2);
                } else {
                    push("" + input.charAt(0));
                    push(action.substring(1));
                    input = input.substring(1);
                }
            } else if (action.contains("R")) {
                act = "Reduce ";
                act += action.substring(1);
                while (!arrGrammer[Integer.parseInt(action.substring(1)) - 1][1].substring(0, 1).equals(stack.get(stack.size() - 1).substring(0, 1))) {
                    pop();
                }
                pop();
                int temp = top();
                push(arrGrammer[Integer.parseInt(action.substring(1)) - 1][0]);
                push(arrTable[temp + 1][indexFound(stack.get(stack.size() - 1))]);
            }

        }
        outputStream.close();
    }


    public static void push(String a) {
        stack.add(a);
    }

    public static String pop() {
        return stack.remove(stack.size() - 1);
    }

    public static int top() {
        return Integer.parseInt(stack.get(stack.size() - 1));
    }

    public static int indexFound(String str) {
        for (int i = 0; i < arrTable.length; i++)
            if (arrTable[0][i].equals(str))
                return i;
        return -1;
    }

    public static String stackToString() {
        String str = "";
        for (int i = 0; i < stack.size(); i++)
            str += stack.get(i);
        return str;
    }
}