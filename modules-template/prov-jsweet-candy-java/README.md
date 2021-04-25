
## Description

This project is a JSweet Maven project that packages as a JSweet candy, so that you can use it in various contexts:

- as a candy in other JSweet projects (just include the dependency in your Maven ``pom.xml``) 
- as a regular Java artifact in regular Java projects, since the implementation does not use any JavaScript APIs (just include the dependency in your Maven ``pom.xml``)
- as a JavaScript library from a TypeScript project (use ``dist/myCandy.d.ts``)
- as a JavaScript library from a JavaScript project (use ``dist/myCandy.js``)

For the sake of example, this project defines a simple function ``myCandy.API.isPrime(n)``, which tells if ``n`` is a prime number.

This project can be used as a template to create your own JSweet candies. 

> NOTE: if you want to package a JSweet candy from an existing JavaScript library, then you should refer to the jsweet-candy-js-quickstart project.

## How to install the candy

Get the project:

```bash
> git clone https://github.com/cincheo/jsweet-candy-quickstart myproject
> cd myproject
```

Install the JSweet candy in your local Maven repository:

```bash
> mvn install
```

Note: if you want to publish the candy in a remote Maven repository, please refer to existing documentation on that particular topic.

## Use from a Java/JSweet project

For example, clone the [jsweet-quickstart](https://github.com/cincheo/jsweet-quickstart) project and add the following dependency to the ``pom.xml``:

```xml
<dependency>
	<groupId>myCandyGroup</groupId>
	<artifactId>myCandy</artifactId>
	<version>0.0.1-SNAPSHOT</version>
</dependency>
```

Now, edit the ``QuickStart.java`` class and in the ``main`` function, add something like:

```java
System.out.println("isPrime: "+myCandy.API.isPrime(3));
```

Note that you can launch with Java and it will work if your project does not use any JavaScript API.

To run with JavaScript in a browser, the ``index.html`` file must include the JavaScript bundle. Add the following line in the HTML header:

```html
<script type="text/javascript" src="myCandy-0.0.1-SNAPSHOT/bundle.js"></script>
```

*Warning: the ``myCandy-0.0.1-SNAPSHOT/bundle.js`` gets extracted from the jar by JSweet, so it will not be available until you include the candy in your dependencies and run ``mvn generate-sources``. Note that the extract location depends on the ``candiesJsOut`` option.*

Generate again the source and open the ``index.html`` as explained in the [jsweet-quickstart](https://github.com/cincheo/jsweet-quickstart) project. You should see the log ``isPrime: true`` showing in your browser's console.

## Use from a TypeScript/JavaScript project

When installing the candy, Maven also puts the ``myCandy.d.ts`` and ``myCandy.js`` bundles in the ``dist`` directory.

- From a TypeScript project, use the ``dist/myCandy.d.ts`` to compile and include the ``myCandy.js`` bundle in your web page.
- From a JavaScript project, include the ``dist/myCandy.js`` bundle in your web page.

## How does it work?

In the ``pom.xml``, we use various options of the JSweet Maven plugin to package the candy. Key options are the following:

- ``<bundle>true</bundle>``: bundles all the generated files in one single file called ``bundle.*``.
- ``<outDir>src/main/resources/META-INF/resources/webjars/${project.artifactId}/${project.version}``</outDir>: puts the generated JavaScript bundle file (``bundle.js``) in the standard location, according to the Webjars conventions.
- ``<declaration>true</declaration>``: ask JSweet to also generate the TypeScript definition file for the bundle (``bundle.d.ts``).
- ``<dtsOut>src/main/resources/src/typings/${project.artifactId}/${project.version}</dtsOut>``: the place where to generate the  the TypeScript definition bundle file.

Additionally, we use Maven resource filtering to automatically place the right transpiler version in the ``src/main/resources/META-INF/candy-metadata.json`` file. This is not mandatory, but if the wrong JSweet transpiler version is set, JSweet will report a warning when compiling with the candy.

Finally, we use the Maven antrun plugin to copy the generated bundles in ``dist/myCandy.d.ts`` and ``dist/myCandy.js``.

## License

Apache 2 Open Source license.


