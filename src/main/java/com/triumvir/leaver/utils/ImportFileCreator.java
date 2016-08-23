package com.triumvir.leaver.utils;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportFileCreator {

	public static void main(String[] args) 
	{
		try 
		{
			String xmlRootPath = "./resources/";
			String resultPath = "./resources/ImportFile/";
			String resultFileName = "ImportFile.xml";
			String xmlHeader = "<?xml version='1.0' encoding='UTF-8'?>\n<!DOCTYPE sailpoint PUBLIC \"sailpoint.dtd\" \"sailpoint.dtd\">\n";

			System.out.println("Creating the result folder if not exists!");
			if(!Files.exists(Paths.get(resultPath)))
			{
				File resultFolder = new File(resultPath);
				resultFolder.mkdir();
			}

			System.out.println("Getting XMLs Path");
			ArrayList<File> xmlsPathList = new ArrayList<File>();
			getFilePath(xmlRootPath, xmlsPathList);

			System.out.println("Creating the xml import file...");
			PrintWriter writer = new PrintWriter(resultPath + resultFileName, "UTF-8");
			writer.write(xmlHeader + "<sailpoint>");

			//Regex to remove the xml header in each file...
			String pattern1 = "<?xml version='1.0' encoding='UTF-8'?>" + System.getProperty("line.separator") + "<!DOCTYPE ";
			String pattern2 = " PUBLIC \"sailpoint.dtd\" \"sailpoint.dtd\">";
			Pattern p = Pattern.compile(Pattern.quote(pattern1) + "(.*?)" + Pattern.quote(pattern2));
			Matcher m = null;
			String stringToRemove = pattern1;
			
			System.out.println("Reading XMLs");
			for(File file : xmlsPathList){
				if(!file.getName().equals(resultFileName))
				{
					String content = new String(Files.readAllBytes(file.toPath()));
					m = p.matcher(content);
					while(m.find()){
						stringToRemove += m.group(1);
					}
					stringToRemove += pattern2;
					content = content.replace(stringToRemove, "");
					writer.write(content);
					stringToRemove = pattern1;
				}
			}

			writer.write("</sailpoint>");
			writer.close();
			System.out.println("Job complete!");
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	private static File[] getFilePath(String directoryName, ArrayList<File> files) {
		File directory = new File(directoryName);

		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				files.add(file);
			} else if (file.isDirectory()) {
				getFilePath(file.getAbsolutePath(), files);
			}
		}
		return fList;
	}


}
