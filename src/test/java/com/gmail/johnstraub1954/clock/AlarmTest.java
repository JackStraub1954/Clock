package com.gmail.johnstraub1954.clock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

class AlarmTest
{
    private final Alarm     alarm               = Alarm.INSTANCE;
    private List<Boolean>   actionListenerFired = new ArrayList<>();
    private List<Long>      actionListenerTime  = new ArrayList<>();
    
    @AfterAll
    public static void afterAll()
    {
        int[]   counters    = Utils.countFrames();
        String  windowCount = Arrays.toString( counters );
        System.out.println( windowCount );
    }
    
    @Test
    void testActionListeners()
    {
        List<ActionTester>  testers = new ArrayList<>();
        int numTestListeners    = 4;
        
        for ( int inx = 0 ; inx < numTestListeners ; ++inx )
        {
            ActionTester    tester  = new ActionTester();
            actionListenerFired.clear();
            alarm.fireActionListeners();
            assertEquals( inx, actionListenerFired.size() );
            assertEquals( inx, testers.size() );
            alarm.addActionListener( tester );
            testers.add( tester );
        }
        
        // + 1: an extra action listener got added at the end
        // of add loop, above.
        for ( int inx = numTestListeners ; inx >= 0 ; --inx )
        {
            ActionTester    tester  = null;
            if ( inx > 0 ) 
                tester = testers.get( testers.size() - 1 );
            actionListenerFired.clear();
            alarm.fireActionListeners();
            assertEquals( inx, actionListenerFired.size() );
            alarm.removeActionListener( tester );
            testers.remove( tester );
        }
        
        // validate test state expectations
        assertTrue( testers.isEmpty() );
    }

    @Test
    void testSetGetTimeout()
    {
        Alarm   alarm   = Alarm.INSTANCE;
        long[]  timeOuts    = { 125, 250 };
        for ( long timeOut : timeOuts )
        {
            alarm.setTimeout( timeOut );
            assertEquals( timeOut, alarm.getTimeout() );
        }
    }
    
    @Test
    void testKickTimer()
    {
        // all tests should complete in 
        // time <= currTime <= (currTime + timeout)

        // don't make this too big; 3 is a good number
        int     numTests    = 3;
        long    currTime    = System.currentTimeMillis();
        long    timeout     = alarm.getTimeout();
        long    maxTime     = currTime + timeout;
        alarm.addActionListener( new ActionTester() );
        
        // within the given timespan, action listeners should 
        // first at least numTests times; since the alarm could
        // time out in the given span, action listeners might
        // have fired no more than one extra time. All listeners
        // should be fired within the given timespan.
        actionListenerTime.clear();
        actionListenerFired.clear();
        System.out.println( "***** kick test" );
        for ( int inx = 0 ; inx < numTests ; ++inx )
        {
            alarm.kickTimerThread();
            Utils.pause( 1 );
        }
        
        System.out.println( actionListenerFired );
        
        String  msg = null;
        int size    = actionListenerTime.size();
        msg = "Expected: " + numTests + " Actual: " + size;
        System.out.println( "***** end kick test" );
        assertTrue( (size >= numTests), msg );
        assertTrue( size <= numTests + 1, msg );
        for ( long time : actionListenerTime )
        {
            msg = "currTime: " + currTime + "  time: " + time
                + " diff: " + (time - currTime) + " max: " + (maxTime - currTime);
            System.out.println( msg );
            assertTrue( time >= currTime, msg );
            assertTrue( time < maxTime, msg );
        }
    }
    
    private class ActionTester implements ActionListener
    {
        public void actionPerformed( ActionEvent evt )
        {
            System.out.println( "actionPerformed" );
            actionListenerFired.add( true );
            actionListenerTime.add( System.currentTimeMillis() );
        }
        
    }
}
