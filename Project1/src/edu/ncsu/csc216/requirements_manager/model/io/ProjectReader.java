package edu.ncsu.csc216.requirements_manager.model.io;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import edu.ncsu.csc216.requirements_manager.model.manager.Project;
import edu.ncsu.csc216.requirements_manager.model.user_story.UserStory;

/**
 * Method handles input from project files and reads them into the program 
 * so the projects can be interacted with. Exclusively called from the 
 * RequirementsManager controller class.
 * @author jeff
 *
 */
public class ProjectReader {
	
	/**
	 * Method reads projects in from a file. Ignores projects and UserStories that are improperly formatted.
	 * @param fileName Name of the file to read from
	 * @return ArrayList of all projects found within the file with UserStories contained.
	 * @throws IllegalArgumentException if file cannot be located
	 */
	public static ArrayList<Project> readProjectFile(String fileName) {
		ArrayList<Project> list = new ArrayList<>();
		Scanner s;
		try {
			s = new Scanner(new FileInputStream(fileName));
		}
		catch (FileNotFoundException e) {
			throw new IllegalArgumentException("Unable to load file.");
		}
		s.useDelimiter("\\r?\\n?[#]");
		while (s.hasNext()) {
			Project p = processProject(s.next());
			if (p == null) continue;
			else { list.add(p); }
		}
		s.close();
		return list;
	}
	
	/**
	 * Method is called from readProjectFile to handle single projects. 
	 * @param project Project in string form, waiting for individual stories to be handled
	 * @return A Project containing all the stories found within
	 */
	private static Project processProject(String project) {
		Scanner projScan = new Scanner(project);
		Project p = new Project(projScan.nextLine().trim());
		if (p.getProjectName().equals("*")) { 
			projScan.close();
			return null; 
		}
		projScan.useDelimiter("\\r?\\n?[*]");
		while (projScan.hasNext()) {
			UserStory s = processUserStory(projScan.next());
			if (s == null) continue;
			else { p.addUserStory(s); }
		}
		projScan.close();
		if (p.getUserStories().size() == 0) {
			return null;
		}
		return p;
	}
	
	/**
	 * Method is called from processProject to turn a String into a story
	 * @param userStory String that should be a single story
	 * @return Story to processProject to be placed into an ArrayList
	 */
	private static UserStory processUserStory(String userStory) {
		Scanner storyScan = new Scanner(userStory);
		storyScan.useDelimiter("\\r?\\n?[-]");
		Scanner firstLine = new Scanner(storyScan.next());
		firstLine.useDelimiter(",");
		int id;
		String state = null;
		String title = null;
		String priority = null;
		String devId = null;
		String rejReason = null;
		String user = null;
		String action = null;
		String value = null;
		try {
			id = Integer.parseInt(firstLine.next().trim());
			state = firstLine.next();
			title = firstLine.next();
			switch (state) {
				case UserStory.COMPLETED_NAME:
				case UserStory.WORKING_NAME:
				case UserStory.VERIFYING_NAME:
					priority = firstLine.next();
					devId = firstLine.next();
					if (firstLine.hasNext()) {
						firstLine.close();
						storyScan.close();
						return null;
					}
					break;
				case UserStory.REJECTED_NAME:
					rejReason = firstLine.next();
					if (firstLine.hasNext()) {
						firstLine.close();
						storyScan.close();
						return null;
					}
					break;
				case UserStory.BACKLOG_NAME:
					priority = firstLine.next();
					if (firstLine.hasNext()) {
						firstLine.close();
						storyScan.close();
						return null;
					}
					break;
				case UserStory.SUBMITTED_NAME:
					if (firstLine.hasNext()) {
						firstLine.close();
						storyScan.close();
						return null;
					}
					break;
				default:
					storyScan.close();
					return null;
			}
			user = storyScan.next().trim();
			action = storyScan.next().trim();
			value = storyScan.next().trim();
		}
		catch (NumberFormatException | NoSuchElementException IME) {
			storyScan.close();
			return null;
		}

		storyScan.useDelimiter(" ");
		if (storyScan.hasNext()) {
			storyScan.close();
			return null;
		}

		storyScan.close();
		firstLine.close();
		UserStory s;
		try {
			s = new UserStory(id, state, title, user, action, value, priority, devId, rejReason);
		}
		catch (IllegalArgumentException IAE) {
			return null;
		}
		return s;
	}

}
