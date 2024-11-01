
import javax.swing.*;
import java.util.*;

interface Operations {
    //Calculating adding and subtracting equations
    default Double calculateSimpleOperators(JTextField text) {
        try {
            String[] newArr = splitToArray(text, "\\+","\\-");
            String percentageTest = String.join(",",newArr);
            int length = newArr.length;
            double accumulator = Double.parseDouble(newArr[0]);
            if(newArr[newArr.length-1].contains("%")) {
                length -= 2;
            }
            for (int i = 0; i < length; i++) {
                if (newArr[i].equals("+")) {
                    accumulator += Double.parseDouble(newArr[i + 1]);
                }else if (newArr[i].equals("-")) {
                    accumulator -= Double.parseDouble(newArr[i + 1]);
                }
                if(percentageTest.contains("%")) {
                    if((i+2 <= newArr.length) && (newArr[i+2].contains("%"))) {
                            accumulator = Double.parseDouble(calculatePercentage(String.valueOf(accumulator),newArr[i+1],newArr[i+2]));
                            break;
                    }
                }

            }
            return accumulator;
        }catch (NumberFormatException e) {
            System.out.println("CalculateSimpleOperators error " + e);
            return -1.0;
        }
    }
    //Both dividing and multiplying
    default Double divideAndMultiply(String[] arrayToCalculate) {
        double accumulator = Double.parseDouble(arrayToCalculate[0]);
        try {
            for (int j = 0; j < arrayToCalculate.length; j++) {
                if (arrayToCalculate[j].equals("*")) {
                    accumulator *= Double.parseDouble(arrayToCalculate[j + 1]);
                } else if (arrayToCalculate[j].equals("/")) {
                    accumulator /= Double.parseDouble(arrayToCalculate[j + 1]);
                }
            }
        }catch (NumberFormatException e) {
            System.out.println("Exception in divideAndMultiply method");
            return -1.0;
        }
        return accumulator;
    }

    //Calculate equations with multiplying
    default Double multiply(String[] array) {
        try {
            List<Double> list = new LinkedList<>();
            for (String str : array) {
                list.add(Double.parseDouble(str));
            }
            return list.stream().reduce(1.0, (a, b) -> a * b);
        }catch (NumberFormatException e) {
            System.out.println(e + " in multiplying() method");
            JOptionPane.showMessageDialog(null, e);
            return -1.0;
        }
    }
    //Calculating equations with dividing
    default Double divide(String[] array) {
        try {
            double accumulator = Double.parseDouble(array[0]);
            for (int i = 1; i < array.length; i++) {
                accumulator /= Double.parseDouble(array[i]);
            }
            return accumulator;
        }catch (NumberFormatException e) {
            System.out.println(e + " in dividing() method");
            JOptionPane.showMessageDialog(null, e);
            return -1.0;
        }
    }
    //Rounding result
    default Integer round(String round) {
        try {
            String number = round.substring(0, round.indexOf('r'));
            return (int) Math.round(Double.parseDouble(number));
        }catch (NumberFormatException e) {
            System.out.println(e + " in rounding() method");
            JOptionPane.showMessageDialog(null, e);
            return -1;
        }
    }
    //Resolving equations with percentage wildcard
    default String calculatePercentage(String... text) {
        Double calculatePercentage = 0.0;
        for(int i = 0; i < text.length; i++) {
            try {
                if (text[i].contains("%")) {
                    if (i > 0) {
                        double percentElement = Double.parseDouble(text[i].replace("%", "").trim());
                        double previousElement = Double.parseDouble(text[i - 2]);
                        calculatePercentage = text[i - 1].equals("/") ? 100 / percentElement * previousElement : percentElement / 100 * previousElement;
                        if (text[i - 1].equals("+")) {
                            calculatePercentage = previousElement + calculatePercentage;
                        } else if (text[i - 1].equals("-")) {
                            calculatePercentage = previousElement - calculatePercentage;
                        }
                    } else {
                        calculatePercentage = Double.parseDouble(text[i].replace("%", "").trim()) / 100;
                        calculatePercentage = switch (text[i + 1]) {
                            case "+" -> calculatePercentage + Double.parseDouble(text[i + 2]);
                            case "-" -> calculatePercentage - Double.parseDouble(text[i + 2]);
                            case "*" -> calculatePercentage * Double.parseDouble(text[i + 2]);
                            case "/" -> calculatePercentage / Double.parseDouble(text[i + 2]);
                            default -> calculatePercentage;
                        };
                    }
                }
            }catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, e);
                System.out.println("Some error in percentage method" + e);
            }
        }
        return String.valueOf(calculatePercentage);
    }
    //Finding modulo of equation
    default Integer findModulo(String text) {
        try {
            String[] array = text.split("mod");
            double firstElement = Double.parseDouble(array[0]);
            double secondElement = Double.parseDouble(array[1]);
            if (array.length > 2) {
                return -1;
            } else {
                System.out.println(firstElement % 2);
                return (int) (firstElement % secondElement);
            }
        }catch (NumberFormatException e) {
            System.out.println(e + " in modulo() method");
            return -1;
        }
    }
    //Splitting text into array with required operator(s)
    default String[] splitToArray(JTextField text, String... operators) {
        String result = "";
        Queue<String> queue = new LinkedList<>();
        Collections.addAll(queue, operators);
        while(!queue.isEmpty()) {
            text.setText(text.getText().replaceAll(queue.peek(),"x"+queue.poll().replaceAll("\\\\","").trim()+"x"));
            result = text.getText();
        }
        return result.split("x");
    }

    default Double sqrt(String[] array) {
        return 0.0;
    }

    default Double root(String[] array) {
        return 0.0;
    }
}