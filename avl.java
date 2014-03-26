
//Jared Kramer

/*
This is a class for an avl tree designed to function as a search tree for articles.
Each node in the tree contains a keyword and a pointer 
to a linked list of articles associated with that keyword.
The avl tree is designed to have a worst case O(logN) complexity.
Whenever the difference in height between two sub trees is more than the a pre-set limit,
(here it is 1), the tree calls one of four rotation methods to re-balance the tree.

Also included is a class for a node for the avl tree.	
The functions are listed here. Further description of each function is availble below.
	height
	balance
	2 single rotation functions
	2 double rotation functions
	contains
	insert
	get record
	print
	tree stats
	
	EXTRA CREDIT:
	printLisp

*/


/*
This is the avl tree node class. 
Each node has the following fields:
	keyword
	left child
	right child
	height
	a pointer to a linked list of article records
*/
public class avl{
    
    private node root;
    private static final int BAL = 1;
	 private int noBalance;
	 private int oneBalance;
	 private int twoBalance;
	 public int nodesTouched;

    private class node{
		
		node(String k, Record rec){
			this(k, null, null, rec);
		}
		
		public node(String k, node lt, node rt, Record rec){
			this.k = k;
			this.left = lt;
			this.right = rt;
			this.height = 0;
			this.rec = rec;
		}
		String k; 		//data in the node
		node left;		//Left child
		node right;		//Right child
		int height;		//Height
		Record rec;		//Record
    }


	/*
	This method returns the height of a given node.
	The height is used to test whether or not the tree is balanced.
	*/
	private int height(node t){
		return t == null? -1 : t.height;
	}

	/*
	This is the balance method.
	This method compares the heights of two subtrees.
	If the sub trees are unbalanced, 
	this method calls the proper rotation methods to balance the tree.
	*/
    private node balance(node t) {
		if (t == null){
			this.noBalance++;
			return t;
		}
		if( height( t.left ) - height( t.right ) <= BAL && height( t.right ) - height ( t.left ) <= BAL)
			this.noBalance++;
		
		if( height( t.left) - height( t.right ) > BAL){
			if( height(t.left.left ) >= height( t.left.right) ){
				this.oneBalance++;
				t = rotateWithLeftChild( t );
			}
			else{
				this.twoBalance++;
				t = doubleWithLeftChild( t );
			}
		}
		else
		if( height( t.right ) - height( t.left ) > BAL){
			if( height( t.right.right ) >= height( t.right.left) ){
				this.oneBalance++;
				t = rotateWithRightChild( t );
			}
			else{
				this.twoBalance++;
				t = doubleWithRightChild( t );
			}
		}
		t.height = Math.max( height( t.left ), height( t.right ) ) + 1;
		return t;
	 }
        

    /*  
	The following four methods are the rotation methods.	 
	In each case, the method chanegs the pointers of nodes to rearrange the tree.
	Each rotation also reassigns the proper height to the nodes being moved.
	The four rotation cases covered by these methods are:
		Single rotation: left of left and right of right
		Double rotation: left of right and right of left
	*/
	
	//left of left
    private node rotateWithLeftChild(node k2) {
	 	node k1 = k2.left;
		k2.left = k1.right;
		k1.right = k2;
		k2.height = Math.max( height( k2.left), height( k2.right ) ) +1;
		return k1;
	 }
    
	 //right of right
	 private node rotateWithRightChild(node k2) {
	 	node k1 = k2.right;
	 	k2.right = k1.left;
		k1.left = k2;
		k2.height = Math.max( height( k2.left), height( k2.right ) ) +1;
		return k1;
	 }
  
  	 // right of left
    private node doubleWithLeftChild(node k3) {
	 	k3.left = rotateWithRightChild( k3.left);
		return rotateWithLeftChild( k3 );
	 }
	 
	 //left of right
    private node doubleWithRightChild(node k3) {
	 	k3.right = rotateWithLeftChild( k3.right );
		return rotateWithRightChild( k3 );
	 
	 }

    /*
	 This method recursively searches the tree for a node with a given keyword,
	 it returns true if the tree contains the keyword.
	 */
    private boolean contains(String k, node t) {
	 	if(t.k.equals(k))
			return true;
		else{
			t = t.left;
			boolean temp = contains(k, t);
			t = t.right;
			temp = temp || contains(k, t);
			return temp;
	 	}
	 }
	 
	 //public contains function that calls the private contains function above.
    public boolean contains(String k) {
	 	return contains(k, root); 
	 }
    
	 /*
	 This is the insert function.
	 When passed a keyword, one of several things may happen:
	 
	 If the tree is empty, a new node will be created to start the tree.
	 If the tree is not empty, function recursively traverses the tree to
	 insert at proper place.
	 If a node already exists for that keyword, 
	 the new record is attached to the linked list for that node.
	 */
    private node insert(String k, node t, Record rec) {
	 	if(t == null)
			return new node(k, null, null, rec);
		
		int compareResult = k.compareTo(t.k);
		
		if(compareResult < 0)
			t.left = insert(k, t.left, rec);
		else if(compareResult > 0)
			t.right = insert(k, t.right, rec);
		else{
			rec.next = t.rec;
			t.rec = rec;
		}
		return balance( t );
	 }
	 
	 //public insert function that calls the private insert function above.
    public void insert(String k, Record rec) {
	 	if(root == null){
	 		node temp = new node(k, rec);
			this.root = temp;
			this.noBalance++;
		}
		else	
			root = insert(k, root, rec);
	 }
    
    /*
	 This function searches the rtee for a keyword,
	 upon finding the keyword, it returns the first Record
	 in a linked list of records associated with that keyword.
	 This function also keeps track of the nodes touched,
	 for use in the get_stats function.
	 */
    public Record get_record(String k) {		
	 	node walker = this.root;
		this.nodesTouched = 1;
		while(walker != null){
			int comparator = k.compareTo(walker.k);
			if(comparator == 0){
				return walker.rec;
			}
			else{
				this.nodesTouched++;
				if(comparator > 0){
					walker = walker.right;
				}
				else{
					walker = walker.left;
				}
			}
		}
		return null;
	 }
	
	//public print funciton that calls the private print function below, unless the tree is empty.
    public void print(){
		if(root == null){
			System.out.println("Empty Tree");
			return;
		}
		else
			print(root);
	 }
	 
	 /*
	 Recursive function that prints each keyword in the tree 
	 followed by an indented list of articles associated with that keyword.
	 */
    private void print(node t) {
	 	if ( t != null ){
			print( t.left );
			System.out.println( t.k );
			Record walker = t.rec;
			while(walker != null){
				System.out.println("\t" + walker.title);
				walker = walker.next;
			}
			print( t.right );
		}
	 }
	 
	 /*
	 This is the extra credt lispPrint function.
	 This function prints out an abbreviated form of the tree, 
	 where each node is marked by parentheses around the keyword for that node.
	 */
	 private void printLisp(node t, String tabs){
		System.out.print("(" + t.k + " ");
		if(t.left != null)
			printLisp(t.left, tabs + "    ");
		else
			System.out.print("null ");
		if(t.right != null){
			System.out.println();
			System.out.print(tabs);
			printLisp(t.right, tabs + "    ");
		}
		else
			System.out.print("null");
		System.out.print(")");
	 }	

	 //private printLisp function that calls the printLisp function above.
	 public void printLisp(){
	 	printLisp(root, "");
	 }
	 	 
	 
    /*
	 This function prints out key statistics for the tree, including:
	 	the number of insertions that did not require the use of a rotate function
		the number of insertions that required a single rotation
		the number of rotations that required a double rotation
	 */
    public void tree_stats() {
	 	System.out.print("Number of insertions that required no balancing: ");
		System.out.println(this.noBalance);
		
		System.out.print("Number of insertions that required only a single rotation: ");
		System.out.println(this.oneBalance);
		
		System.out.print("Number of insertions that required a double rotation: ");
		System.out.println(this.twoBalance);
	 }

}
