<jsp:useBean id="txn" class="formHandlers.P3sTransaction" scope="session"/>
<jsp:setProperty name="txn" property="*"/> 

<%
String txnid = (request.getParameter( "txnid" ));
String newstatus = (request.getParameter( "newstatus" ));

// System.out.println("updatetransaction.jsp  : txnid is "+txnid+"   NewStatus="+newstatus);


txn.runSave(txnid, newstatus);
if (txn.validate())
{
	txn.empty();
	%>  
	<script>
		alert('Saved');
		window.location = "../index.jsp";
	</script>
	<%

	// Now prepare data for the next page display
	txn.populate();

}
else
{
	%>  
	<script>
		alert('Change failed');
		window.location = "javascript: history.go(-1)";
	</script>
	<%
}

%>

<!-- 
		window.location = "../step3.jsp?user=< % =user% >&clean=T";
 -->



