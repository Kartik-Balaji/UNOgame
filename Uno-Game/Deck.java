import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<Card> cards;

    public Deck() {
        cards = new ArrayList<>();
        for (Color color : Color.values()) {
            if (color != Color.WILD) {
                for (Rank rank : Rank.values()) {
                    if (rank != Rank.WILD && rank != Rank.WILD_DRAW_FOUR) {
                        cards.add(new Card(color, rank));
                        if (rank != Rank.ZERO) {
                            cards.add(new Card(color, rank));  // Add two of each non-zero card
                        }
                    }
                }
            }
        }
        // Add wild and wild draw four cards
        for (int i = 0; i < 4; i++) {
            cards.add(new Card(Color.WILD, Rank.WILD));
            cards.add(new Card(Color.WILD, Rank.WILD_DRAW_FOUR));
        }
        Collections.shuffle(cards);
    }

    public Card drawCard() {
        return cards.isEmpty() ? null : cards.remove(0);
    }

    public void addCard(Card card) {
        cards.add(card);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }
}
