<%@ page import="org.openprovenance.prov.service.translator.ViewService" %>

<div class="row">
<div class="span12">
<%=  ViewService.longVersion %>
</div>
</div>

<script src="${param.adjust}../bootstrap/js/bootstrap-dropdown.js"></script>


<script>
    var myUrl=document.URL;
    var urlPrefix=myUrl.substring(0,myUrl.lastIndexOf("/")).replace('/view', '/provapi/');

    function updateURLInputBox(val){
        document.getElementById("the-url-input").value = val;
    }
    function updateLocalURLInputBox(val){
        document.getElementById("the-url-input").value =  urlPrefix + val;
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

$('.dropdown-toggle').dropdown()
</script>

