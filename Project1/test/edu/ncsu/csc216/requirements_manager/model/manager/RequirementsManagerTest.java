package edu.ncsu.csc216.requirements_manager.model.manager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.ncsu.csc216.requirements_manager.model.command.Command;
import edu.ncsu.csc216.requirements_manager.model.user_story.UserStory;

/**
 * Tests RequirementsManager controller class
 * 
 * @author jeff
 *
 */
public class RequirementsManagerTest {

  /** RequirementsManager object */
  private RequirementsManager manager;
  /** Example project instance */
  static Project project;

  /**
   * Method creates a new project with six stories before each test Sets up a new
   * RequirementsManager object before every single test
   */
  @BeforeEach
  public void setUp() {
    manager = RequirementsManager.getInstance();
    manager.loadProjectsFromFile("test-files/project1.txt");
  }


  /**
   * Tests capability of controller class to execute a command on a story with a
   * particular ID in a specific project.
   */
  @Test
  public void testExecuteCommand() {
    assertThrows(UnsupportedOperationException.class,
        () -> manager.executeCommand(3, new Command(Command.CommandValue.REOPEN, null)));
    manager.executeCommand(3, new Command(Command.CommandValue.REJECT, "Duplicate"));
    assertEquals(UserStory.REJECTED_NAME, manager.getUserStoryById(3).getState());
  }

  /**
   * Tests capability of controller class to call a projects getUserStoryById
   * method
   */
  @Test
  public void testGetUserStoryById() {
    assertEquals(UserStory.VERIFYING_NAME, manager.getUserStoryById(1).getState());
  }

  /**
   * Tests project creation method to ensure title passed in is not null, empty,
   * or already existing
   */
  @Test
  public void testCreateNewProject() {
    assertThrows(IllegalArgumentException.class, () -> manager.createNewProject(""));
    assertThrows(IllegalArgumentException.class, () -> manager.createNewProject(null));
    manager.createNewProject("My Project");
    assertThrows(IllegalArgumentException.class, () -> manager.createNewProject("my project"));
  }

  /**
   * Tests project capability to return stories as an array for the GUI to display
   */
  @Test
  public void testGetUserStoriesAsArray() {
    String[][] expected = new String[6][4];
    expected[0] = new String[] { "0", "Completed", "Load Catalog", "sesmith5" };
    expected[1] = new String[] { "1", "Verifying", "Select Course", "jctetter" };
    expected[3] = new String[] { "3", "Backlog", "Export Schedule", "" };
    String[][] actual = manager.getUserStoriesAsArray();
    assertEquals(expected[0][0], actual[0][0]);
    assertEquals(expected[0][1], actual[0][1]);
    assertEquals(expected[0][2], actual[0][2]);
    assertEquals(expected[0][3], actual[0][3]);
    assertEquals(expected[1][0], actual[1][0]);
    assertEquals(expected[1][1], actual[1][1]);
    assertEquals(expected[1][2], actual[1][2]);
    assertEquals(expected[1][3], actual[1][3]);
    assertEquals(expected[3][0], actual[3][0]);
    assertEquals(expected[3][1], actual[3][1]);
    assertEquals(expected[3][2], actual[3][2]);
    assertEquals(expected[3][3], actual[3][3]);
  }

  /**
   * Method tests adding a UserStory to a project and then loading a new project from a file. 
   */
  @Test
  public void testAddUserStoryToProject() {
      manager.resetManager();
      manager.createNewProject("Project");
      manager.addUserStoryToProject("title1", "user1", "action1", "value1");
      String[][] arr = manager.getUserStoriesAsArray();
      assertEquals(1, arr.length);
      assertEquals("0", arr[0][0]);
      assertEquals("Submitted", arr[0][1]);
      assertEquals("title1", arr[0][2]);
      assertEquals("", arr[0][3]);
      manager.loadProjectsFromFile("test-files/project2.txt");
      assertEquals("WolfScheduler", manager.getProjectName());
      arr = manager.getUserStoriesAsArray();
      assertEquals(6, arr.length);
      assertEquals("0", arr[0][0]);
      assertEquals("Completed", arr[0][1]);
      assertEquals("Load Catalog", arr[0][2]);
      assertEquals("sesmith5", arr[0][3]);
      assertEquals("3", arr[3][0]);
      assertEquals("Backlog", arr[3][1]);
      assertEquals("Export Schedule", arr[3][2]);
      assertEquals("", arr[3][3]);
      assertEquals("5", arr[5][0]);
      assertEquals("Rejected", arr[5][1]);
      assertEquals("Post to Piazza", arr[5][2]);
      assertEquals("", arr[5][3]);
  }

}
