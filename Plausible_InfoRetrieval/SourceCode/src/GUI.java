import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.*;

import java.awt.*;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import java.awt.Color;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.JScrollBar;

public class GUI extends JPanel{

	private JFrame frame;
	private JTextField textField;
	private JTable table;
	private JScrollBar scrollBar;
	private JScrollPane scrollPane_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1271, 754);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		textField = new JTextField();
		textField.setBounds(147, 87, 385, 20);
		frame.getContentPane().add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel = new JLabel("                                   Enter the Query");
		lblNewLabel.setFont(new Font("Times New Roman", Font.BOLD, 16));
		lblNewLabel.setBounds(147, 62, 385, 26);
		frame.getContentPane().add(lblNewLabel);
		
		JButton btnSearch = new JButton("SEARCH");
		btnSearch.setIcon(new ImageIcon("search1.png"));
		btnSearch.addActionListener(new ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				DefaultTableModel model = (DefaultTableModel) table.getModel();
				RetrievalFunctions callRetrievalFunction = new RetrievalFunctions();
				String firedQuery = textField.getText();
				callRetrievalFunction.SetQuery(firedQuery);
				ArrayList<String> listOfRetrievedDocuments = callRetrievalFunction.RetrieveDocumentsForQuery(firedQuery);
				
				TreeMap<String, Double> result = callRetrievalFunction.GetURLForDocID(listOfRetrievedDocuments);
				//result.sort
				int Rank = 0;
				String DOC_ID = "";
				String HeadLine = "";
				String URL = "";
				Double Score;
				int index = 0;
				if(textField.getText().length() > 0)
				{
				for(Map.Entry<String, Double> entry : result.entrySet()){
			      	
			    	 if(index <10){
			    		 
			    		//(DOCID~URL~TITLE, Score)
			    		 Rank = index+1;
			    		 DOC_ID = entry.getKey().split("~")[0];
			    		 URL = entry.getKey().split("~")[1];
			    		 HeadLine = entry.getKey().split("~")[2];
			    		 Score = entry.getValue();
			    		 model.insertRow(index,new Object[]{Rank, DOC_ID, HeadLine, URL, Score});
			      		index++;
			      	}
			    	 else{
			    		 break;
			    	 }
				}
				}
			}
			
		});
		btnSearch.setBounds(301, 130, 88, 87);
		frame.getContentPane().add(btnSearch);
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 239, 684, 466);
		frame.getContentPane().add(panel);
		
		table = new JTable();
		table.setForeground(Color.BLACK);
		table.setModel(new DefaultTableModel(
			new Object[][] {
			},
			new String[] {
				"Rank", "Document Name", "Title", "URL", "Score"
			}
		));
		table.setBorder(new LineBorder(new Color(0, 0, 0)));
		table.setFont(new Font("Tahoma", Font.PLAIN, 14));
		JScrollPane scrlPane = new JScrollPane(table);
		table.setBounds(33, 256, 656, 202);
		panel.add(scrlPane);
		
		JLabel lblNewLabel_1 = new JLabel(new ImageIcon("logo.jpg"));
		//add(lblNewLabel_1);
		lblNewLabel_1.setBounds(724, 70, 402, 385);
		frame.getContentPane().add(lblNewLabel_1);
		
		table.addMouseListener(new MouseAdapter() {
	        @Override
	        public void mouseClicked(MouseEvent e) {
	            int row = table.rowAtPoint(new Point(e.getX(), e.getY()));
	            int col = table.columnAtPoint(new Point(e.getX(), e.getY()));
	            //System.out.println(row + " " + col);
	            String temp = (String) table.getModel().getValueAt(row, col);
	            URL url;
				try {
					url = new URL(temp);
					//System.out.println(url + " was clicked");
					openWebpage(url.toURI());
				} catch (MalformedURLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (URISyntaxException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
	            
	           
	         // DO here what you want to do with your url
	        }

	        @Override
	        public void mouseEntered(MouseEvent e) {
	            int col = table.columnAtPoint(new Point(e.getX(), e.getY()));
	            if (col == 0) {
	                table.setCursor(new Cursor(Cursor.HAND_CURSOR));
	            }
	        }
	        public void openWebpage(URI uri) {
	            Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
	            if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
	                try {
	                    desktop.browse(uri);
	                } catch (Exception e) {
	                    e.printStackTrace();
	                }
	            }
	        }

	        @Override
	        public void mouseExited(MouseEvent e) {
	            int col = table.columnAtPoint(new Point(e.getX(), e.getY()));
	            if (col != 0) {
	                table.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	            }
	        }
	    });
		
		/*Browser browser = BrowserFactory.create(BrowserType.);
        
        
        JPanel panel_1 = new JPanel();
        panel_1.setBounds(694, 11, 551, 694);
        frame.getContentPane().add(panel_1);
        
       panel_1.add((Component) browser, BorderLayout.CENTER);
        //browser.navigate("http://www.google.com");*/
	}
	public JTable getTable() {
		return table;
	}
}
