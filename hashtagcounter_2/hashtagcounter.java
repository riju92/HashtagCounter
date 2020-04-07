import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.io.FileOutputStream;

/**
 * 
 * @author Rishabh Aryan Das
 *
 */
public class hashtagcounter {
	
	static StringBuilder sb = new StringBuilder(); // global parameter sb declared to store the ouput string
	/**
	 * Writes the n most popular hashtags to the output file.
	 * @param outputFile
	 * @param sb
	 */

	public static void writeFile(File outputFile, StringBuilder sb)
	{
			BufferedWriter bw = null;
			try {
				bw = new BufferedWriter(new FileWriter(outputFile));
				bw.write(sb.toString());
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
	* Generates the Max Fibonacci heap and calls deleteMax() for
	* query number of times.
	* @param output
	* @param removedNodes
	* @param fh
	* @param queryString
	* @param hashtable
	*/
	public static void process(File output,ArrayList<Node> removedNodes,FibonacciHeap fh, String queryString, Hashtable<String, Node> hashtable)
	{
			int query=0;
			try{
				query=Integer.parseInt(queryString) ;
			}
			catch(NumberFormatException e){
				e.getStackTrace();
			}
			String outputLine="";
			while(query>0)
			{		
				//remove max node from fibonacci heap
				Node maxNode=fh.deleteMax();
				Node removedNode=new Node(maxNode.getKey(),maxNode.getHashtag());
				removedNodes.add(removedNode);
				String maxNodeHashtag = removedNode.getHashtag();
				//remove the hashtable entry corresponding to the maxNode
				hashtable.remove(maxNodeHashtag);
				//Append to the outline string
				outputLine=outputLine+maxNodeHashtag+",";
				query--;
			}
			
			sb.append(outputLine.substring(0, outputLine.length()-1) + "\n");// to append the output lines to the stringbuilder to write to the output file
			//System.out.println(sb);
			//write to the output file
			writeFile(output,sb);

			//Add back the removed nodes to the Max Fibonacci heap
			for(int remNodeCount=0;remNodeCount<removedNodes.size();remNodeCount++){
				fh.addNode(removedNodes.get(remNodeCount), removedNodes.get(remNodeCount).getKey());
				hashtable.put(removedNodes.get(remNodeCount).getHashtag(), removedNodes.get(remNodeCount));
			}
			//clear the removeNodes array list
			removedNodes.clear();
		
	}

	public static void main(String[] args)
	{
		// to validate if a valid input filename is present in commandline argument
		if(args.length == 0)
		{
			System.out.println("Please provide a valid input filename"); 
			return;
		}
		
		//input file
		String inputFile = args[0];

		//output file
		File output;
		if(args.length > 1)
		{
			output = new File(args[1]);   
		}
		else
		{
			output = new File("output_file.txt");
		}

		//ArrayList to store removed Nodes
		ArrayList<Node> removedNodes = new ArrayList<Node>();
		
		//Max Fibonacci heap
		FibonacciHeap fh = new FibonacciHeap();
		
		//Hash table
		Hashtable<String, Node> hashtable = new Hashtable<String, Node>();
		//hashtagcounter hc = new hashtagcounter(); //new code

		/**
		* Reads each line of the input file and processes accordingly.
		* @param inputFile
		*/
		try {
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			String inputLine;
			
			while ((inputLine = in.readLine()) != null) 
			{
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
						fh.addNode(newNode, frequency);
						hashtable.put(hashtag,newNode);
					}
					//If the node already exists, increment its frequency.
					else{
						int newFrequency=hashtable.get(hashtag).getKey()+frequency;
						Node x=fh.increaseKey(hashtable.get(hashtag),newFrequency);
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
						//If query, generate the fibonacci heap and call deleteMax()
						process(output, removedNodes, fh, inputLine , hashtable);
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
}
