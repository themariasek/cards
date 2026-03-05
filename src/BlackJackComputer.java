public class BlackJackComputer {
	public boolean doDealerStep(BlackJackHand dealerHand, BlackJackHand playerHand, BlackJack game) {
		// Returns true if dealer's turn is over, false if more steps needed
		int dealerTotal = dealerHand.getTotalPoints();
		if (dealerTotal > 21) {
			game.gameActive = false;
			game.dealerTurnActive = false;
			return true;
		} else if (dealerTotal < 17) {
			game.drawCard(dealerHand);
			dealerHand.positionCards(50, 50, 80, 120, 20);
			game.dealerFrameDelay = 30;
			return false;
		} else {
			game.gameActive = false;
			game.dealerTurnActive = false;
			return true;
		}
	}
}
