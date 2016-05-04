package by.epamlab;

import java.util.Random;

public class Person implements Runnable {
	private volatile boolean interrupt;
	private volatile int initialFloor;
	private volatile int targetFloor;
	private volatile int indexElevator = -1;
	private volatile House house;
	
	public Person (House house) {
		this.house = house;
		int numberOfFloors = house.getNumberOfFloors();
		Random random = new Random();
		initialFloor = random.nextInt(numberOfFloors);
		targetFloor = initialFloor;
		while(initialFloor == targetFloor) {
			targetFloor = random.nextInt(numberOfFloors);
		}
		
		synchronized (house) {
			house.notifyAll();
		}
		System.out.println(this.toString() + initialFloor);
	}
	
	@Override
	public void run() {
		while(!interrupt) {
			if(indexElevator == -1) {
				//man not in an elevator
				for(int index = 0; index < house.getElevators().size(); index++) {
					Elevator elevator = house.getElevators().get(index);
					synchronized (elevator) {
						//elevator at current floor?
						if(elevator.getCurrentFloor() == initialFloor && 
							elevator.getCurrentPassengers() < elevator.getCapacity()) {
							//elevator goes in the right direction
							boolean enter = elevator.getDirection() * (targetFloor - initialFloor) > 0 ? true : false;
							if(enter) {
								//entered and wait for the elevator movement 
								elevator.addPassenger(this);
								synchronized (house) {
									house.removePerson(this);
								}
								indexElevator = index;
								System.out.println(this.toString() + elevator.getCurrentFloor() + " and entered the elevator(" + elevator.getIndexElevator() + ")");
								try {
									elevator.wait();
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
								break;
							}
						}
					}
				}
				
				//person on floor?
				if(indexElevator == -1) {
					Object floor = house.getFloors().get(initialFloor);
					synchronized (floor) {
						try {
							// waiting for the arrival of the elevator
							floor.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}else
			{
				//person in an elevator
				Elevator elevator = house.getElevators().get(indexElevator);
				synchronized (elevator) {
					int currentFloor = elevator.getCurrentFloor(); 
					//person has arrived on the floor?
					if(currentFloor == targetFloor) {
						elevator.removePassenger(this);
						interrupt = true;
						System.out.println(this.toString() + currentFloor + " and came out of the elevator(" + elevator.getIndexElevator() + ").");
					}else {
						try {
							elevator.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		System.out.println(this.toString() + targetFloor + " - interrupted!!!");
	}

	@Override
	public synchronized String toString() {
		return "Person(from="+initialFloor + "; to=" + targetFloor + ") now at floor ";
	}
}
