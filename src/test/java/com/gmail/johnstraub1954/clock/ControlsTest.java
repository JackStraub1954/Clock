package com.gmail.johnstraub1954.clock;

import static com.gmail.johnstraub1954.clock.Constants.CANCEL_BUTTON;
import static com.gmail.johnstraub1954.clock.Constants.CURR_TEXT;
import static com.gmail.johnstraub1954.clock.Constants.ENDING_TEXT;
import static com.gmail.johnstraub1954.clock.Constants.EXIT_BUTTON;
import static com.gmail.johnstraub1954.clock.Constants.HOURS_DECR;
import static com.gmail.johnstraub1954.clock.Constants.HOURS_INCR;
import static com.gmail.johnstraub1954.clock.Constants.HOURS_SPINNER;
import static com.gmail.johnstraub1954.clock.Constants.MINS_DECR;
import static com.gmail.johnstraub1954.clock.Constants.MINS_INCR;
import static com.gmail.johnstraub1954.clock.Constants.MINS_SPINNER;
import static com.gmail.johnstraub1954.clock.Constants.OK_BUTTON;
import static com.gmail.johnstraub1954.clock.Constants.PAUSE_BUTTON;
import static com.gmail.johnstraub1954.clock.Constants.REMAINING_TEXT;
import static com.gmail.johnstraub1954.clock.Constants.RESET_BUTTON;
import static com.gmail.johnstraub1954.clock.Constants.SECS_DECR;
import static com.gmail.johnstraub1954.clock.Constants.SECS_INCR;
import static com.gmail.johnstraub1954.clock.Constants.SECS_SPINNER;
import static com.gmail.johnstraub1954.clock.Constants.SET_BUTTON;
import static com.gmail.johnstraub1954.clock.Constants.START_BUTTON;
import static com.gmail.johnstraub1954.clock.Constants.STOP_BUTTON;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Component;
import java.time.Duration;
import java.util.Arrays;
import java.util.function.BiPredicate;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JSpinner;
import javax.swing.JTextField;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ControlsTest
{
    // Time to wait after precipitating a GUI event
    private static final TimeKeeper keeper          = TimeKeeper.INSTANCE;
    private static final long       pauseForMillis  = 250;
    private static final long       defTimeout      = 128;
    
    // Name of the SetDialog thread
    private static final String     setDialogThreadName = "SetDialog thread";
    
    /**************************************
     * WIDGETS
     **************************************/
    /*** Controls Panel ***/
    /** Button to show SetDialog, for setting the countdown time. */
    private JButton     setButton;
    /** Button to pause the timer **/
    private JCheckBox   pauseButton;
    /** Button to start the timer */
    private JButton     startButton;
    /** Button to reset the timer */
    private JButton     resetButton;
    /** Button to stop the timer */
    private JButton     stopButton;
    /** Button to exit the application */
    private JButton     exitButton;
    
    /** Field to display projected end time*/
    private JTextField endingText;
    /** Field to display current time*/
    private JTextField currText;
    /** Field to display remaining time*/
    private JTextField remainingText;

    /*** Set Dialog ***/
    /** OK button */
    private JButton      okButton;
    /** Cancel button */
    private JButton     cancelButton;
    /** Hours spinner */
    private JSpinner    hoursSpinner;
    /** Hours increment button */
    private JButton     hoursIncr;
    /** Hours decrement button */
    private JButton     hoursDecr;
    /** Minute spinner */
    private JSpinner    minsSpinner;
    /** Minute increment button */
    private JButton     minsIncr;
    /** Minute decrement button */
    private JButton     minsDecr;
    /** Seconds spinner */
    private JSpinner    secsSpinner;
    /** Second increment button */
    private JButton     secsIncr;
    /** Second decrement button */
    private JButton     secsDecr;
    
    @BeforeEach
    public void beforeEach()
    {
        openClockGUI();

        /**************************************
         * WIDGETS
         **************************************/
        /*** Controls Panel ***/
        /** Button to show SetDialog, for setting the countdown time. */
         setButton =  (JButton)Utils.getComponent( SET_BUTTON );
        /** Button to pause the timer **/
         pauseButton =  (JCheckBox)Utils.getComponent( PAUSE_BUTTON );
        /** Button to start the timer */
         startButton =  (JButton)Utils.getComponent( START_BUTTON);
        /** Button to reset the timer */
         resetButton = (JButton)Utils.getComponent( RESET_BUTTON);
        /** Button to stop the timer */
         stopButton =  (JButton)Utils.getComponent( STOP_BUTTON );
        /** Button to exit the application */
         exitButton =  (JButton)Utils.getComponent( EXIT_BUTTON );
        
        /** Field to display projected end time*/
         endingText =  (JTextField)Utils.getComponent( ENDING_TEXT );
        /** Field to display current time*/
         currText =  (JTextField)Utils.getComponent( CURR_TEXT );
        /** Field to display remaining time*/
         remainingText =  (JTextField)Utils.getComponent( REMAINING_TEXT );

        /*** Set Dialog ***/
        /** OK button */
         okButton =  (JButton)Utils.getComponent( OK_BUTTON );
        /** Cancel button */
         cancelButton =  (JButton)Utils.getComponent( CANCEL_BUTTON );
        /** Hours spinner */
         hoursSpinner =  (JSpinner)Utils.getComponent( HOURS_SPINNER );
        /** Hours increment button */
         hoursIncr =  (JButton)Utils.getComponent( HOURS_INCR );
        /** Hours decrement button */
         hoursDecr =  (JButton)Utils.getComponent( HOURS_DECR);
        /** Minute spinner */
         minsSpinner =  (JSpinner)Utils.getComponent( MINS_SPINNER );
        /** Minute increment button */
         minsIncr =  (JButton)Utils.getComponent( MINS_INCR );
        /** Minute decrement button */
         minsDecr =  (JButton)Utils.getComponent( MINS_DECR);
        /** Seconds spinner */
         secsSpinner =  (JSpinner)Utils.getComponent( SECS_SPINNER );
        /** Second increment button */
         secsIncr =  (JButton)Utils.getComponent( SECS_INCR );
        /** Second decrement button */
         secsDecr =  (JButton)Utils.getComponent( SECS_DECR );
    }
    
    @AfterAll
    public static void afterAll()
    {
        pause();
        int[]   counters    = Utils.countFrames();
        String  windowCount = Arrays.toString( counters );
        System.out.println( windowCount );
        Utils.showDisplayableStatus();
    }

    /**
     * Open the set dialog; change the timeout to a non-zero number
     * and choose cancel. Verify that the state of the TimeKeeper
     * is not changed.
     */
    @Test
    void setCancelTest()
    {
        // Check for visible JDialog
        BiPredicate<Component, Object>  findDialogPred  = 
            (c, u) -> c != null && c instanceof JDialog; 
            
        initTimer( 0 );
        
        SetDialog   setDialog   = openAndValidateSetDialog();//(SetDialog)dialog;

        // Set a value; poke cancel button and verify 
        // operation canceled
        minsSpinner.setValue( 5 );
        cancelButton.doClick();
        pause();
        assertEquals( 0, keeper.getTimeRemaining() );
        assertFalse( keeper.isPaused() );
        assertFalse( keeper.isStarted() );
        
        // validate SetDialog is closed
        assertFalse( setDialog.isVisible() );
    }

    /**
     * Open the set dialog; change the timeout to a non-zero number
     * and choose OK. Verify that the TimeKeeper timeout is changed
     * to the given value, and remains in the expected state.
     */
    @Test
    void setOKTest()
    {
        // Check for visible JDialog
        BiPredicate<Component, Object>  findDialogPred  = 
            (c, u) -> c != null && c instanceof JDialog; 

        final long  timerVal    = 5 * 60 * 1000; // 5 minutes
        initTimer( timerVal );
            
        SetDialog   setDialog   = openAndValidateSetDialog();//(SetDialog)dialog;

        // Set a value; poke cancel button and verify 
        // operation canceled
        minsSpinner.setValue( 5 );
        okButton.doClick();
        
        // validate SetDialog is closed
        pause();
        assertFalse( setDialog.isVisible() );
        
        // verify timeout is set
        assertEquals( timerVal, keeper.getTimeRemaining() );
        
        // verify timer is not running
        assertEquals( timerVal, keeper.getTimeRemaining() );
        pause();
        assertEquals( timerVal, keeper.getTimeRemaining() );
        
        // Poke start button; verify timer is in correct state.
        startAndValidateTimer();
        
        exitButton.doClick();
    }
    
    // Set timer, start test; verify timer is counting-down.
    @Test
    public void startTestNormal()
    {
        final long  timerVal    = 5 * 60 * 60 * 1000;
        initTimer( timerVal );
        
        // Poke start button; verify timer is in correct state.
        startAndValidateTimer();
        
        exitButton.doClick();
    }
    
    // Set timer, start test; verify timer is counting-down.
    // Poke start button while timer is running; verify that
    // the state of the timer doesn't change.
    @Test
    public void startTestRedundant()
    {
        final long  timerVal    = 5 * 60 * 60 * 1000;
        
        initTimer( timerVal );
        
        // Poke start button; verify timer is in correct state.
        startAndValidateTimer();
        
        // Poke start button again; verify timer state does not change.
        startAndValidateTimer();
        
        exitButton.doClick();
    }
    
    // Set timer, start test; verify timer is counting-down.
    @Test
    public void stopTestNormal()
    {
        final long  timerVal    = 5 * 60 * 60 * 1000;
        initTimer( timerVal );
        
        // Poke start button; verify timer is in correct state.
        startAndValidateTimer();
        
        // stop timer and verify timer is stopped
        stopButton.doClick();
        pause();
        verifyTimerIsNotRunning();
        
        exitButton.doClick();
    }
    
    // Set timer, start test; verify timer is counting-down.
    @Test
    public void stopTestRedundant()
    {
        final long  timerVal    = 5 * 60 * 60 * 1000;
        initTimer( timerVal );
        
        // Poke start button; verify timer is in correct state.
        startAndValidateTimer();
        
        // stop timer and verify timer is stopped
        stopButton.doClick();
        pause();
        verifyTimerIsNotRunning();
        
        // redundantly stop timer; verify timer state hasn't changed.
        long    laterTime   = keeper.getTimeRemaining();
        stopButton.doClick();
        pause();
        verifyTimerIsNotRunning();
        assertEquals( laterTime, keeper.getTimeRemaining() );
        
        exitButton.doClick();
    }
    
    @Test
    public void pauseTest()
    {
        final long  timerVal    = 5 * 60 * 60 * 1000;
        initTimer( timerVal );
        
        // Poke start button; verify timer is in correct state.
        startAndValidateTimer();
        
        // set pause button and verify timer is paused
        pauseButton.doClick();
        pause();
        verifyTimerIsPaused();
        
        // Reset pause button and verify timer is running
        pauseButton.doClick();
        pause();
        verifyTimerIsRunning();
        
        exitButton.doClick();
    }
    
    /**
     * Set timer to N milliseconds
     * Start timer 
     * Reset timer; verify
     * <ul>
     * <li>Timer is not running</li>
     * <li>Countdown has been reset to N
     * </ul>
     */
    @Test
    public void resetWhileStartedTest()
    {
        final long  timerVal    = 5 * 60 * 60 * 1000;
        initTimer( timerVal );
        
        // Poke start button; verify timer is in correct state.
        startAndValidateTimer();
        
        // reset timer and validate
        resetButton.doClick();
        pause();
        verifyTimerIsNotRunning();
        assertEquals( timerVal, keeper.getTimeRemaining() );
        
        exitButton.doClick();
    }
    
    // Set timer to an initial, quiescent state; validate state.
    private void initTimer( long timerValMillis )
    {
        keeper.stop();
        keeper.pause( false );
        keeper.setTime( Duration.ofMillis( timerValMillis ) );
        
        assertFalse( keeper.isStarted() );
        assertFalse( keeper.isPaused() );
        assertEquals( timerValMillis, keeper.getTimeRemaining() );
        
        // verify timer isn't running
        pause();
        assertEquals( timerValMillis, keeper.getTimeRemaining() );
    }
    
    private void startAndValidateTimer()
    {
        startButton.doClick();
        pause();
        verifyTimerIsRunning();
    }
    
    private void verifyTimerIsRunning()
    {
        long    startTimeVal   = keeper.getTimeRemaining();
        assertTrue( startTimeVal > 0 );
        assertTrue( keeper.isStarted() );
        assertFalse( keeper.isPaused() );
        pause();
        long    laterTimeVal    = keeper.getTimeRemaining();
        assertTrue( laterTimeVal < startTimeVal );
        assertTrue( laterTimeVal > 0 );
    }
    
    // May be stopped, may be paused.
    private void verifyTimerIsNotRunning()
    {
        long    startTimeVal   = keeper.getTimeRemaining();
        assertTrue( startTimeVal > 0 );
        pause();
        long    laterTimeVal    = keeper.getTimeRemaining();
        assertEquals( laterTimeVal, startTimeVal );
    }
    
    private void checkTimerIsStopped()
    {
        long    startTimeVal   = keeper.getTimeRemaining();
        assertTrue( startTimeVal > 0 );
        assertTrue( keeper.isStarted() );
        assertFalse( keeper.isPaused() );
        pause();
        long    laterTimeVal    = keeper.getTimeRemaining();
        assertEquals( laterTimeVal, startTimeVal );
    }
    
    private void verifyTimerIsPaused()
    {
        long    startTimeVal   = keeper.getTimeRemaining();
        assertTrue( startTimeVal > 0 );
        assertTrue( keeper.isStarted() );
        assertTrue( keeper.isPaused() );
        pause();
        long    laterTimeVal    = keeper.getTimeRemaining();
        assertEquals( laterTimeVal, startTimeVal );
    }
    
    private SetDialog openAndValidateSetDialog()
    {
        BiPredicate<Component, Object>  findDialogPred  = 
            (c, u) -> c != null && c instanceof JDialog; 

        // SetDialog is modal; must be opened in separate thread
        openSetDialog();
        Utils.pause( 4 * pauseForMillis );
        
        // Verify SetDialog is in the expected state
        JDialog dialog  = 
            (JDialog)Utils.getComponent( findDialogPred, null );
        assertNotNull( dialog, "SetDialog not found" );
        assertTrue( dialog instanceof SetDialog, "JDialog not SetDialog" );
        assertTrue( dialog.isModal(), "SetDialog not modal" );
        assertTrue( dialog.isVisible(), "SetDialog not visible" );
        
        return (SetDialog)dialog;
    }
    
    /**
     * Open SetDialog in separate thread.
     * Because the SetDialog is modal, is can't run in
     * the current thread.
     */
    private void openSetDialog()
    {
        new Thread( () -> setButton.doClick(), setDialogThreadName ).start();
        pause();
    }
    
    private void openClockGUI()
    {
        keeper.stop();
        keeper.pause( false );
        keeper.setTime( Duration.ofMillis( defTimeout ) );
        new Clock( true );
        // give the GUI extra long time to settle down
//        Utils.pause( 2 * pauseForMillis );
    }
    
    private static void pause()
    {
        Utils.pause( pauseForMillis );
    }
}
