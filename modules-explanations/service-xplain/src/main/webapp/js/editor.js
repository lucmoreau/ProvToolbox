/* TODO: Change toolbox XML ID if necessary. Can export toolbox XML from Workspace Factory. */
var toolbox = document.getElementById("toolbox");

var options = {
    toolbox : toolbox,
    collapse : true,
    comments : true,
    disable : true,
    maxBlocks : Infinity,
    trashcan : true,
    horizontalLayout : false,
    toolboxPosition : 'start',
    css : true,
    media : 'https://blockly-demo.appspot.com/static/media/',
    rtl : false,
    scrollbars : true,
    sounds : true,
    oneBasedIndex : true,
    grid: {
        spacing: 25,
        length: 3,
        colour: '#ccc'
    },
    move: {
        scrollbars: true,
        drag: true,
        wheel: true,
    },
    zoom: {
        controls: true,
        startScale: 1.0,
        maxScale: 4,
        minScale: 0.25,
        scaleSpeed: 1.1
    }
};

/* Inject your workspace */
var workspace = Blockly.inject("blocklyDiv"/* TODO: Add ID of div to inject Blockly into */, options);

/* Load Workspace Blocks from XML to workspace. Remove all code below if no blocks to load */

/* TODO: Change workspace blocks XML ID if necessary. Can export workspace blocks XML from Workspace Factory. */
var workspaceBlocks = document.getElementById("workspaceBlocks");

/* Load blocks to workspace. */
Blockly.Xml.domToWorkspace(workspaceBlocks, workspace);

function prepForService(item) {
    if ((item===undefined) || (item===null) || (item==="")) return '{"type": "string", "value": "null"}'
    //console.log(item)
    let asJson=JSON.parse(item);
    var result;
    if (typeof asJson==="string") {
        var tmp={}
        tmp["type"]="string";
        if (asJson==="") {
            tmp["value"] = "empty";
        } else {
            tmp["value"] = asJson;
        }
        result=JSON.stringify(tmp);
    } else {
        if ((asJson["type"]==="noun_phrase") && ((asJson["head"]===undefined) || (asJson["head"]===""))) {
            asJson["head"]="null";
            result=JSON.stringify(asJson);
        } else if (asJson.type==="config") {
            //configuration_profile=asJson.config; // LUC, used to get this from blockly, now part of configuration tab
            //console.log("found config " + configuration_profile);
            result=null;
        } else {
            result=item;
        }
    }
    //console.log(result)
    return result;
}

var configuration_profile=null;

function createParagraph(code) {
    var items=code.split(';')
    var paragraph=""
    paragraph=paragraph + '{\n "type": "paragraph", \n';
    paragraph=paragraph +  ' "items": [ ';
    var first=true;
    for (var count =0; count<items.length; count++) {
        let avalue=prepForService(items[count])
        if (avalue!==null) {
            if (first) {
                first = false
            } else {
                paragraph = paragraph + ", ";
            }
            paragraph = paragraph + avalue;
        }
    }
    paragraph=paragraph + "], \n";
    paragraph=paragraph + ' "properties": {}\n} \n';
    return paragraph;
}

function updatePhraseFunction(event) {
    var code = Blockly.JavaScript.workspaceToCode(workspace);
    // remove the ;
    code=code.substring(0, code.lastIndexOf(";"));
    code=createParagraph(code);
    document.getElementById('textarea').innerHTML =code;

    expandPhrase(code, prov_document_prov_jsonld, dictionary, selected_context, profiles, selected_query, selected_select);
}


workspace.addChangeListener(updatePhraseFunction);

function showCode() {
    // Generate JavaScript code and display it.
    // Blockly.JavaScript.INFINITE_LOOP_TRAP = null;
    var code = Blockly.JavaScript.workspaceToCode(workspace);
    alert(code);
}

function displayInResponseTab(kind, text) {
    var pre = $('<pre>');
    pre.append(text);
    $('#tab-Response').html(pre);
}

function displayErrorInResponseTab(kind, text1, text2) {
    var pre1 = $('<div>');
    pre1.append(text1);
    var pre2 = $('<pre>');
    pre2.append(text2);
    $('#tab-Response').html(pre1).append("<h2>Error</h2>").append(pre2);
}


function displayInRequestTab(text) {
    var pre = $('<pre>');
    pre.append(escapeHTMLChars(text));
    $('#tab-Request').html(pre);
}

function setHoverFunction() {
    $('.provelement').off("removing any handler");
    $('.provelement').hover(function () {
        var myid = $(this).attr('data-id');
        $(this).css("background-color", "gold");
        console.log("*** hover: entering " + myid);

    }, function () {
        $(this).css("background-color", "white");
        var myid = $(this).attr('data-id');
        console.log(" *** hover: exiting " + myid);
    })
}

function expandPhrase(phrase, prov_document, dictionary, context, profiles, query, select) {
    if ((prov_document === undefined)
        || (dictionary === undefined)
        || (phrase === undefined)) {
        var warning="";
        if (dictionary === undefined) warning=warning + " Dictionary not specified. ";
        if (prov_document === undefined) warning=warning + " PROV Document not specified. ";
        if (phrase === undefined) warning=warning + " Use editor to construct a phrase. ";

        document.getElementById('resultDiv').innerHTML = "<i><b>" + warning + "</b></i>";
    } else {
        var url = '../../xplain/provapi/nlg/expander2/';
        var xhr = new XMLHttpRequest();
        xhr.open("POST", url);
        xhr.setRequestHeader("ACCEPT", "application/json");
        xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

        xhr.onreadystatechange = (e) => {
            if (xhr.responseText) {
                try {
                    //console.log(xhr.responseText)

                    var responseText = JSON.parse(xhr.responseText);
                    var oldSentence = document.getElementById('resultDiv').innerHTML;
                    var newSentence = responseText.result; //JSON.stringify(responseText.result);
                    if (oldSentence !== newSentence) {
                        document.getElementById('resultDiv').innerHTML = newSentence;
                        console.log(newSentence)
                    }
                    setHoverFunction();

                    displayInResponseTab('success', xhr.responseText);

                } catch (err) {
                    if (err instanceof SyntaxError) {
                        displayErrorInResponseTab('success', xhr.responseText, err);

                    }
                    displayInResponseTab('failure', err);
                }
            }
        }

        var body = "{" +
            "\"document\": " + JSON.stringify(prov_document) +
            ",\n \"dictionary\": " + JSON.stringify(dictionary) +
            ",\n \"profiles\": " + JSON.stringify(profiles) +
            ",\n \"phrase\": " + phrase +
            ",\n \"query\": " + JSON.stringify(query) +
            ",\n \"select\": " + JSON.stringify(select) +
            ",\n \"format\": " + selected_format +
            ",\n \"theProfile\": \"" + configuration_profile + "\" " +
            ",\n \"context\": " + JSON.stringify(context) + "}";
        xhr.send(body);
        displayInRequestTab(body);
    }
}

function realisePhrase(phrase) {
    //console.log ("posting to realiser ")
    //console.log (phrase)
    var url = '/xplain/provapi/nlg/realiser/';
    var xhr = new XMLHttpRequest();
    xhr.open("POST", url);
    xhr.setRequestHeader("ACCEPT", "application/json");
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");

    xhr.onreadystatechange = (e) => {
        if (xhr.responseText) {
            try {
                console.log(xhr.responseText)
                var responseText = JSON.parse(xhr.responseText);
                var oldSentence = document.getElementById('resultDiv').innerHTML;
                var newSentence = JSON.stringify(responseText.result);
                if (oldSentence !== newSentence) {
                    document.getElementById('resultDiv').innerHTML = newSentence;
                    console.log(newSentence)
                }
            } catch (err) {
                console.log(err);
            }

        }

    }

    xhr.send(phrase);

}



function toXml() {
    var output = document.getElementById('importExport');

    var xml = Blockly.Xml.workspaceToDom(workspace);
    output.value = Blockly.Xml.domToPrettyText(xml);
    output.focus();
    output.select();
}

function fromXml() {
    var input = document.getElementById('importExport');
    var xml = Blockly.Xml.textToDom(input.value);
    Blockly.Xml.domToWorkspace(xml, workspace);
}


function runCode() {
    // Generate JavaScript code and run it.
    window.LoopTrap = 1000;
    Blockly.JavaScript.INFINITE_LOOP_TRAP =
        'if (--window.LoopTrap == 0) throw "Infinite loop.";\n';
    var code = Blockly.JavaScript.workspaceToCode(workspace);
    Blockly.JavaScript.INFINITE_LOOP_TRAP = null;
    try {
        eval(code);
    } catch (e) {
        alert(e);
    }
}
function updateDiv(id,val){
    document.getElementById(id).value = val;
}

var documentUri = document.documentURI;

function getQueryVariable(query, variable) {
    //var query = window.location.search.substring(1);
    var vars = query.split('&');
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=');
        if (decodeURIComponent(pair[0]) == variable) {
            return decodeURIComponent(pair[1]);
        }
    }
    console.log('Query variable %s not found', variable);
}

var xplan_library_url;
var github_access_token;
var xplan_library;
var xplan_active_profiles;
var dictionary;
var profiles;
var xplans;
var selected_xplan;
var selected_query;
var selected_select;
var selected_context;
var selected_profiles={}
var selected_format=0;


var prov_document_url;
var prov_document;
var prov_document_prov_jsonld;

function loadConfiguration(id) {
    xplan_library_url=document.getElementById(id).value;
    console.log(xplan_library_url);
    github_access_token = window.location.href.match(/\?github_access_token=(.*)/)[1];
    var isHashThere = github_access_token.indexOf("#");
    if (isHashThere>0) {
        github_access_token=github_access_token.substring(0,isHashThere);
    }


    console.log(github_access_token)


    $.ajax({
        url: xplan_library_url,
        type: 'GET',
        accept: 'application/vnd.github.v3+json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", "token " + github_access_token);
        },

    }).done(function( data ) {

        console.log(data);
        var content=data.content;
        var decoded=atob(content);
        //console.log(decoded);
        var json=JSON.parse(decoded);
        console.log(json);
        xplan_library=json;
        xplan_active_profiles=json.active_profiles;
        var dictionary_name=xplan_library.dictionaries[0];
        var profiles_name=xplan_library.profiles[0];
        var xplans=xplan_library.templates;
        var dictionary_url=xplan_library_url.substring(0,xplan_library_url.lastIndexOf("/"))+"/"+dictionary_name;
        var profiles_url=xplan_library_url.substring(0,xplan_library_url.lastIndexOf("/"))+"/"+profiles_name;



        $.ajax({
            url: dictionary_url,
            type: 'GET',
            accept: 'application/vnd.github.v3+json',
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", "token " + github_access_token);
            },

        }).done(function( data ) {
            var content=data.content;
            var decoded=atob(content);
            dictionary=JSON.parse(decoded)
            $('#the-dictionary').html(syntaxHighlight_json(JSON.stringify(dictionary, null, 2)));
            console.log(dictionary)
        });

        $.ajax({
            url: profiles_url,
            type: 'GET',
            accept: 'application/vnd.github.v3+json',
            beforeSend: function(xhr) {
                xhr.setRequestHeader("Authorization", "token " + github_access_token);
            },

        }).done(function( data ) {
            var content=data.content;
            var decoded=atob(content);
            profiles=JSON.parse(decoded)
        });

        var menu=$('#xplan-menu-list');

        $.each(xplans, function(k,v) {
            var link=$('<a className="dropdown-item" href="#" onclick="selectXplan(\'' + v + '\')">');

            link.append(v);
            var li=$('<li>');
            li.append(link);
            menu.append(li);

            $('#xplan-dropdown-menu-button').removeAttr("disabled").removeClass("btn-secondary").addClass("btn-success")
        })

        console.log(xplan_active_profiles);
        $.each(xplan_active_profiles, function (indx, val) {
            var row=$('<div>');
            $.each(val, function (i,key) {
               row.append("<input type=\"radio\" class=\"btn-check\" name=\"row" + indx + "\" id=\"option"+ indx + "_" + i + "\" autocomplete=\"off\" >");
               row.append("<label class=\"btn btn-secondary\" for=\"option" + indx + "_" + i + "\" onclick=\"selectProfile('row" + indx + "','" + key + "')\">" + key + "</label>");
            });
            $('#profile-selection').append(row);
        })
        //$('#profile-selection').html(JSON.stringify(xplan_active_profiles, 4))


    });


}


function selectProfile(row, key) {
    selected_profiles[row]=key;
    var first=true;
    var tmp="";
    $.each(selected_profiles, function(k,v) {
        if (first) {
            first=false;
        } else {
            tmp=tmp+","
        }
        tmp=tmp+v;
    })
    configuration_profile=tmp;
    console.log(configuration_profile);
    updatePhraseFunction();
}



function selectXplan(key) {
    var xplan_url=xplan_library_url.substring(0,xplan_library_url.lastIndexOf("/"))+"/"+key;

    console.log("found xplan " + xplan_url);


    $.ajax({
        url: xplan_url,
        type: 'GET',
        accept: 'application/vnd.github.v3+json',
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", "token " + github_access_token);
        },

    }).done(function( data ) {
        var content=data.content;
        var decoded=atob(content);
        selected_xplan=JSON.parse(decoded)
        console.log(selected_xplan);

        selected_query=selected_xplan.query;
        if (Array.isArray(selected_query)) {
            selected_query=selected_query.join("\n");
        }
        selected_select=selected_xplan.select;
        selected_context=selected_xplan.context;


        var xhr = new XMLHttpRequest();
        xhr.open('POST', "../provapi/xplan", true);
        xhr.onerror= function(xhr,event) {
            console.log("error with " + event);
        };
        xhr.onabort= function(xhr,event) {
            console.log("abort with " + event);
        };
        xhr.onload= function() {
            console.log("loaded " + xhr.status);
            //console.log("loaded " + xhr.responseText);
            document.getElementById('importExport').value=xhr.responseText;
            $('#xplan-selection').html("<span style='font-style: italic'>Loaded: <span style=\"color:blue\">" + key + " </span></span>")
            fromXml();
        }
        xhr.setRequestHeader("Accept", "text/xml");
        xhr.setRequestHeader("Content-Type", "application/json");

        xhr.send(JSON.stringify(selected_xplan.sentence));


        $('#the-query').append(syntaxHighlight_provquery(selected_query));

    });

}


function selectFormat(num, text) {
    selected_format=num;
    $('#format-selection').html("Selected format: " + text);
}

function loadDocument(id) {
    prov_document_url = document.getElementById(id).value;
    console.log(prov_document_url);


    console.log(github_access_token)


    $.ajax({
        url: prov_document_url,
        type: 'GET',
        accept: 'application/vnd.github.v3+json',
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "token " + github_access_token);
        },

    }).done(function (data) {
        var content=data.content;
        prov_document=atob(content);

        var extension=prov_document_url.substring(prov_document_url.lastIndexOf("."));

        console.log(extension);

        if (extension !== "prov-jsonld") {

            var mediaType;
            if (extension===".provn") mediaType="text/provenance-notation"
            else  if (extension===".json") mediaType="application/json"


            var xhr = new XMLHttpRequest();
            xhr.open('POST', "../provapi/conversion", true);
            xhr.onerror = function (xhr, event) {
                console.log("error with " + event);
            };
            xhr.onabort = function (xhr, event) {
                console.log("abort with " + event);
            };
            xhr.onload = function () {
                console.log("loaded " + xhr.status);
                $('#prov-document-jsonld').html(syntaxHighlight_json(JSON.stringify(JSON.parse(xhr.responseText), null, 2)));
                prov_document_prov_jsonld=JSON.parse(xhr.responseText);
                updatePhraseFunction();
            }
            xhr.setRequestHeader("Accept", "application/ld+json");
            xhr.setRequestHeader("Content-Type", mediaType);

            var prov_document_string;
            if (typeof prov_document === "string") {
                prov_document_string=prov_document;
            } else {
                prov_document_string=JSON.stringify(prov_document);
            }
            xhr.send(prov_document_string);

        } else {
            prov_document_prov_jsonld=prov_document;
            updatePhraseFunction();
        }


    });
}

function xmlToString(xmlData) {
    var oSerializer = new XMLSerializer();
    var xmlString = oSerializer.serializeToString(xmlData);
    return xmlString;
}



function escapeHTMLChars(json) {
    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json;
}

function syntaxHighlight_json(json) {
    if (typeof json != 'string') {
        json = JSON.stringify(json, undefined, 2);
    }
    json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?)/g, function (match) {
        var cls = 'number';
        if (/^"/.test(match)) {
            if (/:$/.test(match)) {
                cls = 'key';
            } else {
                cls = 'string';
            }
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/null/.test(match)) {
            cls = 'null';
        }
        if (cls=='key') {
            if (/^"(wasGeneratedBy|prov:)|wasAssociatedWith|used|wasDerivedFrom|wasAttributedTo|specializationOf|alternateOf|entity|agent|activity|type|\$|actedOnBehalfOf/.test(match)) {
                cls='provkeyword';
            }
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}


function syntaxHighlight_provquery(code) {
    console.log("in syntaxHighlight_provquery");
    //code = code.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
    return code.replace(/(&lt;.*&gt;)|((\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d\.\d+([+-][0-2]\d:[0-5]\d|Z))|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d([+-][0-2]\d:[0-5]\d|Z))|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d([+-][0-2]\d:[0-5]\d|Z))|=|&gt;=|(\d+([.]\d+)?)|([a-zA-Z0-9]+(:[a-zA-Z0-9_\/.]+)?))/g, function (match) {
        var cls = 'string';
        if (/(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d\.\d+([+-][0-2]\d:[0-5]\d|Z))|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d:[0-5]\d([+-][0-2]\d:[0-5]\d|Z))|(\d{4}-[01]\d-[0-3]\dT[0-2]\d:[0-5]\d([+-][0-2]\d:[0-5]\d|Z))/.test(match)) {
            cls = 'date';
        } else if (/&lt;.*&gt;/.test(match)) {
            cls = 'uri';
        } else if (/=|>=/.test(match)) {
            cls = 'operator';
        } else if (/&gt;/.test(match)) {
            cls = 'operator';
        } else if (/true|false/.test(match)) {
            cls = 'boolean';
        } else if (/^[+-]?\d+([.]\d+)?$/.test(match)) {
            cls = 'number';
        } else if (/(([a-zA-Z0-9])+:[a-zA-Z0-9_\/.]+?)/.test(match)) {
            if (/^prov/.test(match)) {
                cls = 'provkeyword';
            } else {
                cls = 'symbol';
            }
        } else if (/null/.test(match)) {
            cls = 'null';
        } else if (/document|endDocument|prefix|wasGeneratedBy|wasAssociatedWith|used|wasDerivedFrom|wasAttributedTo|specializationOf|alternateOf|entity|agent|activity|actedOnBehalfOf/.test(match)) {
            cls='provkeyword';
        } else if (/select|join|aggregate|where|and|from|group|by|order|prefix|with/.test(match)) {
            cls='querykeyword';
        } else if (/^a$/.test(match)) {
            cls='querykeyword';
        }
        return '<span class="' + cls + '">' + match + '</span>';
    });
}



$(document).ready(function(){
    console.log(documentUri);

    var mypagePrefix=documentUri.substring(0,documentUri.indexOf("xplain/"));
    var redirect_page=mypagePrefix+"xplain/provapi/authenticate";

    console.log("redirect_page " + redirect_page)

    var theAuthenticatePage="https://github.com/login/oauth/authorize?client_id=Iv1.c3c8c66c0772c07b&redirect_uri=" + redirect_page;

    $('#github-login-link').attr("href",theAuthenticatePage);

    if (documentUri.indexOf("github_access_token")===-1 || !github_access_token) {
        console.log("not logged in");
        $('#link_configuration').addClass("show").addClass("active").removeClass("disabled");
        $('#link_editor').removeClass("show").removeClass("active").addClass("disabbled");
    }


});
