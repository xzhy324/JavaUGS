package drawer;

import java.awt.Canvas;
import java.awt.Graphics;
import java.awt.Image;

public class MyCanvas extends Canvas {
    private Image image = null;
    public void setImage(Image image) {
        this.image = image;
    }
    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, null);
    }
    public void update(Graphics g) {
        paint(g);
    }
}