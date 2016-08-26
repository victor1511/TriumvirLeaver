package com.triumvir.leaver.utils;

import java.io.File;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.commons.lang.StringUtils;

public class ImportFileCreator {

	private final static String XML_HEADER = "<?xml version='1.0' encoding='UTF-8'?>";
	private final static String DOC_TYPE_P1 = "<!DOCTYPE ";
	private final static String DOC_TYPE_P2 = " PUBLIC \"sailpoint.dtd\" \"sailpoint.dtd\">";
	
	private final static String ROOT_PATH = "./resources/";
	private final static String RESULT_PATH = "./resources/ImportFile/";
	private final static String RESULT_NAME = "ImportFile.xml";
	
	public static void main(String[] args) 
	{
		try 
		{
			System.out.println("Creating the result folder if not exists!");
			if(!Files.exists(Paths.get(RESULT_PATH)))
			{
				File resultFolder = new File(RESULT_PATH);
				resultFolder.mkdir();
			}

			System.out.println("Getting XMLs Path");
			ArrayList<File> xmlsPathList = new ArrayList<File>();
			getFilePath(ROOT_PATH, xmlsPathList);

			System.out.println("Creating the xml import file...");
			PrintWriter writer = new PrintWriter(RESULT_PATH + RESULT_NAME, "UTF-8");
			writer.write(createFileHeader("sailpoint") + "<sailpoint>");

			System.out.println("Reading XMLs");
			String doc_type = "";
			for(File file : xmlsPathList){
				if(!file.getName().equals(RESULT_NAME))
				{
					String content = new String(Files.readAllBytes(file.toPath()));
					doc_type = DOC_TYPE_P1 + StringUtils.substringBetween(content, DOC_TYPE_P1, DOC_TYPE_P2) + DOC_TYPE_P2;
					writer.write(content.replace(XML_HEADER, "").replace(doc_type, ""));
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

	private static String createFileHeader(String objectType){
		return XML_HEADER + System.lineSeparator() + DOC_TYPE_P1 + objectType + DOC_TYPE_P2 + System.lineSeparator();
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