import processing.core.PApplet;

public class App extends PApplet {
    private enum ScreenState { START, BET, GAME, STATS, HOW_TO_PLAY }
    private ScreenState screenState = ScreenState.START;
    private int playerBalance = 1000;
    private int currentBet = 0;
    private ClickableRectangle playButton = new ClickableRectangle();
    private ClickableRectangle statsButton = new ClickableRectangle();

    CardGame cardGame = new BlackJack();
    private int timer;

    private ClickableRectangle[] betButtons = new ClickableRectangle[4];
    private final int[] betValues = {50, 100, 250, 500};
    private ClickableRectangle homeButton = new ClickableRectangle();
    private ClickableRectangle getMoreMoneyButton = new ClickableRectangle();
    private boolean showGetMoreMoney = false;
    private ClickableRectangle howToPlayButton = new ClickableRectangle();
    private ClickableRectangle backButton = new ClickableRectangle();
    private int gamesWon = 0, gamesLost = 0, gamesTied = 0;

    public static void main(String[] args) {
        PApplet.main("App");
    }

    @Override
    public void settings() {
        size(600, 600);
    }

    private boolean showEndScreen = false;
    private static final int END_BUTTON_WIDTH = 125;
    private static final int END_BUTTON_HEIGHT = 40;

    private void resetStartScreenLayout() {
        playButton.width = 120;
        playButton.height = 45;
        playButton.x = width / 2 - playButton.width / 2;
        playButton.y = height / 2 - 65;

        statsButton.width = 120;
        statsButton.height = 45;
        statsButton.x = width / 2 - statsButton.width / 2;
        statsButton.y = height / 2 - 5;

        howToPlayButton.width = 120;
        howToPlayButton.height = 45;
        howToPlayButton.x = width / 2 - howToPlayButton.width / 2;
        howToPlayButton.y = height / 2 + 55;

        backButton.width = 110;
        backButton.height = 40;
        backButton.x = width / 2 - backButton.width / 2;
        backButton.y = height - 70;
    }

    private void syncEndScreenButtons() {
        cardGame.playAgainButton.width = END_BUTTON_WIDTH;
        cardGame.playAgainButton.height = END_BUTTON_HEIGHT;
        cardGame.playAgainButton.x = width / 2 - END_BUTTON_WIDTH / 2;
        cardGame.playAgainButton.y = height / 2 + 10;

        homeButton.width = END_BUTTON_WIDTH;
        homeButton.height = END_BUTTON_HEIGHT;
        homeButton.x = width / 2 - END_BUTTON_WIDTH / 2;
        homeButton.y = cardGame.playAgainButton.y + END_BUTTON_HEIGHT + 12;
    }

    private void applyGameResult() {
        if (cardGame instanceof BlackJack) {
            int delta = ((BlackJack) cardGame).getBalanceDelta(currentBet);
            playerBalance += delta;
            if (delta > 0) gamesWon++;
            else if (delta < 0) gamesLost++;
            else gamesTied++;
        }
    }

    @Override
    public void setup() {
        int betScreenButtonWidth = 80;
        int betScreenButtonHeight = 40;
        int betScreenSpacing = 24;

        syncEndScreenButtons();
        int totalWidth = betButtons.length * betScreenButtonWidth + (betButtons.length - 1) * betScreenSpacing;
        int betStartX = width / 2 - totalWidth / 2;
        int betY = height / 2 - 20;
        for (int i = 0; i < betButtons.length; i++) {
            betButtons[i] = new ClickableRectangle();
            betButtons[i].width = betScreenButtonWidth;
            betButtons[i].height = betScreenButtonHeight;
            betButtons[i].x = betStartX + i * (betScreenButtonWidth + betScreenSpacing);
            betButtons[i].y = betY;
        }
        getMoreMoneyButton.width = 160;
        getMoreMoneyButton.height = 38;
        getMoreMoneyButton.x = width / 2 - 80;
        getMoreMoneyButton.y = betY + betScreenButtonHeight + 20;
        resetStartScreenLayout();
    }

    @Override
    public void draw() {
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
        if (screenState == ScreenState.START) {
            resetStartScreenLayout();
            fill(0);
            textAlign(CENTER, CENTER);
            textSize(64);
            text("Blackjack", width / 2, height / 2 - 135);
            strokeWeight(2);
            for (int i = 0; i < playButton.height; i++) {
                float t = (float)i / playButton.height;
                int r = (int)lerp(110, 180, t);
                int g = (int)lerp(10, 40, t);
                int b = (int)lerp(10, 40, t);
                stroke(r, g, b);
                fill(r, g, b);
                rect(playButton.x, playButton.y + i, playButton.width, 1);
            }
            noFill();
            stroke(0);
            rect(playButton.x, playButton.y, playButton.width, playButton.height);
            fill(255);
            textSize(24);
            text("Play", playButton.x + playButton.width / 2, playButton.y + playButton.height / 2);
            strokeWeight(2);
            for (int i = 0; i < statsButton.height; i++) {
                float t = (float)i / statsButton.height;
                int r = (int)lerp(20, 60, t);
                int g = (int)lerp(40, 120, t);
                int b = (int)lerp(90, 220, t);
                stroke(r, g, b);
                fill(r, g, b);
                rect(statsButton.x, statsButton.y + i, statsButton.width, 1);
            }
            noFill();
            stroke(0);
            rect(statsButton.x, statsButton.y, statsButton.width, statsButton.height);
            fill(255);
            text("Stats", statsButton.x + statsButton.width / 2, statsButton.y + statsButton.height / 2);
            strokeWeight(2);
            for (int i = 0; i < howToPlayButton.height; i++) {
                float t = (float)i / howToPlayButton.height;
                int r = (int)lerp(0, 40, t);
                int g = (int)lerp(100, 170, t);
                int b = (int)lerp(90, 160, t);
                stroke(r, g, b);
                fill(r, g, b);
                rect(howToPlayButton.x, howToPlayButton.y + i, howToPlayButton.width, 1);
            }
            noFill();
            stroke(0);
            rect(howToPlayButton.x, howToPlayButton.y, howToPlayButton.width, howToPlayButton.height);
            fill(255);
            text("Guide", howToPlayButton.x + howToPlayButton.width / 2, howToPlayButton.y + howToPlayButton.height / 2);
            noStroke();
            fill(0);
            textSize(18);
            text("Balance: $" + playerBalance, width / 2, height / 2 + 135);
            return;
        }
        if (screenState == ScreenState.BET) {
            fill(0);
            textAlign(CENTER, CENTER);
            textSize(36);
            text("Place Your Bet", width / 2, height / 2 - 120);
            textSize(20);
            text("Balance: $" + playerBalance, width / 2, height / 2 - 80);
            strokeWeight(2);
            for (int i = 0; i < backButton.height; i++) {
                float t = (float)i / backButton.height;
                int r = (int)lerp(110, 180, t);
                int g = (int)lerp(10, 40, t);
                int b = (int)lerp(10, 40, t);
                stroke(r, g, b);
                fill(r, g, b);
                rect(backButton.x, backButton.y + i, backButton.width, 1);
            }
            noFill();
            stroke(0);
            rect(backButton.x, backButton.y, backButton.width, backButton.height);
            fill(255);
            textSize(22);
            text("Back", backButton.x + backButton.width / 2, backButton.y + backButton.height / 2);
            for (int i = 0; i < betButtons.length; i++) {
                stroke(0);
                strokeWeight(2);
                fill(200, 220, 255);
                for (int j = 0; j < betButtons[i].height; j++) {
                    float t = (float)j / betButtons[i].height;
                    int r = (int)lerp(20, 60, t);
                    int g = (int)lerp(40, 120, t);
                    int b = (int)lerp(90, 220, t);
                    stroke(r, g, b);
                    fill(r, g, b);
                    rect(betButtons[i].x, betButtons[i].y + j, betButtons[i].width, 1);
                }
                noFill();
                stroke(0);
                rect(betButtons[i].x, betButtons[i].y, betButtons[i].width, betButtons[i].height);
                fill(255);
                textAlign(CENTER, CENTER);
                text("$" + betValues[i], betButtons[i].x + betButtons[i].width / 2, betButtons[i].y + betButtons[i].height / 2);
            }
            if (playerBalance == 0 && showGetMoreMoney) {
                for (int i = 0; i < getMoreMoneyButton.height; i++) {
                    float t = (float)i / getMoreMoneyButton.height;
                    int r = (int)lerp(160, 220, t);
                    int g = (int)lerp(120, 200, t);
                    int b = (int)lerp(0, 20, t);
                    stroke(r, g, b);
                    fill(r, g, b);
                    rect(getMoreMoneyButton.x, getMoreMoneyButton.y + i, getMoreMoneyButton.width, 1);
                }
                noFill();
                stroke(0);
                strokeWeight(2);
                rect(getMoreMoneyButton.x, getMoreMoneyButton.y, getMoreMoneyButton.width, getMoreMoneyButton.height);
                fill(0);
                textAlign(CENTER, CENTER);
                textSize(18);
                text("Get more money?", getMoreMoneyButton.x + getMoreMoneyButton.width / 2, getMoreMoneyButton.y + getMoreMoneyButton.height / 2);
            }
            if (currentBet > 0) {
                float chipY = betButtons[0].y + betButtons[0].height + 60;
                float chipX = width / 2;
                float chipRadius = 38;
                float chipVert = chipRadius * 0.55f;
                float chipHeight = 18;
                int chipR = 170, chipG = 30, chipB = 30;
                int wallR1 = 170, wallG1 = 30, wallB1 = 30, wallR2 = 110, wallG2 = 10, wallB2 = 10;
                if (currentBet == 100) {
                    chipR = 10; chipG = 90; chipB = 30;
                    wallR1 = 10; wallG1 = 90; wallB1 = 30;
                    wallR2 = 5; wallG2 = 50; wallB2 = 15;
                } else if (currentBet == 250) {
                    chipR = 40; chipG = 80; chipB = 200;
                    wallR1 = 40; wallG1 = 80; wallB1 = 200;
                    wallR2 = 10; wallG2 = 40; wallB2 = 110;
                } else if (currentBet == 500) {
                    chipR = 110; chipG = 40; chipB = 160;
                    wallR1 = 110; wallG1 = 40; wallB1 = 160;
                    wallR2 = 60; wallG2 = 15; wallB2 = 90;
                }
                int sideSteps = 80;
                noStroke();
                for (int i = 0; i < sideSteps; i++) {
                    float theta0 = PI + i * PI / sideSteps;
                    float theta1 = PI + (i + 1) * PI / sideSteps;
                    float x0t = chipX + chipRadius * cos(theta0);
                    float y0t = chipY + chipVert * sin(theta0);
                    float x1t = chipX + chipRadius * cos(theta1);
                    float y1t = chipY + chipVert * sin(theta1);
                    float x0b = x0t;
                    float y0b = y0t + chipHeight;
                    float x1b = x1t;
                    float y1b = y1t + chipHeight;
                    float t = 0.5f + 0.5f * cos((theta0 + theta1) / 2.0f);
                    int r = (int)lerp(wallR1, wallR2, t);
                    int g = (int)lerp(wallG1, wallG2, t);
                    int b = (int)lerp(wallB1, wallB2, t);
                    float highlight = exp(-sq((theta0 - PI * 1.15f) / 0.35f));
                    r += (int)(60 * highlight);
                    g += (int)(40 * highlight);
                    b += (int)(40 * highlight);
                    float colorNoise = 8 * sin(theta0 * 3 + chipY * 0.1f);
                    r += colorNoise;
                    g += colorNoise * 0.5f;
                    fill(r, g, b);
                    beginShape(QUADS);
                    vertex(x0t, y0t);
                    vertex(x1t, y1t);
                    vertex(x1b, y1b);
                    vertex(x0b, y0b);
                    endShape();
                }
                noStroke();
                for (int i = 0; i < 32; i++) {
                    int alpha = (int)lerp(40, 0, i / 32.0f);
                    fill(0, 0, 0, alpha);
                    float shadowX = chipX - 12;
                    float shadowY = chipY + chipHeight + 5;
                    ellipse(shadowX, shadowY, chipRadius * 2.25f - i * 1.1f, chipVert * 1.45f - i * 0.7f);
                }
                stroke(chipR + 10, chipG + 10, chipB + 10);
                strokeWeight(3);
                fill(chipR, chipG, chipB);
                ellipse(chipX, chipY + chipHeight, chipRadius * 2, chipVert * 2);
                stroke(255);
                strokeWeight(5);
                fill(chipR + 30, chipG + 10, chipB + 30);
                ellipse(chipX, chipY, chipRadius * 2, chipVert * 2);
                noStroke();
                for (int i = 0; i < 10; i++) {
                    fill(255, 255, 255, 60 - i * 5);
                    ellipse(chipX - chipRadius * 0.25f, chipY - chipVert * 0.25f, chipRadius * 0.7f - i * 2, chipVert * 0.3f - i * 0.7f);
                }
                stroke(255);
                strokeWeight(3);
                for (int i = 0; i < 8; i++) {
                    float angle = PI / 8 + PI / 4 * i;
                    float x1 = chipX + cos(angle) * (chipRadius - 6);
                    float y1 = chipY + sin(angle) * (chipVert - 4);
                    float x2 = chipX + cos(angle) * (chipRadius - 2);
                    float y2 = chipY + sin(angle) * (chipVert - 1);
                    line(x1, y1, x2, y2);
                }
                noStroke();
                fill(255);
                ellipse(chipX, chipY, chipRadius * 1.2f, chipVert * 1.2f);
                fill(0);
                textAlign(CENTER, CENTER);
                textSize(22);
                text(currentBet + "", chipX, chipY - chipVert * 0.18f);
                strokeWeight(2);
                playButton.y = (int)(chipY + chipRadius + 30);
                for (int i = 0; i < playButton.height; i++) {
                    float t = (float)i / playButton.height;
                    int r = (int)lerp(110, 180, t);
                    int g = (int)lerp(10, 40, t);
                    int b = (int)lerp(10, 40, t);
                    stroke(r, g, b);
                    fill(r, g, b);
                    rect(playButton.x, playButton.y + i, playButton.width, 1);
                }
                noFill();
                stroke(0);
                rect(playButton.x, playButton.y, playButton.width, playButton.height);
                fill(255);
                textSize(24);
                text("Start Game", playButton.x + playButton.width / 2, playButton.y + playButton.height / 2);
            }
            noStroke();
            return;
        }
        if (screenState == ScreenState.STATS) {
            fill(0);
            textAlign(CENTER, CENTER);
            textSize(52);
            text("Stats", width / 2, height / 2 - 190);
            textSize(22);
            int totalGames = gamesWon + gamesLost + gamesTied;
            text("Games Won:  " + gamesWon, width / 2, height / 2 - 80);
            text("Games Lost: " + gamesLost, width / 2, height / 2 - 40);
            text("Games Tied: " + gamesTied, width / 2, height / 2);
            text("Win Rate: " + (totalGames > 0 ? (int)(100.0 * gamesWon / totalGames) + "%" : "N/A"), width / 2, height / 2 + 50);
            text("Balance: $" + playerBalance, width / 2, height / 2 + 100);
            strokeWeight(2);
            for (int i = 0; i < backButton.height; i++) {
                float t = (float)i / backButton.height;
                int r = (int)lerp(110, 180, t);
                int g = (int)lerp(10, 40, t);
                int b = (int)lerp(10, 40, t);
                stroke(r, g, b);
                fill(r, g, b);
                rect(backButton.x, backButton.y + i, backButton.width, 1);
            }
            noFill();
            stroke(0);
            rect(backButton.x, backButton.y, backButton.width, backButton.height);
            fill(255);
            textSize(22);
            text("Back", backButton.x + backButton.width / 2, backButton.y + backButton.height / 2);
            return;
        }
        if (screenState == ScreenState.HOW_TO_PLAY) {
            fill(0);
            textAlign(CENTER, CENTER);
            textSize(52);
            text("How to Play", width / 2, height / 2 - 190);
            textSize(16);
            int lineH = 30;
            int startY = height / 2 - 110;
            text("Goal: Get closer to 21 than the dealer without going over.", width / 2, startY);
            text("Cards 2-10 are worth their face value.", width / 2, startY + lineH);
            text("J, Q, and K are each worth 10.", width / 2, startY + lineH * 2);
            text("Ace is worth 1 or 11, whichever helps most.", width / 2, startY + lineH * 3);
            text("Hit: Draw another card.", width / 2, startY + lineH * 4);
            text("Stand: Keep your hand and let the dealer play.", width / 2, startY + lineH * 5);
            text("Dealer must keep hitting until they reach 17+.", width / 2, startY + lineH * 6);
            text("Blackjack (Ace + 10-value card) is an instant win!", width / 2, startY + lineH * 7);
            strokeWeight(2);
            for (int i = 0; i < backButton.height; i++) {
                float t = (float)i / backButton.height;
                int r = (int)lerp(110, 180, t);
                int g = (int)lerp(10, 40, t);
                int b = (int)lerp(10, 40, t);
                stroke(r, g, b);
                fill(r, g, b);
                rect(backButton.x, backButton.y + i, backButton.width, 1);
            }
            noFill();
            stroke(0);
            rect(backButton.x, backButton.y, backButton.width, backButton.height);
            fill(255);
            textSize(22);
            text("Back", backButton.x + backButton.width / 2, backButton.y + backButton.height / 2);
            return;
        }
        if (cardGame instanceof BlackJack) {
            BlackJack bj = (BlackJack) cardGame;
            bj.updateDealerThinking();
            if (bj.showDealerThinkingText()) {
                fill(0);
                textSize(16);
                text("Computer is thinking...", width / 2, height / 2 + 80);
            }
            bj.updateGameDelays();
            showEndScreen = bj.shouldShowEndScreen();
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

        if (cardGame instanceof BlackJack) {
            BlackJack bj = (BlackJack) cardGame;
            bj.drawScoreUI(this, showEndScreen);
        }

        if (cardGame.gameActive && cardGame.getLastPlayedCard() != null) {
            cardGame.getLastPlayedCard().setPosition(width / 2 - 40, height / 2 - 60, 80, 120);
            cardGame.getLastPlayedCard().draw(this);
        }

        if (cardGame.gameActive) {
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
            syncEndScreenButtons();
            
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

            strokeWeight(2);
            for (int i = 0; i < cardGame.playAgainButton.height; i++) {
                float t = (float)i / cardGame.playAgainButton.height;
                int r = (int)lerp(40, 100, t);
                int g = (int)lerp(120, 200, t);
                int b = (int)lerp(40, 100, t);
                stroke(r, g, b);
                fill(r, g, b);
                rect(cardGame.playAgainButton.x, cardGame.playAgainButton.y + i, cardGame.playAgainButton.width, 1);
            }
            noFill();
            stroke(0);
            strokeWeight(3);
            rect(cardGame.playAgainButton.x, cardGame.playAgainButton.y, cardGame.playAgainButton.width, cardGame.playAgainButton.height);
            fill(255);
            textSize(22);
            textAlign(CENTER, CENTER);
            text("Play Again", cardGame.playAgainButton.x + cardGame.playAgainButton.width / 2,
                    cardGame.playAgainButton.y + cardGame.playAgainButton.height / 2);

            strokeWeight(2);
            for (int i = 0; i < homeButton.height; i++) {
                float t = (float)i / homeButton.height;
                int r = (int)lerp(110, 180, t);
                int g = (int)lerp(10, 40, t);
                int b = (int)lerp(10, 40, t);
                stroke(r, g, b);
                fill(r, g, b);
                rect(homeButton.x, homeButton.y + i, homeButton.width, 1);
            }
            noFill();
            stroke(0);
            strokeWeight(3);
            rect(homeButton.x, homeButton.y, homeButton.width, homeButton.height);
            fill(255);
            textSize(22);
            textAlign(CENTER, CENTER);
            text("Home", homeButton.x + homeButton.width / 2, homeButton.y + homeButton.height / 2);
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
        if (showEndScreen || !cardGame.gameActive) {
            syncEndScreenButtons();
        }
        if (showEndScreen) {
            if (cardGame.playAgainButton.isClicked(mouseX, mouseY)) {
                applyGameResult();
                cardGame.initializeGame();
                showEndScreen = false;
                screenState = ScreenState.BET;
                currentBet = 0;
            } else if (homeButton.isClicked(mouseX, mouseY)) {
                applyGameResult();
                cardGame.initializeGame();
                showEndScreen = false;
                screenState = ScreenState.START;
                currentBet = 0;
            }
            return;
        }
        if (screenState == ScreenState.STATS || screenState == ScreenState.HOW_TO_PLAY) {
            if (backButton.isClicked(mouseX, mouseY)) {
                screenState = ScreenState.START;
            }
            return;
        }
        if (screenState == ScreenState.START) {
            if (playButton.isClicked(mouseX, mouseY)) {
                screenState = ScreenState.BET;
                return;
            }
            if (statsButton.isClicked(mouseX, mouseY)) {
                screenState = ScreenState.STATS;
                return;
            }
            if (howToPlayButton.isClicked(mouseX, mouseY)) {
                screenState = ScreenState.HOW_TO_PLAY;
                return;
            }
            return;
        }
        if (screenState == ScreenState.BET) {
            if (backButton.isClicked(mouseX, mouseY)) {
                currentBet = 0;
                showGetMoreMoney = false;
                screenState = ScreenState.START;
                return;
            }
            for (int i = 0; i < betButtons.length; i++) {
                if (betButtons[i].isClicked(mouseX, mouseY)) {
                    int bet = betValues[i];
                    if (playerBalance >= bet) {
                        currentBet = bet;
                    } else if (playerBalance == 0) {
                        showGetMoreMoney = true;
                    }
                    return;
                }
            }
            if (playerBalance == 0 && showGetMoreMoney && getMoreMoneyButton.isClicked(mouseX, mouseY)) {
                playerBalance = 1000;
                showGetMoreMoney = false;
                return;
            }
            if (currentBet > 0 && playButton.isClicked(mouseX, mouseY)) {
                cardGame = new BlackJack();
                screenState = ScreenState.GAME;
                return;
            }
            return;
        }
        if (cardGame.gameActive) {
            if (cardGame instanceof BlackJack) {
                BlackJack bj = (BlackJack) cardGame;
                boolean wasPlayerTurn = bj.playerOneTurn;
                if (bj.handleBlackJackMousePressed(mouseX, mouseY)) {
                    if (wasPlayerTurn && !bj.playerOneTurn && bj.dealerTurnActive) {
                        bj.beginDealerThinking();
                    }
                    return;
                }
            } else {
                cardGame.handleDrawButtonClick(mouseX, mouseY);
            }
            cardGame.handleCardClick(mouseX, mouseY);
        } else {
            if (cardGame.playAgainButton.isClicked(mouseX, mouseY)) {
                applyGameResult();
                cardGame.initializeGame();
                showEndScreen = false;
                screenState = ScreenState.BET;
                currentBet = 0;
            } else if (homeButton.isClicked(mouseX, mouseY)) {
                applyGameResult();
                cardGame.initializeGame();
                showEndScreen = false;
                screenState = ScreenState.START;
                currentBet = 0;
            }
        }
    }

}
