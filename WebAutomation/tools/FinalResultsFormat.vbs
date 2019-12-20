pathoftheFile = WScript.Arguments.Item(0)
CSVFormatResultsFilePath = replace(pathoftheFile,".xlsx",".csv")
Set objExcel = createObject("Excel.Application")
'objExcel.Visible = true
objExcel.DisplayAlerts = false
Set objFinalResultWB = objExcel.Workbooks.Open(pathoftheFile)
objFinalResultWB.Worksheets(1).delete

Set objFinalResultWS = objFinalResultWB.WorkSheets(1)
objFinalResultWS.Columns("G:G").EntireColumn.Cut
objFinalResultWS.Columns("J:J").EntireColumn.Select
objFinalResultWS.Paste
objFinalResultWS.Columns("G:G").EntireColumn.delete

objFinalResultWB.Saveas CSVFormatResultsFilePath,23
objFinalResultWB.Save
objFinalResultWB.Close
Set objFinalResultWB = nothing

Set fso=createobject("Scripting.FileSystemObject")
If fso.fileExists(pathoftheFile) Then 
    fso.DeleteFile(pathoftheFile)
End If

objExcel.Quit
