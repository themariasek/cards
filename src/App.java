import processing.core.PApplet;

public class App extends PApplet {

    CardGame cardGame = new BlackJack();
    private int timer;
    private boolean pendingDealerTurn = false;
    private int dealerThinkingTimer = 0;

    private ClickableRectangle[] betButtons = new ClickableRectangle[4];
    private final int[] betValues = {50, 100, 250, 500};

    public static void main(String[] args) {
        PApplet.main("App");
    }

    @Override
    public void settings() {
        size(600, 600);
    }

    private boolean showEndScreen = false;
    private int endScreenTimer = 0;

    @Override
    public void setup() {
        int buttonWidth = 60;
        int buttonHeight = 35;
        int spacing = 10;
        int startX = width - buttonWidth - 20;
        for (int i = 0; i < betButtons.length; i++) {
            betButtons[i] = new ClickableRectangle();
            betButtons[i].width = buttonWidth;
            betButtons[i].height = buttonHeight;
            betButtons[i].x = startX;
            betButtons[i].y = height - (betButtons.length - i) * (buttonHeight + spacing) + spacing;
        }
    }

    @Override
    public void draw() {
                
        if (cardGame instanceof BlackJack) {
            BlackJack bj = (BlackJack) cardGame;
            boolean showDealerThinking = false;
            if (pendingDealerTurn) {
                dealerThinkingTimer++;
                if (dealerThinkingTimer >= 40) {
                    bj.dealerTurnActive = true;
                    pendingDealerTurn = false;
                    dealerThinkingTimer = 0;
                }
                showDealerThinking = true;
            } else if (bj.dealerTurnActive) {
                showDealerThinking = true;
            }
            if (showDealerThinking) {
                fill(0);
                textSize(16);
                text("Computer is thinking...", width / 2, height / 2 + 80);
            }
            if (!pendingDealerTurn) {
                bj.updateGameDelays(this);
                showEndScreen = bj.shouldShowEndScreen();
            }
        }
        noStroke();
        float cx = width / 2.0f;
        float cy = height / 2.0f;
        float maxR = dist(0, 0, cx, cy);
        for (float r = maxR; r > 0; r -= 1) {
            float t = r / maxR;
            float greyFactor = 0.12f;
            int red = (int) (20 + (1 - t) * 80);
            int green = (int) (50 + (1 - t) * 180);
            int blue = (int) (15 + (1 - t) * 35);
            red = (int)(red * (1 - greyFactor) + 200 * greyFactor);
            green = (int)(green * (1 - greyFactor) + 200 * greyFactor);
            blue = (int)(blue * (1 - greyFactor) + 200 * greyFactor);
            fill(red, green, blue);
            ellipse(cx, cy, r * 2, r * 2);
        }
        for (int i = 0; i < cardGame.playerOneHand.getSize(); i++) {
            Card card = cardGame.playerOneHand.getCard(i);
            if (card != null) {
                card.draw(this);
            }
        }
        for (int i = 0; i < cardGame.playerTwoHand.getSize(); i++) {
            Card card = cardGame.playerTwoHand.getCard(i);
            if (card != null) {
                if (cardGame instanceof BlackJack) {
                    BlackJack bj = (BlackJack) cardGame;
                    if (i == 1) {
                        if (bj.dealerTurnActive || bj.dealerSecondCardRevealed || !bj.gameActive) {
                            card.setTurned(false);
                        } else {
                            card.setTurned(true);
                        }
                    } else {
                        card.setTurned(false);
                    }
                } else {
                    card.setTurned(true);
                }
                card.draw(this);
            }
        }

        int playerCircleX = 60;
        int playerCircleY = 400;
        int computerCircleX = 60;
        int computerCircleY = 220;
        int circleDiameter = 50;
        textSize(20);
        if (cardGame.playerOneHand instanceof BlackJackHand) {
            BlackJackHand bjHand = (BlackJackHand) cardGame.playerOneHand;
            int playerPoints = bjHand.getTotalPoints();
            int softValue = 0;
            int hardValue = 0;
            int aceCount = 0;
            for (Card card : bjHand.getCards()) {
                if (card instanceof BlackJackCard && ((BlackJackCard) card).getValue().equals("A")) {
                    aceCount++;
                }
            }
            for (Card card : bjHand.getCards()) {
                if (card instanceof BlackJackCard) {
                    hardValue += ((BlackJackCard) card).getPointValue();
                }
            }
            softValue = hardValue;
            if (aceCount > 0 && hardValue + 10 <= 21) {
                softValue = hardValue + 10;
            }
            stroke(0);
            strokeWeight(1.25f);
            fill(255, 255, 200);
            ellipse(playerCircleX, playerCircleY, circleDiameter, circleDiameter);
            strokeWeight(1);
            fill(0);
            textAlign(CENTER, CENTER);
            if (aceCount > 0 && softValue != hardValue) {
                textSize(16);
                text(hardValue + "/" + softValue, playerCircleX, playerCircleY);
            } else {
                textSize(20);
                text(playerPoints, playerCircleX, playerCircleY);
            }
            if (cardGame instanceof BlackJack) {
                BlackJack bj = (BlackJack) cardGame;
                if (playerPoints > 21 && !bj.gameActive && !showEndScreen) {
                    fill(220, 0, 0);
                    textSize(24);
                    text("BUST!", playerCircleX + circleDiameter / 2 + 40, playerCircleY);
                    textSize(20);
                }
            }
        }
        if (cardGame.playerTwoHand instanceof BlackJackHand) {
            BlackJackHand bjHand = (BlackJackHand) cardGame.playerTwoHand;
            int visiblePoints = 0;
            int softValue = 0;
            int hardValue = 0;
            int aceCount = 0;
            for (int i = 0; i < bjHand.getSize(); i++) {
                Card card = bjHand.getCard(i);
                if (card != null && !card.turned) {
                    visiblePoints += card.getPointValue();
                    if (card instanceof BlackJackCard && ((BlackJackCard) card).getValue().equals("A")) {
                        aceCount++;
                    }
                }
            }
            for (int i = 0; i < bjHand.getSize(); i++) {
                Card card = bjHand.getCard(i);
                if (card != null && !card.turned && card instanceof BlackJackCard) {
                    hardValue += ((BlackJackCard) card).getPointValue();
                }
            }
            softValue = hardValue;
            if (aceCount > 0 && hardValue + 10 <= 21) {
                softValue = hardValue + 10;
            }
            stroke(0);
            strokeWeight(1.25f);
            fill(220, 240, 255);
            ellipse(computerCircleX, computerCircleY, circleDiameter, circleDiameter);
            strokeWeight(1);
            fill(0);
            textAlign(CENTER, CENTER);
            if (aceCount > 0 && softValue != hardValue) {
                textSize(16);
                text(hardValue + "/" + softValue, computerCircleX, computerCircleY);
            } else {
                textSize(20);
                text(visiblePoints, computerCircleX, computerCircleY);
            }
            if (cardGame instanceof BlackJack) {
                BlackJack bj = (BlackJack) cardGame;
                if (visiblePoints > 21 && !bj.gameActive && !showEndScreen && !bj.dealerTurnActive) {
                    fill(220, 0, 0);
                    textSize(24);
                    text("BUST!", computerCircleX + circleDiameter / 2 + 40, computerCircleY);
                    textSize(20);
                }
            }
        }

        if (cardGame.gameActive && cardGame.getLastPlayedCard() != null) {
            cardGame.getLastPlayedCard().setPosition(width / 2 - 40, height / 2 - 60, 80, 120);
            cardGame.getLastPlayedCard().draw(this);
        }

        if (showEndScreen) {
            if (endScreenTimer > 0) {
                return;
            }
        }
        if (cardGame.gameActive || (showEndScreen && endScreenTimer > 0)) {
            fill(200);
            if (cardGame instanceof BlackJack) {
                BlackJack bj = (BlackJack) cardGame;
                bj.drawBlackJackUI(this);
            } else {
                cardGame.drawButton.draw(this);
                fill(0);
                textAlign(CENTER, CENTER);
                text("Draw", cardGame.drawButton.x + cardGame.drawButton.width / 2,
                    cardGame.drawButton.y + cardGame.drawButton.height / 2);
            }
        } else {
            
            fill(120, 120, 120, 220);
            rect(0, 0, width, height);

            fill(255);
            textAlign(CENTER, CENTER);
            textSize(32);
            String winnerText = "";
            if (cardGame instanceof BlackJack) {
                BlackJack bj = (BlackJack) cardGame;
                winnerText = bj.getEndScreenText();
            } else {
                if (cardGame.getPlayerOneHand().getSize() == 0) {
                    winnerText = "You win!";
                } else if (cardGame.getPlayerTwoHand().getSize() == 0) {
                    winnerText = "Computer wins!";
                }
            }
            text(winnerText, width / 2, height / 2 - 40);

            fill(100, 200, 100);
            cardGame.playAgainButton.draw(this);
            fill(0);
            textSize(20);
            text("Play Again", cardGame.playAgainButton.x + cardGame.playAgainButton.width / 2,
                    cardGame.playAgainButton.y + cardGame.playAgainButton.height / 2);
        }

        
        fill(0);
        textSize(16);
        text("Current Player: " + cardGame.getCurrentPlayer(), width / 2, 20);

        
        text("Deck Size: " + cardGame.getDeckSize(), width / 2,
                height - 20);
        if (cardGame.getCurrentPlayer() == "Player Two") {
            if (cardGame.gameActive) {
                fill(0);
                textSize(16);
                text("Computer is thinking...", width / 2, height / 2 + 80);
                timer++;
                if (timer == 8) {
                    cardGame.handleComputerTurn();
                    timer = 0;
                }
            }
        }

        cardGame.drawChoices(this);
    }

    @Override
    public void mousePressed() {
        if (cardGame.gameActive) {
            if (cardGame instanceof BlackJack) {
                BlackJack bj = (BlackJack) cardGame;
                boolean wasPlayerTurn = bj.playerOneTurn;
                if (bj.handleBlackJackMousePressed(mouseX, mouseY)) {
                    if (wasPlayerTurn && !bj.playerOneTurn && bj.dealerTurnActive) {
                        bj.dealerTurnActive = false;
                        pendingDealerTurn = true;
                        dealerThinkingTimer = 0;
                    }
                    return;
                }
            } else {
                cardGame.handleDrawButtonClick(mouseX, mouseY);
            }
            cardGame.handleCardClick(mouseX, mouseY);
        } else {
            if (cardGame.playAgainButton.isClicked(mouseX, mouseY)) {
                cardGame.initializeGame();
                cardGame.gameActive = true;
                if (cardGame instanceof BlackJack) {
                    BlackJack bj = (BlackJack) cardGame;
                    bj.dealerSecondCardRevealed = false;
                }
            }
        }
    }

}
