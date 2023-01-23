package edu.ncsu.csc216.requirements_manager.model.command;

import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests command class
 * 
 * @author jeff
 *
 */
public class CommandTest {
	/**
	 * Tests to make sure commands can properly be set accounting for commandInfo
	 * requirements. Backlog, Assign, and Reject must not have an empty or null
	 * string passed as commandInformation. Remaining states must have a null field
	 * for commandInformation.
	 */
	@Test
	public void testSetCommand() {
		new Command(Command.CommandValue.ASSIGN, "jrpowel9");
		new Command(Command.CommandValue.BACKLOG, "Medium");
		new Command(Command.CommandValue.REJECT, "Duplicate");
		assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.ASSIGN, ""),
				"Value thrown");
		assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.BACKLOG, null),
				"Value thrown");
		assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.REJECT, null),
				"Value thrown");

		new Command(Command.CommandValue.REVIEW, null);
		new Command(Command.CommandValue.CONFIRM, null);
		new Command(Command.CommandValue.REOPEN, null);
		new Command(Command.CommandValue.RESUBMIT, null);
		assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.REVIEW, "str"),
				"Value thrown");
		assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.CONFIRM, "str"),
				"Value thrown");
		assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.REOPEN, "str"),
				"Value thrown");
		assertThrows(IllegalArgumentException.class, () -> new Command(Command.CommandValue.RESUBMIT, "str"),
				"Value thrown");
	}
}
