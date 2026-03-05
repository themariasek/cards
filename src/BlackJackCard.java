import processing.core.PApplet;

public class BlackJackCard extends Card {
    @Override
    public int getPointValue() {
        String v = getValue();
        if (v.equals("A")) {
            return 1;
        } else if (v.equals("K") || v.equals("Q") || v.equals("J")) {
            return 10;
        } else {
            try {
                return Integer.parseInt(v);
            } catch (NumberFormatException e) {
                return 0;
            }
        }
    }
    public BlackJackCard(String value, String suit) {
        super(value, suit);
    }
    
    @Override
    public void draw(PApplet sketch) {
        super.draw(sketch);
    }
}
