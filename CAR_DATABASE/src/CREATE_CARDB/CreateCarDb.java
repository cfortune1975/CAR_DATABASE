package CREATE_CARDB;

import java.awt.EventQueue;
import javax.swing.*;
import java.io.*;
import COMMON_CARDB.Car;
import COMMON_CARDB.FileIO;
import org.apache.commons.lang3.*;

import javax.swing.JFrame;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * @author Logan Moore
 *
 */

public class CreateCarDb {

	private JFrame frmCreateCarDatabase;
	private JLabel lblEnterVIN;
	private JTextField textFieldVinNumber;
	private JLabel lblEnterMake;
	private JTextField textFieldMake;
	private JLabel lblEnterModel;
	private JTextField textFieldModel;
	private JLabel lblEnterType;
	private JTextField textFieldType;
	private JLabel lblEnterEngine;
	private JTextField textFieldEngine;
	private JLabel lblEnterDoors;
	private JTextField textFieldDoors;
	private JLabel lblEnterColor;
	private JTextField textFieldColor;
	private JButton submitButton;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CreateCarDb window = new CreateCarDb();
					window.frmCreateCarDatabase.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CreateCarDb() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		/** Create frame content */
		frmCreateCarDatabase = new JFrame();
		frmCreateCarDatabase.setResizable(false);
		frmCreateCarDatabase.setTitle("Create Car Database");
		frmCreateCarDatabase.setBounds(100, 100, 450, 347);
		frmCreateCarDatabase.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frmCreateCarDatabase.getContentPane().setLayout(null);

		/** Add input content */
		lblEnterVIN = new JLabel("Enter VIN:");
		lblEnterVIN.setBounds(15, 20, 138, 26);
		frmCreateCarDatabase.getContentPane().add(lblEnterVIN);

		textFieldVinNumber = new JTextField();
		lblEnterVIN.setLabelFor(textFieldVinNumber);
		textFieldVinNumber.setBounds(156, 20, 262, 26);
		frmCreateCarDatabase.getContentPane().add(textFieldVinNumber);
		textFieldVinNumber.setColumns(10);

		lblEnterMake = new JLabel("Enter Make:");
		lblEnterMake.setBounds(15, 55, 138, 26);
		frmCreateCarDatabase.getContentPane().add(lblEnterMake);

		textFieldMake = new JTextField();
		lblEnterMake.setLabelFor(textFieldMake);
		textFieldMake.setColumns(10);
		textFieldMake.setBounds(156, 55, 262, 26);
		frmCreateCarDatabase.getContentPane().add(textFieldMake);

		lblEnterModel = new JLabel("Enter Model:");
		lblEnterModel.setBounds(15, 92, 138, 26);
		frmCreateCarDatabase.getContentPane().add(lblEnterModel);

		textFieldModel = new JTextField();
		lblEnterModel.setLabelFor(textFieldModel);
		textFieldModel.setColumns(10);
		textFieldModel.setBounds(156, 92, 262, 26);
		frmCreateCarDatabase.getContentPane().add(textFieldModel);

		lblEnterType = new JLabel("Enter Type:");
		lblEnterType.setBounds(15, 129, 138, 26);
		frmCreateCarDatabase.getContentPane().add(lblEnterType);

		textFieldType = new JTextField();
		lblEnterType.setLabelFor(textFieldType);
		textFieldType.setColumns(10);
		textFieldType.setBounds(156, 129, 262, 26);
		frmCreateCarDatabase.getContentPane().add(textFieldType);

		lblEnterEngine = new JLabel("Enter Engine Volume:");
		lblEnterEngine.setBounds(15, 165, 138, 26);
		frmCreateCarDatabase.getContentPane().add(lblEnterEngine);

		textFieldEngine = new JTextField();
		lblEnterEngine.setLabelFor(textFieldEngine);
		textFieldEngine.setColumns(10);
		textFieldEngine.setBounds(156, 165, 262, 26);
		frmCreateCarDatabase.getContentPane().add(textFieldEngine);

		lblEnterDoors = new JLabel("Enter Doors");
		lblEnterDoors.setBounds(15, 200, 138, 26);
		frmCreateCarDatabase.getContentPane().add(lblEnterDoors);

		textFieldDoors = new JTextField();
		lblEnterDoors.setLabelFor(textFieldDoors);
		textFieldDoors.setColumns(10);
		textFieldDoors.setBounds(156, 200, 262, 26);
		frmCreateCarDatabase.getContentPane().add(textFieldDoors);

		lblEnterColor = new JLabel("Enter Color");
		lblEnterColor.setBounds(15, 235, 138, 26);
		frmCreateCarDatabase.getContentPane().add(lblEnterColor);

		textFieldColor = new JTextField();
		lblEnterColor.setLabelFor(textFieldColor);
		textFieldColor.setColumns(10);
		textFieldColor.setBounds(156, 235, 262, 26);
		frmCreateCarDatabase.getContentPane().add(textFieldColor);

		submitButton = new JButton("Submit");
		submitButton.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				try {
					btnSubmit_CLICK();
				} catch (Exception ex) {
					System.out.println(ex.toString());
				}
			}
		});
		submitButton.setBounds(156, 279, 117, 29);
		frmCreateCarDatabase.getContentPane().add(submitButton);

	} // end of method initialize

	private void btnSubmit_CLICK() {

		// -------------- First, validate all the text fields
		// If any problem, a dialog warning pops up to stop the program
		boolean isValidated = validateTextFields();

		if (!isValidated)
			return;

		// -------------- All the text fields have been validated
		FileIO fileIOHandler = new FileIO();

		// Declare output file database: bookDatabase.txt
		// MUST use \ to qualify '\' in the path of the file
		File outputFile = new File("J:\\\\JAVA\\OUTPUTS\\carDatabase.txt");

		String strVinNumber = textFieldVinNumber.getText();
		long vin = Long.parseLong(strVinNumber);

		String strMake = textFieldMake.getText();
		String strModel = textFieldModel.getText();
		String strType = textFieldType.getText();
		String strEngine = textFieldEngine.getText();
		String strDoors = textFieldDoors.getText();
		String strColor = textFieldColor.getText();

		// Create a Car object
		Car aCar = new Car(vin, strMake, strModel);
		aCar.setType(strType);
		aCar.setEngine(strEngine);
		aCar.setDoors(Integer.parseInt(strDoors));
		aCar.setColor(strColor);

		// Get the string of book data
		String strCarInfo = aCar.toString();

		try {

			// Write the string to the book database file
			// by adding a line to the file
			fileIOHandler.appendOneLineToFile(outputFile, strCarInfo);
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		// At this point, already successfully inserting a new book record into
		// the database
		JOptionPane.showMessageDialog(frmCreateCarDatabase,
				"The new car record has been successfully inserted into the database.");

		// After successfully inserting a new book record to the database
		// refresh all the text fields to prepare for the next record
		textFieldVinNumber.setText("");
		textFieldMake.setText("");
		textFieldModel.setText("");
		textFieldType.setText("");
		textFieldEngine.setText("");
		textFieldDoors.setText("");
		textFieldColor.setText("");

	} // End of btnSubmit_CLICK

	/****************************
	 * Name: validateTextFields 
	 * Parameters: None Return: boolean --> TRUE: all
	 * the text fields are successfully validate --> FALSE: at least one text
	 * field has failed the validation Description: --> This method verify to be
	 * sure each text field contains valid data: --> Valid data: not null, not
	 * zero-size data, not empty String, not filled only with blank space -->
	 * For ISBN: valid data must also be numeric, i.e. only consisting of digits
	 * 
	 ****************************/
	private boolean validateTextFields() {
		
		boolean isValidated = true;

		// ----------- Validate VIN text field

		try {
			Validate.notBlank(textFieldVinNumber.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmCreateCarDatabase,
					"All the text fields must have valid values - VIN must have a Numeric Value.");
			textFieldVinNumber.requestFocusInWindow(); // make it ready to enter
													// the value
			textFieldVinNumber.selectAll(); // select all text in the text field
											// to delete it or to replace it
			isValidated = false;
		}

		if (!isValidated)
			return (isValidated);

		// For VIN, also need to verify the entered value is a valid numeric
		try {
			long tempLong = Long.parseLong(textFieldVinNumber.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmCreateCarDatabase, "VIN must have a Numeric Value.");
			textFieldVinNumber.requestFocusInWindow(); // make it ready to enter
														// the value
			textFieldVinNumber.selectAll(); // select all text in the text field
											// to delete it or to replace it
			isValidated = false;
		}

		if (!isValidated)
			return (isValidated);

		// ----------- Validate make text field

		try {
			Validate.notBlank(textFieldMake.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmCreateCarDatabase,
					"All the text fields must have valid values - make cannot be blank !!!.");
			textFieldMake.requestFocusInWindow(); // make it ready to enter the
													// value
			textFieldMake.selectAll(); // select all text in the text field to
										// delete it or to replace it
			isValidated = false;
		}

		if (!isValidated)
			return (isValidated);

		// ----------- Validate model text field

		try {
			Validate.notBlank(textFieldModel.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmCreateCarDatabase,
					"All the text fields must have valid values - model cannot be blank !!!.");
			textFieldModel.requestFocusInWindow(); // make it ready to enter the
													// value
			textFieldModel.selectAll(); // select all text in the text field to
										// delete it or to replace it
			isValidated = false;
		}

		if (!isValidated)
			return (isValidated);

		// ----------- Validate type text field

		try {
			Validate.notBlank(textFieldType.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmCreateCarDatabase,
					"All the text fields must have valid values - type cannot be blank !!!.");
			textFieldType.requestFocusInWindow(); // make it ready to enter the
													// value
			textFieldType.selectAll(); // select all text in the text field to
										// delete it or to replace it
			isValidated = false;
		}

		if (!isValidated)
			return (isValidated);

		// ----------- Validate engine text field

		try {
			Validate.notBlank(textFieldEngine.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmCreateCarDatabase,
					"All the text fields must have valid values - Engine cannot be blank !!!.");
			textFieldEngine.requestFocusInWindow(); // make it ready to enter the
													// value
			textFieldEngine.selectAll(); // select all text in the text field to
										// delete it or to replace it
			isValidated = false;
		}

		if (!isValidated)
			return (isValidated);

		// ----------- Validate doors text field

		try {
			Validate.notBlank(textFieldDoors.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmCreateCarDatabase,
					"All the text fields must have valid values - doors cannot be blank !!!.");
			textFieldDoors.requestFocusInWindow(); // make it ready to enter
													// the value
			textFieldDoors.selectAll(); // select all text in the text field to
											// delete it or to replace it
			isValidated = false;
		}

		if (!isValidated)
			return (isValidated);

		// ----------- Validate color text field

		try {
			Validate.notBlank(textFieldColor.getText());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(frmCreateCarDatabase,
					"All the text fields must have valid values - color cannot be blank !!!.");
			textFieldColor.requestFocusInWindow(); // make it ready to enter
													// the value
			textFieldColor.selectAll(); // select all text in the text field to
											// delete it or to replace it
			isValidated = false;
		}
		
		return (isValidated);
	}
} // End of class CreatebookDB
