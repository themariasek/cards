public class BlackJackHand extends Hand {
    public int getTotalPoints() {
        int total = 0;
        int aceCount = 0;
        for (Card card : getCards()) {
            if (card instanceof BlackJackCard) {
                int value = ((BlackJackCard) card).getPointValue();
                total += value;
                if (((BlackJackCard) card).getValue().equals("A")) {
                    aceCount++;
                }
            }
        }
        while (aceCount > 0 && total + 10 <= 21) {
            total += 10;
            aceCount--;
        }
        return total;
    }
}
