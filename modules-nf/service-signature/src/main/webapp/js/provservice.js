


var myUrl=document.URL;
var urlPrefix=myUrl.substring(0,myUrl.lastIndexOf("/")).replace('/view', '/provapi/');

var theURL=new URL(myUrl)
var origin=theURL.origin;

function updateURLInputBox(val){
    document.getElementById("the-url-input").value = val;
}

function updateLocalURLInputBox(val){
    document.getElementById("the-url-input").value =  urlPrefix + val;
}

function updateRootURLInputBox(val){
    document.getElementById("the-url-input").value =  origin + val;
}

function updateBindingsBox(url){
    if (url == '') {
        document.getElementById("the-bindings-input").value =  '';
    } else {
        $.get(url, function(data, status){
            //console.log(data);
            document.getElementById("the-bindings-input").value =  data;
        }, "text");
    }
}

function updateRootBindingsBox(url){
    if (url == '') {
        document.getElementById("the-bindings-input").value =  '';
    } else {
        console.log(origin+url);
        $.get(origin+url, function(data, status){
            //console.log(data);
            document.getElementById("the-bindings-input").value =  data;
        }, "text");
    }
}



//// narrative


function updateXplainTemplateInputBox(val){
    document.getElementById("the-xplain-templates").value = val;
}
function updateXplainProfileInputBox(val){
    document.getElementById("the-xplain-profiles").value = val;
}
