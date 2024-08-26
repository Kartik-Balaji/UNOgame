import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Connect4GUI extends JFrame {
    public static int SCORE1=0;
    public static int SCORE2=0;
    private static final int ROWS = 6;
    private static final int COLS = 7;
    private static final char EMPTY = '-';
    private static final char PLAYER1 = 'X';
    private static final char PLAYER2 = 'O';

    private char[][] board = new char[ROWS][COLS];
    private boolean player1Turn = true;

    private JPanel boardPanel = new JPanel(new GridLayout(ROWS, COLS));
    private JButton[] columnButtons = new JButton[COLS];
    private JLabel[][] boardLabels = new JLabel[ROWS][COLS];

    public Connect4GUI() {
        setTitle("Connect 4");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new GridLayout(1, COLS));
        for (int i = 0; i < COLS; i++) {
            columnButtons[i] = new JButton("Click To Drop");
            int col = i;
            columnButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    dropDisc(col);
                }
            });
            topPanel.add(columnButtons[i]);
        }
        add(topPanel, BorderLayout.NORTH);

        boardPanel.setPreferredSize(new Dimension(700, 500));
        initializeBoard();
        add(boardPanel, BorderLayout.CENTER);
    }

    private void initializeBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
                boardLabels[i][j] = new JLabel("", SwingConstants.CENTER);
                boardLabels[i][j].setOpaque(true);
                boardLabels[i][j].setBackground(Color.BLACK);
                boardLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.WHITE));
                boardPanel.add(boardLabels[i][j]);
            }
        }
    }

    private void dropDisc(int col) {
        for (int i = ROWS - 1; i >= 0; i--) {
            if (board[i][col] == EMPTY) {
                board[i][col] = player1Turn ? PLAYER1 : PLAYER2;
                boardLabels[i][col].setBackground(player1Turn ? Color.RED : Color.YELLOW);
//                boardLabels[i][col].setForeground(player1Turn ? Color.RED : Color.BLUE);
                if (checkWin(i, col, player1Turn ? PLAYER1 : PLAYER2)) {
                    JOptionPane.showMessageDialog(this, "Player " + (player1Turn ? "Red" : "Yellow") + " wins!");
                    resetBoard();
                    if(player1Turn){
                        SCORE1+=1;
                    }
                    else{
                        SCORE2+=1;
                    }
                    JOptionPane.showMessageDialog(this,"The scores are " + SCORE1 + " and " + SCORE2 + "!");

                } else if (isBoardFull()) {
                    JOptionPane.showMessageDialog(this, "The game is a draw!");
                    resetBoard();
                }
                player1Turn = !player1Turn;
                break;
            }
        }
    }

    private boolean checkWin(int row, int col, char disc) {
        return checkDirection(row, col, disc, 1, 0) || // Horizontal
                checkDirection(row, col, disc, 0, 1) || // Vertical
                checkDirection(row, col, disc, 1, 1) || // Diagonal /
                checkDirection(row, col, disc, 1, -1);  // Diagonal \
    }

    private boolean checkDirection(int row, int col, char disc, int rowDir, int colDir) {
        int count = 0;
        for (int i = -3; i <= 3; i++) {
            int r = row + i * rowDir;
            int c = col + i * colDir;
            if (r >= 0 && r < ROWS && c >= 0 && c < COLS && board[r][c] == disc) {
                count++;
                if (count == 4) {
                    return true;
                }
            } else {
                count = 0;
            }
        }
        return false;
    }

    private boolean isBoardFull() {
        for (int j = 0; j < COLS; j++) {
            if (board[0][j] == EMPTY) {
                return false;
            }
        }
        return true;
    }

    private void resetBoard() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                board[i][j] = EMPTY;
                boardLabels[i][j].setText("");
                boardLabels[i][j].setBackground(Color.BLACK);
            }
        }
        player1Turn = true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                Connect4GUI frame = new Connect4GUI();
                frame.setVisible(true);
            }
        });
    }
}
