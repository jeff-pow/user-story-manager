package edu.ncsu.csc216.requirements_manager.model.io;

import edu.ncsu.csc216.requirements_manager.model.manager.Project;
import edu.ncsu.csc216.requirements_manager.model.user_story.UserStory;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Method handles writing projects to files. Called exclusively by the RequirementManager
 * controller class.
 * @author jeff
 *
 */
public class ProjectWriter {
	
	/**
	 * Method writes project to a file
	 * @param fileName Name of the file to write to
	 * @param project Project to be written to a file
	 * @throws IllegalArgumentException if file cannot be found or written to for any other reason
	 */
	public static void writeProjectToFile(String fileName, Project project) {
		try {
			FileWriter file = new FileWriter(fileName);
			file.write("# " + project.getProjectName() + "\n");
			for (UserStory u : project.getUserStories()) {
				file.write(u.toString());
			}
			file.close();
		}
		catch (IOException IOE) {
			throw new IllegalArgumentException("Unable to save file.");
		}
	}

}
