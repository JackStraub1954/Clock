package com.gmail.johnstraub1954.clock;

import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;

public class Clock
{
    /** Frame that encloses a JTabbedPane */
    private final JFrame            frame       = new JFrame( "Clock" );
    /** 
     * Content pane for this appllication's frame. 
     * All the magic happens in the tabs.
     * 
     */
    private final JTabbedPane       tabbedPane  = new JTabbedPane();
    
    /**
     * Main method to start the clock.
     * 
     * @param args  command-line arguments; not used
     */
    public static void main( String[] args )
    {
        new Clock( false );
    }
    
    /**
     * Constructor. Builds and displays the GUI.
     * @param wait  true    to wait for build to complete;
     *                      false to begin construction 
     *                      and return immediately.
     */
    public Clock( boolean wait )
    {
        Runnable    constructGUI    = () ->buildGUI();
        if ( !wait )
            SwingUtilities.invokeLater( constructGUI );
        else
        {
            try
            {
                SwingUtilities.invokeAndWait( constructGUI );
            }
            // Don't know how to get test coverage on the catch blocks.
            catch ( InterruptedException exc )
            {
                System.out.println( exc.getMessage() );
            }
            catch ( InvocationTargetException exc )
            {
                String  msg = "MALFUNCTION: " + exc.getMessage();
                System.err.println( msg );
                System.exit( 1 );
            }
        }
    }
    
    /**
     * Add the appropriate JPanels to the JTabbedPane.
     * Make the JTabbedPane the content pane of the frame.
     * Size and make visible.
     */
    private void buildGUI()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        frame.setContentPane( tabbedPane );
        tabbedPane.add( "Break Timer", new BreakTimerPanel( frame ) );
        tabbedPane.add( "Clock", new BasicClockPanel( frame ) );
        frame.pack();
        frame.setVisible( true );
    }
}
