package edu.ncsu.csc216.requirements_manager.model.user_story;

import edu.ncsu.csc216.requirements_manager.model.command.Command;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests UserStory object. Getters are not tested specifically as they are
 * verified through the other methods.
 * 
 * @author jeff
 *
 */
public class UserStoryTest {
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

	/**
	 * Method ensures proper titles will be set. Errors if String supplied to setter
	 * is null or empty
	 */
	@Test
	public void testSetTitle() {
		UserStory s = new UserStory(TITLE, USER, ACTION, VALUE);
		assertEquals(UserStory.SUBMITTED_NAME, s.getState()); // See that the state of a new story with this constructor
		// is set to submitted
		assertThrows(IllegalArgumentException.class, () -> new UserStory(null, USER, ACTION, VALUE));
		assertThrows(IllegalArgumentException.class, () -> new UserStory("", USER, ACTION, VALUE));
	}

	/**
	 * Method ensures proper users will be set. Errors if String supplied to setter
	 * is null or empty
	 */
	@Test
	public void testSetUser() {
		assertThrows(IllegalArgumentException.class, () -> new UserStory(TITLE, null, ACTION, VALUE));
		assertThrows(IllegalArgumentException.class, () -> new UserStory(TITLE, "", ACTION, VALUE));
		new UserStory(TITLE, USER, ACTION, VALUE);

	}

	/**
	 * Method ensures proper action field will be set. Errors if String supplied to
	 * setter is null or empty
	 */
	@Test
	public void testSetAction() {
		assertThrows(IllegalArgumentException.class, () -> new UserStory(TITLE, USER, null, VALUE));
		assertThrows(IllegalArgumentException.class, () -> new UserStory(TITLE, USER, "", VALUE));
		new UserStory(TITLE, USER, ACTION, VALUE);
	}

	/**
	 * Method ensures proper value field will be set. Errors if String supplied to
	 * setter is null or empty
	 */
	@Test
	public void testSetValue() {
		new UserStory(TITLE, USER, ACTION, VALUE);
		assertThrows(IllegalArgumentException.class, () -> new UserStory(TITLE, USER, ACTION, null));
		assertThrows(IllegalArgumentException.class, () -> new UserStory(TITLE, USER, ACTION, ""));
	}

	/**
	 * Method ensures proper StoryId will be set.
	 */
	@Test
	public void testSetStoryId() {
		UserStory.setCounter(0);
		new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		UserStory s2 = new UserStory(TITLE, USER, ACTION, VALUE);
		assertEquals(3, s2.getId());
		UserStory s3 = new UserStory("homework grader", "teacher", "grade homework", "have more free time");
		assertEquals(4, s3.getId());
	}

	/**
	 * Method ensures proper States will be set. Errors if String supplied to setter
	 * is not one of six global constants in UserStory class
	 */
	@Test
	public void testSetState() {
		UserStory s1 = new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		assertEquals(UserStory.WORKING_NAME, s1.getState());
		UserStory s2 = new UserStory(STORY_ID, UserStory.REJECTED_NAME, TITLE, USER, ACTION, VALUE, null, null, REJECTION_REASON);
		assertEquals(UserStory.REJECTED_NAME, s2.getState());
		UserStory s3 = new UserStory(STORY_ID, UserStory.SUBMITTED_NAME, TITLE, USER, ACTION, VALUE, null, null, null);
		assertEquals(UserStory.SUBMITTED_NAME, s3.getState());
		UserStory s4 = new UserStory(STORY_ID, UserStory.BACKLOG_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, null, null);
		assertEquals(UserStory.BACKLOG_NAME, s4.getState());
		UserStory s5 = new UserStory(STORY_ID, UserStory.COMPLETED_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		assertEquals(UserStory.COMPLETED_NAME, s5.getState());
		UserStory s6 = new UserStory(STORY_ID, UserStory.VERIFYING_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		assertEquals(UserStory.VERIFYING_NAME, s6.getState());
		assertThrows(IllegalArgumentException.class, () -> new UserStory(STORY_ID, "invalid state", TITLE, USER, ACTION, VALUE,
				PRIORITY, DEVELOPER_ID, REJECTION_REASON));
		assertThrows(IllegalArgumentException.class, () -> new UserStory(STORY_ID, "", TITLE, USER, ACTION, VALUE,
				PRIORITY, DEVELOPER_ID, REJECTION_REASON));
	}

	/**
	 * Method ensures proper priorities will be set. Errors if String supplied to
	 * setter is not a one of three global constants in UserStory class
	 */
	@Test
	public void testSetPriority() {
		new UserStory(STORY_ID, UserStory.BACKLOG_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, null, null);
		assertThrows(IllegalArgumentException.class, () -> new UserStory(STORY_ID, UserStory.BACKLOG_NAME, TITLE, USER, ACTION, VALUE,
				"invalid priority", null, null));
		assertThrows(IllegalArgumentException.class, () -> new UserStory(STORY_ID, UserStory.BACKLOG_NAME, TITLE, USER, ACTION, VALUE,
				"", null, null));
		assertThrows(IllegalArgumentException.class, () -> new UserStory(STORY_ID, UserStory.BACKLOG_NAME, TITLE, USER, ACTION, VALUE,
				"low", null, null));
	}

	/**
	 * Method ensures proper developer id will be set. Errors if String supplied to
	 * setter is null or empty
	 */
	@Test
	public void testSetDeveloperId() {
		new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		assertThrows(IllegalArgumentException.class, () -> new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE,
				PRIORITY, "", null));
		assertThrows(IllegalArgumentException.class, () -> new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE,
				PRIORITY, null, null));

	}

	/**
	 * Method ensures proper rejection reason will be set. Errors if String supplied
	 * to setter is not one of three global constants in UserStory class
	 */
	@Test
	public void testSetRejectionReason() {
		new UserStory(STORY_ID, UserStory.REJECTED_NAME, TITLE, USER, ACTION, VALUE, null, null, REJECTION_REASON);
		assertThrows(IllegalArgumentException.class, () -> new UserStory(STORY_ID, UserStory.REJECTED_NAME, TITLE, USER, ACTION, VALUE,
				null, null, "invalid rejection reason"));
		assertThrows(IllegalArgumentException.class, () -> new UserStory(STORY_ID, UserStory.REJECTED_NAME, TITLE, USER, ACTION, VALUE,
				null, null, ""));

	}

	/**
	 * Method tests that toString method meets format requirements of project
	 */
	@Test
	public void testToString() {
		String expected = "* 2,Working,Math Solver,Low,jrpowel9,\n" +
				"- Student\n" +
				"- Program that will do my math homework\n" +
				"- Have more free time\n";
		UserStory s = new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		assertEquals(expected, s.toString());
	}

	/**
	 * Method tests if passing a command with update correctly updates the information of a story
	 */
	@Test
	public void testUpdate() {
		UserStory s = new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		s.update(new Command(Command.CommandValue.ASSIGN, "lritter"));
		assertEquals("lritter", s.getDeveloperId());

		UserStory s2 = new UserStory(STORY_ID, UserStory.REJECTED_NAME, TITLE, USER, ACTION, VALUE, null, null, REJECTION_REASON);
		assertThrows(UnsupportedOperationException.class, () -> s2.update(new Command(Command.CommandValue.BACKLOG, "Low")));
		s2.update(new Command(Command.CommandValue.RESUBMIT, null));
		assertEquals(UserStory.SUBMITTED_NAME, s2.getState());

		UserStory s3 = new UserStory(STORY_ID, UserStory.SUBMITTED_NAME, TITLE, USER, ACTION, VALUE, null, null, null);
		assertThrows(UnsupportedOperationException.class, () -> s3.update(new Command(Command.CommandValue.ASSIGN, "jrpowel9")));
		s3.update(new Command(Command.CommandValue.BACKLOG, "High"));
		assertEquals(UserStory.HIGH_PRIORITY, s3.getPriority());

		UserStory s4 = new UserStory(STORY_ID, UserStory.BACKLOG_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, null, null);
		assertThrows(UnsupportedOperationException.class, () -> s4.update(new Command(Command.CommandValue.CONFIRM, null)));
		s4.update(new Command(Command.CommandValue.ASSIGN, "jrpowel9"));
		assertEquals(s4.getDeveloperId(), "jrpowel9");

		UserStory s5 = new UserStory(STORY_ID, UserStory.COMPLETED_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		assertThrows(UnsupportedOperationException.class, () -> s5.update(new Command(Command.CommandValue.REJECT, "Duplicate")));
		s5.update(new Command(Command.CommandValue.REOPEN, null));
		assertEquals(UserStory.WORKING_NAME, s5.getState());

		UserStory s6 = new UserStory(STORY_ID, UserStory.VERIFYING_NAME, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		assertThrows(UnsupportedOperationException.class, () -> s6.update(new Command(Command.CommandValue.BACKLOG, "Medium")));
		s6.update(new Command(Command.CommandValue.CONFIRM, null));
		assertEquals(UserStory.COMPLETED_NAME, s6.getState());
	}

	/**
	 * Method tests that projects are correctly updated from the WorkingState to other states
	 */
	@Test
	public void testWorkingState() {
		UserStory s = new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		s.update(new Command(Command.CommandValue.REJECT, "Inappropriate"));
		assertEquals(UserStory.REJECTED_NAME, s.getState());

		UserStory s2 = new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, null);
		s2.update(new Command(Command.CommandValue.REVIEW, null));
		assertEquals(UserStory.VERIFYING_NAME, s2.getState());
	}

	/**
	 * Method tests transitions from rejected state to other states
	 */
	@Test
	public void testRejectedState() {
		UserStory s2 = new UserStory(STORY_ID, UserStory.REJECTED_NAME, TITLE, USER, ACTION, VALUE, null, null, REJECTION_REASON);
		assertThrows(UnsupportedOperationException.class, () -> s2.update(new Command(Command.CommandValue.CONFIRM, null)));
		assertEquals(UserStory.REJECTED_NAME, s2.getState());
		assertThrows(UnsupportedOperationException.class, () -> s2.update(new Command(Command.CommandValue.ASSIGN, "jrpowel9")));
		assertEquals(UserStory.REJECTED_NAME, s2.getState());
		assertThrows(UnsupportedOperationException.class, () -> s2.update(new Command(Command.CommandValue.REJECT, "Duplicate")));
		assertEquals(UserStory.REJECTED_NAME, s2.getState());
		assertThrows(UnsupportedOperationException.class, () -> s2.update(new Command(Command.CommandValue.REVIEW, null)));
		assertEquals(UserStory.REJECTED_NAME, s2.getState());
		assertThrows(UnsupportedOperationException.class, () -> s2.update(new Command(Command.CommandValue.REOPEN, null)));
		assertEquals(UserStory.REJECTED_NAME, s2.getState());
	}

	/**
	 * Method tests Verifying state
	 */
	@Test
	public void testVerifyingState() {
		UserStory s3 = new UserStory(STORY_ID, UserStory.SUBMITTED_NAME, TITLE, USER, ACTION, VALUE, null, null, null);
		assertEquals(UserStory.SUBMITTED_NAME, s3.getState());
		s3.update(new Command(Command.CommandValue.REJECT, "Duplicate"));
		assertEquals(UserStory.REJECTED_NAME, s3.getState());
		assertEquals(UserStory.DUPLICATE_REJECTION, s3.getRejectionReason());
		s3.update(new Command(Command.CommandValue.RESUBMIT, null));
		assertEquals(UserStory.SUBMITTED_NAME, s3.getState());
	}

	/**
	 * Method tests Submitted State
	 */
	@Test
	public void testSubmittedState() {
		UserStory s3 = new UserStory(STORY_ID, UserStory.SUBMITTED_NAME, TITLE, USER, ACTION, VALUE, null, null, null);
		assertEquals(UserStory.SUBMITTED_NAME, s3.getState());
		s3.update(new Command(Command.CommandValue.REJECT, "Duplicate"));
		assertEquals(UserStory.REJECTED_NAME, s3.getState());
		assertEquals(UserStory.DUPLICATE_REJECTION, s3.getRejectionReason());
		s3.update(new Command(Command.CommandValue.RESUBMIT, null));
		assertEquals(UserStory.SUBMITTED_NAME, s3.getState());

		UserStory s2 = new UserStory(STORY_ID, UserStory.SUBMITTED_NAME, TITLE, USER, ACTION, VALUE, null, null, null);
		s2.update(new Command(Command.CommandValue.REJECT, "Inappropriate"));
		assertEquals(UserStory.REJECTED_NAME, s2.getState());
		assertEquals(UserStory.INAPPROPRIATE_REJECTION, s2.getRejectionReason());
		s2.update(new Command(Command.CommandValue.RESUBMIT, null));
		assertEquals(UserStory.SUBMITTED_NAME, s2.getState());
	}

	/**
	 * Method tests transitions from backlog state
	 */
	@Test
	public void testBacklogState() {
		UserStory s3 = new UserStory(STORY_ID, UserStory.SUBMITTED_NAME, TITLE, USER, ACTION, VALUE, null, null, null);
		assertEquals(UserStory.SUBMITTED_NAME, s3.getState());
		s3.update(new Command(Command.CommandValue.BACKLOG, "Medium"));
		assertEquals(UserStory.BACKLOG_NAME, s3.getState());
		assertEquals(UserStory.MEDIUM_PRIORITY, s3.getPriority());
		s3.update(new Command(Command.CommandValue.REJECT, "Duplicate"));
		assertEquals(UserStory.REJECTED_NAME, s3.getState());

	}

	/**
	 * Tests an invalid constructor
	 */
	@Test
	public void testInvalidConstructor() {
		assertThrows(IllegalArgumentException.class, () ->  new UserStory(STORY_ID, STATE, TITLE, USER, ACTION, VALUE, PRIORITY, DEVELOPER_ID, REJECTION_REASON));
	}

}
