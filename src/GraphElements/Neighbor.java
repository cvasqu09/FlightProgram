package GraphElements;

public class Neighbor {
	private String APC;
	private int cost;
	private int time;
	private final int MAX = 10000000; 
	
	//Constructor
	public Neighbor(String apc, int cost, int time) {
		APC = apc;
		this.cost = cost;
		this.time = time;
	}
	
	//Constructor
	public Neighbor(String apc){
		APC = apc;
		cost = MAX;
		time = MAX;
	}
	
	//Constructor
	public Neighbor(){
		APC = null;
		cost = MAX;
		time = MAX;
	}
	
	//Getters and Setters for Neighbor fields
	public String getAPC(){
		return APC;
	}
	
	public int getCost(){
		return cost;
	}
	
	public int getTime(){
		return time;
	}
	
	public void setAPC(String apc){
		APC = apc;
	}
	
	public void setCost(int newCost){
		cost = newCost;
	}
	
	public void setTime(int newTime){
		time = newTime;
	}
	
}
