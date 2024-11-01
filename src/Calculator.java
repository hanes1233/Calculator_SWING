import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

// Constructor creates Calculator application and inherits logic from Logic class
public class Calculator extends Logic {
    public Calculator() {

        //Creating application frame with all settings
        JFrame frame = new JFrame();
        JPanel panel = new JPanel();
        JButton equals = new JButton("=");
        equals.setFont(new Font("Arial",Font.BOLD,20));
        equals.setBackground(Color.CYAN);
        panel.setSize(600,200);
        panel.setFont(new Font("Verdana", Font.BOLD, 12));
        panel.setBackground(Color.black);
        JTextField usersInput = new JTextField(13);
        usersInput.setFont(usersInput.getFont().deriveFont(46f));
        panel.add(usersInput);

        // While loop to add buttons on panel from two Queue collections - 'digits' and 'symbols'
        while(!symbols.isEmpty() || !digits.isEmpty()) {

            // While queues are not empty - create new buttons with specific symbols or digits
            JButton buttonToAppend = (!digits.isEmpty()) ? digits.poll() : symbols.poll();
            buttonToAppend.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);

                    // Clear the input if 'C' button pressed
                    if(buttonToAppend.getText().equals("C")) {
                        usersInput.setText("");
                    }else {
                        usersInput.setText(usersInput.getText().concat(buttonToAppend.getText()));
                    }
                }
            });
            // Append button on JPanel
            panel.add(buttonToAppend);
        }
        // Add functionality to pressed keys
        usersInput.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyChar() == KeyEvent.VK_ENTER) {
                    usersInput.setText(calculateEquation(usersInput));
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        // Setting up functionality to final 'equals' button
        equals.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent me) {
                usersInput.setText(calculateEquation(usersInput));
            }
        });
        panel.add(equals);
        frame.getContentPane().setBackground(Color.BLACK);
        frame.setResizable(false);
        frame.setTitle("Calculator");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.add(panel);
        URL appIcon = getClass().getResource("2344132.png");
        assert appIcon != null;
        ImageIcon image = new ImageIcon(appIcon);
        frame.setIconImage(image.getImage());
        frame.setSize(600,300);
        frame.setVisible(true);
    }
        // Method contains loop, which create JButtons with 1 to 9 content,
        // then creating two extra buttons with '.' and 'C' content
        private final Queue<JButton> digits = new LinkedList<>();
         {
             for(int i = 0; i < 10; i++) {
                 digits.add(new JButton(String.valueOf(i)));
             }
             digits.add(new JButton("."));
             digits.add(new JButton(("C")));
         }

         // Adding equations symbols to queue to interact with
         private final Queue<JButton> symbols = new LinkedList<>();
        {symbols.addAll(Arrays.asList(buttons()));}

        // Method creates buttons with symbol content
        private JButton[] buttons() {
            JButton[] buttons = new JButton[] {
                    new JButton("+"),
                    new JButton("-"),
                    new JButton("*"),
                    new JButton("/"),
                    new JButton("%"),
                    new JButton("mod"),
                    new JButton("round"),
                    new JButton("("),
                    new JButton(")"),
                    new JButton("√"),
                    new JButton("x²")
            };
            for(JButton btn : buttons) {
                btn.setBackground(Color.yellow);
            }
            return buttons;
        }
}