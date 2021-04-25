## Description

To be usable by JSweet programs, JavaScript libraries need to be accessible from Java. To make them available, we need to package the Java APIs in Maven artifacts (so-called candies).

Most well-known JavaScript libraries are already available as candies in the JSweet repository. However, you may want to package (and maybe publish) your own candy because the right library (or library version) is not available, or simply because you are not happy with the existing candy.

This project defines a template and can be used as a starting point to create your own candy from an existing JavaScript library.

For the sake of example, we will use a very simple library: [sprintf-js](https://www.npmjs.com/package/sprintf-js).

For more information on how this Maven project works, check the [jsweet-candy-quickstart](https://github.com/cincheo/jsweet-candy-quickstart), which implements a very similar structure.

## How to build

Install the ``sprintf`` JavaScript library (to be included in the candy).

```bash
> bower install
```


Install with Maven:

```bash
> mvn install
```

This command performs the following Maven phases:
- ``compile``: compile with Java the Java APIs sources found in ``src/main/java``
- ``generate-sources``: create the ``.d.ts`` TypeScript definitions bundle and puts it as a resource in ``src/typings``
- ``package``: create the jar with the compiled Java APIs, and all the resources (including the TypeScript definitions, the ``sprintf`` JavaScript library, and the ``candy-metadata.json`` file filtered to set the targeted transpiler version)
- ``install``: install the Maven artifact in your local Maven repository

## How to adapt/modify

You can adapt this template to your own needs in order to bridge any JavaScript library you want.

For details on how to define an API (definitions) with JSweet, please refer to the <a href="https://github.com/cincheo/jsweet/blob/master/doc/jsweet-language-specifications.md#definitions">Language Specifications</a>. Note that if you have the TypeScript definition file of your library, you can use the help of the online [API translator tool](http://www.jsweet.org/online-typescript-to-java-api-translator/) to translate it to Java.

If you want, consider contributing to the [JSweet](https://github.com/jsweet-candies) Candies Open Source organization on Github.

## License

Apache 2 Open Source license.

