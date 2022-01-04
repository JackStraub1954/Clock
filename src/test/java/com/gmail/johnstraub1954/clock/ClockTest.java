package com.gmail.johnstraub1954.clock;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Container;

import javax.swing.JFrame;

import org.junit.jupiter.api.Test;

class ClockTest
{
    /**
     * This is primarily to improve test coverage.
     */
    @Test
    void testMain()
    {
        Clock.main( null );
        Utils.pause( 250 );
        verifyClockDisplayed();
    }

    /**
     * Clock GUI should be constructed with 
     * SwingUtilities.invokeAndWait, therefore there is not need
     * to pause after Clock construction.
     */
    @Test
    void testClockWait()
    {
        new Clock( true );
        Utils.pause( 250 );
        verifyClockDisplayed();
    }

    /**
     * Clock GUI should be constructed with 
     * SwingUtilities.invokeLater, therefore there must be a
     * a pause after Clock construction to give GUI a chance
     * to stabilize.
     */
    @Test
    void testClockNoWait()
    {
        new Clock( false );
        Utils.pause( 250 );
        verifyClockDisplayed();
    }
    
    /**
     * Make a minimal effort to verify that the Clock GUI
     * is created and visible.
     */
    private void verifyClockDisplayed()
    {
        JFrame      mainFrame   = (JFrame)
            Utils.getComponent( (c,o) -> (c instanceof JFrame), null );
        SetDialog   setDialog   = (SetDialog)
            Utils.getComponent( (c,o) -> (c instanceof SetDialog), null );
        
        assertNotNull( mainFrame );
        assertNotNull( setDialog );
        assertTrue( mainFrame.isDisplayable() );
        assertTrue( setDialog.isDisplayable() );
        assertTrue( mainFrame.isVisible() );
        assertFalse( setDialog.isVisible() );
        
        Container   mainPane    = mainFrame.getContentPane();
        Container   setPane     = setDialog.getContentPane();
        assertNotNull( mainPane );
        assertNotNull( setPane );
        assertTrue( mainPane.isDisplayable() );
        assertTrue( setPane.isDisplayable() );
        assertTrue( mainPane.isVisible() );
        assertTrue( setPane.isVisible() );
    }
}
