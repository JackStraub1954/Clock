package com.gmail.johnstraub1954.clock;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class BreakTimerPanel extends JPanel implements ActionListener
{
    private final Canvas            canvas          = new Canvas();
    private final DigitalDisplay    digitalDisplay  = new DigitalDisplay();
    private final Controls          controls;
    private final Alarm             alarm           = Alarm.INSTANCE;

    public BreakTimerPanel( JFrame frame )
    {
        controls = new Controls( frame );
        setLayout( new BorderLayout() );
        add( canvas, BorderLayout.CENTER );
        
        add( controls, BorderLayout.WEST );
        add( digitalDisplay, BorderLayout.NORTH );
        alarm.addActionListener( this );
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        canvas.repaint();
    }
}
