/**
     A few DOM Manipulation functions for prov-dm and prov-n.

*/

/* A function to rename Authors: into Contributors: */
function setContributors () {
    $('dt').each(function(index) {
        var content=$(this).text();
        if (content== "Authors:") {
            console.log( "content " +  content);
            $(this).text("Contributors:")
        }
    })
}

/* A function to add a link to color-coded diffs next to previous version URI */
function setColoredDiffs () {
    setColoredDiffsForFile('diff.html');
}

function setColoredDiffsForFile (diff_file) {
    $('dt').each(function(index) {
        var content=$(this).text();
        if (content== "Previous version:") {
            console.log( "content " +  content);
	    $(this).next().append(" ").append($('<a>').attr('href',diff_file).append("(color-coded diff)"));
        }
    })
}


/* A function to replace a glossary reference by the definition found in the glossary.
   */
function updateGlossaryRefs() {
    $('.glossary-ref').each(function(index) {
        var ref=$(this).attr('data-ref');
        var span=$(this).attr('data-withspan')
        $(this).removeAttr('data-withspan');
        $(this).removeAttr('data-ref');
	
        $('#'+ref+'.glossary').contents().clone().appendTo($(this));
	//          $(this).attr("prov:hadOriginalSource",glossary_hg);
        if (span) {
            $(this).children('dfn').replaceWith(function(){return $('<span>').addClass('dfn').append($(this).contents())});
        }

    });

    // now, discard all definitions
    $('#glossary_div').html("ignore");
}

// function to replace figcaption since not allowed by prov rules, and not transformed by respec.js
function updateFigCaptions() {
    var figureCount=1;
    
    $('figcaption').each(function(index) {
	
        var myid=$(this).attr('id');
        var mycount=figureCount++;
	
        $(this).replaceWith(function(){return $('<span>').addClass('figcaption').attr('data-count', mycount).attr('id',myid).append("Figure " + mycount)
				       .append($('<sup>').append($('<a>').addClass('internalDFN').attr('href','#'+myid).append($('<span>').addClass('diamond').append(" &#9674;:")))).append(" ")
				       .append($(this).contents())});
    });
}

// function to update caption with linkable diamond
function updateCaptions() {
    var tableCount=1;
    
    $('caption').each(function(index) {
	
        var myid=$(this).attr('id');
        var mycount=tableCount++;
	
        $(this).attr('data-count', mycount)
	    .prepend($('<span>').append("Table " + mycount)
		     .append($('<sup>').append($('<a>').addClass('internalDFN').attr('href','#'+myid).append($('<span>').addClass('diamond').append(" &#9674;:")))).append(" "))
    });
}

// function to replace figure since not allowed by prov rules, and not transformed by respec.js
function updateFigures() {
    $('figure').each(function(index) {
	
        var myid=$(this).attr('id');
        var mystyle=$(this).attr('style');
	
        console.log( "figure " + myid + " " + $(this).contents());
	
        $(this).replaceWith(function(){
            var aNewElement=$('<span>').addClass('figure').append($(this).contents());
            if (myid) {
                aNewElement.attr('id',myid)
            }                                          
            if (mystyle) {
                aNewElement.attr('style',mystyle)
            }                                          
            return aNewElement });

        console.log( "figure " + myid);
    });
}

function updateExamples() {
    var count=1;
    $('.anexample').each(function(index) {
	
        var myid=$(this).attr('id');
        var mycount=count++;
	
        if (myid==undefined) {
            myid='example_' + mycount;
            $(this).attr('id',myid);
        }
	
        
        $(this).attr('data-count', mycount).prepend($('<div>').addClass('anexampleTitle')
                                                    //.append($('<a>').addClass('internalDFN').attr('href','#'+myid).append("Example " + mycount))
						    .append("Example " + mycount)
						    .append($('<sup>').append($('<a>').addClass('internalDFN').attr('href','#'+myid).append($('<span>').addClass('diamond').append(" &#9674;"))))
						   );
	
	
        //console.log( "example for " + myid + " " + mycount);
	
    });
}

/* Similar function for prov-n, but no diamond, just a link */
function updateExamplesWithLinks() {
    var count=1;
    $('.anexample').each(function(index) {

        var myid=$(this).attr('id');
        var mycount=count++;

        if (myid==undefined) {
            myid='example_' + mycount;
            $(this).attr('id',myid);
        }

        
        $(this).attr('data-count', mycount).prepend($('<div>').addClass('anexampleTitle')
                                                    .append($('<a>').addClass('internalDFN').attr('href','#'+myid).append("Example " + mycount)));

        //console.log( "example for " + myid + " " + mycount);

    });
}

function updateDfn() {
    var count=1;
    $('dfn').each(function(index) {

        var myid=$(this).addClass('internalDFN').attr('id');

        $(this).after($('<sup>').append($('<a>').addClass('internalDFN').attr('href','#'+myid).append($('<span>').addClass('diamond').append(" &#9674;"))));  //&#9674;//&#9830;
        
	//              console.log( "dfn for " + myid + " ");

    });
}


function updateExamplesRefs() {
    $('.anexample-ref').each(function(index) {

        myhref=$(this).attr('href');

        //console.log( "example ref for " + myhref);

        mycount=$(myhref).attr('data-count');

        //console.log( "example ref for " + myhref + " " + mycount);

        $(this).children('span').replaceWith(function(){return $('<span>').append("Example " + mycount)});

    });

    $('.anexample').each(function(index) {
        $(this).removeAttr('data-count');
    });

    $('caption').each(function(index) {
        $(this).removeAttr('data-count');
    });

    $('.figcaption').each(function(index) {
        $(this).removeAttr('data-count');
    });

}


function updateSectionRefs() {

    $('.section-ref').each(function(index) {

        myhref=$(this).attr('href');

        console.log( "section ref for " + myhref);

        if (myhref.startsWith("#")) {

            mysectionNumber=$(myhref).find('span.secno').first().text().trim();

            console.log( "section ref for " + myhref + " " + mysectionNumber);

            $(this).children('span').replaceWith(function(){return $('<span>').append("Section " + mysectionNumber)});

        }

    });
}

function insertProductionDefinition(doc, content, name) {

	var xml=$.parseXML(content);

    var segment = $(xml).find('a[name~="' + name + '"]').parents("tbody")[0];

//	return doc._esc((new XMLSerializer()).serializeToString(segment));
	return (new XMLSerializer()).serializeToString(segment);
}

function insertProductionGeneration(doc, content) {  return insertProductionDefinition(doc,content,'prod-prov_n_LL-generationExpression'); }

      function updateGrammarRefs() {
        $('.grammar-ref').each(function(index) {
          var ref=$(this).attr('data-ref');
          console.log( "updating grammar for " + ref);
//          $('#'+ref).parents("tbody").clone().appendTo($(this));


          // $(this).replaceWith(function() {return $('#'+ref).parents("tbody").clone()});

           $(this).replaceWith($('#'+ref).parents("tbody").clone());

        });
      }

function checkFragments() {
    $('a').each(function(index) {
        var myhref=$(this).attr('href');
        var myid=$(this).attr('id');
	if (myid!=undefined) {
	    // all OK!
	} else {
	    if (myhref==undefined) {
		var mytitle=$(this).attr('title');
		var myname=$(this).attr('data-name');
		if ((myname==undefined) && (mytitle==undefined)) {
		    console.log("fragment EMPTY HREF, title " + mytitle + ", id " + myid + ": " + $(this).html());
		}
	    } else {
		if (myhref.startsWith("#")) {
		    var elem=$(myhref).contents();
		    if (elem==undefined) {
			console.log("fragment " + myhref + " " + elem);
		    } else {
			// All OK
		    }
		}
	    }
	}
    });
}


function updateRules() {
    var count=1;
    $('.constraint,.definition,.inference').each(function(index) {

        var myid=$(this).attr('id');
        var mycount=count++;

        if (myid==undefined) {
            myid='rule_' + mycount;
            $(this).attr('id',myid);
        }

        var myClass=$(this).attr('class');

        var myTitle=capitaliseFirstLetter(myClass) + ' ' + mycount + ' (' + myid + ')';
        
        $(this).attr('data-count', mycount)
            .attr('data-title',myTitle).prepend($('<div>').addClass('ruleTitle')
						.append($('<a>').attr('id',mycount))
						.append($('<a>').addClass('internalDFN').attr('href','#'+myid).append(myTitle))
					       );

        //console.log( "rule for " + myid + " " + mycount);

    });
    
    $('.constraint-example,.definition-example,.inference-example').each(function(index) {

        var myid=$(this).attr('id');
        var mycount='NNN';

        if (myid==undefined) {
            myid='rule_' + mycount;
            $(this).attr('id',myid);
        }

        var myClass=$(this).attr('class');

        var myTitle=capitaliseFirstLetter(myClass) + ' ' + mycount + ' (' + myid + ')';
        
        $(this).attr('data-count', mycount)
            .attr('data-title',myTitle).prepend($('<div>').addClass('ruleTitle')
                                                .append($('<a>').addClass('internalDFN').attr('href','#'+myid).append(myTitle)));

        //console.log( "rule for " + myid + " " + mycount);

    });
}

function capitaliseFirstLetter(string)
{
    return string.charAt(0).toUpperCase() + string.slice(1);
}


function updateRulesRefs() {
    $('.rule-ref').each(function(index) {

        myhref=$(this).attr('href');

        //console.log( "example ref for " + myhref);

        mytitle=$(myhref).attr('data-title');

        console.log( "rule ref for " + myhref + " " + mytitle);

        $(this).children('span').replaceWith(function(){return $('<span>').append(mytitle)});

    });

    $('.rule-text').each(function(index) {

        myhref=$(this).attr('href');
        $(this).attr('href', myhref + '_text');

        //console.log( "example ref for " + myhref);

        mytitle=$(myhref).attr('data-title');

        console.log( "rule ref for " + myhref + " " + mytitle);

        $(this).children('span').replaceWith(function(){return $('<span>').append(mytitle)});

    });
}


function removeDataAttributes() {


    $('.anexample').each(function(index) {
        $(this).removeAttr('data-count');
    });

    $('caption').each(function(index) {
        $(this).removeAttr('data-count');
    });

    $('.figcaption').each(function(index) {
        $(this).removeAttr('data-count');
    });

    $('.definition').each(function(index) {
        $(this).removeAttr('data-count');
        $(this).removeAttr('data-title');
    });

    $('.inference').each(function(index) {
        $(this).removeAttr('data-count');
        $(this).removeAttr('data-title');
    });

    $('.constraint').each(function(index) {
        $(this).removeAttr('data-count');
        $(this).removeAttr('data-title');
    });

    $('.inference-example').each(function(index) {
        $(this).removeAttr('data-count');
        $(this).removeAttr('data-title');
    });

    $('.constraint-example').each(function(index) {
        $(this).removeAttr('data-count');
        $(this).removeAttr('data-title');
    });

    $('.definition-example').each(function(index) {
        $(this).removeAttr('data-count');
        $(this).removeAttr('data-title');
    });
}				    
