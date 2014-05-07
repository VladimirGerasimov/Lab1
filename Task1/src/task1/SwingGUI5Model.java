package task1;

import java.util.ArrayList;
import java.util.Enumeration;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

class SwingGUI5Model
{

    private DefaultTreeModel theModel;
    private static String alphabet= new String("ÀÁÂÃÄÅ¨ÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÝÞß");
    private static String alphabet1= new String("ÀÁÂÃÄÅ¨ÆÇÈÉÊËÌÍÎÏÐÑÒÓÔÕÖ×ØÙÚÝÜÛÞß");
	private DefaultMutableTreeNode theRoot;
	
	public SwingGUI5Model()
	{
		
	}
	
	public TreePath findPerson (String name, String surname, String date, String number)
	{
		TreePath path;
		DictionaryAnchor anchor = new DictionaryAnchor();
		
		anchor.topic = null;
		anchor.entry = null;
		
		DictionaryEntry new_entry = new DictionaryEntry(name, surname, date, number);
		
		if(this.findEntry(new_entry, anchor)) {
			TreeNode[] nodes = theModel.getPathToRoot(anchor.entry);
			path = new TreePath(nodes);
						
		} else {
			path = null;
		}

		return path;
	}
	
	public void deletePerson (DefaultMutableTreeNode selectedNode){
		if (selectedNode != theRoot){
			DictionaryElem elem = (DictionaryElem) selectedNode.getUserObject();
			if ("Entry".equals(elem.getType())){
				DefaultMutableTreeNode parent = (DefaultMutableTreeNode) selectedNode.getParent();
				DictionaryTopic obj = (DictionaryTopic) parent.getUserObject();
				if(obj.getValue()[0].length() > 1){
					DefaultMutableTreeNode parentForTwo = (DefaultMutableTreeNode) selectedNode.getParent().getParent();
					int elems = 0;
					elems = parentForTwo.getChildAt(0).getChildCount()+parentForTwo.getChildAt(1).getChildCount();
					if(elems < 11){
						DefaultMutableTreeNode first = (DefaultMutableTreeNode) parentForTwo.getChildAt(0);
						DefaultMutableTreeNode second = (DefaultMutableTreeNode) parentForTwo.getChildAt(1);
						
						ArrayList<DefaultMutableTreeNode>a = new ArrayList<DefaultMutableTreeNode>();
						Enumeration en = first.children();
						while(en.hasMoreElements()){
							a.add((DefaultMutableTreeNode)en.nextElement());
						}
						Enumeration en1 = second.children();
						while(en1.hasMoreElements()){
							a.add((DefaultMutableTreeNode)en1.nextElement());						}
						
						for(int i = 0; i < a.size(); i++){
							theModel.removeNodeFromParent(a.get(i));
							String str = ((DictionaryEntry)a.get(i).getUserObject()).getName();
							theModel.insertNodeInto(a.get(i), parentForTwo, getPosition(parentForTwo, str));
						}
						theModel.removeNodeFromParent(first);
						theModel.removeNodeFromParent(second);
						
					}
				}
				theModel.removeNodeFromParent(selectedNode);
				
			} else if("Topic".equals(elem.getType()) && elem.toString().length() > 1){
					theModel.removeNodeFromParent(selectedNode);
			}
		}
	}
	
	public int getPosition(DefaultMutableTreeNode topic, String put){
		int i = 0;
		if(topic != null){
			Enumeration en = topic.children();
			while(en.hasMoreElements()){
				String obj = en.nextElement().toString();
				if(put.compareToIgnoreCase(obj) < 0){
					break;
				} else {
					i++;
				}
			}
		}
		return i;
	}
	
	public TreePath insertPerson (String name, String surname, String date, String number){
		TreePath path;
		DictionaryAnchor anchor = new DictionaryAnchor();
		
		anchor.topic = null;
		anchor.entry = null;
		
		DictionaryEntry new_entry = new DictionaryEntry(name, surname, date, number);
		
		if(this.findEntry(new_entry, anchor)){
			TreeNode[] nodes = theModel.getPathToRoot(anchor.entry);
			path = new TreePath(nodes);			
		} else {
			if (anchor.topic!= null){
				DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(new_entry);
				new_node.setAllowsChildren(false);
				if(anchor.topic.getDepth() == 2){
					String sl = name.substring(1,2).toUpperCase();
					DefaultMutableTreeNode t;
					if(alphabet1.indexOf(sl) > 13){
						t = (DefaultMutableTreeNode) anchor.topic.getChildAt(1);				
					} else {
						t = (DefaultMutableTreeNode) anchor.topic.getChildAt(0);				
					}
					theModel.insertNodeInto(new_node, t, getPosition(t, name+" "+surname));		
				} else {
					if(doesNeedSplit(anchor)){
						String fl = name.substring(0,1).toUpperCase();
						String sl = name.substring(1,2).toUpperCase();
						
						DictionaryElem nodeElem = new DictionaryTopic(fl+"À-"+fl+"Ì");
						DefaultMutableTreeNode topic = new DefaultMutableTreeNode(nodeElem);
						topic.setAllowsChildren(true);
						theModel.insertNodeInto(topic, anchor.topic, 0);
						
						nodeElem = new DictionaryTopic(fl+"Í-"+fl+"ß");
						DefaultMutableTreeNode topic1 = new DefaultMutableTreeNode(nodeElem);
						topic1.setAllowsChildren(true);
						theModel.insertNodeInto(topic1, anchor.topic, 1);

						Enumeration en = anchor.topic.children();
						DefaultMutableTreeNode [] nodes = new DefaultMutableTreeNode[10];
						int i = 0;
						while(en.hasMoreElements()){
							DefaultMutableTreeNode x = (DefaultMutableTreeNode)en.nextElement();
							if(!x.getAllowsChildren()){		
								nodes[i] = x;
								i++;
							}
						}
						for(int k = 0; k < 10; k++){
							DictionaryEntry a = (DictionaryEntry) nodes[k].getUserObject();
							theModel.removeNodeFromParent(nodes[k]);
							if(alphabet1.indexOf(a.getName().substring(1, 2)) > 13){
								theModel.insertNodeInto(nodes[k], topic1, getPosition(topic1, name+" "+surname));
							} else {
								theModel.insertNodeInto(nodes[k], topic, getPosition(topic, name+" "+surname));
							}
							
						}
						if(alphabet1.indexOf(sl) > 13){
							theModel.insertNodeInto(new_node, topic1, getPosition(topic1, name+" "+surname));
						} else {
							theModel.insertNodeInto(new_node, topic, getPosition(topic, name+" "+surname));
						}
						
					} else {
						theModel.insertNodeInto(new_node, anchor.topic, getPosition(anchor.topic, name+" "+surname));
					}
				}
				
				TreeNode[] nodes = theModel.getPathToRoot(new_node);
				path = new TreePath(nodes);	
			} else {
			 path = null;
			}
		}
		
		return path;
	}	
	public TreePath insertPerson (String data){
		TreePath path;
		DictionaryAnchor anchor = new DictionaryAnchor();
		
		anchor.topic = null;
		anchor.entry = null;
		
		DictionaryEntry new_entry = new DictionaryEntry(data);
		
		if(this.findEntry(new_entry, anchor)){
			TreeNode[] nodes = theModel.getPathToRoot(anchor.entry);
			path = new TreePath(nodes);			
		} else {
			if (anchor.topic!= null){
				DefaultMutableTreeNode new_node = new DefaultMutableTreeNode(new_entry);
				new_node.setAllowsChildren(false);
				if(anchor.topic.getDepth() == 2){
					String sl = data.substring(1,2).toUpperCase();
					DefaultMutableTreeNode t;
					if(alphabet1.indexOf(sl) > 13){
						t = (DefaultMutableTreeNode) anchor.topic.getChildAt(1);				
					} else {
						t = (DefaultMutableTreeNode) anchor.topic.getChildAt(0);				
					}
					theModel.insertNodeInto(new_node, t, getPosition(t, data));		
				} else {
					if(doesNeedSplit(anchor)){
						String fl = data.substring(0,1).toUpperCase();
						String sl = data.substring(1,2).toUpperCase();
						
						DictionaryElem nodeElem = new DictionaryTopic(fl+"À-"+fl+"Ì");
						DefaultMutableTreeNode topic = new DefaultMutableTreeNode(nodeElem);
						topic.setAllowsChildren(true);
						theModel.insertNodeInto(topic, anchor.topic, 0);
						
						nodeElem = new DictionaryTopic(fl+"Í-"+fl+"ß");
						DefaultMutableTreeNode topic1 = new DefaultMutableTreeNode(nodeElem);
						topic1.setAllowsChildren(true);
						theModel.insertNodeInto(topic1, anchor.topic, 1);

						Enumeration en = anchor.topic.children();
						DefaultMutableTreeNode [] nodes = new DefaultMutableTreeNode[10];
						int i = 0;
						while(en.hasMoreElements()){
							DefaultMutableTreeNode x = (DefaultMutableTreeNode)en.nextElement();
							if(!x.getAllowsChildren()){		
								nodes[i] = x;
								i++;
							}
						}
						for(int k = 0; k < 10; k++){
							DictionaryEntry a = (DictionaryEntry) nodes[k].getUserObject();
							theModel.removeNodeFromParent(nodes[k]);
							if(alphabet1.indexOf(a.getName().substring(1, 2)) > 13){
								theModel.insertNodeInto(nodes[k], topic1, getPosition(topic1, data));
							} else {
								theModel.insertNodeInto(nodes[k], topic, getPosition(topic, data));
							}
							
						}
						if(alphabet1.indexOf(sl) > 13){
							theModel.insertNodeInto(new_node, topic1, getPosition(topic1, data));
						} else {
							theModel.insertNodeInto(new_node, topic, getPosition(topic, data));
						}
						
					} else {
						theModel.insertNodeInto(new_node, anchor.topic, getPosition(anchor.topic, data));
					}
				}
				
				TreeNode[] nodes = theModel.getPathToRoot(new_node);
				path = new TreePath(nodes);	
			} else {
			 path = null;
			}
		}
		
		return path;
	}
	protected boolean findEntry(DictionaryEntry new_entry,  DictionaryAnchor anchor){
		String firstLetter = new_entry.getValue()[0].substring(0,1);
		boolean result = false;
		
		if (anchor == null) return false;
		anchor.topic = null;
		
		Enumeration en = theRoot.children();
						
		while (en.hasMoreElements()){
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
			
			DictionaryElem elem = (DictionaryElem) node.getUserObject();
			if ("Topic".equals(elem.getType())){
				if (firstLetter.equalsIgnoreCase(elem.getValue()[0])){
					anchor.topic = node;
					break;
				}
			} else {
				break;
			}
		}
		
		if (anchor.topic != null){
			en = anchor.topic.children();
			anchor.entry = null;
			
			while (en.hasMoreElements()){
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();
				DictionaryElem elem = (DictionaryElem) node.getUserObject();
				if ("Entry".equals(elem.getType())){
						if (new_entry.getValue()[0].equalsIgnoreCase(elem.getValue()[0]) & new_entry.getValue()[1].equalsIgnoreCase(elem.getValue()[1])){
							boolean dateOk = false;
							if(!new_entry.getValue()[2].equals("")){
								if(new_entry.getValue()[2].equals(elem.getValue()[2])){
										dateOk = true;
								}
							} else {
								dateOk = true;
							}
							if(dateOk){
								if(!new_entry.getValue()[3].equals("GE-0000-0000")){
									if(new_entry.getValue()[3].equals(elem.getValue()[3])){
										anchor.entry = node;
										result = true;
										break;
									}	
								} else {
									if(new_entry.getValue()[3].equals(elem.getValue()[3])){
										anchor.entry = node;
										result = true;
										break;
									} else {
										anchor.entry = node;
										result = true;
									}
								}
							}
						}
				}
			}		
		}
		return result;
	}
		
	public void removeNodeFromParent(MutableTreeNode selectedNode){
	    theModel.removeNodeFromParent(selectedNode);	
	}
	
	public TreeNode[] getPathToRoot(MutableTreeNode newNode){
		return theModel.getPathToRoot(newNode);
	}
	
	public boolean doesNeedSplit(DictionaryAnchor anchor){
		boolean need = false;
		
		if(anchor.topic != null){
			int children = anchor.topic.getChildCount() + 1; 
			if(children > 10){
				need = true;
			}
		}
		
		return need;
	}
	
	public DefaultTreeModel buildDefaultTreeStructure(){  	
	 theRoot = new DefaultMutableTreeNode("Ñïèñîê");
	 theRoot.setAllowsChildren(true);

		for (int i = 0; i < alphabet.length(); i++){
			DictionaryElem nodeElem = new DictionaryTopic(alphabet.substring(i, i+1));
			DefaultMutableTreeNode topic = new DefaultMutableTreeNode(nodeElem);
			topic.setAllowsChildren(true);
		
			theRoot.add(topic);
		}
		theModel = new DefaultTreeModel(theRoot);
		theModel.setAsksAllowsChildren(true);
			
		return theModel;
	}

}