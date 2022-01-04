package com.gmail.johnstraub1954.clock;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ClipTest
{
    private final JFrame    frame   = new JFrame( "Clip Test" );
    
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater( () -> new ClipTest().buildGUI() );
    }
    
    public void buildGUI()
    {
        Canvas  canvas  = null;
        try 
        {
            canvas = new Canvas();
        }
        catch ( IOException exc )
        {
            exc.printStackTrace();
            System.exit( 1 );
        }
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( canvas );
        frame.pack();
        frame.setVisible( true );
    }
    
    private class Canvas extends JPanel
    {
        private final int       defWidth        = 300;
        private final int       defHeight       = defWidth;
        private final Dimension size            = new Dimension( defWidth, defHeight ); 
        private final Shape     workingShape    =
            new Ellipse2D.Double( 0, 0, defWidth, defHeight );
        private final String    texturePath     = "photos_2015_09_18_fst_698oway7o7h.jpg";
//        private final String    texturePath     = "abstract-orange-background.jpg";
        private final Image     texture;
        
        private Graphics2D  gtx;
        private int         currWidth;
        private int         currHeight;
        
        public Canvas() throws IOException
        {
            setPreferredSize( size );
            texture = getTexture();
        }
        
        @Override
        public void paintComponent( Graphics graphics )
        {
            gtx = (Graphics2D)graphics.create();
            currWidth = getWidth();
            currHeight = getHeight();
            gtx.setColor( Color.DARK_GRAY );
            gtx.fillRect( 0, 0, currWidth, currHeight );
            
            gtx.setClip( workingShape );    
            gtx.drawImage( texture, 0, 0, null );
        }
        
        private BufferedImage getTexture()
        {
            ClassLoader loader      = ClipTest.class.getClassLoader();
            InputStream inStream    = 
                    loader.getResourceAsStream( texturePath );
            if ( inStream == null )
            {
                String  message = 
                    "Failed to load texture input stream: " + texturePath;
                throw new IllegalStateException( message );
            }
            
            BufferedImage   image   = null;
            try
            {
                image = ImageIO.read( inStream );
            }
            catch ( IOException exc )
            {
                String  message = 
                    "Failed to audio texture file: " + texturePath;
                throw new IllegalStateException( message );
            }
            
            return image;
        }
    }
}
