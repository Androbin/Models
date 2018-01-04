package de.androbin.opengl.model;

import java.io.*;
import java.util.*;
import org.lwjgl.util.vector.*;

public final class ModelParser {
  private final List<Vector3f> vertices = new ArrayList<>();
  private final List<Vector2f> textures = new ArrayList<>();
  private final List<Vector3f> normals = new ArrayList<>();
  
  private final List<Face> faces = new ArrayList<>();
  
  private Model model;
  
  public Model getModel() {
    if ( model == null ) {
      model = new Model(
          vertices.toArray( new Vector3f[ vertices.size() ] ),
          textures.isEmpty() ? null : textures.toArray( new Vector2f[ textures.size() ] ),
          normals.isEmpty() ? null : normals.toArray( new Vector3f[ normals.size() ] ),
          faces.toArray( new Face[ faces.size() ] ) );
    }
    
    return model;
  }
  
  private static Vector2f parseVec2f( final String[] split ) {
    final float x = Float.parseFloat( split[ 1 ] );
    final float y = Float.parseFloat( split[ 2 ] );
    
    return new Vector2f( x, y );
  }
  
  private static Vector3f parseVec3f( final String[] split ) {
    final float x = Float.parseFloat( split[ 1 ] );
    final float y = Float.parseFloat( split[ 2 ] );
    final float z = Float.parseFloat( split[ 3 ] );
    
    return new Vector3f( x, y, z );
  }
  
  public ModelParser read( final BufferedReader reader ) {
    reader.lines().forEachOrdered( line -> {
      final String[] split = trim( line ).split( " " );
      
      switch ( split[ 0 ] ) {
        case "f":
          faces.add( new FaceParser( split.length - 1 ).parse( split ).getFace() );
          break;
        case "v":
          vertices.add( parseVec3f( split ) );
          break;
        case "vn":
          normals.add( parseVec3f( split ) );
          break;
        case "vt":
          textures.add( parseVec2f( split ) );
          break;
      }
    } );
    
    return this;
  }
  
  private static String trim( final String s ) {
    return s.trim().replaceAll( "[ |\t]+", " " );
  }
}