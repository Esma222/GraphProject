package GraphPackage;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import ADTPackage.*; // Classes that implement various ADTs
/**
 A class that implements the ADT directed graph.
 @author Frank M. Carrano
 @author Timothy M. Henry
 @version 5.1
 */
public class DirectedGraph<T> implements GraphInterface<T>
{
   private DictionaryInterface<T, VertexInterface<T>> vertices;
   private int edgeCount;
   private int adjMatrix[][];//it is created for adjacency matrix
   public int  theNumberOfVisitedVerticesForBFS;
   public int  theNumberOfVisitedVerticesForDFS;
   public int  theNumberOfVisitedVerticesForTheWeightedGraph;
  
   
   public DirectedGraph()
   {
      vertices = new UnsortedLinkedDictionary<>();
      edgeCount = 0;
   } // end default constructor

   //created adj matrix
   public void adjMatrix(int size )
   {
	   this.adjMatrix = new int[size][size];
   }
   
   
   public boolean addVertex(T vertexLabel)
   {
      VertexInterface<T> addOutcome = vertices.add(vertexLabel, new Vertex<>(vertexLabel));
      return addOutcome == null; // Was addition to dictionary successful?
   } // end addVertex
   
   public boolean addEdge(T begin, T end, double edgeWeight)
   {
      boolean result = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
         result = beginVertex.connect(endVertex, edgeWeight);
      if (result)
         edgeCount++;
      return result;
   } // end addEdge
   
   
   //It controls the right, left, down and up and connects the vertexes, 
   //decides whether to add weight according to the incoming parameter.
   //and adds to adj matrix
   public void makeEdges(boolean weightFlag) {
	   
	   Random random=new Random();
	   
	   Iterator<T> vertex =vertices.getKeyIterator();
		  while( vertex.hasNext()) {
			  String vertexName1 =(String)vertex.next();
	        	String[] line2=vertexName1.split("-");
	        	 Iterator<T> vertex2=vertices.getKeyIterator();
	        	while( vertex2.hasNext()) {
	        		String vertexName2=(String)vertex2.next();
	        		String[] line3=vertexName2.split("-");
	        		//********weight**********

	        	    double weight=random.nextInt(1,5);
	        		if(weightFlag==false) {
	        			   weight=0.0;
	        		   }
	        		if((line3[0].equals(line2[0])) &&( Math.abs(Integer.parseInt(line3[1])-Integer.parseInt(line2[1]))==1)) {//undirected çift taraflý
	        			addEdge((T)vertexName1, (T)vertexName2,weight);
	        			addMatrix((T)vertexName1, (T)vertexName2);
	        			
	        		}
	        		else if((line3[1].equals(line2[1])) && (Math.abs(Integer.parseInt(line3[0])-Integer.parseInt(line2[0]))==1)) {
	        			addEdge((T)vertexName1, (T)vertexName2, weight);
	        			addMatrix((T)vertexName1, (T)vertexName2);
	        		}
	        		
	        	}
	        	
	       //displayEdges();
		  }
   }

   
   //add to adjacency matrix
   public void addMatrix(T begin, T end) {
	   int beginIndex=indexOf(begin);
	   int endIndex=indexOf(end);
	   this.adjMatrix[beginIndex ][endIndex]=1;
	   this.adjMatrix[endIndex][beginIndex]=1;
   }
   

   
   //to print the  adj matrix
   public String toString() {
	 
	   StringBuilder sb = new StringBuilder(); 
	   Iterator<T> vertex =vertices.getKeyIterator();
	   while( vertex.hasNext()) {
		   T v1 =vertex.next();
		   System.out.print("\t(" + v1 + ")");
	   }
	   System.out.println();
	   for(int v = 0; v < vertices.getSize(); v++) {
	   sb.append("(" + getVertex(v) + ")\t");

	   for(int w: adjMatrix[v]) {

	   sb.append(w + "\t");
	   }
	   sb.append("\n");
	   }
	   return sb.toString();

	   }
   
   public boolean addEdge(T begin, T end)
   {
      return addEdge(begin, end, 0);
   } // end addEdge

   public boolean hasEdge(T begin, T end)
   {
      boolean found = false;
      VertexInterface<T> beginVertex = vertices.getValue(begin);
      VertexInterface<T> endVertex = vertices.getValue(end);
      if ( (beginVertex != null) && (endVertex != null) )
      {
         Iterator<VertexInterface<T>> neighbors = beginVertex.getNeighborIterator();
         while (!found && neighbors.hasNext())
         {
            VertexInterface<T> nextNeighbor = neighbors.next();
            if (endVertex.equals(nextNeighbor))
               found = true;
         } // end while
      } // end if
      
      return found;
   } // end hasEdge

	public boolean isEmpty()
	{
	  return vertices.isEmpty();
	} // end isEmpty

	public void clear()
	{
	  vertices.clear();
	  edgeCount = 0;
	} // end clear

	public int getNumberOfVertices()
	{
	  return vertices.getSize();
	} // end getNumberOfVertices

	public int getNumberOfEdges()
	{
	  return edgeCount;
	} // end getNumberOfEdges

	protected void resetVertices()
	{
	   Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
	   while (vertexIterator.hasNext())
	   {
	      VertexInterface<T> nextVertex = vertexIterator.next();
	      nextVertex.unvisit();
	      nextVertex.setCost(0);
	      nextVertex.setPredecessor(null);
	   } // end while
	} // end resetVertices
	
	public StackInterface<T> getTopologicalOrder() 
	{
		resetVertices();

		StackInterface<T> vertexStack = new LinkedStack<>();
		int numberOfVertices = getNumberOfVertices();
		for (int counter = 1; counter <= numberOfVertices; counter++)
		{
			VertexInterface<T> nextVertex = findTerminal();
			nextVertex.visit();
			vertexStack.push(nextVertex.getLabel());
		} // end for
		
		return vertexStack;	
	} // end getTopologicalOrder
	
	// Returns the element at the specified position in vertices.
	public T getVertex(int index ) {
		   int counter=0;
		   T searchedIndexVertex=null;
		   int endIndex=0;
		Iterator<T> vertex =vertices.getKeyIterator();
		  while( vertex.hasNext()) {
			  T vertexName =vertex.next();
			  if(counter==index) {
				  searchedIndexVertex=vertexName;  
			  }
			  counter++;
		   }
		return  searchedIndexVertex;
	}
	
	//Returns the index of the first occurrence of the specified element
    // in this list, or -1 if this list does not contain the element.
	public int indexOf(T searchedVertex) {
		   int counter=0;
		   int searchedVertexIndex=0;
		   int endIndex=0;
		Iterator<T> vertex =vertices.getKeyIterator();
		  while( vertex.hasNext()) {
			  T vertexName =vertex.next();
			  if(vertexName==searchedVertex) {
				  searchedVertexIndex=counter;  
			  }
			  counter++;
		   }
		return searchedVertexIndex;
	}
	
	
	
	
	@SuppressWarnings("unchecked")
	public QueueInterface<T> getBreadthFirstTraversal(T origin, T end) {
		resetVertices();
		LinkedQueue vertexQueue = new LinkedQueue();
		LinkedQueue traversalOrder = new LinkedQueue();
		
		vertexQueue.enqueue(origin);
		traversalOrder.enqueue(origin);
		vertices.getValue(origin).visit();
		theNumberOfVisitedVerticesForBFS++;
		boolean flag =false;
		while (!vertexQueue.isEmpty()&&!flag) {
			T current_vertex = (T)vertexQueue.dequeue(); // the top element is selected and removed from queue
			T v;
			while (vertices.getValue(current_vertex).hasNeighbor() && vertices.getValue(current_vertex).getUnvisitedNeighbor()!=null) {
	
				v=vertices.getValue(current_vertex).getUnvisitedNeighbor().getLabel();
				if(!vertices.getValue(v).isVisited()) {
					vertices.getValue(v).visit();
					theNumberOfVisitedVerticesForBFS++;
					vertexQueue.enqueue(v); 
					traversalOrder.enqueue(v);
					
				}
				if(v.equals((Object)end)) {
					flag=true;
					break;
				}
				
			}
			
		}
		
		return traversalOrder;
		
	}

  

  
	@SuppressWarnings("unchecked")
	public QueueInterface<T> getDepthFirstTraversal(T origin, T end) {
		resetVertices();
		LinkedStack vertexStack = new LinkedStack();
		LinkedQueue traversalOrder = new LinkedQueue();
		
		vertexStack.push(origin);
		traversalOrder.enqueue(origin);
		theNumberOfVisitedVerticesForDFS++;
		vertices.getValue(origin).visit();
	
		while(!vertexStack.isEmpty()) { 
			T topStack= (T)vertexStack.peek();
			T v;
			if(vertices.getValue(topStack).hasNeighbor() && vertices.getValue(topStack).getUnvisitedNeighbor()!=null) {
				
				v=vertices.getValue(topStack).getUnvisitedNeighbor().getLabel();
				if(!vertices.getValue(v).isVisited()) {
					vertices.getValue(v).visit();
					theNumberOfVisitedVerticesForDFS++;
					vertexStack.push(v); 
					traversalOrder.enqueue(v);
				}
				if(vertexStack.peek().equals(end)) {
					break;
				}
			}
			else {
				vertexStack.pop();
			}
		}
		return traversalOrder;
		
	}
	
	
	@SuppressWarnings("unchecked")
	public int getShortestPath(T begin, T end, StackInterface<T> path) {
		resetVertices();
		boolean flag=false;
		double pathLength=0;
		LinkedQueue vertexQueue = new LinkedQueue();
		vertices.getValue(begin).visit();
		vertexQueue.enqueue(begin);
		while (!flag && !vertexQueue.isEmpty()) {
			T current_vertex = (T)vertexQueue.dequeue(); // the top element is selected and removed from queue
			T v;
			while (!flag && vertices.getValue(current_vertex).hasNeighbor() && vertices.getValue(current_vertex).getUnvisitedNeighbor()!=null) {
			
				v=vertices.getValue(current_vertex).getUnvisitedNeighbor().getLabel();
				if(!vertices.getValue(v).isVisited()) {
					vertices.getValue(v).visit();
					vertices.getValue(v).setPredecessor(vertices.getValue(current_vertex));
					vertices.getValue(v).setCost(1+vertices.getValue(current_vertex).getCost());
					vertexQueue.enqueue(v);
				}
				if(v.equals((Object)end)) {
					flag=true;
					break;
				}
			}
		}
		pathLength=vertices.getValue(end).getCost();
		path.push(end);
		T vertex=end;
		while(vertices.getValue(vertex).hasPredecessor()) {
			vertex= vertices.getValue(vertex).getPredecessor().getLabel();
			path.push(vertex);
		}
		return (int)pathLength;
	}
	
	
   
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public double getCheapestPath(T begin, T end, StackInterface<T> path) {
		resetVertices();
		boolean flag=false;
		HeapPriorityQueue cheapestPathPQ= new HeapPriorityQueue();
		EntryPQ entry=new EntryPQ(vertices.getValue(begin) ,0.0,null);
		cheapestPathPQ.add(entry);
		while (!flag && !cheapestPathPQ.isEmpty()) {
			EntryPQ frontEntry = (EntryPQ)cheapestPathPQ.remove(); // the top element is selected and removed from queue
			T frontVertex = frontEntry.getVertex().getLabel();
			if(!vertices.getValue(frontVertex).isVisited()) {
				vertices.getValue(frontVertex).visit();
				vertices.getValue(frontVertex).setCost(frontEntry.getCost());
				vertices.getValue(frontVertex).setPredecessor(frontEntry.getPredecessor());
				if(frontVertex.equals(end)) {
					flag=true;
				}
				
				 else
				 {
					 Iterator<VertexInterface<T>> neighbors = vertices.getValue(frontVertex).getNeighborIterator();
					
				 while (neighbors.hasNext()&& vertices.getValue(frontVertex).getUnvisitedNeighbor()!=null ) 
				 {

					 VertexInterface<T> nextNeighbor = neighbors.next();
				     //System.out.println(nextNeighbor);
				     Double weightOfEdgeToNeighbor = (Double) nextNeighbor.getWeightIterator().next();
				     
				 if(!nextNeighbor.isVisited()) {
				   double nextCost= weightOfEdgeToNeighbor+ vertices.getValue(frontVertex).getCost();
				   entry=new EntryPQ(nextNeighbor, nextCost, vertices.getValue(frontVertex));
				   cheapestPathPQ.add(entry);
				 }
				 }
				 }
				 }
			
		}
		// Traversal ends; construct cheapest path
		double pathCost = vertices.getValue(end).getCost();
		path.push(end);
		//System.out.println(  path.peek()+"  "+vertices.getValue(end).getCost());
		theNumberOfVisitedVerticesForTheWeightedGraph++;
		T vertex=end;
		while(vertices.getValue(vertex).hasPredecessor()) {
			vertex= vertices.getValue(vertex).getPredecessor().getLabel();
			path.push(vertex);
			theNumberOfVisitedVerticesForTheWeightedGraph++;
			//System.out.println(  path.peek()+"  "+vertices.getValue(vertex).getCost());
		}
		return pathCost;
			
	}
	
    //###########################################################################
	/** Precondition: path is an empty stack (NOT null) */
    /* Use EntryPQ instead of Vertex in Priority Queue because multiple entries contain
     * 	the same vertex but different costs - cost of path to vertex is EntryPQ's priority value
     * public double getCheapestPath(T begin, T end, StackInterface<T> path)
     * 		return the cost of the cheapest path
     */
    //###########################################################################


	
	protected VertexInterface<T> findTerminal()
	{
		boolean found = false;
		VertexInterface<T> result = null;

		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();

		while (!found && vertexIterator.hasNext())
		{
			VertexInterface<T> nextVertex = vertexIterator.next();
			
			// If nextVertex is unvisited AND has only visited neighbors)
			if (!nextVertex.isVisited())
			{ 
				if (nextVertex.getUnvisitedNeighbor() == null )
				{ 
					found = true;
					result = nextVertex;
				} // end if
			} // end if
		} // end while

		return result;
	} // end findTerminal

	// Used for testing
	public void displayEdges()
	{
		System.out.println("\nEdges exist from the first vertex in each line to the other vertices in the line.");
		System.out.println("(Edge weights are given; weights are zero for unweighted graphs):\n");
		Iterator<VertexInterface<T>> vertexIterator = vertices.getValueIterator();
		while (vertexIterator.hasNext())
		{
			((Vertex<T>)(vertexIterator.next())).display();
		} // end while
	} // end displayEdges 
	
	private class EntryPQ implements Comparable<EntryPQ>
	{
		private VertexInterface<T> vertex; 	
		private VertexInterface<T> previousVertex; 
		private double cost; // cost to nextVertex
		
		private EntryPQ(VertexInterface<T> begin, double cost, VertexInterface<T> begin2)
		{
			this.vertex = begin;
			this.previousVertex = begin2;
			this.cost = cost;
		} // end constructor
		
		public VertexInterface<T> getVertex()
		{
			return vertex;
		} // end getVertex
		
		public VertexInterface<T> getPredecessor()
		{
			return previousVertex;
		} // end getPredecessor

		public double getCost()
		{
			return cost;
		} // end getCost
		
		public int compareTo(EntryPQ otherEntry)
		{
			// Using opposite of reality since our priority queue uses a maxHeap;
			// could revise using a minheap
			return (int)Math.signum(otherEntry.cost - cost);
		} // end compareTo
		
		public String toString()
		{
			return vertex.toString() + " " + cost;
		} // end toString 
	} // end EntryPQ


} // end DirectedGraph
