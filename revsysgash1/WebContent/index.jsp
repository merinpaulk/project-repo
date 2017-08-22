<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">


<script type="text/javascript">
function confirmSubmit()
{
var agree=confirm("This will change the transaction status. Are you sure you wish to continue?");
if (agree)
	return true ;
else
	return false ;
}
</script>

<%@ page import="javax.naming.*,java.sql.*,javax.sql.*,java.util.*,mcsimdb.ActiveTransactions,model.McTransaction" %>

<%
	ActiveTransactions db1 = new ActiveTransactions();
	List<McTransaction> txns = db1.getActiveTransactions();
	int rows = txns.size();
	
%>
<p/>
<i>ALL</i> transactions (Current & Historic) are listed below 
<p/>







<table border="1">
 <tr>
 <th>Txn id</th>
 <th>Number of Patents</th>
 <th>P3S Ref</th>
 <th>Create Date</th>
 <th>Target Date</th>
 <th>Status</th>
 <th>Last Change <br/>date</th>
 <th>amount_usd</th>
 <th colspan=2>CHANGE !</th>
 </tr>

<% 
	for (int ii=0; ii<rows; ii++) { 
		McTransaction dis = txns.get(ii);	
%>
 <tr>

 <td><%= dis.getTransactionId() %></td>
 <td><%= dis.numPatents %></td>
 <td><%= dis.P3S_TransRef %></td>
 <td><%= dis.getTransStartDate() %></td>
 <td><%= dis.getTransTargetEndDate() %></td>
 <td><%= dis.latestTransStatus %></td>
 <td><%= dis.getLastUpdatedDate() %></td>

 <td>$ <%= dis.getTransAmount_USD() %></td>

	<form action="beanjsps/UpdateTransaction.jsp?txnid=<%out.print(dis.getTransactionId());%>"   method=post>
 <td>
		<input type=submit name="submit" value="Change to:" onclick="return confirmSubmit()"/>
 </td>
 <td>
		<input type="hidden" name="txnia" value="<%out.print(dis.getTransactionId());%>" />
 		<select name="newstatus" >
 			<option value="Initiated">Initiated</option>
 			<option value="Pending">Pending</option>
 			<option value="Funds Received">Funds Received</option>
 			<option value="Funds Sent">Funds Sent</option>
 			<option value="EPO Received">EPO Received</option>
 			<option value="EPO Instructed">EPO Instructed</option>
 			<option value="Failed">Failed</option>
 			<option value="Completed">Completed</option>
 		</select>
 </td>
	</form> 	 

 </tr>
<%
 }
%>

</table>



<p/>
&nbsp;
<p/>
Setting status to Completed or Failed changes the transaction from Current to Historic
<br/>
& also amends:
<br/>&nbsp;&nbsp;&nbsp;&nbsp;
renewal : renewal_status
<br/>&nbsp;&nbsp;&nbsp;&nbsp;
patent : renewal_status
<br/>&nbsp;&nbsp;&nbsp;&nbsp;
patent : epo_patent_status
<br/>&nbsp;&nbsp;&nbsp;&nbsp;
patent : last_renewed_date_ex_epo


<p/>
Note: MoneyCorp will never cause a status change TO:
<br/>&nbsp;&nbsp;&nbsp;&nbsp;
Initiated
<br/>&nbsp;&nbsp;&nbsp;&nbsp;
EPO Instructed
<br/>&nbsp;&nbsp;&nbsp;&nbsp;
Completed
<br/>&nbsp;&nbsp;&nbsp;&nbsp;
& is unclear if/how they would cause us to change to status : Failed
<p/>

		
</html>