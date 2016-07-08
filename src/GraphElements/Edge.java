package GraphElements;

public class Edge {
	private String src;
	private String dest;
	private int cost;
	private int startTime;
	private int endTime;
	
	//Constructor
	public Edge(String from, String to, int cost, int start, int end) {
		this.src = from;
		this.dest = to;
		this.cost = cost;
		this.startTime = start;
		this.endTime = end;
	}
	
	//Getters and setters for Edge fields
	public String getSource(){
		return src;
	}
	
	public String getDest(){
		return dest;
	}
	
	public int getCost(){
		return cost;
	}
	
	public int getStartTime(){
		return startTime;
	}
	
	public int getEndTime(){
		return endTime;
	}
	
}
