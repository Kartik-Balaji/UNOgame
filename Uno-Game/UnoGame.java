import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class UnoGame extends JFrame {
    private Deck deck;
    private ArrayList<Player> players;
    private int currentPlayerIndex;
    private Card topCard;
    private JLabel topCardLabel;
    private JPanel playerPanel;
    private JButton drawCardButton;
    private JLabel playerTurnLabel;
    private boolean directionClockwise = true; // For handling Reverse cards

    public UnoGame(int numPlayers) {
        deck = new Deck();
        players = new ArrayList<>();

        // Create players
        for (int i = 0; i < numPlayers; i++) {
            Player player = new Player();
            for (int j = 0; j < 7; j++) {
                player.addCard(deck.drawCard());
            }
            players.add(player);
        }

        topCard = deck.drawCard();
        currentPlayerIndex = 0;

        // Set up the frame
        setTitle("UNO Game - Multi-Player");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        // setBackground(red);
        // Display the top card
        topCardLabel = new JLabel("Top Card: " + topCard.toString());
        topCardLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(topCardLabel, BorderLayout.NORTH);

        // Player's hand panel
        playerPanel = new JPanel();
        updatePlayerHand();
        add(new JScrollPane(playerPanel), BorderLayout.CENTER);

        // Player turn label
        playerTurnLabel = new JLabel("Player " + (currentPlayerIndex + 1) + "'s turn");
        playerTurnLabel.setFont(new Font("Arial", Font.BOLD, 16));
        add(playerTurnLabel, BorderLayout.WEST);

        // Draw Card button
        drawCardButton = new JButton("Draw Card");
        drawCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                drawCard();
            }
        });
        add(drawCardButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    // Update the player's hand in the GUI
    private void updatePlayerHand() {
        playerPanel.removeAll();
        Player currentPlayer = players.get(currentPlayerIndex);
        for (int i = 0; i < currentPlayer.getHandSize(); i++) {
            Card card = currentPlayer.getCard(i);
            JButton cardButton = new JButton(card.toString());
            int cardIndex = i;
            cardButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playCard(cardIndex);
                }
            });
            playerPanel.add(cardButton);
        }
        playerPanel.revalidate();
        playerPanel.repaint();
    }

    // Draw a card from the deck
    // Draw a card from the deck and move to the next player's turn
    private void drawCard() {
        Player currentPlayer = players.get(currentPlayerIndex);
        if (deck.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No more cards in the deck!");
            return;
        }
        Card newCard = deck.drawCard();
        currentPlayer.addCard(newCard);
        JOptionPane.showMessageDialog(this, "Player " + (currentPlayerIndex + 1) + " drew: " + newCard.toString());

        // Automatically end the current player's turn after drawing a card
        nextTurn();
    }


    // Play the selected card
    private void playCard(int cardIndex) {
        Player currentPlayer = players.get(currentPlayerIndex);
        Card selectedCard = currentPlayer.getCard(cardIndex);
        if (isValidPlay(selectedCard)) {
            currentPlayer.playCard(cardIndex);
            handlePlayedCard(selectedCard);
            if (currentPlayer.getHandSize() == 0) {
                JOptionPane.showMessageDialog(this, "Player " + (currentPlayerIndex + 1) + " wins!");
                System.exit(0);
            } else {
                // Move to the next player's turn
                nextTurn();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Invalid move!");
        }
    }

    // Handle the effects of the played card
    private void handlePlayedCard(Card playedCard) {
        topCard = playedCard;
        topCardLabel.setText("Top Card: " + topCard.toString());

        switch (playedCard.getRank()) {
            case REVERSE:
                directionClockwise = !directionClockwise;
                JOptionPane.showMessageDialog(this, "Reverse! Direction changed.");
                break;
            case SKIP:
                JOptionPane.showMessageDialog(this, "Skip! Next player is skipped.");
                nextTurn(); // Skip the next player
                break;
            case DRAW_TWO:
                Player nextPlayer = players.get(getNextPlayerIndex());
                nextPlayer.addCard(deck.drawCard());
                nextPlayer.addCard(deck.drawCard());
                JOptionPane.showMessageDialog(this, "Draw Two! Player " + (getNextPlayerIndex() + 1) + " draws 2 cards.");
                nextTurn(); // Skip the affected player's turn
                break;
            case WILD:
                chooseColor();
                break;
            case WILD_DRAW_FOUR:
                chooseColor();
                Player affectedPlayer = players.get(getNextPlayerIndex());
                for (int i = 0; i < 4; i++) {
                    affectedPlayer.addCard(deck.drawCard());
                }
                JOptionPane.showMessageDialog(this, "Wild Draw Four! Player " + (getNextPlayerIndex() + 1) + " draws 4 cards.");
                nextTurn(); // Skip the affected player's turn
                break;
            default:
                // No special action
                break;
        }
    }

    // Check if the card can be played
    private boolean isValidPlay(Card card) {
        if (card.getColor() == Color.WILD) {
            return true; // Wild cards can be played anytime
        }
        return card.getColor() == topCard.getColor() || card.getRank() == topCard.getRank();
    }

    // Move to the next player's turn
    private void nextTurn() {
        currentPlayerIndex = getNextPlayerIndex();
        playerTurnLabel.setText("Player " + (currentPlayerIndex + 1) + "'s turn");
        updatePlayerHand();
    }

    // Get the next player's index, considering the game direction
    private int getNextPlayerIndex() {
        int nextIndex = directionClockwise ? currentPlayerIndex + 1 : currentPlayerIndex - 1;
        if (nextIndex >= players.size()) {
            nextIndex = 0;
        } else if (nextIndex < 0) {
            nextIndex = players.size() - 1;
        }
        return nextIndex;
    }

    // Allow the player to choose a color after playing a Wild card
    private void chooseColor() {
        String[] options = {"RED", "BLUE", "GREEN", "YELLOW"};
        int choice = JOptionPane.showOptionDialog(this, "Choose a color:", "Wild Card",
                JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
        if (choice >= 0 && choice < options.length) {
            topCard = new Card(Color.valueOf(options[choice]), topCard.getRank());
            topCardLabel.setText("Top Card: " + topCard.toString());
        } else {
            JOptionPane.showMessageDialog(this, "No color selected. Defaulting to RED.");
            topCard = new Card(Color.RED, topCard.getRank());
            topCardLabel.setText("Top Card: " + topCard.toString());
        }
    }

    public static void main(String[] args) {
        String input = JOptionPane.showInputDialog("Enter number of players (2 or 3):");
        int numPlayers;
        try {
            numPlayers = Integer.parseInt(input);
            if (numPlayers < 2 || numPlayers > 3) {
                JOptionPane.showMessageDialog(null, "Please enter 2 or 3 players.");
                System.exit(0);
            }
            new UnoGame(numPlayers);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please enter a number.");
            System.exit(0);
        }
    }
}
