package com.gmail.johnstraub1954.clock;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Uses the javax.sound.sampled package to play short audio clips
 * over the default audio line.
 * 
 * @author johns
 *
 */
/**
 * @author johns
 *
 */
public class ClipPlayer
{
    private static final    String  className   =  ClipPlayer.class.getName();
    private static final    Logger  logger      = Logger.getLogger( className );
    private final           Clip    clip;
    private final           Object  source;

    /**
     * Used for ad hoc testing during development.
     * 
     * @param args  command line arguments; not used.
     */
    public static void main(String[] args)
    {
        String clipFileBoing    = 
            "boing-6222.wav";
        ClipPlayer  player      = ClipPlayer.ofResource( clipFileBoing );
        System.out.println( player.getMicrosecondLength() );
        for ( int inx = 0 ; inx < 3 ; ++inx )
        {
            try
            {
                System.out.println( "playing" );
                player.play();
                Thread.sleep( 2100 );
            }
            catch ( InterruptedException exc )
            {
            }
        }
    }
    
    /**
     * Creates a ClipPlayer using an audio source from a resource folder.
     * Returns null on failure.
     * 
     * @param resourceName  the name of the audio source resource
     * 
     * @return  ClipPlayer created from audio source; null if failed.
     */
    public static ClipPlayer ofResource( String resourceName )
    {
        ClipPlayer  player      = null;
        ClassLoader loader      = ClipPlayer.class.getClassLoader();
        InputStream inStream    = loader.getResourceAsStream( resourceName );
        if ( inStream == null )
        {
            String  message = 
                "Failed to load audio clip file: " + resourceName;
            logger.severe( message );
        }
        else
            player = new ClipPlayer( inStream, resourceName );
        return player;
    }
    
    /**
     * Constructs a ClipPlayer using a given audio source.
     * This operation might fail.
     * To determine if a failure has occurred 
     * use the <em>isValid</em> method.
     * 
     * @param audioPath the given audio source
     * @param source    object, usually a string, identifying the source 
     *                  of the audio clip; used only for status reporting
     */
    public ClipPlayer( String audioPath, Object source )
    {
        this.source = source;
        clip = init( audioPath );
    }
    
    /**
     * Constructs a ClipPlayer from a given input stream.
     * 
     * @param inStream  the given input stream
     */
    public ClipPlayer( InputStream inStream )
    {
        this( inStream, "(unspecified source)" );
    }
    
    /**
     * Constructs a ClipPlayer from a given input stream.
     * 
     * @param inStream  the given input stream
     * @param source    object, usually a string, identifying the source 
     *                  of the audio clip; used only for status reporting
     */
    public ClipPlayer( InputStream inStream, Object source )
    {
        this.source = source;
        clip = init( inStream );
    }

    /**
     * Plays the encapsulated audio clip, if possible.
     * If the clip is already running, it is stopped
     * and restarted from the beginning.
     */
    public void play()
    {
        if ( clip != null )
        {
            if ( clip.isRunning() )
                clip.stop();
            clip.setMicrosecondPosition( 0 );
            clip.start();
        }
    }
    
    /**
     * Indicates whether instantiation of this ClipPlayer fully succeeded.
     * 
     * @return  true if instantiation of this ClipPlayer fully succeeded
     */
    public boolean isValid()
    {
        boolean valid   = clip != null;
        return valid;
    }
    
    /**
     * Returns the length of the encapsulated audio clip in microseconds.
     * 
     * @return the length of the encapsulated audio clip in microseconds
     */
    public long getMicrosecondLength()
    {
        long    length  = 0;
        if (clip != null )
            length = clip.getMicrosecondLength();
        return length;
    }
    
    /**
     * Instantiates a Clip object using a given audio file.
     * Errors are logged.
     * Null is returned if the operation fails.
     * 
     * @param audioPath the given audio file
     * 
     * @return the instantiated Clip object, or null if failed
     */
    private Clip init( String audioPath )
    {
        Clip    workingClip = null;
        File    audioFile   = new File( audioPath );
        try
        {
            InputStream inStream    = new FileInputStream( audioFile );
            workingClip = init( inStream );
        }
        catch ( FileNotFoundException exc )
        {
            String  message = "Audio file not found: " + audioPath;
            logger.log( Level.SEVERE, message, exc );
            workingClip = null;
        }
        return workingClip;
    }
    
    
    /**
     * Instantiates a Clip object using a given input stream.
     * Errors are logged.
     * Null is returned if the operation fails.
     * 
     * @param audioPath the given input stream
     * 
     * @return the instantiated Clip object, or null if failed
     */
    private Clip init( InputStream inStream )
    {
        Clip    workingClip = null;
        
        try
        {
            AudioInputStream    input       =
                AudioSystem.getAudioInputStream( inStream );
            workingClip = AudioSystem.getClip();
            workingClip.open( input );
        } 
        catch ( 
            UnsupportedAudioFileException 
            | IOException
            | LineUnavailableException exc
        ) 
        {
            String  message = 
                "Failed to read input stream, source: " + source;
            logger.log( Level.SEVERE, message, exc );
            workingClip = null;
        }
        
        return workingClip;
    }
}
