package GraphElements;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;

public class Graph {
	private ArrayList<Vertex> vertices;
	private ArrayList<Edge> edges;
	
	//Constructor
	public Graph() {
		vertices = new ArrayList<Vertex>();
		edges = new ArrayList<Edge>();
	}
	
	//newFlight: String x String x int x int x int -> void
	//newFlight adds a flight given a source string, destination string, integer for cost,
	//integer for start time, and integer for end time to the graph.
	public void newFlight(String src, String dest, int cost, int start, int end){
		Edge newEdge = new Edge(src, dest, cost, start, end);
		
		//If the edge already exists do not add it
		for(int i = 0; i < edges.size(); i++){
			if(sameSourceAndDest(edges.get(i), newEdge)){
				if(sameData(edges.get(i), newEdge)){
					return;
				}
			}
		}
		
		//Add to the graph otherwise
		addEdgeToGraph(newEdge);
		addVerticesToGraph(src, dest);
				
		//For debugging
		//System.out.println("Printing vertices");
		//printVertices();
		//
		//System.out.println("Printing edges");
		//printEdges();
		
	}
	
	//newEdge: Edge -> void
	//newEdge adds the edge into the edges list if it does not already exist.
	//Note that there may be edges with the same airports to and from but have
	//different costs and times.
	public void newEdge(Edge e){
		for(int i = 0; i < edges.size(); i++){
			//If the edge already exists do not re-add the edge
			if(sameSourceAndDest(edges.get(i), e)){
				if(sameData(edges.get(i), e)){
					return;
				}
			}
		}
		
		//If we didn't find a match then add it to edges following alphabetical 
		//order then start time.
		if(edges.size() == 0){
			edges.add(e);
		} else {
			int i = 0;
			//Advance lexicographically by source
			while(e.getSource().compareTo(edges.get(i).getSource()) > 0){
				i++;
			}
			//Advance by start time
			while(e.getStartTime() > edges.get(i).getStartTime()){
				i++;
			}
			edges.add(i, e);
		}
	}
	
	//cheapest: BufferedWriter x String x String -> void
	//Calculates the cheapest path using Dijikstra's algorithm from the src to dest and writes the shortest path
	//to the buffered writer bw.
	public void cheapest(BufferedWriter bw, String src, String dest) throws IOException{
		APQ q = new APQ();		
		clearBestEdges();
		//Add all vertices to Queue, src vertex will have cost/time = 0 and everything
		//else will have cost/time = MAX
		for(int i = 0; i < vertices.size(); i++){
			if(vertices.get(i).getAPC().equals(src)){
				vertices.get(i).setBestTimeValue(0);
				q.enqueue(new Neighbor(src, 0, 0));
			} else {
				q.enqueue(new Neighbor(vertices.get(i).getAPC()));
			}
		}
		
		//Dijikstra's algorithm
		while(!q.isEmpty()){
			//Sort so that the first element has the lowest weight
			q.sortCost();
			Neighbor currentNeighbor = q.dequeue();
			Vertex currentVertex = getVertex(currentNeighbor.getAPC());
			ArrayList<Edge> adjacentEdges;
			adjacentEdges = getAdjacentEdges(edges, currentNeighbor.getAPC());
			
			//For every neighbor
			for(int i = 0; i < adjacentEdges.size(); i++){
				String target = adjacentEdges.get(i).getDest();
				Neighbor targetNeighbor = q.find(target);
				Vertex targetVertex = getVertex(target);
				Edge currentEdge = adjacentEdges.get(i);
				//If targetNeighbor has already been visited
				if(targetNeighbor == null){
					continue;
				}
				else if(currentEdge.getStartTime() < currentVertex.getBestTimeValue()){
					//If flight current edge's flight starts earlier than current Vertex best time
					continue;
				} else if(currentNeighbor.getCost() + currentEdge.getCost() < targetNeighbor.getCost()){
					//Update weight in queue as well as values for vertex best edges
					targetNeighbor.setCost(currentNeighbor.getCost() + currentEdge.getCost());
					targetNeighbor.setTime(currentNeighbor.getTime() + currentEdge.getEndTime());
					targetVertex.setBestCostEdge(currentEdge);
					targetVertex.setBestCostValue(targetNeighbor.getCost());
					targetVertex.setBestTimeValue(currentEdge.getEndTime());
				}	
			}
			adjacentEdges.clear();
		}
				
		ArrayList<Edge> cheapestItinerary;
		cheapestItinerary = returnCheapestItinerary(vertices, src, dest);
		
		//Write path to take to buffered writer
		bw.write("Cheapest From:" + src + " To:" + dest);
		bw.newLine();
		for(int i = 0; i < cheapestItinerary.size(); i++){
			Edge currentEdge = cheapestItinerary.get(i);
			bw.write("Flight " + currentEdge.getSource() + " " + currentEdge.getDest() +
					" cost:" + currentEdge.getCost() + " start:" + currentEdge.getStartTime() +
					" end:" + currentEdge.getEndTime());
			bw.newLine();
		}
	}
	
	//earliest: BufferedWriter x String x String -> void
	//Calculates the earliest path from src to dest airports using Dijikstra's algorithm
	//and writes the path to take to the buffered writer bw.
	public void earliest(BufferedWriter bw, String src, String dest) throws IOException{
		APQ q = new APQ();		
		clearBestEdges();
		//Add all vertices to Queue, src vertex will have cost/time = 0 and everything
		//else will have cost/time = MAX
		for(int i = 0; i < vertices.size(); i++){
			if(vertices.get(i).getAPC().equals(src)){
				vertices.get(i).setBestTimeValue(0);
				q.enqueue(new Neighbor(src, 0, 0));
			} else {
				q.enqueue(new Neighbor(vertices.get(i).getAPC()));
			}
		}
		
		//Dijikstra's algorithm
		while(!q.isEmpty()){
			//Sort to have lowest weight at front of the queue
			q.sortTime();
			Neighbor currentNeighbor = q.dequeue();
			ArrayList<Edge> adjacentEdges;
			adjacentEdges = getAdjacentEdges(edges, currentNeighbor.getAPC());
			
			for(int i = 0; i < adjacentEdges.size(); i++){
				String target = adjacentEdges.get(i).getDest();
				Vertex targetVertex = getVertex(target);
				Neighbor targetNeighbor = q.find(adjacentEdges.get(i).getDest());
				Edge currentEdge = adjacentEdges.get(i);
				//If the current edge starts before the bestEdge ends
				if(currentEdge.getStartTime() < currentNeighbor.getTime()){
					continue;
				} else if(currentEdge.getEndTime() < targetVertex.getBestTimeValue()){
					//Update weight in the queue as well as vertex's best edge and best edge values
					targetVertex.setBestTimeEdge(currentEdge);
					targetVertex.setBestTimeValue(currentEdge.getEndTime());
					System.out.println(currentEdge.getEndTime());
					if(targetNeighbor == null){
						//targetNeighbor has already been visited
						continue;
					} else {
						targetNeighbor.setTime(currentEdge.getEndTime());
					}
				} 
				
			}
			adjacentEdges.clear();
		}
		
		
		ArrayList<Edge> earliestItinerary;
		earliestItinerary = returnEarliestItinerary(vertices, src, dest);

		//Write the path to take to the buffered writer
		bw.write("Earliest From:" + src + " To:" + dest);
		bw.newLine();
		
		for(int i = 0; i < earliestItinerary.size(); i++){
			Edge currentEdge = earliestItinerary.get(i);
			bw.write("Flight " + currentEdge.getSource() + " " + currentEdge.getDest() +
					" cost:" + currentEdge.getCost() + " start:" + currentEdge.getStartTime() +
					" end:" + currentEdge.getEndTime());
			bw.newLine();
		}
		
	}
	
	//getAllEdges: void -> ArrayList<Edge>
	//Returns the edges in the graph.
	public ArrayList<Edge> getAllEdges(){
		return edges;
	}
	
	//getVertex: String -> Vertex
	//If String is an airport code then getVertex returns the vertex
	//in the arraylist that contains that airport code. If the 
	//airport code is not found and error message is printed and null
	//is returned.
	public Vertex getVertex(String APC){
		for(int i = 0; i < vertices.size(); i++){
			if(APC.equals(vertices.get(i).getAPC())){
				return vertices.get(i);
			}
		}
		
		//Vertex was not found
		System.out.println("No vertex with " + APC + "was found");
		return null;
	}
	
	//getVertices: void -> ArrayList<Vertex>
	//Returns all the vertices in the graph.
	public ArrayList<Vertex> getVertices(){
		return vertices;
	}
	
	//sameFromAndTo: Edge x Edge -> boolean
	//Returns true iff both edges contain the same to and from airport
	//codes and false otherwise.
	public boolean sameSourceAndDest(Edge e1, Edge e2){
		return e1.getSource().equals(e2.getSource()) && 
				e1.getDest().equals(e2.getDest());
	}
	
	//sameSource: Edge x Edge -> boolean
	//Returns true if the edges have the same source and false otherwise
	public boolean sameSource(Edge e1, Edge e2){	
		return e1.getSource().compareTo(e2.getSource()) == 0;
	}
	
	//sameDest: Edge x Edge -> boolean
	//Returns true if the edges have the same destination and false otherwise
	private boolean sameDest(Edge e1, Edge e2){
		return e1.getDest().compareTo(e2.getDest()) == 0;
	}
	
	//sameData: Edge x Edge -> boolean
	//Returns true if both edges have the same data info for their flight
	public boolean sameData(Edge e1, Edge e2){
		return ((e1.getStartTime() == e2.getStartTime()) &&
				(e1.getEndTime() == e2.getEndTime()) &&
				(e1.getCost() == e2.getCost()));
	}
	
	//vertexExists: String -> bool
	//vertexExists takes in a string for an airport code and
	//returns whether or not that vertex exists in the graph or not
	public boolean vertexExists(String apc){
		for(int i = 0; i < vertices.size(); i++){
			if(vertices.get(i).getAPC().equals(apc)){
				return true;
			}
		}
		return false;
	}
	
	//printEdge: Edge -> void
	//Prints out the information of the Edge e that is passed in as a parameter.
	private void printEdge(Edge e){
		System.out.println("Source: " + e.getSource());
		System.out.println("Dest: " + e.getDest());
		System.out.println("Cost: " + e.getCost());
		System.out.println("Start: " + e.getStartTime());
		System.out.println("End: " + e.getEndTime());
	}
	
	//returnEarliestItinerary: ArrayList<Vertex> x String x String -> ArrayList<Edges>
	//Returns the edges that are needed to achieve the earliest flight. This is used in the earliest function
	//after Dijikstra's has been performed.
	private ArrayList<Edge> returnEarliestItinerary(ArrayList<Vertex> vertices, String src, String dest){
		ArrayList<Edge> pathToTake = new ArrayList<Edge>();

		if(getVertex(dest) == null){
			return pathToTake;
		}
		
		Edge e = getVertex(dest).getBestTimeEdge();
		while(!e.getSource().equals(src)){
			if(e.getSource() == null){
				return pathToTake;
			}
			pathToTake.add(0, e);
			if(getVertex(e.getSource()) == null){
				return pathToTake;
			}
			e = getVertex(e.getSource()).getBestTimeEdge();
		}
		pathToTake.add(0, e);
		return pathToTake;
	}
	
	//returnCheapestItinerary: ArrayList<Vertex> x String x String -> ArrayList<Edges>
	//Returns the edges that are needed to achieve the cheapest flight. This is used in the cheapest function
	//after Dijikstra's has been performed.
	private ArrayList<Edge> returnCheapestItinerary(ArrayList<Vertex> vertices, String src, String dest){
		ArrayList<Edge> pathToTake = new ArrayList<Edge>();

		if(getVertex(dest) == null){
			return pathToTake;
		}
		
		Edge e = getVertex(dest).getBestCostEdge();
		while(!e.getSource().equals(src)){
			if(e.getSource() == null){
				return pathToTake;
			}
			pathToTake.add(0, e);
			if(getVertex(e.getSource()) == null){
				return pathToTake;
			}
			
			e = getVertex(e.getSource()).getBestCostEdge();
		}
		pathToTake.add(0, e);
		return pathToTake;
	}
	
	//getAdjacentEdges: ArrayList<Edge> x String -> ArrayList<Edge>
	//Returns all the edges that contain apc as their source string. Essentially returns all the
	//neighbors of apc.
	private ArrayList<Edge> getAdjacentEdges(ArrayList<Edge> edges, String apc){
		ArrayList<Edge> ret = new ArrayList<Edge>();
		for(int i = 0; i < edges.size(); i++){
			if(edges.get(i).getSource().equals(apc)){
				ret.add(edges.get(i));
			}
		}
		return ret;
	}
	
	//clearBestEdges(): void -> void
	//Clears all the best edges of all the vertices in the graph.
	private void clearBestEdges(){
		for(int i = 0; i < vertices.size(); i++){
			Vertex currentVertex = vertices.get(i);
			currentVertex.setBestCostEdge(new Edge("", "", 10000, 0, 0));
			currentVertex.setBestCostValue(100000);
			currentVertex.setBestTimeEdge(new Edge("", "", 10000, 0, 0));
			currentVertex.setBestTimeValue(100000);
		}
	}
	
	//addEgdeToGraph: Edge -> void
	//Adds newEdge to the graph in it's proper place. The sort first sorts by source, then by destination, and
	//finally by the earliest start time.
	private void addEdgeToGraph(Edge newEdge){
		if(edges.size() == 0){
			edges.add(newEdge);
		} else {
			int i = 0;
			while(i < edges.size() && newEdge.getSource().compareTo(edges.get(i).getSource()) >= 0){
				//Sort by source string first
				if(sameSource(newEdge, edges.get(i))){
					//Sort by destination next
					if(newEdge.getDest().compareTo(edges.get(i).getDest()) < 0){
						break;
					} else if(sameDest(newEdge, edges.get(i))){
						//Sort by start time last
						if(newEdge.getStartTime() < edges.get(i).getStartTime()){
							break;
						} else {
							i++;
						}
					} else {
						i++;
					}
				} else {
					i++;
				}
			}
			edges.add(i, newEdge);
		}
	}
	
	//addVerticesToGraph: String x String -> void
	//Adds the vertices into the graph if they do not already exist and does nothing otherwise.
	private void addVerticesToGraph(String src, String dest){
		//If vertices do not exist for APCs then create them
		Vertex source = new Vertex(src);
		Vertex destination = new Vertex(dest);
		if(!vertexExists(src)){
			vertices.add(source);
			System.out.println("Adding vertex: " + src);
			source.addNeighbor(dest);
		} else {
			//Update existing src's neighbors
			int i = 0;
			while(!(vertices.get(i).getAPC().equals(src))){
				i++;
			}
			vertices.get(i).addNeighbor(dest);
		}
		
		if(!vertexExists(dest)){
			vertices.add(destination);
			System.out.println("Adding vertex: " + dest);
		}
		
		//Sort the vertices
		Collections.sort(vertices);
	}
	
	//printVertices: void -> void
	//Prints out all the vertices in the graph.
	private void printVertices(){
		for(int i = 0; i < vertices.size(); i++){
			System.out.println(vertices.get(i).getAPC());
		}
	}
	
	//printEdges: void -> void
	//Prints out all the edges in the graph.
	private void printEdges(){
		for(int i = 0; i < edges.size(); i++){
			printEdge(edges.get(i));
		}
	}
	
	
}
