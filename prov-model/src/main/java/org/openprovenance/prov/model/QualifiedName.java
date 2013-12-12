package org.openprovenance.prov.model;

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
 * <table class="grammar">
 * <tbody class="term"> <tr style="vertical-align: baseline; "> <td><a id="prod-QUALIFIED_NAME" data-name="prod-QUALIFIED_NAME"></a>[<span class="prodNo">52</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">QUALIFIED_NAME</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">( <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_PREFIX">PN_PREFIX</a></span> ":" )? <span class="prod"><a class="grammarRef" href="#prod-PN_LOCAL">PN_LOCAL</a></span><br> | <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_PREFIX">PN_PREFIX</a></span> ":"</code></td> </tr> </tbody>
 * <tbody class="term"> <tr style="vertical-align: baseline; "> <td><a id="prod-PN_LOCAL" data-name="prod-PN_LOCAL"></a>[<span class="prodNo">53</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">PN_LOCAL</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">( <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS_U">PN_CHARS_U</a></span> | [0-9] | <span class="prod"><a class="grammarRef" href="#prod-PN_CHARS_OTHERS">PN_CHARS_OTHERS</a></span> ) ( ( <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS">PN_CHARS</a></span> | "." | <span class="prod"><a class="grammarRef" href="#prod-PN_CHARS_OTHERS">PN_CHARS_OTHERS</a></span> )* ( <span class="prod"><a class="grammarRef" href="http://www.w3.org/TR/rdf-sparql-query/#rPN_CHARS">PN_CHARS</a></span> | <span class="prod"><a class="grammarRef" href="#prod-PN_CHARS_OTHERS">PN_CHARS_OTHERS</a></span> ) )?</code></td> </tr> </tbody>
 * <tbody class="term"> <tr style="vertical-align: baseline; "> <td><a id="prod-PN_CHARS_OTHERS" data-name="prod-PN_CHARS_OTHERS"></a>[<span class="prodNo">54</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">PN_CHARS_OTHERS</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">"/"<br> | "@"<br> | "~"<br> | "&amp;"<br> | "+"<br> | "*"<br> | "?"<br> | "#"<br> | "$"<br> | "!"<br> | <span class="prod"><a class="grammarRef" href="#prod-PERCENT">PERCENT</a></span><br> | <span class="prod"><a class="grammarRef" href="#prod-PN_CHARS_ESC">PN_CHARS_ESC</a></span></code></td> </tr> </tbody>
 * <tbody class="term"> <tr style="vertical-align: baseline; "> <td><a id="prod-PN_CHARS_ESC" data-name="prod-PN_CHARS_ESC"></a>[<span class="prodNo">55</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">PN_CHARS_ESC</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">"\" ( "=" | "'" | "(" | ")" | "," | "-" | ":" | ";" | "[" | "]" | "." )</code></td> </tr> </tbody>
 * <tbody class="term"> <tr style="vertical-align: baseline; "> <td><a id="prod-PERCENT" data-name="prod-PERCENT"></a>[<span class="prodNo">56</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">PERCENT</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">"%" <span class="prod"><a class="grammarRef" href="#prod-HEX">HEX</a></span> <span class="prod"><a class="grammarRef" href="#prod-HEX">HEX</a></span></code></td> </tr> </tbody>
 * <tbody class="term"> <tr style="vertical-align: baseline; "> <td><a id="prod-HEX" data-name="prod-HEX"></a>[<span class="prodNo">57</span>]&nbsp;&nbsp;&nbsp;</td> <td>&lt;<code class="production term">HEX</code>&gt;</td> <td>&nbsp;&nbsp;&nbsp;::=&nbsp;&nbsp;&nbsp;</td> <td><code class="content">[0-9]<br> | [A-F]<br> | [a-f]</code></td> </tr> </tbody>
 * </table>
 * 
 * 
 * @author lavm
 *
 */
public interface QualifiedName {

    public javax.xml.namespace.QName toQName();


    public String getUri();

    public void setUri(String uri);

    /** Get the local part of this QualifiedName. 
     * 
     * 
     * @return a string, the local part of this QualifiedName.
     *
     */

    public String getLocalPart();

    /** Set the local part of this QualifiedName. 
     * @param local the local part*/

    
    public void setLocalPart(String local);
    
    /** Get the Namespace URI of this QualifiedName.
     * 
     * @return a string, namespace URI of this QualifiedName.
     */

    public String getNamespaceURI();
    
    /** Set the Namespace URI of this QualifiedName.
     * @param namespaceURI the namespace URI
     */


    public void setNamespaceURI(String namespaceURI);

    /** Get the prefix of this Qualified Name. 
     * 
     * @return a string, prefix for the Qualifed Name.
     */


    public String getPrefix();

    public void setPrefix(String prefix);

    public boolean equals(Object objectToTest);

    /**
     * <p>Generate the hash code for this <code>QualifiedName</code>.</p>
     *
     * <p>The hash code is calculated using both the Namespace URI and
     * the local part of the <code>QualifiedName</code>.  The prefix is
     * <strong><em>NOT</em></strong> used to calculate the hash
     * code.</p>
     *
     * <p>This method satisfies the general contract of {@link
     * java.lang.Object#hashCode() Object.hashCode()}.</p>
     *
     * @return hash code for this <code>QualifiedName</code> <code>Object</code>
     */
    public abstract int hashCode();

}
