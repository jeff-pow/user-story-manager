package edu.ncsu.csc216.requirements_manager.model.manager;

import java.util.ArrayList;
import java.util.List;

import edu.ncsu.csc216.requirements_manager.model.command.Command;
import edu.ncsu.csc216.requirements_manager.model.io.ProjectReader;
import edu.ncsu.csc216.requirements_manager.model.io.ProjectWriter;
import edu.ncsu.csc216.requirements_manager.model.user_story.UserStory;

/**
 * Method serves as the controller for the rest of the backend of this program, 
 * and is the only class that the GUI must interact with to control the program.
 * @author jeff
 *
 */
public class RequirementsManager {

	/** Project with current focus by the program */
	private Project currentProject;
	/** ArrayList to keep track of projects */
	private ArrayList<Project> projectList;
	/** Private instance of RequirementsManager to ensure there is only ever one instance */
	private static RequirementsManager thereCanBeOnlyOne = null;

	/**
	 * Method initiates a RequirementsManager object. Called from GUI class.
	 */
	private RequirementsManager() {
		projectList = new ArrayList<>();
		currentProject = null;
	}
	
	/**
	 * Method used by GUI class to get the single instance of this class that the GUI
	 * will interact with. Preserves singlet design philosophy
	 * @return Single instance of RequirementsManager
	 */
	public static RequirementsManager getInstance() {
		if (thereCanBeOnlyOne == null) {
			thereCanBeOnlyOne = new RequirementsManager();
		}
		return thereCanBeOnlyOne;
	}
	
	/**
	 * Method calls class that handles writing projects to files. This preserves the ability for 
	 * GUI to only have to worry about interacting with a single class. 
	 * @param fileName File to write all current projects to
	 */
	public void saveCurrentProjectToFile(String fileName) {
		if (currentProject == null || currentProject.getUserStories().size() == 0 || currentProject.getUserStories() == null) {
			throw new IllegalArgumentException("Invalid stories/Project");
		}
		else {
			ProjectWriter.writeProjectToFile(fileName, currentProject);
		}
	}
	
	/**
	 * Method calls class that handles reading projects from files. This preserves the ability for 
	 * GUI to only have to worry about interacting with a single class. 
	 * @param fileName File to read projects from and place in program
	 */
	public void loadProjectsFromFile(String fileName) {
		projectList = ProjectReader.readProjectFile(fileName);
		loadProject(projectList.get(0).getProjectName());
	}
	
	/**
	 * Method creates a new project and places it in an ArrayList to be stored in program memory.
	 * @param projectName Name of project that should be created. User defined
	 */
	public void createNewProject(String projectName) {
		if (projectList.size() > 0) {
			if (projectName == null || "".equals(projectName)) {
				throw new IllegalArgumentException("Invalid project title");
			}
			for (Project p : projectList) {
				if (p.getProjectName().equalsIgnoreCase(projectName)) {
					throw new IllegalArgumentException("Name already exists");
				}
			}
		}
		projectList.add(new Project(projectName));
		loadProject(projectName);
	}
	
	/**
	 * Method converts stories of a project in an array. Used for GUI
	 * @return 2D array of Strings describing all stories in a project
	 */
	public String[][] getUserStoriesAsArray() {
		if (currentProject == null) return null;
		String[][] arr = new String[currentProject.getUserStories().size()][4];
		List<UserStory> list = currentProject.getUserStories();
		for (int i = 0; i < list.size(); i++) {
			arr[i][0] = String.valueOf(list.get(i).getId());
			arr[i][1] = list.get(i).getState();
			arr[i][2] = list.get(i).getTitle();
			if (list.get(i).getDeveloperId() != null) {
				arr[i][3] = list.get(i).getDeveloperId();
			}
			else {
				arr[i][3] = "";
			}
		}
		return arr;
	}
	
	/**
	 * Method returns a user story by id. Calls a method inside project class so that GUI only
	 * has to interact with this class.
	 * @param id ID of the story to be found
	 * @return Story if it is found
	 */
	public UserStory getUserStoryById(int id) {
		if (currentProject == null) return null;
		return currentProject.getUserStoryById(id);
	}
	
	/**
	 * Method changes state of a project specified by the ID of the project
	 * @param id ID of the project
	 * @param command Command the project should be changed to
	 */
	public void executeCommand(int id, Command command) {
		if (currentProject != null) currentProject.executeCommand(id, command);
	}
	
	/**
	 * Method deletes a story given the project and ID of the story
	 * @param id ID of the project to delete
	 */
	public void deleteUserStoryById(int id) {
		if (currentProject != null) currentProject.deleteUserStoryById(id);
	}
	
	/**
	 * Method adds a user story to the project. Called by GUI
	 * @param title Title of the story to create
	 * @param user Name of the user
	 * @param action Name of the action user wants to accomplish
	 * @param value What the user would like to accomplish with the story
	 */
	public void addUserStoryToProject(String title, String user, String action, String value) {
		if (currentProject != null) currentProject.addUserStory(title, user, action, value);
	}
	
	/**
	 * Makes a project front and center for programs focus, and sets UserStory counter, so it is
	 * usable by that project rather than a previous one
	 * @param projectName Name of the project to find
	 */
	public void loadProject(String projectName) {
		for (Project p : projectList) {
			if (p.getProjectName().equals(projectName)) {
				currentProject = p;
				p.setUserStoryId();
			}
		}
	}
	
	/**
	 * Returns the name of a project in question
	 * @return Name of a project
	 */
	public String getProjectName() {
		if (currentProject != null) return currentProject.getProjectName();
		else return null;
	}
	
	/**
	 * Returns names of projects in an array of Strings. Used in a menu in GUI
	 * @return array of Strings containing project names
	 */
	public String[] getProjectList() {
		String[] arr = new String[projectList.size()];
		for (int i = 0; i < projectList.size(); i++) {
			arr[i] = projectList.get(i).getProjectName();
		}
		return arr;
	}
	
	/**
	 * Method can wipe all projects in the memory of the program.
	 */
	protected void resetManager() {
		thereCanBeOnlyOne = new RequirementsManager();
	}

}
