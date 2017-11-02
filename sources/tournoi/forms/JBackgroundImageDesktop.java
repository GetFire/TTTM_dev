/*
 *
 */
package tournoi.forms;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JDesktopPane;

public class JBackgroundImageDesktop 
    extends JDesktopPane { 

  /**
     * 
     */
    private static final long serialVersionUID = 1L;
private Image backgroundImage = null; 
  int positionx = 0; 
  int positiony = 0; 
  int sizex = 0; 
  int sizey = 0; 
  boolean windowsize = false; 

  /** 
   * Constructor 
   */ 
  public JBackgroundImageDesktop() { 
    super(); 
  } 

  /** 
   * Returns the background image 
   * @return    Background image 
   */ 
  public Image getBackgroundImage() { 
    return backgroundImage; 
  } 

  /** 
   * Sets the background image 
   * @param backgroundImage    Background image 
   */ 
  public void setBackgroundImage(Image backgroundImage) { 
    this.backgroundImage = backgroundImage; 
  } 

  /** 
   * Sets the background image, its position and its size 
   * @param backgroundImage    Background image 
   * @param positionx     Position x 
   * @param positiony     Position y 
   * @param sizex     Size x 
   * @param sizey     Size y 
   */ 

  public void setBackgroundImage(Image backgroundImage, int positionx, 
                                 int positiony, int sizex, int sizey) { 
    this.backgroundImage = backgroundImage; 
    this.positionx = positionx; 
    this.positiony = positiony; 
    this.sizex = sizex; 
    this.sizey = sizey; 
  } 

  /** 
   * Sets the background image and its position 
   * @param backgroundImage    Background image 
   * @param positionx     Position x 
   * @param positiony     Position y 
   */ 

  public void setBackgroundImage(Image backgroundImage, int positionx, 
                                 int positiony) { 
    this.backgroundImage = backgroundImage; 
    this.positionx = positionx; 
    this.positiony = positiony; 
  } 

  /** 
   * Sets the background image and its size as large as the window 
   * @param backgroundImage    Background image 
   * @param windowsize     Should the background image be als large as the window ? 
   */ 

  public void setBackgroundImage(Image backgroundImage, boolean windowsize) { 
    this.backgroundImage = backgroundImage; 
    this.windowsize = windowsize; 

  } 

  

  /** 
   * Overrides the painting to display a background image 
   */ 
  protected void paintComponent(Graphics g) { 
    if (isOpaque()) { 
      g.setColor(getBackground()); 
      g.fillRect(0, 0, getWidth(), getHeight()); 
    } 
    if (backgroundImage != null) { 
      if (sizex != 0 && sizey != 0) { 
        g.drawImage(backgroundImage, positionx, positiony, sizex, sizey, this); 
      } 
      else if (windowsize) { 
        g.drawImage(backgroundImage, positionx, positiony, getWidth(), 
                    getHeight(), this); 
      } 
      else { 
        g.drawImage(backgroundImage, positionx, positiony, this); 
      } 
    } 
  } 

} 
