<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>ProvTranslator</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="../bootstrap/css/bootstrap.css" rel="stylesheet">
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
    </style>
    <script src="../jquery/jquery-1.7.2.min.js"></script>
    <link href="../bootstrap/css/bootstrap-responsive.css" rel="stylesheet">

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <%@ include file="google-analytics.jsp" %> 
  </head>

  <body>

    <%@ include file="header.jsp" %> 
<script>
    $('#header-translator').addClass("active");
</script>    

    <section class="round">
      <div class="span12 row-fluid">
	<div class="span8" style="text-align:center;margin-top:80px;margin-bottom:50px">
	  <form action="../provapi/documents/" method="post" enctype="multipart/form-data">
	    <style type="text/css">
	      .input-append .btn .dropdown-toggle {
	      float: none;
	      }
	    </style>
	    <div style="text-align:left;">
	      <table style="margin-left: auto; margin-right: auto;">
		<tr>
		  <td>Select a file:</td>
		  <td>
		    <input type="file"   name="file" class="primary" style="width:320px; "/>
		  </td>
		  <td> </td>
	          <td rowspan="3">
                    <input type="submit"   value="json"   name="translate"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="provx"    name="translate"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="provn"  name="translate"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="turtle" name="translate"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="trig"   name="translate"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="svg"    name="translate"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
		  </td>
		</tr>
		
		<tr>
		  <td>Enter a URL:</td>
		  <td>
		    
		    
		    <div class="input-append btn-group">
		      <input id="the-url-input" type="text" name="url" class="input-large" style="width:300px; "  />
		      
		      <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#">
			<span class="caret"></span>
		      </a>


				<ul class="dropdown-menu">
					<li>Examples</li>
					<li><a href="#" onclick="updateLocalURLInputBox('resources/prov/primer.provn')">
						<i class="icon-ok"></i> Primer</a>
					</li>
					<li><a href="#" onclick="updateLocalURLInputBox('resources/prov/pc1.provn')">
						<i class="icon-ok"></i> PC1</a>
					</li>
					<li><a href="#" onclick="updateLocalURLInputBox('resources/prov/invalid.provn')">
						<i class="icon-remove"></i> Invalid</a>
					</li>

					<li class="divider"></li>
					<li><a href="#" onclick="updateURLInputBox('')"><i class="icon-trash"></i> Clear</a></li>
				</ul>
			</div>
		  </td>
		</tr>
		<tr>
		  <td>Enter PROV statements:
		    <br>
		    <table>
		      <tr>
			<td><input type="radio" name="type" value="ttl" checked>ttl</input></td>
			<td><input type="radio" name="type" value="rdf">rdf</input></td>
		      </tr>    
		      <tr>
			<td><input type="radio" name="type" value="provn">provn</input></td>
			<td><input type="radio" name="type" value="provx">provx</input></td>
		      </tr>
		      <tr>
			<td><input type="radio" name="type" value="trig">trig</input></td>
			<td><input type="radio" name="type" value="json">json</input></td>
		      </tr>
		    </table>
		  </td>
		  <td>
                    <textarea rows="10" name="statements" style="width:350px; "></textarea>
		    
		    <!-- <input type="text" name="statements" class="input-large" style="width:350px;height:200px; "  />-->
		  </td>
		</tr>
		
	      </table>
	    </div>
	  </form>
	</div>
	
    <%@ include file="terms.jsp" %>       

    </section>

    <script src="../bootstrap/js/bootstrap-dropdown.js"></script>

    <%@ include file="footer.jsp" %> 

  </body>
</html>
