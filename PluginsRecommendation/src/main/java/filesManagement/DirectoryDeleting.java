package filesManagement;

import java.io.File;


public class DirectoryDeleting {
	


	 public  void deleteDirectory (File directorio){
         
         File[] ficheros = directorio.listFiles();
         
         for (int x=0;x<ficheros.length;x++){
                 if (ficheros[x].isDirectory()) {
                	 deleteDirectory(ficheros[x]);
                 }
                 ficheros[x].delete();
         }               
 }

 
 
}
