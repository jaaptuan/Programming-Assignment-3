package Assignment3;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class PostFix {
    public static void main(String[] args) throws IOException {
        try {
            Scanner filePath = new Scanner(System.in);
            String path = filePath.nextLine();
            File f = new File(path);
            Scanner readFile = new Scanner(f);
            while (readFile.hasNext()) { // loop through each line in the file
                String line = readFile.nextLine();
                if (isValidInfix(line)) {
                    System.out.println("Valid\n" + conversion(line).toString().trim());
                } else {
                    System.out.println("Not-Valid");
                }
            }
            
        } catch (IOException e) {
            System.out.println("Error! " + " " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean isValidInfix(String expression) {
        expression = expression.replaceAll("\\s+", ""); // remove all spaces
        int openParenthesis = 0;
        boolean wasOperator = true;

        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);

            if (Character.isLetterOrDigit(c)) {
                wasOperator = false;
            } else if (c == '(') {
                openParenthesis++;
                wasOperator = true;
            } else if (c == ')') {
                if (openParenthesis == 0) {
                    return false; // mismatched closing parenthesis
                }
                openParenthesis--;
                wasOperator = false;
            } else if (isOperator(c)) {
                if (wasOperator) {
                    return false; // consecutive operators or operators at the beginning
                }
                wasOperator = true; // after an operator, we should expect an operand or parenthesis
            } else {
                return false; // invalid char
            }
        }

        if (openParenthesis != 0) {
            return false; // unbalanced parentheses
        }

        return !wasOperator; // expression should not end with an operator
    }

    public static boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    public static StringBuilder conversion(String e) {
        StringBuilder output = new StringBuilder();
        LinkedList ll = new LinkedList();

        for (int i = 0; i < e.length(); i++) {
            char c = e.charAt(i);

            if (isOperand(c)) {
                output.append(c); // operand appended to output
            } else if (c == '(') {
                ll.push(c); // push '(' to stack
            } else if (c == ')') {
                while (!ll.isEmpty() && ll.peek() != '(') {
                    output.append(ll.pop()); // pop until stack is left with left parenthesis
                }
                ll.pop(); // get rid of '('
            } else if (isOperator(c)) { // for operators
                while (!ll.isEmpty() && precedence(c) <= precedence(ll.peek()) && isLeftAssociative(c)) {
                    output.append(ll.pop());
                }
                ll.push(c); // push current operator to stack
            }
        }

        // Pop all remaining operators from the stack
        while (!ll.isEmpty()) {
            output.append(ll.pop());
        }
        return output;
    }

    public static boolean isOperand(char c) {
        return Character.isLetterOrDigit(c);
    }

    public static int precedence(char c) {
        switch (c) {
            case '+':
            case '-':
                return 1;
            case '*':
            case '/':
                return 2;
            case '^':
                return 3;
            default:
                return -1;
        }
    }

    public static boolean isLeftAssociative(char c) {
        return (c != '^'); // ^ is right-associative rest are left-associative
    }
}

class Node {
    char data;
    Node next;

    public Node(char data) {
        this.data = data;
        this.next = null;
    }
}

class LinkedList {
    private Node top;

    public LinkedList() {
        this.top = null;
    }

    public void push(char data) {
        Node newNode = new Node(data);
        newNode.next = top;
        top = newNode;
    }

    public char pop() {
        if (top == null) {
            return '\0'; // return null character if stack is empty
        }
        char poppedData = top.data;
        top = top.next;
        return poppedData;
    }

    public char peek() {
        if (top == null) {
            System.out.println("Stack is empty.");
            return '\0'; // return null character if stack is empty
        }
        return top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }

    public void clear() {
        top = null;
    }

    public void display() {
        Node current = top;
        if (current == null) {
            System.out.println("Linked List is empty");
        }

        while (current != null) {
            System.out.print(current.data + " -> ");
            current = current.next;
        }
    }
    
}
