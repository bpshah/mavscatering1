package data;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
//import java.util.Arrays;

import model.User;
import util.SQLConnection;

public class UserDAO {
static SQLConnection DBMgr = SQLConnection.getInstance();
	
	public static void registerUser(User user) {
		Statement stmt = null;   
		Connection conn = SQLConnection.getDBConnection();  
		String registerUser = "INSERT INTO USER (username, password, role, utaid, firstname, lastname, phone, email, streetname, streetno, city, state, zipcode) ";					
		registerUser += " VALUES ('"  
				+ user.getUsername() + "','"
				+ user.getPassword() + "','"		
				+ user.getRole() + "','"
				+ user.getUtaid() + "','" 
				+ user.getFirstname() + "','"
				+ user.getLastname() + "','"
				+ user.getPhone() + "','"		
				+ user.getEmail() + "','"
				+ user.getStreetname() + "','"
				+ user.getStreetnumber() + "','"
				+ user.getCity() + "','"
				+ user.getState() + "','"
				+ user.getZipcode() + "')" ;
		System.out.println("Query: "+registerUser);
		
		try {   
		conn = SQLConnection.getDBConnection();  
		conn.setAutoCommit(false);   
		stmt = conn.createStatement();
		stmt.executeUpdate(registerUser);
		System.out.println(registerUser);
		conn.commit();					 
	} catch (SQLException sqle) { 
		sqle.printStackTrace();
	} finally {
		try {
			conn.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		};
	}
	}
	public static boolean uniqueUsername(String username) {  
		Statement stmt = null;   
		Connection conn = null;  
		try {   
			conn = SQLConnection.getDBConnection();  
			stmt = conn.createStatement();
			String searchUsername = " SELECT * from USER WHERE USERNAME = '"+username+"'";
			ResultSet userList = stmt.executeQuery(searchUsername);
			ArrayList<User> userListInDB = new ArrayList<User>();
			while (userList.next()) {
				User user = new User(); 
				userListInDB.add(user);	 
			} 
			return (userListInDB.isEmpty());
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			};
		return false;
	}
	
	public static User getUser(String username) {
		Statement stmt = null;   
		Connection conn = null;  
		User user = new User();
		try {   
			conn = SQLConnection.getDBConnection();  
			stmt = conn.createStatement();
			String searchUsername = " SELECT * from USER WHERE USERNAME = '"+username+"'";
			ResultSet userList = stmt.executeQuery(searchUsername);
			while(userList.next()) {
				String password = userList.getString("password");
				String role  = userList.getString("role");
				String utaId = userList.getString("utaid");
				String firstName  = userList.getString("firstname");
				String lastName  = userList.getString("lastname");
				String phone = userList.getString("phone");
				String email  = userList.getString("email");
				String streetname  = userList.getString("streetname");
				String streetno  = userList.getString("streetno");
				String city  = userList.getString("city");
				String state  = userList.getString("state");
				String zipcode  = userList.getString("zipcode");
				user.setUser( username, password, lastName, firstName, role, utaId, phone, email, streetno, streetname, city, state, zipcode);				  	
			}
			
			} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				conn.close();
				stmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			};
		return user;
	}
	
	private static ArrayList<User> ReturnMatchingUsersList (String queryString) {
		ArrayList<User> userListInDB = new ArrayList<User>();
		
		Statement stmt = null;
		Connection conn = SQLConnection.getDBConnection();  
		try {
			stmt = conn.createStatement();
			ResultSet userList = stmt.executeQuery(queryString);
			while (userList.next()) {
				User user = new User(); 
				user.setLastname(userList.getString("lastname"));
				user.setFirstname(userList.getString("firstname"));
				user.setUsername(userList.getString("username"));
				user.setRole(userList.getString("role"));
				user.setUtaid(userList.getString("utaid"));
				user.setCity(userList.getString("city"));
				user.setState(userList.getString("state"));
				user.setZipcode(userList.getString("zipcode"));
				user.setStreetname(userList.getString("streetname"));
				user.setStreetnumber(userList.getString("streetno"));
				user.setEmail(userList.getString("email"));
				user.setPhone(userList.getString("phone"));
				userListInDB.add(user);	
			}
		} catch (SQLException e) {}
		return userListInDB;
	}
	
	public static ArrayList<User>  searchUsers(String userlastname)  {  
		return ReturnMatchingUsersList(" SELECT * from user WHERE lastname LIKE '%"+userlastname+"%' ORDER BY lastname,firstname,role");
	}
	
	public static ArrayList<User>   searchUser (String username)  {  
		return ReturnMatchingUsersList(" SELECT * from user WHERE username = '"+username+"'");
	}
	
	public static void deleteUser(String username) {

		String sql = "delete from mavs_catering.user where username = 'bxs5834';";
		Statement stmt = null;
		Connection conn = SQLConnection.getDBConnection();  
		try { 
		      stmt = conn.createStatement();
		      
		      stmt.executeUpdate(sql);
		      System.out.println("Record deleted successfully");
		    } catch (SQLException e) {
		      e.printStackTrace();
		    }
	}
	
	public static ArrayList<String> checkRoles() {
		ArrayList<String> roles = new ArrayList<String>();
		Statement stmt = null;
		String sql = "select role from user;";
		Connection conn = SQLConnection.getDBConnection();
		try {
			stmt = conn.createStatement();
			ResultSet roleList = stmt.executeQuery(sql);
			while(roleList.next()) {
				roles.add(roleList.getString("role"));
			}
			//conn.close();
		}catch(SQLException e) {}
		return roles;
	}
	
	public static ArrayList<String> getUTAId(){
		ArrayList<String> ids = new ArrayList<String>();
		Statement stmt = null;
		String sql = "select utaid from user;";
		Connection conn = SQLConnection.getDBConnection();
		try {
			stmt = conn.createStatement();
			ResultSet idList = stmt.executeQuery(sql);
			while(idList.next()) {
				ids.add(idList.getString("utaid"));
			}
			//conn.close();
		}catch(SQLException e) {}
		return ids;
	}
	
	public static void modifyUser(String username, String role) {
		Connection conn = SQLConnection.getDBConnection();  
		PreparedStatement pstmt = null;
		String sqlUpdate = "update mavs_catering.user set role = '"+role+"' where username = '"+username+"';";
		try {
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(sqlUpdate);
			//pstmt.setString(1, role);
			//pstmt.setString(2, username);
			pstmt.executeUpdate(sqlUpdate);
			conn.commit();	
			//pstmt.close();
		}catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				conn.close();
				pstmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			};
		}
		
	}
}	
