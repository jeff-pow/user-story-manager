package edu.ncsu.csc216.requirements_manager.model.manager;

import edu.ncsu.csc216.requirements_manager.model.command.Command;
import edu.ncsu.csc216.requirements_manager.model.io.ProjectReader;
import edu.ncsu.csc216.requirements_manager.model.user_story.UserStory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests Project class. Getters are tested via other methods rather than implicitly
 * @author jeff
 *
 */
public class ProjectTest {
	/** Valid title */
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
	static Project project;

	/**
	 * Method creates a new project with six stories before each test
	 */
	@BeforeEach
	public void setUp() {
		UserStory.setCounter(0);
		UserStory s = new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		UserStory s2 = new UserStory(3, UserStory.REJECTED_NAME, TITLE, USER, ACTION, VALUE, null, null, REJECTION_REASON);
		UserStory s3 = new UserStory(4, UserStory.SUBMITTED_NAME, TITLE, USER, ACTION, VALUE, null, null, null);
		UserStory s4 = new UserStory(5, UserStory.BACKLOG_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, null, null);
		UserStory s5 = new UserStory(6, UserStory.COMPLETED_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		UserStory s6 = new UserStory(7, UserStory.VERIFYING_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		project = new Project("My Project");
		project.addUserStory(s);
		project.addUserStory(s2);
		project.addUserStory(s3);
		project.addUserStory(s4);
		project.addUserStory(s5);
		project.addUserStory(s6);
	}
	/**
	 * Method tests capability of project to add a story to the project's ArrayList
	 */
	@Test
	public void testAddUserStory() {
		int i = project.addUserStory(TITLE, USER, ACTION, VALUE);
		assertEquals(8, i);
		int j = project.addUserStory("Homework Grader", "Teacher", "Grade homework", "Have more free time");
		assertEquals(9, j);
	}
	
	/**
	 * Method ensures proper project title will be set. Errors if String supplied to
	 * setter is null or empty
	 */
	@Test
	public void testSetProjectName() {
		assertThrows(IllegalArgumentException.class, () -> project = new Project(null));
		assertThrows(IllegalArgumentException.class, () -> project = new Project(""));
		project = new Project("My Super Awesome Project");
		assertEquals("My Super Awesome Project", project.getProjectName());
	}
	
	/**
	 * Method tests capability of project to return its stories in a proper format. Called by RequirementsManager class.
	 */
	@Test
	public void testGetUserStories() {
		assertEquals(6, project.getUserStories().size());
	}
	
	/**
	 * Tests capabilities to fetch a story by ID. Used by GUI to request information about a specific story
	 */
	@Test 
	public void testGetUserStoryById() {
		UserStory s = project.getUserStoryById(6);
		assertEquals(UserStory.COMPLETED_NAME, s.getState());
		UserStory s1 = project.getUserStoryById(3);
		assertEquals(UserStory.REJECTED_NAME, s1.getState());
	}

	/**
	 * Tests capabilities of a project to execute a command on a story given its ID.
	 */
	@Test
	public void testExecuteCommand() {
		assertThrows(UnsupportedOperationException.class,
				() -> project.executeCommand(6, new Command(Command.CommandValue.REJECT, "Duplicate")));
		project.executeCommand(6, new Command(Command.CommandValue.REOPEN, null));
		assertEquals(UserStory.WORKING_NAME, project.getUserStoryById(6).getState());
	}
	
	/**
	 * Tests capability to delete a story given the ID.
	 */
	@Test
	public void testDeleteUserStoryById() {
		project.deleteUserStoryById(6);
		boolean flag = false;
		List<UserStory> list = project.getUserStories();
		for (UserStory userStory : list) {
			if (userStory.getState().equals(UserStory.COMPLETED_NAME)) {
				flag = true;
			}
		}
		assertFalse(flag);
	}

	/**
	 * Method tests that the program can determine when a new story has a higher ID than the counter 
	 * in UserStory and then change the counter appropropriatly.
	 */
	@Test
	public void testSetUserStoryById() {
		UserStory.setCounter(0);
		project.setUserStoryId();
		int i = project.addUserStory(TITLE, USER, ACTION, VALUE);
		assertEquals(8, i);
	}

	/**
	 * Test read in projects in an invalid order
	 */
	@Test
	public void testInvalidOrder() {
		ArrayList<Project> projList = ProjectReader.readProjectFile("test-files/project3.txt");
		assertEquals(0, projList.get(0).getUserStories().get(0).getId());
	}
}
