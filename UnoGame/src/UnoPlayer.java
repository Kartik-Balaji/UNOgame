import java.util.ArrayList;
import java.util.List;

public class UnoPlayer {
    private List<UnoCard> hand;

    public UnoPlayer() {
        hand = new ArrayList<>();
    }

    public void draw(UnoDeck deck, int numberOfCards) {
        for (int i = 0; i < numberOfCards; i++) {
            hand.add(deck.drawCard());
        }
    }

    public List<UnoCard> getHand() {
        return hand;
    }

    public boolean playCard(UnoCard card) {
        if (hand.remove(card)) {
            return true;
        }
        return false;
    }

    public boolean hasWon() {
        return hand.isEmpty();
    }
}
