package COMMON_CARDB;

/**
 * @author Chad Fortune
 *
 */

import java.io.*;

public class FileIO {

	// ------------- Declare attributes

	private static final int maxNumLines = 1000; // a constant
	private int numLines;
	private String[] strLinesArray;

	// ------------- Declare constructors
	// Default constructor
	public FileIO() {

		// Initialize with 0
		numLines = 0;

		// For simplicity, allocate space for maxNumLine lines,
		// adequate for normal files
		strLinesArray = new String[maxNumLines];

		// Initialize all the elements with empty string
		java.util.Arrays.fill(strLinesArray, "");

	} // End of default constructor FiLEIO

	// Another constructor
	public FileIO(File aFile) throws FileNotFoundException, IOException {

		// Initialize with 0
		// numLines = 0;

		// For simplicity, allocate space for maxNumLine lines,
		// adequate for normal files
		strLinesArray = new String[maxNumLines];

		// Initialize all the elements with empty string
		java.util.Arrays.fill(strLinesArray, "");

		try {
			numLines = readLinesFromFile(aFile);
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// ------------- GET & SET methods

	public static int getMaxNumLines() {
		return (maxNumLines);
	}

	public int getNumLines() {
		return (numLines);
	}

	public void setNumLines(int aNumLines) {
		numLines = aNumLines;
	}

	public String[] getStrLinesArray() {
		return (strLinesArray);
	}

	public void setStrLinesArray(String[] aStrArray) {

		for (int i = 0; i < aStrArray.length; i++) {
			// Copy element by element from aStrArray to strLinesArray
			strLinesArray[i] = aStrArray[i];
		}
	}

	// ------------- File IO methods

	public static void checkAndThrowExceptionsForInputFile(File aFile) throws FileNotFoundException, IOException {

		if (aFile == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		if (!aFile.exists()) {
			throw new FileNotFoundException("File does not exist: " + aFile);
		}
		if (!aFile.isFile()) {
			throw new IllegalArgumentException("Should not be a directory: " + aFile);
		}
		if (!aFile.canRead()) {
			throw new IllegalArgumentException("File cannot be read: " + aFile);
		}

	} // End of checkAndThrowExceptionsForInputFile

	public static void checkAndThrowExceptionsForOutputFile(File aFile) throws FileNotFoundException, IOException {

		if (aFile == null) {
			throw new IllegalArgumentException("File should not be null.");
		}
		if (!aFile.exists()) {
			throw new FileNotFoundException("File does not exist: " + aFile);
		}
		if (!aFile.isFile()) {
			throw new IllegalArgumentException("Should not be a directory: " + aFile);
		}
		if (!aFile.canWrite()) {
			throw new IllegalArgumentException("File cannot be written: " + aFile);
		}

	} // End of checkAndThrowExceptionsForOutputFile

	/**
	 **********************************************************************************************
	 * This method changes the contents of text file in its entirety,
	 * overwriting any existing text. This style of implementation throws all
	 * exceptions to the caller.
	 * 
	 * This method uses the following classes: File, Writer, FileWriter,
	 * BufferedWriter This method uses FileWriter --> using the default coding
	 * scheme of the system
	 * ********************************************************************************************
	 */

	public void setContents(File aFile, String aContents) throws FileNotFoundException, IOException {

		checkAndThrowExceptionsForOutputFile(aFile);

		// use buffering
		Writer output = new BufferedWriter(new FileWriter(aFile));

		try {
			// FileWriter always assumes default encoding is OK!
			output.write(aContents);
		} finally {
			output.close();
		}

	} // End of setContents

	/**
	 **********************************************************************************************
	 * This method fetches the content of the whole file and returns the content
	 * as a String The String contains "lineSeparator" This style of
	 * implementation throws all exceptions to the caller.
	 * 
	 * This method uses the following classes: File, StringBuilder,
	 * BufferedReader, FileReader This method uses readLine() --> returns the
	 * content of a line MINUS the newline --> returns null only for the END of
	 * the stream --> returns an empty String if two newlines appear in a row
	 * ********************************************************************************************
	 */

	public String getContents(File aFile) {

		// Create an object "contents" of class StringBuilder
		StringBuilder contents = new StringBuilder();

		try { // try #1

			// use buffering, reading one line at a time
			// FileReader always assumes default encoding is OK!

			BufferedReader input = new BufferedReader(new FileReader(aFile));

			try { // try #2

				String line = null; // not declared within while loop

				// readLine is a bit quirky :
				// it returns the content of a line MINUS the newline.
				// it returns null only for the END of the stream.
				// it returns an empty String if two newlines appear in a row.

				while ((line = input.readLine()) != null) {

					contents.append(line);
					contents.append(System.getProperty("line.separator"));

				} // End of while

			} // End of try #2
			finally { // try #2
				input.close();
			}

		} // End of try #1
		catch (IOException ex) { // try #1
			ex.printStackTrace();
		}

		return contents.toString();

	} // End of getContents()

	/**
	 **********************************************************************************************
	 * This method appends one line to an existing file This style of
	 * implementation throws all exceptions to the caller.
	 * 
	 * This method uses the following classes: File, FileWriter, BufferedWriter
	 * This method uses FileWriter --> using the default coding scheme of the
	 * system
	 * ********************************************************************************************
	 */

	public void appendOneLineToFile(File aFile, String aStrLine) throws FileNotFoundException, IOException {

		checkAndThrowExceptionsForOutputFile(aFile);

		// Use constructor: FileWriter(File aFile, boolean append)
		// boolean append: TRUE --> appending is allowed for this file

		try { // try #1

			FileWriter aFileWriter = new FileWriter(aFile, true);
			BufferedWriter output = new BufferedWriter(aFileWriter);

			try {

				// FileWriter always assumes default encoding is OK!
				// Append the input String to the file as a new line
				output.append(aStrLine);
				output.newLine();
			} finally {
				output.close();
			}

		} // End of try #1
		catch (IOException ex) { // try #1
			ex.printStackTrace();
		}

	} // End of appendOnelineToFile

	/**
	 **********************************************************************************************
	 * This method read one line from an existing file and returns a String This
	 * style of implementation throws all exceptions to the caller.
	 * 
	 * This method uses the following classes: File, FileReader, BufferedReader
	 * ********************************************************************************************
	 */

	public int readLinesFromFile(File aFile) throws FileNotFoundException, IOException {

		// Reset numLines to 0 to be sure
		numLines = 0;

		checkAndThrowExceptionsForInputFile(aFile);

		// Use constructor" FileWriter(File aFile, boolean append)
		// boolean append: TRUE --> appending is allowed for this file

		try { // try #1

			FileReader aFileReader = new FileReader(aFile);
			BufferedReader input = new BufferedReader(aFileReader);

			String line = null; // not declared within while loop

			try { // try #2

				// readLine is a bit quirky:
				// it returns the content of a line MINUS the newline.
				// it returns null only for the END of the stream.
				// it returns an empty String if two newlines appear in a row.

				line = input.readLine();

				while (line != null) {

					// numLines can be used as array index before being
					// incremented
					// MUST allocate new space for array element
					// to be sure that it refers to a String physically
					// different from "line"
					strLinesArray[numLines] = new String(line);

					// Increment by 1 to indicate the correct number of lines
					numLines++;

					// Read another line if existing
					line = input.readLine();

				} // End of while

			} // End of try #2
			finally {
				input.close();
			}

		} // End of try #1
		catch (IOException ex) { // try #1
			ex.printStackTrace();
		}

		return (numLines);

	} // End of readLinesFromFile

	public void displayFileContent() {
		if (numLines > 0) {
			for (int j = 0; j < numLines; j++) {
				System.out.println(strLinesArray[j]);
			}

		} else {
			// File is empty
			System.out.println("File is empty: no line");

		}

	} // End of displayFileContent

} // End of class FileIO