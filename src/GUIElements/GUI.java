package GUIElements;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import GraphElements.Main;

public class GUI extends JFrame{
	private JPanel panel1;
	private JPanel panel2;
	private JPanel inputPanel;
	private JPanel outputPanel;
	private JComboBox fileDropDown;
	private JTextArea tab1TextArea;
	private JTextArea tab2TextArea;
	private JScrollPane textScrollPane1;
	private JScrollPane textScrollPane2;
	private JLabel fileInputLabel;
	private JTextField fileInputField;
	private JLabel fileOutputLabel;
	private JTextField fileOutputField;
	private JButton EnterButton;
	
	public GUI() throws IOException{
	
		setTitle("Flight Program");
		setLayout(new BorderLayout());
		
		//Panel1 for default test cases
		panel1 = new JPanel();
		panel1.setLayout(new BoxLayout(panel1, BoxLayout.PAGE_AXIS));
		EventListener eListener = new EventListener();
		JTextField tab1TextField = new JTextField("Default test case outputs.");
		tab1TextField.setEditable(false);
		
		//Default output file names for test cases 1-8
		String[] files = {"output1.txt", "output2.txt", "output3.txt", "output4.txt",
				"output5.txt", "output6.txt", "output7.txt", "output8.txt"};
		
		//Drop down for default output files
		fileDropDown = new JComboBox(files);
		fileDropDown.addActionListener(eListener);
		
		//Store first test file as a string to output to text area
		FileInputStream is = new FileInputStream("output1.txt");
		String fileAsString = storeInputInString(is);
		tab1TextArea = new JTextArea(fileAsString);
		tab1TextArea.setEditable(false);
		
		textScrollPane1 = new JScrollPane(tab1TextArea); 
		panel1.add(tab1TextField);
		panel1.add(fileDropDown);
		panel1.add(textScrollPane1);


		//Panel2 for custom file 
		panel2 = new JPanel();
		panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));
		
		//Components for panel2
		fileInputLabel = new JLabel("Name of custom input file: ");
		fileInputField = new JTextField("", 15);
		fileOutputLabel = new JLabel("Name of output file: " );
		fileOutputField = new JTextField("", 15);
		EnterButton = new JButton("Enter");
		EnterButton.addActionListener(eListener);
		
		//Input file panel
		inputPanel = new JPanel();
		inputPanel.add(fileInputLabel);
		inputPanel.add(fileInputField);
		
		//Output file panel
		outputPanel = new JPanel();
		outputPanel.add(fileOutputLabel);
		outputPanel.add(fileOutputField);
		outputPanel.add(EnterButton);
		
		//Output file contents
		tab2TextArea = new JTextArea("No output file given.");
		tab2TextArea.setEditable(false);
		textScrollPane2 = new JScrollPane(tab2TextArea);
		
		//Custom file panel
		panel2.add(inputPanel);
		panel2.add(outputPanel);
		panel2.add(textScrollPane2);
		
		//Add panel1 and panel2 to the tabbedPane
		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Default Files", panel1);
		tabbedPane.addTab("Custom File", panel2);
		this.add(tabbedPane);
		
		//Pack and set visibility for window
		this.pack();	
		setVisible(true);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	
	private class EventListener implements ActionListener{
		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == fileDropDown){
				changeOutputFile();
			} else if(event.getSource() == EnterButton){
				if(fileOutputField.getText() == ""){
					JOptionPane.showMessageDialog(null, "Enter an output file name.", "Output file not specified", JOptionPane.ERROR_MESSAGE);
				} else {
					FileInputStream is;
					try {
						parseSingleFile();
						is = new FileInputStream(fileOutputField.getText());
						String output = storeInputInString(is);
						tab2TextArea.setText(output);
					} catch (FileNotFoundException e) {
						JOptionPane.showMessageDialog(null, "Could not find input file " + "\"" + fileInputField.getText() + "\"", "Invalid input file", JOptionPane.ERROR_MESSAGE);
					} catch (IOException e) {
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}
	
	//changeOutputFile: void -> void
	//This changes the output file contents in the tab1TextArea for the scroll
	//pane in the first tab. The contents will change depending on the drop down
	//selection.
	private void changeOutputFile(){
		String file = "";
		file = (String)fileDropDown.getSelectedItem();
		try{
			FileInputStream is = new FileInputStream(file);
			String newFileString = storeInputInString(is);
			tab1TextArea.setText(newFileString);
			this.pack();
		} catch (IOException e){
			JOptionPane.showMessageDialog(null, "Could not find output file " + "\"" + file + "\"",  "Invalid file", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	//storeInputInString: FileInputStream -> String
	//Given a FileInputStream this function returns the file contents stored as a string
	//in order to display the contents in JTextAreas
	private String storeInputInString(FileInputStream is) throws IOException{
		BufferedReader buf = new BufferedReader(new InputStreamReader(is));
		String line = buf.readLine();
		StringBuilder sb = new StringBuilder();
		while(line != null){
			sb.append(line).append("\n");
			line = buf.readLine();
		}
		is.close();
		return sb.toString();
	}
	
	//parseSingleFile: void -> void
	//Gets the input file name, output file name,  and calls upon the parseFile function
	//in Main.java in order to parse the file and write the contents of performing the commands
	//into the output file.
	private void parseSingleFile(){
		try{
			String inputFile = fileInputField.getText();
			String outputFile = fileOutputField.getText();
			Main.parseFile(inputFile, outputFile);
		} catch (IOException e){
			JOptionPane.showMessageDialog(null, "Could not find file " + "\"" + fileInputField.getText() + "\"",  "Invalid file", JOptionPane.ERROR_MESSAGE);
		}
		return;
	}
}
