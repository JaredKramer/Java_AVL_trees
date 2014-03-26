import java.util.Scanner;
import java.io.*;
import java.util.Arrays;

//Jared Kramer
//CSE 373 HW3

/*
This file contains code not written by me as well as the following code written by me:
	Code that builds an avl tree given a data file
	Code that calls the get_record function from avl.java on data from a query file
		The query file contains a list of keywords
		The get_records function prints a list of articles associated with each of those keywords
	Code that calls the get_stats function from avl.java
		This function displays key stats about the insertions for the avl tree.
*/

public class test{

    private BufferedReader b;
    private avl a;


    public test(String filename){
       try{

           this.a = new avl();
           this.b = new BufferedReader(new FileReader(filename));
				/*
				This code creates the avl tree.
				First it creates a record for each keyword of each article.
				Then inserts a node into the tree with that record.
				See avl.java for more info on the insert functions.
				*/
				for(FileData dat = readNextRecord(); dat != null; dat = readNextRecord()){
					for (String key : dat.keywords){
						Record rec = new Record(dat.id, dat.author, dat.title, null);
						a.insert(key, rec);
					}
				}				
        } catch (IOException e) {
           e.printStackTrace();
        } finally {
            try {
                if (b!=null) b.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }

    }


    private void get_records(String filename){ 
        try{
            this.b = new BufferedReader(new FileReader(filename));
            /*
				This code reads queries from a query file and outputs
				the list of articles associated with each keyword.
				*/

				for(String k = b.readLine(); k != null; k = b.readLine()){
					Record art = this.a.get_record(k);
					
					System.out.println("KEYWORD: " + k);
					System.out.println();

					if (art == null){						
						System.out.println("NO TITLES FOUND");
						System.out.println();
					}
					else{
						while(art != null){
							System.out.println(art.id);
							System.out.println(art.title);
							System.out.println(art.author);
							System.out.println();
							art = art.next;	
						}
					}
				}
				
        } catch (IOException e) {
           e.printStackTrace();
        } finally {
            try {
                if (b!=null) b.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    private void get_stats(String filename){ 
        try{
            this.b = new BufferedReader(new FileReader(filename));

            /*
				This function displays the minimum, maximum and average number
				of nodes touched over all keywords in the list. 
				The avl class keeps track of a nodesTouched field,
				which is stored in an array. This array is used to generate the stats.
				*/
			int[] answer = new int[10];
			int i = 0;
			for(String k = b.readLine(); k != null; k = b.readLine()){
				this.a.get_record(k);
				if(i >= answer.length){
					answer = Arrays.copyOf(answer, answer.length*2);
				}
				answer[i] = this.a.nodesTouched;
				i++;
			}
			int min = answer[0];
			int max = answer[0];
			float sum = answer[0];
			
			for(int j = 1; j < i; j++){
				if (answer[j] < min)
					min = answer[j];
				if (answer[j] > max)
					max = answer[j];
				sum = sum + answer[j];
			}	 	
			System.out.println("The minimum number of nodes accessed in this tree is: " + min + "\n");
			System.out.println("The maximum number of nodes accessed in this tree is: " + max + "\n");
			System.out.println("The average number of nodes accessed in this tree is:: " + (sum/i) + "\n");

        } catch (IOException e) {
           e.printStackTrace();
        } finally {
            try {
                if (b!=null) b.close();
            } catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        if (args.length < 2)
            usage();
        else{
            test T = new test(args[0]);
            if (args[1].equals("print"))
                    T.a.print();
            else if (args[1].equals("tree_stats"))
                    T.a.tree_stats();
            else if (args[1].equals("get_records")){
                    if (args.length < 3)
                        usage();
                    else
                        T.get_records(args[2]);
	    }
            else if (args[1].equals("get_stats")){
                    if (args.length < 3)
                        usage();
                    else
                        T.get_stats(args[2]);
	    }
	    else {
                    usage();
	    }
        }
    }

//####################!!!!!!!!!!!!!!!!!#################
//####################!!!!!!!!!!!!!!!!!#################
//####################!!!!!!!!!!!!!!!!!#################
// you need not edit anything below this line

    private static void usage(){
        System.out.println("Usage: java test datafile.txt <command [query file]> ");
        System.out.println("Commands:");
        System.out.printf("print - prints tree\ntree_stats - output of tree_stats()\n" +
                "get_records [query file] - output of get_records(x) for each x in [query file]\n" +
                "get_stats [query file] - output of get_stats() over [query file]\n");
        System.exit(1);
    }
	public FileData readNextRecord(){
	/* Returns the next data record (a whole record object)
	 * in the data input file. Returns null if there
	 * is not such record. Hence a null indicates end of file or some error
	 * Error message will be displayed on the screen.
	 * DO NOT CHANGE THIS FUNCTION!
         */
		if(b == null){
			System.out.println("Error: You must open the file first.");
			return null;
		}
		else{
			FileData readData;
			try{
				String data=b.readLine();
				if(data==null)
					return null;
				int readNo = Integer.parseInt(data);
				readData= new FileData(readNo,b.readLine(),
					b.readLine(),Integer.parseInt(b.readLine()));		
				for(int i=0;i<readData.keywords.length;i++){
					readData.addKeyword(b.readLine());
				}
				String space=b.readLine();
				if((space!=null)&&(!space.trim().equals(""))){
					System.out.println("Error in file format");
					return null;
				}
			}
			catch(NumberFormatException e){
				System.out.println("Error Number Expected! ");
				return null;
			}
			catch(Exception e){
				System.out.println("Fatal Error: "+e);
				return null;
			}	
			return readData;
		}
	}
}

class FileData{
/** Class: FileData
 *  Contains the content of a record found in the input file. Each 
 *  FileData object contains exactly one record. An object of this
 *  type will be returned by readNextRecord(..) function on successful
 *  read. 
 *  Fields:
 *  id : ID of the record
 *  title : contains the title of the paper
 *  author: contains the author of the paper
 *  keywords is an array of all keywords related to that paper.
 */ 


	int id;
	String title;
	String author;
	String keywords[];

	/* Constructor */
	FileData(int id, String title, String author, int keywordCount){
		this.id=id;
		this.title=title;
		this.author=author;
		keywords=new String[keywordCount];
		for(int i=0;i<keywords.length; i++){
			keywords[i]=null;
		}
	} 

	/* Returns true if the keyword was successfully added 
	 * Keyword addition might fail if it does not meet the 
	 * original limit. This method adds a single keyword to the
         * keywords array in the end. This method will be invoked
         * by the getNextRecord() function at the time of building 
         * an object of this type
         */
	boolean addKeyword(String keyword){
		for(int i=0;i<keywords.length;i++){
			if(keywords[i]==null){
				keywords[i]=keyword;
				return true;
			}
		}
		return false;
	}

}
