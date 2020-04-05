import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;

/**
 * 
 * @author Rishabh Aryan Das
 *
 */
public class ProcessInput {

	//output file
	File output = new File("output_file.txt");   
	//ArrayList to store removed Nodes
	ArrayList<Node> removedNodes = new ArrayList<Node>();
	//Max Fibonacci heap
	FibonacciHeap fibonacciHeap = new FibonacciHeap();
	//Hash table
	Hashtable<String, Node> hashtable = new Hashtable<String, Node>();

	/**
	 * Reads each line of the input file and processes accordingly.
	 * @param inputFile
	 */
	void readInput(String inputFile) {
		try {
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				//Input line starts with #
				if(inputLine.startsWith("#")){             
					//Extract the hashtag
					String hashtag=inputLine.split(" ")[0].substring(1, inputLine.split(" ")[0].length());
					//Extract the frequency of hashtag
					int frequency=Integer.parseInt(inputLine.split(" ")[1]);
					//If hashtag was not encountered so far, add a new node to the
					//Fibonacci heap
					if(!hashtable.containsKey(hashtag)){
						Node newNode=new Node(frequency, hashtag);
						fibonacciHeap.insert(newNode, frequency);
						hashtable.put(hashtag,newNode);
					}
					//If the node already exists, increment its frequency.
					else{
						int newFrequency=hashtable.get(hashtag).getKey()+frequency;
						Node x=fibonacciHeap.increaseKey(hashtable.get(hashtag),newFrequency);
						hashtable.remove(hashtag);
						hashtable.put(hashtag, x);
					}       
				}
				else {
					//If line is STOP, terminate
					if(inputLine.equalsIgnoreCase("stop")){
						return;
					}
					else{  
						//If query, generate the fibonacci heap and call removeMax()
						process(inputLine);
					}
				}
			}
		}
		catch (FileNotFoundException e) {          
			e.printStackTrace();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Writes the n most popular hashtags to the output file.
	 * @param outputFile
	 * @param outputLine
	 */
	void writeFile(File outputFile, String outputLine){
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(outputFile, true));
			bw.write(outputLine);
			bw.newLine();
			bw.flush();
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		finally 
		{                      
			if (bw != null)
			{
				try {
					bw.close();
				} catch (Exception e) {

					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Generates the Max Fibonacci heap and calls removeMax() for
	 * query number of times.
	 * @param queryString
	 */
	void process(String queryString){
		int query=0;
		try{
			query=Integer.parseInt(queryString) ;
		}
		catch(NumberFormatException e){
			e.getStackTrace();
		}
		String outputLine="";
		while(query>0){		
			//remove max node from fibonacci heap
			Node maxNode=fibonacciHeap.removeMax();
			Node removedNode=new Node(maxNode.getKey(),maxNode.getHashtag());
			removedNodes.add(removedNode);
			String maxNodeHashtag = removedNode.getHashtag();
			//remove the hashtable entry corresponding to the maxNode
			hashtable.remove(maxNodeHashtag);
			//Append to the outline string
			outputLine=outputLine+maxNodeHashtag+",";
			query--;
		}
		//write to the output file
		writeFile(output,outputLine.substring(0, outputLine.length()-1));

		//Add back the removed nodes to the Max Fibonacci heap
		for(int remNodeCount=0;remNodeCount<removedNodes.size();remNodeCount++){
			fibonacciHeap.insert(removedNodes.get(remNodeCount), removedNodes.get(remNodeCount).getKey());
			hashtable.put(removedNodes.get(remNodeCount).getHashtag(), removedNodes.get(remNodeCount));
		}
		//clear the removeNodes array list
		removedNodes.clear();
	}
}





