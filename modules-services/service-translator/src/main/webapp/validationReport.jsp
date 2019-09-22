<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <title>ProvValidator</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">

    <!-- Le styles -->
    <link href="../../../../bootstrap/css/bootstrap.css" rel="stylesheet">
    <link href="../../../../google-code-prettify/src/prettify.css" type="text/css" rel="stylesheet" />
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
      .accordion-heading .accordion-toggle {
        display: block;
        padding: 8px 15px;
      }
      .accordion-toggle {
        cursor: pointer;
      }
 
      a[class="nounderline"]:link {text-decoration:none;}    /* unvisited link */
      a[class="nounderline"]:visited {text-decoration:none;} /* visited link */
      a[class="nounderline"]:hover {text-decoration:none;}   /* mouse over link */
      a[class="nounderline"]:active {text-decoration:none;}  /* selected link */

     .popover-inner { width: 600px; }

      .glossary {
            font-style:    italic;
      }
    </style>

    <link href="../../../../bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
    <script src="../../../../jquery/jquery-1.7.2.min.js"></script>
    <script src="../../../../jquery/sarissa.js"></script>
    <script src="../../../../jquery/sarissa_ieemu_xpath.js"></script>
    <script src="../../../../jquery/jquery.xslTransform.js"></script>

    <!-- Le HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
      <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <%@ include file="google-analytics.jsp" %> 
  </head>

  <body>  <!--  onload="prettyPrint()" -->
<jsp:include page="header.jsp">
    <jsp:param name="adjust" value="../../../" />
</jsp:include>

<script>
    $('#header-validator').addClass("active");
</script>    




    <section>
      <div class="container" style="text-align:left;margin-top:20px">
	
	<p><em>No guarantees are provided as to the correctness of this validation result.</em></p>

      </div>

      <div id="report-to-insert" class="row">
      </div>






    <div id="overview-ordering-constraint" style="display:none; " >
      <p>Provenance consists of a description of past entities and activities. Valid provenance instances MUST satisfy ordering constraints between instantaneous <span class="glossary" data-ref="dfn-event">events.</span></p>
      <p>Specifically, there exists a preorder between instantaneous events. A constraint of the form 'e1 precedes e2' means that e1 happened at the same time as or before e2. For symmetry, follows is defined as the inverse of precedes; that is, a constraint of the form 'e1 follows e2' means that e1 happened at the same time as or after e2. Both relations are preorders, meaning that they are reflexive and transitive. Moreover, we sometimes consider strict forms of these orders: we say e1 strictly precedes e2 to indicate that e1 happened before e2, but not at the same time. This is a transitive relation.</p>
      <p>One way to check ordering constraints is to generate all precedes and strictly precedes relationships arising from the ordering constraints to form a directed graph, with edges marked precedes or strictly precedes, and check that there is no cycle containing a strictly precedes edge.</p>
    </div>

    <div id="overview-malformed-statements" style="display:none; ">
      Expressions are malformed  when ...
    </div>

    <div id="overview-failed-merge" style="display:none; ">
      Statements may be merged when ...
    </div>

    <div id="overview-type-check" style="display:none; ">
      PROV defines some type inferences and identifies some mutually exclusive types.  For instance, an activity cannot be an entity.  This section identifies type impossiblities.
    </div>
    <div id="overview-specialization-constraints" style="display:none; ">
      Specialization is not reflexive.
    </div>
    <div id="overview-duplicate-statements" style="display:none; ">
      This is purely a warning. The purpose of this section is to identify terms that are repeated in the current instance but successfully unified. No action is required, since such terms are perfect valid as far as [PROV-Constraints] is concerned.
    </div>

    <div id="overview-misleading-qnames" style="display:none; ">
      This is purely a warning. Qualified names, in PROV, map to URIs by concatenating the namespace (denoted by a qualified name prefix) and the local name. Two different qualified names may map to the same URI. Nothing wrong, from a PROV view point, since comparison is on URIs. However, having two distinct qualified names for a same URIs make provenance<em> confusing for human readers</em>.
    </div>

    </section>


    <!-- Le javascript
    ================================================== -->
    <!-- Placed at the end of the document so the pages load faster -->

    <script src="../../../../bootstrap/js/bootstrap-transition.js"></script>
    <script src="../../../../bootstrap/js/bootstrap-collapse.js"></script>
    <script src="../../../../bootstrap/js/bootstrap-tab.js"></script>
    <script src="../../../../bootstrap/js/bootstrap-tooltip.js"></script>
    <script src="../../../../bootstrap/js/bootstrap-popover.js"></script>
    <script src="../../../../google-code-prettify/src/prettify.js"></script>
    <script src="../../../../vkbeautify.0.98.01.beta.js"></script>


<script>
var thisUrl=document.URL;
var xmlUrl=thisUrl.replace('/view/','/provapi/');
xmlUrl=xmlUrl.replace('.html','.xml');
//alert(xmlUrl);

var viewPrefix=thisUrl.search('/view/');
var xslUrl=thisUrl.substr(0,viewPrefix);
xslUrl=xslUrl+"/xslt/validation.xsl";
console.log(xslUrl);

$('#report-to-insert').getTransform(
xslUrl,
//'../../../../xslt/validation.xsl', // path or xsl document in javascript variable
//'validationReport.xml',                          // path or xml document in javascript variable
xmlUrl,
{
  params: {                              // object for your own xsl parameters
    paramName1: 'paramValue1',
    paramName2: 'paramValue2'
  },
  xpath: '/',                   // trims your xml file to that defined by this xpath
  eval: true,                   // evaluates any <script> blocks it finds in the transformed result
  callback: function(){        // getTransform evaluates this function when transformation is complete
      console.log("Iam being called back");
        $('.withpopover').each (function (index) {
                                 var ref=$(this).attr('data-div');
                                 $(this).attr('data-content',$('#' + ref).html());
             })
      console.log("Iam being called back again");
	$('.withpopover').popover();

      $('.showHideTab_IGNORE').click( function () {
                                 var target=$(this).attr('href');
                                 var state=$(target).attr('data-state');
                                 if (state==null || state=='') { 
                                   $(target).attr('data-state','open');
                                   $(target).show();
                                 } else {
                                   $(target).attr('data-state','');
                                   $(target).hide();
                                 }
                                 console.log( 'showHideTab' ); 
                             });

    var myUrl=document.URL;
    var suffix="/validation/report.html";
    var callback=myUrl.replace(suffix,"/callback");
    $('#link-deposit').attr('href',myUrl.replace(suffix,"/deposit?callback="+callback));
  }
});

</script>

<style type="text/css">
    .popover-inner { width: 600px; }
</style>

<jsp:include page="footer.jsp">
    <jsp:param name="adjust" value="../../../" />
</jsp:include>



  </body>
</html>
