package com.gmail.johnstraub1954.clock;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

public class SetDialog extends JDialog
{
    /** Generated serial version UID */
    private static final long serialVersionUID = -1170051066987067697L;
    
    public static final int     CANCEL  = 0;
    public static final int     OKAY = 1;
    
    private final SpinnerNumberModel  hoursModel  =
        new SpinnerNumberModel( 0, 0, 11, 1 );
    private final SpinnerNumberModel  minsModel   = 
        new SpinnerNumberModel( 0, 0, 59, 1 );
    private final SpinnerNumberModel  secsModel   = 
        new SpinnerNumberModel( 0, 0, 59, 1 );
    private final JSpinner      hours       = new JSpinner( hoursModel );
    private final JSpinner      mins        = new JSpinner( minsModel );
    private final JSpinner      secs        = new JSpinner( secsModel );
    
    private long    milliseconds    = 0;
    private int     selection       = 0;
    
    public SetDialog( Window owner )
    {
        super( 
            owner, 
            "Set Timer",
            Dialog.ModalityType.APPLICATION_MODAL
        );
        
        setDefaultCloseOperation( JDialog.HIDE_ON_CLOSE );
        setLayout( new FlowLayout() );
        
        add( new MainPanel() );
    }
    
    public int display( boolean visible )
    {
        if ( visible )
        {
            long[]  timeComps   = parseTimeComponents();
            selection = CANCEL;
            hours.setValue( timeComps[0] );
            mins.setValue( timeComps[1] );
            secs.setValue( timeComps[2] );
        }
        setVisible( visible );
        return selection;
    }
    
    public long getValue()
    {
        return milliseconds;
    }
    
    public int getSelection()
    {
        return selection;
    }
    
    private long[] parseTimeComponents()
    {
        long    secs    = milliseconds / 1000;
        long    mins    = secs / 60;
        secs %= 60;
        long    hours   = mins / 60;
        mins %= 60;
        hours %= 12;
        
        long[] comps = { hours, mins, secs };
        return comps;
    }
    
    private class MainPanel extends JPanel
    {
        /** Generated serial version UID */
        private static final long serialVersionUID = 5044308748662852889L;
        private static final int    INCR   = 5;
        public MainPanel()
        {
            super( new BorderLayout() );
            
            hours.setName( Constants.HOURS_SPINNER );
            mins.setName( Constants.MINS_SPINNER );
            secs.setName( Constants.SECS_SPINNER );
            
            JPanel  spinnerPanel    = 
                new JPanel( new GridLayout( 3, 5, 5, 5 ) );
            Border  border          =
                BorderFactory.createEmptyBorder( 5, 5, 5, 5 );
            spinnerPanel.setBorder( border );
            
            int         rightAlign  = SwingConstants.RIGHT;
            JLabel      hoursLabel  = new JLabel( "Hours", rightAlign );
            JLabel      minsLabel   = new JLabel( "Minutes", rightAlign );
            JLabel      secsLabel   = new JLabel( "Seconds", rightAlign );
            
            Font            font    = getFont();
            float           size    = font.getSize2D();
            font = font.deriveFont( size * 1.5f );
            JComponent[]    spinnerComps    = 
            {
                hoursLabel,
                hours,
                new Incrementer( hoursModel, INCR, Constants.HOURS_INCR ),
                new Incrementer( hoursModel, -INCR, Constants.HOURS_DECR ),
                minsLabel,
                mins,
                new Incrementer( minsModel, INCR, Constants.MINS_INCR ),
                new Incrementer( minsModel, -INCR, Constants.MINS_DECR ),
                secsLabel,
                secs,
                new Incrementer( secsModel, INCR, Constants.SECS_INCR ),
                new Incrementer( secsModel, -INCR, Constants.SECS_DECR ),
            };
            for ( JComponent comp : spinnerComps )
            {
                spinnerPanel.add( comp );
                comp.setFont( font );
            }
            add( spinnerPanel, BorderLayout.CENTER );
            
            Border  buttonBorder    = 
                BorderFactory.createLineBorder( Color.BLACK );
            JPanel  buttonPanel     = new JPanel();
            JButton okButton        = new JButton( "OK" );
            JButton cancelButton    = new JButton( "Cancel" );
            buttonPanel.add( okButton );
            buttonPanel.add( cancelButton );
            buttonPanel.setBorder( buttonBorder );
            add( buttonPanel, BorderLayout.SOUTH );
            
            okButton.addActionListener( e -> okayCallback() );
            okButton.setName( Constants.OK_BUTTON );
            cancelButton.addActionListener( e -> display( false ) );
            cancelButton.setName( Constants.CANCEL_BUTTON );
        }
        
        private void okayCallback()
        {
            display( false );
            selection = OKAY;
            milliseconds = hoursModel.getNumber().intValue()  * 60 * 60;
            milliseconds += minsModel.getNumber().intValue() * 60;
            milliseconds += secsModel.getNumber().intValue();
            milliseconds *= 1000;
            System.out.println( milliseconds );
        }
        
        private class Incrementer extends JButton
        {
            /** Generated serial version UID */
            private static final long serialVersionUID = 8877989409238646122L;
            private final SpinnerNumberModel  model;
            
            public Incrementer( SpinnerNumberModel model, int incr, String name )
            {
                this.model = model;
                
                String  text    = incr < 0 ? "-" : "+";
                text += Math.abs( incr );
                setText( text );
                setName( name );
                addActionListener( e -> increment( incr ) );
            }
            
            private void increment( int increment )
            {
                System.out.println( model.getNumber().getClass().getName() );
                int     oldValue    = model.getNumber().intValue();
                int     max         = ((Number)model.getMaximum()).intValue();
                int     min         = ((Number)model.getMinimum()).intValue();
                int     newValue    = (oldValue + increment) % max;
                if ( newValue < min )
                    newValue = min;
                model.setValue( newValue );
            }
        }
    }
}

