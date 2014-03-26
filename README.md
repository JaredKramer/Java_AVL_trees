Java_AVL_trees
==============

Automatically balancing AVL trees in Java

This is a class for an avl tree designed to function as a search tree for articles,
as well as code to test the avl class. In the avl tree, each node in the tree contains 
a keyword and a pointer to a linked list of articles associated with that keyword.
The avl tree is deisgned to have a worst case O(logN) complexity.
Whenever the difference in height between two sub trees is more than the a pre set limit,
(here it is 1), the tree calls one of four rotation methods to re-balance the tree.
