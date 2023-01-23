package edu.ncsu.csc216.requirements_manager.view.ui;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;

import edu.ncsu.csc216.requirements_manager.model.command.Command;
import edu.ncsu.csc216.requirements_manager.model.manager.RequirementsManager;
import edu.ncsu.csc216.requirements_manager.model.user_story.UserStory;

/**
 * Container for the RequirementsManager that has the menu options for new project 
 * files, loading existing files, saving files and quitting.
 * Depending on user actions, other JPanels are loaded for the
 * different ways users interact with the UI.
 * 
 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
 */
public class RequirementsManagerGUI extends JFrame implements ActionListener {
	
	/** ID number used for object serialization. */
	private static final long serialVersionUID = 1L;
	/** Title for top of GUI. */
	private static final String APP_TITLE = "Requirements Manager";
	/** Text for the File Menu. */
	private static final String FILE_MENU_TITLE = "File";
	/** Text for the New menu item. */
	private static final String NEW_TITLE = "New";
	/** Text for the Load menu item. */
	private static final String LOAD_TITLE = "Load";
	/** Text for the Save menu item. */
	private static final String SAVE_TITLE = "Save";
	/** Text for the Quit menu item. */
	private static final String QUIT_TITLE = "Quit";
	/** Menu bar for the GUI that contains Menus. */
	private JMenuBar menuBar;
	/** Menu for the GUI. */
	private JMenu menu;
	/** Menu item for creating a new file containing a Project and its UserStories. */
	private JMenuItem itemNewProject;
	/** Menu item for loading a file containing a Project and its UserStories. */
	private JMenuItem itemLoadProject;
	/** Menu item for saving the Project and its UserStories. */
	private JMenuItem itemSaveProject;
	/** Menu item for quitting the program. */
	private JMenuItem itemQuit;
	/** Panel that will contain different views for the application. */
	private JPanel panel;
	/** Constant to identify ProjectPanel for CardLayout. */
	private static final String PROJECT_PANEL = "ProjectPanel";
	/** Constant to identify SubmittedPanel for CardLayout. */
	private static final String SUBMITTED_PANEL = "SubmittedPanel";
	/** Constant to identify BacklogPanel for CardLayout. */
	private static final String BACKLOG_PANEL = "BacklogPanel";
	/** Constant to identify WorkingPanel for CardLayout. */
	private static final String WORKING_PANEL = "WorkingPanel";
	/** Constant to identify VerifyingPanel for CardLayout. */
	private static final String VERIFYING_PANEL = "VerifyingPanel";
	/** Constant to identify CompletedPanel for CardLayout. */
	private static final String COMPLETED_PANEL = "CompletedPanel";
	/** Constant to identify RejectedPanel for CardLayout. */
	private static final String REJECTED_PANEL = "RejectedPanel";
	/** Constant to identify CreateUserStoryPanel for CardLayout. */
	private static final String CREATE_USER_STORY_PANEL = "CreateUsers";
	/** Project panel - we only need one instance, so it's final. */
	private final ProjectPanel pnlUserStoryList = new ProjectPanel();
	/** Submitted panel - we only need one instance, so it's final. */
	private final SubmittedPanel pnlSubmitted = new SubmittedPanel();
	/** Backlog panel - we only need one instance, so it's final. */
	private final BacklogPanel pnlBacklog = new BacklogPanel();
	/** Working panel - we only need one instance, so it's final. */
	private final WorkingPanel pnlWorking = new WorkingPanel();
	/** Verifying panel - we only need one instance, so it's final. */
	private final VerifyingPanel pnlVerifying = new VerifyingPanel();
	/** Completed panel - we only need one instance, so it's final. */
	private final CompletedPanel pnlCompleted = new CompletedPanel();
	/** Rejected panel - we only need one instance, so it's final. */
	private final RejectedPanel pnlRejected = new RejectedPanel();
	/** Add UserStory panel - we only need one instance, so it's final. */
	private final AddUserStoryPanel pnlAddUserStory = new AddUserStoryPanel();
	/** Reference to CardLayout for panel.  Stacks all of the panels. */
	private CardLayout cardLayout;
	
	
	/**
	 * Constructs a RequirementsManagerGUI object that will contain a JMenuBar and a
	 * JPanel that will hold different possible views of the data in
	 * the RequirementsManager.
	 */
	public RequirementsManagerGUI() {
		super();
		
		//Set up general GUI info
		setSize(500, 700);
		setLocation(50, 50);
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setUpMenuBar();
		
		//Create JPanel that will hold rest of GUI information.
		//The JPanel utilizes a CardLayout, which stack several different
		//JPanels.  User actions lead to switching which "Card" is visible.
		panel = new JPanel();
		cardLayout = new CardLayout();
		panel.setLayout(cardLayout);
		panel.add(pnlUserStoryList, PROJECT_PANEL);
		panel.add(pnlSubmitted, SUBMITTED_PANEL);
		panel.add(pnlBacklog, BACKLOG_PANEL);
		panel.add(pnlWorking, WORKING_PANEL);
		panel.add(pnlVerifying, VERIFYING_PANEL);
		panel.add(pnlCompleted, COMPLETED_PANEL);
		panel.add(pnlRejected, REJECTED_PANEL);
		panel.add(pnlAddUserStory, CREATE_USER_STORY_PANEL);
		cardLayout.show(panel, PROJECT_PANEL);
		
		//Add panel to the container
		Container c = getContentPane();
		c.add(panel, BorderLayout.CENTER);
		
		//Add window listener to save when closing
		addWindowListener(new WindowAdapter() {

			/**
			 * Ask the user to save the project's file when closing GUI.
			 * @param e WindowEvent leading to GUI closing
			 */
			@Override
			public void windowClosing(WindowEvent e) {
				RequirementsManager model = RequirementsManager.getInstance();
				try {
					model.saveCurrentProjectToFile(getFileName(false));
				} catch (IllegalArgumentException exp) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, exp.getMessage());
				} catch (IllegalStateException exp) {
					//Don't do anything - user canceled (or error)
				}
			}
			
		});
		
		//Set the GUI visible
		setVisible(true);
	}
	
	/**
	 * Makes the GUI Menu bar that contains options for loading a project
	 * file containing user stories or for quitting the application.
	 */
	private void setUpMenuBar() {
		//Construct Menu items
		menuBar = new JMenuBar();
		menu = new JMenu(FILE_MENU_TITLE);
		itemNewProject = new JMenuItem(NEW_TITLE);
		itemLoadProject = new JMenuItem(LOAD_TITLE);
		itemSaveProject = new JMenuItem(SAVE_TITLE);
		itemQuit = new JMenuItem(QUIT_TITLE);
		itemNewProject.addActionListener(this);
		itemLoadProject.addActionListener(this);
		itemSaveProject.addActionListener(this);
		itemQuit.addActionListener(this);
		
		//Start with save button disabled
		itemSaveProject.setEnabled(false);
		
		//Build Menu and add to GUI
		menu.add(itemNewProject);
		menu.add(itemLoadProject);
		menu.add(itemSaveProject);
		menu.add(itemQuit);
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
	}
	
	/**
	 * Performs an action based on the given ActionEvent.
	 * @param e user event that triggers an action.
	 */
	public void actionPerformed(ActionEvent e) {
		//Use RequirementsManager's singleton to create/get the sole instance.
		RequirementsManager model = RequirementsManager.getInstance();
		if (e.getSource() == itemNewProject) {
			//Create a new project
			try {
				String projectName = (String) JOptionPane.showInputDialog(this, "Project Name?", "Create New Project", JOptionPane.QUESTION_MESSAGE);
				model.createNewProject(projectName);
				itemSaveProject.setEnabled(true);
				pnlUserStoryList.updateProject();
				cardLayout.show(panel, PROJECT_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			}
		} else if (e.getSource() == itemLoadProject) {
			//Load an existing project list
			try {
				model.loadProjectsFromFile(getFileName(true));
				itemSaveProject.setEnabled(true);
				pnlUserStoryList.updateProject();
				cardLayout.show(panel, PROJECT_PANEL);
				validate();
				repaint();
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemSaveProject) {
			//Save current project and user stories
			try {
				model.saveCurrentProjectToFile(getFileName(false));
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		} else if (e.getSource() == itemQuit) {
			//Quit the program
			try {
				model.saveCurrentProjectToFile(getFileName(false));
				System.exit(0);  //Ignore SpotBugs warning here - this is the only place to quit the program!
			} catch (IllegalArgumentException exp) {
				JOptionPane.showMessageDialog(this, exp.getMessage());
			} catch (IllegalStateException exp) {
				//Don't do anything - user canceled (or error)
			}
		}
	}
	
	/**
	 * Returns a file name generated through interactions with a JFileChooser
	 * object.
	 * @param load true if loading a file, false if saving
	 * @return the file name selected through JFileChooser
	 * @throws IllegalStateException if no file name provided
	 */
	private String getFileName(boolean load) {
		JFileChooser fc = new JFileChooser("./");  //Open JFileChoose to current working directory
		int returnVal = Integer.MIN_VALUE;
		if (load) {
			returnVal = fc.showOpenDialog(this);
		} else {
			returnVal = fc.showSaveDialog(this);
		}
		if (returnVal != JFileChooser.APPROVE_OPTION) {
			//Error or user canceled, either way no file name.
			throw new IllegalStateException();
		}
		File gameFile = fc.getSelectedFile();
		return gameFile.getAbsolutePath();
	}

	/**
	 * Starts the GUI for the RequirementsManager application.
	 * @param args command line arguments
	 */
	public static void main(String [] args) {
		new RequirementsManagerGUI();
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows the project including the list of user stories.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class ProjectPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Label for selecting current project */
		private JLabel lblCurrentProject;
		/** Combo box for Project list */
		private JComboBox<String> comboProjectList;
		/** Button to update to the select project */
		private JButton btnUpdateProject;
		
		/** Button for creating a new User Story */
		private JButton btnAdd;
		/** Button for deleting the selected user story in the list */
		private JButton btnDelete;
		/** Button for editing the selected user story in the list */
		private JButton btnEdit;
		/** JTable for displaying the list of userStories */
		private JTable tableUserStories;
		/** TableModel for UserStories */
		private UserStoryTableModel tableModel;
		
		/**
		 * Creates the user story list.
		 */
		public ProjectPanel() {
			super(new BorderLayout());
			
			//Set up JPanel for projects
			lblCurrentProject = new JLabel("Current Project");
			comboProjectList = new JComboBox<String>();
			btnUpdateProject = new JButton("Update Project");
			btnUpdateProject.addActionListener(this);
						
			//Set up the JPanel that will hold action buttons		
			btnAdd = new JButton("Add User Story");
			btnAdd.addActionListener(this);
			btnDelete = new JButton("Delete User Story");
			btnDelete.addActionListener(this);
			btnEdit = new JButton("Edit User Story");
			btnEdit.addActionListener(this);
			
			JPanel pnlActions = new JPanel();
			pnlActions.setLayout(new GridLayout(2, 3));
			pnlActions.add(lblCurrentProject);
			pnlActions.add(comboProjectList);
			pnlActions.add(btnUpdateProject);
			pnlActions.add(btnAdd);
			pnlActions.add(btnDelete);
			pnlActions.add(btnEdit);
						
			//Set up table
			tableModel = new UserStoryTableModel();
			tableUserStories = new JTable(tableModel);
			tableUserStories.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			tableUserStories.setPreferredScrollableViewportSize(new Dimension(500, 500));
			tableUserStories.setFillsViewportHeight(true);
			
			JScrollPane listScrollPane = new JScrollPane(tableUserStories);
			
			add(pnlActions, BorderLayout.NORTH);
			add(listScrollPane, BorderLayout.CENTER);
			
			updateProject();
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == btnUpdateProject) {
				int idx = comboProjectList.getSelectedIndex();
				
				if (idx == -1) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "No project selected");
				} else {				
					String projectName = comboProjectList.getItemAt(idx);
					RequirementsManager.getInstance().loadProject(projectName);
				}
				updateProject();
			} else if (e.getSource() == btnAdd) {
				//If the add button is clicked switch to the createUserStoryPanel
				cardLayout.show(panel,  CREATE_USER_STORY_PANEL);
			} else if (e.getSource() == btnDelete) {
				//If the delete button is clicked, delete the user story
				int row = tableUserStories.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "No user story selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						RequirementsManager.getInstance().deleteUserStoryById(id);
					} catch (NumberFormatException nfe ) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid id");
					}
				}
				updateProject();
			} else if (e.getSource() == btnEdit) {
				//If the edit button is clicked, switch panel based on state
				int row = tableUserStories.getSelectedRow();
				if (row == -1) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "No user story selected");
				} else {
					try {
						int id = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
						String stateName = RequirementsManager.getInstance().getUserStoryById(id).getState();
						if (stateName.equals(UserStory.SUBMITTED_NAME)) {
							cardLayout.show(panel, SUBMITTED_PANEL);
							pnlSubmitted.setInfo(id);
						}
						if (stateName.equals(UserStory.BACKLOG_NAME)) {
							cardLayout.show(panel, BACKLOG_PANEL);
							pnlBacklog.setInfo(id);
						}
						if (stateName.equals(UserStory.WORKING_NAME)) {
							cardLayout.show(panel, WORKING_PANEL);
							pnlWorking.setInfo(id);
						} 
						if (stateName.equals(UserStory.VERIFYING_NAME)) {
							cardLayout.show(panel, VERIFYING_PANEL);
							pnlVerifying.setInfo(id);
						}  
						if (stateName.equals(UserStory.COMPLETED_NAME)) {
							cardLayout.show(panel, COMPLETED_PANEL);
							pnlCompleted.setInfo(id);
						} 
						if (stateName.equals(UserStory.REJECTED_NAME)) {
							cardLayout.show(panel, REJECTED_PANEL);
							pnlRejected.setInfo(id);
						}
					} catch (NumberFormatException nfe) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid id");
					} catch (NullPointerException npe) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid id");
					}
				}
			} 
			RequirementsManagerGUI.this.repaint();
			RequirementsManagerGUI.this.validate();
		}
		
		public void updateProject() {
			tableModel.updateData();
			
			String projectName = RequirementsManager.getInstance().getProjectName();
			
			if (projectName == null) {
				projectName = "Create a Project";
				btnAdd.setEnabled(false);
				btnDelete.setEnabled(false);
				btnEdit.setEnabled(false);
				btnUpdateProject.setEnabled(false);
			} else {
				btnAdd.setEnabled(true);
				btnDelete.setEnabled(true);
				btnEdit.setEnabled(true);
				btnUpdateProject.setEnabled(true);
			}
			
			comboProjectList.removeAllItems();
			String [] projectList = RequirementsManager.getInstance().getProjectList();
			for (int i = 0; i < projectList.length; i++) {
				comboProjectList.addItem(projectList[i]);
			}
			comboProjectList.setSelectedItem(RequirementsManager.getInstance().getProjectName());
			
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Project: " + projectName);
			setBorder(border);
			setToolTipText("Project: " + projectName);
		}
		
		/**
		 * UserStoryTableModel is the object underlying the JTable object that displays
		 * the list of UserStories to the user.
		 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
		 */
		private class UserStoryTableModel extends AbstractTableModel {
			
			/** ID number used for object serialization. */
			private static final long serialVersionUID = 1L;
			/** Column names for the table */
			private String [] columnNames = {"ID", "State", "Title", "Developer"};
			/** Data stored in the table */
			private Object [][] data;
			
			/**
			 * Constructs the UserStoryTableModel by requesting the latest information
			 * from the UserStoryTableModel.
			 */
			public UserStoryTableModel() {
				updateData();
			}

			/**
			 * Returns the number of columns in the table.
			 * @return the number of columns in the table.
			 */
			public int getColumnCount() {
				return columnNames.length;
			}

			/**
			 * Returns the number of rows in the table.
			 * @return the number of rows in the table.
			 */
			public int getRowCount() {
				if (data == null) 
					return 0;
				return data.length;
			}
			
			/**
			 * Returns the column name at the given index.
			 * @param col the column index
			 * @return the column name at the given column.
			 */
			public String getColumnName(int col) {
				return columnNames[col];
			}

			/**
			 * Returns the data at the given {row, col} index.
			 * @param row the row index
			 * @param col the column index
			 * @return the data at the given location.
			 */
			public Object getValueAt(int row, int col) {
				if (data == null)
					return null;
				return data[row][col];
			}
			
			/**
			 * Sets the given value to the given {row, col} location.
			 * @param value Object to modify in the data.
			 * @param row the row index
			 * @param col the column index
			 */
			public void setValueAt(Object value, int row, int col) {
				data[row][col] = value;
				fireTableCellUpdated(row, col);
			}
			
			/**
			 * Updates the given model with UserStory information from the RequirementsManager.
			 */
			private void updateData() {
				RequirementsManager m = RequirementsManager.getInstance();
				data = m.getUserStoriesAsArray();
			}
		}
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a submitted user story.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class SubmittedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** UserStoryPanel that presents the User Story's information to the user */
		private UserStoryPanel pnlInfo;
		/** Current UserStory's id */
		private int id;
		
		/** Label - priority */
		private JLabel lblPriority;
		/** ComboBox - priority */
		private JComboBox<String> comboPriority;
		/** Button - Accept w/ priority */
		private JButton btnAccept;
		
		/** Label - rejection reason */
		private JLabel lblRejectionReason;
		/** ComboBox - rejection reason */
		private JComboBox<String> comboRejectionReason;
		/** Button - Reject w/ rejection reason */
		private JButton btnReject;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a UserStory in the SubmittedState
		 */
		public SubmittedPanel() {
			
			pnlInfo = new UserStoryPanel();		
			
			lblPriority = new JLabel("Priority:");
			comboPriority = new JComboBox<String>();
			comboPriority.addItem("High");
			comboPriority.addItem("Medium");
			comboPriority.addItem("Low");
			btnAccept = new JButton("Accept");
			
			lblRejectionReason = new JLabel("Rejection Reason:");
			comboRejectionReason = new JComboBox<String>();
			comboRejectionReason.addItem("Duplicate");
			comboRejectionReason.addItem("Inappropriate");
			comboRejectionReason.addItem("Infeasible");
			btnReject = new JButton("Reject");
			
			btnCancel = new JButton("Cancel");
			
			btnAccept.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlAccept = new JPanel();
			pnlAccept.setLayout(new GridLayout(1, 3));
			pnlAccept.add(lblPriority);
			pnlAccept.add(comboPriority);
			pnlAccept.add(btnAccept);
			
			JPanel pnlReject = new JPanel();
			pnlReject.setLayout(new GridLayout(1, 3));
			pnlReject.add(lblRejectionReason);
			pnlReject.add(comboRejectionReason);
			pnlReject.add(btnReject);

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 1));
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlAccept, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlReject, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
					
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
					
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the UserStoryInfoPanel with the given user story data.
		 * @param id id of the user story
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setUserStoryInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnAccept) {
				int idx = comboPriority.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "No priority selected");
				} else {				
					String priority = comboPriority.getItemAt(idx);
					//Try a command.  If problem, go back to user story list.
					try {
						Command c = new Command(Command.CommandValue.BACKLOG, priority);
						RequirementsManager.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} else if (e.getSource() == btnReject) {
				int idx = comboRejectionReason.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "No rejection reason selected");
				} else {				
					String rejectionReason = comboRejectionReason.getItemAt(idx);
					//Try a command.  If problem, go back to user story list.
					try {
						Command c = new Command(Command.CommandValue.REJECT, rejectionReason);
						RequirementsManager.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			}
			if (reset) {
				//All buttons lead back to project
				cardLayout.show(panel, PROJECT_PANEL);
				pnlUserStoryList.updateProject();
				RequirementsManagerGUI.this.repaint();
				RequirementsManagerGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a backlogged user story.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class BacklogPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** UserStoryPanel that presents the User Story's information to the user */
		private UserStoryPanel pnlInfo;
		/** Current UserStory's id */
		private int id;
		
		/** Label - developer */
		private JLabel lblDeveloper;
		/** ComboBox - developer */
		private JTextField txtDeveloper;
		/** Button - Assign w/ developer */
		private JButton btnAssign;
		
		/** Label - rejection reason */
		private JLabel lblRejectionReason;
		/** ComboBox - rejection reason */
		private JComboBox<String> comboRejectionReason;
		/** Button - Reject w/ rejection reason */
		private JButton btnReject;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a UserStory in the BacklogState
		 */
		public BacklogPanel() {
			
			pnlInfo = new UserStoryPanel();		
			
			lblDeveloper = new JLabel("Developer Id:");
			txtDeveloper = new JTextField(25);
			btnAssign = new JButton("Assign");
			
			lblRejectionReason = new JLabel("Rejection Reason:");
			comboRejectionReason = new JComboBox<String>();
			comboRejectionReason.addItem("Duplicate");
			comboRejectionReason.addItem("Inappropriate");
			comboRejectionReason.addItem("Infeasible");
			btnReject = new JButton("Reject");
			
			btnCancel = new JButton("Cancel");
			
			btnAssign.addActionListener(this);
			btnReject.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlAccept = new JPanel();
			pnlAccept.setLayout(new GridLayout(1, 3));
			pnlAccept.add(lblDeveloper);
			pnlAccept.add(txtDeveloper);
			pnlAccept.add(btnAssign);
			
			JPanel pnlReject = new JPanel();
			pnlReject.setLayout(new GridLayout(1, 3));
			pnlReject.add(lblRejectionReason);
			pnlReject.add(comboRejectionReason);
			pnlReject.add(btnReject);

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 1));
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlAccept, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlReject, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
						
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
						
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the UserStoryInfoPanel with the given user story data.
		 * @param id id of the user story
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setUserStoryInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnAssign) {				
				String developer = txtDeveloper.getText();
				//Try a command.  If problem, go back to user story list.
				try {
					Command c = new Command(Command.CommandValue.ASSIGN, developer);
					RequirementsManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnReject) {
				int idx = comboRejectionReason.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "No rejection reason selected");
				} else {				
					String rejectionReason = comboRejectionReason.getItemAt(idx);
					//Try a command.  If problem, go back to user story list.
					try {
						Command c = new Command(Command.CommandValue.REJECT, rejectionReason);
						RequirementsManager.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			}
			if (reset) {
				//All buttons lead back to project
				cardLayout.show(panel, PROJECT_PANEL);
				pnlUserStoryList.updateProject();
				RequirementsManagerGUI.this.repaint();
				RequirementsManagerGUI.this.validate();
				txtDeveloper.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a user story in the working state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class WorkingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** UserStoryPanel that presents the User Story's information to the user */
		private UserStoryPanel pnlInfo;
		/** Current UserStory's id */
		private int id;
		
		/** Label - developer */
		private JLabel lblDeveloper;
		/** ComboBox - developer */
		private JTextField txtDeveloper;
		/** Button - Assign w/ developer */
		private JButton btnAssign;
		
		/** Label - rejection reason */
		private JLabel lblRejectionReason;
		/** ComboBox - rejection reason */
		private JComboBox<String> comboRejectionReason;
		/** Button - Reject w/ rejection reason */
		private JButton btnReject;
		
		/** Button - Reopen and remove developer */
		private JButton btnReopen;
		/** Button - Review */
		private JButton btnReview;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a UserStory in the BacklogState
		 */
		public WorkingPanel() {
			
			pnlInfo = new UserStoryPanel();		
			
			lblDeveloper = new JLabel("Developer Id:");
			txtDeveloper = new JTextField(25);
			btnAssign = new JButton("Assign");
			
			lblRejectionReason = new JLabel("Rejection Reason:");
			comboRejectionReason = new JComboBox<String>();
			comboRejectionReason.addItem("Duplicate");
			comboRejectionReason.addItem("Inappropriate");
			comboRejectionReason.addItem("Infeasible");
			btnReject = new JButton("Reject");
			
			btnReopen = new JButton("Reopen");
			btnReview = new JButton("Review");
			
			btnCancel = new JButton("Cancel");
			
			btnAssign.addActionListener(this);
			btnReject.addActionListener(this);
			btnReopen.addActionListener(this);
			btnReview.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());
			
			JPanel pnlAccept = new JPanel();
			pnlAccept.setLayout(new GridLayout(1, 3));
			pnlAccept.add(lblDeveloper);
			pnlAccept.add(txtDeveloper);
			pnlAccept.add(btnAssign);
			
			JPanel pnlReject = new JPanel();
			pnlReject.setLayout(new GridLayout(1, 3));
			pnlReject.add(lblRejectionReason);
			pnlReject.add(comboRejectionReason);
			pnlReject.add(btnReject);

			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnReview);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlAccept, c);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlReject, c);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
						
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
						
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the UserStoryInfoPanel with the given user story data.
		 * @param id id of the user story
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setUserStoryInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnAssign) {				
				String developer = txtDeveloper.getText();
				//Try a command.  If problem, go back to user story list.
				try {
					Command c = new Command(Command.CommandValue.ASSIGN, developer);
					RequirementsManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnReject) {
				int idx = comboRejectionReason.getSelectedIndex();
				
				if (idx == -1) {
					reset = false;
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "No rejection reason selected");
				} else {				
					String rejectionReason = comboRejectionReason.getItemAt(idx);
					//Try a command.  If problem, go back to user story list.
					try {
						Command c = new Command(Command.CommandValue.REJECT, rejectionReason);
						RequirementsManager.getInstance().executeCommand(id, c);
					} catch (IllegalArgumentException iae) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
						reset = false;
					} catch (UnsupportedOperationException uoe) {
						JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
						reset = false;
					}
				}
			} else if (e.getSource() == btnReopen) {
				try {
					Command c = new Command(Command.CommandValue.REOPEN, null);
					RequirementsManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnReview) {
				try {
					Command c = new Command(Command.CommandValue.REVIEW, null);
					RequirementsManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead back to project
				cardLayout.show(panel, PROJECT_PANEL);
				pnlUserStoryList.updateProject();
				RequirementsManagerGUI.this.repaint();
				RequirementsManagerGUI.this.validate();
				txtDeveloper.setText("");
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a user story in the verifying state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class VerifyingPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** UserStoryPanel that presents the User Story's information to the user */
		private UserStoryPanel pnlInfo;
		/** Current UserStory's id */
		private int id;
		
		/** Button - Reopen and remove developer */
		private JButton btnReopen;
		/** Button - Confirm */
		private JButton btnConfirm;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a UserStory in the BacklogState
		 */
		public VerifyingPanel() {
			
			pnlInfo = new UserStoryPanel();		
			
			btnReopen = new JButton("Reopen");
			btnConfirm = new JButton("Confirm");
			
			btnCancel = new JButton("Cancel");
			btnReopen.addActionListener(this);
			btnConfirm.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());


			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 3));
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnConfirm);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
						
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
						
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the UserStoryInfoPanel with the given user story data.
		 * @param id id of the user story
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setUserStoryInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnReopen) {
				try {
					Command c = new Command(Command.CommandValue.REOPEN, null);
					RequirementsManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
					reset = false;
				}
			} else if (e.getSource() == btnConfirm) {
				try {
					Command c = new Command(Command.CommandValue.CONFIRM, null);
					RequirementsManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
					reset = false;
				}
			}
			if (reset) {
				//All buttons lead back to project
				cardLayout.show(panel, PROJECT_PANEL);
				pnlUserStoryList.updateProject();
				RequirementsManagerGUI.this.repaint();
				RequirementsManagerGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a user story in the completed state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class CompletedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** UserStoryPanel that presents the User Story's information to the user */
		private UserStoryPanel pnlInfo;
		/** Current UserStory's id */
		private int id;
		
		/** Button - Reopen and remove developer */
		private JButton btnReopen;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a UserStory in the BacklogState
		 */
		public CompletedPanel() {
			
			pnlInfo = new UserStoryPanel();		
			
			btnReopen = new JButton("Reopen");
			
			btnCancel = new JButton("Cancel");
			btnReopen.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());


			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnReopen);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
						
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
						
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the UserStoryInfoPanel with the given user story data.
		 * @param id id of the user story
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setUserStoryInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnReopen) {
				try {
					Command c = new Command(Command.CommandValue.REOPEN, null);
					RequirementsManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
					reset = false;
				}
			} 
			if (reset) {
				//All buttons lead back to project
				cardLayout.show(panel, PROJECT_PANEL);
				pnlUserStoryList.updateProject();
				RequirementsManagerGUI.this.repaint();
				RequirementsManagerGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * interacts with a user story in the rejected state.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class RejectedPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		/** UserStoryPanel that presents the User Story's information to the user */
		private UserStoryPanel pnlInfo;
		/** Current UserStory's id */
		private int id;
		
		/** Button - Resubmit*/
		private JButton btnResubmit;
		
		/** Button - cancel edit */
		private JButton btnCancel;
		
		/**
		 * Constructs the JPanel for editing a UserStory in the BacklogState
		 */
		public RejectedPanel() {
			
			pnlInfo = new UserStoryPanel();		
			
			btnResubmit = new JButton("Resubmit");
			
			btnCancel = new JButton("Cancel");
			btnResubmit.addActionListener(this);
			btnCancel.addActionListener(this);
			
			JPanel pnlCommands = new JPanel();
			Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, "Commands");
			pnlCommands.setBorder(border);
			pnlCommands.setToolTipText("Commands");
			
			pnlCommands.setLayout(new GridBagLayout());


			JPanel pnlBtnRow = new JPanel();
			pnlBtnRow.setLayout(new GridLayout(1, 2));
			pnlBtnRow.add(btnResubmit);
			pnlBtnRow.add(btnCancel);
			
			GridBagConstraints c = new GridBagConstraints();
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			pnlCommands.add(pnlBtnRow, c);
						
			setLayout(new GridBagLayout());
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 5;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlInfo, c);
						
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(pnlCommands, c);
			
		}
		
		/**
		 * Set the UserStoryInfoPanel with the given user story data.
		 * @param id id of the user story
		 */
		public void setInfo(int id) {
			this.id = id;
			pnlInfo.setUserStoryInfo(this.id);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true;
			if (e.getSource() == btnResubmit) {
				try {
					Command c = new Command(Command.CommandValue.RESUBMIT, null);
					RequirementsManager.getInstance().executeCommand(id, c);
				} catch (IllegalArgumentException iae) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid command");
					reset = false;
				} catch (UnsupportedOperationException uoe) {
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid state transition");
					reset = false;
				}
			} 
			if (reset) {
				//All buttons lead back to project
				cardLayout.show(panel, PROJECT_PANEL);
				pnlUserStoryList.updateProject();
				RequirementsManagerGUI.this.repaint();
				RequirementsManagerGUI.this.validate();
			}
			//Otherwise, do not refresh the GUI panel and wait for correct user input.
		}
		
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * shows information about the user story.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class UserStoryPanel extends JPanel {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;
		
		/** Label - title */
		private JLabel lblTitle;
		/** JTextField - title */
		private JTextField txtTitle;
		
		/** Label - user */
		private JLabel lblUser;
		/** JTextField - user */
		private JTextField txtUser;
		
		/** Label - action */
		private JLabel lblAction;
		/** JTextField - action */
		private JTextField txtAction;
		
		/** Label - value */
		private JLabel lblValue;
		/** JTextField - value */
		private JTextField txtValue;
		
		/** Label - priority */
		private JLabel lblPriority;
		/** JTextField - priority */
		private JTextField txtPriority;
		
		/** Label - developer */
		private JLabel lblDeveloper;
		/** JTextField - developer */
		private JTextField txtDeveloper;
		
		/** Label - rejection reason */
		private JLabel lblRejectionReason;
		/** JTextField - rejection reason */
		private JTextField txtRejectionReason;
		
		/** 
		 * Construct the panel for the information.
		 */
		public UserStoryPanel() {
			super(new GridBagLayout());
			
			lblTitle = new JLabel("Title");
			lblUser = new JLabel("As a");
			lblAction = new JLabel("I want to");
			lblValue = new JLabel("so I can");
			lblPriority = new JLabel("Priority");
			lblDeveloper = new JLabel("Developer Id");
			lblRejectionReason = new JLabel("Rejection Reason");
			
			txtTitle = new JTextField(15);
			txtUser = new JTextField(15);
			txtAction = new JTextField(15);
			txtValue = new JTextField(15);
			txtPriority = new JTextField(15);
			txtDeveloper = new JTextField(15);
			txtRejectionReason = new JTextField(15);
			
			txtTitle.setEditable(false);
			txtUser.setEditable(false);
			txtAction.setEditable(false);
			txtValue.setEditable(false);
			txtPriority.setEditable(false);
			txtDeveloper.setEditable(false);
			txtRejectionReason.setEditable(false);	
			
			GridBagConstraints c = new GridBagConstraints();
						
			//Row 1 - Title	
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblTitle);
			row1.add(txtTitle);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row1, c);
			
			//Row 2 - User
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblUser);
			row2.add(txtUser);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row2, c);
			
			//Row 3 - Action
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(lblAction);
			row3.add(txtAction);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row3, c);
			
			//Row 4 - Value
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(lblValue);
			row4.add(txtValue);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row4, c);
			
			//Row 5 - Priority
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 2));
			row5.add(lblPriority);
			row5.add(txtPriority);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row5, c);
			
			//Row 6 - Developer Id
			JPanel row6 = new JPanel();
			row6.setLayout(new GridLayout(1, 2));
			row6.add(lblDeveloper);
			row6.add(txtDeveloper);
			
			c.gridx = 0;
			c.gridy = 5;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row6, c);
			
			//Row 7 - Rejection Reason
			JPanel row7 = new JPanel();
			row7.setLayout(new GridLayout(1, 2));
			row7.add(lblRejectionReason);
			row7.add(txtRejectionReason);
			
			c.gridx = 0;
			c.gridy = 6;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row7, c);
		}
		
		/**
		 * Adds information about the user story to the display.  
		 * @param id the id for the user story to display information about.
		 */
		public void setUserStoryInfo(int id) {
			//Get the user story from the model
			UserStory story = RequirementsManager.getInstance().getUserStoryById(id);
			if (story == null) {
				//If the user story doesn't exist for the given id, show an error message
				JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "Invalid id");
				cardLayout.show(panel, PROJECT_PANEL);
				RequirementsManagerGUI.this.repaint();
				RequirementsManagerGUI.this.validate();
			} else {
				//Set the border information with the project name, user story id, and current state
				String projectName = RequirementsManager.getInstance().getProjectName();
				
				Border lowerEtched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
				TitledBorder border = BorderFactory.createTitledBorder(lowerEtched, projectName + " User Story " + story.getId() + " - " + story.getState());
				setBorder(border);
				setToolTipText(projectName + " User Story " + story.getId() + " - " + story.getState());
				
				//Set all of the fields with the information
				txtTitle.setText(story.getTitle());
				txtUser.setText(story.getUser());
				txtAction.setText(story.getAction());
				txtValue.setText(story.getValue());
				txtPriority.setText(story.getPriority());
				txtDeveloper.setText(story.getDeveloperId());
				txtRejectionReason.setText(story.getRejectionReason());
			}
		}
	}
	
	/**
	 * Inner class that creates the look and behavior for the JPanel that 
	 * allows for creation of a new user story.
	 * 
	 * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
	 */
	private class AddUserStoryPanel extends JPanel implements ActionListener {
		/** ID number used for object serialization. */
		private static final long serialVersionUID = 1L;

		/** Label - title */
		private JLabel lblTitle;
		/** JTextField - title */
		private JTextField txtTitle;
		
		/** Label - user */
		private JLabel lblUser;
		/** JTextField - user */
		private JTextField txtUser;
		
		/** Label - action */
		private JLabel lblAction;
		/** JTextField - action */
		private JTextField txtAction;
		
		/** Label - value */
		private JLabel lblValue;
		/** JTextField - value */
		private JTextField txtValue;
		
		/** Button to add a user story */
		private JButton btnAdd;
		/** Button for canceling add action */
		private JButton btnCancel;
		
		/**
		 * Creates the JPanel for adding new user stories to the 
		 * manager.
		 */
		public AddUserStoryPanel() {
			super(new GridBagLayout());  
			
			//Construct widgets
			lblTitle = new JLabel("Title");
			lblUser = new JLabel("As a");
			lblAction = new JLabel("I want to");
			lblValue = new JLabel("so I can");
			
			txtTitle = new JTextField(15);
			txtUser = new JTextField(15);
			txtAction = new JTextField(15);
			txtValue = new JTextField(15);
			
			btnAdd = new JButton("Add to Project");
			btnCancel = new JButton("Cancel");
			
			//Adds action listeners
			btnAdd.addActionListener(this);
			btnCancel.addActionListener(this);
			
			GridBagConstraints c = new GridBagConstraints();
						
			//Row 1 - Title
			JPanel row1 = new JPanel();
			row1.setLayout(new GridLayout(1, 2));
			row1.add(lblTitle);
			row1.add(txtTitle);
			
			c.gridx = 0;
			c.gridy = 0;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row1, c);
			
			//Row 3 - User
			JPanel row2 = new JPanel();
			row2.setLayout(new GridLayout(1, 2));
			row2.add(lblUser);
			row2.add(txtUser);
			
			c.gridx = 0;
			c.gridy = 1;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row2, c);
			
			//Row 3 - Action
			JPanel row3 = new JPanel();
			row3.setLayout(new GridLayout(1, 2));
			row3.add(lblAction);
			row3.add(txtAction);
			
			c.gridx = 0;
			c.gridy = 2;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row3, c);
			
			//Row 4 - Value
			JPanel row4 = new JPanel();
			row4.setLayout(new GridLayout(1, 2));
			row4.add(lblValue);
			row4.add(txtValue);
			
			c.gridx = 0;
			c.gridy = 3;
			c.weightx = 1;
			c.weighty = 2;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row4, c);
			
			//Row 5 - Buttons
			JPanel row5 = new JPanel();
			row5.setLayout(new GridLayout(1, 2));
			row5.add(btnAdd);
			row5.add(btnCancel);
			
			c.gridx = 0;
			c.gridy = 4;
			c.weightx = 1;
			c.weighty = 1;
			c.anchor = GridBagConstraints.LINE_START;
			c.fill = GridBagConstraints.BOTH;
			add(row5, c);
		}

		/**
		 * Performs an action based on the given ActionEvent.
		 * @param e user event that triggers an action.
		 */
		public void actionPerformed(ActionEvent e) {
			boolean reset = true; //Assume done unless error
			if (e.getSource() == btnAdd) {
				String title = txtTitle.getText();
				String user = txtUser.getText();
				String action = txtAction.getText();
				String value = txtValue.getText();
				
				//Get instance of model and add user story
				try {
					RequirementsManager.getInstance().addUserStoryToProject(title, user, action, value);
				} catch (IllegalArgumentException exp) {
					reset = false;
					JOptionPane.showMessageDialog(RequirementsManagerGUI.this, "User Story cannot be created.");
				}
			} 
			if (reset) {
				//All buttons lead to back user story list
				cardLayout.show(panel, PROJECT_PANEL);
				pnlUserStoryList.updateProject();
				RequirementsManagerGUI.this.repaint();
				RequirementsManagerGUI.this.validate();
				//Reset fields
				txtTitle.setText("");
				txtUser.setText("");
				txtAction.setText("");
				txtValue.setText("");
			}
		}
	}
}
