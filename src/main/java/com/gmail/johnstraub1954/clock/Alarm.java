package com.gmail.johnstraub1954.clock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

// Everybody who needs to be updated occasionally should do so 
// via this thread. That keeps things like concurrent updates
// to the GUI from colliding.
public enum Alarm
{
    INSTANCE;
    
    private static final long   defSleepTimer       = 10; // milliseconds
    private static final Object syncher             = new Object();
    
    private final Thread    timerThread = 
        new Thread( new Timer(), "Alarm thread" );
    
    private final List<ActionListener>  actionListeners = new ArrayList<>();
    
    private long    timeout = defSleepTimer;
    
    private Alarm()
    {
        timerThread.start();
    }
    
    public synchronized void addActionListener( ActionListener listener )
    {
        actionListeners.add( listener );
    }
    
    public synchronized void removeActionListener( ActionListener listener )
    {
        actionListeners.remove( listener );
    }
    
    public synchronized void kickTimerThread()
    {
        timerThread.interrupt();
    }
    
    public synchronized void fireActionListeners()
    {
        final int       eventID     = ActionEvent.ACTION_PERFORMED;
        final String    command     = null;        
        ActionEvent event   = new ActionEvent( this, eventID, command );
        for ( ActionListener listener : actionListeners )
            listener.actionPerformed( event );
    }
    
    public void setTimeout( long millis )
    {
        timeout = millis;
    }
    
    public long getTimeout()
    {
        return timeout;
    }
    
    private class Timer implements Runnable
    {
        public void run()
        {
            while ( true )
            {
                try
                {
                    synchronized ( syncher )
                    {
                        // I don't know how to get test coverage 
                        // on this line of code.
                        syncher.wait( timeout );
                    }
                } 
                catch ( InterruptedException exc )
                {
                    String  name    = Thread.currentThread().getName();
                    System.out.println( name + " interrupted" );
                    // nothing to do
                }
                fireActionListeners();
            }
        }
    }
}
