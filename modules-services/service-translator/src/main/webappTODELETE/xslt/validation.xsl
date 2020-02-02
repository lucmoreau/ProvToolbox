<?xml version="1.0" encoding="UTF-8"?>
<!-- ***** BEGIN LICENSE BLOCK *****
   - Version: MPL 1.1/GPL 2.0/LGPL 2.1
   -
   - The contents of this file are subject to the Mozilla Public License Version
   - 1.1 (the "License"); you may not use this file except in compliance with
   - the License. You may obtain a copy of the License at
   - http://www.mozilla.org/MPL/
   -
   - Software distributed under the License is distributed on an "AS IS" basis,
   - WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
   - for the specific language governing rights and limitations under the
   - License.
   -
   - The Original Code is mozilla.org code.
   -
   - The Initial Developer of the Original Code is
   - Netscape Communications Corporation.
   - Portions created by the Initial Developer are Copyright (C) 2002
   - the Initial Developer. All Rights Reserved.
   -
   - Contributor(s):
   -   Jonas Sicking <sicking@bigfoot.com> (Original author)
   -
   - Alternatively, the contents of this file may be used under the terms of
   - either the GNU General Public License Version 2 or later (the "GPL"), or
   - the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
   - in which case the provisions of the GPL or the LGPL are applicable instead
   - of those above. If you wish to allow use of your version of this file only
   - under the terms of either the GPL or the LGPL, and not to allow others to
   - use your version of this file under the terms of the MPL, indicate your
   - decision by deleting the provisions above and replace them with the notice
   - and other provisions required by the LGPL or the GPL. If you do not delete
   - the provisions above, a recipient may use your version of this file under
   - the terms of any one of the MPL, the GPL or the LGPL.
   -
   - ***** END LICENSE BLOCK ***** -->
<!-- <!DOCTYPE overlay SYSTEM "chrome://global/locale/xml/prettyprint.dtd"> -->
<xsl:stylesheet version="1.0" 
       xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
       xmlns="http://www.w3.org/1999/xhtml"
       xmlns:prov="http://www.w3.org/ns/prov#"
       xmlns:val="http://openprovenance.org/validation#" >

  <xsl:output method="html" encoding="UTF-8"/>

  <xsl:template match="/">
    <head>
      <!-- <base href="http://localhost:8080/prov-vservice/"/> -->




<!--      <link href="../src/main/webapp/google-code-prettify/src/prettify.css" type="text/css" rel="stylesheet" />
-->

      <link href="../../../../bootstrap/css/bootstrap.css" rel="stylesheet"/>
      <link href="../../../../css/XMLPrettyPrint/XMLPrettyPrint.css" type="text/css" rel="stylesheet"/>
      <link href="../../../../css/validation.css" type="text/css" rel="stylesheet"/>

      <link title="Monospace" href="../../../../css/XMLPrettyPrint/XMLMonoPrint.css" type="text/css" rel="alternate stylesheet"/>

      <script>
      <![CDATA[
function toggle(ignore) {
        console.log("toggle called on " + ignore);
        try {
          //var par = event.originalTarget;
	  var par = event.target;
	  console.log("toggle found (1) " + par);
	  console.log("toggle found (2) " + par.nodeName);
	  console.log("toggle found (2) " + par.className);

          if (par.nodeName == 'DIV' && par.className == 'expander') {
    	    console.log("toggle found (3) ");
            if (par.parentNode.className == 'expander-closed') {
              console.log("toggle found (4) ");
              par.parentNode.className = 'expander-open';
              event.target.firstChild.data = '\u2212';
            }
            else {
              console.log("toggle found (5) ");
              par.parentNode.className = 'expander-closed';
              event.target.firstChild.data = '+';
            }
          } else {
    	    console.log("toggle found (3.5) ");
          }
        } catch (e) {
          console.log("toggle exception " + e);
        }
}
      ]]>
      </script>

<!-- success icon: http://www.iconspedia.com/icon/agt-action-success-3-88.html
     failure icon: http://www.iconspedia.com/icon/button-cancel-4034.html
  
     all LGL 
   http://www.iconspedia.com/icon/warning-11601.html  cc attribution share alike
-->


    </head>
    <body id="top"> <!--  onload="prettyPrint()" -->
      <title>Validation Report</title>
      <xsl:call-template name="summary">
	<xsl:with-param name="report" select="/val:validationReport" />
      </xsl:call-template>

      <div class="span12" style="margin-bottom:50px">
	<div class="row-fluid" style="margin-top:30px">
	  <xsl:apply-templates>
	    <xsl:with-param name="is-top" select="'true'" />
	  </xsl:apply-templates>
	</div>
      </div>
    </body>
  </xsl:template>

  <xsl:template name="summary">


    <div class="span12" style="margin-bottom:50px; margin-left:20px">

      <div class="row-fluid" style="margin-top:30px">

	    <xsl:variable name="countCycle">
	      <xsl:value-of select="count(/val:validationReport/val:cycle)"/>	      
	    </xsl:variable>
	    <xsl:variable name="countFailedMerge">
	      <xsl:value-of select="count(/val:validationReport/val:failedMerge)"/>	      
	    </xsl:variable>
	    <xsl:variable name="countMalformedExpressions">
	      <xsl:value-of select="count(/val:validationReport/val:malformedStatements)"/>	      
	    </xsl:variable>
	    <xsl:variable name="countTypeChecking">
	      <xsl:value-of select="count(/val:validationReport/val:typeOverlap)"/>	      
	    </xsl:variable>
	    <xsl:variable name="countSpecializationConstraints">
	      <xsl:value-of select="count(/val:validationReport/val:specializationReport)"/>	      
	    </xsl:variable>
	    <xsl:variable name="countSuccessfulMerge">
	      <xsl:value-of select="count(/val:validationReport/val:successfulMerge)"/>	      
	    </xsl:variable>
	    <xsl:variable name="countQualifiedNameMismatch">
	      <xsl:value-of select="count(/val:validationReport/val:qualifiedNameMismatch)"/>	      
	    </xsl:variable>


	    
	  <li class="span4" >

	    <div class="accordion" id="accordion1">

	      <xsl:call-template name="summary-alert-box">
		<xsl:with-param name="count"  select="$countCycle"/>
		<xsl:with-param name="label"  select="'Event ordering constraints '"/>
		<xsl:with-param name="myid"   select="'event-ordering'"/>
		<xsl:with-param name="mytab"  select="'#tab-ordering-constraints'"/>
	      </xsl:call-template>

	      <xsl:call-template name="summary-alert-box">
		<xsl:with-param name="count"  select="$countFailedMerge"/>
		<xsl:with-param name="label"  select="'Merge constraints '"/>
		<xsl:with-param name="myid"   select="'show-report-failedMerge'"/>
		<xsl:with-param name="mytab"  select="'#tab-failed-merge'"/>
	      </xsl:call-template>


	      <xsl:call-template name="summary-alert-box">
		<xsl:with-param name="count"  select="$countTypeChecking"/>
		<xsl:with-param name="label"  select="'Type Constraints '"/>
		<xsl:with-param name="myid"   select="'show-report-typeChecking'"/>
		<xsl:with-param name="mytab"  select="'#tab-type-check'"/>
	      </xsl:call-template>

	      <xsl:call-template name="summary-alert-box">
		<xsl:with-param name="count"  select="$countMalformedExpressions"/>
		<xsl:with-param name="label"  select="'Malformed Expressions '"/>
		<xsl:with-param name="myid"   select="'show-report-malformedExpressions'"/>
		<xsl:with-param name="mytab"  select="'#tab-malformed-expression'"/>
	      </xsl:call-template>


	    </div>
	  </li>

	  <li class="span4" >

	    <div class="accordion" id="accordion2">

	      <xsl:call-template name="summary-alert-box">
		<xsl:with-param name="count"  select="$countSpecializationConstraints"/>
		<xsl:with-param name="label"  select="'Specialization Constraints '"/>
		<xsl:with-param name="myid"   select="'show-report-specializationConstraints'"/>
		<xsl:with-param name="mytab"  select="'#tab-specialization-constraints'"/>
	      </xsl:call-template>


	      <xsl:call-template name="summary-alert-box">
		<xsl:with-param name="count"  select="$countSuccessfulMerge"/>
		<xsl:with-param name="label"  select="'Duplicate Statements '"/>
		<xsl:with-param name="myid"   select="'show-report-successfulMerge'"/>
		<xsl:with-param name="mytab"  select="'#tab-term-duplication'"/>
		<xsl:with-param name="kind"   select="'warning'"/>
	      </xsl:call-template>

	      <xsl:call-template name="summary-alert-box">
		<xsl:with-param name="count"  select="$countQualifiedNameMismatch"/>
		<xsl:with-param name="label"  select="'Misleading Qualified Names '"/>
		<xsl:with-param name="myid"   select="'show-report-qualifiedNameMismatch'"/>
		<xsl:with-param name="mytab"  select="'#tab-qualified-name'"/>
		<xsl:with-param name="kind"   select="'warning'"/>
	      </xsl:call-template>

	    </div>
	  </li>
      </div>
    </div>

  </xsl:template>

  <xsl:template match="/val:validationReport">
     <xsl:message>Entering Validation Report</xsl:message>

     <!--
     <ul class="nav nav-tabs">
       <li> <a href="#tab-ordering-constraints" data-toggle="tab">Event Ordering</a></li>
       <li> <a href="#tab-failed-merge" data-toggle="tab">Merge</a></li>
       <li> <a href="#tab-type-check" data-toggle="tab">Type</a></li>
       <li> <a href="#tab-malformed-expression" data-toggle="tab">Malformed</a></li>
       <li> <a href="#tab-specialization-constraints" data-toggle="tab">Specialization</a></li>
       <li> <a href="#tab-term-duplication" data-toggle="tab">Duplicates</a></li>
       <li> <a href="#tab-qualified-name" data-toggle="tab">Qualified Names</a></li>
     </ul>
     -->
     
      <div class="row" style="margin-bottom:50px">
	<div class="row-fluid" style="margin-top:30px">

	  <li class="span8" >
     
	    <div id="myTabContent-report" class="tab-content">
	      
	      <xsl:call-template name="report-on-one-issue">
		<xsl:with-param name="label"         select="'Ordering Constraints'"/>
		<xsl:with-param name="myid"          select="'tab-ordering-constraints'"/>
		<xsl:with-param name="xpath"         select="/val:validationReport/val:cycle"/>
		<xsl:with-param name="intro_blob"    select="'overview-ordering-constraint'"/>
		<xsl:with-param name="error_label"   select="'Cycles have been detected!'"/>
		<xsl:with-param name="success_label" select="'No ordering constraint was violated.'"/>
	      </xsl:call-template>
	      
	      <xsl:call-template name="report-on-one-issue">
		<xsl:with-param name="label"         select="'Merge Constraints'"/>
		<xsl:with-param name="myid"          select="'tab-failed-merge'"/>
		<xsl:with-param name="xpath"         select="/val:validationReport/val:failedMerge"/>
		<xsl:with-param name="intro_blob"    select="'overview-failed-merge'"/>
		<xsl:with-param name="error_label"   select="'Statements failed to be merged!'"/>
		<xsl:with-param name="success_label" select="'There is no merge failure'"/>
	      </xsl:call-template>
	      
	      <xsl:call-template name="report-on-one-issue">
		<xsl:with-param name="label"         select="'Type Constraints'"/>
		<xsl:with-param name="myid"          select="'tab-type-check'"/>
		<xsl:with-param name="xpath"         select="/val:validationReport/val:typeOverlap"/>
		<xsl:with-param name="intro_blob"    select="'overview-type-check'"/>
		<xsl:with-param name="error_label"   select="'There is some type checking error.'"/>
		<xsl:with-param name="success_label" select="'There is no type checking error.'"/>
	      </xsl:call-template>
	      
	      <xsl:call-template name="report-on-one-issue">
		<xsl:with-param name="label"         select="'Malformed Expressions'"/>
		<xsl:with-param name="myid"          select="'tab-malformed-expression'"/>
		<xsl:with-param name="xpath"         select="/val:validationReport/val:malformedStatements"/>
		<xsl:with-param name="intro_blob"    select="'overview-malformed-statements'"/>
		<xsl:with-param name="error_label"   select="'Some statements are malformed!'"/>
		<xsl:with-param name="success_label" select="'There is no malformed statement.'"/>
	      </xsl:call-template>
	      
	      <xsl:call-template name="report-on-one-issue">
		<xsl:with-param name="label"         select="'Specialization Constraints'"/>
		<xsl:with-param name="myid"          select="'tab-specialization-constraints'"/>
		<xsl:with-param name="xpath"         select="/val:validationReport/val:specializationReport"/>
		<xsl:with-param name="intro_blob"    select="'overview-specialization-constraints'"/>
		<xsl:with-param name="error_label"   select="'Specialization was detected to be reflexive.'"/>
		<xsl:with-param name="success_label" select="'There is no violation of specialization irreflexivity.'"/>
	      </xsl:call-template>
	      
	      <xsl:call-template name="report-on-one-issue">
		<xsl:with-param name="label"         select="'Duplicate Statements'"/>
		<xsl:with-param name="myid"          select="'tab-term-duplication'"/>
		<xsl:with-param name="xpath"         select="/val:validationReport/val:successfulMerge"/>
		<xsl:with-param name="intro_blob"    select="'overview-duplicate-statements'"/>
		<xsl:with-param name="error_label"   select="'Some terms are duplicated.'"/>
		<xsl:with-param name="success_label" select="'There is no duplicated term.'"/>
		<xsl:with-param name="kind"   select="'warning'"/>
	      </xsl:call-template>

	      <xsl:call-template name="report-on-one-issue">
		<xsl:with-param name="label"         select="'Misleading Qualified Name'"/>
		<xsl:with-param name="myid"          select="'tab-qualified-name'"/>
		<xsl:with-param name="xpath"         select="/val:validationReport/val:qualifiedNameMismatch"/>
		<xsl:with-param name="intro_blob"    select="'overview-misleading-qnames'"/>
		<xsl:with-param name="error_label"   select="'Some distinct qualified names map to the same URI.'"/>
		<xsl:with-param name="success_label" select="'There is no mismatching qualified name.'"/>
		<xsl:with-param name="kind"   select="'warning'"/>
	      </xsl:call-template>

	    </div>
	  </li>

	  <li class="span4" >
	    <div id="spec-to-go-here">
	      <a href="http://www.w3.org/TR/prov-constraints/" onclick="window.open('http://www.w3.org/TR/prov-constraints/','popup','width=600,height=700,scrollbars=yes,resizable=yes,toolbar=no,directories=no,location=yes,menubar=no,status=no,left=0,top=0'); return false">PROV-Constraints</a><br/>
	      <a href="http://www.w3.org/TR/prov-constraints/#communication-generation-use-inference" onclick="window.open('http://www.w3.org/TR/prov-constraints/#communication-generation-use-inference','popup','width=600,height=700,scrollbars=yes,resizable=yes,toolbar=no,directories=no,location=yes,menubar=no,status=no,left=0,top=0'); return false">communication-generation-use-inference</a>
	    </div>
	  </li>
	</div>
      </div>
	

   </xsl:template>

  <xsl:template match="/val:validationReport/val:malformedStatements">
     <xsl:message>Entering Malformed Expressions</xsl:message>
     <div class="iterated-intro"><xsl:text>The following expressions are malformed and have been removed from the instance to validate it.</xsl:text></div>
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/val:validationReport/val:failedMerge">
     <xsl:message>Entering Failed Unification</xsl:message>
     <h4>2.<xsl:number value="position()" format="1" /><xsl:text> Failed Unification</xsl:text></h4>
     <div class="iterated-intro"><xsl:text>The following terms fail to unify.</xsl:text></div>
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/val:validationReport/val:cycle">
     <xsl:message>Entering cycle</xsl:message>
     <h4>1.<xsl:number value="position()" format="1" /><xsl:text> Cycle</xsl:text></h4>
     <div class="iterated-intro"><xsl:text>The following sequence of events of events forms cycle.</xsl:text></div>
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/val:validationReport/val:typeOverlap">
     <xsl:message>Entering Type Overlap Merge</xsl:message>
     <h4>3.<xsl:number value="position()" format="1" /><xsl:text> Type Impossiblity</xsl:text></h4>
     <div class="iterated-intro"><xsl:text>The following term has types (asserted or inferred) that are not compatible.</xsl:text></div>
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/val:validationReport/val:specializationReport">
     <xsl:message>Entering Specialization Impossibility Constraints</xsl:message>
     <h4>5.<xsl:number value="position()" format="1" /><xsl:text> Specialization Constraint</xsl:text></h4>
     <p class="overview"><xsl:text>Specialization has been inferred to be reflexive for the following entity.</xsl:text></p>
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/val:validationReport/val:successfulMerge">
     <xsl:message>Entering successful Merge</xsl:message>
     <h4>6.<xsl:number value="position()" format="1" /><xsl:text> Duplicate Terms</xsl:text></h4>
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/val:validationReport/val:qualifiedNameMismatch">
     <xsl:message>Entering Qualified Name Mismatch Merge</xsl:message>
     <h4>7.<xsl:number value="position()" format="1" /><xsl:text> QualifiedName Mismatch</xsl:text></h4>
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/val:validationReport/prov:document">
    <div id="display-provenance-rollover" >
      <p>Showing provenance</p>
      <xsl:apply-templates/>
    </div>
  </xsl:template>



  <!--   ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                                  Report on one Issue

         ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++      -->


  <xsl:template name="summary-alert-box">
    <xsl:param name="count"/>
    <xsl:param name="label"/>
    <xsl:param name="myid"/>
    <xsl:param name="mytab"/>
    <xsl:param name="kind"/>

    <xsl:choose>

      <xsl:when test="$count &gt; 0">
	<a href="{$mytab}" data-toggle="tab" class="showHideTab">

	  <xsl:choose>
	    <xsl:when test="$kind = 'warning'">
	      <div class="accordion-group alert" id="event-ordering">
		<i class="icon-warning-sign"/><span><xsl:text> </xsl:text><xsl:value-of select="$label"/> (<xsl:value-of select="$count"/>)</span>
		<div class="accordion-body collapse" id="{$myid}"></div>
	      </div>
	    </xsl:when>

	    <xsl:otherwise>
	      <div class="accordion-group alert alert-error" id="event-ordering">
		<i class="icon-remove"/><span><xsl:text> </xsl:text><xsl:value-of select="$label"/> (<xsl:value-of select="$count"/>)</span>
		<div class="accordion-body collapse" id="{$myid}"></div>
	      </div>
	    </xsl:otherwise>
	  </xsl:choose>
	</a>
      </xsl:when>
      
      <xsl:otherwise>
	<a href="{$mytab}" data-toggle="tab">
	  <div class="accordion-group alert alert-success" id="event-ordering">
	    <i class="icon-ok"/><span><xsl:text> </xsl:text><xsl:value-of select="$label"/></span>
	    <div class="accordion-body collapse" id="{$myid}"></div>
	  </div>
	</a>
      </xsl:otherwise>

    </xsl:choose>
  </xsl:template>

  <!--     ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

                                  Report on one Issue

           ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++  -->

  <xsl:template name="report-on-one-issue">
    <xsl:param name="label"/>
    <xsl:param name="myid"/>
    <xsl:param name="xpath"/>
    <xsl:param name="intro_blob"/>
    <xsl:param name="error_label"/>
    <xsl:param name="success_label"/>
    <xsl:param name="kind"/>

    <div class="tab-pane fade" id="{$myid}">	
      <div>
	<span style="font-size:large; font-weight: bold;"><xsl:value-of select="$label"/></span>
	<xsl:text> </xsl:text>
	<a rel="popover" class="withpopover" href="#" html="true" title="{$label}" data-div="{$intro_blob}"><xsl:text>What is this?</xsl:text></a>
      </div>
      

      <xsl:choose>
	<xsl:when test="$xpath">
	  <xsl:choose>
	    <xsl:when test="$kind = 'warning'">
	      <div style="margin: 1.5em 10em 1.5em 5em;">
		<div class="alert"><img src="../../../../images/Warning_32.png"></img><xsl:text> </xsl:text><xsl:value-of select="$error_label"/></div>
	      </div>
	    </xsl:when>
	    <xsl:otherwise>
	      <div style="margin: 1.5em 10em 1.5em 5em;">
		<div class="alert alert-error"><img src="../../../../images/button_cancel-32.png"></img><xsl:text> </xsl:text><xsl:value-of select="$error_label"/></div>
	      </div>
	    </xsl:otherwise>
	  </xsl:choose>
	  <xsl:apply-templates select="$xpath"/>
	</xsl:when>
	<xsl:otherwise>
	  <div style="margin: 1.5em 10em 1.5em 5em;">
	    <div class="alert alert-success"><img src="../../../../images/agt_action_success-32.png"/><xsl:text> </xsl:text><xsl:value-of select="$success_label"/></div>
	  </div>
	</xsl:otherwise>
      </xsl:choose>
      </div>


  </xsl:template>
  
<!--
  <xsl:template name="provn" match="//prov:wasStartedBy">
    <div>
      <xsl:text>wasStartedBy(</xsl:text>
      <xsl:apply-templates select="prov:activity/@prov:ref"/>
      <xsl:text>,</xsl:text>
      <xsl:apply-templates select="prov:trigger/@prov:ref"/>
      <xsl:text>,</xsl:text>
      <xsl:apply-templates select="prov:starter/@prov:ref"/>
      <xsl:text>,</xsl:text>
      <xsl:apply-templates select="prov:time"/>
      <xsl:text>)</xsl:text>
    </div>
  </xsl:template>
-->

  <xsl:template match="*">
    <div>
      <!-- <xsl:text>luc1</xsl:text> -->
      <xsl:text>&lt;</xsl:text>
      <span class="start-tag"><xsl:value-of select="name(.)"/></span>
      <xsl:apply-templates select="@*"/>
      <xsl:text>/&gt;</xsl:text>
    </div>
  </xsl:template>

  <xsl:template match="*[node()]">
    <div>
      <!-- <xsl:text>luc2</xsl:text> -->
      <xsl:text>&lt;</xsl:text>
      <span class="start-tag"><xsl:value-of select="name(.)"/></span>
      <xsl:apply-templates select="@*"/>
      <xsl:text>&gt;</xsl:text>

      <span class="text">
        <xsl:value-of select="."/>
      </span>

      <xsl:text>&lt;/</xsl:text>
      <span class="end-tag"><xsl:value-of select="name(.)"/></span>
      <xsl:text>&gt;</xsl:text>
    </div>
  </xsl:template>

  <xsl:template match="*[* or processing-instruction() or comment() or string-length(.) &gt; 80]">
    <xsl:param name="is-top" select="'false'" /> <!-- true if this is the top of the tree being serialized -->


    <div class="expander-open">
      <xsl:if test="$is-top = 'true'">
          <h2><xsl:value-of select="local-name(.)"/></h2>
      </xsl:if>

      <!-- <xsl:text>luc3</xsl:text> -->
      <xsl:call-template name="expander"/>

      <xsl:text>&lt;</xsl:text>
      <span class="start-tag"><xsl:value-of select="name(.)"/></span>
      <xsl:apply-templates select="@*"/>
      <xsl:text>&gt;</xsl:text>

      <div class="expander-content">
        <xsl:apply-templates>
          <xsl:with-param name="is-top" select="'false'" />
        </xsl:apply-templates>
      </div>

      <xsl:text>&lt;/</xsl:text>
      <span class="end-tag"><xsl:value-of select="name(.)"/></span>
      <xsl:text>&gt;</xsl:text>
    </div>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:text> </xsl:text>
    <span class="attribute-name"><xsl:value-of select="name(.)"/></span>
    <xsl:text>=</xsl:text>
    <span class="attribute-value">"<xsl:value-of select="."/>"</span>
  </xsl:template>

  <xsl:template match="text()">
    <xsl:if test="normalize-space(.)">
      <xsl:value-of select="."/>
    </xsl:if>
  </xsl:template>

  <xsl:template match="processing-instruction()">
<!--  remove it all!
    <div class="pi">
      <xsl:text>&lt;?</xsl:text>
      <xsl:value-of select="name(.)"/>
      <xsl:text> </xsl:text>
      <xsl:value-of select="."/>
      <xsl:text>?&gt;</xsl:text>
    </div>
-->
  </xsl:template>

<!--
  <xsl:template match="processing-instruction()[string-length(.) &gt; 50]">
    <div class="expander-open">
      <xsl:call-template name="expander"/>

      <span class="pi">
        <xsl:text> &lt;?</xsl:text>
        <xsl:value-of select="name(.)"/>
      </span>
      <div class="expander-content pi"><xsl:value-of select="."/></div>
      <span class="pi">
        <xsl:text>?&gt;</xsl:text>
      </span>
    </div>
  </xsl:template>
-->

  <xsl:template match="comment()">
    <div class="comment">
      <xsl:text>&lt;!--</xsl:text>
      <xsl:value-of select="."/>
      <xsl:text>--&gt;</xsl:text>
    </div>
  </xsl:template>

  <xsl:template match="comment()[string-length(.) &gt; 50]">
    <div class="expander-open">
      <xsl:call-template name="expander"/>

      <span class="comment">
        <xsl:text>&lt;!--</xsl:text>
      </span>
      <div class="expander-content comment">
        <xsl:value-of select="."/>
      </div>
      <span class="comment">
        <xsl:text>--&gt;</xsl:text>
      </span> 
    </div>
  </xsl:template>
  
  <xsl:template name="expander">
    <div class="expander" onclick="toggle(this)">−</div>
  </xsl:template>

</xsl:stylesheet>
