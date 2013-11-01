package filesManagement;

import java.io.*;


public class FileWriting
{
	public void escribir(String[] finalArtefactsRecomentaion, String[] finalOLD_versionRecommendation, String[] finalNEW_versionRecommendation)
	{
		File file;
		file = new File("C:\\older\\recommendation.txt");

		try{

			FileWriter fileWriter = new FileWriter(file);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			PrintWriter printWriter = new PrintWriter(bufferedWriter);  

			printWriter.write("\t--------------------------------------- UPDATES RECOMMENDATION ---------------------------------- \n");

			for (int i = 0; i < finalNEW_versionRecommendation.length; i++) {
				if(finalArtefactsRecomentaion[i]!=null)	{
					printWriter.write("\nPlugin id:     "+finalArtefactsRecomentaion[i]+"\n");
					printWriter.write("\tOld Version:   "+finalOLD_versionRecommendation[i] +"\n\tNew Version:  "+finalNEW_versionRecommendation[i]+"\n");
				}
			}

			printWriter.close();
			bufferedWriter.close();

		}catch(IOException e){};

	}}
