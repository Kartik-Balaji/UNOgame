import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UnoDeck {
    private List<UnoCard> cards;

    public UnoDeck() {
        cards = new ArrayList<>();
        initializeDeck();
        shuffle();
    }

    private void initializeDeck() {
        for (UnoCard.Color color : UnoCard.Color.values()) {
            if (color != UnoCard.Color.WILD) {
                for (UnoCard.Value value : UnoCard.Value.values()) {
                    if (value != UnoCard.Value.WILD && value != UnoCard.Value.WILD_DRAW_FOUR) {
                        cards.add(new UnoCard(color, value));
                        if (value != UnoCard.Value.ZERO) {
                            cards.add(new UnoCard(color, value));
                        }
                    }
                }
            }
        }

        for (int i = 0; i < 4; i++) {
            cards.add(new UnoCard(UnoCard.Color.WILD, UnoCard.Value.WILD));
            cards.add(new UnoCard(UnoCard.Color.WILD, UnoCard.Value.WILD_DRAW_FOUR));
        }
    }

    public void shuffle() {
        Collections.shuffle(cards);
    }

    public UnoCard drawCard() {
        return cards.remove(cards.size() - 1);
    }

    public int remainingCards() {
        return cards.size();
    }
}
