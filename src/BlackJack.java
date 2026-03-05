	import processing.core.PApplet;

	public class BlackJack extends CardGame {
		private boolean blackjackWinDelayActive = false;
		private int blackjackWinDelayTimer = 0;
		private final int BLACKJACK_WIN_DELAY = 600;
		int dealerFrameDelay = 0;
		public boolean dealerTurnActive = false;
		public boolean dealerSecondCardRevealed = false;

		public void updateGameDelays(PApplet app) {
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
				   boolean showButtons = gameActive && !dealerTurnActive;
				   try {
					   Class<?> appClass = app.getClass();
					   java.lang.reflect.Field pendingField = appClass.getDeclaredField("pendingDealerTurn");
					   pendingField.setAccessible(true);
					   boolean pendingDealerTurn = pendingField.getBoolean(app);
					   showButtons = showButtons && !pendingDealerTurn;
				   } catch (Exception e) {
				   }
				   if (showButtons) {
					   hitButton.draw(app);
					   standButton.draw(app);
					   app.fill(0);
					   app.textAlign(PApplet.CENTER, PApplet.CENTER);
					   app.text("Hit", hitButton.x + hitButton.width / 2,
							   hitButton.y + hitButton.height / 2);
					   app.text("Stand", standButton.x + standButton.width / 2,
							   standButton.y + standButton.height / 2);
				   }
			if (showBlackjackText) {
				app.fill(0, 180, 0);
				app.textSize(32);
				app.text("BLACKJACK!", app.width / 2, 120);
				app.textSize(20);
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
			// Use BlackJackComputer for dealer logic
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