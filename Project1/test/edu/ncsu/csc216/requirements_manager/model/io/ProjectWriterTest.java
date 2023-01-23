package edu.ncsu.csc216.requirements_manager.model.io;

import edu.ncsu.csc216.requirements_manager.model.manager.Project;
import edu.ncsu.csc216.requirements_manager.model.user_story.UserStory;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Tests capabilities of program to write projects to a file for long term storage.
 * @author jeff
 *
 */
public class ProjectWriterTest {
	/*	/** Valid title */
	private static final String TITLE = "Math Solver";
	/** Valid story id */
	private static final int STORY_ID = 2;
	/** Valid user */
	private static final String USER = "Student";
	/** Valid action */
	private static final String ACTION = "Program that will do my math homework";
	/** Valid value */
	private static final String VALUE = "Have more free time";
	/** Valid priority */
	private static final String PRIORITY = "Low";
	/** Valid developerId */
	private static final String DEVELOPER_ID = "jrpowel9";
	/** Valid rejection reason */
	private static final String REJECTION_REASON = "Infeasible";
	/** Valid state */
	private static final String STATE = UserStory.WORKING_NAME;
	/** Example project instance */
	private static Project project;

	/**
	 * Helper method to compare two files for the same contents
	 * Taken from LR1 repo, StudentRecordIOTest.java
	 *
	 * @param expFile expected output
	 * @param actFile actual output
	 */
	public static void checkFiles(String expFile, String actFile) {
		try (Scanner expScanner = new Scanner(new FileInputStream(expFile));
			 Scanner actScanner = new Scanner(new FileInputStream(actFile))) {

			while (expScanner.hasNextLine() && actScanner.hasNextLine()) {
				String exp = expScanner.nextLine();
				String act = actScanner.nextLine();
				assertEquals(exp, act, "Expected: " + exp + " Actual: " + act);
				// The third argument helps with debugging!
			}
			if (expScanner.hasNextLine()) {
				fail("The expected results expect another line " + expScanner.nextLine());
			}
			if (actScanner.hasNextLine()) {
				fail("The actual results has an extra, unexpected line: " + actScanner.nextLine());
			}

			expScanner.close();
			actScanner.close();
		} catch (IOException e) {
			fail("Error reading files.");
		}
	}
	/**
	 * Method tests whether entire projects will properly be written to a file
	 */
	@Test
	public void testWriteProject() {
		UserStory s = new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		UserStory s2 = new UserStory(3, UserStory.REJECTED_NAME, TITLE, USER, ACTION, VALUE, null, null, REJECTION_REASON);
		UserStory s3 = new UserStory(4, UserStory.SUBMITTED_NAME, TITLE, USER, ACTION, VALUE, null, null, null);
		project = new Project("My Project");
		project.addUserStory(s);
		project.addUserStory(s2);
		project.addUserStory(s3);

		ProjectWriter.writeProjectToFile("actual.txt", project);
		checkFiles("test-files/expected_IO.txt", "actual.txt");
		assertEquals(3, project.getUserStories().size());
	}

	/**
	 * Method tests writing a project with a story in the verifying state
	 */
	@Test
	public void testVerifyingState() {
		project = new Project("Project 2");
		project.addUserStory(new UserStory(0, "Verifying", "title", "user", "action", "value", "Medium", "id", null));
		ProjectWriter.writeProjectToFile("actual_verifying.txt", project);
		assertEquals(1, project.getUserStories().size());
	}

}
