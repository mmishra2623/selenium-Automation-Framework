package code;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class ObjectRepository {

	@FindBy(xpath="//input[@name='userName']")
	public static WebElement LoginPage_userNameTextBox;
	
	@FindBy(xpath="//input[@name='password']")
	public static WebElement LoginPage_passwordTextBox;
	
	@FindBy(xpath="//input[@name='login']")
	public static WebElement LoginPage_LoginButton;	
	
	@FindBy(xpath="//input[@name='findFlights']")
	public static WebElement FlightBooking_Homepage;
	
	@FindBy(xpath="//input[@name='tripType'][@value='oneway']")
	public static WebElement FlightBooking_ToNFro;
	
	@FindBy(xpath="//input[@name='findFlights']")
	public static WebElement FlightSearch_Continue;
}
