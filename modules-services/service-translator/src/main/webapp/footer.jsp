

<div class="row">
<div class="span12">
<%=  org.openprovenance.prov.pservice.ViewService.longVersion %>
</div>
</div>

<script src="${param.adjust}../bootstrap/js/bootstrap-dropdown.js"></script>


<script>
function updateURLInputBox(val){
    document.getElementById("the-url-input").value = val;
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

