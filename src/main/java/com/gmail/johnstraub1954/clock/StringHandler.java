package com.gmail.johnstraub1954.clock;

import static com.gmail.johnstraub1954.clock.Constants.CENTER_X;
import static com.gmail.johnstraub1954.clock.Constants.CENTER_Y;

import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

//private static  STRING_RADIUS   = .75 * 

public class StringHandler
{
    private final Graphics2D    gtx;
    private final double        radius;
    private final FontMetrics   metrics;
    private final double        ascent;
    
    public StringHandler( Graphics2D gtx, double radius )
    {
        this.gtx = gtx;
        this.radius = radius;
        metrics = gtx.getFontMetrics();
        ascent = metrics.getAscent();
    }
    
    public void place( String str,  double angle ) 
    {
        Rectangle2D bounds  = metrics.getStringBounds( str, gtx );
        double      sWidth  = bounds.getWidth();
        double      sHeight = bounds.getHeight();
        
        // Center of bounding rectangle...
        // origin of bounding rectangle is relative to baseline;
        // must be adjusted for ascent.
        double      centerX = sWidth / 2;
        double      centerY = sHeight / 2 - ascent;
        
        // how far to translate
        double      tRadius = radius * .75;
        
        // translated center
        double      tcX     = centerX + tRadius * Math.cos( angle );
        double      tcY     = centerY - tRadius * Math.sin( angle );
        
        // translated origin of string
        double      tsX     = CENTER_X + tcX - sWidth;
        double      tsY     = CENTER_Y - tcY;        
        gtx.drawString( str, (float)tsX, (float)tsY );
        
        // Enable bounding box for visual debugging...
        // ... draws a box around the string.
        // translated origin of bounding rectangle
//        double      tbX     = tsX;
//        double      tbY     = tsY - ascent;
        
//        Rectangle2D rect    = new Rectangle2D.Double( tbX, tbY, sWidth, sHeight );
//        gtx.draw( rect );
//        double  ellX    = CENTER_X - tRadius;
//        double  ellY    = CENTER_Y -tRadius;
        
        // Center of bounding box is on this circle
//        Ellipse2D   ell     = new Ellipse2D.Double(ellX, ellY, tRadius * 2, tRadius * 2);
//        gtx.draw( ell );
    }
}
