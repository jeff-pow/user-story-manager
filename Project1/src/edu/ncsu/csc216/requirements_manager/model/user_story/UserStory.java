package edu.ncsu.csc216.requirements_manager.model.user_story;

import edu.ncsu.csc216.requirements_manager.model.command.Command;

/**
 * Method stores all information related to a UserStory and its state. Stored
 * in an ArrayList in the project class.
 * @author jeff
 *
 */
public class UserStory {
    /** Unique id of a story. Distinct from its index in the ArrayList in project. */
    private int storyId;
    /** Title of a story. Cannot be null or empty */
    private String title;
    /** Name of user who created story. Cannot be null or empty */
    private String user;
    /** Name of action user wants to accomplish. Cannot be null or empty */
    private String action;
    /** Reason user wants to be able to do action. Cannot be null or empty */
    private String value;
    /** String to keep track of the priority of an idea. Will be High, Medium, or Low */
    private String priority;
    /** ID of the developer working on the story */
    private String developerId;
    /** Reason the story was rejected if the rejected state is applied */
    private String rejectionReason;
    /** Constant to apply Submitted state */
    public static final String SUBMITTED_NAME = "Submitted";
    /** Constant to apply Backlog state */
    public static final String BACKLOG_NAME = "Backlog";
    /** Constant to apply Working state */
    public static final String WORKING_NAME = "Working";
    /** Constant to apply Verifying state */
    public static final String VERIFYING_NAME = "Verifying";
    /** Constant to apply Completed state */
    public static final String COMPLETED_NAME = "Completed";
    /** Constant to apply Rejected state */
    public static final String REJECTED_NAME = "Rejected";
    /** Constant to apply High priority */
    public static final String HIGH_PRIORITY = "High";
    /** Constant to apply Medium priority */
    public static final String MEDIUM_PRIORITY = "Medium";
    /** Constant to apply Low priority */
    public static final String LOW_PRIORITY = "Low";
    /** Constant to apply Duplicate rejection reason */
    public static final String DUPLICATE_REJECTION = "Duplicate";
    /** Constant to apply Inappropriate rejection reason if a story is deemed inappropriate for the requirements of the program */
    public static final String INAPPROPRIATE_REJECTION = "Inappropriate";
    /** Constant to apply Infeasible rejection reason if a story is deemed infeasible for the requirements of the program */
    public static final String INFEASIBLE_REJECTION = "Infeasible";
    /** Field that is synced across all instances of UserStory in a project and keeps track of the highest ID story. */
    private static int counter = 0;
    /** Field stores the current state of a project and is changed whenever the state of a project changes */
    UserStoryState currentState; 
    /** Only one instance of this state is constructed for performance and simplicity of code reasons. When the state of the project changes, 
     * the currentState variable is updated to reflect one of these. */
	public final UserStoryState submittedState = new SubmittedState();
    /** Only one instance of this state is constructed for performance and simplicity of code reasons. When the state of the project changes, 
     * the currentState variable is updated to reflect one of these. */
    public final UserStoryState backlogState = new BacklogState();
    /** Only one instance of this state is constructed for performance and simplicity of code reasons. When the state of the project changes, 
     * the currentState variable is updated to reflect one of these. */
    public final UserStoryState workingState = new WorkingState();
    /** Only one instance of this state is constructed for performance and simplicity of code reasons. When the state of the project changes, 
     * the currentState variable is updated to reflect one of these. */
    public final UserStoryState verifyingState = new VerifyingState();
    /** Only one instance of this state is constructed for performance and simplicity of code reasons. When the state of the project changes, 
     * the currentState variable is updated to reflect one of these. */
    public final UserStoryState completedState = new CompletedState();
    /** Only one instance of this state is constructed for performance and simplicity of code reasons. When the state of the project changes, 
     * the currentState variable is updated to reflect one of these. */
    public final UserStoryState rejectedState = new RejectedState();

    /**
     * Default constructor for controller class to use to create a story. Used if the story already exists and is being modified
     * rather than for newly submitted stories. 
     * @param id ID of the story to be created
     * @param state Current state of the system. Has predefined values from the requirements
     * @param title Title of a story
     * @param user Name of person creating story
     * @param action Action user would like to be able to do
     * @param value Why the user wants to be able to do an action
     * @param priority Urgency of implementing the story
     * @param developerId ID of developer assigned to the story
     * @param rejectionReason Why the project is rejected. Can only be Duplicate, Inappropriate, or Infeasible
     */
    public UserStory(int id, String state, String title, String user, String action, String value, String priority, String developerId,
            String rejectionReason) {
        if (id > counter) {
        	counter = id;
        }
        setStoryId(id);
        setState(state);
        setTitle(title);
        setUser(user);
        setAction(action);
        setValue(value);
        switch (currentState.getStateName()) {
            case SUBMITTED_NAME:
                if (priority != null || developerId != null || rejectionReason != null) {
                    throw new IllegalArgumentException("Additional information provided");
                }
                break;
            case WORKING_NAME:
            case VERIFYING_NAME:
            case COMPLETED_NAME:
                setDeveloperId(developerId);
                setPriority(priority);
                if (rejectionReason != null) {
                    throw new IllegalArgumentException("Additional information provided");
                }
                break;
            case BACKLOG_NAME:
                if (developerId != null || rejectionReason != null) {
                    throw new IllegalArgumentException("Additional information provided");
                }
                setPriority(priority);
                break;
            case REJECTED_NAME:
                if (developerId != null || priority != null) {
                    throw new IllegalArgumentException("Additional information provided");
                }
                setRejectionReason(rejectionReason);
                break;
            default:
            	throw new IllegalArgumentException();
        }
    }

    /**
     * Creates a freshly submitted user story. State should default to submitted and no other
     * fields need be populated other than title, user, action, and value.
     * @param title Title of a story
     * @param user Name of person creating story
     * @param action Action user would like to be able to do
     * @param value Why the user wants to be able to do an action
     */
    public UserStory(String title, String user, String action, String value) {
        setTitle(title);
        setUser(user);
        setAction(action);
        setValue(value);
        setStoryId(counter);
        setState(SUBMITTED_NAME);
    }

    /**
     * Interface for states in the UserStory State Pattern. All concrete user story
     * states must implement the UserStoryState interface. The UserStoryState
     * interface should be a private interface of the UserStory class.
     * 
     * @author Dr. Sarah Heckman (sarah_heckman@ncsu.edu)
     */
    private interface UserStoryState {

        /**
         * Update the UserStory based on the given Command. An
         * UnsupportedOperationException is thrown if the Command is not a valid action
         * for the given state.
         * 
         * @param command Command describing the action that will update the UserStory's
         *                state.
         * @throws UnsupportedOperationException if the Command is not a valid action
         *                                       for the given state.
         */
        void updateState(Command command);

        /**
         * Returns the name of the current state as a String.
         * 
         * @return the name of the current state as a String.
         */
        String getStateName();

    }

    private class SubmittedState implements UserStoryState {

         /**
         * Update the UserStory based on the given Command. An
         * UnsupportedOperationException is thrown if the Command is not a valid action
         * for the given state.
         * 
         * @param command Command describing the action that will update the UserStory's
         *                state.
         * @throws UnsupportedOperationException if the Command is not a valid action
         *                                       for the given state.
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.BACKLOG) {
                setPriority(command.getCommandInformation());
                currentState = backlogState;
            }
            else if (command.getCommand() == Command.CommandValue.REJECT) {
                setRejectionReason(command.getCommandInformation());
                currentState = rejectedState;
            }
            else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * Returns the name of the current state as a String.
         * 
         * @return the name of the current state as a String.
         */
        @Override
        public String getStateName() {
            return SUBMITTED_NAME;
        }

    }

    private class BacklogState implements UserStoryState {
    	/**
         * Update the UserStory based on the given Command. An
         * UnsupportedOperationException is thrown if the Command is not a valid action
         * for the given state.
         * 
         * @param command Command describing the action that will update the UserStory's
         *                state.
         * @throws UnsupportedOperationException if the Command is not a valid action
         *                                       for the given state.
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.REJECT) {
                setRejectionReason(command.getCommandInformation());
                priority = null;
                currentState = rejectedState;
            }
            else if (command.getCommand() == Command.CommandValue.ASSIGN) {
                setDeveloperId(command.getCommandInformation());
                currentState = workingState;
            }
            else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * Returns the name of the current state as a String.
         * 
         * @return the name of the current state as a String.
         */
        @Override
        public String getStateName() {
            return BACKLOG_NAME;
        }

    }

    private class WorkingState implements UserStoryState {

    	/**
         * Update the UserStory based on the given Command. An
         * UnsupportedOperationException is thrown if the Command is not a valid action
         * for the given state.
         * 
         * @param command Command describing the action that will update the UserStory's
         *                state.
         * @throws UnsupportedOperationException if the Command is not a valid action
         *                                       for the given state.
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.REJECT) {
                setRejectionReason(command.getCommandInformation());
                developerId = null;
                priority = null;
                currentState = rejectedState;
            }
            else if (command.getCommand() == Command.CommandValue.REOPEN) {
                developerId = null;
                currentState = backlogState;
            }
            else if (command.getCommand() == Command.CommandValue.ASSIGN) {
                setDeveloperId(command.getCommandInformation());
                currentState = workingState;
            }
            else if (command.getCommand() == Command.CommandValue.REVIEW) {
                currentState = verifyingState;
            }
            else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * Returns the name of the current state as a String.
         * 
         * @return the name of the current state as a String.
         */
        @Override
        public String getStateName() {
            return WORKING_NAME;
        }

    }

    private class VerifyingState implements UserStoryState {

    	/**
         * Update the UserStory based on the given Command. An
         * UnsupportedOperationException is thrown if the Command is not a valid action
         * for the given state.
         * 
         * @param command Command describing the action that will update the UserStory's
         *                state.
         * @throws UnsupportedOperationException if the Command is not a valid action
         *                                       for the given state.
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.CONFIRM) {
                currentState = completedState;
            }
            else if (command.getCommand() == Command.CommandValue.REOPEN) {
                currentState = workingState;
            }
            else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * Returns the name of the current state as a String.
         * 
         * @return the name of the current state as a String.
         */
        @Override
        public String getStateName() {
            return VERIFYING_NAME;
        }

    }

    private class CompletedState implements UserStoryState {

    	/**
         * Update the UserStory based on the given Command. An
         * UnsupportedOperationException is thrown if the Command is not a valid action
         * for the given state.
         * 
         * @param command Command describing the action that will update the UserStory's
         *                state.
         * @throws UnsupportedOperationException if the Command is not a valid action
         *                                       for the given state.
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.REOPEN) {
                currentState = workingState;
            }
            else {
                throw new UnsupportedOperationException();
            }
        }

        /**
         * Returns the name of the current state as a String.
         * 
         * @return the name of the current state as a String.
         */
        @Override
        public String getStateName() {
            return COMPLETED_NAME;
        }

    }

    private class RejectedState implements UserStoryState {

    	/**
         * Update the UserStory based on the given Command. An
         * UnsupportedOperationException is thrown if the Command is not a valid action
         * for the given state.
         * 
         * @param command Command describing the action that will update the UserStory's
         *                state.
         * @throws UnsupportedOperationException if the Command is not a valid action
         *                                       for the given state.
         */
        @Override
        public void updateState(Command command) {
            if (command.getCommand() == Command.CommandValue.RESUBMIT) {
                currentState = submittedState;
                rejectionReason = null;
            }
            else {
                throw new UnsupportedOperationException("Invalid transition");
            }
        }

        /**
         * Returns the name of the current state as a String.
         * 
         * @return the name of the current state as a String.
         */
        @Override
        public String getStateName() {
            return REJECTED_NAME;
        }

    }

    private void setState(String state) {
        switch (state) {
            case SUBMITTED_NAME:
                this.currentState = submittedState;
                break;
            case WORKING_NAME:
                this.currentState = workingState;
                break;
            case BACKLOG_NAME:
                this.currentState = backlogState;
                break;
            case VERIFYING_NAME:
                this.currentState = verifyingState;
                break;
            case REJECTED_NAME:
                this.currentState = rejectedState;
                break;
            case COMPLETED_NAME:
                this.currentState = completedState;
                break;
            default:
                throw new IllegalArgumentException("Illegal state.");
        }
    }

    /**
     * Method fetches current state of story as a string
     * @return State of a story as a string
     */
    public String getState() {
        if (this.currentState == submittedState) {
            return SUBMITTED_NAME;
        }
        if (this.currentState == workingState) {
            return WORKING_NAME;
        }
        if (this.currentState == backlogState) {
            return BACKLOG_NAME;
        }
        if (this.currentState == verifyingState) {
            return VERIFYING_NAME;
        }
        if (this.currentState == rejectedState) {
            return REJECTED_NAME;
        }
        if (this.currentState == completedState) {
            return COMPLETED_NAME;
        }
        else { return "Error. Invalid state"; }
    }

    /**
     * Method fetches the numerical ID for a story. Used as a unique identifier
     * @return the storyId as an int
     */
    public int getId() {
        return storyId;
    }

    /**
     * Method sets the id. Must be one higher than the highest other id in project
     * @param storyId the storyId to set
     */
    private void setStoryId(int storyId) {
        this.storyId = storyId;
        incrementCounter();
    }

    /**
     * Returns the title of a story. Cannot be null or empty
     * @return the title as a String
     */
    public String getTitle() {
        return title;
    }

    /**
     * Method sets the title. Cannot be null or empty
     * @param title the title to set as a String
     */
    private void setTitle(String title) {
    	if (title == null || "".equals(title)) {
    		throw new IllegalArgumentException("Null title supplied");
    	}
        this.title = title;
    }

    /**
     * Fetches action user would like to see implemented
     * @return the action as a String
     */
    public String getAction() {
        return action;
    }

    /**
     * Sets the action that a user requests.
     * @param action the action to set as a String
     */
    private void setAction(String action) {
    	if (action == null || "".equals(action)) {
    		throw new IllegalArgumentException("Null action supplied");
    	}
        this.action = action;
    }

    /**
     * Fetches the reason user wants to see an action implemented 
     * @return the value as a String
     */
    public String getValue() {
        return value;
    }

    /**
     * Method sets value. Reason the user wants to see an action implemented
     * @param value the value to set 
     */
    private void setValue(String value) {
    	if (value == null || "".equals(value)) {
    		throw new IllegalArgumentException("Null value supplied");
    	}
        this.value = value;
    }

    /**
     * Method returns the priority of a story
     * @return the priority as a String
     */
    public String getPriority() {
        return priority;
    }

    /**
     * Method sets the priority of a story
     * @param priority the priority to set as a String
     */
    private void setPriority(String priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Null priority");
        }
    	if (priority.equals(HIGH_PRIORITY) || priority.equals(MEDIUM_PRIORITY) || priority.equals(LOW_PRIORITY)) {
			this.priority = priority;
		}
    	else {
    		throw new IllegalArgumentException("Invalid priority");
    	}
    }

    /**
     * Method returns developerID of the developer working on a project
     * @return the developerId of the person working on the project
     */
    public String getDeveloperId() {
        return developerId;
    }

    /**
     * Method sets the developer ID
     * @param developerId the developerId to set
     */
    private void setDeveloperId(String developerId) {
    	if (developerId == null || "".equals(developerId)) {
    		throw new IllegalArgumentException("Invalid developer ID");
    	}
        this.developerId = developerId;
    }

    /**
     * Returns the reason a story was rejected. Can be Inappropriate, Infeasible, or Duplicate.
     * @return the rejectionReason as a String
     */
    public String getRejectionReason() {
        return rejectionReason;
    }

    /**
     * Sets the reason a story was rejected. Can be Inappropriate, Infeasible, or Duplicate.
     * @param rejectionReason the rejectionReason String to set
     */
    private void setRejectionReason(String rejectionReason) {
        if (rejectionReason == null) {
            throw new IllegalArgumentException("Null rejection reason");
        }
    	if (rejectionReason.equals(INFEASIBLE_REJECTION) || rejectionReason.equals(DUPLICATE_REJECTION) || rejectionReason.equals(INAPPROPRIATE_REJECTION)) {
			this.rejectionReason = rejectionReason;
    	}
    	else {
    		throw new IllegalArgumentException("Invalid rejection reason");
    	}
    }

    /**
     * Returns the name of the user who created the story
     * @return the name of the user as a String   
     */
    public String getUser() {
        return user;
    }

    /**
     * Sets the name of the user who created the story
     * @param user the user to set as a String
     */
    public void setUser(String user) {
    	if (user == null || "".equals(user)) {
    		throw new IllegalArgumentException("Null user supplied");
    	}
        this.user = user;
    }

    /**
     * Converts a user story into a String to be printed into a file. Special format based off project requirements
     * @return relevant information about a project in a String format
     */
    public String toString() {
    	String s = "";
		s += "* " + storyId + ",";
		s += currentState.getStateName() + ",";
		s += this.getTitle() + ",";
        switch (currentState.getStateName()) {
            case SUBMITTED_NAME:
                break;
            case WORKING_NAME:
            case VERIFYING_NAME:
            case COMPLETED_NAME:
                s += getPriority() + ",";
                s += getDeveloperId() + ",";
                break;
            case BACKLOG_NAME:
                s += getPriority() + ",";
                break;
            case REJECTED_NAME:
                s += getRejectionReason();
                break;
            default:
            	throw new IllegalArgumentException("Illegal state");
        }
		s += "\n";
		s += "- " + this.getUser() + "\n";
		s += "- " + this.getAction() + "\n";
		s += "- " + this.getValue() + "\n";
    	
    	return s;
    }

    /**
     * Method updates a story's state via a command provided as an argument
     * @param command Command used to update the state of a project and change relevant variables.
     * @throws UnsupportedOperationException if Command would transition a project to an undefined state
     */
    public void update(Command command) {
        currentState.updateState(command);
    }

    /** 
     * Method initiates counter field used to keep track of the highest ID story in the project
     * @param idx Index of the highest id numbered story in the project
     */
    public static void setCounter(int idx) {
    	UserStory.counter = idx;
    }

    /**
     * Method increases counter field by one every time a new user story is made in a project 
     */
    public static void incrementCounter() {
    	UserStory.counter++;
    }

}
