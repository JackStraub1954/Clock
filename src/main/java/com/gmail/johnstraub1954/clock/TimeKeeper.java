package com.gmail.johnstraub1954.clock;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.time.Duration;

public enum TimeKeeper
{
    INSTANCE;
    
    public static final String  STARTED_PROPERTY    = "STARTED";
    public static final String  PAUSED_PROPERTY     = "PAUSED";
    
    private static final    String  clipFileBoing    = "boing-6222.wav";
    
    private final Alarm     alarm              = Alarm.INSTANCE;
    
    private final PropertyChangeSupport pcs = 
        new PropertyChangeSupport( this );
    
    private long    atStart         = 0;
    private long    fromTime        = -1;
    private long    endTime         = -1;
    private long    remaining       = 0;
    private boolean paused          = false;
    private boolean started         = false;
    
    private TimeKeeper()
    {
        alarm.addActionListener( e -> stopOnTimerExpiry() );
    }
    
    public void 
    addPropertyChangeListener( PropertyChangeListener listener )
    {
        pcs.addPropertyChangeListener( listener );
    }
    
    public void 
    removePropertyChangeListener( PropertyChangeListener listener )
    {
        pcs.removePropertyChangeListener( listener );
    }
    
    public void start()
    {
        if ( !started )
        {
            fromTime = System.currentTimeMillis();
            endTime = fromTime + atStart;
            remaining = endTime - fromTime;
            started = true;
            paused = false;
            Alarm.INSTANCE.fireActionListeners();
            pcs.firePropertyChange( STARTED_PROPERTY, false, true );
        }
    }
    
    public void reset()
    {
        stop();
        setTime( Duration.ofMillis( atStart ) );
        Alarm.INSTANCE.fireActionListeners();
    }
    
    public void stop()
    {
        // return if not started
        if ( started )
        {            
            // must reset paused before resetting started
            remaining = getTimeRemaining();
            pause( false );
            started = false;
            pcs.firePropertyChange( STARTED_PROPERTY, true, false );
            playClip();
        }
    }
    
    public void pause( boolean pause )
    {
        if ( !started )
            return;
        
        if ( pause == this.paused )
            return;
        
        boolean oldValue    = this.paused;
        this.paused = pause;
        if ( pause )
        {
            remaining = endTime - System.currentTimeMillis();
        }
        else
        {
            fromTime = System.currentTimeMillis();
            endTime = fromTime + remaining;
        }
        pcs.firePropertyChange( PAUSED_PROPERTY, oldValue, paused );
    }
    
    public long getTimeRemaining()
    {
        long    time;
        long    currTime    = System.currentTimeMillis();
        if ( !started )
            time = remaining;
        else if ( paused )
            time = remaining;
        else if ( endTime > currTime )
            time = endTime - currTime;
        else
            time = 0;
        
        return time;
    }
    
    public void setTime( Duration duration )
    {
        atStart = duration.toMillis();
        remaining = atStart;
        Alarm.INSTANCE.fireActionListeners();
    }
    
    public boolean isStarted()
    {
        return started;
    }
    
    public boolean isPaused()
    {
        return paused;
    }
    
    /**
     * Wake up occasionally and test state;
     * if started is true and timeRemaining == 0
     * invoke the stop method.
     */
    private void stopOnTimerExpiry()
    {
        if ( started && getTimeRemaining() == 0 )
        {
            stop();
            ClipPlayer.ofResource( clipFileBoing ).play();
        }
    }
    
    private void playClip()
    {
        ClipPlayer  player  = ClipPlayer.ofResource( clipFileBoing ); 
        if ( player != null )
        {
            long    pause   = player.getMicrosecondLength() / 1000 + 5;
            for ( int inx = 0 ; inx < 4 ; ++inx )
            {
                player.play();
                try
                {
                    Thread.sleep( pause );
                }
                catch ( InterruptedException exc )
                {
                    
                }
            }
        }
    }

}
