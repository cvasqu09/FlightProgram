package GraphElements;
import java.util.ArrayList;
/* Using an ArrayList to implement a queue. The queue interface would 
 * probably work too however I needed a way to use insertion sort for both the cost
 * and time variables in the Neighbor class. This is why Collections.sort was not used.
 */
public class APQ {
	private ArrayList<Neighbor> queue;
	//Constructor
	public APQ() {
		queue = new ArrayList<Neighbor>();
	}
	
	//enqueue: Neighbor -> void
	//Adds n to the end of the queue
	public void enqueue(Neighbor n){
		queue.add(n);
	}
	
	//dequeue: void -> Neighbor
	//Removes the first element from the queue and returns it if the queue is not emtpy and returns null
	//in the case that the queue is empty.
	public Neighbor dequeue(){
		if(queue.size() == 0){
			System.out.println("The queue is empty");
			return null;
		} else {
			return queue.remove(0);
		}
	}
	
	//find: String -> Neighbor
	//Finds the neighbor whose airport code matches apc and returns that Neighbor if it exists in the
	//queue and returns null if the apc is not found in the queue.
	public Neighbor find(String apc){
		for(int i = 0; i < queue.size(); i++){
			if(queue.get(i).getAPC().equals(apc)){
				return queue.get(i);
			}
		}
		
		return null;
	}
	
	//sortCost: void -> void
	//Uses insertion sort to sort the queue by cost in ascending order.
	public void sortCost(){
		//Insertion sort
		for(int i = 1; i < queue.size(); i++){
			Neighbor key = queue.get(i);
			int j = i - 1;
			while(j >= 0 && key.getCost() < queue.get(j).getCost()){
				//Shift over
				queue.set(j + 1, queue.get(j));
				j--;
			}
			queue.set(j + 1, key);
		}
	}
	
	//sortTime: void -> void
	//Uses insertion sort to sort the queue by time in ascending order.
	public void sortTime(){
		//Insertion sort
		for(int i = 1; i < queue.size(); i++){
			Neighbor key = queue.get(i);
			int j = i - 1;
			while(j >= 0 && key.getTime() < queue.get(j).getTime()){
				//Shift over
				queue.set(j + 1, queue.get(j));
				j--;
			}
			queue.set(j + 1, key);
		}
	}
	
	//printQueue: void -> void
	//Prints out all the elements of the queue.
	public void printQueue(){
		System.out.println("Printing APQ");
		for(int i = 0; i < queue.size(); i++){
			Neighbor current = queue.get(i);
			System.out.print(current.getAPC() + " cost: " + current.getCost()
					+ " time: " + current.getTime());
			System.out.println();
		}
	}
	
	//getSize: void -> int
	//Returns the number of elements in the queue.
	public int getSize(){
		return queue.size();
	}
	
	//isEmpty: void -> boolean
	//Returns true if the queue is empty and false otherwise.
	public boolean isEmpty(){
		return queue.size() == 0;
	}

}
