/**
 * 
 */
package edu.ncsu.csc216.requirements_manager.model.command;

/**
 * Class stores values about state of an idea and can be interacted with by the UserStory class to 
 * fetch this data.
 * @author jeff
 *
 */
public class Command {

	/**
	 * Enum is used to keep track of possible states of a story because of a limited number of options 
	 */
	public enum CommandValue {
		/** Moves a story to the backlog state */
		BACKLOG,
		/** Assigns a story to a developer and moves story to the working state or reassigns to a different developer */
		ASSIGN,
		/** Moves a story from working state to verifying state */
		REVIEW,
		/** Moves a story to the completed state from verifying state */
		CONFIRM, 
		/** Moves a story from working state to backlog state */
		REOPEN,
		/** Moves a story to the Rejected state */
		REJECT,
		/** Moves story from working state to backlog state */
		RESUBMIT
	}

	/** Instance keeps track of the current state of a user story */
	private CommandValue command;
	/**
	 * String keeps track of any information associated with a string such as
	 * rejection information
	 */
	private String commandInformation;

	/**
	 * Method constructs a Command to be attached to an idea
	 * 
	 * @param commandValue Member of an enum that keeps track of all possible states
	 * @param commandInfo  String that keeps track of any extraneous information
	 *                     that cannot be conveyed in the name of a state
	 */
	public Command(CommandValue commandValue, String commandInfo) {
		setCommandInformation(commandInfo);
		setCommand(commandValue);
	}

	/**
	 * Method returns the current value of command
	 * @return Current value of command
	 */
	public CommandValue getCommand() {
		return command;
	}

	/**
	 * Method sets value of command. Fails if command's value is null, or if a string is associated
	 * with a command of type Review, Confirm, Reopen, or Resubmit. Also fails if no string is associated
	 * with command of type Backlog, Assign, or Reject as these commands must have details associated.
	 * @param command Command to be set as the current value
	 * @throws IllegalArgumentException if me if command's value is null, or if a string is associated
	 * with a command of type Review, Confirm, Reopen, or Resubmit. Also fails if no string is associated
	 * with command of type Backlog, Assign, or Reject as these commands must have details associated.
	 */
	public void setCommand(CommandValue command) {
		if (command == null) {
			throw new IllegalArgumentException("Null command supplied.");
		}
		if (("".equals(this.commandInformation) || this.commandInformation == null) &&
			(command == CommandValue.BACKLOG || command == CommandValue.ASSIGN
					|| command == CommandValue.REJECT)) {
				throw new IllegalArgumentException("Additional information necessary");
			}
		if (this.commandInformation != null && (command == CommandValue.REVIEW || command == CommandValue.CONFIRM
					|| command == CommandValue.REOPEN || command == CommandValue.RESUBMIT)) {
				throw new IllegalArgumentException("Unnecessary information provided.");
			}

		this.command = command;
	}

	/**
	 * Method returns command information
	 * @return additional details associated with a command 
	 */
	public String getCommandInformation() {
		return commandInformation;
	}

	/**
	 * Method sets command information string
	 * @param commandInformation Extra details associated with a command of type backlog, assign, and reject.
	 */
	public void setCommandInformation(String commandInformation) {
		this.commandInformation = commandInformation;
	}

}
