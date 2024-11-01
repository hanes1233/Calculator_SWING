import javax.swing.*;
public class Logic implements Operations{

    // Send user's input to specific methods/operations to calculate result
    // Set result of equations as output after all necessary modifications
    protected String calculateEquation(JTextField rawText) {
        // Creating copy to manipulate with
        JTextField equation = rawText;
        // Creating conditions to test input for
        boolean isContainsPriorityOperators = equation.getText().contains("*") || equation.getText().contains("/");
        boolean isContainsParentheses = equation.getText().contains("(");

        // Test input for containing parentheses and "*" or "/" operators
        equation = isContainsParentheses ? executeParentheses(equation) : equation;
        equation = isContainsPriorityOperators ? checkPriorityOperators(equation) : equation;

        // Calculate + and - operators
        double equationsResult = calculateSimpleOperators(equation);

        // Test input for containing special words
        if (equation.getText().contains("round")) {
            return String.valueOf(round(equation.getText()));
        } else if (equation.getText().contains("mod")) {
            return String.valueOf(findModulo(equation.getText()));
        }
        // Transforming double result into int (depends)
        if ((equationsResult == Math.floor(equationsResult)) && !Double.isInfinite(equationsResult)) {
            int intResult = (int)Math.rint(equationsResult);
            return String.valueOf(intResult);
        }else {
            return String.valueOf(equationsResult);
        }
    }

    // Calculate equations inside parentheses
        private JTextField executeParentheses(JTextField equation) {
            JTextField equationCopy = new JTextField(equation.getText());
            try{
                // Define variable with closing index of parentheses
              int closingIndex = 0;

              // Loop will execute while input still contains parentheses symbol
              while(equationCopy.getText().contains(")")) {

                  // Define
                String notModifiedEquationInParenthese;
                String equationInsideParentheses;

                // Check if closingIndex is no longer then input, if yes, set it value to 0
                closingIndex = closingIndex >= equationCopy.getText().length() ? 0 : closingIndex;

                // Looking for last closing-parentheses index, if not found, increment closingIndex on 1
                    if(equationCopy.getText().charAt(closingIndex) == ')') {
                        for(int openingIndex = closingIndex-1; openingIndex >= 0; openingIndex--) {

                            // Looking for last opening-parentheses index pair to our closing-index
                            if(equationCopy.getText().charAt(openingIndex) == '(') {
                                // When opening and closing indexes are found, substring equations with open and closing indexes into notModifiedParentheses variable

                                    // Check if closing-index (closing parentheses) is not last symbol in input
                                    // If it is, we're leaving closingIndex without changes to avoid IndexOutOfBond exceptions
                                    if(closingIndex == equationCopy.getText().length()) {
                                        notModifiedEquationInParenthese = equationCopy.getText().substring(openingIndex,closingIndex);

                                        // If not, increment closingIndex on 1 to get next symbol
                                    }else {
                                        notModifiedEquationInParenthese = equationCopy.getText().substring(openingIndex,closingIndex+1);
                                    }

                                    /* Set clear equation to calculate:
                                    // openingIndex symbol '(' avoided by incrementing openingIndex by one,
                                    // closingIndex ')' replaced with white space, after - white space trimmed
                                     */
                                    equationInsideParentheses = equationCopy.getText().substring(openingIndex+1,closingIndex).replaceAll("\\)","").trim();

                                    // Test equation we've got from parentheses for containing following operators
                                    if(equationInsideParentheses.contains("%")) {
                                        equationInsideParentheses = calculatePercentage(splitToArray(new JTextField(equationInsideParentheses), "\\+","\\-","\\*","\\/"));
                                    }else if(equationInsideParentheses.contains("*") || equationInsideParentheses.contains("/")) {
                                        equationInsideParentheses = calculatePriorityOperators(new JTextField(equationInsideParentheses));
                                        if(equationInsideParentheses.contains("+") || equationInsideParentheses.contains("-")) {
                                            equationInsideParentheses = String.valueOf(calculateSimpleOperators(new JTextField(equationInsideParentheses)));
                                        }
                                    }else if(equationInsideParentheses.contains("+") || equationInsideParentheses.contains("-")) {
                                        equationInsideParentheses = String.valueOf(calculateSimpleOperators(new JTextField(equationInsideParentheses)));
                                    }

                                    // Modify input with replacing equation in parentheses with calculated equation's result
                                    equationCopy.setText(equationCopy.getText().replace(notModifiedEquationInParenthese,equationInsideParentheses));
                                    break;
                            }
                        }
                    }
                closingIndex++;
            }
        }catch (NumberFormatException e) {
            System.out.println("Exception caught at checkParentheses method");
        }

            /* Set final version of input with all calculated and replaced equations
            // We're also test input for containing "(" tags and replacing it if found
             */
         equationCopy.setText(equationCopy.getText().replaceAll("\\(","").trim());
        return equationCopy;
    }
    
    
//Priority operators executing
private JTextField checkPriorityOperators(JTextField input) {
        try {
            // Check input for following operators and return result of appropriate operations
            if(input.getText().contains("%")) {
                return new JTextField(calculatePercentage(splitToArray(input, "\\+", "\\-", "\\*", "\\/")));
            }
            if (input.getText().contains("-") || input.getText().contains("+")) {
                return new JTextField(String.valueOf(calculatePriorityOperators(input)));
            } else if(input.getText().contains("*") && !input.getText().contains("/")){
                String[] multiplyArray = input.getText().split("\\*");
                return new JTextField(String.valueOf(multiply(multiplyArray)));
            }else if(input.getText().contains("/") && !input.getText().contains("*")) {
                String[] multiplyArray = input.getText().split("/");
                return new JTextField(String.valueOf(divide(multiplyArray)));
            }else if(input.getText().contains("*") && input.getText().contains("/")) {
                String[] divideAndMultiplyArray = splitToArray(new JTextField(input.getText()), "\\/","\\*");
                return new JTextField(String.valueOf(divideAndMultiply(divideAndMultiplyArray)));
            }
        }catch (NumberFormatException e) {
            return new JTextField("0");
        }
        return new JTextField("0");
    }

    // Detect "*" and "/" operators and calculate as first
    private String calculatePriorityOperators (JTextField input) {
            String[] finalArray = splitToArray(input, "\\+","\\-");
            for(int i = 0; i < finalArray.length; i++) {
                if ((finalArray[i].contains("*")) && !(finalArray[i].contains("/"))) {
                    String[] equationToCalculate = finalArray[i].split("\\*");
                    finalArray[i] = String.valueOf(multiply(equationToCalculate));
                }else if((finalArray[i].contains("/")) && !(finalArray[i].contains("*"))) {
                    String[] equationToCalculate = finalArray[i].split("/");
                    finalArray[i] = String.valueOf(divide(equationToCalculate));
                }else if(finalArray[i].contains("*") && finalArray[i].contains("/")) {
                    String[] subArray = splitToArray(new JTextField(finalArray[i]), "\\/","\\*");
                    finalArray[i] = String.valueOf(divideAndMultiply(subArray));
                }
            }
            String test = "";
            for(String str: finalArray) {
                    test = test.concat(str.trim());
            }
            return test;
    }
}