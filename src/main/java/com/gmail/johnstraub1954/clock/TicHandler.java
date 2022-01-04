package com.gmail.johnstraub1954.clock;

import static com.gmail.johnstraub1954.clock.Constants.CENTER_X;
import static com.gmail.johnstraub1954.clock.Constants.CENTER_Y;

import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public class TicHandler
{
    // Graphics context; supplied by instantiator
    private final Graphics2D    gtx;
    
    // Clock radius; supplied by instantiator
    private final double        radius;
    
    // Major tic size: height as percentage of radius
    private static final double ticPercentOfRadius  = .1; 
//    private final double        ticHeight;
    
    // Major tic size: width as percentage of height
    private static final double ticPercentOfHeight  = .15;
//    private final double        ticWidth;
    
    // Minor tic size: percentage of major tic size
    private static final double ticPercentOfMajor   = .5;
    
    // base rectangles for tics
    private final Rectangle2D   majorTic;
    private final Rectangle2D   minorTic;
    
    public TicHandler( Graphics2D gtx, double radius )
    {
        this.gtx = gtx;
        this.radius = radius;
        
        double  ticHeight = radius * ticPercentOfRadius;
        double  ticWidth = ticHeight * ticPercentOfHeight;
        majorTic = 
            new Rectangle2D.Double( -ticWidth / 2, 0, ticWidth, ticHeight );
        
        ticHeight *= ticPercentOfMajor;
        ticWidth *= ticPercentOfMajor;
        minorTic = 
            new Rectangle2D.Double( -ticWidth / 2, 0, ticWidth, ticHeight );
    }
    
    public Polygon polygify( final Point2D[] points )
    {
        int     len     = points.length;
        int[]   xcos    = new int[len];
        int[]   ycos    = new int[len];
        for ( int inx = 0 ; inx < len ; ++inx )
        {
            xcos[inx] = round( points[inx].getX() );
            ycos[inx] = round( points[inx].getY() );
        }
        Polygon poly    = new Polygon( xcos, ycos, len );
        return poly;
    }
    
    private int round( double dNum )
    {
        return (int)(dNum + .5);
    }
    
    public void place( boolean major, double angle )
    {
        Rectangle2D tic = major ? majorTic : minorTic; 
        // Center of top edge of tic
        double      centerX = CENTER_X;// - ticWidth / 2;
        double      centerY = CENTER_Y;
        
        // translated center
        double      tAngle  = -angle + Math.PI / 2;
        double      tcX     = centerX + radius * Math.cos( angle );
        double      tcY     = centerY - radius * Math.sin( angle );

        AffineTransform linearTransform     = 
            AffineTransform.getTranslateInstance(tcX, tcY);
        Shape   shape   = linearTransform.createTransformedShape( tic );
        AffineTransform rotateTransform =
            AffineTransform.getRotateInstance( tAngle, tcX, tcY );
        shape = rotateTransform.createTransformedShape(shape);
//        if ( !major )
//        {
//            AffineTransform scaleTransform  = AffineTransform.getScaleInstance(ticPercentOfMajor, ticPercentOfMajor);
//            shape = scaleTransform.createTransformedShape(shape);
//        }
        gtx.fill( shape );
    }
}
