package com.gmail.johnstraub1954.clock;

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class DigitalDisplay extends JPanel implements ActionListener
{
    /** Generated serial version UID */
    private static final long serialVersionUID = 5470403987073171333L;
    
    private static final TimeKeeper         KEEPER      = TimeKeeper.INSTANCE;
    private static final Alarm              ALARM       = Alarm.INSTANCE;
    private static final float              FONT_XIER   = 1.4f;
    
    private final JTextField    strEnding       = new JTextField();
    private final JTextField    strCurrTime     = new JTextField();
    private final JTextField    strRemaining    = new JTextField();

    public DigitalDisplay()
    {
        strEnding.setName( Constants.ENDING_TEXT );
        strCurrTime.setName( Constants.CURR_TEXT );
        strRemaining.setName( Constants.REMAINING_TEXT );
        
        setLayout( new GridLayout( 2, 1 ) );
        add( getRemainingRow() );
        add( getTimeRow() );
        ALARM.addActionListener( this );
    }
    
    private JPanel getTimeRow()
    {
        JPanel      row1            = new JPanel();
        JLabel      ending          = new JLabel( "Expiry:" );
        JLabel      currTime        = new JLabel( "Time:" );
        
        JComponent[]    components  =
        { 
            ending, 
            strEnding, 
            currTime,
            strCurrTime
        };
        for ( JComponent comp : components )
        {
            tweakFont( comp );
            addBorder( comp, 10 );
            setReadOnly( comp );
            row1.add( comp);
        }
        
        return row1;
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        final DateTimeFormatter  formatter      = 
            DateTimeFormatter.ofLocalizedTime( FormatStyle.MEDIUM );
        final DateTimeFormatter  remFormatter   = 
            DateTimeFormatter.ofPattern( "KK:mm:ss" );
        final TemporalUnit  baseUnit            = 
            ChronoField.MILLI_OF_DAY.getBaseUnit();
        
        long        remaining   = KEEPER.getTimeRemaining();
        LocalTime   currTime    = LocalTime.now();
        LocalTime   endTime     = currTime.plus( remaining, baseUnit );
        
        LocalTime   remTime     = LocalTime.MIDNIGHT;
        remTime = remTime.plus( remaining, baseUnit );
        strCurrTime.setText( currTime.format( formatter ) );
        strEnding.setText( endTime.format(formatter) );
        strRemaining.setText( remTime.format( remFormatter ) );
    }
    
    private JPanel getRemainingRow()
    {
        JPanel      row2            = new JPanel();
        JLabel      remaining       = new JLabel( "Remaining:" );
                
        strRemaining.setText( "00:09:59" );
        JComponent[]    components  =
        { 
            remaining, 
            strRemaining, 
        };
        for ( JComponent comp : components )
        {
            tweakFont( comp );
            addBorder( comp, 10 );
            setReadOnly( comp );
            row2.add( comp );
        }
        return row2;
    }
    
    private static void tweakFont( JComponent comp )
    {
        Font    font    = comp.getFont();
        font = font.deriveFont( FONT_XIER * font.getSize2D() );
        comp.setFont( font );
    }
    
    private static void addBorder( JComponent comp, int space )
    {
        Border  border  = 
            BorderFactory.createEmptyBorder( 0, space, 0, space );
        comp.setBorder( border );
    }
    
    private static void setReadOnly( JComponent comp )
    {
        if ( comp instanceof JTextField )
            ((JTextField)comp).setEditable(false);
    }
}
