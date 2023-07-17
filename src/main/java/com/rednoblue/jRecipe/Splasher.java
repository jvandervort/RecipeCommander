package com.rednoblue.jRecipe;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.logging.Logger;

public class Splasher {

  private final static Logger LOGGER = Logger.getLogger(Splasher.class.getName());

  public static void main(String[] args) {
    // Read the image data and display the splash screen

    // -------------------------------------------------
    Frame splashFrame = null;
    URL imageURL = Splasher.class.getResource("/com/rednoblue/jRecipe/Images/splash.gif");
    if (imageURL != null) {
      splashFrame = SplashWindow.splash(Toolkit.getDefaultToolkit().createImage(imageURL));
    } else {
      LOGGER.warning("Splash image not found");
    }

    // Call the main method of the application using Reflection
    // --------------------------------------------------------
    try {
      Class.forName("com.rednoblue.jRecipe.AppFrame").getMethod("main", new Class[]{String[].class}).invoke(null, new Object[]{args});
    } catch (Throwable e) {
      LOGGER.severe(e.toString());
      e.printStackTrace();
      System.exit(10);
    }
    // Dispose the splash screen
    // -------------------------
    if (splashFrame != null) {
      splashFrame.dispose();
    }
  }
}

class SplashWindow extends Window {

  private Image splashImage;
  private boolean paintCalled = false;

  public SplashWindow(Frame owner, Image splashImage) {
    super(owner);
    this.splashImage = splashImage;


    // Load the image
    MediaTracker mt = new MediaTracker(this);
    mt.addImage(splashImage, 0);
    try {
      mt.waitForID(0);
    } catch (InterruptedException ie) {
    	ie.printStackTrace();
    }

    // Center the window on the screen.
    int imgWidth = splashImage.getWidth(this);
    int imgHeight = splashImage.getHeight(this);

    setSize(imgWidth, imgHeight);
    Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
    setLocation((screenDim.width - imgWidth) / 2, (screenDim.height - imgHeight) / 2);

    MouseAdapter disposeOnClick = new MouseAdapter() {

      @Override
      public void mouseClicked(MouseEvent evt) {
        // Note: To avoid that method splash hangs, we
        // must set paintCalled to true and call notifyAll.
        // This is necessary because the mouse click may
        // occur before the contents of the window
        // has been painted.
        synchronized (SplashWindow.this) {
          SplashWindow.this.paintCalled = true;
          SplashWindow.this.notifyAll();
        }
        dispose();
      }
    };
    addMouseListener(disposeOnClick);

  }

  @Override
  public void update(Graphics g) {
    g.setColor(getForeground());
    paint(g);
  }

  @Override
  public void paint(Graphics g) {
    g.drawImage(splashImage, 0, 0, this);

    // Notify method splash that the window
    // has been painted.
    if (!paintCalled) {
      paintCalled = true;
      synchronized (this) {
        notifyAll();
      }
    }
  }

  /**
   * Constructs and displays a SplashWindow.<p>
   * This method is useful for startup splashs.
   * Dispose the returned frame to get rid of the splash window.<p>
   *
   * @param splashImage The image to be displayed.
   * @return Returns the frame that owns the SplashWindow.
   */
  public static Frame splash(Image splashImage) {
    Frame f = new Frame();
    SplashWindow w = new SplashWindow(f, splashImage);

    // Show the window.
    w.toFront();
    w.setVisible(true);



    // Note: To make sure the user gets a chance to see the
    // splash window we wait until its paint method has been
    // called at least once by the AWT event dispatcher thread.
    if (!EventQueue.isDispatchThread()) {
      synchronized (w) {
        while (!w.paintCalled) {
          try {
            w.wait();
          } catch (InterruptedException e) {
        	  e.printStackTrace();
          }
        }
      }
    }
    return f;
  }
}
