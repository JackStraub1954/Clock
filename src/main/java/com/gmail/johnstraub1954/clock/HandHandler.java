package com.gmail.johnstraub1954.clock;

import static com.gmail.johnstraub1954.clock.Constants.CENTER_X;
import static com.gmail.johnstraub1954.clock.Constants.CENTER_Y;
import static com.gmail.johnstraub1954.clock.Constants.RIM_SIDE;
import static com.gmail.johnstraub1954.clock.Constants.RIM_WIDTH;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Point2D;

public class HandHandler
{
    // manager for time-remaining, etc.
    private static final TimeKeeper KEEPER  = TimeKeeper.INSTANCE;
    // number of milliseconds in an minute
    private static final double     MSECS_PER_MIN   = 60 * 1000;
    // number of milliseconds in an hour
    private static final double     MSECS_PER_HOUR  = 60 * MSECS_PER_MIN;
    // number of milliseconds in a half day
    private static final double     MSECS_PER_DAY   = MSECS_PER_HOUR * 12;

    // Graphics context; supplied by instantiator
    private final Graphics2D    gtx;
    
    // hands
    private final MinuteHand        minHand     = new MinuteHand();
    private final SecondHand        secHand     = new SecondHand();
    private final HourHand          hourHand    = new HourHand();
    
    public HandHandler( Graphics2D gtx )
    {
        this.gtx = gtx;
    }
    
    public void place()
    {
        gtx.setStroke( new BasicStroke( 1f ) );
        hourHand.place();
        minHand.place();
        secHand.place();
    }
    
    private class Hand
    {
        public Path2D getPath( Point2D[] polyPoints )
        {
            Path2D  path    = new Path2D.Double();
            path.moveTo( polyPoints[0].getX(), polyPoints[0].getY() );
            for ( int inx = 1 ; inx < polyPoints.length ; ++inx )
            {
                double  xco = polyPoints[inx].getX();
                double  yco = polyPoints[inx].getY();
                path.lineTo( xco, yco);
            }
            path.closePath();
            return path;
        }
    }

    private class SecondHand extends Hand
    {
        private static final double    MAX_WIDTH    = RIM_SIDE * .01;
        private static final double    DESCENT      = MAX_WIDTH;
        private static final double    ASCENT       = RIM_SIDE / 2;
        
        private static final double    X_MAX        = CENTER_X + MAX_WIDTH / 2;
        private static final double    X_MIN        = CENTER_X - MAX_WIDTH / 2;
        private static final double    Y_MAX        = CENTER_Y + DESCENT;
        private static final double    Y_MIN        = CENTER_Y - ASCENT;
        
        private final Path2D    path;
        private final Point2D[] polyPoints  =
        {
            new Point2D.Double( X_MAX, CENTER_Y ),
            new Point2D.Double( CENTER_X, Y_MAX ),
            new Point2D.Double( X_MIN, CENTER_Y ),
            new Point2D.Double( CENTER_X, Y_MIN ),
        };
        private final Color     color       = Color.RED;
        
        public SecondHand()
        {
            path = getPath( polyPoints );
        }
        
        public void place()
        {
            long    remaining   = KEEPER.getTimeRemaining();
//            System.out.println( remaining );
            double  mSecs       = remaining % MSECS_PER_MIN;
            double  part        = mSecs / MSECS_PER_MIN;
            double  angle       = 2 * Math.PI * part;

            AffineTransform trans   = 
                AffineTransform.getRotateInstance( angle, CENTER_X, CENTER_Y );
            Shape   shape   = trans.createTransformedShape( path );
            gtx.setColor( color );
            gtx.fill( shape );
        }
    }

    private class MinuteHand extends Hand
    {
        private static final double    MAX_WIDTH    = RIM_SIDE * .02;
        private static final double    DESCENT      = 2 * MAX_WIDTH;
        private static final double    ASCENT       = RIM_SIDE / 2 - RIM_WIDTH / 2;
        
        private static final double    X_MAX        = CENTER_X + MAX_WIDTH;
        private static final double    X_MIN        = CENTER_X - MAX_WIDTH;
        private static final double    Y_MAX        = CENTER_Y + DESCENT;
        private static final double    Y_MIN        = CENTER_Y - ASCENT;
        
        private final Point2D[] polyPoints  =
        {
            new Point2D.Double( X_MAX, CENTER_Y ),
            new Point2D.Double( CENTER_X, Y_MAX ),
            new Point2D.Double( X_MIN, CENTER_Y ),
            new Point2D.Double( CENTER_X, Y_MIN ),
        };
        private final Path2D    path;
        private final Color     color       = new Color( 0x3c598e  );
        
        public MinuteHand()
        {
            path = getPath( polyPoints );
        }
        
        public void place()
        {
            long    remaining   = KEEPER.getTimeRemaining();
            double  mSecs   = remaining % MSECS_PER_HOUR;
            double  part    = mSecs / MSECS_PER_HOUR;
            double  angle   = 2 * Math.PI * part;

            AffineTransform trans   = 
                AffineTransform.getRotateInstance( angle, CENTER_X, CENTER_Y );
            Shape           shape   = trans.createTransformedShape( path );
            gtx.setColor( color );
            gtx.fill( shape );
        }
    }

    private class HourHand extends Hand
    {
        private static final double    MAX_WIDTH    = RIM_SIDE * .02;
        private static final double    DESCENT      = MAX_WIDTH;
        private static final double    ASCENT       = RIM_SIDE / 2 - RIM_WIDTH / 2;
        
        private static final double    X_MAX        = CENTER_X + MAX_WIDTH;
        private static final double    X_MIN        = CENTER_X - MAX_WIDTH;
        private static final double    Y_MAX        = CENTER_Y + DESCENT;
        private static final double    Y_MIN        = CENTER_Y - ASCENT * .8;
        
        private final Point2D[] polyPoints  =
        {
            new Point2D.Double( X_MAX, CENTER_Y ),
            new Point2D.Double( CENTER_X, Y_MAX ),
            new Point2D.Double( X_MIN, CENTER_Y ),
            new Point2D.Double( CENTER_X, Y_MIN ),
        };
        private final Color     color       = new Color( 0x6495ed );
        private final Path2D    path;
        
        public HourHand()
        {
            path = getPath( polyPoints );
        }
        
        public void place()
        {
            long    remaining   = KEEPER.getTimeRemaining();
            double  mSecs       = remaining % MSECS_PER_DAY;
            double  part        = mSecs / MSECS_PER_DAY;
            double  angle       = 2 * Math.PI * part;

            AffineTransform trans   = 
                AffineTransform.getRotateInstance( angle, CENTER_X, CENTER_Y );
            Shape   shape   = trans.createTransformedShape( path );
            gtx.setColor( color );
            gtx.fill( shape );
        }
    }
}
