<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>Translation Landing page</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="../../../bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
    </style>
    <script src="../../../jquery/jquery-1.7.2.min.js"></script>
    <link href="../../../bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->

    <script type="text/javascript">
      var _gaq = _gaq || [];
      _gaq.push(['_setAccount', 'UA-39466031-1']);
      _gaq.push(['_trackPageview']);
      (function() {
      var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
      ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
      var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
      })();
    </script>
  </head>

  <body>


<jsp:include page="header.jsp">
    <jsp:param name="adjust" value="../../" />
</jsp:include>




    
    <section class="round">
      <div class="span12 row-fluid">
	<div class="span8" style="text-align:center;margin-top:80px;margin-bottom:50px">
	    <style type="text/css">
	      .input-append .btn .dropdown-toggle {
	      float: none;
	      }
	    </style>

	    <div>
<%@page import="org.apache.log4j.Logger" %>
<% Logger logger=Logger.getLogger(this.getClass().getName());%>

	    <% 
                String myId=request.getAttribute("docId").toString();
		logger.debug(" >>>>>>> docId " + myId);
	        String resourceUri=org.openprovenance.prov.service.core.ServiceUtils.getRequestURL(request,"provapi",myId, "");
		logger.debug(" >>>>>>> resourceUri " + resourceUri);
		 %>
	    </div>
	    
	    <div style="text-align:left;">
	      <table style="margin-left: auto; margin-right: auto;">
		<tr>
		  <td> </td>
	          <td rowspan="3">
                    <a id="link-ttl"   href="<%=resourceUri%>.ttl"   class="btn btn-primary btn-large" style="text-align:center; width:80px; "/>ttl</a><br>
                    <a id="link-provx" href="<%=resourceUri%>.provx" class="btn btn-primary btn-large" style="text-align:center; width:80px; "/>provx</a><br>
                    <a id="link-provn" href="<%=resourceUri%>.provn" class="btn btn-primary btn-large" style="text-align:center; width:80px; "/>provn</a><br>
                    <a id="link-json"  href="<%=resourceUri%>.json"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/>json</a><br>
                    <a id="link-trig"  href="<%=resourceUri%>.trig"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/>trig</a><br>
                    <a id="link-svg"   href="<%=resourceUri%>.svg"   class="btn btn-primary btn-large" style="text-align:center; width:80px; "/>svg</a><br>
		  </td>
		</tr>
	      </table>
	    </div>
	</div>
	
    <%@ include file="terms.jsp" %>       


    </section>

<script>
// Use javascript to update the links, with the correct url, as seen by client
// (server, may behind a proxy)
    $('#header-translator').addClass("active");
    var myUrl=document.URL;
    myUrl=myUrl.replace("/view/","/provapi/");
    var suffix="/translation.html";
    $('#link-ttl').attr('href',myUrl.replace(suffix,".ttl"));
    $('#link-provx').attr('href',myUrl.replace(suffix,".provx"));
    $('#link-provn').attr('href',myUrl.replace(suffix,".provn"));
    $('#link-json').attr('href',myUrl.replace(suffix,".json"));
    $('#link-trig').attr('href',myUrl.replace(suffix,".trig"));
    $('#link-svg').attr('href',myUrl.replace(suffix,".svg"));
</script>    

<jsp:include page="footer.jsp">
    <jsp:param name="adjust" value="../../" />
</jsp:include>

  </body>
</html>
