package by.epamlab;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Elevator implements Runnable {
	private volatile boolean interrupt;
	private final int NUMBER_OF_FLOORS;
	private final int CAPACITY_OF_ELEVATOR;
	private volatile int currentFloor = -1;
	private volatile List<Person> currentPassengers;
	private volatile int direction = 1;
	private volatile House house;
	private volatile int indexElevator;
	private final int SPEED = (new Random()).nextInt(100);
	private volatile boolean isMove;
	
	public Elevator(House house, int indexElevator, int numberOfCapacity) {
		this.indexElevator = indexElevator;
		this.house = house;
		CAPACITY_OF_ELEVATOR = numberOfCapacity;
		NUMBER_OF_FLOORS = house.getNumberOfFloors();
		Random random = new Random();
		currentFloor = random.nextInt(NUMBER_OF_FLOORS);
		direction = random.nextInt(NUMBER_OF_FLOORS) % 2 == 0 ? -1 : 1;
		currentPassengers = new ArrayList<>(NUMBER_OF_FLOORS);
	}
	
	@Override
	public void run() {
		while(!interrupt) {
			synchronized (house) {
				//elevator moving while there are passengers in the home or in an elevator
				if(house.getNumberOfPersons() <= 0 && getCurrentPassengers() == 0){
					try {
						//stop the elevator when there are no persons
						if(isMove) {
							interrupt = true;
							break;
						}
						house.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			elevatorMovement();
		}
		
		System.out.println(this.toString() + " interrupted!!!");
	}
	
	private void elevatorMovement() {
		isMove = true;
		currentFloor += direction;
		
		if(currentFloor <= 0) {
			currentFloor = 0;
		}
		
		if(currentFloor >= NUMBER_OF_FLOORS - 1) {
			currentFloor = NUMBER_OF_FLOORS - 1;
		}
		
		direction = currentFloor == NUMBER_OF_FLOORS - 1 ? -1 : currentFloor == 0 ? 1 : direction;
		System.out.println(this);
		
		//wake passengers up on this elevator
		synchronized (this) {
			this.notifyAll();
		}
		
		//wait until come out
		synchronized (this) {
			try {
				this.wait(SPEED);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		//wake persons up on current floor
		Object floor = house.getFloors().get(currentFloor);
		synchronized (floor) {
			floor.notifyAll();
		}
		
		//wait until come
		synchronized (this) {
			try {
				this.wait(SPEED);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public synchronized String toString() {
		return "elevator (index=" + indexElevator + "; speed=" + SPEED + "; direction=" + direction + ") at " + currentFloor + " floor with " + currentPassengers.size() + " passengers";
	}

	public synchronized int getIndexElevator() {
		return indexElevator;
	}

	public synchronized int getCurrentFloor() {
		return currentFloor;
	}

	public synchronized int getCurrentPassengers() {
		return currentPassengers.size();
	}
	
	public synchronized void addPassenger(Person passenger) {
		currentPassengers.add(passenger);
	}
	
	public synchronized void removePassenger(Person passenger) {
		currentPassengers.remove(passenger);
	}
	
	public synchronized int getCapacity() {
		return CAPACITY_OF_ELEVATOR;
	}
	
	public synchronized int getDirection() {
		return direction;
	}
}
