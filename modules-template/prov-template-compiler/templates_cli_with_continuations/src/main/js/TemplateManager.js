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

 * const templateManagerVariableName = new TemplateManager(logger, schemaUrl);
 * templateManagerVariableName.createDropdown("#template-dropdown", "templateManagerVariableName");
 *
 */



class TemplateManager {
    constructor(logger, schemaUrl, docUrl) {
        this.logger = logger;
        this.schemaUrl = schemaUrl;
        this.builders = logger.getBuilders();
        this.names = 0;
        this.jsonSchema = 0;
        this.builderTable = this.initializeBuilderTable();
        this.loadSchemaFile();
        if (docUrl) this.loadDocumentationFile();
        this.documentationSnippets={};

    }

    populateBean(template, values) {
        var builder = this.builderTable[template];
        var bean = builder.newBean();
        $.each(values, function (k, v) {
            bean[k] = v;
        });
        console.log(bean);
        var csv = bean.invoke(builder);
        return csv;
    }

    createFormConfig(jsonSchema, template) {
        const myself = this;
        let schemaDef = jsonSchema.definitions[template]
        let required = [];
        $.each(schemaDef.required, function (i, s) {
            required.push(s);
        });
        required.push({
            "type": "submit",
            "title": "Submit"
        });
        console.log(schemaDef);
        console.log(required)
        var config = {};
        config.schema = schemaDef;
        config.form = required;
        config.onSubmit = function (errors, values) {
            console.log(values);
            var csv = myself.populateBean(template, values);
            console.log(csv);
            $('#result').html('<p>' + csv + '</p>');
        };
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
                const item = $('<a>').addClass("dropdown-item").attr("href", "#").html(k).attr('onclick', action);
                menu.append($('<li>').append(item));
            }
        });
        $(id).append(menu);
    }




    updateForm(key) {
        console.log("update form " + key)
        console.log(this.jsonSchema)
        var config = this.createFormConfig(this.jsonSchema, key);
        $('form').jsonForm(config);
    }

    clearForm() {
        $('form').html("");
    }


    initializeBuilderTable() {
        const names = [];
        const table = {};
        $.each(this.builders, function (i, f) {
            names.push(f.getName());
            table[f.getName()] = f;
        });
        this.names = names;
        return table;
    };


    loadSchemaFile() {
        const myself = this;
        $.getJSON(schemaUrl,
            function (schemaFile) {
                myself.updateSchemaFile(schemaFile);

                console.log(myself.jsonSchema);
                console.log(myself.names);
            });
    };

    loadDocumentationFile() {
        const myself = this;
        $.get(docUrl, function(html_string) {
            $('#tmp_div').html(html_string)
            $.each(myself.names, function(i, k) {
                myself.documentationSnippets[k]=$("#template_" + k).html();
            })
            console.log(myself.documentationSnippets);

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
    }
}

