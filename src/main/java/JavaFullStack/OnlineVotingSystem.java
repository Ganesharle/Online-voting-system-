package JavaFullStack;

import java.sql.*;
import java.util.Scanner;

public class OnlineVotingSystem {
	 private static final String URL = "jdbc:postgresql://localhost:5432/voting";
	    private static final String USER = "postgres";
	    private static final String PASSWORD = "123";
	    
	public static void main(String[] args) {
		 Scanner scanner = new Scanner(System.in);
	        while (true) {
	            System.out.println("\nOnline Voting System");
	            System.out.println("1. Register Voter");
	            System.out.println("2. Login and Vote");
	            System.out.println("3. View Results");
	            System.out.println("4. Exit");
	            System.out.print("Choose an option: ");
	            
	            try {
	                int choice = Integer.parseInt(scanner.nextLine().trim());
	                switch (choice) {
	                    case 1:
	                        registerVoter(scanner);
	                        break;
	                    case 2:
	                        loginAndVote(scanner);
	                        break;
	                    case 3:
	                        viewResults();
	                        break;
	                    case 4:
	                        System.out.println("Exiting...");
	                        return;
	                    default:
	                        System.out.println("Invalid choice. Try again.");
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid input. Please enter a number.");
	            }
	        }
	    }

	    private static Connection getConnection() throws SQLException {
	        return DriverManager.getConnection(URL, USER, PASSWORD);
	    }

	    private static void registerVoter(Scanner scanner) {
	        System.out.print("Enter Voter Name: ");
	        String name = scanner.nextLine().trim();
	        System.out.print("Enter Voter ID: ");
	        String voterId = scanner.nextLine().trim();

	        String query = "INSERT INTO voters (voter_id, name) VALUES (?, ?)";
	        try (Connection conn = getConnection();
	             PreparedStatement stmt = conn.prepareStatement(query)) {
	            stmt.setString(1, voterId);
	            stmt.setString(2, name);
	            stmt.executeUpdate();
	            System.out.println("Voter Registered Successfully!");
	        } catch (SQLIntegrityConstraintViolationException e) {
	            System.out.println("Error: Voter ID already exists!");
	        } catch (SQLException e) {
	            System.out.println("Database error: " + e.getMessage());
	        }
	    }

	    private static void loginAndVote(Scanner scanner) {
	        System.out.print("Enter Voter ID: ");
	        String voterId = scanner.nextLine().trim();

	        String checkVoterQuery = "SELECT * FROM voters WHERE voter_id = ?";
	        String checkVoteQuery = "SELECT * FROM votes WHERE voter_id = ?";
	        String voteQuery = "INSERT INTO votes (voter_id, candidate_id) VALUES (?, ?)";

	        try (Connection conn = getConnection();
	             PreparedStatement checkVoterStmt = conn.prepareStatement(checkVoterQuery);
	             PreparedStatement checkVoteStmt = conn.prepareStatement(checkVoteQuery);
	             PreparedStatement voteStmt = conn.prepareStatement(voteQuery)) {
	            
	            checkVoterStmt.setString(1, voterId);
	            ResultSet voterResult = checkVoterStmt.executeQuery();
	            
	            if (!voterResult.next()) {
	                System.out.println("Voter ID not found!");
	                return;
	            }

	            checkVoteStmt.setString(1, voterId);
	            ResultSet voteResult = checkVoteStmt.executeQuery();
	            if (voteResult.next()) {
	                System.out.println("You have already voted!");
	                return;
	            }

	            System.out.println("Voter Verified! Choose a candidate to vote for:");
	            System.out.println("1. Ganesh\n2. Sai\n3. Vikram");
	            System.out.print("Enter Candidate ID: ");
	            int candidateId;
	            
	            try {
	                candidateId = Integer.parseInt(scanner.nextLine().trim());
	                if (candidateId < 1 || candidateId > 3) {
	                    System.out.println("Invalid candidate ID. Vote not recorded.");
	                    return;
	                }
	            } catch (NumberFormatException e) {
	                System.out.println("Invalid input. Please enter a number.");
	                return;
	            }

	            voteStmt.setString(1, voterId);
	            voteStmt.setInt(2, candidateId);
	            voteStmt.executeUpdate();
	            System.out.println("Vote Cast Successfully!");

	        } catch (SQLException e) {
	            System.out.println("Database error: " + e.getMessage());
	        }
	    }

	    private static void viewResults() {
	        String query = "SELECT candidate_id, COUNT(*) AS votes FROM votes GROUP BY candidate_id ORDER BY votes DESC";
	        
	        try (Connection conn = getConnection();
	             Statement stmt = conn.createStatement();
	             ResultSet rs = stmt.executeQuery(query)) {
	            
	            System.out.println("Election Results:");
	            while (rs.next()) {
	                System.out.println("Candidate ID: " + rs.getInt("candidate_id") + " - Votes: " + rs.getInt("votes"));
	            }
	        } catch (SQLException e) {
	            System.out.println("Database error: " + e.getMessage());
	        }


	}

}
