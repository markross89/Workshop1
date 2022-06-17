package pl.coderslab;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class TaskManager {
	
	
	static Path path = Paths.get("/home/marek/Workshop1/TaskManager/src/main/java/pl/coderslab/tasks.csv");
	static String[][] tasks = null;
	
	public static void main (String[] args) {
		
		menu();
	}
	
	public static void menu () {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println(ConsoleColors.BLUE+"Please enter one of listed option and press ENTER:"+ConsoleColors.RESET);
		String[] options = {"add", "remove", "list", "exit"};
		
		for (String i : options) {
			System.out.println(i);
		}
		while (true) {
			
			switch (scanner.nextLine()) {
				
				case "list":
					try {
						print(readList(path));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				case "add":
					try {
						add();
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
				case "remove":
					try {
						remove(readList(path));
					} catch (IOException e) {
						throw new RuntimeException(e);
					}
					break;
				case "exit":
					exit();
					break;
			}
			
		}
		
	}
	
	public static String[][] readList (Path path) throws IOException {
		
		File file = new File(String.valueOf(path));
		if (Files.exists(path) && file.length() != 0) {
			
			List<String> linesOfTasks = new ArrayList<>();
			linesOfTasks = Files.readAllLines(path);
			
			tasks = new String[linesOfTasks.size()][linesOfTasks.get(0).split(",").length];
			
			String[] allElementsOfTasks = new String[Files.readString(path).split(",").length];
			allElementsOfTasks = Files.readString(path).split("[,\n]");
			
			int counter = 0;
			for (int i = 0; i<tasks.length; i++) {
				for (int j = 0; j<linesOfTasks.get(0).split(",").length; j++) {
					tasks[i][j] = allElementsOfTasks[counter];
					counter++;
				}
			}
			return tasks;
		}
		else {
			System.out.println();
			System.out.println("file is empty or do not exist / try add tasks to the list");
			System.out.println();
			menu();
		}
		return tasks = null;
		
	}
	
	public static void add () throws IOException {
		
		Scanner scanner = new Scanner(System.in);
		StringBuilder sb = new StringBuilder();
		int counter = 0;
		System.out.println();
		System.out.println("press ENTER to start adding new task / type QUIT to quit");
		while (true) {
			
			if ("quit".equalsIgnoreCase(scanner.nextLine())) {
				System.out.println();
				menu();
			}
			else {
				System.out.println("enter description");
				sb.append(scanner.nextLine()).append(", ");
				System.out.println("enter date");
				sb.append(scanner.nextLine()).append(", ");
				System.out.println("enter TRUE if important / FALSE if less important");
				sb.append(scanner.nextLine()+"\n");
				
				counter++;
				
				Files.writeString(path, sb.toString(), StandardOpenOption.APPEND);
				sb.setLength(0);
			}
			System.out.println("do u want to continue / press ENTER for yes and type QUIT to quit");
			
		}
	}
	
	
	public static void print (String[][] tasks) {
		
		System.out.println("List");
		int number = 1;
		for (int i = 0; i<tasks.length; i++) {
			System.out.println();
			System.out.print(number+" : ");
			for (int j = 0; j<tasks[i].length; j++) {
				System.out.print(tasks[i][j]+" ");
			}
			number++;
		}
		System.out.println();
		System.out.println();
		menu();
	}
	
	public static void remove (String[][] tasks) throws IOException {
		
		Scanner scanner = new Scanner(System.in);
		System.out.println();
		System.out.println("press ENTER to start removing / type QUIT to quit");
		int numberToBeRemoved = 0;
		StringBuilder sb = new StringBuilder();
		while (true) {
			if ("quit".equalsIgnoreCase(scanner.nextLine())) {
				System.out.println();
				menu();
			}
			else {
				if (tasks.length != 0) {
					System.out.println("enter number of task to be removed between 1 - "+tasks.length);
					numberToBeRemoved = scanner.nextInt();
					scanner.nextLine();
					int counter = 0;
					if (numberToBeRemoved<1 || numberToBeRemoved>tasks.length) {
						System.out.println("enter correct number between 1 - "+tasks.length);
						remove(readList(path));
					}
					else {
						
						for (int i = numberToBeRemoved; i<tasks.length; i++) {
							for (int j = 0; j<tasks[counter].length; j++) {
								tasks[i-1][j] = tasks[i][j];
							}
							counter++;
						}
						tasks = Arrays.copyOf(tasks, tasks.length-1);
					}
				}
				else if (tasks.length == 0) {
					System.out.println("there is no more tasks to be removed");
					System.out.println();
					menu();
				}
			}
			
			for (int i = 0; i<tasks.length; i++) {
				for (int j = 0; j<tasks[i].length; j++) {
					sb.append(tasks[i][j]).append(" ");
				}
				sb.append("\n");
			}
			Files.writeString(path, sb.toString(), StandardOpenOption.TRUNCATE_EXISTING);
			sb.setLength(0);
			System.out.println("task nr "+numberToBeRemoved+" was successfully removed \ndo u want to continue? / press ENTER for yes and type "+
					"QUIT "+
					"to quit");
		}
	}
	
	public static void exit () {
		
		System.out.println();
		System.out.println(ConsoleColors.RED+"Bye, bye."+ConsoleColors.RESET);
		System.exit(0);
	}
}