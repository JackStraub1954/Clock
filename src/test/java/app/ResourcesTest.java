package app;

import java.io.InputStream;

public class ResourcesTest
{
    private static final String clipFileBoing  = 
        "cartoon_twang_spring_ruler_ascending_tail_001_55503.mp3";
    
    public static void main(String[] args)
        throws IllegalStateException
    {
        ClassLoader loader  = ResourcesTest.class.getClassLoader();
        InputStream inStr   = loader.getResourceAsStream( clipFileBoing );
        if ( inStr == null )
        {
            String  message = "Failed to load audio clip file: " + clipFileBoing;
            throw new IllegalStateException( message );
        }
        System.out.println( inStr );
    }

}
