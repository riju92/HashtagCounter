/**
 * 
 * @author Rishabh Aryan Das
 *
 */
public class FibonacciHeap 
{
	/** to store the maximum node in Fibonacci heap. */
	private Node maxNode;
	/** to store the number of nodes in Fibonacci heap. */
	private int nodeCount;

	/**
	 * Function isHeapEmpty returns TRUE if the heap is empty
	 * and FALSE otherwise.
	 * @return
	 */
	public boolean isHeapEmpty() {
		if(maxNode == null)
			return Boolean.TRUE;
		else
			return Boolean.FALSE;
	}

	/**
	 * Function to inserts nodes into Fibonacci heap.
	 * @param newNode
	 * @param key
	 */
	public void addNode(Node newNode, int key) {
		newNode.key = key;
		//Heap is not empty
		if(maxNode != null) {                  
			newNode.leftNode = maxNode;        
			newNode.rightNode = maxNode.rightNode;
			maxNode.rightNode = newNode;
			newNode.rightNode.leftNode = newNode;
			//update maxNode with newNode
			if(key > maxNode.key) {             
				maxNode = newNode;
			}
		}
		//Heap is empty.
		else {                                  
			maxNode = newNode;
		}
		//Incrementing the node count after insertion.
		nodeCount++;                           
	}

	/**
	 * Increases the key of the given node
	 * by the value passed.
	 * @param current
	 * @param newKey
	 * @return Node
	 */
	public Node increaseKey(Node current, int newKey) {
		Node parent=null;
		current.key = newKey;                    
		parent = current.parentNode;
		//If the current node's key is greater than that of its parent
		//then call cut and cascadeCut to preserve
		//Max Fibonacci heap properties
		if((parent != null) && (current.key > parent.key)) { 
			cut(current, parent);
			cascadeCut(parent);
		}
		//Updating the maxNode with current node if maxNode is greater
		//than that of current node
		if(current.key > maxNode.key) {          
			maxNode = current;
		}
		return current;
	}

	/**
	 * Removes and returns the maxNode from the
	 * Max Fibonacci heap
	 * @return Node
	 */
	public Node deleteMax() {
		Node nodeToRemove = maxNode;
		if(nodeToRemove != null) {
			int  childCount = nodeToRemove.nodeDegree;
			Node child = nodeToRemove.childNode;
			Node tempRight;
			// for each child of the node to delete...
			while(childCount > 0) {
				tempRight = child.rightNode;
				//remove child from child list
				child.leftNode.rightNode = child.rightNode;
				child.rightNode.leftNode = child.leftNode;
				//add the child to root list
				child.leftNode = maxNode;
				child.rightNode = maxNode.rightNode;
				maxNode.rightNode = child;
				child.rightNode.leftNode = child;
				//set the parent of child to null
				child.parentNode = null;
				child = tempRight;
				childCount--;
			}
			//remove the node to delete from the root list
			nodeToRemove.leftNode.rightNode = nodeToRemove.rightNode;
			nodeToRemove.rightNode.leftNode = nodeToRemove.leftNode;
			if(nodeToRemove == nodeToRemove.rightNode) {
				maxNode = null;
			}
			else {
				maxNode = nodeToRemove.rightNode;
				pairwiseCombine();
			}
			//decrement the heap size
			nodeCount--;
		}
		//return the removed maxNode
		return nodeToRemove;
	}

	/**
	 * removes child from the child list of parent.
	 * @param child
	 * @param parent
	 */
	protected void cut(Node child, Node parent) {
		//remove the node from the child list of parent
		child.leftNode.rightNode = child.rightNode;
		child.rightNode.leftNode = child.leftNode;
		parent.nodeDegree--;
		//set the child of parent to appropriate node
		if(parent.childNode == child) {
			parent.childNode = child.rightNode;
		}
		if(parent.nodeDegree == 0) {
			parent.childNode = null;
		}
		//add the child to the root list of heap
		child.leftNode = maxNode;
		child.rightNode = maxNode.rightNode;
		maxNode.rightNode = child;
		child.rightNode.leftNode = child;
		child.parentNode = null;
		child.childCut = false;
	}

	/**
	 * Cuts the child from its parent till a parent with
	 * childCut value FALSE is encountered.
	 * @param child
	 */
	protected void cascadeCut(Node child) 
	{
		Node parent = child.parentNode;
		if(parent != null) {
			if(!child.childCut) {
				child.childCut = true;
			}
			else {
				cut(child, parent);
				cascadeCut(parent);
			}
		}
	}

	/**
	 * Combines the trees in the heap by joining trees of equal degree
	 * until there are no more trees of equal degree in the root list.
	 */
	protected void pairwiseCombine() 
	{
		int arraySize = nodeCount + 1;
		Node[] roots = new Node[arraySize];
		for(int i = 0; i < arraySize; i++) {
			roots[i] = null;
		}
		// Find the number of root nodes.
		int numRoots = 0;
		Node x = maxNode;

		if(x != null) {
			numRoots++;
			x = x.leftNode;

			while(x != maxNode) {
				numRoots++;
				x = x.leftNode;
			}
		}
		while(numRoots > 0) {
			int d = x.nodeDegree;
			Node next = x.leftNode;
			while(roots[d] != null) { //if there exists a root node with same degree
				Node y = roots[d];
				if(x.key < y.key) {
					Node temp = y;
					y = x;
					x = temp;
				}
				
				// remove y from root list of heap
				y.leftNode.rightNode = y.rightNode;
				y.rightNode.leftNode = y.leftNode;
				y.parentNode = x;
				if(x.childNode == null) {
					x.childNode = y;
					y.rightNode = y;
					y.leftNode = y;
				}
				else {
					y.leftNode = x.childNode;
					y.rightNode = x.childNode.rightNode;
					x.childNode.rightNode = y;
					y.rightNode.leftNode = y;
				}
				//Increment the degree of the x
				x.nodeDegree++;
				//Set the childCut of y to FALSE
				y.childCut = false;

				roots[d] = null;
				d++;
			}
			roots[d] = x;
			x = next;
			numRoots--;
		}
		maxNode = null;
		// Reconstruct the root list from the array entries in roots[].
		for(int i = 0; i < arraySize; i++) {
			if(roots[i] != null) {           
				if(maxNode != null) {
					roots[i].leftNode.rightNode = roots[i].rightNode;
					roots[i].rightNode.leftNode = roots[i].leftNode;
					roots[i].leftNode = maxNode;
					roots[i].rightNode = maxNode.rightNode;
					maxNode.rightNode = roots[i];
					roots[i].rightNode.leftNode = roots[i];
					//Setting the maxNode
					if(roots[i].key > maxNode.key) {           
						maxNode = roots[i];
					}
				}
				else {
					maxNode = roots[i];
				}
			}
		}
	}
}
