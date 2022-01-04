package com.gmail.johnstraub1954.clock;

import static java.lang.Math.PI;

import java.awt.Color;

public class Constants
{
    // twelfth part of a circle;
    //  12:00 =  3 * CLOCK_PART
    //   1:00 =  2 * CLOCK_PART
    //   2:00 =  1 * CLOCK_PART
    //   3:00 =  0 * CLOCK_PART
    //   4:00 = 11 * CLOCK_PART
    //   5:00 = 10 * CLOCK_PART
    //   etc.
    public static final double      CL_PART     = 2. * PI / 12.;
    
    // width/height of clock window
    public static final double      CL_WIDTH    = 360;
    public static final int         ICL_WIDTH   = round( CL_WIDTH );
    
    // center of clock window
    public static final double      CENTER_X    = CL_WIDTH / 2;
    public static final double      CENTER_Y    = CL_WIDTH / 2;
    
    // side of bounding box for rim of clock
    public static final double      RIM_SIDE    = CL_WIDTH * .8;
    // width of the stroke used to draw rim
    public static final double      RIM_WIDTH   = RIM_SIDE * .05;
    public static final int         IRIM_WIDTH  = round( RIM_WIDTH );    
    // coordinates of bounding box for rim of clock
    public static final double      RIM_X       = (CL_WIDTH - RIM_SIDE) / 2;
    public static final double      RIM_Y       = (CL_WIDTH - RIM_SIDE) / 2;
    
    // coordinates of bounding box for face
    public static final double      FACE_X      = RIM_X;
    public static final double      FACE_Y      = RIM_Y;
    // width of stroke to paint miscellaneous elements of face
    public static final int         FACE_STROKE = 1;
    // radius of clock face
    public  static final double     FACE_RADIUS = RIM_SIDE / 2;
    
    // dimensions of bounding box for edge;
    // must be adjusted for rim width
    public static final double      EDGE_X      = FACE_X - IRIM_WIDTH / 2;
    public static final double      EDGE_Y      = FACE_Y - IRIM_WIDTH / 2;
    public static final double      EDGE_SIDE   = RIM_SIDE + IRIM_WIDTH;
    // width of the stroke used to draw edge
    public static final int         IEDGE_WIDTH = round( RIM_WIDTH / 5 );   
    
    // background color for clock window
    public static final Color       BG_COLOR    = new Color( 0xF0F0F0 );
    // color of clock rim
    public static final Color       RIM_COLOR   = new Color( 0xFFFF66 );
    // the thin line draw around the clock rim
    public static final Color       EDGE_COLOR  = Color.BLACK;
    // color of other face elements: tics, strings
    public static final Color       FACE_COLOR  = new Color( 0xBB7d3e);
    
    /**************************************
     * WIDGET NAMES
     **************************************/
    
    /*** Controls Panel ***/
    /** Button to show SetDialog, for setting the countdown time. */
    public static final String  SET_BUTTON      = "SetButton";
    /** Button to pause the timer **/
    public static final String  PAUSE_BUTTON    = "PauseButton";
    /** Button to start the timer */
    public static final String  START_BUTTON    = "StartButton";
    /** Button to reset the timer */
    public static final String  RESET_BUTTON    = "ResetButton";
    /** Button to stop the timer */
    public static final String  STOP_BUTTON     = "StopButton";
    /** Button to exit the application */
    public static final String  EXIT_BUTTON     = "ExitButton";
    
    /** Field to display projected end time*/
    public static final String  ENDING_TEXT     = "EndingText";
    /** Field to display current time*/
    public static final String  CURR_TEXT       = "CurrText";
    /** Field to display remaining time*/
    public static final String  REMAINING_TEXT  = "RemainingText";

    /*** Set Dialog ***/
    /** OK button */
    public static final String  OK_BUTTON       = "OKButton";
    /** Cancel button */
    public static final String  CANCEL_BUTTON   = "CancelButton";
    /** Hours spinner */
    public static final String  HOURS_SPINNER   = "HoursSpinner";
    /** Hours increment button */
    public static final String  HOURS_INCR      = "HoursIncr";
    /** Hours decrement button */
    public static final String  HOURS_DECR      = "HoursDecr";
    /** Minute spinner */
    public static final String  MINS_SPINNER    = "MinsSpinner";
    /** Minute increment button */
    public static final String  MINS_INCR       = "MinsIncr";
    /** Minute decrement button */
    public static final String  MINS_DECR       = "MinsDecr";
    /** Seconds spinner */
    public static final String  SECS_SPINNER    = "SecsSpinner";
    /** Second increment button */
    public static final String  SECS_INCR       = "SecsIncr";
    /** Second decrement button */
    public static final String  SECS_DECR       = "SecsDecr";
    
    private static int round( double dNum )
    {
        int iNum    = (int)(dNum + .5);
        return iNum;
    }
}
