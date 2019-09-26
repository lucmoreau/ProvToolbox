<html>

<%@page import="org.apache.log4j.Logger" %>
<% Logger logger=Logger.getLogger(this.getClass().getName());%>

	    <% 
                String myId=request.getAttribute("docId").toString();
                String kind=request.getAttribute("kind").toString();
		logger.debug(" >>>>>>> docId " + myId);
		logger.debug(" >>>>>>> kind " + kind);
	    String resourceUri=org.openprovenance.prov.service.core.ServiceUtils.getRequestURL(request,"provapi",myId, "/vis/" + kind);
		logger.debug(" >>>>>>> resourceUri " + resourceUri);
		 %>



<iframe width="100%" height="100%" scrolling="no" src="<%=resourceUri%>">

</html>
