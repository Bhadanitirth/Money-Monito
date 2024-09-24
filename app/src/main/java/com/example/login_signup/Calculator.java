//package com.example.login_signup;
//
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import java.util.Stack;
//
//public class Calculator {
//
//    private EditText calculatorResult;
//    private EditText targetEditText; // The EditText to show the result in
//    private StringBuilder expression = new StringBuilder();
//
//    // Constructor to accept the EditText from the dialog and the target EditText
//    public Calculator(View dialogView, EditText targetEditText) {
//        this.calculatorResult = dialogView.findViewById(R.id.calculator_result);
//        this.targetEditText = targetEditText;
//
//        // Bind number buttons
//        bindNumberButton(dialogView, R.id.button_0);
//        bindNumberButton(dialogView, R.id.button_1);
//        bindNumberButton(dialogView, R.id.button_2);
//        bindNumberButton(dialogView, R.id.button_3);
//        bindNumberButton(dialogView, R.id.button_4);
//        bindNumberButton(dialogView, R.id.button_5);
//        bindNumberButton(dialogView, R.id.button_6);
//        bindNumberButton(dialogView, R.id.button_7);
//        bindNumberButton(dialogView, R.id.button_8);
//        bindNumberButton(dialogView, R.id.button_9);
//
//        // Bind operator buttons
//        bindOperatorButton(dialogView, R.id.button_plus);
//        bindOperatorButton(dialogView, R.id.button_minus);
//        bindOperatorButton(dialogView, R.id.button_multiply);
//        bindOperatorButton(dialogView, R.id.button_divide);
//
//        // Bind other buttons
//        dialogView.findViewById(R.id.button_equals).setOnClickListener(getEqualsClickListener());
//        dialogView.findViewById(R.id.button_clear).setOnClickListener(getClearClickListener());
//        dialogView.findViewById(R.id.button_dot).setOnClickListener(getDotClickListener());
//    }
//
//    // Helper method to bind number buttons
//    private void bindNumberButton(View dialogView, int buttonId) {
//        Button button = dialogView.findViewById(buttonId);
//        button.setOnClickListener(getNumberClickListener(button));
//    }
//
//    // Helper method to bind operator buttons
//    private void bindOperatorButton(View dialogView, int buttonId) {
//        Button button = dialogView.findViewById(buttonId);
//        button.setOnClickListener(getOperatorClickListener(button));
//    }
//
//    // Number click listener to append the number to the expression
//    private View.OnClickListener getNumberClickListener(Button button) {
//        return view -> {
//            expression.append(button.getText().toString());
//            calculatorResult.setText(expression.toString());
//        };
//    }
//
//    // Operator click listener to append the operator to the expression
//    private View.OnClickListener getOperatorClickListener(Button button) {
//        return view -> {
//            expression.append(button.getText().toString());
//            calculatorResult.setText(expression.toString());
//        };
//    }
//
//    // Equals click listener to evaluate the full expression and update the target EditText
//    private View.OnClickListener getEqualsClickListener() {
//        return view -> {
//            String result = evaluateExpression(expression.toString());
//            calculatorResult.setText(result);
//            expression.setLength(0);  // Clear the expression after evaluating
//            targetEditText.setText(result);  // Set the result to the target EditText
//        };
//    }
//
//    // Clear click listener to reset the expression
//    private View.OnClickListener getClearClickListener() {
//        return view -> {
//            expression.setLength(0);  // Clear the expression
//            calculatorResult.setText("0");
//        };
//    }
//
//    // Dot button click listener to add a decimal point
//    private View.OnClickListener getDotClickListener() {
//        return view -> {
//            if (!expression.toString().contains(".")) {
//                expression.append(".");
//                calculatorResult.setText(expression.toString());
//            }
//        };
//    }
//
//    // Method to evaluate the arithmetic expression
//    private String evaluateExpression(String expression) {
//        try {
//            return Double.toString(evaluate(expression));
//        } catch (Exception e) {
//            return "Error";
//        }
//    }
//
//    // Custom method to evaluate the expression
//    private double evaluate(String expression) {
//        Stack<Double> numbers = new Stack<>();
//        Stack<Character> operators = new Stack<>();
//
//        for (int i = 0; i < expression.length(); i++) {
//            char c = expression.charAt(i);
//
//            if (Character.isDigit(c) || c == '.') {
//                StringBuilder numberBuffer = new StringBuilder();
//                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
//                    numberBuffer.append(expression.charAt(i++));
//                }
//                i--;  // Decrement i because it will be incremented in the next iteration
//                numbers.push(Double.parseDouble(numberBuffer.toString()));
//            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
//                while (!operators.isEmpty() && hasPrecedence(c, operators.peek())) {
//                    numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
//                }
//                operators.push(c);
//            }
//        }
//
//        while (!operators.isEmpty()) {
//            numbers.push(applyOperator(operators.pop(), numbers.pop(), numbers.pop()));
//        }
//
//        return numbers.pop();
//    }
//
//    // Method to check operator precedence
//    private boolean hasPrecedence(char op1, char op2) {
//        if (op2 == '(' || op2 == ')') {
//            return false;
//        }
//        if ((op1 == '*' || op1 == '/') && (op2 == '+' || op2 == '-')) {
//            return false;
//        }
//        return true;
//    }
//
//    // Method to apply an operator to two numbers
//    private double applyOperator(char operator, double b, double a) {
//        switch (operator) {
//            case '+':
//                return a + b;
//            case '-':
//                return a - b;
//            case '*':
//                return a * b;
//            case '/':
//                if (b == 0) throw new ArithmeticException("Cannot divide by zero");
//                return a / b;
//        }
//        return 0;
//    }
//}
