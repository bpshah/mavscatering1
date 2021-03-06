package selenium;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.FileInputStream;
import java.sql.SQLException;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Properties;

import functions.CateringManagementFunctions;
import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)

public class AdminPageTest extends CateringManagementFunctions{
	
	private StringBuffer verificationErrors = new StringBuffer();
	public String sAppURL, sSharedUIMapPath, testDelay,username,password;
	
	public String[][] getTableContentsFromPage(int rows){

		String [][] userArray = new String[rows-1][4];
		for (int i=0; i<rows-1; i++) {
			userArray[i][0]=  driver.findElement(By.xpath(prop.getProperty("Txt_seachUserResults_prefix")+(i+2)+
								prop.getProperty("Txt_searchUserResults_LastNameCol"))).getText();
			userArray[i][1] = driver.findElement(By.xpath(prop.getProperty("Txt_seachUserResults_prefix")+(i+2)+
								prop.getProperty("Txt_searchUserResults_FirstNameCol"))).getText();
			userArray[i][2] = driver.findElement(By.xpath(prop.getProperty("Txt_seachUserResults_prefix")+(i+2)+
								prop.getProperty("Txt_searchUserResults_UserNameCol"))).getText();
			userArray[i][3] = driver.findElement(By.xpath(prop.getProperty("Txt_seachUserResults_prefix")+(i+2)+
								prop.getProperty("Txt_searchUserResults_RoleCol"))).getText();
		
			System.out.println("Table:");
			System.out.println(userArray[i][0]+userArray[i][1]+userArray[i][2]+userArray[i][3]);

		}
		System.out.println("IS EMPTY? "+userArray.length);
	  return userArray;

	}
	
	private Boolean arraysDiff (String [][] array1, String [][] array2) { // this method compares the contents of the two tables
		  Boolean diff= false || (array1.length!=array2.length);
		  for (int i=0;i<array1.length && !diff;i++) {
			 diff  = !array1[i][0].equals(array2[i][0]) || !array1[i][1].equals(array2[i][1]) || 
					 !array1[i][2].equals(array2[i][2]) || !array1[i][3].equals(array2[i][3]);
		  }
		  return diff;
	  }


	
	@Before
	public void setUp() throws Exception {
		driver = invokeCorrectBrowser();
	    //driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	    prop = new Properties();	  
	    prop.load(new FileInputStream("./configuration/configuration.properties"));
		sAppURL = prop.getProperty("sAppURL");
		sSharedUIMapPath = prop.getProperty("SharedUIMap");
		testDelay=prop.getProperty("testDelay");
		prop.load(new FileInputStream(sSharedUIMapPath));
	}

		//Verifying Admin HomePage Elements
		@Test
		@FileParameters("test/selenium/AdminHomePage1.csv")
		public void test1(int testCaseNumber, String title, String logout,String searchUser, String viewProfile) throws InterruptedException {
			//fail("Not yet implemented");
			driver.get(sAppURL);
			CM_Login(driver,"axk987","Bhumit!23","AdminHomePageLogin"+testCaseNumber);
			verifyAdminHomePageElements(driver,title,searchUser,viewProfile,logout,"AdminHomePageVerify");
		}
	
		//Verifying SearchUserPage Elements
		@Test
		@FileParameters("test/selenium/AdminHomePage2.csv")
		public void test2(int testCaseNumber,String title,String logout, String hLastName) throws InterruptedException {
			//fail("Not yet implemented");
			driver.get(sAppURL);
			CM_Login(driver,"axk987","Bhumit!23","AdminHomePageLogin"+testCaseNumber);
			verifySearchUserPageElements(driver,title,logout,hLastName,"SearchUserVerification"+testCaseNumber);
			
		}

		//Verifying  Search User Validation
		@Test
		@FileParameters("test/selenium/AdminHomePage3.csv")
		public void test3(int testCaseNumber,String lastName,String errMsg,String lastNameErr) throws InterruptedException {
			
			driver.get(sAppURL);
			CM_Login(driver,"axk987","Bhumit!23","AdminHomePageLogin"+testCaseNumber);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminHomePage_Search_for_User"))).click();
			Thread.sleep(1000);
			validateSearchUser(driver,lastName,errMsg,lastNameErr,"SearchUserLastNameValidations"+testCaseNumber);

			
		}
		
		//Verifying Search user results
		@Test
		@FileParameters("test/selenium/AdminHomePage4.csv")
		public void test4(int testCaseNumber,String lastNameHeader,String firstNameHeader,String userNameHeader,String roleHeader,String lastName) throws InterruptedException {
			//fail("Not yet implemented");
			driver.get(sAppURL);
			CM_Login(driver,"axk987","Bhumit!23","AdminHomePageLogin"+testCaseNumber);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminHomePage_Search_for_User"))).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).clear();
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).sendKeys(lastName);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminSearchUser_submit"))).click();
			Thread.sleep(1000);

			verifySearchUserResultsHeaders(driver,lastNameHeader,firstNameHeader,userNameHeader,roleHeader,"verifySearchUserResultsHeaders"+testCaseNumber);
			WebElement userTable = driver.findElement(By.xpath(prop.getProperty("Table_SearchUser_table")));
			int rows = userTable.findElements(By.tagName("tr")).size();
			try {
				assertFalse(arraysDiff(getUsersFromDB(rows,lastName), getTableContentsFromPage(rows)));
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Thread.sleep(1000);
		}
		
		//Verifying view profile page elements
		@Test
		@FileParameters("test/selenium/AdminHomePage5.csv")
		public void test5(int testCaseNumber,String title,String subtitle,String h1,String h2,String h3,String h4,String h5,String h6,String h7,String h8,
				String h9,String h10,String h11,String h12,String lastName) throws InterruptedException {
			//fail("Not yet implemented");
			driver.get(sAppURL);
			CM_Login(driver,"axk987","Bhumit!23","AdminHomePageLogin"+testCaseNumber);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminHomePage_Search_for_User"))).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).clear();
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).sendKeys(lastName);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminSearchUser_submit"))).click();
			Thread.sleep(1000);
			String username = driver.findElement(By.xpath(prop.getProperty("Txt_seachUserResults_prefix")+"2"+prop.getProperty("Txt_searchUserResults_UserNameCol"))).getText();
			driver.findElement(By.xpath(prop.getProperty("Link_ViewProfile_view1"))).click();
			Thread.sleep(1000);
			verifyViewProfilePageHeaders(driver,"Header_ViewProfile_header",title,"Header2_ViewProfile_header2",subtitle,
					"Header_ViewProfile_Username",h1,
					"Header_ViewProfile_Role",h2,
					"Header_ViewProfile_UTAid",h3,
					"Header_ViewProfile_FirstName",h4,
					"Header_ViewProfile_LastName",h5,
					"Header_ViewProfile_Phone",h6,
					"Header_ViewProfile_Email",h7,
					"Header_ViewProfile_StreetNumber",h8,
					"Header_ViewProfile_StreetName",h9,
					"Header_ViewProfile_City",h10,
					"Header_ViewProfile_State",h11,
					"Header_ViewProfile_Zipcode",h12,
					"ViewProfilePageHeaderTestCase"+testCaseNumber);
			verifyViewUserContent(driver,username,"VerifyViewUserContent"+testCaseNumber);
			driver.findElement(By.xpath(prop.getProperty("Link_ViewProfile_Logout"))).click();
			Thread.sleep(1000);

		}
		

	//Modifying Profile Admin
	@Test
	@FileParameters("test/selenium/AdminHomePage9.csv")
	public void test6(int testCaseNumber,String firstNameErr,String firstName,String lastName,String Phone,String email,String StreetNo,String StreetName,
			 String City,String State,String Zipcode,String err,String err1) throws InterruptedException {
		//fail("Not yet implemented");
		driver.get(sAppURL);
		CM_Login(driver,"axk987","Bhumit!23","AdminHomePageLogin"+testCaseNumber);
		driver.findElement(By.xpath(prop.getProperty("Link_AdminHomePage_view/ModifyProfile"))).click();
		Thread.sleep(1000);
		modifyProfile(driver,firstNameErr,firstName,lastName,Phone,email,StreetNo,StreetName,
				 City,State,Zipcode,err,err1,"ModifyProfileAdmin"+testCaseNumber);
		
	}
	
		//Change Role Validation
		@Test
		@FileParameters("test/selenium/AdminHomePage7.csv")
		public void test7(int testCaseNumber,String lastName,String role,String err,String err1) throws InterruptedException {
			//fail("Not yet implemented");
			driver.get(sAppURL);
			CM_Login(driver,"axk987","Bhumit!23","AdminHomePageLogin"+testCaseNumber);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminHomePage_Search_for_User"))).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).clear();
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).sendKeys(lastName);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminSearchUser_submit"))).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(prop.getProperty("Link_ViewProfile_modify1"))).click();
			changeRoleValidation(driver,role,err,err1,"ChangeRoleValidation"+testCaseNumber);
			
		}

		//Change Role
		@Test
		@FileParameters("test/selenium/AdminHomePage8.csv")
		public void test8(int testCaseNumber,String lastName,String role) throws InterruptedException {
			//fail("Not yet implemented");
			driver.get(sAppURL);
			CM_Login(driver,"axk987","Bhumit!23","AdminHomePageLogin"+testCaseNumber);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminHomePage_Search_for_User"))).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).clear();
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).sendKeys(lastName);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminSearchUser_submit"))).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(prop.getProperty("Link_ViewProfile_modify1"))).click();
			changeRole(driver,role,"ChangeRole"+testCaseNumber);
			driver.findElement(By.xpath(prop.getProperty("Link_AdminViewUser_logout"))).click();
			Thread.sleep(1000);
			
		}
		
		//Deleting User
		@Test
//		@FileParameters("test/selenium/AdminHomePage6.csv")
		public void test9() throws InterruptedException {
			driver.get(sAppURL);
			driver.findElement(By.xpath(prop.getProperty("Btn_login_Register"))).click();
			Thread.sleep(1000);
			RegisterCatererManager(driver,"abc123","Bhumit!23","User","1234567890","Abhishek","Shah","1234567890","abc@gmail.com","848","Mitchell",
					"Arlington","TX","76001", "UserRegistraion");
			CM_Login(driver,"axk987","Bhumit!23","AdminHomePageLogin1");
			driver.findElement(By.xpath(prop.getProperty("Link_AdminHomePage_Search_for_User"))).click();
			Thread.sleep(1000);
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).clear();
			driver.findElement(By.xpath(prop.getProperty("Txt_AdminSearchUser_UsersLastname"))).sendKeys("Shah");
			driver.findElement(By.xpath(prop.getProperty("Link_AdminSearchUser_submit"))).click();
			Thread.sleep(1000);
			deleteUser(driver,"DeleteUser");
			driver.findElement(By.xpath(prop.getProperty("Link_AdminViewUser_logout"))).click();
			Thread.sleep(1000);
		}

		@After
	  public void tearDown() throws Exception {
	    driver.quit();
	    String verificationErrorString = verificationErrors.toString();
	    if (!"".equals(verificationErrorString)) {
	      fail(verificationErrorString);
	    }
	  }


}
