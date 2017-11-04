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

    private Double currentMedian;
    private MaxPQ<Double> maxHeap;
    private MinPQ<Double> minHeap;

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
		MedianStream findMedian = new MedianStream(); //Instance of this class to call on findMedian() to do math.
		Scanner scnr = new Scanner(System.in);
		boolean keepAsking = true;	//Boolean value to maintain the infinite loop of asking user for double input.
		
		while (keepAsking == true) {
			System.out.print(PROMPT_NEXT_VALUE);
			String input = scnr.nextLine().trim();	//We initially get string input just in case user enters "q" first.
			
			if (input.equalsIgnoreCase("q")) { 		//"q" input means quit the interactive mode version of the program.
				keepAsking = false;
			}
			else {
				try {
					Double doubleIn = Double.parseDouble(input); 
					Double currMedian = findMedian.getMedian(doubleIn); //Calculates median whenever user inputs double.
					System.out.print(MEDIAN); 					  		//Display current median.
					System.out.printf(DOUBLE_FORMAT, currMedian); //Then (on same line) show properly formated median.
				}catch(NumberFormatException num) {
					keepAsking = false;
				}
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
    private static void findMedianForFile(String filename)
    {

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
    	Double newMedian = 0d; //The new median value calculated from calling this method.
    	
    	if (newReading > currentMedian) { 		//Insert newReading as an item in minHeap if it's > currentMedian. 
    		if (minHeap.size() == maxHeap.size()) {  
    			minHeap.insert(newReading);		//If min/maxPQ equivalent in size, still insert newReading in minHeap.
    			newMedian = minHeap.getMax();   //Get updated root after insertion into minPQ. 
    		}
    		else if (minHeap.size() == maxHeap.size() - 1) {
    			minHeap.insert(newReading); //Inserting into minHeap makes minHeap size and maxHeap size equivalent.
    			newMedian = (minHeap.getMax() + maxHeap.getMax()) / 2; //Calculate average of 2 heaps for newMedian.
    		}
    		else if (minHeap.size() == maxHeap.size() + 1){ //FIXME comments
    			Double fromMin = minHeap.removeMax();
    			maxHeap.insert(fromMin);
    			minHeap.insert(newReading);
    			newMedian = (minHeap.getMax() + maxHeap.getMax()) / 2; 
    		}
    	}
    	else {  								//Insert newReading as an item in maxHeap if it's < currentMedian.
    		if (minHeap.size() == maxHeap.size()) {  
    			maxHeap.insert(newReading);		//If min/maxPQ equivalent in size, still insert newReading in maxHeap.
    			newMedian = maxHeap.getMax();   //Get updated root after insertion into maxHeap. 
    		}
    		else if (maxHeap.size() == minHeap.size() - 1) {
    			maxHeap.insert(newReading); //Inserting into maxHeap makes minHeap size and minHeap size equivalent.
    			newMedian = (maxHeap.getMax() + minHeap.getMax()) / 2; //Calculate average of 2 heaps for newMedian.
    		}
    		else if (maxHeap.size() == minHeap.size() + 1){ //FIXME comments
    			Double fromMin = maxHeap.removeMax();
    			minHeap.insert(fromMin);
    			maxHeap.insert(newReading);
    			newMedian = (maxHeap.getMax() + minHeap.getMax()) / 2; 
    		}
    	}
    	return newMedian;
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
        	System.out.println("in main()");
            runInteractiveMode();
        }

        // If files are passed in the command line, open each file.
        // For each file, iterate over all the double values in the file.
        // After reading each new double value, write the new median to the
        // corresponding output file whose name will be inputFilename_out.txt
        // Stop reading the file at the moment a non-double value is detected.
        else
        {
            for ( int i=0 ; i < args.length ; i++ )
            {
                findMedianForFile(args[i]);
            }
        }
    }
}
