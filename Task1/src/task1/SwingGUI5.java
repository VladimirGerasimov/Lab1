package task1;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.MaskFormatter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

class SwingGUI5 extends JFrame implements ActionListener, TreeSelectionListener
{
	private SwingGUI5Model theAppModel;

	private JTree theTree;
	private JButton insertButton;
	private JButton deleteButton;
	private JButton findButton;
	private JButton infoSaveButton;
	private JPanel theEntry;
	private JButton changeLookFeelButton;

	private JTextField nameInput;
	private JTextField surnameInput;
	private JFormattedTextField birthInput;
	private JFormattedTextField numberInput;
	private JPanel info;
	private JLabel infoNameLabel;
	private JLabel infoSurnameLabel;
	private JLabel infoDateLabel;
	private JLabel infoCodeLabel;
	private JTextField infoNameInput;
	private JTextField infoSurnameInput;
	private JTextField infoDateInput;
	private JTextField infoNumberInput;
	private JLabel theImage;
	private UIManager.LookAndFeelInfo installedLF[];
	private JMenu openMenu;
	private JMenu saveMenu;
	private DefaultTreeModel myTree;
	private int current;
	
	protected Component buildGUI()
	{
		
		Container contentPane = this.getContentPane();
		//contentPane.setLayout (new FlowLayout());
		myTree = theAppModel.buildDefaultTreeStructure();
		theTree  = new JTree(myTree);
		//theTree.setEditable(true);
		theTree.addTreeSelectionListener(this);
		int mode = TreeSelectionModel.SINGLE_TREE_SELECTION;
		theTree.getSelectionModel().setSelectionMode(mode);
		
		theEntry = new JPanel();
		theEntry.setLayout(new GridLayout(1,2));
		info = new JPanel();
		info.setLayout(new GridLayout(12,1));
		
		infoNameLabel = new JLabel("Имя:",SwingConstants.LEFT);
		info.add(infoNameLabel);
		infoNameInput = new JTextField();
		info.add(infoNameInput);
		
		infoSurnameLabel = new JLabel("Фамилия:",SwingConstants.LEFT);
		info.add(infoSurnameLabel);
		infoSurnameInput = new JTextField();
		info.add(infoSurnameInput);

		infoDateLabel = new JLabel("Дата рождения:",SwingConstants.LEFT);
		info.add(infoDateLabel);
		MaskFormatter mf;
		try {
			mf = new MaskFormatter("##.##.####");
			mf.setPlaceholder("00-00-0000");
			mf.setPlaceholderCharacter('0');
			infoDateInput = new JFormattedTextField(mf);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		info.add(infoDateInput);
		
		infoCodeLabel = new JLabel("Личный код:",SwingConstants.LEFT);
		info.add(infoCodeLabel);
		MaskFormatter mf3;
		try {
			mf3 = new MaskFormatter("UU-####-####");
			mf3.setPlaceholder("GE-0000-0000");
			mf3.setPlaceholderCharacter('_');
			infoNumberInput = new JFormattedTextField(mf3);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		info.add(infoNumberInput);
		infoSaveButton = new JButton("Обновить информацию");
		infoSaveButton.addActionListener(this);
		
		info.add(infoSaveButton);
		theEntry.add(info);
		JPanel infoImage = new JPanel();
		theImage = new JLabel();
		final ImageIcon foto = new ImageIcon("images/default.png");
		theImage.setIcon(foto);
		theImage.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
            	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)theTree.getLastSelectedPathComponent();
            	if(selectedNode != null){
		            theImage.setIcon(new ImageIcon("images/hover.png"));
            	}
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
            	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)theTree.getLastSelectedPathComponent();
            	if(selectedNode != null){
	            	DictionaryEntry a = (DictionaryEntry)selectedNode.getUserObject();
	            	ImageIcon x = a.getPic();
	            	if(x != null){
	            		theImage.setIcon(x);
	            	} else {
	            		theImage.setIcon(foto);
	            	}
            	}
            }
            @Override
            public void mouseClicked(MouseEvent e) {
            	DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)theTree.getLastSelectedPathComponent();
            	if(selectedNode != null){
	    	        JFileChooser fc = new JFileChooser();
					fc.setFileFilter(new ImageTypeFilter());
				    if ( fc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION ) {
				    	File inputFile = fc.getSelectedFile();
				    	////then work with image
				    	ImageProcessor ip = new ImageProcessor(inputFile);
				    	ip.size(200);
				    	DictionaryEntry a = (DictionaryEntry)selectedNode.getUserObject();
		            	a.setImageIcon(ip);
				    }
            	}
            }
        });
		theImage.setPreferredSize(new Dimension(200,200));
		infoImage.add(theImage);
		theEntry.add(infoImage);
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(1,2));

		panel.add(new JScrollPane(theTree));
		panel.add(new JScrollPane(theEntry));
		contentPane.add(panel, "Center");
		
		JPanel panel2 = new JPanel();
		
		insertButton = new JButton("Добавить сотрудника");
		insertButton.addActionListener(this);
		
		deleteButton = new JButton("Удалить сотрудника");
		deleteButton.addActionListener(this);
		
		findButton = new JButton("Искать сотрудника");
		findButton.addActionListener(this);
		
		changeLookFeelButton = new JButton("Изменить вид окна");
		changeLookFeelButton.addActionListener(this);		

		panel2.add(insertButton);
		panel2.add(deleteButton);
		panel2.add(findButton);
		panel2.add(changeLookFeelButton);
		
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(3,1));
		
		panel3.add(panel2);
		JPanel fields = new JPanel();
		fields.setLayout(new GridLayout(2,4));
		nameInput = new JTextField();
		surnameInput = new JTextField();
		
		MaskFormatter mf1;
		try {
			mf1 = new MaskFormatter("##.##.####");
			mf1.setPlaceholder("00-00-0000");
			mf1.setPlaceholderCharacter('0');
			birthInput = new JFormattedTextField(mf1);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		MaskFormatter mf2;
		try {
			mf2 = new MaskFormatter("UU-####-####");
			mf2.setPlaceholder("GE-0000-0000");
			mf2.setPlaceholderCharacter('_');
			numberInput = new JFormattedTextField(mf2);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		JLabel nameLabel = new JLabel("Имя",SwingConstants.RIGHT);
		JLabel surnameLabel = new JLabel("Фамилия",SwingConstants.RIGHT);
		JLabel birthLabel = new JLabel("Дата рождения", SwingConstants.RIGHT);
		JLabel numberLabel = new JLabel("Номер пропуска", SwingConstants.RIGHT);

		fields.add(nameLabel);
		fields.add(nameInput);
		fields.add(surnameLabel);
		fields.add(surnameInput);
		fields.add(birthLabel);
		fields.add(birthInput);
		fields.add(numberLabel);
		fields.add(numberInput);
		
		panel3.add(fields);
		
		contentPane.add(panel3, "South");
			
		JMenuBar menuBar = new JMenuBar();
      	openMenu = new  JMenu("Открыть");
		saveMenu = new  JMenu("Сохранить");
		openMenu.addMenuListener(new MenuListener(){

			@Override
			public void menuCanceled(MenuEvent e) {
			}

			@Override
			public void menuDeselected(MenuEvent e) {
			}

			@Override
			public void menuSelected(MenuEvent e) {
				JFileChooser fc = new JFileChooser();     
				fc.setFileFilter(new TypeFilter());
			    int ret = fc.showDialog(null, "Открыть файл");
				if (ret == JFileChooser.APPROVE_OPTION) {
				    File file = fc.getSelectedFile();
				    String theFile = "";
				    Scanner in;
					try {
						in = new Scanner(file);
						while(in.hasNext()){
							theFile += in.nextLine();
						}
						in.close();
						ArrayList<String> treeNodes = new ArrayList<String>(); 
						String [] parts = theFile.split(", ");
						for(int i = 0; i < parts.length; i++){
							String [] p = parts[i].split("\\:");
							if(p[1].length() > 2){
								String objectsStr = p[1].substring(2, p[1].length()-2);
								String [] objects = objectsStr.split("\\}\\{");
								for(int k = 0; k < objects.length; k++){
									treeNodes.add(objects[k]);
								}
							}
						}
						DefaultMutableTreeNode root = (DefaultMutableTreeNode)myTree.getRoot();
			    		Enumeration en = root.children();///alphabet
			    		ArrayList<DefaultMutableTreeNode> nodes = new ArrayList<DefaultMutableTreeNode>();
			    		while(en.hasMoreElements()){
			    			DefaultMutableTreeNode dmtn = (DefaultMutableTreeNode)en.nextElement();
			    			Enumeration ee = dmtn.children();
			    			while(ee.hasMoreElements()){
			    				DefaultMutableTreeNode dmtn1 = (DefaultMutableTreeNode)ee.nextElement();
				    			Enumeration ek = dmtn1.children();
				    			while(ek.hasMoreElements()){
					    			nodes.add((DefaultMutableTreeNode)ek.nextElement());
				    				//theAppModel.deletePerson((DefaultMutableTreeNode)ek.nextElement());
								}
				    			nodes.add(dmtn1);
			    				//theAppModel.deletePerson(dmtn1);
			    			}
			    		}
			    		for(int u = 0; u < nodes.size(); u++){
			    			theAppModel.deletePerson(nodes.get(u));
			    		}
						for(int l = 0; l < treeNodes.size(); l++){
							TreePath path = theAppModel.insertPerson(treeNodes.get(l));
						}
						
					} catch (Exception e1) {
						e1.printStackTrace();
					}					
				}
			}
			
		});
		saveMenu.addMenuListener(new MenuListener(){
			@Override
			public void menuCanceled(MenuEvent arg0) {
			}

			@Override
			public void menuDeselected(MenuEvent arg0) {
			}

			@Override
			public void menuSelected(MenuEvent arg0) {
				JFileChooser fc = new JFileChooser();
				fc.setFileFilter(new TypeFilter());
			    if ( fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION ) {
			    	File outputFile = fc.getSelectedFile();
		    		if (outputFile.getName().indexOf('.') == -1)
		    	    {
		    	        outputFile = new File(outputFile.getPath() + ".gerdb");
		    	    }
			    	try ( FileWriter fw = new FileWriter(outputFile) ) {
			    		String a = "";

			    		DefaultMutableTreeNode root = (DefaultMutableTreeNode)myTree.getRoot();
			    		Enumeration en = root.children();
			    		while (en.hasMoreElements()){
			    			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
			    			DictionaryElem elem = (DictionaryElem) node.getUserObject();
			    			a += elem.toString()+":{";
			    			Enumeration e = node.children();
			    			a += buildFile(e);
			    			a += "}, ";
			    		}
			    		a = a.substring(0, a.length() - 2);
			    		fw.write(a);
			        } catch ( IOException e ) {
			            System.out.println("Всё погибло!");
			        }
			        }
				}			
		});
		
		
		menuBar.add(openMenu);
		menuBar.add(saveMenu);
		
		setJMenuBar(menuBar);
		
		
		return null;
	}
	
	private String buildFile(Enumeration e){
		String a = "";
		
		while (e.hasMoreElements()){
			DefaultMutableTreeNode n = (DefaultMutableTreeNode) e.nextElement();
			DictionaryElem el = (DictionaryElem) n.getUserObject();
			if(el.toString().indexOf(" ") > 0){
				a += "{";
				a += el.toString()+" ";
				if(el.getPicString() != null){
					a += el.toString();
				}
				a += "}";
	 		} else {
	 			a += buildFile(n.children());
	 			
	 		}
		}
		
		return a;
	}
	
	public SwingGUI5(SwingGUI5Model appModel)
	{
		theAppModel = appModel;
		
		setTitle("Сотрудники ООО \"Матрасы\" ");
		setSize(880,600);
		
		addWindowListener(new WindowAdapter() { 
			public void windowClosing(WindowEvent e)
				              { System.exit(0);}
		});
		
		this.buildGUI();
		
		installedLF =  UIManager.getInstalledLookAndFeels();
		current = 0;
		try
		{
			UIManager.setLookAndFeel(installedLF[current].getClassName());
		}
		catch (Exception ex)
		{
			System.out.println("Exception 1");
		}
	}
	
	public void actionPerformed (ActionEvent event)
	{
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode)theTree.getLastSelectedPathComponent();
		
		if(event.getSource().equals(changeLookFeelButton)){
			current++;
			if (current > installedLF.length - 1)
			{
				current = 0;
			}
			
			System.out.println("New Current Look&Feel:"+ current);
			System.out.println("New Current Look&Feel Class:"+ installedLF[current].getClassName());
			
			try {
					UIManager.setLookAndFeel(installedLF[current].getClassName());
				    SwingUtilities.updateComponentTreeUI(this);
			} catch(Exception ex){
			     	System.out.println("exception");
			}
		} else if(event.getSource().equals(infoSaveButton) ){
			if (selectedNode == null) {
				DialogWind a = new DialogWind("Внимание!", "Не выделен сотрудник из списка!", "Выделите сотрудника, затем обновите!");
				return;
			}
			String nameVal = infoNameInput.getText();
			String surnameVal = infoSurnameInput.getText();
			String dateVal = infoDateInput.getText();
			String numberVal = infoNumberInput.getText();
			//////////flags
			boolean noName = false;
			boolean noSurname = false;
			boolean noDate = false;
			/////////name check
			if (nameVal == null || nameVal.equals("")){
				noName = true;
				DialogWind a = new DialogWind("Внимание!", "Поле ввода имени не заполнено!", "Проверьте правильность ввода!");
				a.setVisible(true);
			}
			////////////surname check
			if (surnameVal == null || surnameVal.equals("")){
				noSurname = true;
				DialogWind a = new DialogWind("Внимание!", "Поле ввода фамилии не заполнено!", "Проверьте правильность ввода!");
				a.setVisible(true);
			}
			/////////////date check
			if(dateVal.equals("00.00.0000")){
				noDate = true;
			} else {
				String [] parts = dateVal.split("\\.");
				int d = Integer.valueOf(parts[0]);
				int m = Integer.valueOf(parts[1]);
				int y = Integer.valueOf(parts[2]);
				boolean wrongdate = false;
				if(y <= 1998 & y > 1930 & m > 0 & d > 0){
					if(m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12){
						if(d >= 32){
							wrongdate = true;
						}
					} else if(m == 2){
						if(y%4 == 0){
							if(d >= 30 ){
								wrongdate = true;
							}
						} else {
							if(d >= 29 ){
								wrongdate = true;
							}
						}
					} else {
						if(d >= 31){
							wrongdate = true;
						}
					}
				} else {
					wrongdate = true;
				}
				if(wrongdate){
					DialogWind a = new DialogWind("Внимание!", "Неверно введена дата рождения!", "Проверьте правильность ввода!");
					a.setVisible(true);
					dateVal = "00.00.0000";
					noDate = true;
					infoDateInput.setText(dateVal);
				}
			}
			
			if(!noName && !noSurname){
				if(noDate){
					dateVal = "";
				}
				DictionaryEntry de = (DictionaryEntry) selectedNode.getUserObject();
				
				if (selectedNode.getParent() != null ) theAppModel.deletePerson(selectedNode);	
				TreePath path;
				if(de.getPic() != null){
					path = theAppModel.insertPerson(nameVal+" "+surnameVal+" "+dateVal+" "+numberVal+" "+de.getPicString());
				} else {
					path = theAppModel.insertPerson(nameVal, surnameVal, dateVal, numberVal);
				}
				if (path != null){
					theTree.scrollPathToVisible(path);		
					theTree.setSelectionPath(path);
				}			
			}
			
		} else if (event.getSource().equals(deleteButton)){
			if (selectedNode == null) return;
			if (selectedNode.getParent() != null )
				theAppModel.deletePerson(selectedNode);
			return;
		} else {
			String nameVal = nameInput.getText();
			String surnameVal = surnameInput.getText();
			String dateVal = birthInput.getText();
			String numberVal = numberInput.getText();
			//////////flags
			boolean noName = false;
			boolean noSurname = false;
			boolean noDate = false;
			/////////name check
			if (nameVal == null || nameVal.equals("")){
				noName = true;
				DialogWind a = new DialogWind("Внимание!", "Поле ввода имени не заполнено!", "Проверьте правильность ввода!");
				a.setVisible(true);
			}
			////////////surname check
			if (surnameVal == null || surnameVal.equals("")){
				noSurname = true;
				DialogWind a = new DialogWind("Внимание!", "Поле ввода фамилии не заполнено!", "Проверьте правильность ввода!");
				a.setVisible(true);
			}
			/////////////date check
			if(dateVal.equals("00.00.0000")){
				noDate = true;
			} else {
				String [] parts = dateVal.split("\\.");
				int d = Integer.valueOf(parts[0]);
				int m = Integer.valueOf(parts[1]);
				int y = Integer.valueOf(parts[2]);
				boolean wrongdate = false;
				if(y <= 1998 & y > 1930 & m > 0 & d > 0){
					if(m == 1 || m == 3 || m == 5 || m == 7 || m == 8 || m == 10 || m == 12){
						if(d >= 32){
							wrongdate = true;
						}
					} else if(m == 2){
						if(y%4 == 0){
							if(d >= 30 ){
								wrongdate = true;
							}
						} else {
							if(d >= 29 ){
								wrongdate = true;
							}
						}
					} else {
						if(d >= 31){
							wrongdate = true;
						}
					}
				} else {
					wrongdate = true;
				}
				if(wrongdate){
					DialogWind a = new DialogWind("Внимание!", "Неверно введена дата рождения!", "Проверьте правильность ввода!");
					a.setVisible(true);
					dateVal = "00.00.0000";
					noDate = true;
					birthInput.setText(dateVal);
				}
			}
			
			if(event.getSource().equals(insertButton) ){
				if(!noName && !noSurname){
					if(noDate){
						dateVal = "";
					}
					TreePath path = theAppModel.insertPerson(nameVal, surnameVal, dateVal, numberVal);
					if (path != null){
						theTree.scrollPathToVisible(path);			
					}			
				}
			}	
			if 	(event.getSource().equals(findButton) ){
				if(!noName && !noSurname){
					if(noDate){
						dateVal = "";
					}
					TreePath path = theAppModel.findPerson(nameVal, surnameVal, dateVal, numberVal);
					if (path != null){
						theTree.scrollPathToVisible(path);	
						theTree.setSelectionPath(path);
					}
				}
			}	
		}
	}
		
	public void valueChanged (TreeSelectionEvent event){
		TreePath path = theTree.getSelectionPath();
		if (path == null) return;
		
		DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) path.getLastPathComponent();
		
		if(selectedNode != null){
			if(selectedNode.toString().indexOf(" ") > 0){
				DictionaryEntry a = (DictionaryEntry)selectedNode.getUserObject();
				infoNameInput.setText(a.getName());
				infoSurnameInput.setText(a.getSurname());
				infoDateInput.setText(a.getDate());
				infoNumberInput.setText(a.getNumber());
				if(a.getPic() != null){
					theImage.setIcon(a.getPic());
				} else {
					theImage.setIcon(new ImageIcon("images/default.png"));
				}
			} else {
				infoNameInput.setText("");
				infoSurnameInput.setText("");
				infoDateInput.setText("");
				infoNumberInput.setText("");
				theImage.setIcon(new ImageIcon("images/default.png"));
			}
		}
		
	}
}