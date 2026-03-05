import java.util.HashMap;

public class UnoComputer {
	public UnoCard playCard(Hand h, UnoCard current) {
		if (h == null || current == null) {
			return null;
		}
		UnoCard wildCandidate = null;
		for (int i = 0; i < h.getSize(); i++) {
			Card baseCard = h.getCard(i);
			if (!(baseCard instanceof UnoCard)) continue;
			UnoCard card = (UnoCard) baseCard;
			if (card == null) {
				continue;
			}
			if (card.suit != null && "Wild".equals(card.suit)) {
				if (wildCandidate == null) {
					wildCandidate = card;
				}
				continue;
			}
			if ((card.suit != null && card.suit.equals(current.suit)) || (card.value != null && card.value.equals(current.value))) {
				return card;
			}
		}

		return wildCandidate;
	}

	public String chooseComputerWildColor(Hand hand) {
		// make hashmap to count colors
		HashMap<String, Integer> colorCount = new HashMap<>();

        for (int i = 0; i < hand.getSize(); i++) {
            Card card = hand.getCard(i);
            if (card == null) {
                continue;
            }
            colorCount.put(card.suit, colorCount.getOrDefault(card.suit, 0) + 1);
        }
        // find color with most cards
        String best = "Red";
        int maxCount = 0;
        for (String color : colorCount.keySet()) {
            if (colorCount.get(color) > maxCount) {
                maxCount = colorCount.get(color);
                best = color;
            }
        }
        return best;
    }
}
