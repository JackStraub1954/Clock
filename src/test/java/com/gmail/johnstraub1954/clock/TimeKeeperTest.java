package com.gmail.johnstraub1954.clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class TimeKeeperTest
{
    private static final TimeKeeper KEEPER  = TimeKeeper.INSTANCE;
    
    private int             pcPauseCounter  = 0;
    private int             pcStartCounter  = 0;
    private boolean         oldPauseVal     = false;
    private boolean         oldStartVal     = false;
    private List<PCTester>  pcTesters       = new ArrayList<>();
    
    @AfterAll
    public static void afterAll()
    {
        int[]   counters    = Utils.countFrames();
        String  windowCount = Arrays.toString( counters );
        System.out.println( windowCount );
    }
    
    @Test
    void testAddRemovePropertyChangeListener()
    {
        int     numTests        = 3;
        
        for ( int inx = 0 ; inx < numTests ; ++inx )
        {
            int numTesters  = pcTesters.size();
            initKeeperState( true );
            
            KEEPER.start();
            KEEPER.pause( true );
            assertEquals( numTesters, pcStartCounter );
            assertEquals( numTesters, pcPauseCounter );
            
            initKeeperState( false );
            KEEPER.pause( false );
            assertEquals( numTesters, pcPauseCounter );

            initKeeperState( false );
            KEEPER.stop();
            assertEquals( numTesters, pcStartCounter );
            
            PCTester    tester  = new PCTester();
            pcTesters.add( tester );
            KEEPER.addPropertyChangeListener( tester );
        }
        
        initKeeperState( true );
        KEEPER.start();
        while ( pcTesters.size() != 0 )
        {
            pcPauseCounter = 0;
            boolean nextPauseState  = !KEEPER.isPaused();
            int     numTesters      = pcTesters.size();
            KEEPER.pause(nextPauseState);
            assertEquals( numTesters, pcPauseCounter );
            
            PCTester    tester  = pcTesters.remove( numTesters - 1 );
            initKeeperState( false );
            KEEPER.removePropertyChangeListener(tester);
        }
        pcStartCounter = 0;
        KEEPER.stop();
        assertEquals( 0, pcStartCounter );
    }

    @Test
    void testStart()
    {
        long    initMillis  = 5000;
        initKeeperState( true );
        
        KEEPER.setTime( Duration.ofMillis( initMillis ) );
        assertFalse( isClockRunning() );
        
        KEEPER.start();
        assertTrue( isClockRunning() );
    }

    @Test
    void testStartRestart()
    {
        // "Starting" the clock when it is already running
        // must not change the state of the clock.
        long    initMillis  = 5000;
        initKeeperState( true );
        
        // Verify clock isn't running
        KEEPER.setTime( Duration.ofMillis( initMillis ) );
        assertFalse( isClockRunning() );
        
        // Verify clock started/clock state
        KEEPER.start();
        assertTrue( isClockRunning() );
        assertTrue( KEEPER.isStarted() );
        assertFalse( KEEPER.isPaused() );
        
        // Make redundant call to start; verify clock state
        // doesn't change
        assertTrue( isClockRunning() );
        KEEPER.start();
        assertTrue( isClockRunning() );
        assertTrue( KEEPER.isStarted() );
        assertFalse( KEEPER.isPaused() );
    }
    
    private boolean isClockRunning()
    {
        long    currMillis  = KEEPER.getTimeRemaining();
        long    pauseFor    = Alarm.INSTANCE.getTimeout() * 2;
        Utils.pause( pauseFor );
        
        boolean running = currMillis > KEEPER.getTimeRemaining();
        return running;
    }

    @Test
    void testReset()
    {
        long    initMillis  = 5000;
        long    pauseFor    = Alarm.INSTANCE.getTimeout() * 2;
        initKeeperState( true );
        
        // Since timer is stopped, no countdown should be taking place.
        KEEPER.setTime( Duration.ofMillis( initMillis ) );
        Utils.pause( pauseFor );
        assertEquals( initMillis, KEEPER.getTimeRemaining() );
        
        // Start the timer and verify that countdown is occurring;
        KEEPER.start();
        Utils.pause( pauseFor );
        assertTrue( initMillis > KEEPER.getTimeRemaining() );
        
        // Reset the timer; verify that countdown has reset,
        // and that the timer is not running.
        KEEPER.reset();
        Utils.pause( pauseFor );
        assertEquals( initMillis, KEEPER.getTimeRemaining() );
        assertFalse( KEEPER.isStarted() );
    }

    @Test
    void testStop()
    {
        long    currMillis  = 0;
        long    initMillis  = 5000;
        long    pauseFor    = Alarm.INSTANCE.getTimeout() * 2;
        initKeeperState( true );
        
        // Since timer is stopped, no countdown should be taking place.
        KEEPER.setTime( Duration.ofMillis( initMillis ) );
        Utils.pause( pauseFor );
        assertEquals( initMillis, KEEPER.getTimeRemaining() );
        
        // Start the timer and verify that countdown is occurring;
        KEEPER.start();
        Utils.pause( pauseFor );
        assertTrue( initMillis > KEEPER.getTimeRemaining() );
        
        // Pause the timer; verify that countdown has stopped
        KEEPER.stop();
        currMillis = KEEPER.getTimeRemaining();
        Utils.pause( pauseFor );
        assertEquals( currMillis, KEEPER.getTimeRemaining() );
        
        // Verify properties; note that "stop" should reset "paused"
        assertFalse( KEEPER.isPaused() );
        assertFalse( KEEPER.isStarted() );
    }

    @Test
    void testPause()
    {
        long    currMillis  = 0;
        long    initMillis  = 5000;
        long    pauseFor    = Alarm.INSTANCE.getTimeout() * 2;
        initKeeperState( true );
        
        // Since timer is stopped, no countdown should be taking place.
        KEEPER.setTime( Duration.ofMillis( initMillis ) );
        Utils.pause( pauseFor );
        assertEquals( initMillis, KEEPER.getTimeRemaining() );
        
        // Start the timer and verify that countdown is occurring;
        KEEPER.start();
        Utils.pause( pauseFor );
        assertTrue( initMillis > KEEPER.getTimeRemaining() );
        
        // Pause the timer; verify that countdown has stopped
        KEEPER.pause( true );
        currMillis = KEEPER.getTimeRemaining();
        Utils.pause( pauseFor );
        assertEquals( currMillis, KEEPER.getTimeRemaining() );
        
        // Unpause the timer; verify that countdown has resumed.
        KEEPER.pause( false );
        Utils.pause( pauseFor );
        assertTrue( currMillis >  KEEPER.getTimeRemaining() );
    }

    @Test
    void testGetSetTimeRemaining()
    {
        long    initMillis  = 5000;
        long    pauseFor    = Alarm.INSTANCE.getTimeout() * 2;
        initKeeperState( true );
        
        KEEPER.setTime( Duration.ofMillis( initMillis ) );
        assertEquals( initMillis, KEEPER.getTimeRemaining() );
        KEEPER.start();
        Utils.pause( pauseFor );
        assertTrue( initMillis > KEEPER.getTimeRemaining() );
    }

    @Test
    void testGetSetTimeRemainingAfterExpiry()
    {
        // Wait for a bit after timer has expired; verify
        // that remaining-time == 0.
        long    initMillis  = 500;
        initKeeperState( true );
        
        KEEPER.setTime( Duration.ofMillis( initMillis ) );
        assertEquals( initMillis, KEEPER.getTimeRemaining() );
        KEEPER.start();
        Utils.pause( (long)(1.5 * initMillis) );
        assertEquals( 0, KEEPER.getTimeRemaining() );
    }
    
    // If begin is true, initialize state to "base" level:
    // timer not started, timer not paused.
    //
    // If begin is true, do not change pause or start properties.
    private void initKeeperState( boolean begin )
    {
        if ( begin )
        {
            KEEPER.pause( false );
            KEEPER.stop();
            
            assertFalse( KEEPER.isStarted() );
            assertFalse( KEEPER.isPaused() );
        }
        
        pcPauseCounter = 0;
        pcStartCounter = 0;
        oldPauseVal = KEEPER.isPaused();
        oldStartVal = KEEPER.isStarted();
    }

    private class PCTester implements PropertyChangeListener
    {
        @Override
        public void propertyChange( PropertyChangeEvent evt )
        {
            String  prop    = evt.getPropertyName();
            boolean oldVal  = (Boolean)evt.getOldValue();
            boolean newVal  = (Boolean)evt.getNewValue();

            assertNotEquals( oldVal, newVal );
            
            System.out.println( "***************" );
            if ( prop.equals( TimeKeeper.PAUSED_PROPERTY ) )
            {
                ++pcPauseCounter;
                System.out.println( "Pause counter: " + pcPauseCounter );
                assertEquals( oldPauseVal, oldVal );
                assertEquals( !oldPauseVal, newVal );
            }
            else if ( prop.equals( TimeKeeper.STARTED_PROPERTY ) )
            {
                ++pcStartCounter;
                System.out.println( "Start counter: " + pcStartCounter );
                assertEquals( oldStartVal, oldVal );
                assertEquals( !oldStartVal, newVal );
            }
            else
            {
                fail( "Unknown property changed: " + prop );
            }
        }
    }
}
