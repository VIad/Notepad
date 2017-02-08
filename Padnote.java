import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import java.awt.BorderLayout;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Color;
import java.awt.Toolkit;




public class Padnote {
	
	
	private JFrame frmNotepad;
	private HandleEvents handler = new HandleEvents();
	private FileNameExtensionFilter filter = new FileNameExtensionFilter("TEXT FILES", "txt", "text");

	private JMenuItem newFile;
	private JMenuItem openFile;
	private JMenuItem Save;
	private JTextArea textArea;
	private JTextArea props = new JTextArea();
	private JMenu mnFormat;
	private JMenuItem fontText;
	private JMenuItem openSettings;
    private Font setNewFont = new Font(null);
	private JList displayTextSizes;
	private JList displayFontTypes;
	private JList displayFonts;
	private JFileChooser fileChooser;
	private boolean TextSizeChanged = false;
	private boolean FontStyleChanged = false;
	private boolean FontChanged = false;
    private FileWriter writer;
    private FileWriter filePropsWriter;
    private FileWriter filePathWriter;
    private Scanner read;
    String get = getFontProperties("Arial","Standart","25");
  
    private final String [] TextSizeDisplay = {
    		"8","9","10","11","12","20","24","30","36","48","52","74"
    };
    private final int [] TextSize = {
    	     8,9,10,11,12,20,24,30,36,48,52,74	
    };
    private final String [] Fonts = {
    	"Arial", "Bodoni MT", "Book Antiqua","Calisto MT","Courier New","Tahoma","Serif","Times New Roman"	
    };
    private final String [] FontTypeDisplay = {
    	"Standart","Bold","Italic","Bold & Italic"	
    };
    private final int [] FontType = {
    	Font.PLAIN,Font.BOLD,Font.ITALIC,Font.BOLD + Font.ITALIC,	
    };
    private JMenuItem infoOption;
    private JMenuItem exitOption;
    
    

    
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Padnote window = new Padnote();
					window.frmNotepad.setVisible(true);
				}catch(Exception e){}
			}
		});
	}

	/**
	 * Create the application.
	 * @throws IOException 
	 */
	public Padnote() throws IOException{
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 * @throws IOException 
	 */
	private void initialize() throws IOException{
		//setting up writing panel
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());			
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e){}
		//Create directory & files for notepad stuff
		File createdir = new File("C:\\VladPad\\SavedProperties");
		File createPathsFile = new File("C:\\VladPad\\Paths.path");
		if(!createdir.exists()){
			createdir.mkdirs();
		}
		if(!createPathsFile.exists()){
			createPathsFile.createNewFile();
		}
        validateFiles();
		frmNotepad = new JFrame();
		frmNotepad.setForeground(Color.WHITE);
		frmNotepad.setTitle("Notepad");
		frmNotepad.setBounds(100, 100, 700, 625);
		frmNotepad.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmNotepad.addWindowListener(
				new WindowAdapter(){
					@Override
					public void windowClosing(WindowEvent e){
						if(!textArea.getText().trim().equals("")){
							Object[] Options ={
									"Save",
									"Don't save"
							};
							int choice = JOptionPane.showOptionDialog(null,"Do you want to save before exiting ?","Notepad",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,Options,Options[0]);
							if(choice == JOptionPane.YES_OPTION){
								Save();								
							}
						
						}
					}
				}				
			);
		props.setText("Times New Roman\nStandart\n25");
		frmNotepad.setIconImage(Toolkit.getDefaultToolkit().getImage(Padnote.class.getResource("/com/sun/javafx/scene/web/skin/Paste_16x16_JFX.png")));
		frmNotepad.getContentPane().setLayout(new BorderLayout(0, 0));
		
		
		JPanel panel = new JPanel();
		panel.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		frmNotepad.getContentPane().add(panel);
		panel.setLayout(new BorderLayout(0, 0));
		
		textArea = new JTextArea();
		textArea.setBounds(10, 11, 394, 217);
		textArea.setFont(new Font("Arial",Font.PLAIN,25));
		JScrollPane scroll = new JScrollPane(textArea);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel.add(scroll);
		//Writing panel set up END
		
		JMenuBar menuBar = new JMenuBar();
		frmNotepad.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		newFile = new JMenuItem("New");
		newFile.setIcon(new ImageIcon(Padnote.class.getResource("/javax/swing/plaf/metal/icons/ocean/file.gif")));
		mnFile.add(newFile);
		
		openFile = new JMenuItem("Open");
		openFile.setIcon(new ImageIcon(Padnote.class.getResource("/javax/swing/plaf/metal/icons/ocean/directory.gif")));
		mnFile.add(openFile);
		
		Save = new JMenuItem("Save");
		Save.setIcon(new ImageIcon(Padnote.class.getResource("/javax/swing/plaf/metal/icons/ocean/floppy.gif")));
		mnFile.add(Save);
		
		infoOption = new JMenuItem("Info");
		infoOption.setIcon(new ImageIcon(Padnote.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Italic.png")));
		mnFile.add(infoOption);
		
		openSettings = new JMenuItem("Settings");
		openSettings.setIcon(new ImageIcon(Padnote.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Right.png")));
		mnFile.add(openSettings);
		
		exitOption = new JMenuItem("Exit");
		exitOption.setIcon(new ImageIcon(Padnote.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Outdent.png")));
		mnFile.add(exitOption);
		
		mnFormat = new JMenu("Format");
		menuBar.add(mnFormat);
		
		fontText = new JMenuItem("Font & Text size options");
		fontText.setIcon(new ImageIcon(Padnote.class.getResource("/com/sun/javafx/scene/control/skin/modena/HTMLEditor-Bold.png")));
		mnFormat.add(fontText);
		
		
			
		//Handle events
		newFile.addActionListener(handler);
		openFile.addActionListener(handler);
		Save.addActionListener(handler);
		fontText.addActionListener(handler);
		openSettings.addActionListener(handler);
		infoOption.addActionListener(handler);
		exitOption.addActionListener(handler);
		
	}
	private void Save(){
		fileChooser = new JFileChooser();
		fileChooser.setFileFilter(filter);
		int returnval = fileChooser.showSaveDialog(null);
		if(returnval == JFileChooser.APPROVE_OPTION){	
			try {
				filePathWriter = new FileWriter("C:\\VladPad\\Paths.path",true);
				filePropsWriter = new FileWriter("C:\\VladPad\\SavedProperties\\"+fileChooser.getSelectedFile().getName()+"SAV.npad");
				writer = new FileWriter(fileChooser.getSelectedFile()+".txt",true);
				for(String line:textArea.getText().split("\\n")){
					writer.write(line);
					writer.write("\r\n");
				}
				for(String line:props.getText().split("\\n")){
					filePropsWriter.write(line);
					filePropsWriter.write("\r\n");
				}
				filePathWriter.write(fileChooser.getName(fileChooser.getSelectedFile()));
				filePathWriter.write("\r\n");
				filePathWriter.write(fileChooser.getSelectedFile().getPath()+".txt");
				filePathWriter.write("\r\n");
				filePathWriter.close();
				filePropsWriter.close();
				writer.close();
			}catch (Exception e){}		
		}
	   
	}
	private class HandleEvents implements ActionListener, ListSelectionListener{

		@Override
		public void actionPerformed(ActionEvent ActionEv) {
			if(ActionEv.getSource() ==newFile){
				if(!textArea.getText().trim().equals("")){
					//Code to save prompt if textarea is not empty
					Object[] Options ={
							"Save",
							"Don't save"
					};
					int choice = JOptionPane.showOptionDialog(null,"Do you want to save before writing to a new file ?","Notepad",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,Options,Options[0]);
					if(choice == JOptionPane.YES_OPTION){
						Save();
						textArea.setText(null);
					}else if(choice ==JOptionPane.NO_OPTION){
						textArea.setText(null);
						textArea.setFont(new Font("Arial",Font.PLAIN,24));
					}
					
				}else{
					textArea.setFont(new Font("Arial",Font.PLAIN,24));
				}
				
			}
			if(ActionEv.getSource() == Save){
				Save();				
			}
			if(ActionEv.getSource() == openFile){
				fileChooser = new JFileChooser();
				fileChooser.setFileFilter(filter);
				int returnval = fileChooser.showOpenDialog(null);
				if(returnval ==JFileChooser.APPROVE_OPTION){
					File exist = new File("C:\\VladPad\\SavedProperties\\"+fileChooser.getName(fileChooser.getSelectedFile())+"SAV.npad");
					String font = "";
					String fontType = "";
					String toParseSize = "";
					int size = 0;
					int type = 0;
					if(exist.exists() && exist.isFile()){
						openFile("C:\\VladPad\\SavedProperties\\"+fileChooser.getName(fileChooser.getSelectedFile())+"SAV.npad");
						byte assignTo = 1;
						while(read.hasNextLine()){
							if(assignTo ==1)font = read.nextLine();
							if(assignTo ==2)fontType = read.nextLine();
							if(assignTo ==3)toParseSize = read.nextLine();			
							assignTo++;
						}
						if(fontType.equals("Standart"))type = Font.PLAIN;					
						if(fontType.equals("Bold"))type = Font.BOLD;	
						if(fontType.equals("Italic"))type = Font.ITALIC;
						if(fontType.equals("Bold & Italic"))type = Font.BOLD + Font.ITALIC;
						size = Integer.parseInt(toParseSize);
						closeFile();
					}
					openFile(fileChooser.getSelectedFile().getPath());
				    if(!font.equals("") && type !=0 && size !=0){
				    	textArea.setFont(new Font(font,type,size));
				    }
					readFile();
					closeFile();			
				}
			}
			if(ActionEv.getSource() == fontText){
                displayTextSizes = new JList(TextSizeDisplay);
				displayTextSizes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				displayTextSizes.setVisibleRowCount(4);
				displayTextSizes.addListSelectionListener(handler);
				;;
				displayFontTypes = new JList(FontTypeDisplay);
				displayFontTypes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				displayFontTypes.setVisibleRowCount(4);
				displayFontTypes.addListSelectionListener(handler);
				;;
				displayFonts = new JList(Fonts);
				displayFonts.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
				displayFonts.setVisibleRowCount(4);
				displayFonts.addListSelectionListener(handler);
				;;
				
				JScrollPane scrollSize = new JScrollPane(displayTextSizes);
				JScrollPane scrollTypes = new JScrollPane(displayFontTypes);
				JScrollPane scrollFonts = new JScrollPane(displayFonts);
				Object [] message = {
					"Set text size",
					scrollSize,
					"Select text style",
					scrollTypes,
					"Select font",
					scrollFonts
				};
				Object[] ops = {
					"Apply","Cancel"	
				};
				int op = JOptionPane.showOptionDialog(null,message,"Title",JOptionPane.OK_CANCEL_OPTION,JOptionPane.INFORMATION_MESSAGE,null,ops,ops[0]);
				if(op == JOptionPane.OK_OPTION){
					if(setNewFont != null){
						Font();
						textArea.setFont(setNewFont);
					}					
				}
	
			}
			if(ActionEv.getSource() == openSettings){
				
			}
			if(ActionEv.getSource()==infoOption){
				JOptionPane.showMessageDialog(null,"Regular notepad made by Vlad the greatest   ;-)\n NOTE: dont save two files with same names, errors might occur\nNew to write to a new file\nOpen to open a text file(All extensions supported)\nSave to save current text to a file","Notepad",JOptionPane.INFORMATION_MESSAGE);
				JOptionPane.showMessageDialog(null, "NEVER CLICK ON A FILE TO SAVE DURING SAVE DIALOGS, ONLY TYPE THE NAME AND HIT SAVE","A Genuine warning",JOptionPane.WARNING_MESSAGE);
			}
			if(ActionEv.getSource()==exitOption){
				if(!textArea.getText().trim().equals("")){
					Object[] Options ={
							"Save",
							"Don't save"
					};
					int choice = JOptionPane.showOptionDialog(null,"Do you want to save before exiting ?","Notepad",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE,null,Options,Options[0]);
					if(choice == JOptionPane.YES_OPTION){
						Save();
						
					}
					System.exit(0);
				}else{
					System.exit(0);
				}
			}
			
		}
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if(e.getSource() == displayTextSizes){	
				 	TextSizeChanged = true;
			}
			if(e.getSource() == displayFontTypes){
				    FontStyleChanged = true;
			}
			if(e.getSource() == displayFonts){
				    FontChanged = true;
			}
				//setNewFont = setTextProperties(Fonts[displayFonts.getSelectedIndex()],FontType[displayFontTypes.getSelectedIndex()],TextSize[displayTextSizes.getSelectedIndex()]);
			
		}
	
	}
	private Font setTextProperties(String fontName,int f,int Size){
		return new Font(fontName,f,Size);
	}
	private void Font(){
		if(TextSizeChanged && FontStyleChanged && FontChanged){
			setNewFont = setTextProperties(Fonts[displayFonts.getSelectedIndex()],FontType[displayFontTypes.getSelectedIndex()],TextSize[displayTextSizes.getSelectedIndex()]);
		    this.props.setText(getFontProperties(Fonts[displayFonts.getSelectedIndex()],FontTypeDisplay[displayFontTypes.getSelectedIndex()],TextSizeDisplay[displayTextSizes.getSelectedIndex()]));
		}else if(TextSizeChanged && FontStyleChanged && !FontChanged){
			setNewFont = setTextProperties("Times New Roman",FontType[displayFontTypes.getSelectedIndex()],TextSize[displayTextSizes.getSelectedIndex()]);
			this.props.setText(getFontProperties("Times New Roman",FontTypeDisplay[displayFontTypes.getSelectedIndex()],TextSizeDisplay[displayTextSizes.getSelectedIndex()]));
		}else if(TextSizeChanged && FontChanged && !FontStyleChanged){
			setNewFont = setTextProperties(Fonts[displayFonts.getSelectedIndex()],Font.PLAIN,TextSize[displayTextSizes.getSelectedIndex()]);
			this.props.setText(getFontProperties(Fonts[displayFonts.getSelectedIndex()],"Standart",TextSizeDisplay[displayTextSizes.getSelectedIndex()]));
		}else if(FontStyleChanged && FontChanged && !TextSizeChanged){
			setNewFont = setTextProperties(Fonts[displayFonts.getSelectedIndex()],FontType[displayFontTypes.getSelectedIndex()],25);
			this.props.setText(getFontProperties(Fonts[displayFonts.getSelectedIndex()],FontTypeDisplay[displayFontTypes.getSelectedIndex()],"25"));
		}else if(FontStyleChanged && !FontChanged && !TextSizeChanged){
			setNewFont = setTextProperties("Times New Roman",FontType[displayFontTypes.getSelectedIndex()],25);
			this.props.setText(getFontProperties("Times New Roman",FontTypeDisplay[displayFontTypes.getSelectedIndex()],"25"));
		}else if(FontChanged && !TextSizeChanged && !FontStyleChanged){
			setNewFont = setTextProperties(Fonts[displayFonts.getSelectedIndex()],Font.PLAIN,25);
			this.props.setText(getFontProperties(Fonts[displayFonts.getSelectedIndex()],"Standart","25"));
		}else if(TextSizeChanged && !FontStyleChanged && !FontChanged){
			setNewFont = setTextProperties("Times New Roman",Font.PLAIN,TextSize[displayTextSizes.getSelectedIndex()]);
			this.props.setText(getFontProperties("Times New Roman","Standart",TextSizeDisplay[displayTextSizes.getSelectedIndex()]));
		}
		TextSizeChanged = false;
		FontStyleChanged = false;
		FontChanged = false;
		
	}
	private void openFile(String filename){
		try{
			read = new Scanner(new File(filename));	
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, "Error");
			}
	}
	private void readFile(){
		boolean first = true;
		while(read.hasNextLine()){
			if(first){
				textArea.setText(read.nextLine());
				first = false;
			}else{
				textArea.setText(textArea.getText()+"\n"+read.nextLine());
			}
		}
	}
	private void closeFile(){
		read.close();
	}
	private String getFontProperties(String fontName, String fontType,String fontSize){	
		return String.format("%s\n%s\n%s",fontName,fontType,fontSize);
	}
	private void validateFiles(){
		openFile("C:\\VladPad\\Paths.path");
		ArrayList<String>fileNames = new ArrayList<String>();
		ArrayList<String>fileDirs = new ArrayList<String>();
		byte count = 1;
		while(read.hasNextLine()){
			if(count ==1){
				 fileNames.add(read.nextLine());
			}
			if(count ==2){
				fileDirs.add(read.nextLine());			
				count = 0;
			}
			count++;
		}
		for(int i =0;i<fileDirs.size();i++){
			File f = new File(fileDirs.get(i));
			if(!f.exists()){
				File op = new File("C:\\VladPad\\SavedProperties\\"+fileNames.get(i)+"SAV.npad");
				if(op.exists()){
					op.delete();
				}
			}
		}
		
	}
	
  

}
