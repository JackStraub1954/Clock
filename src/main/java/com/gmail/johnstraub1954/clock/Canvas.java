package com.gmail.johnstraub1954.clock;

import static com.gmail.johnstraub1954.clock.Constants.BG_COLOR;
import static com.gmail.johnstraub1954.clock.Constants.CENTER_X;
import static com.gmail.johnstraub1954.clock.Constants.CENTER_Y;
import static com.gmail.johnstraub1954.clock.Constants.CL_PART;
import static com.gmail.johnstraub1954.clock.Constants.EDGE_COLOR;
import static com.gmail.johnstraub1954.clock.Constants.EDGE_SIDE;
import static com.gmail.johnstraub1954.clock.Constants.EDGE_X;
import static com.gmail.johnstraub1954.clock.Constants.EDGE_Y;
import static com.gmail.johnstraub1954.clock.Constants.FACE_COLOR;
import static com.gmail.johnstraub1954.clock.Constants.FACE_RADIUS;
import static com.gmail.johnstraub1954.clock.Constants.FACE_X;
import static com.gmail.johnstraub1954.clock.Constants.FACE_Y;
import static com.gmail.johnstraub1954.clock.Constants.ICL_WIDTH;
import static com.gmail.johnstraub1954.clock.Constants.IEDGE_WIDTH;
import static com.gmail.johnstraub1954.clock.Constants.IRIM_WIDTH;
import static com.gmail.johnstraub1954.clock.Constants.RIM_SIDE;
import static com.gmail.johnstraub1954.clock.Constants.RIM_X;
import static com.gmail.johnstraub1954.clock.Constants.RIM_Y;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import javax.swing.JPanel;

public class Canvas extends JPanel
{    
    /**
     *  Generated serial version UID
     */
    private static final long serialVersionUID = 1739319586402909129L;
    
    // Guides are for examining text positioning during development/testing
    private static final Line2D[]   guides;
        
    // origin of rim's enclosing rectangle
    private static final double RECT_X      = RIM_X;
    private static final double RECT_Y      = RIM_Y;
    // circle for rim of clock
    private static final Ellipse2D  RIM    = 
        new Ellipse2D.Double( RECT_X, RECT_Y, RIM_SIDE, RIM_SIDE );     
    // circle for rim of clock
    private static final Ellipse2D  EDGE   = 
        new Ellipse2D.Double( EDGE_X, EDGE_Y, EDGE_SIDE, EDGE_SIDE );     
    // circle for face of clock
    private static final Ellipse2D  FACE    = 
        new Ellipse2D.Double( FACE_X, FACE_Y, RIM_SIDE, RIM_SIDE );   
    
    // Gradient paint for clock face; lighter upper left, darker lower right
    private static final Color      gpFaceColorNW       = 
        new Color( 225, 225, 225 );
    private static final Color      gpFaceColorSE       = 
        new Color( 125, 125, 125 );
    private static final Point2D    gpFacePositionNW    = 
        new Point2D.Double( FACE_X, FACE_Y );
    private static final Point2D    gpFacePositionSE    = 
        new Point2D.Double( FACE_X + EDGE_SIDE, FACE_Y + EDGE_SIDE );
    private static final GradientPaint  gpFacePaint     =
        new GradientPaint( 
            gpFacePositionNW, 
            gpFaceColorNW, 
            gpFacePositionSE, 
            gpFaceColorSE 
        );
    
    // Gradient paint for clock Rim; lighter upper left, darker lower right
    private static final Color      gpRimColorNW       = 
        new Color( 0xCE4407 );
    private static final Color      gpRimColorSE       = 
        new Color( 0x7C1004 );
    private static final Point2D    gpRimPositionNW    = 
        new Point2D.Double( RIM_X, RIM_Y );
    private static final Point2D    gpRimPositionSE    = 
        new Point2D.Double( RIM_X + RIM_SIDE, RIM_Y + RIM_SIDE );
    private static final GradientPaint  gpRimPaint     =
        new GradientPaint( 
            gpRimPositionNW, 
            gpRimColorNW, 
            gpRimPositionSE, 
            gpRimColorSE 
        );
    
    // 12 angles representing the positions on the face of an analog clock
    private static final double     nums[];
    
    static
    {
        nums = new double[12];
        guides = new Line2D.Double[12];
        double  workingRadius   = FACE_RADIUS + 15;
        for ( int inx = 0 ; inx < 12 ; ++inx )
        {
            double  angle   = inx * CL_PART;
            double  xco     = CENTER_X + workingRadius * Math.cos( angle );
            double  yco     = CENTER_Y - workingRadius * Math.sin( angle );
            guides[inx]     =
                new Line2D.Double( CENTER_X, CENTER_Y, xco, yco );
            
            nums[inx] = inx * CL_PART;
        }
    }
    
    public Canvas()
    {
        Dimension   dim = new Dimension( ICL_WIDTH, ICL_WIDTH );
        setPreferredSize( dim );
    }
    
    public void paintComponent( Graphics graphics )
    {
        Graphics2D  gtx     = (Graphics2D)(graphics.create());
        gtx.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );
        int         width   = getWidth();
        int         height  = getHeight();
        gtx.setColor( BG_COLOR );
        gtx.fillRect( 0, 0, width, height );

        gtx.setColor( FACE_COLOR );
        
        Paint   oldPaint    = gtx.getPaint();
        gtx.setPaint( gpFacePaint );
        gtx.fill( FACE );
        
//        gtx.setColor( RIM_COLOR );
        gtx.setPaint( gpRimPaint );
        gtx.setStroke( new BasicStroke( IRIM_WIDTH ) );
        gtx.draw( RIM );
        gtx.setPaint( oldPaint );

        gtx.setStroke( new BasicStroke( IEDGE_WIDTH ) );
        gtx.setColor( EDGE_COLOR );
        gtx.draw( EDGE );
        gtx.setColor( FACE_COLOR );  
        
        // Enable guides for visual debugging...
        // ... draws guides outward from radius at key angles.
        // This makes visual verification of component positioning
        // easier.
//        for ( Line2D guide : guides )
//            gtx.draw( guide );
        
        Font        font    = gtx.getFont();
        float       size    = font.getSize2D();
        Font        newFont = font.deriveFont( size * 2 );
        gtx.setFont( newFont );
        gtx.setColor( Color.BLACK );
        
        StringHandler   handler = new StringHandler( gtx, FACE_RADIUS );
        for ( int inx = 0 ; inx < nums.length ; ++inx )
        {
            // Use inx as angle relative to X axis (as usual)...
            // angle = 0 -> 3:00, angle = 1 = 2:00, etc.
            // Adjust * 5 = "display 15 units remaining" at 3:00 position, etc.
            int     relTime = ((inx + 3) % 12);
            String  str = "" + relTime;
            double  angle   = nums[inx];
            handler.place( str, angle );
        }
        
        TicHandler      tHandler    = new TicHandler( gtx, FACE_RADIUS );
        for ( int inx = 0 ; inx < 60 ; ++inx )
        {
            // Use inx as angle relative to X axis (as usual)...
            // angle = 0 -> 3:00, angle = 1 = 2:00, etc.
            // Adjust * 5 = "display 15 units remaining" at 3:00 position, etc.
            double      angle   = Math.PI * 2. / 60. * inx;
            boolean     isMajor = inx % 5 == 0;
            tHandler.place( isMajor, angle );
        }
        
        HandHandler hHandler    = new HandHandler( gtx );
        hHandler.place();
        gtx.dispose();
    }
    
//    private Image getTexture()
//    {
//        String    texturePath     = "pexels-lukas-wood-cropped-349610.jpg";
//
//        ClassLoader loader      = Canvas.class.getClassLoader();
//        InputStream inStream    = 
//                loader.getResourceAsStream( texturePath );
//        if ( inStream == null )
//        {
//            String  message = 
//                "Failed to load texture input stream: " + texturePath;
//            throw new IllegalStateException( message );
//        }
//        
//        Image   image   = null;
//        try
//        {
//            image = ImageIO.read( inStream );
//        }
//        catch ( IOException exc )
//        {
//            String  message = 
//                "Failed to load texture file: " + texturePath;
//            throw new IllegalStateException( message );
//        }
//        
//        image = image.getScaledInstance( (int)RIM_SIDE, -1, Image.SCALE_SMOOTH );
//        return image;
//    }
}
