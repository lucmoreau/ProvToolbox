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
     all LGL -->


    </head>
    <body id="top"> <!--  onload="prettyPrint()" -->
      <xsl:apply-templates>
	<xsl:with-param name="is-top" select="'true'" />
	<title>Validation Report</title>
      </xsl:apply-templates>
    </body>
  </xsl:template>

  <xsl:template match="/val:validationReport">
     <xsl:message>Entering Validation Report</xsl:message>
     <h2><xsl:text>Validation Report</xsl:text></h2>

     <h4><xsl:text>1. Statement Unification</xsl:text></h4>
     <p class="overview"><xsl:text>Statements may be unified when ...</xsl:text></p>
     <xsl:choose>
       <xsl:when test="/val:validationReport/val:failedMerge">
	 <xsl:apply-templates select="/val:validationReport/val:failedMerge"/>
       </xsl:when>
       <xsl:otherwise>
	 <div class="alert alert-success"><img src="../../../images/agt_action_success-32.png"/><xsl:text> There is no unification failure.</xsl:text></div>
       </xsl:otherwise>
     </xsl:choose>


     <h2><xsl:text>2. Ordering Constraints</xsl:text></h2>
     <p class="overview"><xsl:text>PROV statements imply ordering constraints...</xsl:text></p>

     <xsl:choose>
       <xsl:when test="/val:validationReport/val:cycle">
	 <div class="alert alert-error"><img src="../../../images/button_cancel-32.png"></img><xsl:text> Cycles have been detected!</xsl:text></div>
	 <xsl:apply-templates select="/val:validationReport/val:cycle"/>
       </xsl:when>
       <xsl:otherwise>
	 <div class="alert alert-success"><img src="../../../images/agt_action_success-32.png"/><xsl:text>No ordering constraint was violated.</xsl:text></div>
       </xsl:otherwise>
     </xsl:choose>


     <h2><xsl:text>3. Type Checking</xsl:text></h2>
     <p class="overview"><xsl:text>PROV defines some type inferences and identifies some mutually exclusive types.  For instance, an activity cannot be an entity.  This section identifies type impossiblities.</xsl:text></p>
     <xsl:choose>
       <xsl:when test="/val:validationReport/val:typeOverlap">
	 <div class="alert alert-error"><img src="../../../images/button_cancel-32.png"></img><xsl:text>There is some type checking error.</xsl:text></div>
	 <xsl:apply-templates select="/val:validationReport/val:typeOverlap"/>
       </xsl:when>
       <xsl:otherwise>
	 <div class="alert alert-success"><img src="../../../images/agt_action_success-32.png"/><xsl:text>There is no type checking error.</xsl:text></div>
       </xsl:otherwise>
     </xsl:choose>

     
     <h2><xsl:text>4. Specialization Constraints</xsl:text></h2>
     <p class="overview"><xsl:text>Specialization is not reflexive.</xsl:text></p>
     <xsl:choose>
       <xsl:when test="/val:validationReport/val:specializationReport">
	 <div class="alert alert-error"><img src="../../../images/button_cancel-32.png"></img><xsl:text>Specialization was detected to be reflexive.</xsl:text></div>
	 <xsl:apply-templates select="/val:validationReport/val:specializationReport"/>
       </xsl:when>
       <xsl:otherwise>
	 <div class="alert alert-success"><img src="../../../images/agt_action_success-32.png"/><xsl:text>There is no violation of specialization irreflexivity.</xsl:text></div>
       </xsl:otherwise>
     </xsl:choose>

     <h2><xsl:text>5. Warning: Term Duplication</xsl:text></h2>
     <p class="overview"><xsl:text>This is purely a warning. The purpose of this section is to identify terms that are repeated in the current instance but successfully unified. No action is required, since such terms are perfect valid as far as [PROV-Constraints] is concerned. </xsl:text></p>
     <xsl:choose>
       <xsl:when test="/val:validationReport/val:successfulMerge">
	 <div class="alert"><xsl:text>Some terms are duplicated.</xsl:text></div>
	 <xsl:apply-templates select="/val:validationReport/val:successfulMerge"/>
       </xsl:when>
       <xsl:otherwise>
	 <div class="alert alert-success"><img src="../../../images/agt_action_success-32.png"/><xsl:text>There is no duplicated term.</xsl:text></div>
       </xsl:otherwise>
     </xsl:choose>


     
     <h2><xsl:text>6. Warning: Qualified Name Mismatch</xsl:text></h2>
     <div class="overview"><xsl:text>This is purely a warning. Qualified names, in PROV, map to URIs by concatenating the namespace (denoted by a qualified name prefix) and the local name. Two different qualified names may map to the same URI. Nothing wrong, from a PROV view point, since comparison is on URIs. However, having two distinct qualified names for a same URIs make provenance</xsl:text><em><xsl:text> confusing for human readers</xsl:text></em><xsl:text>.</xsl:text></div>
     <xsl:choose>
       <xsl:when test="/val:validationReport/val:qualifiedNameMismatch">
	 <div class="alert alert-error"><img src="../../../images/button_cancel-32.png"></img><xsl:text>Some distinct qualified names map to the same URI.</xsl:text></div>
	 <xsl:apply-templates select="/val:validationReport/val:qualifiedNameMismatch"/>
       </xsl:when>
       <xsl:otherwise>
	 <div class="alert alert-success"><img src="../../../images/agt_action_success-32.png"/><xsl:text>There is no mismatching qualified name.</xsl:text></div>
       </xsl:otherwise>
     </xsl:choose>
   </xsl:template>

  <xsl:template match="/val:validationReport/val:cycle">
     <xsl:message>Entering cycle</xsl:message>
     <h4>2.<xsl:number value="position()" format="1" /><xsl:text> Cycle</xsl:text></h4>
     <p class="iterated-intro
"><xsl:text>The following sequence of events of events forms cycle.</xsl:text></p>
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/val:validationReport/val:failedMerge">
     <xsl:message>Entering Failed Unification</xsl:message>
     <h4>1.<xsl:number value="position()" format="1" /><xsl:text> Failed Unification</xsl:text></h4>
     <p class="iterated-intro"><xsl:text>The following terms fail to unify.</xsl:text></p>
     <xsl:apply-templates/>

  </xsl:template>

  <xsl:template match="/val:validationReport/val:successfulMerge">
     <xsl:message>Entering successful Merge</xsl:message>
     <h4>5.<xsl:number value="position()" format="1" /><xsl:text> Duplicate Terms</xsl:text></h4>
     <xsl:apply-templates/>
  </xsl:template>
  

  <xsl:template match="/val:validationReport/val:typeOverlap">
     <xsl:message>Entering Type Overlap Merge</xsl:message>
     <h4>3.<xsl:number value="position()" format="1" /><xsl:text> Type Impossiblity</xsl:text></h4>
     <p class="iterated-intro"><xsl:text>The following term has types (asserted or inferred) that are not compatible.</xsl:text></p>
     <xsl:apply-templates/>
  </xsl:template>

  <xsl:template match="/val:validationReport/val:qualifiedNameMismatch">
     <xsl:message>Entering Qualified Name Mismatch Merge</xsl:message>
     <h4>6.<xsl:number value="position()" format="1" /><xsl:text> QualifiedName Mismatch</xsl:text></h4>
     <xsl:apply-templates/>
  </xsl:template>
  
  
  <xsl:template match="/val:validationReport/val:specializationReport">
     <xsl:message>Entering Specialization Impossibility Constraints</xsl:message>
     <h4>4.<xsl:number value="position()" format="1" /><xsl:text> Specialization Constraint</xsl:text></h4>
     <p class="overview"><xsl:text>Specialization has been inferred to be reflexive for the following entity.</xsl:text></p>
     <xsl:apply-templates/>
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

  <xsl:template match="*[* or processing-instruction() or comment() or string-length(.) &gt; 50]">
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
