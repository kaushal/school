import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SongLib extends JPanel implements ListSelectionListener {
	private JButton deleteSongButton, editSongButton, addSongButton, saveButton;
	private JList list;
	private DefaultListModel model;
	private JTextField name, artist, year, album;
	private boolean editing = false;
	
	public SongLib(){
		super(new BorderLayout());
		
		model = new DefaultListModel();
		
		
		list = new JList(model); 
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.addListSelectionListener(this);
		list.setVisibleRowCount(5);
		JScrollPane listScrollPane = new JScrollPane(list);
		
		
		addSongButton = new JButton("Add Song");
		addSongButton.setActionCommand("Add Song");
		addSongButton.addActionListener(new AddListener());
		
		
		deleteSongButton = new JButton("Delete Song");
		deleteSongButton.setActionCommand("Delete Song");
		deleteSongButton.addActionListener(new DeleteListener());
		
		editSongButton = new JButton("Edit Song");
		editSongButton.setActionCommand("Delete Song");		
		editSongButton.addActionListener(new EditListener());
		
		saveButton = new JButton("Save");
		saveButton.setActionCommand("Save");
		saveButton.addActionListener(new SaveListener());
		saveButton.setEnabled(false);
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.LINE_AXIS));
		buttonPane.add(addSongButton);
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
		buttonPane.add(editSongButton);
		buttonPane.add(deleteSongButton);
		buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(editSongButton);
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(saveButton);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
        
        JPanel textPane = new JPanel();
        textPane.setLayout(new BoxLayout(textPane, BoxLayout.LINE_AXIS));
        
        
        name = new JTextField(15);
        artist = new JTextField(15);
        year = new JTextField(15);
        album = new JTextField(15);
        
        JTextField[] fields = {name, artist, year, album};
        
        String[] labels = {"Name: ", "Artist: ", "Year: ", "Album: "};
        int numPairs = labels.length;
        

        //Create and populate the panel.
        JPanel p = new JPanel(new SpringLayout());
        for (int i = 0; i < numPairs; i++) {
            JLabel l = new JLabel(labels[i], JLabel.TRAILING);
            p.add(l);
            JTextField textField = new JTextField(10);
            fields[i].setEditable(false);
            l.setLabelFor(fields[i]);
            p.add(fields[i]);
        }

        //Lay out the panel.
        SpringUtilities.makeCompactGrid(p,
                                        numPairs, 2, //rows, cols
                                        6, 6,        //initX, initY
                                        6, 6);       //xPad, yPad
        
        
        
        populateList(model);
		add(listScrollPane, BorderLayout.CENTER);
		add(buttonPane, BorderLayout.PAGE_END);
		add(p, BorderLayout.EAST);
	}

	class EditListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			updateValues();
			editing = true;
			setFieldsEditable(true);
			saveButton.setEnabled(true);
		}
	}
	
	class DeleteListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			editing = false;
			int index = list.getSelectedIndex();
			if(index >= 0)
				model.remove(index);	
			writeListToFile();
		}
	}
	
	class AddListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			editing = false;
			clearFields();
			setFieldsEditable(true);
			saveButton.setEnabled(true);
			
		}
	}
	
	class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent e){
			if(!editing){
				saveNewSong();
			}
			else{
				int index = list.getSelectedIndex();
				Song song = (Song) model.get(index);
				song.name = name.getText();
				song.artist = name.getText();
				song.year = name.getText();
				song.album = album.getText();
			}
			saveButton.setEnabled(false);
			editing = false;
			writeListToFile();
			updateValues();
		}
	}
	
	
	public void setFieldsEditable(boolean choice){
		name.setEditable(choice);
		artist.setEditable(choice);
		year.setEditable(choice);
		album.setEditable(choice);
	}
	
	public void clearFields(){
		name.setText("");
		artist.setText("");
		year.setText("");
		album.setText("");
	}
	
	public void saveNewSong(){
		model.addElement(new Song(name.getText(), artist.getText(), year.getText(), album.getText()));
		setFieldsEditable(false);
	}
	
	public void writeListToFile(){
		File file = new File("songs.txt");
		file.delete();
		BufferedWriter bw;
		try {
			bw = new BufferedWriter(new FileWriter("songs.txt"));
			bw.write(Integer.toString(model.size()));
			bw.write("\n");
			for(int i = 0; i < model.size(); i++)
			{
				Song song = (Song) model.get(i);
				bw.write(song.name);
				bw.write("\n");
				bw.write(song.artist);
				bw.write("\n");
				bw.write(song.year);
				bw.write("\n");
				bw.write(song.album);
				bw.write("\n");
			}
			bw.close();
		} catch (IOException e) {
			System.out.println("Error Writing to file. ");
		}
	}
	
	public  DefaultListModel populateList(DefaultListModel model) {
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("songs.txt"));
			int lengthOfFile = getLength(br);
			if((lengthOfFile - 1) % 4 == 0 && lengthOfFile >= 4){
				br = new BufferedReader(new FileReader("songs.txt"));
				int songNum = Integer.parseInt(br.readLine());
				
				for(int i = 0; i < songNum; i++)
				{
					Song song = new Song(br.readLine(), br.readLine(), br.readLine(), br.readLine());
					model.add(i, song);
					if(i == 0){
						name.setText(song.name);
						artist.setText(song.artist);
						year.setText(song.year);
						album.setText(song.album);
					}
				}
				br.close();
			}
			else
				throw new IOException();
			
		}catch (IOException e){
			System.out.println("Error reading file");
		}
		return model;
	}
	private int getLength(BufferedReader br) throws IOException{
		int count = 0;
		while (br.readLine() != null)
			count++;
		return count;
	}
	
	public static void showGUI(){
		JFrame frame = new JFrame("Song Library");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JComponent opaquePane = new SongLib();
		opaquePane.setOpaque(true);
		frame.setContentPane(opaquePane);
		
		frame.pack();
		frame.setVisible(true);
		
	}
	
	public void updateValues(){
		int index = list.getSelectedIndex();
		if(index >= 0){
			Song song = (Song) model.get(index);
			name.setText(song.name);
			artist.setText(song.artist);
			year.setText(song.year);
			album.setText(song.album);
		}
		
	}

	public void valueChanged(ListSelectionEvent e) {
		int index = list.getSelectedIndex();
		if(index >= 0){
			Song song = (Song) model.get(index);
			name.setText(song.name);
			artist.setText(song.artist);
			year.setText(song.year);
			album.setText(song.album);
		}
		else{
			clearFields();
		}
	}
	
	public static void main(String[] args){
		
		javax.swing.SwingUtilities.invokeLater(new Runnable(){
			public void run() {
				showGUI();
				System.out.println("GUI Started.");
			}
		});
		
	}
}
