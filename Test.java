import ADTPackage.*;
import GraphPackage.*;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Test {
	public static Object[][] map;
	
	public static void main(String[] args) {
	//The maze txt can be changed in the getRawMap function 
	start();
	UndirectedGraph graph = new UndirectedGraph();
	int endVertexNamei=(map.length-2);
	int endVertexNamej=(map[0].length-1);
	String endVertexName = endVertexNamei+"-"+endVertexNamej;
	String firstVertexName="0-1";
	
	//The result changes if the order of adding vertexes is changed
	// append at the end
	
	        for (int i = map.length-1; i >=0; i--) {
	            for (int j = map[0].length-1; j >=0; j--) {
	                if((char)map[i][j]!= '#'){
	                	String vertexName=i+"-"+j;
	               graph.addVertex(vertexName) ;
	                }
	            }
	            
	        }
	
	// add from beginning
	/*  for (int i = 0; i <map.length; i++) {
          for (int j = 0; j < map[0].length; j++) {
              if((char)map[i][j]!= '#'){
              	String vertexName=i+"-"+j;
             graph.addVertex(vertexName) ;
              }
          }
	  }*/
	        graph.adjMatrix(graph.getNumberOfVertices());//to create the adjmatrix 
	        
	       graph.makeEdges(false);//connects vertex // false mean unweighted
	        
	       graph.displayEdges();
	        
	      
	      System.out.println("************");
	      int vs =graph.getNumberOfVertices();
	      int es= graph.getNumberOfEdges();
	      System.out.println("Vertex size: "+vs+"   \nEdge size: " +es+ " ");
	      System.out.println("\n***********ADJACENCY MATRIX************\n");
	     
	   System.out.println(graph);// this part uses getString method  in DirectedGraph which writes the adj matrix
	    
	  
	    System.out.println("**********************************************");
	    QueueInterface q = graph.getBreadthFirstTraversal(firstVertexName, endVertexName);
	    System.out.println("breadthfirst search for 0-1 vertex:");
	    String stringBFS=".";
	    while(!q.isEmpty()) {
	    	String peekQ = (String)q.getFront()+".";
	    	stringBFS = stringBFS + peekQ;
	    	System.out.print(q.dequeue()+" ");
	    }
	   System.out.println("\nThe Number Of Visited Vertices For BFS : "+graph.theNumberOfVisitedVerticesForBFS+"\n");
	   printMapWithPath(map,stringBFS);
	   
	  
	    
	    System.out.println("\n**************************************************************************************\n");
	    QueueInterface s= graph.getDepthFirstTraversal(firstVertexName, endVertexName);
	    System.out.println("Depthfirst search for 0-1 vertex:");
	    String stringDFS=".";
	    while(!s.isEmpty()) {
	    	String peekQ = (String)s.getFront()+".";
	    	stringDFS = stringDFS  + peekQ;
	    	System.out.print(s.dequeue()+" ");
	    }
	    System.out.println("\nThe Number Of Visited Vertices For DFS : "+graph.theNumberOfVisitedVerticesForDFS+"\n");
	    printMapWithPath(map,stringDFS);
	    
	   
	    System.out.println("\n**************************************************************************************\n");
	    
	    LinkedStack shortestPathStack= new LinkedStack();
	    int lengthOfSP = graph.getShortestPath(firstVertexName, endVertexName , shortestPathStack)+1;//edge+1
	    System.out.println("\nThe length of the shortest path: "+lengthOfSP+"\n");
	    String stringShortestPath=".";
	    while(!shortestPathStack.isEmpty()) {
	    	String peekQ = (String)shortestPathStack.peek()+".";
	    	stringShortestPath = stringShortestPath  + peekQ;
	    	System.out.print(shortestPathStack.pop()+" ");
	    }
	    System.out.println();
	    printMapWithPath(map,stringShortestPath);
	    
	   
	    
	    //CheapestPath
	    System.out.println("\n**************************************************************************************\n");
	    
	        UndirectedGraph graphCP = new UndirectedGraph();
	        for (int i = map.length-1; i >=0; i--) {
	            for (int j = map[0].length-1; j >=0; j--) {
	                if((char)map[i][j]!= '#'){
	                	String vertexName=i+"-"+j;
	               graphCP.addVertex(vertexName) ;
	                }
	            }
	            
	        }
	        graphCP.adjMatrix(graphCP.getNumberOfVertices());//to create the adjmatrix 
	        
	       graphCP.makeEdges(true);//connects vertex //true mean weighted
	        
	       //graphCP.displayEdges();
	       
	       LinkedStack cheapestPathStack= new LinkedStack();
		    double lengthOfCP = graphCP.getCheapestPath(firstVertexName, endVertexName , cheapestPathStack);//edge+1
		    System.out.println("\nThe cost of the cheapest path: "+lengthOfCP);
             System.out.println("The number of visited vertices for the Weighted Graph :"+ graphCP.theNumberOfVisitedVerticesForTheWeightedGraph+"\n");
		    String stringCheapestPath=".";
		    while(!cheapestPathStack.isEmpty()) {
		    	String peekQ = (String)cheapestPathStack.peek()+".";
		    	stringCheapestPath = stringCheapestPath  + peekQ;
		    	System.out.print(cheapestPathStack.pop()+" ");
		    }
		    System.out.println();
		    printMapWithPath(map,stringCheapestPath);
	    
	    
	}
	
	public static void start() {
        map = getRawMap();
    }
	
	public static void printMapWithPath(Object[][] map, String stringVertex) {
		Object[][] mapPath = new Object[map.length][map[0].length];
		mapPath=getRawMap();
		  // System.out.println(map[0].length+"   " +map.length);
	        for (int i = 0; i <mapPath.length; i++) {
	            for (int j = 0; j < mapPath[0].length; j++) {
	            	String vertexName ="."+i+"-"+j+".";
	            	if( (stringVertex.indexOf(vertexName)!=-1)) {
	            		mapPath[i][j]='.';
	            	}
	                System.out.print(mapPath[i][j]);
	            }
	            System.out.println();
	        }
	    }
	

	   public static void printMap(Object[][] map) {
	        for (int i = 0; i <map.length; i++) {
	            for (int j = 0; j < map[0].length; j++) {
	                System.out.print(map[i][j]);
	            }
	            System.out.println();
	        }

	    }
	   
	 //MapArray// mazetxt can be changed
    public static Object[][] getRawMap() {
	        
	        try {
	        	String line="";
	            File file = new File("maze2.txt");
	            Scanner sc = new Scanner(file);
	            int i = 0;
	            int counter=0;
	            
	            while (sc.hasNextLine()) {
	                 line = sc.nextLine();
	                
	                i++;
	            }
	            
	            sc.close();
	            
	            map = new Object[i][line.length()];
	            i=0;
	            Scanner sc2 = new Scanner(file);
	            while (sc2.hasNextLine()) {
	                 line = sc2.nextLine();
	                for (int j = 0; j < line.length(); j++) {
	                    map[i][j] = line.charAt(j);
	                }
	                i++;
	            }
	            sc2.close();
	           
	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        }

	        return map;
	    }
	 
	 
}
