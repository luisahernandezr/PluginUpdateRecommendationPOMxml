package filesManagement;
import java.io.File;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class PomTreatment {

	String artifactName_Old_F1 []= new String [50];
	String artifactName_New_F1 []= new String [50];

	String versions_Old_F1 []= new String [50];
	String versions_New_F1 []= new String [50];

	/**
	 * @author Luisa Hernández R.
	 * @param typeVersion String that lets know if the version is OLD or NEW
	 * @param filePOM_path String that indicates the path of the POM.xml
	 * 
	 * This method allows to read each file pom.xml
	 */
	public void readFile_DOM(String typeVersion , String filePOM_path)
	{
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(new File(filePOM_path));
			doc.getDocumentElement().normalize();

			System.out.println("El elemento raíz es: " + doc.getDocumentElement().getNodeName());
			NodeList listaPersonas = doc.getElementsByTagName("plugin");

			String primerHijo []=new String[listaPersonas.getLength()];
			String segundoHijo []=new String[listaPersonas.getLength()];
			String tercerHijo []=new String[listaPersonas.getLength()];

			String primerHijo_dato []=new String[listaPersonas.getLength()];
			String segundoHijo_dato []=new String[listaPersonas.getLength()];
			String tercerHijo_dato []=new String[listaPersonas.getLength()];

			for (int i = 0; i < listaPersonas.getLength(); i ++) {

				Node persona = listaPersonas.item(i);
				Element elemento = (Element) persona;
				//PRUEBA
				if(elemento.getChildNodes().item(1)!=null)
				{
					primerHijo[i]=elemento.getChildNodes().item(1).getNodeName();
					primerHijo_dato[i]=elemento.getChildNodes().item(1).getTextContent();
					segundoHijo[i]=elemento.getChildNodes().item(3).getNodeName();
					segundoHijo_dato[i]=elemento.getChildNodes().item(3).getTextContent();

					if (elemento.getChildNodes().item(5)==null)
					{
						tercerHijo[i]="NULO";
					}
					else
					{
						tercerHijo[i]=elemento.getChildNodes().item(5).getNodeName();
						tercerHijo_dato[i]=elemento.getChildNodes().item(5).getTextContent();
					}


					if(primerHijo[i].equals("groupId"))	{
						if(segundoHijo[i].equals("artifactId")) {
							if (tercerHijo[i].equals("version"))	{
								if(typeVersion.equals("old"))	{
									artifactName_Old_F1[i]=segundoHijo_dato[i];
									versions_Old_F1[i]=tercerHijo_dato[i];
								}else {
									artifactName_New_F1[i]=segundoHijo_dato[i];
									versions_New_F1[i]=tercerHijo_dato[i];
								}
							}
						}
					}else if(primerHijo[i].equals("artifactId")) {
						if(segundoHijo[i].equals("version")){
							if (typeVersion.equals("old")){
								artifactName_Old_F1[i]=primerHijo_dato[i];
								versions_Old_F1[i]=segundoHijo_dato[i];
							}else	{
								artifactName_New_F1[i]=primerHijo_dato[i];
								versions_New_F1[i]=segundoHijo_dato[i];
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}



	}

	/**
	 * @author Luisa Hernández R.
	 * @param vectorToRemove vector with nulls to be removed
	 * @return vector without nulls
	 */
	public String [] removeNullsFromVector (String [] vectorToRemove)
	{
		int count_newSize=0;

		for (int i = 0; i < vectorToRemove.length; i++) {
			if(vectorToRemove[i]!=null)
				count_newSize++;
		}

		String vectorWithoutNulls [] = new String[count_newSize];
		for (int j = 0; j < count_newSize; j++) 
		{
			if(vectorToRemove[j]!=null)
				vectorWithoutNulls[j]=vectorToRemove[j];
			else
				vectorWithoutNulls[j]="0000";
		}
		return vectorWithoutNulls;
	}

	/**
	 * @author Luisa Hernández R.
	 * void method that compareArtifacts to find the equalnames and pass it as a new vector with just the equasl to be
	 * compare then their versions, so it calls the @method compareVersion() 
	 */
	public void compareArtifactPlugins()
	{
		//Aqui se atribuyen los datos SIN valores NULLOS de cada vector OLD y NEW para los artefactos y las versiones 
		String [] artifactName_OLD_F1_withoutNULLS =removeNullsFromVector(artifactName_Old_F1);
		String [] artifactName_NEW_F1_withoutNULLS=removeNullsFromVector(artifactName_New_F1);

		String [] versions_OLD_F1_withoutNULLS=removeNullsFromVector(versions_Old_F1);
		String [] versions_NEW_F1_withoutNULLS= removeNullsFromVector(versions_New_F1);

		//EN ESTE VECTOR SE VAN A GUARDAR LAS POSICIONES DE LOS IGUALES EN FASE I para despues imprimirlos con esos valores.
		int [] versionPosicionesOLD= new int [artifactName_OLD_F1_withoutNULLS.length];
		int [] versionPosicionesNEW= new int [artifactName_OLD_F1_withoutNULLS.length];
		int llenaVectores=0;


		//AQUI SE CREAN LOS VECTORES DE LA FASE II para tener solo los vectores con datos iguales
		String [] versions_OLD_F2_artEquals= new String [versions_OLD_F1_withoutNULLS.length];
		String [] versions_NEW_F2_artEquals=new String [versions_NEW_F1_withoutNULLS.length];

		String [] artifactName_OLD_F2_equalNames= new String [artifactName_OLD_F1_withoutNULLS.length];
		String [] artifactName_NEW_F2_equalNames= new String[artifactName_NEW_F1_withoutNULLS.length];

		if (artifactName_OLD_F1_withoutNULLS.length<artifactName_NEW_F1_withoutNULLS.length) //si es MENOR old
		{
			for (int i = 0; i < artifactName_NEW_F1_withoutNULLS.length; i++)
			{
				for (int j = 0; j < artifactName_OLD_F1_withoutNULLS.length; j++) 
				{
					if (artifactName_NEW_F1_withoutNULLS[i].equals(artifactName_OLD_F1_withoutNULLS[j]))
					{    // se llenan los vectores de la Fase 2 
						// vector de nombres iguales, solo para comparar
						artifactName_OLD_F2_equalNames[llenaVectores]=artifactName_OLD_F1_withoutNULLS[j];
						artifactName_NEW_F2_equalNames[llenaVectores]= artifactName_NEW_F1_withoutNULLS[i];

						// vector de versiones con nombres iguales, falta comparar que las versiones sean actualizables en OLD
						versions_OLD_F2_artEquals[llenaVectores]=versions_OLD_F1_withoutNULLS[j];
						versions_NEW_F2_artEquals[llenaVectores]=versions_NEW_F1_withoutNULLS[i];

						//GUARDAMOS POSICIONES DE NOMBRES IGUALES PARA OLD Y NEW 
						versionPosicionesOLD[llenaVectores]=j;	
						versionPosicionesNEW[llenaVectores]=i;

						llenaVectores++;
					}
					else
					{
					}}

			}
		}
		else if (artifactName_OLD_F1_withoutNULLS.length>artifactName_NEW_F1_withoutNULLS.length)//old es mayor
		{
			for (int i = 0; i < artifactName_OLD_F1_withoutNULLS.length; i++)
			{
				for (int j = 0; j < artifactName_NEW_F1_withoutNULLS.length; j++) 
				{
					if (artifactName_OLD_F1_withoutNULLS[i].equals(artifactName_NEW_F1_withoutNULLS[j]))
					{
						// se	 llenan los vectores de la Fase 2 
						// vector de nombres iguales, solo para comparar
						artifactName_OLD_F2_equalNames[llenaVectores]=artifactName_OLD_F1_withoutNULLS[i];
						artifactName_NEW_F2_equalNames[llenaVectores]= artifactName_NEW_F1_withoutNULLS[j];

						// vector de versiones con nombres iguales, falta comparar que las versiones sean actualizables en OLD
						versions_OLD_F2_artEquals[llenaVectores]=versions_OLD_F1_withoutNULLS[i];
						versions_NEW_F2_artEquals[llenaVectores]=versions_NEW_F1_withoutNULLS[j];

						//GUARDAMOS POSICIONES DE NOMBRES IGUALES PARA OLD Y NEW 
						versionPosicionesOLD[llenaVectores]=i;	
						versionPosicionesNEW[llenaVectores]=j;

						llenaVectores++;
					}
					else
					{
					}
				}
			}
		}
		else //SI SON IGUALES
			for (int i = 0; i < artifactName_OLD_F1_withoutNULLS.length; i++)
			{
				for (int j = 0; j < artifactName_NEW_F1_withoutNULLS.length; j++) 
				{
					if (artifactName_OLD_F1_withoutNULLS[i].equals(artifactName_NEW_F1_withoutNULLS[j]))
					{
						// se	 llenan los vectores de la Fase 2 
						// vector de nombres iguales, solo para comparar
						artifactName_OLD_F2_equalNames[llenaVectores]=artifactName_OLD_F1_withoutNULLS[i];
						artifactName_NEW_F2_equalNames[llenaVectores]= artifactName_NEW_F1_withoutNULLS[j];

						// vector de versiones con nombres iguales, falta comparar que las versiones sean actualizables en OLD
						versions_OLD_F2_artEquals[llenaVectores]=versions_OLD_F1_withoutNULLS[i];
						versions_NEW_F2_artEquals[llenaVectores]=versions_NEW_F1_withoutNULLS[j];

						//GUARDAMOS POSICIONES DE NOMBRES IGUALES PARA OLD Y NEW 
						versionPosicionesOLD[llenaVectores]=i;	
						versionPosicionesNEW[llenaVectores]=j;

						llenaVectores++;
					}
					else
					{
					}
				}
			}
		compareVersions(artifactName_OLD_F2_equalNames, artifactName_NEW_F2_equalNames, versions_OLD_F2_artEquals, versions_NEW_F2_artEquals);
	}

	/**
	 * This method compares the versions and call the @class FileWriting to generate a file recomendation.txt with the final result of the plugins recommendation
	 * @param artifactName_OLD_F2_equalNames vector with artifactsNames equals to the Newone
	 * @param artifactName_NEW_F2_equalNames vector with artifactsNames equals to the Oldone
	 * @param versions_OLD_F2_artEquals vector with the OLDversions of the artifact that were equal in method comparevector
	 * @param versions_NEW_F2_artEquals vector with the NEWversions of the artifact that were equal in method comparevector
	 */
	public void compareVersions(String artifactName_OLD_F2_equalNames[], String artifactName_NEW_F2_equalNames[],
			String [] versions_OLD_F2_artEquals, String []versions_NEW_F2_artEquals)
	{
		int contadorOLD=0;
		int contadorNEW=0;

		for (int i = 0; i < versions_OLD_F2_artEquals.length; i++)		{
			if(versions_OLD_F2_artEquals[i]!=null)
				contadorOLD++;
		}
		for (int i = 0; i < versions_NEW_F2_artEquals.length; i++)		{
			if(versions_NEW_F2_artEquals[i]!=null)
				contadorNEW++;
		}
		String finalArtifactsRecommendation_F3[]= new String [contadorOLD];
		String finalVersion_OLD_recommendation_F3[]=new String [contadorOLD];
		String finalVersion_NEW_recommendation_F3[]=new String [contadorOLD];
		int posicionFinalRecommendations=0;

		if (artifactName_OLD_F2_equalNames!=null && contadorNEW>0 && versions_NEW_F2_artEquals!=null && versions_OLD_F2_artEquals!=null) 
			for (int i = 0; i < versions_OLD_F2_artEquals.length; i++) // hasta el tamaño del vector versiones, los dos son iguales
			{
				//TOKENIZO EL SUBVECTOR DE LAS VERSIONES VIEJAS
				if (versions_OLD_F2_artEquals[i]==null||versions_NEW_F2_artEquals==null	||artifactName_OLD_F2_equalNames==null||artifactName_OLD_F2_equalNames==null) 
					i++;
				else {
					StringTokenizer tokens=new StringTokenizer(versions_OLD_F2_artEquals[i],".");
					String subVersionesOLD []= new String[versions_OLD_F2_artEquals[i].length()];
					int cuentaOLD=tokens.countTokens();
					while (tokens.hasMoreTokens())	{
						for (int j = 0; j < cuentaOLD; j++)	{
							subVersionesOLD [j] = tokens.nextToken().toString();
						}
					}

					//TOKENIZO EL VECTOR DE LAS VERSIONES NUEVAS
					StringTokenizer tokensNEW=new StringTokenizer(versions_NEW_F2_artEquals[i],".");
					String subVersionesNEW []= new String[versions_NEW_F2_artEquals[i].length()];
					int cuentaNEW=tokensNEW.countTokens();
					while (tokensNEW.hasMoreTokens()){
						for (int j = 0; j < cuentaNEW; j++) {
							subVersionesNEW [j] = tokensNEW.nextToken().toString();
						}
					}

					if (cuentaOLD< cuentaNEW) {
						for (int j = 0; j < cuentaOLD; j++) {
							int numsubOLD= Integer.parseInt(subVersionesOLD[j]);
							int numsubNEW= Integer.parseInt(subVersionesNEW[j]);

							if(numsubOLD<numsubNEW) //Hay que recomendar cambio 
							{
								finalArtifactsRecommendation_F3[posicionFinalRecommendations]=artifactName_OLD_F2_equalNames[i];
								finalVersion_OLD_recommendation_F3[posicionFinalRecommendations]=versions_OLD_F2_artEquals[i];
								finalVersion_NEW_recommendation_F3[posicionFinalRecommendations]=versions_NEW_F2_artEquals[i];

								posicionFinalRecommendations++;

								j=cuentaOLD; //ROMPER CICLO PARA QUE MIRE SIGUUIENTE VERSION
							}else if(numsubOLD>numsubNEW){	
								j=cuentaOLD;
							}else if(numsubOLD==numsubNEW && subVersionesNEW[j++]!=null){
								finalArtifactsRecommendation_F3[posicionFinalRecommendations]=artifactName_OLD_F2_equalNames[i];
								finalVersion_OLD_recommendation_F3[posicionFinalRecommendations]=versions_OLD_F2_artEquals[i];
								finalVersion_NEW_recommendation_F3[posicionFinalRecommendations]=versions_NEW_F2_artEquals[i];

								posicionFinalRecommendations++;
								j=cuentaOLD;
							}
						}
					}
					else if (cuentaOLD > cuentaNEW)// tiene mas digitos tipo 2.3.4.5 el NEW
					{
						for (int j = 0; j < cuentaNEW; j++) {
							int numsubOLD= Integer.parseInt(subVersionesOLD[j]);
							int numsubNEW= Integer.parseInt(subVersionesNEW[j]);

							if(numsubOLD<numsubNEW) //Hay que recomendar cambio 
							{
								finalArtifactsRecommendation_F3[posicionFinalRecommendations]=artifactName_OLD_F2_equalNames[i];
								finalVersion_OLD_recommendation_F3[posicionFinalRecommendations]=versions_OLD_F2_artEquals[i];
								finalVersion_NEW_recommendation_F3[posicionFinalRecommendations]=versions_NEW_F2_artEquals[i];

								posicionFinalRecommendations++;
								j=cuentaNEW; //ROMPER CICLO PARA QUE MIRE SIGUUIENTE VERSION
							}
							else if(numsubOLD>numsubNEW)
								j=cuentaOLD;
						}
					}
					else if(cuentaOLD==cuentaNEW)
					{
						for (int j = 0; j < cuentaOLD; j++) {
							int numsubOLD= Integer.parseInt(subVersionesOLD[j]);
							int numsubNEW= Integer.parseInt(subVersionesNEW[j]);

							if(numsubOLD<numsubNEW) //Hay que recomendar cambio 
							{
								finalArtifactsRecommendation_F3[posicionFinalRecommendations]=artifactName_OLD_F2_equalNames[i];
								finalVersion_OLD_recommendation_F3[posicionFinalRecommendations]=versions_OLD_F2_artEquals[i];
								finalVersion_NEW_recommendation_F3[posicionFinalRecommendations]=versions_NEW_F2_artEquals[i];


								posicionFinalRecommendations++;
								j=cuentaOLD;//ROMPER CICLO PARA QUE MIRE SIGUUIENTE VERSION
							}else if(numsubOLD>numsubNEW)
								j=cuentaOLD;
						}
					}
				}
			}
		FileWriting insFileWriting= new FileWriting();
		insFileWriting.escribir(finalArtifactsRecommendation_F3, finalVersion_OLD_recommendation_F3,  finalVersion_NEW_recommendation_F3);
	}

}