package de.androbin.opengl.model;

public final class FaceParser {
  private final int[] ivertices;
  private final int[] itextures;
  private final int[] inormals;
  
  private boolean normal;
  private boolean texture;
  
  private Face face;
  
  public FaceParser( final int length ) {
    ivertices = new int[ length ];
    itextures = new int[ length ];
    inormals = new int[ length ];
  }
  
  public Face getFace() {
    if ( face == null ) {
      face = new Face( ivertices,
          texture ? itextures : null,
          normal ? inormals : null );
    }
    
    return face;
  }
  
  public FaceParser parse( final String[] split ) {
    for ( int i = 0; i < split.length - 1; i++ ) {
      final String[] split_ = split[ ( i + 1 ) ].split( "/" );
      
      if ( split_.length == 3 ) {
        inormals[ i ] = Integer.parseInt( split_[ 2 ] ) - 1;
        normal = true;
      }
      
      if ( split_.length >= 2 && !split_[ 1 ].isEmpty() ) {
        itextures[ i ] = Integer.parseInt( split_[ 1 ] ) - 1;
        texture = true;
      }
      
      ivertices[ i ] = Integer.parseInt( split_[ 0 ] ) - 1;
    }
    
    return this;
  }
}