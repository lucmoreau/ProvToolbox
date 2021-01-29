/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.prov.model {
    /**
     * 
     * <p>Interface for PROV Qualified Name.
     * <p><a href="http://www.w3.org/TR/prov-dm/#concept-qualifiedName">PROV-DM Definition for Qualified Name</a>:
     * A qualified name is a name subject to namespace interpretation. It consists of a namespace, denoted by an optional prefix, and a local name.
     * <p>PROV-DM stipulates that a qualified name can be mapped into an IRI by concatenating the IRI associated with the prefix and the local part.
     * <p>A qualified name's prefix is optional. If a prefix occurs in a qualified name, it refers to a namespace declared in a namespace declaration. In the absence of prefix, the qualified name refers to the default namespace.
     * 
     * <p>This interface specification is derived from {@link javax.xml.namespace.QName} without the implied syntactic restrictions of xsd:QName.
     * 
     * <p><a href="http://www.w3.org/TR/prov-n/#prod-QUALIFIED_NAME">PROV-N production for Qualified Name</a>:.
     * 
     * <table class="grammar" >
     * <tbody class="term"> <tr style="vertical-align: baseline; "> <td>[<span class="prodNo">52</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">QUALIFIED_NAME</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">( <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_PREFIX">PN_PREFIX</a></span> ":" )? <span class="prod"><a class="grammarRef" href="#prod-PN_LOCAL">PN_LOCAL</a></span><br> | <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_PREFIX">PN_PREFIX</a></span> ":"</code></td> </tr> </tbody>
     * <tbody class="term"> <tr style="vertical-align: baseline; "> <td>[<span class="prodNo">53</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">PN_LOCAL</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">( <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS_U">PN_CHARS_U</a></span> | [0-9] | <span class="prod"><a class="grammarRef" href="#prod-PN_CHARS_OTHERS">PN_CHARS_OTHERS</a></span> ) ( ( <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS">PN_CHARS</a></span> | "." | <span class="prod"><a class="grammarRef" href="#prod-PN_CHARS_OTHERS">PN_CHARS_OTHERS</a></span> )* ( <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS">PN_CHARS</a></span> | <span class="prod"><a class="grammarRef" href="#prod-PN_CHARS_OTHERS">PN_CHARS_OTHERS</a></span> ) )?</code></td> </tr> </tbody>
     * <tbody class="term"> <tr style="vertical-align: baseline; "> <td>[<span class="prodNo">54</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">PN_CHARS_OTHERS</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">"/"<br> | "@"<br> | "~"<br> | "&amp;"<br> | "+"<br> | "*"<br> | "?"<br> | "#"<br> | "$"<br> | "!"<br> | <span class="prod"><a class="grammarRef" href="#prod-PERCENT">PERCENT</a></span><br> | <span class="prod"><a class="grammarRef" href="#prod-PN_CHARS_ESC">PN_CHARS_ESC</a></span></code></td> </tr> </tbody>
     * <tbody class="term"> <tr style="vertical-align: baseline; "> <td>[<span class="prodNo">55</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">PN_CHARS_ESC</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">"\" ( "=" | "'" | "(" | ")" | "," | "-" | ":" | ";" | "[" | "]" | "." )</code></td> </tr> </tbody>
     * <tbody class="term"> <tr style="vertical-align: baseline; "> <td>[<span class="prodNo">56</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">PERCENT</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">"%" <span class="prod"><a class="grammarRef" href="#prod-HEX">HEX</a></span> <span class="prod"><a class="grammarRef" href="#prod-HEX">HEX</a></span></code></td> </tr> </tbody>
     * <tbody class="term"> <tr style="vertical-align: baseline; "> <td>[<span class="prodNo">57</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">HEX</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">[0-9]<br> | [A-F]<br> | [a-f]</code></td> </tr> </tbody>
     * <caption></caption>
     * </table>
     * 
     * 
     * @author lavm
     * @class
     */
    export interface QualifiedName {
        /**
         * Converts this QualifiedName to a valid xsd:QName by unescaping \-characters in the local names, and _-encoding the local name.
         * @see <a href="https://github.com/lucmoreau/ProvToolbox/wiki/Mapping-PROV-Qualified-Names-to-xsd:QName#2-a-reversible-encoding">a reversible encoding</a>
         * @return {javax.xml.namespace.QName} a valid javax.xml.namespace.QName
         */
        toQName(): javax.xml.namespace.QName;

        getUri(): string;

        setUri(uri: string);

        /**
         * Get the local part of this QualifiedName.
         * 
         * 
         * @return {string} a string, the local part of this QualifiedName.
         */
        getLocalPart(): string;

        /**
         * Set the local part of this QualifiedName.
         * @param {string} local the local part
         */
        setLocalPart(local: string);

        /**
         * Get the Namespace URI of this QualifiedName.
         * 
         * @return {string} a string, namespace URI of this QualifiedName.
         */
        getNamespaceURI(): string;

        /**
         * Set the Namespace URI of this QualifiedName.
         * @param {string} namespaceURI the namespace URI
         */
        setNamespaceURI(namespaceURI: string);

        /**
         * Get the prefix of this Qualified Name.
         * 
         * @return {string} a string, prefix for the Qualifed Name.
         */
        getPrefix(): string;

        setPrefix(prefix: string);
    }
}

