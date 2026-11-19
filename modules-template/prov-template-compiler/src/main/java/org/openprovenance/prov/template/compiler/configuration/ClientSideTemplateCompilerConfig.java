package org.openprovenance.prov.template.compiler.configuration;

/**
 * T36 (R11, and E1: "Entry in templates"): an entry of the catalogue's {@code templates} that adds nothing to the server side: a
 * subtype of a simple template, or a flow between the beans of two subtypes. Every loop that walks {@code configs.templates} skips
 * these entries ({@link TemplateConfigurationEnum#isClientSide}); only the generators of the client-side classes read them.
 */
abstract public class ClientSideTemplateCompilerConfig extends TemplateCompilerConfig {
}
