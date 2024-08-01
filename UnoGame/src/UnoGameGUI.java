import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class UnoGameGUI extends JFrame {
    private UnoDeck deck;
    private UnoPlayer player1;
    private UnoPlayer player2;
    private UnoCard topCard;
    private int currentPlayer;
    private boolean gameOver;
    private JPanel player1Panel;
    private JPanel player2Panel;
    private JLabel topCardLabel;
    private JButton drawButton;
    private JLabel currentPlayerLabel;

    public UnoGameGUI() {
        deck = new UnoDeck();
        player1 = new UnoPlayer();
        player2 = new UnoPlayer();
        player1.draw(deck, 7);
        player2.draw(deck, 7);
        topCard = deck.drawCard();
        currentPlayer = 0;  // 0 for player 1, 1 for player 2
        gameOver = false;

        initGUI();
    }

    private void initGUI() {
        setTitle("UNO Game");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        player1Panel = new JPanel();
        player1Panel.setLayout(new FlowLayout());
        add(player1Panel, BorderLayout.SOUTH);

        player2Panel = new JPanel();
        player2Panel.setLayout(new FlowLayout());
        add(player2Panel, BorderLayout.NORTH);

        topCardLabel = new JLabel("Top Card: " + topCard);
        add(topCardLabel, BorderLayout.CENTER);

        drawButton = new JButton("Draw Card");
        drawButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPlayer == 0) {
                    player1.draw(deck, 1);
                    updatePlayerPanel(player1, player1Panel);
                } else {
                    player2.draw(deck, 1);
                    updatePlayerPanel(player2, player2Panel);
                }
                switchPlayer();
            }
        });
        add(drawButton, BorderLayout.WEST);

        currentPlayerLabel = new JLabel("Player 1's turn");
        add(currentPlayerLabel, BorderLayout.NORTH);

        updatePlayerPanel(player1, player1Panel);
        updatePlayerPanel(player2, player2Panel);

        setVisible(true);
    }

    private void updatePlayerPanel(UnoPlayer player, JPanel panel) {
        panel.removeAll();
        List<UnoCard> hand = player.getHand();
        for (UnoCard card : hand) {
            JButton cardButton = new JButton(card.toString());
            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (canPlayCard(card)) {
                        player.playCard(card);
                        topCard = card;
                        topCardLabel.setText("Top Card: " + topCard);
                        updatePlayerPanel(player, panel);
                        checkForWinner();
                        if (!gameOver) {
                            switchPlayer();
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Invalid card. Try again.");
                    }
                }
            });
            panel.add(cardButton);
        }
        panel.revalidate();
        panel.repaint();
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer + 1) % 2;
        String playerName = (currentPlayer == 0) ? "Player 1" : "Player 2";
        currentPlayerLabel.setText(playerName + "'s turn");
        JOptionPane.showMessageDialog(null, playerName + "'s turn");
    }

    private boolean canPlayCard(UnoCard card) {
        return card.getColor() == topCard.getColor() || card.getValue() == topCard.getValue() ||
                card.getColor() == UnoCard.Color.WILD;
    }

    private void checkForWinner() {
        if (player1.hasWon()) {
            JOptionPane.showMessageDialog(null, "Player 1 wins!");
            gameOver = true;
            System.exit(0);
        } else if (player2.hasWon()) {
            JOptionPane.showMessageDialog(null, "Player 2 wins!");
            gameOver = true;
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new UnoGameGUI();
    }
}
