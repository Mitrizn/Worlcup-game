import javafx.scene.image.*;
import javafx.scene.canvas.*;
import javafx.geometry.*;

/*
 * Store data and methods
 * for movable images on screen in a game.
 */
public class Sprite
{
    public double x;
    public double y;
    public double width;
    public double height;
    public Image  pic;
    
    public double distanceX, distanceY;
    
    // constructor
    public Sprite(double _x, double _y, double _width, double _height, String fileName)
    {
        x = _x;
        y = _y;
        width = _width;
        height = _height;
        pic = new Image(fileName, width, height, false, true);
    }
    
    // move the sprite by changing the x and y coordinates
    public void move(double dx, double dy)
    {
        x = x + dx;
        y = y + dy;
    }
    
    // draw the image on a canvas using a graphics context
    public void draw(GraphicsContext context)
    {
        context.drawImage( pic, x, y );
    }
    
    // check if this sprite overlaps with another sprite (boundary rectangles)
    public boolean overlaps(Sprite other)
    {
        // represents the boundary of this sprite
        Rectangle2D rect1 = new Rectangle2D(x,y, width,height);
        
        // boundary of other sprite
        Rectangle2D rect2 = new Rectangle2D(other.x, other.y, other.width, other.height);
        
        // check if they overlap
        boolean overlap = rect1.intersects( rect2 );
        
        // return the result
        return overlap;
    }
    
    public double getX()
    {
        return this.x;
    }
    
    public double getY()
    {
        return this.y;
    }
    
    // two tasks: keep sprite on screen, and reverse direction
    public void wallBounce()
    {
        // left wall
        if (x < 0)
        {
            x = 0;
            distanceX *= -1;
        }
        
        // top wall
        if (y < 0)
        {
            y = 0;
            distanceY *= -1;
        }
        
        // right wall
        if (x > 600 - width)
        {
            x = 600 - width;
            distanceX *= -1;
        }
        
        // bottom wall
        if (y > 600 - height)
        {
            y = 600 - height;
            distanceY *= -1;
        }
    }
    
    
    
    
    
    
    
    
    
    
}