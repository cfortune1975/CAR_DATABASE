package COMMON_CARDB;

/**
 * @model Chad Fortune
 * 
 */
public class Car {

	// --------------- Declare attributes
	private long vinNumber;
	private String make;
	private String model;
	private String type;
	private String engine;
	private int doors;
	private String color;
	
	// --------------- Declare constructors
	public Car() {
		vinNumber = 0;
		make = "";
		model = "";
		type = "";
		engine = "";
		doors = 0;
		color = "";
	} // End of default constructor
	
	// Another constructor
	public Car(long aVin, String aMake, String aModel) {
		vinNumber  = aVin;
		make = aMake;
		model = aModel;
		type = "";
		engine = "";
		doors = 0;
		color = "";
	} // End of Another constructor
	
	// Another constructor
	// This constructor recreates a car object out of a string of car info
	// The string aStrCarData is of CSV format - has been verified: NOT NULL
	// and NOT EMPTY STRING
	public Car(String aStrCarData) {
		
		Car aCar = recreateCarFromString(aStrCarData);
		
		vinNumber  = aCar.getVin();
		make = aCar.getMake();
		model = aCar.getModel();
		type = aCar.getType();
		engine = aCar.getEngine();
		doors = aCar.getDoors();
		color = aCar.getColor();
		
	} // End of Another constructor
	
	
	// --------------- GET & SET methods
	public long getVin() {
		return (vinNumber);
	}
	
	public void setVin(long aVin) {
		vinNumber = aVin;
	}
	
	public String getMake() {
		return (make);
	}
	
	public void setMake(String aMake) {
		make = aMake;
	}
	
	public String getModel() {
		return (model);
	}
	
	public void setModel(String aModel) {
		make = aModel;
	}
	
	public String getType() {
		return (type);
	}
	
	public void setType(String aType) {
		make = aType;
	}
	
	public String getEngine() {
		return (engine);
	}
	
	public void setEngine(String aEngine) {
		make = aEngine;
	}
	
	public int getDoors() {
		return (doors);
	}
	
	public void setDoors(int aDoors) {
		doors = aDoors;
	}

	public String getColor() {
		return (color);
	}
	
	public void setColor(String aColor) {
		make = aColor;
	}
	
	
	// --------------- Other methods

	// This method returns a string that stores all data fields of a car record
	// The fields are separated by a comma ','
	// The string can be used to insert a car record (a line) into a .CSV file
	// database
	// This method overrides the same method of class Object from which all Java
	// classes inherit
	@Override
	public String toString() {
		
		String carStr = Long.toString(vinNumber) + ","
					  + make + ","
					  + model + ","
					  + type + ","
					  + engine + ","
					  + Integer.toString(doors) + ","
					  + color;
		
		return (carStr);
		
	}// End of toString
	
	// This method recreate a Car object out of a string of CSV format.
	// This string contains all data fields of the car
	// After recreating the car, the method returns the object
	public static Car recreateCarFromString(String aStrCarData) {
		
		Car aCar = new Car();

		if (aStrCarData.isEmpty()) {
			return (null);
		}

		// ---------- Initialize local variables
		int preCommaIndex = 0; // index of the preceding comma in the CSV string
		int nextCommaIndex = 0; // index of the next comma in the CSV string
		String nextSubstring = "";

		// ---------- Retrieve the VIN number
		// Get the index of the 1st comma that separates VIN and make
		nextCommaIndex = aStrCarData.indexOf(',');

		// Get the substring of the VIN number
		// BE CAREFUL!!!: substring (int startIndex, int endIndex) - See manual
		// of this method
		// Actually, the substring starts at startIndex and extends to (endIndex
		// - 1) - NOT endIndex
		// So, endIndex must be nextCommaIndex, NOT nextCommaIndex - 1
		nextSubstring = aStrCarData.substring(0, nextCommaIndex);

		// Convert this string to long
		long aVin = Long.parseLong(nextSubstring);

		// Set VIN number
		aCar.setVin(aVin);

		// ---------- Retrieve the make
		// Get the index of the 1st comma that separates VIN and make
		preCommaIndex = nextCommaIndex;
		nextCommaIndex = aStrCarData.indexOf(',', preCommaIndex + 1);

		// Get the substring of make
		nextSubstring = aStrCarData.substring(preCommaIndex + 1, nextCommaIndex);

		// Set make
		aCar.setMake(nextSubstring);

		// ---------- Retrieve the model
		// Get the index of the 1st comma that separates make and model
		preCommaIndex = nextCommaIndex;
		nextCommaIndex = aStrCarData.indexOf(',', preCommaIndex + 1);

		// Get the substring of model
		nextSubstring = aStrCarData.substring(preCommaIndex + 1, nextCommaIndex);

		// Set model
		aCar.setModel(nextSubstring);

		// ---------- Retrieve the type
		// Get the index of the 1st comma that separates model and type
		preCommaIndex = nextCommaIndex;
		nextCommaIndex = aStrCarData.indexOf(',', preCommaIndex + 1);

		// Get the substring of type
		nextSubstring = aStrCarData.substring(preCommaIndex + 1, nextCommaIndex);

		// Set type
		aCar.setType(nextSubstring);

		// ---------- Retrieve engine
		// Get the index of the 1st comma that separates type and engine
		preCommaIndex = nextCommaIndex;
		nextCommaIndex = aStrCarData.indexOf(',', preCommaIndex + 1);

		// Get the substring of engine
		nextSubstring = aStrCarData.substring(preCommaIndex + 1, nextCommaIndex);

		// Set engine
		aCar.setEngine(nextSubstring);

		// ---------- Retrieve doors
		// Get the index of the 1st comma that separates engine and doors
		preCommaIndex = nextCommaIndex;
		nextCommaIndex = aStrCarData.indexOf(',', preCommaIndex + 1);

		// Get the substring of doors
		nextSubstring = aStrCarData.substring(preCommaIndex + 1, nextCommaIndex);
		int numDoors = Integer.parseInt(nextSubstring);

		// Set doors
		aCar.setDoors(numDoors);

		// ---------- Retrieve color
		// Get the substring of color - last substring starting from index of
		// last comma + 1
		preCommaIndex = nextCommaIndex;
		nextSubstring = aStrCarData.substring(preCommaIndex + 1);

		// Set color
		aCar.setColor(nextSubstring);

		return (aCar);
	}
	
	
	
} // End of class Car
