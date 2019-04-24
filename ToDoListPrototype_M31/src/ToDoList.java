//Libraries
import java.util.ArrayList;
import java.util.Collections;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ToDoList {
	ArrayList<Task> currentTasks;
	ArrayList<Task> completedTasks;
	ArrayList<Task> deletedTasks;

	/**
	 * Constructor with no parameters. Initializes 3 ArrayLists
	 */
	ToDoList() {
		this.currentTasks = new ArrayList<Task>(); //in progress or not started
		this.completedTasks = new ArrayList<Task>(); //completed
		this.deletedTasks = new ArrayList<Task>(); //deleted
	}



	public ArrayList<Task> getCurrentTasks(){
		return currentTasks;
	}

	public ArrayList<Task> getDeletedTasks(){
		return deletedTasks;
	}

	public ArrayList<Task> getCompletedTasks(){
		return completedTasks;
	}

	public Task getTask(int index) {
		return currentTasks.get(index);
	}



	/**
	 * <p>
	 * Adds a Task to the list. First checks to see if the description is unique
	 * meaning that it is not already in the list. If unique, it returns true.
	 * If not unique, the Task is not added and the method returns false.
	 * </p>
	 * @param Task	-	task object to add to the ArrayList.
	 * @author Tanner Cooper
	 */
	public boolean addTaskToList(Task newTask) {
		System.out.println("adding task to list...");
		boolean uniqueDescription = checkUniqueDescription(currentTasks, newTask);
		sortPrio(currentTasks);
		int count = 1;
		for(int i=0; i < currentTasks.size(); i++) {
			if(currentTasks.get(i).getPriority()==count) {
				count++;
			}
		}
		newTask.setPriority(count);

		//duplicate description or priority returns false - failed to add
		if(uniqueDescription) {
			fixPriorities(newTask);
			currentTasks.add(newTask);
			return true;
		}
		//no duplicate - Task is added to list
		else if(!uniqueDescription) {
			return false;

		}
		return false;
	}
	
	/**
	 * <p>
	 * Sets a Task status to "In progress". Sets the start date and time. 
	 * </p>
	 * @param Task	-	Task object to modify.
	 * @return boolean	- true if completed, false if the Task does not exist in the currentTasks ArrayList.
	 * @author Ramon West
	 */
	public boolean startTask(Task task) {
		// change status to 'in progress'
		System.out.println("setting task status to complete...");
		boolean taskDoesNotExist = checkUniqueDescription(currentTasks, task);
		//if the task does not exist - nothing is changed
		if(taskDoesNotExist) {
			return false;
		}
		//if the task exists - change status to IN_PROGRESS
		else if(!taskDoesNotExist) {
			task.status = Status.IN_PROGRESS;
			task.setStartDate();
			return true;
		}
		return false;
	}
	
	/**
	 * <p>
	 * Sets a Task status to complete. Sets the finish date and time.  Adds Task to completedTasks ArrayList. Removes Task from currentTasks ArrayList.
	 * </p>
	 * @param Task	-	Task object to be set to complete.
	 * @return boolean	- true if completed, false if the Task does not exist.
	 * @author Ramon West
	 */
	public boolean completeTask(Task task) {
		// change status to complete, remove from toDolist, add to complete list.
		System.out.println("setting task status to complete...");
		boolean taskDoesNotExist = checkUniqueDescription(currentTasks, task);
		//if the task does not exist or if the task has not been started - nothing is removed from 'current' list
		if(taskDoesNotExist || task.status != Status.IN_PROGRESS) {
			return false;
		}
		//if the task exists and has been started - set status to COMPLETE, set finishDate, add to 'completed' task list, and finally remove from 'current' task list.
		else if(!taskDoesNotExist && task.status == Status.IN_PROGRESS) {
			task.status = Status.COMPLETE;
			task.setFinishDate();
			completedTasks.add(task);
			currentTasks.remove(task);
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * Checks to see if the task exists. Adds task to deletedTasks list, then removes the task from currentTasks list.
	 * </p>
	 * @param Task	-	task object to search for in the ArrayList and remove.
	 * @author Ramon West
	 */
	public boolean deleteTask(Task task) {
		// delete task from list.
		System.out.println("deleting task from list...");
		boolean taskDoesNotExist = checkUniqueDescription(currentTasks, task);

		//if the task does not exist - nothing is removed from 'current' list
		if(taskDoesNotExist) {
			return false;
		}
		//if the task exists - add to 'deleted' list then remove from 'current' list.
		else if(!taskDoesNotExist) {
			deletedTasks.add(task);
			currentTasks.remove(task);
			return true;

		}
		return false;
	}

	/**
	 * <p>
	 * Takes an input Task that will replace the Task at the input index.
	 * Returns true if the Task is successfully changed.
	 * Returns false if the Task is not modified, usually due to an incorrect
	 * index or the new description is one that is already in the list.
	 * </p>
	 * @param Task	-	takes in a Task object.
	 * @param index	-	takes in an int value representing the index of Task in the ArrayList.
	 * @author Tanner Cooper
	 */
	public boolean changeTask(Task newTask, int index) {
		// edit / replace task from list.
		System.out.println("changing task...");
		boolean uniqueDescription = checkUniqueDescription(currentTasks, newTask);

		if(uniqueDescription) {
			fixPriorities(newTask);
			currentTasks.set(index, newTask);
			return true;
		}

		//For when the description is the same but the other options might not be
		else if(newTask.getDescription().equals(currentTasks.get(index).getDescription())
				&& (newTask.getPriority()!=currentTasks.get(index).getPriority()
				|| !newTask.getDueDate().equals(currentTasks.get(index).getDueDate())))
		{
			fixPriorities(newTask);
			currentTasks.set(index, newTask);
			return true;
		}
		return false;
	}

	/**
	 * <p>
	 * Searches the list of current Tasks for the input description.
	 * Because descriptions are unique, this is all that is needed to
	 * search for a Task. Returns the index of the task if found.
	 * If not found, returns -1.
	 * </p>
	 * @param String description	-	String type Task description.
	 * @return int index	-	index of the Task, if found. If not found, index = -1.
	 * @author Tanner Cooper
	 */
	public int getIndexOfTask(String description) {
		for(int index = 0; index < currentTasks.size(); index++) {
			if((currentTasks.get(index).getDescription()).equals(description)) {
				return index;
			}
		}
		return -1;
	}

	/**
	 * <p>
	 * Checks if the Task description is unique.
	 * </p>
	 * @param ArrayList list	-	ArrayList of type 'Task.'
	 * @param Task	task			-	Task object.
	 * @return boolean	 -	Returns true if the Task description field is unique in the ArrayList. Returns false if the description exists.
	 * @author Ramon West
	 */
	private boolean checkUniqueDescription(ArrayList<Task> list, Task task) {
		boolean unique = true;
		for(int index = 0; index < list.size(); index++) {
			if((list.get(index).getDescription()).equals(task.getDescription())) {
				unique = false;
			}
		}
		if(unique) {
			 return true;
		}
		else if (!unique) {
			return false;
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if the Task priority is unique.
	 * </p>
	 * @param ArrayList list	-	ArrayList of type 'Task.'
	 * @param Task	task			-	Task object.
	 * @return boolean	 -	Returns true if the Task priority field is unique in the ArrayList. Returns false if the priority is taken.
	 * @author Ramon West
	 */
	private Task checkUniquePriority(ArrayList<Task> list, Task task) {
		boolean unique = true;
		Task duplicate = null;
		for(int index = 0; index < list.size(); index++) {
			if((list.get(index).getPriority()) == (task.getPriority())) {
				unique = false;
				duplicate = list.get(index);
			}
		}
		if(unique) {
			 return null;
		}
		else if (!unique) {
			return duplicate;
		}
		return null;
	}
	
	public void fixPriorities(Task task) {
		Task duplicatePrio = checkUniquePriority(currentTasks, task);
		if(duplicatePrio==null) {
			return;
		}
		else if(duplicatePrio!=task){
			int newPrio = duplicatePrio.getPriority() + 1;
			duplicatePrio.setPriority(newPrio);
			fixPriorities(duplicatePrio);
		}
	}

	
	/**
	 * <p>
	 * Sorts input array list by priority
	 * </p>
	 * @param ArrayList list	-	ArrayList of type 'Task.'
	 * @author Tanner Cooper
	 */
	public void sortPrio(ArrayList<Task> list) {
		Collections.sort(list);
	}
	
	/**
	 * <p>
	 * Prints report to Report.txt. Parses through the currentTasks, completedTasks, and deletedTasks ArrayLists to print each task and its properties.
	 * Current tasks will print description, start date, due date, and priority.
	 * Completed tasks will print description, start date, due date, and finish date.
	 * Deleted tasks will print description, start date, due date, and finish date.
	 * </p>
	 * @author Ramon West
	 * @throws FileNotFoundException
	 */
	public void printReport() throws FileNotFoundException {
		// print all content and status to a file.
		// requires File I/O.
		sortPrio(currentTasks);
		sortPrio(completedTasks);
		sortPrio(deletedTasks);
		System.out.println("printing list to report...");
		try (PrintWriter out = new PrintWriter("Report.txt")) {
		    out.println("***********************************************************************************************************");
			out.println("\nCurrent To Do List:");
			out.println("***********************************************************************************************************");
		    for (int index = 0; index < currentTasks.size(); index++) {
		    	out.println("|Description: " + currentTasks.get(index).getDescription());
		    	out.println("| Start Date: " + currentTasks.get(index).getStartDate());
		    	out.println("| Due Date: " + currentTasks.get(index).getDueDate());
		    	out.println("| Priority: " + currentTasks.get(index).getPriority());
		    	out.println("-----------------------------------------------------------------------------------------------------------");
		    }
		    out.println("***********************************************************************************************************");
		    out.println("\nCompleted Tasks:");
		    for (int index = 0; index < completedTasks.size(); index++) {
		    	out.println("| Description: " + completedTasks.get(index).getDescription());
		    	out.println("| Start Date: " + completedTasks.get(index).getStartDate());
		   		out.println("| Due Date: " + completedTasks.get(index).getDueDate());
		   		out.println("| Finish Date: " + completedTasks.get(index).getFinishDate());
		    	out.println("-----------------------------------------------------------------------------------------------------------");
		    }

		    out.println("***********************************************************************************************************");
		    out.println("\nDeleted Tasks:");
		    for (int index = 0; index < deletedTasks.size(); index++) {
		    	out.println("|Description: " + deletedTasks.get(index).getDescription());
		    	out.println("| Start Date: " + deletedTasks.get(index).getStartDate());
		    	out.println("| Due Date: " + deletedTasks.get(index).getDueDate());
		    	out.println("| Finish Date: " + deletedTasks.get(index).getFinishDate());
		    	out.println("-----------------------------------------------------------------------------------------------------------");
		    }
		    out.println("***********************************************************************************************************");
		    out.close();
		}
	}

	void save() {
		// format the list and save to file.
		// requires File I/O.
		System.out.println("saving list...");
		File file = new File("DoNotDelteIAmYourPrecious.txt");

		try {
		if(!file.exists()) {
				file.createNewFile();
			}

		PrintWriter pw = new PrintWriter(file);
		pw.println("Current Taks: ");
		for(int iterator=0; iterator < currentTasks.size(); iterator++) {
			pw.println(iterator);
		}

		pw.println("Completed Taks: ");
		for(int iterator=0; iterator < completedTasks.size(); iterator++) {
			pw.println(iterator);
		}

		pw.println("Deleted Taks: ");
		for(int iterator=0; iterator < deletedTasks.size(); iterator++) {
			pw.println(iterator);  
		}
		pw.close();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				System.out.println("saving list...");
	}


	void restore() {
		// read from file to restore previous session.
		// requires File I/O.
		System.out.println("restoring list...");
	}
	/*
	 * Prints the description, due date, and priority of all elements in currentTasks
	 */
	void testPrint() {
		System.out.println();
		for(int index = 0; index < currentTasks.size(); index ++) {
			System.out.print(currentTasks.get(index).getDescription());
			System.out.print("\t" + currentTasks.get(index).getDueDate());
			System.out.print("\t" + currentTasks.get(index).getPriority());
			System.out.println();
		}
	}
}
