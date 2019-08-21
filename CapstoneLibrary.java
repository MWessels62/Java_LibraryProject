import java.sql.*;
//import java.util.*;
import javax.swing.*;
//Capstone library project

public class CapstoneLibrary {
	
	
	Connection databaseConnection = null;
	Statement stmt = null; 
	ResultSet rset = null; 	
	String executionString = "";

	
	public void dbConnection(){
		try {
			// Step 1: Allocate a database 'Connection' object
			//Class.forName("com.mysql.jdbc.Driver");  
			databaseConnection = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/capstone_library","myuser", "xxxx");
			stmt = databaseConnection.createStatement();
		} catch(SQLException ex) {
			ex.printStackTrace();
		}
	}
	
	
	private void showAll_books()  {
		 
			executionString = "select * from library_db";
			printQuery(executionString);
	}


	private void printQuery(String commandString) {
		try {
			rset = stmt.executeQuery(commandString); 
		
		
		String outputString = "";
		int entryCount = 0;
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
		  JTextField newId = new JTextField(11);
	      JTextField newTitle = new JTextField(80);
	      JTextField newAuthor = new JTextField(50);
	      JTextField newQty = new JTextField(11);

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
	
	
	//Worked on the assumption that the update function only needed to update the quantity of books
	
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
		String searchVariable  = JOptionPane.showInputDialog(null, //I should convert this to look like the insert book function
				"Which field would you like to search in: \n"
				+ "1. ID\n"
				+ "2. Title\n"
				+ "3. Author\n"
				+ "4. Quantity", JOptionPane.QUESTION_MESSAGE);
		int searchVariableInt = Integer.parseInt(searchVariable);
		
		String searchValue  = JOptionPane.showInputDialog(null, //I should convert this to look like the insert book function
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
		int input_int = -1; //Allocated -1 as arbitrary number that wasnt 0
		
		while (input_int != 0) { 
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
