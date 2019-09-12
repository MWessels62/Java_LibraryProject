//Capstone library project

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class CapstoneLibrary {
	
	Connection databaseConnection = null;
	Statement stmt = null; 
	ResultSet rset = null; 	
	String executionString = "";

	//Establishes connection with the MySQL server using the JDBC driver
	/*On another computer, the following (capstone_library","myuser", "xxxx") will need to be replaced by your
		database name, user name and password*/
	public void dbConnection() { 

		//This will open up cmd.exe and initialise the MySQL server so that the user doesn't need to do this before running the program
		try
        { 
            // Just one line and you are done !  
            // We have given a command to start cmd 
            // /K : Carries out command specified by string 
			Runtime.getRuntime().exec("cmd /c start cmd.exe /K \"mysqld --console\"");
        } 
        catch (Exception e) 
        { 
            System.out.println("Incorrect input"); 
            e.printStackTrace(); 
        } 
		try {

			// Allocate a database 'Connection' object
			databaseConnection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/capstone_library","myuser", "xxxx");
			stmt = databaseConnection.createStatement();
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	//Shows all books within the database
	private void showAll_books()  {
			// 'executionString' contains the mySQL instruction
			executionString = "select * from library_db";
			printQuery(executionString);
	}

	//Following code physically passes the execution instructions from the other methods to MySQL, and then prints out the result
	private void printQuery(String commandString) {
		try {
			//rset object is the connection object that gets passed the instruction to be executed 
			rset = stmt.executeQuery(commandString); 
				
			String outputString = "";
			int entryCount = 0;
			//The output for all entries is appended onto outputString
			while(rset.next()) {
				entryCount += 1;
				outputString += (entryCount + ". " + rset.getInt("id") + ", " 
						+ rset.getString("Title") + ", " 
						+ rset.getString("Author") + ", " 
						+ rset.getInt("Qty") + "\n");
			
		}
		JOptionPane.showMessageDialog(null,outputString,"All results from library database", JOptionPane.INFORMATION_MESSAGE);
		} catch(SQLException ex) {
				ex.printStackTrace();
		}
			}
			
	private void enterBook() {
		try {
			//JTextFields take as input the user's input into dialog boxes
		  JTextField newId = new JTextField(11);
	      JTextField newTitle = new JTextField(80);
	      JTextField newAuthor = new JTextField(50);
	      JTextField newQty = new JTextField(11);

	      //Constructing the dialogue box
	      JPanel myPanel = new JPanel();
	      myPanel.setLayout(new BoxLayout(myPanel, BoxLayout.Y_AXIS));
	    
	      myPanel.add(new JLabel("Id: "));
	      myPanel.add(newId);
	      
	      myPanel.add(new JLabel("Title: "));
	      myPanel.add(newTitle);
	      
	      myPanel.add(new JLabel("Author: "));
	      myPanel.add(newAuthor);
	      
	      myPanel.add(new JLabel("Quantity: "));
	      myPanel.add(newQty);

	      JOptionPane.showConfirmDialog(null, myPanel, 
	               "Details of new book", JOptionPane.OK_CANCEL_OPTION);
	      
	      //Parse integer data from the user input
		  String tempId = newId.getText();
		  String tempQty = newQty.getText();
		  int parsedId = Integer.parseInt(tempId);
		  int parsedQty = Integer.parseInt(tempQty);

	     executionString = ("insert into library_db values (" + parsedId + ",'" + newTitle.getText() + "','" + newAuthor.getText() + "'," + parsedQty + ")");
	     System.out.println(executionString);
	      } catch (NumberFormatException nfe) {
    	  }  
	      
		 try {
	      stmt.executeUpdate(executionString);
		  } catch(SQLException ex) {
				ex.printStackTrace();
		  	}
	 }
	
	private void deleteBook() {
		String inputId = JOptionPane.showInputDialog(
				null,"Please enter in the ID (4 digits) of the book that you would like to DELETE: ", JOptionPane.QUESTION_MESSAGE);
		int inputInt = Integer.parseInt(inputId);
		
		executionString = ("delete from library_db where id = " + inputInt);
		
		try {
		      stmt.executeUpdate(executionString);
			  } catch(SQLException ex) {
					ex.printStackTrace();
			  	}
	}
	
	
	//Currently this function can only change the no. of books (i.e. it cant be used to change other features of existing books)
	
	private void updateBook() {
		String inputId = JOptionPane.showInputDialog(
				null,"Please enter in the ID (4 digits) of the book that you would like to UPDATE: ", JOptionPane.QUESTION_MESSAGE);
		int inputInt = Integer.parseInt(inputId);
	
		String inputQty = JOptionPane.showInputDialog(
				null,"What would you like to change the quantity to: ", JOptionPane.QUESTION_MESSAGE);
		int qtyUpdate = Integer.parseInt(inputQty);
		
		
		executionString = ("update library_db set qty = " + qtyUpdate + " where id = " + inputInt);
		
		try {
		      stmt.executeUpdate(executionString);
			  } catch(SQLException ex) {
					ex.printStackTrace();
			  	}
		
		
	}
	
	private void searchBooks() {
		String searchVariable  = JOptionPane.showInputDialog(null, 
				"Which field would you like to search in: \n"
				+ "1. ID\n"
				+ "2. Title\n"
				+ "3. Author\n"
				+ "4. Quantity", JOptionPane.QUESTION_MESSAGE);
		int searchVariableInt = Integer.parseInt(searchVariable);
		
		String searchValue  = JOptionPane.showInputDialog(null, 
				"Type in the value that you would like to search for "
				+ "(for quantity, the items returned will be any books that have >= the number indicated: "
				, JOptionPane.QUESTION_MESSAGE);
		
		switch (searchVariableInt) {
			case 1: 
				executionString = ("select * from library_db where id = " + searchValue); 
				break;
			case 2: 
				executionString = ("select * from library_db where Title LIKE '%" + searchValue + "%'");
				break;
			case 3: 
				executionString = ("select * from library_db where Author LIKE '%" + searchValue + "%'");
				break;
			case 4: 
				executionString = ("select * from library_db where Qty >= " + searchValue);
				break;
		}
		printQuery(executionString);
		
	}
     
	private void printMenu() {
		String input;
		int input_int = -1; //Allocated -1 as arbitrary number that wasn't 0
			
		//printMenu function repeats after each action, until a '0' is entered
		while (input_int != 0) { 
			//Main menu
			input = JOptionPane.showInputDialog(
					null,"1. Enter book\n"
					+ "2. Update book\n"
					+ "3. Delete book\n"
					+ "4. Search books\n"
					+ "5. Show all books\n"
					+ "0. Exit", "Library Database: Please select an option...", JOptionPane.QUESTION_MESSAGE);
			input_int = Integer.parseInt(input);
			
			switch (input_int) {		
				case 1: 
					enterBook();
					break;
				case 2: 
					updateBook();
					break;
				case 3: 
					deleteBook();
					break;
				case 4:
					searchBooks();
					break;
				case 5:
					showAll_books();
					break;
			}
		}
	}

	public static void main(String[] args) {
       
		CapstoneLibrary newInstance = new CapstoneLibrary();
        newInstance.dbConnection();
		newInstance.printMenu();
	}
}