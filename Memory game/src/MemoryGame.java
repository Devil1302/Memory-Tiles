import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Collections;

public class MemoryGame extends JFrame implements ActionListener {
    private JPanel panel;
    private JButton[] buttons;
    private int[] tiles;
    private int numTiles;
    private int firstIndex;
    private int secondIndex;
    private int attempts;
    private int score;
    private JLabel attemptsLabel;
    private JLabel scoreLabel;
    private boolean gameOver;

    public MemoryGame(int numTiles) {
        this.numTiles = numTiles;
        this.tiles = new int[numTiles];
        this.buttons = new JButton[numTiles];
        this.attempts = 0;
        this.score = 0;
        this.gameOver = false;

        panel = new JPanel();
        panel.setLayout(new GridLayout(4, 4));

        for (int i = 0; i < numTiles; i++) {
            buttons[i] = new JButton();
            buttons[i].setBackground(Color.BLUE);
            buttons[i].addActionListener(this);
            panel.add(buttons[i]);
        }

        add(panel, BorderLayout.CENTER);

        attemptsLabel = new JLabel("Attempts: " + attempts);
        add(attemptsLabel, BorderLayout.SOUTH);

        scoreLabel = new JLabel("Score: " + score);
        add(scoreLabel, BorderLayout.NORTH);

        JButton restartButton = new JButton("Restart");
        restartButton.addActionListener(this);
        add(restartButton, BorderLayout.EAST);

        setTitle("Memory Game");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);

        initializeTiles();
        updateButtons();
    }

    private void initializeTiles() {
        ArrayList<Integer> numbers = new ArrayList<>();

        for (int i = 0; i < numTiles / 2; i++) {
            numbers.add(i + 1);
            numbers.add(i + 1);
        }

        Collections.shuffle(numbers);

        for (int i = 0; i < numTiles; i++) {
            tiles[i] = numbers.get(i);
        }
    }

    private void updateButtons() {
        for (int i = 0; i < numTiles; i++) {
            if (buttons[i].isEnabled()) {
                buttons[i].setText("");
                buttons[i].setBackground(Color.BLUE);
            } else {
                buttons[i].setText(Integer.toString(tiles[i]));
                buttons[i].setBackground(Color.WHITE);
            }
        }
    }

    private void checkMatch() {
        if (tiles[firstIndex] == tiles[secondIndex]) {
            buttons[firstIndex].setEnabled(false);
            buttons[secondIndex].setEnabled(false);
            score++;
            scoreLabel.setText("Score: " + score);
        }
        attempts++;
        attemptsLabel.setText("Attempts: " + attempts);

        if (isGameOver()) {
            gameOver = true;
            JOptionPane.showMessageDialog(this, "Game Over! Your score is " + score);
        }
    }

    private boolean isGameOver() {
        for (int i = 0; i < numTiles; i++) {
            if (buttons[i].isEnabled()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Restart")) {
            restartGame();
            return;
        }

        for (int i = 0; i < numTiles; i++) {
            if (e.getSource() == buttons[i] && buttons[i].isEnabled()) {
                if (firstIndex == -1) {
                    firstIndex = i;
                    buttons[firstIndex].setText(Integer.toString(tiles[firstIndex]));
                    buttons[firstIndex].setBackground(Color.WHITE);
                } else {
                    secondIndex = i;
                    buttons[secondIndex].setText(Integer.toString(tiles[secondIndex]));
                    buttons[secondIndex].setBackground(Color.WHITE);
                    checkMatch();
                    Timer timer = new Timer(1000, new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            buttons[firstIndex].setText("");
                            buttons[firstIndex].setBackground(Color.BLUE);
                            buttons[secondIndex].setText("");
                            buttons[secondIndex].setBackground(Color.BLUE);
                            firstIndex = -1;
                        }
                    });
                    timer.setRepeats(false);
                    timer.start();
                }
            }
        }
    }

    private void restartGame() {
        gameOver = false;
        attempts = 0;
        score = 0;
        attemptsLabel.setText("Attempts: " + attempts);
        scoreLabel.setText("Score: " + score);
        initializeTiles();
        updateButtons();
        for (int i = 0; i < numTiles; i++) {
            buttons[i].setEnabled(true);
        }
    }

    public static void main(String[] args) {
        new MemoryGame(20); // Change the argument to change the number of tiles
    }
}