OVERVIEW
The following program is used to calculate the cheapest and earliest flights from one airport to another. The connection between airports is represented using a graph where each vertex is an airport and each connecting flight is an edge.

COMMANDS
FLIGHT COMMAND:
The flight command adds a flight to the graph where <APC1> and <APC2> are three character airport codes for the source and destination airports respectively. The <Cost>
is an integer representing the cost of the flight and <start time> and <end time> are integers representing the start and end time of the flight. The general flight command is as follows:

General use: 
Flight <APC1> <APC2> <Cost> <start time> <end time>

Example of use:
Flight LAX PTL 100 200 400

---------------------------------------------------------------------------------------
LIST COMMAND:
The list command is overloaded and can be used in two different ways. The first use prints the flights coming out of a given airport where <APC> is the three character airport code and prints that is "Airport <APC> Not Found" if no flights exist and is generalized as follows:

General use:
List <APC>

Example of use:
List LAX

The second use of the command lists all the flights that currently exist in the graph alphabetically and prints "Network Is Empty" if no flights exist and is used as follows:

List *

----------------------------------------------------------------------------------------
CHEAPEST COMMAND:
If <APC1> and <APC2> are three character airport codes then the cheapest command returns the itinerary for the cheapest flight from <APC1> to <APC2> using Dijkstra's algorithm.

General use: 
Cheapest <APC1> <APC2>

Example of use: 
Cheapest LAX PTL

----------------------------------------------------------------------------------------
EARLIEST COMMAND:
If <APC1> and <APC2> are three character airport codes then the earliest command returns the itinerary for the earliest flight from <APC1> to <APC2> using Dijkstra's algorithm.

General use:
Earliest <APC1> <APC2>

Example of use: 
Earliest LAX PTL


GUI
The graphical user interface is a simple way to see the outputs of the default test cases as well as run and see the results of running the program on a custom text file. The first tab is used in order to see the output of all the default test cases. This assumes that the user has all the test cases and outputs saved in the same project folder as the program. The second tab is used for custom text files where the user enters their file name (again saved where all the other test.txt files are saved) and also enters the name of their output file. Upon hitting enter and assuming the user entered valid .txt names then their output will be displayed in a text area.