package directoryManagement;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class CopyDirectories {

	/**
	 * CopyDirectory source to DirectoryDest.
	 * @param srcDir source path to be copy
	 * @param dstDir destiny path where copy is made for treating the pom.xml files
	 * @throws IOException for the directories and files exceptions.
	 */
	 public void copyDirectory(String versionType, File srcDir, File dstDir) throws IOException 
	 { 
	        if (srcDir.isDirectory()) { 
	        	    if (!dstDir.exists()) { 
	                dstDir.mkdir(); 
	            }else{	            	
	            	if(versionType.equals("old"))
	   	            	 dstDir = new File("C:\\older");
	            	else
	   	            	 dstDir = new File("C:\\newer");
	            }
	             
	            String[] children = srcDir.list(); 
	            for (int i=0; i<children.length; i++) { 
	                copyDirectory(versionType, new File( srcDir, children[i]), 
	                    new File(dstDir, children[i])); 
	            } 
	        } else { 
	           copyFiles(srcDir, dstDir); 
	        	//Copia los archivos que están dentro del directorio
	        } 
	    } 
	 

	 /**
	  * CopyFiles from directory to directory 
	  * @param src source files to be copy
	  * @param dst destiny files where copy is made for treating the pom.xml files
	  * @throws IOException IOException for the directories and files exceptions.
	  */
	 public void copyFiles(File src, File dst) throws IOException { 
	        InputStream in = new FileInputStream(src); 
	        OutputStream out = new FileOutputStream(dst); 
	       	         
	        byte[] buf = new byte[1024]; 
	        int len; 
	        while ((len = in.read(buf)) > 0) { 
	            out.write(buf, 0, len); 
	        } 
	        in.close(); 
	        out.close(); 
	    } 
	 
	 
	
}
