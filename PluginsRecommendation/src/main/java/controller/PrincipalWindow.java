package controller;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import directoryManagement.CopyDirectories;
import filesManagement.DirectoryDeleting;
import filesManagement.PomTreatment;

public class PrincipalWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 001;
	JPanel pdatos;
	JLabel limagen,labelName;
	String directoryNames="";

	//Ruta Destino de proyecto old y es ahi donde estara el POM.xml old

	String pathDestOlder="C:\\older";
	String pathDestNewer="C:\\newer";
		
	//Rutas de cada POM.XML tanto del sw antiguo como del nuevo
	public String path_POM_older=pathDestOlder+"\\pom.xml";	
	String path_POM_newer=pathDestNewer+"\\pom.xml";	
	
	//Aqui se guardara un reporte en cada una de las versiones
	String path_FileRecommendation=pathDestOlder+"\\recommendation.txt";

	int contaOld=0;
	int contaNew=0;

	//inicializa instancia para manejo de los archivos pom.xml
	PomTreatment insFileReading= new PomTreatment();

	//Maneja los atributos gráficos para leer el archivo recomendación.
	FileInputStream pathFileRecommendation_inStream;
	BufferedReader bufferedReader_FileRecommendation;
	JTextArea textArea_recommendation;
	String textAreatoShow = " ";
	JScrollPane scrollArea;
	//maneja los botones para abrir proyectos OLD y NEW
	JPanel software_old;	
	JButton btmOpen_swOld;
	JPanel software_new;
	JButton btmOpen_swNew;

	/**
	 * This method manage all the principal window interface and call method to do the plugins recommendation
	 */
	public PrincipalWindow()
	{

		//Declaration of Labels and buttoms
		software_old=new JPanel();		
		software_old.add(new JLabel("Select old software: "));
		software_new=new JPanel();
		software_new.add(new JLabel("Select new software: "));

		btmOpen_swOld=new JButton("Open Old");
		btmOpen_swNew=new JButton("Open New");

		//activates file chooser for user and call method to copy directories
		fileChooserAction(btmOpen_swOld, "oldVersion");
		fileChooserAction(btmOpen_swNew, "newVersion");

		//add buttoms to labels
		software_old.add(btmOpen_swOld);
		software_new.add(btmOpen_swNew); 

		pdatos=new JPanel();
		pdatos.add(new JLabel("DIRECTORIES"));
		labelName=new JLabel();
		pdatos.add(labelName);

		add(software_old,BorderLayout.WEST);
		add(software_new,BorderLayout.EAST);
		//add(recommendationPanel,BorderLayout.WEST);
		add(pdatos);
	}

	/**
	 * this method writes the @file recommendation.txt with the final result of the program and add the
	  content of the file to the window.
	 * @throws IOException this is for treating the file exception
	 */
	public void readRecommendationFile() throws IOException
	{
		//DESDE AQUI INTENTO PARA MOSTRAR ARCHIVO GENERADO //ESTO DEBE IR CUANDO SE ACTIVAN LOS DOS BOTONES
		pathFileRecommendation_inStream = new FileInputStream(path_FileRecommendation);
		bufferedReader_FileRecommendation = new BufferedReader(new InputStreamReader(pathFileRecommendation_inStream));

		textArea_recommendation = new JTextArea(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		try {
			String strLinea;
			while ((strLinea = bufferedReader_FileRecommendation.readLine()) != null)   {
				textAreatoShow= textAreatoShow+strLinea+"\n";
			}
		} finally {
			if (bufferedReader_FileRecommendation != null) {
				bufferedReader_FileRecommendation.close();
			}
		}

		textArea_recommendation.setText(textAreatoShow);
		textArea_recommendation.setEditable(false);

		scrollArea = new JScrollPane(textArea_recommendation, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		add(scrollArea, BorderLayout.SOUTH);
		
		DirectoryDeleting insDirectoryDeleting= new DirectoryDeleting();
		File pathOld= new File(pathDestOlder);
		File pathNew= new File(pathDestNewer);
		insDirectoryDeleting.deleteDirectory(pathOld);
		insDirectoryDeleting.deleteDirectory(pathNew);
		
		if(pathNew.delete()&&pathOld.delete())
			System.out.println("han sido borrado correctamente");
	}

	/**
	 * this method calls the rigth methods when the bottoms are chosen, and lets select the OLD and new projects for treating them
	 * @param btmType tipo de boton oprimido old/new
	 * @param inputVersion Cadena de entrada que indica si es old/new para asi mismo elegir ruta destino
	 */
	public void fileChooserAction(JButton btmType, String inputVersion)
	{
		JButton type=btmType;
		final String strInputVersion=inputVersion;

		type.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				JFileChooser selector=new JFileChooser();
				selector.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int r=selector.showOpenDialog(null);
				if(r==JFileChooser.APPROVE_OPTION){
					try {
						File fileSelected=selector.getSelectedFile();

						//Ruta Fuente de proyectos a copiar
						File ruta= new File (fileSelected.getAbsolutePath());
						System.out.println("ruta "+ruta);

						//Validación si es nuevo o antoguo para guardar separados proyectos en ruta destino, con eso saber donde queda cada archivo POM
						if(strInputVersion.equals("oldVersion"))		{												
							//llama metodo para copiar en ruta destino de carpeta antigua
							pathDestinyByVersion("old",ruta, pathDestOlder);
							System.out.println("Se ha copiado directorio OLD ");
							contaOld++;
							insFileReading.readFile_DOM("old",path_POM_older);

						}else if(strInputVersion.equals("newVersion"))			{
							//llama metodo para copiar en ruta destino de carpeta nueva
							pathDestinyByVersion("new",ruta, pathDestNewer);		
							System.out.println("Se ha copiado directorio NEW ");	
							contaNew++;
							insFileReading.readFile_DOM("new",path_POM_newer);
						}

						if(contaOld>0 &&contaNew>0)		{
							software_old.setVisible(false);
							software_new.setVisible(false);
							btmOpen_swOld.setVisible(false);
							btmOpen_swNew.setVisible(false);
							//System.out.println("- - - - - - - - han abierto los dos proyectos- - -  - - - - - -\n");
							//llamamos metodo que compara los nombres de los plugins para ver si son iguales y comparar sus numero de versiones
							insFileReading.compareArtifactPlugins();

							readRecommendationFile();
						}
						//String con nombre de cada uno de los directorios seleccionados
						directoryNames=directoryNames+" \n "+fileSelected.getName();						
						labelName.setText(directoryNames);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		} );
	}

	/**
	 * this method lets duplicate the projects content for reading the pom.xml of each one without problems of overwriting
	 * @param pathSource path of the origin projects
	 * @param pathDestinyString path to make the copy and treat the pom.xml files
	 */
	public void pathDestinyByVersion(String versionType,File pathSource, String pathDestinyString)
	{
		//llamado a clase para copiar directorios y archivos correspondientes
		CopyDirectories insCopyDirectories= new CopyDirectories();
		//Ruta Destino de proyecto nuevo
		File pathDestiny = new File(pathDestinyString);

		System.out.println("rutaDestino "+pathDestiny);
		try {
			insCopyDirectories.copyDirectory(versionType,pathSource, pathDestiny);
		} catch (IOException e1) {
			e1.printStackTrace();
		}	
	}

}