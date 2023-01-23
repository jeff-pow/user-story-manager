package edu.ncsu.csc216.requirements_manager.model.manager;

import edu.ncsu.csc216.requirements_manager.model.command.Command;
import edu.ncsu.csc216.requirements_manager.model.user_story.UserStory;

import java.util.ArrayList;
import java.util.List;

/**
 * Class stores many user stories in an ArrayList. Projects are stored in an ArrayList inside of RequirementsManager.
 * @author jeff
 *
 */
public class Project {
	/** Name of the project, can be user defined */
	private String projectName;
	/** ArrayList to keep track of stories in the project */
	private ArrayList<UserStory> list;

	/**
	 * Constructs an empty project
	 * @param name to use to title project
	 */
	public Project(String name) {
		setProjectName(name);
		list = new ArrayList<>();
	}
	
	/**
	 * Method keeps track of ids of user stories in a project and can be called to find what the ID of 
	 * the next story created should be. Then sets the ID of the project it is called on.
	 */
	public void setUserStoryId() {
		int j = 0;
		for (UserStory userStory : list) {
			if (userStory.getId() >= j) {
				j = userStory.getId() + 1;
			}
		}
		UserStory.setCounter(j);
	}


	/**
	 * Creates a story to add to the project
	 * @param title Name of the story
	 * @param user Name of the user creating the story
	 * @param action Name of the action the user would like to do
	 * @param value Why the user wants to be able to do the action
	 * @return Numerical ID of the project
	 */
	public int addUserStory(String title, String user, String action, String value) {
		UserStory s = new UserStory(title, user, action, value);
		list.add(s);
		return s.getId();
	}
	
	/**
	 * Method to add a story if the story has already been created
	 * @param story to add to the project
	 * @throws IllegalArgumentException if a story with the index provided already exists
	 */
	public void addUserStory(UserStory story) {
		if (list.size() != 0) {
			for (UserStory userStory : list) {
				if (userStory.getId() == story.getId()) {
					throw new IllegalArgumentException("Id already exists");
				}
			}
		}
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getId() > story.getId()) {
				list.add(i, story);
				return;
			}
		}
		list.add(story);
	}
	
	/**
	 * Method returns all user stories contained in the project to RequirementsManager for the
	 * UI to be able to display a project
	 * @return List of all stories in project
	 */
	public List<UserStory> getUserStories() {
		return list;
	}
	
	/**
	 * Method fetches user story by story ID. Allows GUI user to fetch individual stories. Notably
	 * not the index of story in ArrayList, but the ID field associated with it.
	 * @param id ID to find story of. 
	 * @return Story that program was searching for
	 */
	public UserStory getUserStoryById(int id) {
		for (UserStory userStory : list) {
			if (userStory.getId() == id) {
				return userStory;
			}
		}
		return null;
	}
	
	/**
	 * Method changes the state of a story from one to another, and is used by manager class to 
	 * interact with state.
	 * @param id of story to find in the project
	 * @param command Command to put on the story
	 */
	public void executeCommand(int id, Command command) {
		UserStory s = getUserStoryById(id);
		s.update(command);
	}
	
	/**
	 * Method searches for a story by ID and then deletes the story. Notably
	 * not the index of story in ArrayList, but the ID field associated with it.
	 * @param id Story id field to find a story by
	 */
	public void deleteUserStoryById(int id) {
		list.removeIf(userStory -> userStory.getId() == id);
	}
	
	/**
	 * Returns project name
	 * @return the projectName
	 */
	public String getProjectName() {
		return projectName;
	}

	/**
	 * Sets project name. Checks if string is null or of length zero before setting field.
	 * @param projectName the projectName to set
	 * @throws IllegalArgumentException if string supplied is null or empty
	 */
	private void setProjectName(String projectName) {
		if (projectName == null || "".equals(projectName)) {
			throw new IllegalArgumentException("Invalid project title");
		}
		this.projectName = projectName;
	}
}
