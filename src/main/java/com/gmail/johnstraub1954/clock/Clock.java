package com.gmail.johnstraub1954.clock;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Clock implements ActionListener
{
    private final JFrame            frame           = 
        new JFrame( "Break Timer" );
    private final Canvas            canvas          = new Canvas();
    private final DigitalDisplay    digitalDisplay  = new DigitalDisplay();
    private final Controls          controls        = new Controls( frame );
    private final Alarm             alarm           = Alarm.INSTANCE;
    
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
        alarm.addActionListener( this );
    }
    
    public void actionPerformed( ActionEvent event )
    {
        canvas.repaint();
    }
    
    private void buildGUI()
    {
        frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        Container   pane        = frame.getContentPane();
        pane.setLayout( new BorderLayout() );
        pane.add( canvas, BorderLayout.CENTER );
        
        pane.add( controls, BorderLayout.WEST );
        pane.add( digitalDisplay, BorderLayout.NORTH );
        frame.pack();
        frame.setVisible( true );
    }
}
