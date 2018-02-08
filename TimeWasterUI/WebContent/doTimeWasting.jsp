<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Time Wasting Site</title>
</head>
<body>

<%@ page import="org.apache.log4j.Logger" %> 
<%@ page import="com.datadog.presales.load.TimeWasterUI" %> 
<% // Get the form requests 
Logger.getLogger(this.getClass()).info("Was called");
String secondsToWaste = request.getParameter("secondsToWaste"); 
Logger.getLogger(this.getClass()).info("secondsToWaste set to [" + secondsToWaste + "].");

// Using DataModel in a JSP 
TimeWasterUI twui = new com.datadog.presales.load.TimeWasterUI();
String timeTaken = "";
if (secondsToWaste == null ) {
	timeTaken = "Input was null";
} else {
	timeTaken = twui.calculateNumber(secondsToWaste);
}

%> 

<h1>Thanks for wasting time</h1> 
<p>Kindly have a look what you have entered :</p> 
<table cellspacing="0" cellpadding="5" border="1"> 
	<tr> 
		<td align="right">Time Requested :</td> <td><%=secondsToWaste%></td> 
	</tr>	
	<tr> 
		<td align="right">Time Wasted (in ms):</td> <td><%=timeTaken%></td> 
	</tr> 
</table> 
<p> To go back, click 'back' on browser window 
<br>or click on Back button below : 
</p> 
<form action="TimeWaster.html" method="post"> <input type="submit" value="Back"> 
</form>

</body>
</html>