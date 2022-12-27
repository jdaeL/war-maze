import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game implements KeyListener {
    private GamePanel gamePanel;

    public Game() { // public Game(difficulty d) {

        String[] options = new String[] {"Fácil", "Medio", "Dificil"};
        
        String message = "Solo existe el nivel fácil por ahora! Espera la siguiente update.";
        int difficultyChoice = JOptionPane.showOptionDialog(null, message,
                "Elije facil!",
                JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, options, options[0]);


        JFrame frame = new JFrame("The Maze");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        gamePanel = new GamePanel(difficultyChoice);
        frame.getContentPane().add(gamePanel);
        frame.addKeyListener(this);
        frame.pack();
        frame.setVisible(true);
    
    }

    @Override
    public void keyPressed(KeyEvent e) {
        gamePanel.handleInput(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
   
    @Override
    public void keyReleased(KeyEvent e) {}
}
