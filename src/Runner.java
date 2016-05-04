import java.util.Scanner;

import by.epamlab.House;

public class Runner {

	public static void main(String[] args) {
		Scanner scanner = null;
		try{
			int numberOfFloors = 0;
			int numberOfElevators = 0;
			int numberOfPersons = 0;
			int numberOfCapacity = 0;
			
			scanner = new Scanner(System.in);
			System.out.println("Input number of floors:");
			if(scanner.hasNext()) {
				numberOfFloors = Integer.parseInt(scanner.next().trim());
			}
			if(numberOfFloors < 2) {
				throw new NumberFormatException("Number of floors must be more than one.");
			}
			
			System.out.println("Input number of elevators:");
			if(scanner.hasNext()) {
				numberOfElevators = Integer.parseInt(scanner.next().trim());
			}
			if(numberOfElevators < 1) {
				throw new NumberFormatException("Number of elevators must be one or more.");
			}
			
			System.out.println("Input number of capacity of elevators:");
			if(scanner.hasNext()) {
				numberOfCapacity = Integer.parseInt(scanner.next().trim());
			}
			if(numberOfCapacity < 4) {
				throw new NumberFormatException("Number of capacity of elevator must be four or more.");
			}
			
			System.out.println("Input number of persons:");
			if(scanner.hasNext()) {
				numberOfPersons = Integer.parseInt(scanner.next().trim());
			}
			if(numberOfPersons < 1) {
				throw new NumberFormatException("Number of persons must be one or more.");
			}
			
			new House(numberOfFloors, numberOfElevators, numberOfCapacity, numberOfPersons);
		}catch (NumberFormatException e) {
			System.out.println(e.getMessage() + "\nThe value is not correct");
		}finally{
			if(scanner != null)
				scanner.close();
		}
	}

}
