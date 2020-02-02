/*
 * Interface to provenance validator / translator
 *
 * validator = new ProvService(endpoint);
 *
 * validator.translate( ... );
 * validator.validate( ... );
 *
 */
function ProvService(url)
{
	this.endpoint = url;
}

ProvService.prototype.call = function(fd, mimetype, callback, use_success)
{
	var ajax_params = {
	    type: "POST",
	    headers: {Accept: mimetype },
	    contentType: false,
	    processData: false,
	    url: this.endpoint + "provapi/documents",
	    data: fd
	}

	if (use_success) {
	    ajax_params['success'] = callback;
	} else {
	    ajax_params['complete'] = callback;
	}

	$.ajax(ajax_params);
}

ProvService.prototype.parseInputToForm = function(input, fd)
{
	var fd = new FormData();

	if ( input.hasOwnProperty('statements') ) {
		fd.append('statements', input['statements']);
		fd.append('type', input['type']);
	} else if ( input.hasOwnProperty('url') ) {
		fd.append('url', input['url']);
	} else {
		return null;
	}

	return fd;
}

/*
 * translate between PROV formats
 *
 * translate(input, mimetype, callback, data_only)
 *
 * input: an object in one of two forms:
 *       { url: <url to translate> }
 *       { statements: <provenance data>,
 *         type: <type of data> }
 *       Supported types: provn, provx, json, rdf, trig, ttl
 *
 * mimetype: mimetype of the desired output
 *
 * callback: callback once translation has occured
 *
 * data_only: determines the type of the object sent to callback:
 *            true: just return the data in as native format as possible
 *            false: return a jqXHR so you can check response code etc
 *                   this is the default
 *
 */

ProvService.prototype.translate = function(input, mimetype, callback, use_success)
{
	var fd = this.parseInputToForm(input);
	var type = {'text/turtle': 'ttl',
	            'text/provenance-notation': 'provn',
	            'application/provenance+xml': 'provx',
	            'application/json': 'json',
	            'application/tdf+xml': 'rdf',
	            'application/x-trig': 'trig',
	            'image/svg+xml': 'svg'}[mimetype];
	if (fd) {
		fd.append('translate', type);
		this.call(fd, mimetype, callback, use_success);
	}
}

/*
 * validate a prov document
 *
 * validate(input, callback, data_only)
 *
 * arguments as with translate()
 *
 * Note: the result, a validation report, is in xml
 *
 */

ProvService.prototype.validate = function(input, callback, use_success)
{
	var fd = this.parseInputToForm(input);
	if (fd) {
		fd.append('validate', 'Validate');
		this.call(fd, "text/xml", callback, use_success);
	} else {
		return false;
	}
}
