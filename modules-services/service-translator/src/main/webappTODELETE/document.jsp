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
	    <div style="text-align:left;">
	      <table style="margin-left: auto; margin-right: auto;">
		<tr>
		  <td> </td>
	          <td rowspan="3">
                    <a  href="translation.html" class="btn btn-primary btn-large" style="text-align:center; width:80px; "/>translate</a><br>
                    <a  href="validation/report.html" class="btn btn-primary btn-large" style="text-align:center; width:80px; "/>validate</a><br>
		  </td>
		</tr>
	      </table>
	    </div>
	</div>
	
    <%@ include file="terms.jsp" %>       
      
    </section>

<!--    <%@ include file="footer.jsp" %>  -->
<jsp:include page="footer.jsp">
    <jsp:param name="adjust" value="../../" />
</jsp:include>

  </body>
</html>
