package GraphElements;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.JOptionPane;

import GUIElements.GUI;


public class Main{

	public static void main(String args[]) throws IOException {
		GUI g = new GUI();
		//Run the first 8 default test files
		for(int i = 0; i < 8; i++){
			String inputFileName = "test" + Integer.toString(i+1) + ".txt";
			String outputFileName = "output" + Integer.toString(i+1) + ".txt";
			parseFile(inputFileName, outputFileName);
		}
	}	
	
	public static void parseFile(String inputFileName, String outputFileName) throws IOException{
		Graph flightGraph = new Graph();
		System.out.println("Running " + inputFileName);
		//Create output to write to using buffered writer
		FileWriter writer = new FileWriter(outputFileName);
		BufferedWriter bw = new BufferedWriter(writer);
		
		//Create buffered reader to read from the file
		BufferedReader br = new BufferedReader(new FileReader(inputFileName));
		String line = null;
		
		//Parse each line for commands
		while((line = br.readLine()) != null){
			Parse.parseLine(bw, flightGraph, line);
		}
		br.close();
		bw.close();	
	}
}
