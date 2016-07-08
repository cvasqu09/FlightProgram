package GraphElements;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Parse {
	//parseLine: Graph x String -> void
	//parseLine takes in the graph to operate with and a string to
	//parse which should be a valid command as specified in the pdf file
	//and parse will modify the graph if necessary or perform the operation
	//specified by the command read in.
	public static void parseLine(BufferedWriter bw, Graph fGraph, String line) throws IOException{
		//Split string on whitespace
		String[] tokens = line.split("\\s");
		String command = tokens[0];
		System.out.println("Command " + command);
		switch(command){
			case "Flight":
				String src = tokens[1];
				String dest = tokens[2];
				int cost = Integer.parseInt(tokens[3]);
				int start = Integer.parseInt(tokens[4]);
				int end = Integer.parseInt(tokens[5]);
				fGraph.newFlight(src, dest, cost, start, end);
				break;
				
			case "List":
				//FIXME: Print by vertex instead of all edges
				if(tokens[1].equals("*")){			//List everything
					System.out.println("List All");
					ArrayList<Edge> edgeList = fGraph.getAllEdges();
					ArrayList<Vertex> vertexList = fGraph.getVertices();
					if(edgeList.size() == 0){
						bw.write("Network Is Empty");
						bw.newLine();
					} else {
						int j = 0;
						for(int i = 0; i < vertexList.size(); i++){
							bw.write("Flights From " + vertexList.get(i).getAPC());
							bw.newLine();
							while(j < edgeList.size() && edgeList.get(j).getSource().equals(vertexList.get(i).getAPC())){
								printFlightInfo(bw, edgeList, j);
								j++;
							}	
						}
					}
					break;
				} else {							//List specific APC flights
					System.out.println("List specific");
					ArrayList<Edge> edgeList = fGraph.getAllEdges();
					ArrayList<Vertex> vertexList = fGraph.getVertices();
					boolean found = false;
					for(int i = 0; i < vertexList.size(); i++){
						if(vertexList.get(i).getAPC().equals(tokens[1])){
							found = true;
							break;
						}
					}
					
					if(found == false){
						bw.write("Airport " + tokens[1] + " Not Found");
						bw.newLine();
					} else {
						bw.write("Flights From " + tokens[1]);
						bw.newLine();
						int i = 0;
						//Advance until src APCs match up
						while(!(tokens[1].equals(edgeList.get(i).getSource()))){
							i++;
						}
						
						//Print out information while the srcs still match
						while(i < edgeList.size() && tokens[1].equals(edgeList.get(i).getSource())){
							bw.write("-- Flight to " + edgeList.get(i).getDest() + 
									" cost:" + edgeList.get(i).getCost() + 
									" start:" + edgeList.get(i).getStartTime() +
									" end:" + edgeList.get(i).getEndTime());
							bw.newLine();
							i++;
						}
					}
					
				}
				break;
				
			case "Cheapest": 
				String apc1 = tokens[1];
				String apc2 = tokens[2];
				fGraph.cheapest(bw, apc1, apc2);
				break;
				
			case "Earliest":
				String from = tokens[1];
				String to = tokens[2];
				fGraph.earliest(bw, from, to);
				break;
				
			default:
				System.out.println("Invalid command given " + command);
				break;
		}
	}
	
	//printFlighDestInfo: BufferedWriter x ArrayList<Edge> x int -> void
	//Prints the flight info in a specified format to the buffered writer bw.
	private static void printFlightInfo(BufferedWriter bw, ArrayList<Edge> edgeList, int i) throws IOException{
		bw.write("-- Flight to " + edgeList.get(i).getDest() + 
				" cost:" + edgeList.get(i).getCost() + 
				" start:" + edgeList.get(i).getStartTime() +
				" end:" + edgeList.get(i).getEndTime());
		bw.newLine();
	}

}
