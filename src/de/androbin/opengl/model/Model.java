package de.androbin.opengl.model;

import static org.lwjgl.opengl.GL11.*;
import de.androbin.collection.util.*;
import de.androbin.math.util.floats.*;
import de.androbin.opengl.*;
import de.androbin.opengl.util.*;
import org.lwjgl.util.vector.*;

public final class Model implements Renderable {
  public Vector3f[] vertices;
  public Vector2f[] textures;
  public Vector3f[] normals;
  
  public Face[] faces;
  
  public Vector3f pos;
  public Vector3f size;
  
  public Model( final Vector3f[] vertices, final Vector2f[] textures, final Vector3f[] normals,
      final Face[] faces ) {
    this.vertices = vertices;
    this.textures = textures;
    this.normals = normals;
    this.faces = faces;
  }
  
  public void calcNormals() {
    normals = ObjectCollectionUtil.fill( new Vector3f[ faces.length ], i -> {
      final Face face = faces[ i ];
      face.normals = new int[ face.vertices.length ];
      IntegerCollectionUtil.fill( face.normals, j -> i );
      
      return NormalUtil.getNormalCCW( new Vector3f(), new Vector3f(),
          new Vector3f[] {
              vertices[ face.vertices[ 0 ] ],
              vertices[ face.vertices[ 1 ] ],
              vertices[ face.vertices[ 2 ] ]
          } );
    } );
  }
  
  public boolean contains( final float[][] constant, final float[][] multiple,
      final float x, final float y, final float z ) {
    boolean oddNodes = false;
    
    for ( int i = 0; i < faces.length; i++ ) {
      final Face face = faces[ i ];
      final float face_z = FloatFunctionMathUtil.add( face.vertices.length,
          c -> vertices[ face.vertices[ c ] ].getZ() );
      oddNodes ^= face_z > face.vertices.length * z
          && face.contains( vertices, constant[ i ], multiple[ i ], x, y );
    }
    
    return oddNodes;
  }
  
  public void meta() {
    boolean init = true;
    
    float minX = Float.NaN;
    float minY = Float.NaN;
    float minZ = Float.NaN;
    float maxX = Float.NaN;
    float maxY = Float.NaN;
    float maxZ = Float.NaN;
    
    for ( final Vector3f v : vertices ) {
      if ( init ) {
        minX = maxX = v.x;
        minY = maxY = v.y;
        minZ = maxZ = v.z;
        
        init = false;
      } else {
        minX = FloatMathUtil.min( v.x, minX );
        minY = FloatMathUtil.min( v.y, minY );
        minZ = FloatMathUtil.min( v.z, minZ );
        
        maxX = FloatMathUtil.max( v.x, maxX );
        maxY = FloatMathUtil.max( v.y, maxY );
        maxZ = FloatMathUtil.max( v.z, maxZ );
      }
    }
    
    if ( pos == null ) {
      pos = new Vector3f();
    }
    
    pos.set( minX, minY, minZ );
    
    if ( size == null ) {
      size = new Vector3f();
    }
    
    size.set( maxX - minX, maxY - minY, maxZ - minZ );
  }
  
  public void precalc( final float[][] constant, final float[][] multiple ) {
    for ( int i = 0; i < faces.length; i++ ) {
      final Face face = faces[ i ];
      constant[ i ] = new float[ face.vertices.length ];
      multiple[ i ] = new float[ face.vertices.length ];
      face.precalc( vertices, constant[ i ], multiple[ i ] );
    }
  }
  
  @ Override
  public void render() {
    for ( final Face face : faces ) {
      glBegin( GL_POLYGON );
      
      for ( int i = 0; i < face.vertices.length; i++ ) {
        if ( normals != null ) {
          final int iNormal = face.normals[ i ];
          NormalUtil.glNormal3f( normals[ iNormal ] );
        }
        
        if ( textures != null ) {
          final int iTexture = face.textures[ i ];
          TexCoordUtil.glTexCoord2f( textures[ iTexture ] );
        }
        
        final int iVertex = face.vertices[ i ];
        VertexUtil.glVertex3f( vertices[ iVertex ] );
      }
      
      glEnd();
    }
  }
}