package by.epamlab;

import java.util.ArrayList;
import java.util.List;

public class House {
	private List<Elevator> elevators;
	private List<Person> persons;
	private List<Object> floors;
	
	public House (int numberOfFloors, int numberOfElevators, int numberOfCapacity, int numberOfPersons) {
		elevators = new ArrayList<>(numberOfElevators);
		floors = new ArrayList<>(numberOfFloors);
		persons = new ArrayList<>(numberOfPersons);

		for(int index = 0; index < numberOfFloors; index++) {
			floors.add(new Object());
		}
		
		for(int index = 0; index < numberOfElevators; index++) {
			Elevator elevator = new Elevator(this, index, numberOfCapacity);
			elevators.add(elevator);
			(new Thread(elevator)).start();
		}
		
		for(int index = 0; index < numberOfPersons; index++) {
			Person person = new Person(this);
			persons.add(person);
			(new Thread(person)).start();
		}
	}
	
	public synchronized List<Elevator> getElevators() {
		return elevators;
	}
	
	public synchronized int getNumberOfElevators() {
		return elevators.size();
	}

	public synchronized List<Object> getFloors() {
		return floors;
	}
	
	public synchronized int getNumberOfFloors() {
		return floors.size();
	}
	
	public synchronized List<Person> getPersons() {
		return persons;
	}
	
	public synchronized int getNumberOfPersons() {
		return persons.size();
	}
	
	public synchronized void removePerson(Person person) {
		persons.remove(person);
	}
}
