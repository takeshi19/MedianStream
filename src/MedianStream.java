import java.io.File;



import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class MedianStream
{

    private static final String PROMPT_NEXT_VALUE = "Enter next value or q to quit: ";
    private static final String MEDIAN = "Current median: ";
    private static final String EXIT_MESSAGE = "That wasn't a double or 'q'. Goodbye!";
    private static final String FNF_MESSAGE = " not found.";

    /**
     * Use this format to ensure that double values are formatted correctly.
     * Double doubleValue = 1412.1221132
     * System.out.printf(DOUBLE_FORMAT, doubleValue);
     */
    private static final String DOUBLE_FORMAT = "%8.3f\n";

    private Double currentMedian;  //The current version of the median value out of the two sets of data below.
    private MaxPQ<Double> maxHeap; //Data set 1: the heap that contains the largest value computed. 
    private MinPQ<Double> minHeap; //Data set 2: the heap that contains the smallest value computed.

    /**
     * Override Default Constructor
     *
     *  Initialize the currentMedian = 0.0
     *  Create a new MaxPQ and MinPQ.
     */
    public MedianStream()
    {
        this.currentMedian = 0.0;
        this.maxHeap = new MaxPQ<Double>();
        this.minHeap = new MinPQ<Double>();
    }

    /**
     * This method is called if the user passes NO command line arguments.
     * The method prompts the user for a double value on each iteration.
     *
     * If the input received is a double, the current median is updated.
     * After each iteration, print the new current median using MEDIAN string
     * as declared and initialized with the data members above.
     *
     * If the input is the character 'q', return from the method.
     *
     * If the input is anything else, then you print an error using EXIT_MESSAGE
     * string as declared and initialized with the data members above and
     * then return from the method.
     *
     * For the purposes of calculating the median, every input received since
     * the beginning of the method execution is part of the same stream.
     */
    private static void runInteractiveMode() {	
		MedianStream findMedian = new MedianStream(); 
		Scanner scnr = new Scanner(System.in); //Scanner for reading in keyboard-input.
		boolean keepAsking = true;	//Boolean value to maintain the infinite loop of asking user for double input.
		
		while (keepAsking == true) {
			System.out.print(PROMPT_NEXT_VALUE);
			String input = scnr.nextLine().trim();	//We initially get string input just in case user enters "q" first.
			
			if (input.trim().equals("q")) { 		//"q" input means quit the interactive mode version of the program.
				keepAsking = false;
			}
			else if (input.matches(".*\\d+.*")) {
				try {
					Double doubleIn = Double.parseDouble(input); 
					Double currMedian = findMedian.getMedian(doubleIn); //Calculates median whenever user inputs double.
					System.out.print(MEDIAN); 					  		//Display current median.
					System.out.printf(DOUBLE_FORMAT, currMedian); //Then (on same line) show properly formated median.
				}catch(NumberFormatException num) {
					keepAsking = false;
				}
			}
			else { //User has not entered a double or a "q", notify invalid input.
				System.out.println(EXIT_MESSAGE);
				keepAsking = false;
			}
		}
		if (scnr != null) { //Close scanner when the loop breaks and we are done with program in interactive mode.
			scnr.close();
		}
	} 

    /**
     * This method is called if the user passes command line arguments.
     * The method is called once for every filename passed by the user.
     *
     * The method reads values from the given file and writes the new median
     * after reading each new double value to the output file.
     *
     * The name of the output file follows a format specified in the write-up.
     *
     * If the input file contains a non-double value, the program SHOULD NOT
     * throw an exception, instead it should just read the values up to that
     * point, write medians after each value up to that point and then
     * return from the method.
     *
     * If a FileNotFoundException occurs, just print the string FNF_MESSAGE
     * as declared and initialized with the data members above.
     */
    private static void findMedianForFile(String filename) {
		MedianStream findMedian = new MedianStream(); 
    	Scanner fileIn = null;     //Scanner to scan through data of inputFile.
    	File inputFile = null;	   //The input file that holds the various temperatures (doubles). 
    	PrintWriter writer = null; //Enables the writing of data from inputFile to an output file.

    	try {
    		inputFile = new File(filename);
    		fileIn = new Scanner(inputFile);
    		filename = filename.substring(0, filename.lastIndexOf('.')); //TODO
    		System.out.println(filename);
    		writer = new PrintWriter(filename + "_out.txt"); //Creating an output file with a custom name.
    		
    		while (fileIn.hasNextDouble()) { //While there are still Doubles to be read from the file, keep processing.
    			Double dataFromFile = fileIn.nextDouble(); 			//Get a double from the file.
    			Double median = findMedian.getMedian(dataFromFile); //Update median based on the data from this input.
				writer.printf(DOUBLE_FORMAT, median);				//Write the medians to each line of the output file. 
    		}			
    	} catch(FileNotFoundException e) {
    		System.out.println(filename + FNF_MESSAGE);
    	} finally {
    		writer.close();
    		fileIn.close();
    	}
    }

    /**
     *
     * Adds the new temperature reading to the corresponding
     * maxPQ or minPQ depending upon the current state.
     *
     * Then calculates and returns the updated median.
     *
     * @param newReading - the new reading to be added.
     * @return the updated median.
     */
    private Double getMedian(Double newReading){
    	Double rootFromMin = 0d; //Value of removing root from the minPQ to add to the maxPQ heap.
    	Double rootFromMax = 0d; //Value of removing root from the maxPQ to add to the minPQ heap.
    	
    	if (newReading > currentMedian) { 			//Insert newReading as an item in minHeap if it's > currentMedian. 
    		if (minHeap.size() == maxHeap.size()) {  
    			minHeap.insert(newReading);		//If min/maxPQ equivalent in size, still insert newReading in minHeap.
    			currentMedian = minHeap.getMax();   					//Get updated root after insertion into minPQ. 
    		}
    		else if (minHeap.size() == maxHeap.size() - 1) {
    			minHeap.insert(newReading); 	//Inserting into minHeap makes minHeap size and maxHeap size equivalent.
    			currentMedian = (minHeap.getMax() + maxHeap.getMax()) / 2d; //Calculate average of 2 heaps for newMedian.
    		}
    		/*
    		 * If minHeap size < maxHeap size, then find the average of the two roots after insertion of newReading 
    		 * value. Similar concept to finding the average of two sets by taking the Nth largest value from one set
    		 * and adding it to the Nth smallest value from the other set and diving by two (for a total of N*2 items).
    		 */
    		else if (minHeap.size() == maxHeap.size() + 1){ //If minHeap size < maxHeap size 
    			rootFromMin = minHeap.removeMax();
    			maxHeap.insert(rootFromMin);
    			minHeap.insert(newReading);
    			currentMedian = (minHeap.getMax() + maxHeap.getMax()) / 2d; 
    		}
    	}
    	else if (newReading < currentMedian) {  	//Insert newReading as an item in maxHeap if it's < currentMedian.
    		if (maxHeap.size() == minHeap.size()) {  
    			maxHeap.insert(newReading);		//If min/maxPQ equivalent in size, still insert newReading in maxHeap.
    			currentMedian = maxHeap.getMax();   				  //Get updated root after insertion into maxHeap.
    		}
    		else if (maxHeap.size() == minHeap.size() - 1) {
    			maxHeap.insert(newReading);   //Inserting into maxHeap makes minHeap size and minHeap size equivalent.
    			currentMedian = (maxHeap.getMax() + minHeap.getMax()) / 2d; //Calculate average of 2 heaps for median.
    		}
    		else if (maxHeap.size() == minHeap.size() + 1){ 
    			rootFromMax = maxHeap.removeMax(); 				
    			minHeap.insert(rootFromMax);
    			maxHeap.insert(newReading);
    			currentMedian = (maxHeap.getMax() + minHeap.getMax()) / 2d; 
    		}
    	}
    	return currentMedian; //Return the new currentMedian after user inputs a new value to either set/PQ.
    }

    // DO NOT EDIT THE main METHOD.
    public static void main(String[] args)
    {
        // Check if files have been passed in the command line.
        // If no files are passed, run an infinite interactive loop taking a double
        // input each time until "q" is entered by the user.
        // After each iteration of the loop, update and display the new median.
        if ( args.length == 0 )
        {
            runInteractiveMode();
        }

        // If files are passed in the command line, open each file.
        // For each file, iterate over all the double values in the file.
        // After reading each new double value, write the new median to the
        // corresponding output file whose name will be inputFilename_out.txt
        // Stop reading the file at the moment a non-double value is detected.
        else
        {
        	System.out.println("boner?");
            for ( int i=0 ; i < args.length ; i++ )
            {
            	System.out.println("dianna chain tea");
                findMedianForFile(args[i]);
            }
        }
    }
}
