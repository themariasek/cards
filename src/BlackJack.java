	import processing.core.PApplet;

	public class BlackJack extends CardGame {
		private boolean blackjackWinDelayActive = false;
		private int blackjackWinDelayTimer = 0;
		private final int BLACKJACK_WIN_DELAY = 600;
		int dealerFrameDelay = 0;
		public boolean dealerTurnActive = false;
		public boolean dealerSecondCardRevealed = false;
		private boolean pendingDealerTurn = false;
		private int dealerThinkingTimer = 0;

		public void updateGameDelays() {
			if (pendingDealerTurn) {
				return;
			}
			if (dealerTurnActive) {
				dealerSecondCardRevealed = true;
				handleDealerTurnStep();
			}
			int playerTotal = ((BlackJackHand) playerOneHand).getTotalPoints();
			boolean playerHasAce = false;
			for (Card card : playerOneHand.getCards()) {
				if (card instanceof BlackJackCard && ((BlackJackCard) card).getValue().equals("A")) {
					playerHasAce = true;
					break;
				}
			}
			if (gameActive && playerTotal == 21 && playerHasAce) {
				gameActive = false;
				dealerTurnActive = false;
			}
			if (!gameActive && !blackjackWinDelayActive && !dealerTurnActive) {
				blackjackWinDelayActive = true;
				blackjackWinDelayTimer = BLACKJACK_WIN_DELAY;
			}
			if (!gameActive && blackjackWinDelayActive) {
				blackjackWinDelayTimer--;
				if (blackjackWinDelayTimer <= 0) {
					blackjackWinDelayActive = false;
				}
			}
		}

		public boolean shouldShowEndScreen() {
			return !gameActive && !dealerTurnActive && !blackjackWinDelayActive;
		}

		public void beginDealerThinking() {
			dealerTurnActive = false;
			pendingDealerTurn = true;
			dealerThinkingTimer = 0;
		}

		public void updateDealerThinking() {
			if (pendingDealerTurn) {
				dealerThinkingTimer++;
				if (dealerThinkingTimer >= 40) {
					dealerTurnActive = true;
					pendingDealerTurn = false;
					dealerThinkingTimer = 0;
				}
			}
		}

		public boolean showDealerThinkingText() {
			return pendingDealerTurn || dealerTurnActive;
		}

		public void drawBlackJackUI(PApplet app) {
			int playerTotal = ((BlackJackHand) playerOneHand).getTotalPoints();
			boolean playerHasAce = false;
			for (Card card : playerOneHand.getCards()) {
				if (card instanceof BlackJackCard && ((BlackJackCard) card).getValue().equals("A")) {
					playerHasAce = true;
					break;
				}
			}
			boolean showBlackjackText = (playerTotal == 21 && playerHasAce && !gameActive && blackjackWinDelayActive);
				   boolean showButtons = gameActive && !dealerTurnActive && !pendingDealerTurn;
				   if (showButtons) {
					   app.strokeWeight(2);
					   for (int i = 0; i < hitButton.height; i++) {
						   float t = (float)i / hitButton.height;
						   int r = (int)PApplet.lerp(110, 180, t);
						   int g = (int)PApplet.lerp(10, 40, t);
						   int b = (int)PApplet.lerp(10, 40, t);
						   app.stroke(r, g, b);
						   app.fill(r, g, b);
						   app.rect(hitButton.x, hitButton.y + i, hitButton.width, 1);
					   }
					   app.noFill();
					   app.stroke(0);
					   app.rect(hitButton.x, hitButton.y, hitButton.width, hitButton.height);
					   app.fill(255);
					   app.textSize(24);
					   app.textAlign(PApplet.CENTER, PApplet.CENTER);
					   app.text("Hit", hitButton.x + hitButton.width / 2, hitButton.y + hitButton.height / 2);

					   app.strokeWeight(2);
					   for (int i = 0; i < standButton.height; i++) {
						   float t = (float)i / standButton.height;
						   int r = (int)PApplet.lerp(20, 60, t);
						   int g = (int)PApplet.lerp(40, 120, t);
						   int b = (int)PApplet.lerp(90, 220, t);
						   app.stroke(r, g, b);
						   app.fill(r, g, b);
						   app.rect(standButton.x, standButton.y + i, standButton.width, 1);
					   }
					   app.noFill();
					   app.stroke(0);
					   app.rect(standButton.x, standButton.y, standButton.width, standButton.height);
					   app.fill(255);
					   app.textSize(24);
					   app.textAlign(PApplet.CENTER, PApplet.CENTER);
					   app.text("Stand", standButton.x + standButton.width / 2, standButton.y + standButton.height / 2);
				   }
			if (showBlackjackText) {
				app.fill(0, 180, 0);
				app.textSize(32);
				app.text("BLACKJACK!", app.width / 2, 120);
				app.textSize(20);
			}
		}

		public void drawScoreUI(PApplet app, boolean showEndScreen) {
			int playerCircleX = 95;
			int playerCircleY = 400;
			int computerCircleX = 95;
			int computerCircleY = 220;
			int circleDiameter = 50;

			BlackJackHand playerHand = (BlackJackHand) playerOneHand;
			int playerPoints = playerHand.getTotalPoints();
			int playerSoftValue = 0;
			int playerHardValue = 0;
			int playerAceCount = 0;
			for (Card card : playerHand.getCards()) {
				if (card instanceof BlackJackCard && ((BlackJackCard) card).getValue().equals("A")) {
					playerAceCount++;
				}
			}
			for (Card card : playerHand.getCards()) {
				if (card instanceof BlackJackCard) {
					playerHardValue += ((BlackJackCard) card).getPointValue();
				}
			}
			playerSoftValue = playerHardValue;
			if (playerAceCount > 0 && playerHardValue + 10 <= 21) {
				playerSoftValue = playerHardValue + 10;
			}

			int playerRadius = circleDiameter / 2;
			for (int i = 0; i < circleDiameter; i++) {
				float t = (float)i / circleDiameter;
				int r = (int)PApplet.lerp(110, 180, t);
				int g = (int)PApplet.lerp(10, 40, t);
				int b = (int)PApplet.lerp(10, 40, t);
				float dy = i - playerRadius;
				float halfW = (float)Math.sqrt(playerRadius * playerRadius - dy * dy);
				app.stroke(r, g, b);
				app.strokeWeight(1);
				app.line(playerCircleX - halfW, playerCircleY - playerRadius + i,
				         playerCircleX + halfW, playerCircleY - playerRadius + i);
			}
			app.stroke(0);
			app.strokeWeight(2);
			app.noFill();
			app.ellipse(playerCircleX, playerCircleY, circleDiameter, circleDiameter);
			app.fill(255);
			app.textAlign(PApplet.CENTER, PApplet.CENTER);
			if (playerAceCount > 0 && playerSoftValue != playerHardValue) {
				app.textSize(16);
				app.text(playerHardValue + "/" + playerSoftValue, playerCircleX, playerCircleY);
			} else {
				app.textSize(20);
				app.text(playerPoints, playerCircleX, playerCircleY);
			}
			if (playerPoints > 21 && !gameActive && !showEndScreen) {
				app.fill(220, 0, 0);
				app.textSize(24);
				app.text("BUST!", playerCircleX + circleDiameter / 2 + 40, playerCircleY);
				app.textSize(20);
			}

			BlackJackHand dealerHand = (BlackJackHand) playerTwoHand;
			int visiblePoints = 0;
			int dealerSoftValue = 0;
			int dealerHardValue = 0;
			int dealerAceCount = 0;
			for (int i = 0; i < dealerHand.getSize(); i++) {
				Card card = dealerHand.getCard(i);
				if (card != null && !card.turned) {
					visiblePoints += card.getPointValue();
					if (card instanceof BlackJackCard && ((BlackJackCard) card).getValue().equals("A")) {
						dealerAceCount++;
					}
				}
			}
			for (int i = 0; i < dealerHand.getSize(); i++) {
				Card card = dealerHand.getCard(i);
				if (card != null && !card.turned && card instanceof BlackJackCard) {
					dealerHardValue += ((BlackJackCard) card).getPointValue();
				}
			}
			dealerSoftValue = dealerHardValue;
			if (dealerAceCount > 0 && dealerHardValue + 10 <= 21) {
				dealerSoftValue = dealerHardValue + 10;
			}

			int dealerRadius = circleDiameter / 2;
			for (int i = 0; i < circleDiameter; i++) {
				float t = (float)i / circleDiameter;
				int r = (int)PApplet.lerp(20, 60, t);
				int g = (int)PApplet.lerp(40, 120, t);
				int b = (int)PApplet.lerp(90, 220, t);
				float dy = i - dealerRadius;
				float halfW = (float)Math.sqrt(dealerRadius * dealerRadius - dy * dy);
				app.stroke(r, g, b);
				app.strokeWeight(1);
				app.line(computerCircleX - halfW, computerCircleY - dealerRadius + i,
				         computerCircleX + halfW, computerCircleY - dealerRadius + i);
			}
			app.stroke(0);
			app.strokeWeight(2);
			app.noFill();
			app.ellipse(computerCircleX, computerCircleY, circleDiameter, circleDiameter);
			app.fill(255);
			app.textAlign(PApplet.CENTER, PApplet.CENTER);
			if (dealerAceCount > 0 && dealerSoftValue != dealerHardValue) {
				app.textSize(16);
				app.text(dealerHardValue + "/" + dealerSoftValue, computerCircleX, computerCircleY);
			} else {
				app.textSize(20);
				app.text(visiblePoints, computerCircleX, computerCircleY);
			}
			if (visiblePoints > 21 && !gameActive && !showEndScreen && !dealerTurnActive) {
				app.fill(220, 0, 0);
				app.textSize(24);
				app.text("BUST!", computerCircleX + circleDiameter / 2 + 40, computerCircleY);
				app.textSize(20);
			}
		}

		public int getBalanceDelta(int bet) {
			int playerTotal = ((BlackJackHand) playerOneHand).getTotalPoints();
			int computerTotal = ((BlackJackHand) playerTwoHand).getTotalPoints();
			boolean playerHasAce = false;
			for (Card card : playerOneHand.getCards()) {
				if (card instanceof BlackJackCard && ((BlackJackCard) card).getValue().equals("A")) {
					playerHasAce = true;
					break;
				}
			}
			if (playerTotal == 21 && playerHasAce) {
				return bet;
			} else if (playerTotal > 21) {
				return -bet;
			} else if (computerTotal > 21) {
				return bet;
			} else {
				if (playerTotal > computerTotal) {
					return bet;
				} else if (computerTotal > playerTotal) {
					return -bet;
				} else {
					return 0;
				}
			}
		}

		public String getEndScreenText() {
			int playerTotal = ((BlackJackHand) playerOneHand).getTotalPoints();
			int computerTotal = ((BlackJackHand) playerTwoHand).getTotalPoints();
			boolean playerHasAce = false;
			for (Card card : playerOneHand.getCards()) {
				if (card instanceof BlackJackCard && ((BlackJackCard) card).getValue().equals("A")) {
					playerHasAce = true;
					break;
				}
			}
			if (playerTotal == 21 && playerHasAce) {
				return "Blackjack! You win!";
			} else if (playerTotal > 21) {
				return "Bust! Computer wins!";
			} else if (computerTotal > 21) {
				return "Computer busts! You win!";
			} else if (!gameActive && !dealerTurnActive) {
				if (playerTotal > computerTotal) {
					return "You win!";
				} else if (computerTotal > playerTotal) {
					return "Computer wins!";
				} else {
					return "It's a tie!";
				}
			}
			return "";
		}

		public boolean handleBlackJackMousePressed(int mouseX, int mouseY) {
			if (gameActive) {
				if (hitButton.isClicked(mouseX, mouseY)) {
					handleHit();
					return true;
				}
				if (standButton.isClicked(mouseX, mouseY)) {
					handleStand();
					return true;
				}
			}
			return false;
		}
	BlackJackComputer computerPlayer;
	ClickableRectangle hitButton;
	ClickableRectangle standButton;
	public BlackJack() {
		playerOneHand = new BlackJackHand();
		playerTwoHand = new BlackJackHand();
		hitButton = new ClickableRectangle();
		hitButton.x = drawButtonX;
		hitButton.y = drawButtonY;
		hitButton.width = drawButtonWidth;
		hitButton.height = drawButtonHeight;

		standButton = new ClickableRectangle();
		standButton.x = drawButtonX + drawButtonWidth + 20;
		standButton.y = drawButtonY;
		standButton.width = drawButtonWidth;
		standButton.height = drawButtonHeight;

		playAgainButton = new ClickableRectangle();
		playAgainButton.x = 250;
		playAgainButton.y = 300;
		playAgainButton.width = 100;
		playAgainButton.height = 40;

		deck = new java.util.ArrayList<>();
		discardPile = new java.util.ArrayList<>();
		gameActive = true;
		createDeck();
		dealCards(2);
		computerPlayer = new BlackJackComputer();
		if (hasBlackjack((BlackJackHand)playerOneHand)) {
			gameActive = false;
			dealerTurnActive = false;
		}
	}

	@Override
	protected void createDeck() {
		String[] suits = { "Hearts", "Diamonds", "Clubs", "Spades" };
		String[] values = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A" };
		for (String suit : suits) {
			for (String value : values) {
				deck.add(new BlackJackCard(value, suit));
			}
		}
	}
	

			public void handleHit() {
				   if (playerOneTurn && gameActive) {
					   drawCard(playerOneHand);
					   playerOneHand.positionCards(50, 450, 80, 120, 20);
					   int total = ((BlackJackHand) playerOneHand).getTotalPoints();
					   if (total == 21) {
						   gameActive = false;
						   dealerTurnActive = false;
					   } else if (hasBlackjack((BlackJackHand) playerOneHand)) {
						   gameActive = false;
						   dealerTurnActive = false;
					   } else if (total > 21) {
						   gameActive = false;
					   }
				   }
			}

			private boolean hasBlackjack(BlackJackHand hand) {
				int total = hand.getTotalPoints();
				if (total != 21) return false;
				int aceCount = 0;
				for (Card card : hand.getCards()) {
					if (card instanceof BlackJackCard && ((BlackJackCard) card).getValue().equals("A")) {
						aceCount++;
					}
				}
				return aceCount > 0;
			}

		public void handleStand() {
			if (playerOneTurn && gameActive) {
					switchTurns();
					dealerTurnActive = true;
					dealerSecondCardRevealed = true;
					dealerFrameDelay = 30;
			}
		}


		public void handleDealerTurnStep() {
			if (!dealerSecondCardRevealed) {
				if (dealerFrameDelay > 0) {
					dealerFrameDelay--;
					return;
				}
				if (playerTwoHand.getSize() > 1) {
					playerTwoHand.getCard(1).setTurned(false);
				}
				playerTwoHand.positionCards(50, 50, 80, 120, 20);
				dealerSecondCardRevealed = true;
				dealerFrameDelay = 30;
				return;
			}
			if (dealerFrameDelay > 0) {
				dealerFrameDelay--;
				int dealerTotal = ((BlackJackHand) playerTwoHand).getTotalPoints();
				if (dealerTotal >= 17) {
					gameActive = false;
					dealerTurnActive = false;
					return;
				}
				return;
			}
			computerPlayer.doDealerStep((BlackJackHand) playerTwoHand, (BlackJackHand) playerOneHand, this);
		}

	@Override
	protected void initializeGame() {
		playAgainButton = new ClickableRectangle();
		playAgainButton.x = 250;
		playAgainButton.y = 300;
		playAgainButton.width = 100;
		playAgainButton.height = 40;

		deck = new java.util.ArrayList<>();
		discardPile = new java.util.ArrayList<>();
		playerOneHand = new BlackJackHand();
		playerTwoHand = new BlackJackHand();
		gameActive = true;

		createDeck();
		dealCards(2);
	}
}