
import processing.core.PImage;
import processing.core.PApplet;

public class Card extends ClickableRectangle {
    String value;
    String suit;
    PImage img;
    boolean turned = false;
    private int clickableWidth = 30; // Width of the left sliver that is clickable
    private boolean selected = false;
    private int baseY;
    private boolean hasBaseY = false;

    public String getValue() {
        return value;
    }

    public int getPointValue() {
        return 0;
    }

    Card(String value, String suit) {
        this.value = value;
        this.suit = suit;
    }

    Card(String value, String suit, PImage img) {
        this.value = value;
        this.suit = suit;
        this.img = img;
    }

    public void setTurned(boolean turned) {
        this.turned = turned;
    }

    public void setClickableWidth(int width) {
        this.clickableWidth = width;
    }

    public void setSelected(boolean selected, int raiseAmount) {
        if (selected && !this.selected) {
            baseY = y;
            hasBaseY = true;
            y = baseY - raiseAmount;
        } else if (!selected && this.selected && hasBaseY) {
            y = baseY;
        }
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean isClicked(int mouseX, int mouseY) {
        // Only the left sliver of the card is clickable
        return mouseX >= x && mouseX <= x + clickableWidth &&
                mouseY >= y && mouseY <= y + height;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setSize(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setPosition(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @Override
    public void draw(PApplet sketch) {
        if (turned) {
            sketch.stroke(0);
            sketch.strokeWeight(1.25f);
            sketch.fill(150);
            sketch.rect(x, y, width, height);
            sketch.strokeWeight(1);
            return;
        }
        if (isSelected()) {
            sketch.stroke(0);
            sketch.strokeWeight(4);
        } else {
            sketch.stroke(0);
            sketch.strokeWeight(2);
        }
        if (img != null) {
            sketch.image(img, x, y, width, height);
        } else {
            sketch.fill(255);
            sketch.rect(x, y, width, height);
            if (suit.equals("Hearts")) {
                sketch.push();
                sketch.translate(x + width / 2, y + height / 2 + 6);
                sketch.scale(0.9f);
                sketch.noStroke();
                sketch.fill(255, 0, 0);
                sketch.beginShape();
                sketch.vertex(0, -19);
                sketch.bezierVertex(14, -38, 44, -20, 0, 20);
                sketch.bezierVertex(-44, -20, -14, -38, 0, -19);
                sketch.endShape(PApplet.CLOSE);
                sketch.pop();
                sketch.stroke(0);
            } else if (suit.equals("Diamonds")) {
                sketch.push();
                sketch.translate(x + width / 2, y + height / 2 + 2);
                sketch.scale(1.0f, 0.9f);
                sketch.noStroke();
                sketch.fill(255, 0, 0);
                sketch.beginShape();
                sketch.vertex(0, -30);
                sketch.vertex(20, 0);
                sketch.vertex(0, 30);
                sketch.vertex(-20, 0);
                sketch.endShape(PApplet.CLOSE);
                sketch.pop();
                sketch.stroke(0);
            } else if (suit.equals("Clubs")) {
                sketch.push();
                sketch.translate(x + width / 2, y + height / 2);
                sketch.noStroke();
                sketch.fill(0);
                sketch.ellipse(0, -10, 22, 22);
                sketch.ellipse(-11, 6, 22, 22);
                sketch.ellipse(11, 6, 22, 22);
                sketch.ellipse(0, 2, 18, 18);
                sketch.beginShape();
                sketch.vertex(-7, 22);
                sketch.vertex(7, 22);
                sketch.vertex(0, 8);
                sketch.endShape(PApplet.CLOSE);
                sketch.pop();
                sketch.stroke(0);
            } else if (suit.equals("Spades")) {
                sketch.push();
                sketch.translate(x + width / 2, y + height / 2 - 4);
                sketch.scale(0.9f, 0.85f);
                sketch.noStroke();
                sketch.fill(0);
                sketch.rotate((float)Math.PI);
                sketch.beginShape();
                sketch.vertex(0, -20);
                sketch.bezierVertex(20, -40, 40, -20, 0, 20);
                sketch.bezierVertex(-40, -20, -20, -40, 0, -20);
                sketch.endShape(PApplet.CLOSE);
                float tailHalfWidth = 4;
                float tailLength = 16;
                sketch.beginShape();
                sketch.vertex(-tailHalfWidth, -32);
                sketch.vertex(tailHalfWidth, -32);
                sketch.vertex(0, -32 + tailLength);
                sketch.endShape(PApplet.CLOSE);
                sketch.pop();
                sketch.stroke(0);
            }
            if (suit.equals("Hearts") || suit.equals("Diamonds")) {
                sketch.fill(255, 0, 0);
            } else {
                sketch.fill(0);
            }
                sketch.textSize(16); // Ensure card value text size is always consistent
                sketch.text(value, x + 10, y + 10);

            sketch.fill(0);
            sketch.text(value, x + 10, y + 10);
        }
        sketch.strokeWeight(1);
    }
}
