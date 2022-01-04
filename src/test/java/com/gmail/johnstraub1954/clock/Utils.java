package com.gmail.johnstraub1954.clock;

import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.function.BiPredicate;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class Utils
{
    public static final BiPredicate<Component,Object> nameFinder    = 
        (c,s) -> s.equals( c.getName() );
        
    public static void pause( long millis )
    {
        try
        {
            Thread.sleep(millis);
        }
        catch ( InterruptedException exc )
        {
            // no action necessary
        }
    }
    
    // Print the disposed status of all top level windows
    public static void showDisplayableStatus()
    {
        for ( Window window : Window.getWindows() )
        {
            StringBuilder   bldr    = new StringBuilder();
            bldr.append( window.getClass().getName() )
                .append( " Displayable: " );
            if ( window.isDisplayable() )
                bldr.append( "YES" );
            else
                bldr.append( "NO" );
            System.out.println( bldr );
        }
    }
    
    // array[0] = JFrame count
    // array[1] = JDialog count
    // array[2] = disposed Frames
    // array[3] = all (non-disposed) windows
    // Note: disposed frames are not counted
    public static int[] countFrames()
    {
        int[]       counters    = { 0, 0, 0 };
        Window[]    windows     = Window.getWindows();
        
        for ( Window window : windows )
        {
            if ( !window.isDisplayable() )
                ++counters[2];
            else if ( window instanceof JDialog )
                ++counters[1];
            else if ( window instanceof JFrame )
                ++counters[0];
        }
        
        return counters;
    }
    
    public static Component getComponent( String name )
    {
        Component   comp    = getComponent( nameFinder, name );
        return comp;
    }
    
    public static Component 
    getComponent( BiPredicate<Component,Object> pred, Object obj )
    {
        Component   comp    = null;
        Window[]    frames  = Window.getWindows();
        for ( int inx = 0 ; inx < frames.length && comp == null ; ++inx )
        {
            Window  frame   = frames[inx];
            if ( !frame.isDisplayable() )
                continue;
            if ( pred.test( frame, obj ) )
                comp = frame;
            else if ( frame instanceof JFrame )
            {
                Container   cont    = ((JFrame)frame).getContentPane();
                comp = getComponent( cont, pred, obj );
            }
            else if ( frame instanceof JDialog )
            {
                Container   cont    = ((JDialog)frame).getContentPane();
                comp = getComponent( cont, pred, obj );
            }
            else
                comp = getComponent( frame, pred, obj );
        }
        return comp;
    }
    
    private static Component getComponent(
        Container container, 
        BiPredicate<Component,Object> pred, 
        Object obj
    )
    {
        Component       comp        = null;
        Component[]     children    = container.getComponents();
        int             numChildren = children.length;
        for ( int inx = 0 ; inx < numChildren && comp == null ; ++inx )
        {
            Component   test    = children[inx];
            if ( pred.test( test, obj ) )
                comp = test;
            else if ( test instanceof Container )
                comp = getComponent( (Container)test, pred, obj );
            else
                ;
        }
        return comp;
    }
}
