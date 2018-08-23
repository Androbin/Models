package de.androbin.opengl.model.util;

import de.androbin.io.util.*;
import de.androbin.opengl.model.*;
import java.io.*;
import java.nio.file.*;

public final class ModelUtil {
  private ModelUtil() {
  }
  
  public static Model loadModel( final Path file ) {
    try {
      return FileReaderUtil.readFile( file, reader -> loadModel( reader ) );
    } catch ( final IOException e ) {
      e.printStackTrace();
      return null;
    }
  }
  
  public static Model loadModel( final BufferedReader reader ) {
    return new ModelParser().read( reader ).getModel();
  }
}