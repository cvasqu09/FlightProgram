package GraphElements;
import java.util.ArrayList;

public class Vertex implements Comparable<Vertex>{
	private String APC;
	private ArrayList<Neighbor> neighbors;
	private Edge bestTimeEdge;
	private int bestTimeValue;
	private final int MAX = 10000;
	private Edge bestCostEdge;
	private int bestCostValue;
	
	//Constructor
	public Vertex(String APC) {
		this.APC = APC;
		this.neighbors = new ArrayList<Neighbor>();
		bestTimeEdge = new Edge("", "", MAX, MAX, MAX);
		bestTimeValue = MAX;
		bestCostEdge = new Edge("", "", MAX, MAX, MAX);
		bestCostValue = MAX;
	}
	
	//Implement the comparable interface to allow for sorting
	@Override
	public int compareTo(Vertex v) {
		return APC.compareTo(v.getAPC());
	}
	
	//addNeighbor: String -> void
	//Adds neighbor to the list of neighbors for a given vertex and does nothing
	//if the neighbor already exists.
	public void addNeighbor(String neighbor){
		for(int i = 0; i < neighbors.size(); i++){
			//Neighbor already exists
			if(neighbors.get(i).equals(neighbor)){
				return;
			}
		}
		System.out.println(APC + " new neighbor: " + neighbor);
		neighbors.add(new Neighbor(neighbor));
	}
	
	//printNeighbors: void -> void
	//Prints out all the neighbors for a given vertex.
	public void printNeighbors(){
		System.out.println("Printing Neighbors for " + this.getAPC() + ": ");
		for(int i = 0; i < neighbors.size(); i++){
			System.out.println(neighbors.get(i).getAPC());
		}
		System.out.println("Done printing neighbors");
		System.out.println();
	}
	
	//Getters and setters for Vertex fields
	public String getAPC(){
		return APC;
	}
	
	public Edge getBestTimeEdge(){
		return bestTimeEdge;
	}
	
	public void setBestTimeEdge(Edge e){
		bestTimeEdge = e;
	}
	
	public int getBestTimeValue(){
		return bestTimeValue;
	}
	
	public void setBestTimeValue(int val){
		bestTimeValue = val;
	}
	
	public Edge getBestCostEdge(){
		return bestCostEdge;
	}
	
	public void setBestCostEdge(Edge e){
		bestCostEdge = e;
	}
	
	public int getBestCostValue(){
		return bestCostValue;
	}
	
	public void setBestCostValue(int value){
		bestCostValue = value;
	}
	
	public ArrayList<Neighbor> getNeighbors(){
		return neighbors;
	}
	
}
