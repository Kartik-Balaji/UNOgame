import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand;

    public Player() {
        hand = new ArrayList<>();
    }

    public void addCard(Card card) {
        hand.add(card);
    }

    public Card playCard(int index) {
        return hand.remove(index);
    }

    public Card getCard(int index) {
        return hand.get(index);
    }

    public int getHandSize() {
        return hand.size();
    }

    public void showHand() {
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(i + ": " + hand.get(i));
        }
    }

    public boolean hasPlayableCard(Card topCard) {
        for (Card card : hand) {
            if (card.getColor() == topCard.getColor() || card.getRank() == topCard.getRank() || card.getColor() == Color.WILD) {
                return true;
            }
        }
        return false;
    }
}
