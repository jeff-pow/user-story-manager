/**
 * 
 */
package edu.ncsu.csc216.requirements_manager.model.io;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.requirements_manager.model.manager.Project;
import edu.ncsu.csc216.requirements_manager.model.user_story.UserStory;

/**
 * Tests capabilities to read projects in from a file
 * @author jeff
 *
 */
public class ProjectReaderTest {
	/**
	 * Method takes in a file and reads them to see if program is capable of reading in projects from a file
	 */
	@Test
	public void testReadProjectsFromFile() {
		String fileName = "test-files/project2.txt";
		ArrayList<Project> projList = ProjectReader.readProjectFile(fileName);
		assertEquals(UserStory.COMPLETED_NAME, projList.get(0).getUserStoryById(0).getState());
	}

	/**
	 * Method tests program dealing an invalid story in a project file
	 */
	@Test
	public void testInvalidStoryInProjectFile() {
		ArrayList<Project> projList = ProjectReader.readProjectFile("test-files/project24.txt");
		assertEquals(0, projList.size());
	}

	/**
	 * Method tests program dealing a submitted story that also has a priority field
	 */
	@Test
	public void testSubmittedStoryWithPriority() {
		ArrayList<Project> projList = ProjectReader.readProjectFile("test-files/project14.txt");
		assertEquals(0, projList.size());
	}


	/**
	 * Tests project reader class for files with empty projects due to invalid stories
	 */
	@Test
	public void testEmptyProject() {
		ArrayList<Project> projList = ProjectReader.readProjectFile("test-files/project4.txt");
		assertEquals(0, projList.size());
	}

	/**
	 * Program should throw an error if invalid file type is provided
	 */
	@Test
	public void testInvalidFileName() {
		assertThrows(IllegalArgumentException.class, () -> ProjectReader.readProjectFile("Submitted"));
	}

}
