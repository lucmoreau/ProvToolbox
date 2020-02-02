<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>ProvExpander</title>
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
    $('#header-expander').addClass("active");
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
		  <td> </td>
		  <td> </td>
	          <td rowspan="3">
                    <input type="submit"   value="json"   name="expand"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="provx"  name="expand"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="provn"  name="expand"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="turtle" name="expand"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="trig"   name="expand"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
                    <input type="submit"   value="svg"    name="expand"  class="btn btn-primary btn-large" style="text-align:center; width:80px; "/><br>
		  </td>
		</tr>
		
		<tr>
		  <td>Template</td>
		  <td>
		    <div class="input-append btn-group">
		      <input id="the-url-input" type="text" name="url" class="input-large" style="width:300px; "  />

		      <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#">
			<span class="caret"></span>
		      </a> 
		      
		      
		      <ul class="dropdown-menu">
			<li>Templates</li>
			<li><a href="#" onclick="updateURLInputBox('https://openprovenance.org/templates/org/openprovenance/generic/binaryop/1.provn')">
			    <i class="icon-ok"></i>  generic.binaryop</a>
			</li>
			
			<li class="divider"></li>
			<li><a href="#" onclick="updateURLInputBox('')"><i class="icon-trash"></i> Clear</a></li>
		      </ul>

		    </div>
		  </td>
		</tr>
		<tr>
		  <td>Bindings</td>
		  <td>
                    <textarea id="the-bindings-input" rows="10" name="statements" placehoder="Set of bindings in json format" style="width:350px; "></textarea>
		  </td>
		</tr>
		
		<tr>
		  <td>Examples</td>
		  <td>
		    <div class="input-append btn-group">

		      <a class="btn btn-primary dropdown-toggle" data-toggle="dropdown" href="#">
			<span class="caret"></span>
		      </a> 
		      
		      
		      <ul class="dropdown-menu">
			<li>Bindings Examples</li>
			<li><a href="#" onclick="updateBindingsBox('https://openprovenance.org/templates/org/openprovenance/generic/binaryop/1/b1.json')">
			    <i class="icon-ok"></i>  generic.binaryop.b1 (1 output)</a>
			</li>
			<li><a href="#" onclick="updateBindingsBox('https://openprovenance.org/templates/org/openprovenance/generic/binaryop/1/b2.json')">
			    <i class="icon-ok"></i>  generic.binaryop.b2 (2 outputs)</a>
			</li>
			<li><a href="#" onclick="updateBindingsBox('https://openprovenance.org/templates/org/openprovenance/generic/binaryop/1/b3.json')">
			    <i class="icon-ok"></i>  generic.binaryop.b3 (1 output, 2 properties)</a>
			</li>
			<li><a href="#" onclick="updateBindingsBox('https://openprovenance.org/templates/org/openprovenance/generic/binaryop/1/b4.json')">
			    <i class="icon-ok"></i>  generic.binaryop.b4 (2 outputs, 1 or 2 properties)</a>
			</li>
			
			<li class="divider"></li>
			<li><a href="#" onclick="updateBindingsBox('')"><i class="icon-trash"></i> Clear</a></li>
		      </ul>

		    </div>
		  </td>
		</tr>

		<tr>
		  <td></td>
		  <td><p/><p/><p/><p/><p/><p/><p/><p/>For details about the templating approach, please see: "<em>Luc Moreau, Belfrit Victor Batlajery, Trung Dong Huynh, Danius Michaelides, and Heather Packer. A templating system to generate provenance. IEEE Transactions on Software Engineering, April 2017</em>" (<a href="http://dx.doi.org/10.1109/TSE.2017.2659745">doi:10.1109/TSE.2017.2659745</a>) available in open access.</td>
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
