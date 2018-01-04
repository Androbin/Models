package de.androbin.opengl.model.util;

import de.androbin.io.util.*;
import de.androbin.opengl.model.*;
import java.io.*;

public final class ModelUtil {
  private ModelUtil() {
  }
  
  public static Model loadModel( final File file ) {
    return FileReaderUtil.readFile( file, reader -> loadModel( reader ) );
  }
  
  public static Model loadModel( final BufferedReader reader ) {
    return new ModelParser().read( reader ).getModel();
  }
}