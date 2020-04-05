/**
 * 
 * @author Rishabh Aryan Das
 *
 */
public class Node {
	String hashtag;
	int key;
	int nodeDegree;
	boolean childCut;
	Node childNode;									
	Node leftNode;									
	Node parentNode;									
	Node rightNode;									
	/**
	 * default constructor
	 */
	public Node() {
	}
	/**
	 * Constructor
	 * @param key
	 */
	public Node(int key) {
		rightNode = this;
		leftNode = this;
		this.key = key;
	}
	/**
	 * Constructor
	 * @param key
	 * @param hashtag
	 */
	public Node(int key, String hashtag) {
		rightNode = this;
		leftNode = this;
		this.key = key;
		this.hashtag = hashtag;
	}
	/**
	 * Returns key of the Node called.
	 * @return int
	 */
	public final int getKey() {
		return key;
	}
	/**
	 * Returns the hashtag of the Node called.
	 * @return String
	 */
	public final String getHashtag() {
		return hashtag;
	}
}
