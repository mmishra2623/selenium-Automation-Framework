package code;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import junit.framework.Assert;

public class BusinessKeywords extends ObjectRepository{
	
	public static WebDriver driver;
	public static String actualResult = null;
	public static Process proc;
	public static String onErrorUserName;
	public static String onErrorPassword;
	
	public static void TCStart(){
		Date date = new Date();
		String str = String.format("StartTime :%tc",date);
		BusinessKeywords.actualResult = "Execution is started for the test case "+ Driver.sTestCaseID;
		Driver.bResult = str;
	}
	public static void TCEnd(){
		Date date = new Date();
		String str = String.format("EndTime :%tc",date);
		BusinessKeywords.actualResult = "Execution is Ended for the test case "+ Driver.sTestCaseID;
		Driver.bResult = str;

	}	
    
	public static void LaunchApplication(){
		try{
			System.setProperty("webdriver.chrome.driver","B:\\AutomationFramework\\WebAutomation\\Drivers\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.get(GlobalVariables.URL);
		    System.out.println("URL Entered");
		    PageFactory.initElements(driver, ObjectRepository.class);
			
		}catch(Exception e){
			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
			Driver.bResult = "FAIL";
		}
		BusinessKeywords.actualResult = "Application is Invoked";
		Driver.bResult = "PASS";
	}
	
	public static void APP_CloseApplication(){
		try{
			driver.quit();
			Thread.sleep(10000);
			System.out.println("Application is Closed");
			
		}catch(Exception e){
			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
			Driver.bResult = "FAIL";
		}
		BusinessKeywords.actualResult = "Application is Invoked";
		Driver.bResult = "PASS";
	}
	
	public static HashMap<String,Object> createArgsMap(String myString){
		HashMap<String,Object> myDict = new HashMap<String,Object>();
		String[] paramsArray = myString.split("\\|");
		int numOfparams = paramsArray.length;
		for (int paramIndex=0;paramIndex<numOfparams;paramIndex++){
			String[] argArray = paramsArray[paramIndex].split("=");
			myDict.put(argArray[0], argArray[1]);
		}
		return myDict;
	}
	
	public static void APP_OnErrorGoToBaseState() throws IOException{
		
		File scr = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
		FileUtils.copyFile(scr, new File(Driver.actualResultsFolderPath+"//"+Driver.sTestCaseID+".png"));
		
		driver.quit();
		driver = new ChromeDriver();
		driver.get(GlobalVariables.URL);
		driver.manage().window().maximize();
		PageFactory.initElements(driver, ObjectRepository.class);
		
		try{
			ObjectRepository.LoginPage_userNameTextBox.sendKeys(onErrorUserName);
			ObjectRepository.LoginPage_passwordTextBox.sendKeys(onErrorPassword);
			ObjectRepository.LoginPage_LoginButton.click();
			
		}catch (Exception e){
			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
			Driver.bResult = "FAIL";
			return;
		}
	}
	
	public static void Login(String params){
		HashMap<String,Object> myDict = createArgsMap(params);
		String userName = myDict.get("userName").toString();
		onErrorUserName = userName;
		String password = myDict.get("password").toString();
		onErrorPassword = password;
		try{	
      		ObjectRepository.LoginPage_userNameTextBox.sendKeys(userName);
			ObjectRepository.LoginPage_passwordTextBox.sendKeys(password);
			ObjectRepository.LoginPage_LoginButton.click();
				
		}catch (Exception e){
			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
			Driver.bResult = "FAIL";
			return;
		}
		BusinessKeywords.actualResult = "Application is Invoked";
		Driver.bResult = "PASS";
	}


     public static void Type (String params){
    	 HashMap<String,Object> myDict = createArgsMap(params);
    	 String Route = myDict.get("Route").toString();
    	 String RouteSelection = "//input[@name='tripType'][@value='"+Route+"']";
    	   	 
    	 try{
    		 driver.findElement(By.xpath(RouteSelection)).click();
    		 
    	 } catch (Exception e){
 			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
 			Driver.bResult = "FAIL";
 			return;
    	 }
     }
     
     public static void Passengers (String params){
    	 HashMap<String,Object> myDict = createArgsMap(params);
    	 String Tickets = myDict.get("Tickets").toString();
    	 String NoOfTickets = "//select[@name='passCount']/option[@value='"+Tickets+"']";
    	 try{
    		 driver.findElement(By.xpath(NoOfTickets)).click();
    		 
    	 } catch (Exception e){
  			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
  			Driver.bResult = "FAIL";
  			return;
     }
     }
     
     public static void DepartingFrom (String params){
    	 HashMap<String,Object> myDict = createArgsMap(params);
    	 String FromPlace = myDict.get("Origin").toString();
    	 String Place = "//select[@name='fromPort']/option[@value='"+FromPlace+"']";
     
     try{
    	 driver.findElement(By.xpath(Place)).click();
    	 
     } catch (Exception e){
			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
			Driver.bResult = "FAIL";
			return;
     }
     
}
     public static void Preferences_Class(String params){
    	 HashMap<String,Object> myDict = createArgsMap(params);
    	 String Class = myDict.get("ServiceClass").toString();
    	 String SClass = "//input[@name='servClass'][@value='"+Class+"']";
    	 
    	 try{
    		 driver.findElement(By.xpath(SClass)).click();
    	 }catch (Exception e){
 			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
 			Driver.bResult = "FAIL";
 			return;
    	 }
     }
     
     public static void Preferences_Airline(String params){
    	 HashMap<String,Object> myDict = createArgsMap(params);
    	 String airline = myDict.get("Airline").toString();
    	 String pAirline = "//select[@name='airline']/option[text()='"+airline+"']";
    	 
    	 try{
    		 driver.findElement(By.xpath(pAirline)).click();
    		 
    	 }catch (Exception e){
  			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
  			Driver.bResult = "FAIL";
  			return;
     }
  }
     
     public static void FlightSearchContinue (){
    	 try{
    		 ObjectRepository.FlightSearch_Continue.click();
    		 
    	 }catch (Exception e){
 			BusinessKeywords.actualResult = "This step is failed because of following reason" +e.toString();
 			Driver.bResult = "FAIL";
 			return;
     } 	 
}
       
}

















