package SEARCH_CARDB;

import java.awt.EventQueue;
import javax.swing.*;
import java.io.*;
import COMMON_CARDB.Car;
import COMMON_CARDB.FileIO;
import org.apache.commons.lang3.*;


import javax.swing.JFrame;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.border.BevelBorder;

public class SearchCarDb {

	private JFrame frmSearchCarDatabase;
	private JComboBox comboBoxAttribute;
	private JLabel lblEnterSearchValue;
	private JTextField textFieldSearchValue;
	private JTextArea textAreaSearchResults;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					SearchCarDb window = new SearchCarDb();
					window.frmSearchCarDatabase.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public SearchCarDb() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmSearchCarDatabase = new JFrame();
		frmSearchCarDatabase.setTitle("Search Car Database");
		frmSearchCarDatabase.setBounds(100, 100, 450, 457);
		frmSearchCarDatabase.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmSearchCarDatabase.getContentPane().setLayout(null);
		
		JLabel lblSearchAttribute = new JLabel("Select attribute to Search:");
		lblSearchAttribute.setBounds(26, 11, 163, 23);
		lblSearchAttribute.setFont(new Font("Times New Roman", Font.PLAIN, 16));
		frmSearchCarDatabase.getContentPane().add(lblSearchAttribute);
		
		String [] carAttributeList = {"ALL", "VIN", "Make", "Model", "Type", "Engine", "Doors", "Color"};
		comboBoxAttribute = new JComboBox(carAttributeList);
		comboBoxAttribute.setBounds(199, 14, 209, 20);
		frmSearchCarDatabase.getContentPane().add(comboBoxAttribute);
		
		lblEnterSearchValue = new JLabel("Enter value to Search:");
		lblEnterSearchValue.setBounds(26, 155, 129, 23);
		lblEnterSearchValue.setFont(new Font("Times New Roman", Font.PLAIN, 14));
		frmSearchCarDatabase.getContentPane().add(lblEnterSearchValue);
		
		textFieldSearchValue = new JTextField();
		textFieldSearchValue.setBounds(199, 157, 86, 20);
		frmSearchCarDatabase.getContentPane().add(textFieldSearchValue);
		textFieldSearchValue.setColumns(10);
		
		JButton btnSubmit = new JButton("Submit");
		btnSubmit.setBounds(199, 300, 89, 23);
		btnSubmit.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				try {
					btnSubmit_CLICK();
				}
				catch (Exception e) { 
					System.out.println(e.toString());
				}
			}
		});
		btnSubmit.setFont(new Font("Times New Roman", Font.PLAIN, 12));
		frmSearchCarDatabase.getContentPane().add(btnSubmit);
		
		JScrollPane scrollPaneSearchResults = new JScrollPane();
		scrollPaneSearchResults.setBounds(26, 326, 398, 81);
		scrollPaneSearchResults.setViewportBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		frmSearchCarDatabase.getContentPane().add(scrollPaneSearchResults);
		
		JLabel lblCarSearchResults = new JLabel("Car Search Results");
		lblCarSearchResults.setFont(new Font("Times New Roman", Font.PLAIN, 11));
		scrollPaneSearchResults.setColumnHeaderView(lblCarSearchResults);
		
		textAreaSearchResults = new JTextArea();
		textAreaSearchResults.setFont(new Font("Times New Roman", Font.PLAIN, 13));
		scrollPaneSearchResults.setViewportView(textAreaSearchResults);
	}//end of method initialize
	
	private void btnSubmit_CLICK() {
			
			// Refresh the text area of search results --> make it blank
			textAreaSearchResults.setText("");

			//-------------- First, validate all the text fields
			// If any problem, a dialog warning pops up to stop the program
			boolean isValidated = validateTextFields();
			
			if (! isValidated) return;
					
					
			//-------------- All the text fields have been validated
			FileIO fileIOHandler = new FileIO();
			
			// Declare the target file database: carDatabase.txt
			// MUST use \ to qualify '\' in the path of the file
			File fileDB = new File("C:\\\\JAVA\\OUTPUTS\\carDatabase.txt");		
			
			int numCarsInDb = 0; // number of cars in DB
			int numFoundCars = 0; // number of found cars
			String[] carsDbArray = null; // Array of all car records in DB --> array of String
			Car[] foundCarsArray = new Car[FileIO.getMaxNumLines()]; // Array of found cars --> array of Car
					
			try {			
				// Read all the records from car file database 
				numCarsInDb = fileIOHandler.readLinesFromFile(fileDB);
				carsDbArray = fileIOHandler.getStrLinesArray();
				
				// Get the selected item from ComboBox
				String strSelectedAttribute = (String) comboBoxAttribute.getSelectedItem();
				
				// Get the search value
				String strVin = textFieldSearchValue.getText();
				
				// Invoke searchCars()
				numFoundCars = searchCars(numCarsInDb, carsDbArray, foundCarsArray, strSelectedAttribute, strVin);
				
				// If no matched car is found, display warning
				if (numFoundCars == 0) {
					textAreaSearchResults.append("No matched car is found in the database. \n");
				}

			}
			catch (IOException ex){ 
	      		ex.printStackTrace();
	    	}

			
		 // End of btnSubmit_CLICK
	}
	
	/****************************
	 * Name: validateTextFields
	 * Parameters: None
	 * Return: boolean
	 * --> TRUE: The search value text field is successfully validate
	 * --> FALSE: The text field has failed the validation
	 * Description:
	 * --> This method verify to be sure the search value text field contains valid data: 
	 * --> Valid data: not null, not zero-size data, not empty String, not filled only with blank space
	 * --> If ISBN is selected as search attribute: valid data must also be numeric, i.e. only consisting of digits
	 * 
	 ****************************/
	
	private boolean validateTextFields() {
		
		boolean isValidated = true;
		
		//----------- Validate the text field of search value
		// Need to validate for every search attribute except for "ALL"
		
		if( ! comboBoxAttribute.getSelectedItem().equals("ALL")) {
			
			try{
				Validate.notBlank(textFieldSearchValue.getText());
			}catch(Exception e){
				JOptionPane.showMessageDialog(frmSearchCarDatabase, "The text field of search value must have valid values - Cannot be blank !!!.");
				textFieldSearchValue.requestFocusInWindow(); // make it ready to enter the value
				textFieldSearchValue.selectAll(); // select all text in the text field to delete it or to replace it
				isValidated = false;
			}
		}
		
		// If any problem, stop the program
		if (! isValidated) return (isValidated);
		
		// For ISBN, also need to verify the entered value is a valid numeric
		if(comboBoxAttribute.getSelectedItem().equals("Vin")){
    		try{
    			Long.parseLong(textFieldSearchValue.getText());
    		}catch(Exception e){
    			JOptionPane.showMessageDialog(frmSearchCarDatabase, "VIN must have a Numeric Value.");
				textFieldSearchValue.requestFocusInWindow(); // make it ready to enter the value
				textFieldSearchValue.selectAll(); // select all text in the text field to delete it ot to replace it
				isValidated = false;
    		}
    	}
		
		return (isValidated);
		
	} // validateTextFields

	
	
	
	
	/*******************
	 * Name: searchCars
	 * Parameters:
	 * --> numCars: number of cars in the carArray
	 * --> carsArray: array of strings, each string is a car record (in DB) of CSV format
	 * --> foundCarsArray: array of class Car elements that are found in the database
	 * --> aStrSearchAttr: a String that represents an attribute that is used to search
	 * --> aStrSearchValue: a String that represents the value of the attribute used to search
	 * Return
	 * --> This method returns an array of Car objects - cars that are found in the database
	 * Description:
	 * This method performs a search for cars whose attribute has the value that is 
	 * matched with cars in the car array
	********************/
	
	private int searchCars(int numCars, 
								String[] carsArray, 
								Car[] foundCarsArray, 
								String aStrSearchAttr, 
								String aStrSearchValue) {
		
		int numFoundCars = 0;
		
		if (aStrSearchAttr.equals("ALL")) {
			
			// Vin is used to search for cars
			numFoundCars = searchCarByAll(numCars, carsArray, foundCarsArray);
			
		} // End of if (Vin)
		
		if (aStrSearchAttr.equals("Vin")) {
		
			// Vin is used to search for cars
			numFoundCars = searchCarByVin(numCars, aStrSearchValue, carsArray, foundCarsArray);
			
		} // End of if (VIN)
			
		if (aStrSearchAttr.equals("Make")) {
				
			// Title is used to search for cars
			numFoundCars = searchCarByMake(numCars, aStrSearchValue, carsArray, foundCarsArray);
				
				
		} // End of if (Make)
			
			
		if (aStrSearchAttr.equals("Model")) {
				
			// Author is used to search for cars
			numFoundCars = searchCarByModel(numCars, aStrSearchValue, carsArray, foundCarsArray);
				
		} // End of if (Model)
			
			
		if (aStrSearchAttr.equals("Type")) {
				
			// Publisher is used to search for cars
			numFoundCars = searchCarByType(numCars, aStrSearchValue, carsArray, foundCarsArray);
			
		} // End of if (Type)
			
			
		if (aStrSearchAttr.equals("Engine")) {
				
			// Month&Year is used to search for cars
			numFoundCars = searchCarByEngine(numCars, aStrSearchValue, carsArray, foundCarsArray);
				
		} // End of if (Engine)
			
			
		if (aStrSearchAttr.equals("Doors")) {
				
			// Edition is used to search for cars
			numFoundCars = searchCarByDoors(numCars, aStrSearchValue, carsArray, foundCarsArray);
			
			
				
		} // End of if (Doors)
		if (aStrSearchAttr.equals("Color")) {
			
			// Title is used to search for cars
			numFoundCars = searchCarByColor(numCars, aStrSearchValue, carsArray, foundCarsArray);
				
				
		} // End of if (Color)
			
		
		return (numFoundCars);
		
		
	} // End of searchCars
	
	
	

	private int searchCarByAll(int numCars, String[] carsArray, Car[] foundCarsArray) {
		
		int numFoundCars = 0;
		String aStrCarRecord = "";
		
		for (int i = 0; i < numCars; i++) {
			
			aStrCarRecord = carsArray[i];
	
			// a car is found --> add car into found-car array
			Car aFoundCar = new Car (aStrCarRecord);
				
			foundCarsArray[numFoundCars] = aFoundCar;
				
			// Increment numFoundCars to indicate one more car is found
			numFoundCars++;
							
		} // End of for (scan car array)

		// Write car record of each found car into the text area (car search results)
		for (int j = 0; j < numFoundCars; j++) {
			
			// Append a car record into the search results text area
			textAreaSearchResults.append((foundCarsArray[j]).toString() + "\n"); 
			
		}
		
		
		return (numFoundCars);

		
	} // End of searchCarByAll
	
	
	private int searchCarByVin(int numCars, String strVin, String[] carsArray, Car[] foundCarsArray) {
		
		Car aCar;
		int numFoundCars = 0;
		String aStrCarRecord = "";
		
		for (int i = 0; i < numCars; i++) {
			
			aStrCarRecord = carsArray[i];
			aCar = Car.recreateCarFromString(aStrCarRecord);
	
			// First convert string value to long
			long aVin = Long.parseLong(strVin);
			
			if (aVin == aCar.getVin()){
				// a car is found --> add car into found-car array
				Car aFoundCar = new Car (aStrCarRecord);
				
				foundCarsArray[numFoundCars] = aFoundCar;
				
				// Increment numFoundCars to indicate one more car is found
				numFoundCars++;
			}			
		
		} // End of for (scan car array)

		// Write car record of each found car into the text area (car search results)
		for (int j = 0; j < numFoundCars; j++) {
			
			// Append a car record into the search results text area
			textAreaSearchResults.append((foundCarsArray[j]).toString() + "\n"); 
			
		}
		
		
		return (numFoundCars);
		
		
	} // End of searchCarByVin
	

	private int searchCarByMake(int numCars, String strMake, String[] carsArray, Car[] foundCarsArray) {
		
		Car aCar;
		String strDbCarMake = ""; // Make of car in database
		int numFoundCars = 0;
		String aStrCarRecord = "";
		
		for (int i = 0; i < numCars; i++) {
			
			aStrCarRecord = carsArray[i];
			aCar = Car.recreateCarFromString(aStrCarRecord);
	
			strDbCarMake = aCar.getMake(); // Get title of the car in db
			
			if ((strDbCarMake.equals(strMake))){
				// a car is found --> add car into found-car array
				Car aFoundCar = new Car (aStrCarRecord);
				
				foundCarsArray[numFoundCars] = aFoundCar;
				
				// Increment numFoundCars to indicate one more car is found
				numFoundCars++;
			}			
		
		} // End of for (scan car array)

		
		// Write car record of each found car into the text area (car search results)
		for (int j = 0; j < numFoundCars; j++) {
			
			// Append a car record into the search results text area
			textAreaSearchResults.append((foundCarsArray[j]).toString() + "\n"); 
			
		}

		
		
		return (numFoundCars);
		
		
	} // End of searchCarByTitle
	

	private int searchCarByModel(int numCars, String strModel, String[] carsArray, Car[] foundCarsArray) {
		
		Car aCar;
		String strDbCarModel = ""; // Model of car in database
		int numFoundCars = 0;
		String aStrCarRecord = "";
		
		for (int i = 0; i < numCars; i++) {
			
			aStrCarRecord = carsArray[i];
			aCar = Car.recreateCarFromString(aStrCarRecord);
	
			strDbCarModel = aCar.getModel(); // Get model of the car in db
			
			if ((strDbCarModel.equals(strModel))){
				// a car is found --> add car into found-car array
				Car aFoundCar = new Car (aStrCarRecord);
				
				foundCarsArray[numFoundCars] = aFoundCar;
				
				// Increment numFoundCars to indicate one more car is found
				numFoundCars++;
			}			
		
		} // End of for (scan car array)
		
		// Write car record of each found car into the text area (car search results)
		for (int j = 0; j < numFoundCars; j++) {
			
			// Append a car record into the search results text area
			textAreaSearchResults.append((foundCarsArray[j]).toString() + "\n"); 
			
		}

		return (numFoundCars);
		
		
	} // End of searchCarByAuthor	


	private int searchCarByType(int numCars, String strType, String[] carsArray, Car[] foundCarsArray) {
		
		Car aCar;
		String strDbCarType = ""; // Publisher of car in database
		int numFoundCars = 0;
		String aStrCarRecord = "";
		
		for (int i = 0; i < numCars; i++) {
			
			aStrCarRecord = carsArray[i];
			aCar = Car.recreateCarFromString(aStrCarRecord);
	
			strDbCarType = aCar.getType(); // Get type of the car in db
			
			if ((strDbCarType.equals(strType))){
				// a car is found --> add car into found-car array
				Car aFoundCar = new Car (aStrCarRecord);
				
				foundCarsArray[numFoundCars] = aFoundCar;
				
				// Increment numFoundCars to indicate one more car is found
				numFoundCars++;
			}			
		
		} // End of for (scan car array)

		// Write car record of each found car into the text area (car search results)
		for (int j = 0; j < numFoundCars; j++) {
			
			// Append a car record into the search results text area
			textAreaSearchResults.append((foundCarsArray[j]).toString() + "\n"); 
			
		}

		return (numFoundCars);
		
		
	} // End of searchCarByPublisher	


	private int searchCarByEngine(int numCars, String strEngine, String[] carsArray, Car[] foundCarsArray) {
		
		Car aCar;
		String strDbCarEngine = ""; // Month and year of car in database
		int numFoundCars = 0;
		String aStrCarRecord = "";
		
		for (int i = 0; i < numCars; i++) {
			
			aStrCarRecord = carsArray[i];
			aCar = Car.recreateCarFromString(aStrCarRecord);
	
			strDbCarEngine = aCar.getEngine(); // Get month & year of the car in db
			
			if ((strDbCarEngine.equals(strEngine))){
				// a car is found --> add car into found-car array
				Car aFoundCar = new Car (aStrCarRecord);
				
				foundCarsArray[numFoundCars] = aFoundCar;
				
				// Increment numFoundCars to indicate one more car is found
				numFoundCars++;
			}			
		
		} // End of for (scan car array)


		// Write car record of each found car into the text area (car search results)
		for (int j = 0; j < numFoundCars; j++) {
			
			// Append a car record into the search results text area
			textAreaSearchResults.append((foundCarsArray[j]).toString() + "\n"); 
			
		}

		return (numFoundCars);
		
		
	} // End of searchCarByMonthYear	


private int searchCarByDoors(int numCars, String strDoors, String[] carsArray, Car[] foundCarsArray) {
		
		Car aCar;
		String strDbCarDoors = ""; // Make of car in database
		int numFoundCars = 0;
		String aStrCarRecord = "";
		
		for (int i = 0; i < numCars; i++) {
			
			aStrCarRecord = carsArray[i];
			aCar = Car.recreateCarFromString(aStrCarRecord);
	
			strDbCarDoors = aCar.getDoors(); // Get doors of the car in db
			
			if ((strDbCarDoors.equals(strDoors))){
				// a car is found --> add car into found-car array
				Car aFoundCar = new Car (aStrCarRecord);
				
				foundCarsArray[numFoundCars] = aFoundCar;
				
				// Increment numFoundCars to indicate one more car is found
				numFoundCars++;
			}			
		
		} // End of for (scan car array)

		
		// Write car record of each found car into the text area (car search results)
		for (int j = 0; j < numFoundCars; j++) {
			
			// Append a car record into the search results text area
			textAreaSearchResults.append((foundCarsArray[j]).toString() + "\n"); 
			
		}

		
		
		return (numFoundCars);
		
		
	} // End of searchCarByTitle

	private int searchCarByColor(int numCars, String strColor, String[] carsArray, Car[] foundCarsArray) {
		
		Car aCar;
		String strDbCarColor = ""; // Make of car in database
		int numFoundCars = 0;
		String aStrCarRecord = "";
		
		for (int i = 0; i < numCars; i++) {
			
			aStrCarRecord = carsArray[i];
			aCar = Car.recreateCarFromString(aStrCarRecord);
	
			strDbCarColor = aCar.getColor(); // Get title of the car in db
			
			if ((strDbCarColor.equals(strColor))){
				// a car is found --> add car into found-car array
				Car aFoundCar = new Car (aStrCarRecord);
				
				foundCarsArray[numFoundCars] = aFoundCar;
				
				// Increment numFoundCars to indicate one more car is found
				numFoundCars++;
			}			
		
		} // End of for (scan car array)

		
		// Write car record of each found car into the text area (car search results)
		for (int j = 0; j < numFoundCars; j++) {
			
			// Append a car record into the search results text area
			textAreaSearchResults.append((foundCarsArray[j]).toString() + "\n"); 
			
		}

		
		
		return (numFoundCars);
		
		
	} // End of searchCarByColor



}//end of class CreateCarDb
