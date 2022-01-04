package com.gmail.johnstraub1954.clock;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.border.Border;

public class Controls extends JPanel 
    implements PropertyChangeListener
{
    /** Generated serial version UID */
    private static final long serialVersionUID = -5276644673111175089L;
    private static final TimeKeeper     KEEPER  = TimeKeeper.INSTANCE;
    private static final Disposer       disposer    = new Disposer();
    
    private final JButton   set         = new JButton( "Set" );
    private final JCheckBox pause       = new JCheckBox( "Pause" );
    private final JButton   start       = new JButton( "Start" );
    private final JButton   stop        = new JButton( "Stop" );
    private final JButton   reset       = new JButton( "Reset" );
    private final JButton   exit        = new JButton( "Exit" );
    
    // SetDialog can't be instantiated until we know the reference
    // for this Controls object (the owner of the dialog)
    private final SetDialog setDialog;
    private final JFrame    owner;

    public Controls( JFrame owner )
    {
        super( new GridBagLayout() );
        this.owner = owner;
        this.owner.addWindowListener( disposer );
        setNames();
        
        setDialog   = new SetDialog( owner );
        Border  border = BorderFactory.createEmptyBorder( 0, 5, 0, 5 );
        setBorder( border );
        
        JComponent[]    comps   =
        {
            set,
            start,
            stop,
            reset,
            pause,
            exit
        };
        
        GridBagConstraints  gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        for ( JComponent comp : comps )
        {
            add( comp, gbc );
            gbc.gridy++;
        }
        
        set.addActionListener( e -> showSetDialog() );
        start.addActionListener( e -> KEEPER.start() );
        stop.addActionListener( e -> KEEPER.stop() );
        reset.addActionListener( e -> KEEPER.reset() );
        exit.addActionListener( e -> owner.dispose() );
        pause.addActionListener( this::pause );
        
        TimeKeeper.INSTANCE.addPropertyChangeListener( this );
        setDialog.pack();
    }

    @Override
    public void propertyChange( PropertyChangeEvent evt )
    {
        String  propName    = evt.getPropertyName();
        if ( propName.equals( TimeKeeper.STARTED_PROPERTY ) )
        {
            boolean newValue    = (Boolean)evt.getNewValue();
            pause.setEnabled( newValue );
            pause.setSelected( false );
        }
        else if ( propName.equals( TimeKeeper.PAUSED_PROPERTY ) )
        {
            boolean newValue    = (Boolean)evt.getNewValue();
//            pause.setEnabled( newValue );
            pause.setSelected( newValue );
        }
    }
    
    private void setNames()
    {
        set.setName( Constants.SET_BUTTON );
        start.setName( Constants.START_BUTTON );
        stop.setName( Constants.STOP_BUTTON );
        reset.setName( Constants.RESET_BUTTON );
        pause.setName( Constants.PAUSE_BUTTON );
        exit.setName( Constants.EXIT_BUTTON );
    }
    
    private void pause( ActionEvent evt )
    {
        Object  src = evt.getSource();
        if ( src instanceof JToggleButton )
        {
            JToggleButton   button  = (JToggleButton)src;
            KEEPER.pause( button.isSelected() );
        }
    }
    
    private void showSetDialog()
    {
        int selection   = setDialog.display( true );
        if ( selection == SetDialog.OKAY )
            KEEPER.setTime( Duration.ofMillis( setDialog.getValue() ) );
    }
    
    // When clock window is disposed, make sure all other
    // top-level windows are disposed.
    private static class Disposer extends WindowAdapter
    {
        @Override
        public void windowDeactivated( WindowEvent evt )
        {
            System.out.println( "deactivation..." );
            disposeAll();                
        }
        
        private void disposeAll()
        {
            for ( Window window : Window.getWindows() )
            {
                System.out.print( window.getClass().getName());
                System.out.println( " " + window.isDisplayable() );
//                window.dispose();
            }
        }
    }
}
