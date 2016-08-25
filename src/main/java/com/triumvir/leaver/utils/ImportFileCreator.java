package com.triumvir.leaver.utils;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;

public class ImportFileCreator {

	private static String XML_HEADER = "<?xml version='1.0' encoding='UTF-8'?>";
	private static String DOC_TYPE_P1 = "<!DOCTYPE ";
	private static String DOC_TYPE_P2 = " PUBLIC \"sailpoint.dtd\" \"sailpoint.dtd\">";
	
	private static String newLineWin = "\r\n";
	private static String newLineLin = "\n";
	
	private static String xmlRootPath = "./resources/";
	private static String resultPath = "./resources/ImportFile/";
	private static String resultFileName = "ImportFile.xml";
	
	public static void main(String[] args) 
	{
		try 
		{


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
			writer.write(CreateFileHeader("sailpoint", newLineLin) + "<sailpoint>" + newLineLin);

			System.out.println("Reading XMLs");
			for(File file : xmlsPathList){
				if(!file.getName().equals(resultFileName))
				{
					String content = new String(Files.readAllBytes(file.toPath()));
					writer.write(removeXMLHeader(content));
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
	
	private static String removeXMLHeader(String content){
		String stringToRemove = "";
		String docTypeWin = newLineWin + DOC_TYPE_P1;
		String docTypeLin = newLineLin + DOC_TYPE_P1;
		
		String objectType = StringUtils.substringBetween(content, XML_HEADER + docTypeWin, DOC_TYPE_P2);
		if(objectType == null){
			objectType = StringUtils.substringBetween(content, XML_HEADER + docTypeLin, DOC_TYPE_P2);
			stringToRemove = CreateFileHeader(objectType, newLineLin);
		}
		else
			stringToRemove = CreateFileHeader(objectType, newLineWin);
		
		content = content.replace(stringToRemove, "");
		return content;
	}

	private static String CreateFileHeader(String objectType, String lineSeparator){
		return XML_HEADER + lineSeparator + DOC_TYPE_P1 + objectType + DOC_TYPE_P2 + lineSeparator;
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