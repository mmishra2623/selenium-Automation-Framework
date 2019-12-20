package code;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.io.FileUtils;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.ss.usermodel.*;

import code.BusinessKeywords;
import code.GlobalVariables;

public class Driver {
	public static BusinessKeywords businessKeywords;
	public static Method method[];
	public static String strdata=GlobalVariables.URL;
	public static String currDir;
	public static String testCasesPath;
	public static String resultsFilePath;
	public static String resultsDir;
	public static XSSFSheet ExcelWSheet;
	public static XSSFWorkbook ExcelWBook;
	public static org.apache.poi.ss.usermodel.Cell Cell;
	public static XSSFRow Row;
	public static String sTestCaseID;
	public static int iTestStep;
	public static String bResult;
	public static int iTestLastStep;
	public static String businessKeyword;
	public static String sData;
	public static int iTotalTestCases;
	public static String testCaseResult;
	public static String runID;
	public static int iTestcase;
	public static String interimStatusFilePath;
	public static String summaryReport;
	public static final String NEW_LINE_SEPERATOR = "\n";
	public static String executionStartDate;
	public static String executionStartTime;
	public static String executionEndDate;
	public static String executionEndTime;
	public static double elapsedTime;
	public static long elapsedTimeInMins;
	public static String actualResultsFolderPath;
	
	public Driver() throws NoSuchMethodException, SecurityException{
		businessKeywords = new BusinessKeywords();
		method = businessKeywords.getClass().getMethods();
	}
			
	public static void main(String[] args) throws Exception{
		DateFormat dateFormat= new SimpleDateFormat("MM/dd/yyyy");
		executionStartDate = dateFormat.format(new Date());
		DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");
		executionStartTime = dateFormat1.format(new Date());
		Date StartDateTime = new Date();
		
		currDir = System.getProperty("user.dir");
		testCasesPath = currDir+"\\runtime\\testcases\\Testcases.xlsx";
		runID = getRunID();
		resultsDir = currDir+"\\runtime\\results\\"+runID;
		resultsFilePath = resultsDir+"\\TestCases_Final_Results.xlsx";
		//Report Summary
		actualResultsFolderPath = resultsDir+"\\ActualResults";
		interimStatusFilePath = resultsDir+"\\InterimStatus.csv";
		summaryReport = resultsDir+"\\TestCases_SummaryReport.csv";
		createDirectory(resultsDir);
		createDirectory(actualResultsFolderPath);
		
		File interimStatus = new File(interimStatusFilePath);
		interimStatus.createNewFile();
		File source = new File(testCasesPath);
		File dest = new File(resultsFilePath);
		if(dest.exists()){
			dest.delete();
		}
		
		FileWriter fileWriter = new FileWriter(interimStatusFilePath);
		fileWriter.append(GlobalVariables.interimStatusHeader);
		fileWriter.append(NEW_LINE_SEPERATOR);
		fileWriter.flush();
		fileWriter.close();
		Files.copy(source.toPath(),dest.toPath());
		setExcelFile(resultsFilePath);
		Driver myDriver = new Driver();
		myDriver.runTestSet();
		executionEndDate = dateFormat.format(new Date());
		
		Date endDateTime = new Date(System.currentTimeMillis());
		
		elapsedTimeInMins = Driver.getTimeDifference(endDateTime, StartDateTime, Driver.TimeField.MINUTE);
		DateFormat dateFormat2 = new SimpleDateFormat("MM/dd/yyyy");
		executionEndDate = dateFormat2.format(new Date());
		DateFormat dateFormat3 = new SimpleDateFormat("HH:mm:ss");
		executionEndTime = dateFormat3.format(new Date());
		createSummaryResult();
		ExcelWBook.close();
		String[] parms = {"wscript", GlobalVariables.finalResultsFormatVbsFilePath, resultsFilePath};
		Runtime.getRuntime().exec(parms);
	}
	
	
	public static void runKeyword() throws Exception {
		for(int i=0;i<method.length;i++){
			if(method[i].getName().equals(businessKeyword)){
				if(sData.length() !=0){
					method[i].invoke(businessKeywords, sData);
				}else
				{
					method[i].invoke(businessKeywords);	
				}
				if(businessKeyword.equals("TCStart")||businessKeyword.equals("TCEnd")){
					setCellData(bResult,iTestStep,GlobalVariables.statusColumnID,GlobalVariables.sheet_TestCases);
					setCellData(BusinessKeywords.actualResult,iTestStep,GlobalVariables.actualResultColumnID,GlobalVariables.sheet_TestCases);
				}
				else if(bResult=="PASS"){
					setCellData(GlobalVariables.KEYWORD_PASS,iTestStep,GlobalVariables.statusColumnID,GlobalVariables.sheet_TestCases);
					setCellData(BusinessKeywords.actualResult,iTestStep,GlobalVariables.actualResultColumnID,GlobalVariables.sheet_TestCases);
				}
				else{
					setCellData(GlobalVariables.KEYWORD_FAIL,iTestStep,GlobalVariables.statusColumnID,GlobalVariables.sheet_TestCases);
					setCellData(BusinessKeywords.actualResult,iTestStep,GlobalVariables.actualResultColumnID,GlobalVariables.sheet_TestCases);
					System.out.println("next is on error");
//					BusinessKeywords.APP_OnErrorGoToBaseState();
				}
				
				}
			}
		}
		
		public static void setExcelFile(String Path) throws Exception {
			try{
				FileInputStream ExcelFile = new FileInputStream(Path);
				ExcelWBook = new XSSFWorkbook(ExcelFile);
			}
			catch (Exception e){
				Driver.bResult = "Fail";	
			}
		}
		@SuppressWarnings({ "static-access", "deprecation" })
		public static void setCellData(String Result, int RowNum, int ColNum, String SheetName)throws Exception {
			try{
				ExcelWSheet = ExcelWBook.getSheet(SheetName);
				Row = ExcelWSheet.getRow(RowNum);
				Cell = Row.getCell(ColNum,Row.RETURN_BLANK_AS_NULL);
				if (Cell == null){
					Cell = Row.createCell(ColNum);
					Cell.setCellValue(Result);
				}else {
					Cell.setCellValue(Result);
				}
				FileOutputStream fileOut = new FileOutputStream(resultsFilePath);
				ExcelWBook.write(fileOut);
				fileOut.close();
				ExcelWBook = new XSSFWorkbook(new FileInputStream(resultsFilePath));
			}catch(Exception e){
				Driver.bResult = "FAIL";
				}
			}
		public static String getRunID(){
			try{
				FileInputStream ExcelFile = new FileInputStream(testCasesPath);
				ExcelWBook = new XSSFWorkbook(ExcelFile);
				ExcelWSheet = ExcelWBook.getSheet("TestCases");
				Cell = ExcelWSheet.getRow(1).getCell(0);
				String CellData = Cell.getStringCellValue();
				ExcelWBook.close();
				return CellData;
			}catch (Exception e){
				Driver.bResult = "FAIL";
				return "";
			}
		}
		public static String getCellData(int RowNum, int ColNum, String sheetName) throws Exception {
		    try{
		    	ExcelWSheet = ExcelWBook.getSheet(sheetName);
		    	Cell = ExcelWSheet.getRow(RowNum).getCell(ColNum);
		    	String CellData = Cell.getStringCellValue();
		    	return CellData;
		    }catch (Exception e){
		    	Driver.bResult = "FAIL";
		    	return "";
			}
		}
		public static void createDirectory(String directoryPath) throws IOException{
			File file = new File(directoryPath);
			if(file.exists()){
				FileUtils.forceDelete(new File(directoryPath));
			}
			file.mkdir();
		}
		
		public static int getRowCount(String SheetName){
			int iNumber=0;
			try{
				ExcelWSheet = ExcelWBook.getSheet(SheetName);
				iNumber=ExcelWSheet.getLastRowNum()+1;
			} catch (Exception e){
				Driver.bResult = "FAIL";
			}
			return iNumber;
		}
		public static int getRowContains(String sTestCaseName, int colNum, String SheetName) throws Exception{
			int iRowNum=0;
			try{
				int rowCount = getRowCount(SheetName);
				for (;iRowNum<rowCount;iRowNum++){
					if (getCellData(iRowNum,colNum,SheetName).equalsIgnoreCase(sTestCaseName)){
						break;
					}
				}
			}catch (Exception e){
				Driver.bResult = "FAIL";
			}
			return iRowNum;
		}
		public static int getTestStepsCount(String SheetName, String sTestCaseID, int iTestCaseStart) throws Exception{
			try{
				for(int i=iTestCaseStart;i<=getRowCount(SheetName);i++){
					if(getCellData(i,GlobalVariables.keywordColumnID,SheetName).equals("TCEnd")){
						int number=i;
						return number;
					}
				}
				ExcelWSheet = ExcelWBook.getSheet(SheetName);
				int number=ExcelWSheet.getLastRowNum()+1;
				return number;
			}catch (Exception e){
				Driver.bResult = "FAIL";
				return 0;
			}
		}
		
		public void runTestSet() throws Exception{
			String tcEndKeyword;
			iTotalTestCases = getRowCount("TestSet");
			setCellData("ActualResult",iTestStep,GlobalVariables.actualResultColumnID,GlobalVariables.sheet_TestCases);
			setCellData("Status", iTestStep ,GlobalVariables.statusColumnID,GlobalVariables.sheet_TestCases);
			for(iTestcase=1;iTestcase<iTotalTestCases;iTestcase++){
				sTestCaseID = getCellData(iTestcase,GlobalVariables.testSet_testcaseColID,GlobalVariables.sheet_TestSet);
				iTestStep = getRowContains(sTestCaseID, GlobalVariables.testCaseIDColumnID, GlobalVariables.sheet_TestCases);
				iTestLastStep = getTestStepsCount(GlobalVariables.sheet_TestCases,sTestCaseID,iTestStep);
				
				for(;iTestStep<=iTestLastStep;iTestStep++){
					businessKeyword = getCellData(iTestStep,GlobalVariables.keywordColumnID, GlobalVariables.sheet_TestCases);
					sData = getCellData(iTestStep, GlobalVariables.inputORExpValueColumnID,GlobalVariables.sheet_TestCases);
					runKeyword();
					if(bResult=="FAIL"){
						testCaseResult="FAIL";
						do{
							tcEndKeyword = getCellData(iTestStep,GlobalVariables.keywordColumnID, GlobalVariables.sheet_TestCases);
							iTestStep = iTestStep + 1;
						}while (tcEndKeyword == "TCEnd");
						BusinessKeywords.APP_OnErrorGoToBaseState();
					}else if (bResult=="PASS"){
						testCaseResult = "PASS";
					}
				}
				FileWriter fileWriter = new FileWriter(interimStatusFilePath, true);
				fileWriter.append(runID+","+(iTotalTestCases-1)+","+sTestCaseID+","+testCaseResult);
				fileWriter.append(NEW_LINE_SEPERATOR);
				fileWriter.close();
			}
		}
		public static void createSummaryResult() throws IOException{
			String spiltBy = ",";
			int passedTestcases = 0;
			int failedTestcases = 0;
			int totalTestcases = 0;
			int notRun = 0;
			double passPercentage;
			double failPercentage;
			
			BufferedReader br= new BufferedReader(new FileReader(interimStatusFilePath));
			String line = br.readLine();
			while ((line = br.readLine()) !=null){
				String[] b = line.split(spiltBy);
				String resultStatus = b[4];
				if(resultStatus.equals("PASS")){
					passedTestcases = passedTestcases +1;
				}
				else if (resultStatus.equals("FAIL")){
					failedTestcases = failedTestcases +1;
				}
				else if (resultStatus.equals("NORUN")){
					notRun = notRun+1;
				}
			}
			totalTestcases = passedTestcases +failedTestcases +notRun;
			passPercentage = (passedTestcases/totalTestcases)*100;
			failPercentage = (failedTestcases/totalTestcases)*100;
			
			System.out.println(passedTestcases +" "+failedTestcases+" "+totalTestcases+" "+passPercentage+" "+failPercentage);
			br.close();
			File summaryReportFile = new File(summaryReport);
			summaryReportFile.createNewFile();
			FileWriter fileWriter = new FileWriter(summaryReport,true);
			fileWriter.append(GlobalVariables.summaryReportHeader);
			fileWriter.append(NEW_LINE_SEPERATOR);
			fileWriter.append(GlobalVariables.projectName+","+Driver.runID+","+totalTestcases+","+passedTestcases+","+failedTestcases+","+notRun+","+passPercentage+","+failPercentage+","+Driver.executionStartDate+","+Driver.executionStartTime+","+Driver.executionEndDate+","+Driver.executionEndTime+","+Driver.elapsedTimeInMins);
			fileWriter.flush();
			fileWriter.close();
		}
		
		public static long getTimeDifference(Date d1, Date d2, TimeField field){
			return Driver.getTimeDifference(d1, d2)[field.ordinal()];
		}
		
		public static long[] getTimeDifference(Date d1,Date d2){
			long[] result = new long[5];
			Calendar cal = Calendar.getInstance();
			cal.setTimeZone(TimeZone.getTimeZone("UST"));
			cal.setTime(d1);
			long t1 = cal.getTimeInMillis();
			cal.setTime(d2);
			long diff = Math.abs(cal.getTimeInMillis()-t1);
			final int ONE_DAY = 1000*60*60*24;
			final int ONE_HOUR = ONE_DAY/24;
			final int ONE_MINUTE = ONE_HOUR/60;
			final int ONE_SECOND = ONE_MINUTE/60;
			long d =diff/ONE_DAY;
			diff %= ONE_DAY;
			long h = diff/ONE_HOUR;
			diff %= ONE_HOUR;
			long m = diff/ONE_MINUTE;
			diff %=ONE_MINUTE;
			long s = diff/ONE_SECOND;
			long ms = diff%ONE_SECOND;
			result[0]=d;
			result[1]=h;
			result[2]=m;
			result[3]=s;
			result[4]=ms;
			return result;
		}
		
		public static void printDiffs(long[] diffs){
			System.out.printf("Days:          %3d\n",diffs[0]);
			System.out.printf("Hours:         %3d\n",diffs[1]);
			System.out.printf("Minutes:       %3d\n",diffs[2]);
			System.out.printf("Seconds:       %3d\n",diffs[3]);
			System.out.printf("Milliseconds:  %3d\n",diffs[4]);
		}
		
		public static enum TimeField {
			DAY,
			HOUR,
			MINUTE,
			SECOND,
			MILLISECOND;
			
		}
	}
	

















