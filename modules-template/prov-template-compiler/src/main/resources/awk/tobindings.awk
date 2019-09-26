
function ltrim(s) { sub(/^[ \t\r\n]+/, "", s); return s }
function rtrim(s) { sub(/[ \t\r\n]+$/, "", s); return s }
function trim(s)  { return rtrim(ltrim(s)); }

BEGIN {
      printf("{\"var\":\n{")
      OFS=FS=","
}
NR==1 {                                # Process header
    for (i=1;i<=NF;i++)                
        head[i] = trim($i)                  
    next                               
}
NR==2 {                                # Process types
    for (i=1;i<=NF;i++)                
        type[i] = trim($i)             
    next                               
}
NR==line{
    first=1
    for (i=1;i<=NF;i++) {              # For each field
	if (first) {
	    first=0
	} else {
	    printf ","
	}
	if (type[i]=="prov:QUALIFIED_NAME") {
	    printf "\"%s\": [{\"@id\": \"%s\"}]",  trim(head[i]), trim($i)
	} else if (type[i]=="xsd:string") {
	    printf "\"%s\": [ \"%s\" ]",  trim(head[i]), trim($i)
	} else  {
	    printf "\"%s\": [ {\"@value\": \"%s\", \"@type\": \"%s\"} ]",trim(head[i]), trim($i), trim(type[i])
	}
    }
    printf "\n"                        
}
END {
    printf("},\n")
    printf("\"context\": {\"ex\": \"http://example.org/\"}\n")
    printf("}\n")    
}

