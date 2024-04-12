
/**
 * ProvToolbox
 *
 * Once the jar file MODULE.VERSION has been generated by the template compiler,
 * it can be exposed as a webjar resource
 *
 *
 * The following file can be imported as
 * <script type="text/javascript" src="/webjars/MODULE/VERSION/js/TemplateManager.js"></script>
 *
 * It can then be used as follows:
 *
 *
 *
 * let schemaUrl = "http://domainname/webjars/MODULE/VERSION/schema/schema.json";
 * const logger = new your.application.path.Logger();

 * const templateManagerVariableName = new TemplateManager(logger, schemaUrl, docurl, profile);
 * templateManagerVariableName.createDropdown("#template-dropdown", "templateManagerVariableName");
 *
 */

const ELEMENTS = "__elements";


class TemplateManager {

    constructor(logger, schemaUrl, docUrl, profileValue) {
        this.logger = logger;
        this.schemaUrl = schemaUrl;
        this.builders = logger.getBuilders();
        this.names = 0;
        this.jsonSchema = 0;
        this.builderTable = this.initializeBuilderTable();
        this.loadSchemaFile();
        this.docUrl=docUrl;
        if (docUrl) this.loadDocumentationFile();
        this.documentationSnippets={};
        this.profile=profileValue;
        console.log(this.profile);
        this.csv_result='#csv_result';
        this.filenameConverter={};
    }

    setFilenameConverter(obj) {
        this.filenameConverter=obj;
    }

    setImageUrlPrefix(location)  {
        this.image_url_prefix=location;
    }
    setImageLocation(location)  {
        this.image_location=location;
    }

    setCsvResult(location) {
        this.csv_result = location;
    }

    setJsonResult(location) {
        this.json_result=location;
    }
    setBean(bean) {
        this.bean=bean;
    }
    getBean() {
        return this.bean;
    }

    populateBean(template, values) {
        var builder = this.builderTable[template];
        var bean = builder.newBean();
        console.log("populateBean")
        for (const k in values) {
            if (k===ELEMENTS) {
                var subBeans = [];
                for (const i in values[k]) {
                    subBeans.push(this.populateSubBean(values["type"], values[k][i]));
                }
                bean[k] = subBeans;
            } else {
                bean[k] = values[k];
            }
        }
        console.log(bean);
        var csv = bean.process(builder.aArgs2CsVConverter);
        values["isA"]=builder.getName();
        return [csv,values]; // note: i am only exporting the populated values, as opposed to the whole bean
    }

    populateSubBean(template, values) {
        console.log("populateSubBean " + template )
        console.log(values)
        var builder = this.builderTable[template];
        var bean = builder.newBean();
        for (const k in values) {
            bean[k] = values[k];
        }
        console.log(bean);
        values["isA"]=builder.getName();
        return bean; // note: i am only exporting the populated values, as opposed to the whole bean
    }


    defaultSubmitFunction(myself, template, csv_location, json_location) {
        return function (errors, values) {
            console.log(values);
            if (values[ELEMENTS]) {
                values.count=values[ELEMENTS].length;
            }
            let csv_bean = myself.populateBean(template, values);
            let csv=csv_bean[0];
            let bean=csv_bean[1];
            let name=bean["isA"];
            console.log(csv);
            myself.setBean(bean);
            if (values[ELEMENTS]) {
                myself.getBean().count=values[ELEMENTS].length;
            }
            if (csv_location) {
                let csv_div = $('<div>');
                csv_div.append('<p>' + csv + '</p>')
                if (errors) {
                    csv_div.append('<p>Reported errors:</p>')
                    csv_div.append(errors)
                }
                $(csv_location).html(csv_div);
            }

            if (json_location) {
                let json_div = $('<div>');

                json_div.append('<p>' + myself.syntaxHighlight_json(JSON.stringify(bean, null, 2), name) + '</p>');
                if (errors) {
                    json_div.append('<p>Reported errors:</p>')
                    json_div.append(errors)
                }
                $(json_location).html(json_div);
            }
        }
    }

    createFormConfig(jsonSchema, template) {
        const myself = this;
        let onSubmit = this.defaultSubmitFunction(myself, template, this.csv_result, this.json_result);
        let schemaDef = jsonSchema.definitions[template]
        let required = [];
        let myprofile;
        if (this.profile) {
            myprofile = this.profile[template];
            if (!myprofile) {
                myprofile="ALL";
            }
        } else {
            myprofile = "ALL";
        }
        console.log(myprofile);
        $.each(schemaDef.required, function (i, property) {
            let compulsory=myprofile.compulsory.includes(property)
            if (myprofile==="ALL" || compulsory || myprofile.optional.includes(property)) {
                if (compulsory) {
                    schemaDef.properties[property].required = true;
                }
                required.push({
                    "key": property,
                    "onChange": function (evt) {
                        var value = $(evt.target).val();
                        if (value) {
                            console.log(property + ": " + value);
                            let values = $('form').jsonFormValue();
                            console.log(values)
                            onSubmit(undefined, values);
                        }
                    }
                });
            }
        });

        if (schemaDef.properties[ELEMENTS]) {
            const nested_compulsory=myprofile.compulsory.filter(x => Array.isArray(x)).map(x => x[1])
            $.each(schemaDef.properties[ELEMENTS].items.required, function (i, property) {
                let compulsory=nested_compulsory.includes(property)
                if (compulsory) {
                    schemaDef.properties[ELEMENTS].items.properties[property].required = true;
                }
            });
            const newSubProperties={}
            $.each(schemaDef.properties[ELEMENTS].items.required, function (i, property) {
                newSubProperties[property]=schemaDef.properties[ELEMENTS].items.properties[property]
            });
            schemaDef.properties[ELEMENTS].items.properties=newSubProperties;

        }

        if (false) {
            required.push({
                "type": "submit",
                "title": "Submit"
            });
        }
        //console.log(schemaDef);
        //console.log(required)
        var config = {};
        config.schema = schemaDef;
        config.form = required;
        config.onSubmit = onSubmit;
        config.params = {
            "fieldHtmlClass": "input-small"
        };
        return config;
    }

    createDropdownMenu(id, templateManagerVariableName, keys) {
        var createActionStringForDocumentation= function (templateManagerVariableName, k) {
            return templateManagerVariableName + '.updateForm("' + k + '")';
        }
        this.createDropdownMenuWithAction(id,templateManagerVariableName,keys,createActionStringForDocumentation,[])
    }

    createDropdownMenuWithAction(id, templateManagerVariableName, keys, dropDownAction, rejects) {
        const menu = $('<ul>').addClass("dropdown-menu").attr('id', id)
        $.each(keys, function (i, k) {
            if (rejects && rejects.includes(k)) {
                // ignore the rejected keys, if any
            } else {
                let action = dropDownAction(templateManagerVariableName, k);
                const item = $('<a>').addClass("dropdown-item").attr("data-tmpl", k).attr("href", "#").html(k).attr('onclick', action);
                menu.append($('<li>').append(item));
            }
        });
        $(id).append(menu);
    }

    updateForm(key) {
        console.log("update form " + key);
        this.clearForm();
        var config = this.createFormConfig(this.jsonSchema, key);
        console.log(config)
        $('form').jsonForm(config);
    }

    clearForm() {
        $('form').html("");
    }


    initializeBuilderTable() {
        const names = [];
        const table = {};
        this.builders.forEach(function (f) {
            names.push(f.getName());
            table[f.getName()] = f;
        });
        this.names = names;
        return table;
    };


    loadSchemaFile() {
        const myself = this;
        $.getJSON(this.schemaUrl,
            function (schemaFile) {
                myself.updateSchemaFile(schemaFile);

                console.log(myself.jsonSchema);
                console.log(myself.names);
            });
    };

    loadDocumentationFile() {
        const myself = this;
        $.get(this.docUrl, function(html_string) {
            $('#tmp_div').html(html_string)
            $.each(myself.names, function(i, k) {
                myself.documentationSnippets[k]=$("#template_" + k).html();
            })
            //console.log(myself.documentationSnippets);

        },'html');
    }

    updateSchemaFile(file) {
        this.jsonSchema = file;
    }

    createDropdownForPlayground(id, templateManagerVariableName) {
        this.createDropdownMenu(id, templateManagerVariableName, this.names);
    }

    createDropdownForDocumentation(id, templateManagerVariableName) {
        var createActionStringForDocumentation= function (templateManagerVariableName, k) {
            return templateManagerVariableName + '.insertDocumentation("' + k + '")';
        }
        this.createDropdownMenuWithAction(id, templateManagerVariableName, this.names, createActionStringForDocumentation, []);
    }

    insertDocumentation(template) {
        console.log("documentation " + template);
        $('#documentation').html(this.documentationSnippets[template]);

        if (this.image_location && this.image_url_prefix)  {
            let img=$('<img/>').attr("src", this.image_url_prefix + (this.filenameConverter[template] || template) + ".png");
            $(this.image_location).html(img);
            console.log("added image at " + this.image_location);
            console.log(img);
        }
    }


    syntaxHighlight_json(json, name) {
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
                    let regex=new RegExp('^"' + name);
                    if (regex.test(match)) {
                        cls='isA';
                    }
                }
		
            } else if (/true|false/.test(match)) {
                cls = 'boolean';
            } else if (/null/.test(match)) {
                cls = 'null';
            }
            if (cls==='key') {
                if (/^"(wasGeneratedBy|prov:)|wasAssociatedWith|used|wasDerivedFrom|wasAttributedTo|specializationOf|alternateOf|entity|agent|activity|type|\$|actedOnBehalfOf/.test(match)) {
                    cls='provkeyword';
                } else if (/^"isA/.test(match)) {
                    cls='isA';
                }
            }
            return '<span class="' + cls + '">' + match + '</span>';
        });
    }

}

