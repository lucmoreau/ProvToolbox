package org.openprovenance.prov.json;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.commonjs.module.Require;
import org.mozilla.javascript.tools.shell.Global;

public class RhinoValidator {

	private NativeObject parseJSON(BufferedReader reader) throws IOException
	{
		StringBuffer sb = new StringBuffer();
		String data;
		while ((data = reader.readLine()) != null)
			sb.append(data);

		return parseJSON(sb.toString());
	}
	
	private NativeObject parseJSON(String json)
	{

		Context cx = Context.enter();
		NativeObject result = null;
		try
		{
			Scriptable scope = cx.initStandardObjects();
			cx.evaluateString(scope, "x=" + json, "<cmd>", 1, null);
			result = (NativeObject) scope.get("x", scope);

		} finally
		{
			Context.exit();
		}
		return result;
	}

	private NativeObject getJSON(String path, Boolean isURL) throws IOException
	{
		BufferedReader in;
		if (!isURL)
		{
			FileReader fr = new FileReader(path);
			in = new BufferedReader(fr);
		} else
		{
			URL url = new URL(path);
			in = new BufferedReader(new InputStreamReader(url.openStream()));
		}

		NativeObject json = parseJSON(in);
		in.close();
		return json;
	}

	public List<String> validate(String path, Boolean isURL) throws IOException {
		Object json = getJSON(path, isURL);
		return validate(json);
	}
	
	public List<String> validate(String json) throws IOException {
		return validate(parseJSON(json));
	}

	private List<String> validate(Object json) throws IOException
	{
		ArrayList<String> result = new ArrayList<String>();
		
		Object schema = getJSON("schema/prov-json-schema.js", false);

		Global global = new Global();
		Context cx = Context.enter();
		try
		{
			global.initStandardObjects(cx, true);
			cx.setLanguageVersion(180);
			
			// Remote module path:
			// https://raw.github.com/garycourt/json-schema/master/lib/
			
			List<String> modulePaths = Arrays
					.asList("lib/");
			Require require = global.installRequire(cx, modulePaths, false);

			Scriptable exports = (Scriptable) require.call(cx, global, global,
					new String[] { "validate" });

			Function validate = (Function) exports.get("validate", exports);
			NativeObject res = (NativeObject) validate.call(cx, global, global,
					new Object[] { json, schema });

			NativeArray errors = (NativeArray) res.get("errors");

			for (int i = 0; i < errors.size(); i++)
			{
				NativeObject error = (NativeObject) errors.get(i);
				result.add(error.get("message").toString());
			}

		} finally
		{
			Context.exit();
		}

		return result;
	}

	public static void main(String args[])
	{
		if (args.length != 2)
		{
			System.err.println("Usage: provaljs -url|-file <url|file>");
			System.exit(1);
		}

		try
		{
			RhinoValidator validator = new RhinoValidator();
			List<String> errors = validator.validate(args[1],
					(args[0].equals("-url")));
			if (errors.size() == 0)
			{
				System.out.println("JSON is compliant with the PROV-JS Schema");
			} else
			{
				System.out.println(errors.size() + " validation error(s):");
				for (String error : errors)
				{
					System.out.println(error);
				}
			}
		} catch (Exception e)
		{
			System.err.println("Parsing failed: " + e.getMessage());
		}
	}
}
