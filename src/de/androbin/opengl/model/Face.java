package de.androbin.opengl.model;

import org.lwjgl.util.vector.Vector3f;

public final class Face {
  public final int[] vertices;
  public final int[] textures;
  public int[] normals;
  
  public Face( final int[] vertices, final int[] textures, final int[] normals ) {
    this.vertices = vertices;
    this.textures = textures;
    this.normals = normals;
  }
  
  public boolean contains( final Vector3f[] vertices, final float[] constant,
      final float[] multiple, final float x, final float y ) {
    Vector3f vj = vertices[ this.vertices[ this.vertices.length - 1 ] ];
    boolean oddNodes = false;
    
    for ( int i = 0; i < this.vertices.length; i++ ) {
      final Vector3f vi = vertices[ this.vertices[ i ] ];
      
      if ( vi.y < y && vj.y >= y || vj.y < y && vi.y >= y ) {
        oddNodes ^= y * multiple[ i ] + constant[ i ] < x;
      }
      
      vj = vi;
    }
    
    return oddNodes;
  }
  
  public void precalc( final Vector3f[] vertices, final float[] constant, final float[] multiple ) {
    Vector3f vj = vertices[ this.vertices[ this.vertices.length - 1 ] ];
    
    for ( int i = 0; i < this.vertices.length; i++ ) {
      final Vector3f vi = vertices[ this.vertices[ i ] ];
      
      if ( vj.y == vi.y ) {
        constant[ i ] = vi.x;
        multiple[ i ] = 0f;
      } else {
        multiple[ i ] = ( vj.x - vi.x ) / ( vj.y - vi.y );
        constant[ i ] = vi.x - vi.y * multiple[ i ];
      }
      
      vj = vi;
    }
  }
}