package org.openprovenance.prov.template.compiler.test;

import org.junit.Test;
import org.openprovenance.prov.template.compiler.past.emitter.registry.ArgTreatment;
import org.openprovenance.prov.template.compiler.past.emitter.registry.ChainedGetBehavior;
import org.openprovenance.prov.template.compiler.past.emitter.registry.ReceiverTransform;
import org.openprovenance.prov.template.compiler.past.emitter.registry.ResultTransform;
import org.openprovenance.prov.template.compiler.past.emitter.registry.RustMethodRegistry;
import org.openprovenance.prov.template.compiler.past.emitter.registry.RustMethodSpec;
import org.openprovenance.prov.template.compiler.past.emitter.registry.RustTypeCategory;
import org.openprovenance.prov.template.compiler.past.emitter.registry.RustTypeSpec;
import org.openprovenance.prov.template.compiler.past.type.ClassName;
import org.openprovenance.prov.template.compiler.past.type.ParameterizedType;
import org.openprovenance.prov.template.compiler.past.type.TypeName;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link RustMethodRegistry}.
 *
 * <p>Covers three construction paths:</p>
 * <ol>
 *   <li>JSON loading via {@link RustMethodRegistry#loadFromClasspath()}.</li>
 *   <li>Programmatic construction via {@link RustMethodRegistry#builder()}.</li>
 *   <li>Parity assertions between both paths.</li>
 * </ol>
 *
 * <p>These tests also act as a regression harness for Phase 2 of the registry
 * refactoring: every test here corresponds to a type-classification or
 * method-name-conversion decision previously made by a hardcoded {@code if} in
 * {@code Rust.java}.</p>
 */
public class RustMethodRegistryTest {

    // ---- Helpers ----------------------------------------------------------------------------

    private RustMethodRegistry json() {
        return RustMethodRegistry.loadFromClasspath();
    }

    private ClassName cn(String pkg, String simpleName) {
        return new ClassName(simpleName, pkg);
    }

    // ---- JSON loading -----------------------------------------------------------------------

    @Test
    public void jsonFileLoads() {
        RustMethodRegistry r = json();
        assertNotNull(r);
        assertFalse("types list must not be empty", r.getTypes().isEmpty());
    }

    @Test
    public void jsonContainsAtLeast15TypeEntries() {
        assertTrue(json().getTypes().size() >= 15);
    }

    @Test
    public void jsonContainsMapMethodEntries() {
        RustMethodRegistry r = json();
        assertNotNull(r.resolveMethod(RustTypeCategory.MAP, "get"));
        assertNotNull(r.resolveMethod(RustTypeCategory.MAP, "containsKey"));
        assertNotNull(r.resolveMethod(RustTypeCategory.MAP, "put"));
    }

    @Test
    public void jsonContainsListMethodEntries() {
        RustMethodRegistry r = json();
        assertNotNull(r.resolveMethod(RustTypeCategory.LIST, "add"));
        assertNotNull(r.resolveMethod(RustTypeCategory.LIST, "size"));
        assertNotNull(r.resolveMethod(RustTypeCategory.LIST, "isEmpty"));
    }

    @Test
    public void jsonContainsGlobalMethods() {
        RustMethodRegistry r = json();
        assertNotNull(r.resolveMethodGlobal("toString"));
        assertNotNull(r.resolveMethodGlobal("forEach"));
    }

    // ---- Type category: MAP -----------------------------------------------------------------

    @Test
    public void fullyQualifiedMapIsMap() {
        RustMethodRegistry r = json();
        assertTrue(r.isCategory(cn("past.util", "Map"),     RustTypeCategory.MAP));
        assertTrue(r.isCategory(cn("past.util", "HashMap"), RustTypeCategory.MAP));
    }

    @Test
    public void simpleNameHashMapIsMap() {
        assertTrue(json().isCategory(cn(null, "HashMap"), RustTypeCategory.MAP));
    }

    @Test
    public void parameterizedMapIsMap() {
        ParameterizedType mapType = new ParameterizedType(
                cn("past.util", "HashMap"), cn(null, "String"), cn(null, "Integer"));
        assertTrue(json().isCategory(mapType, RustTypeCategory.MAP));
    }

    @Test
    public void mapIsNotList() {
        assertFalse(json().isCategory(cn("past.util", "HashMap"), RustTypeCategory.LIST));
    }

    // ---- Type category: LIST ----------------------------------------------------------------

    @Test
    public void allListVariantsAreList() {
        RustMethodRegistry r = json();
        assertTrue(r.isCategory(cn("past.util", "List"),       RustTypeCategory.LIST));
        assertTrue(r.isCategory(cn("past.util", "ArrayList"),  RustTypeCategory.LIST));
        assertTrue(r.isCategory(cn("past.util", "LinkedList"), RustTypeCategory.LIST));
    }

    @Test
    public void parameterizedListIsList() {
        ParameterizedType listType = new ParameterizedType(
                cn("past.util", "ArrayList"), cn(null, "Integer"));
        assertTrue(json().isCategory(listType, RustTypeCategory.LIST));
    }

    // ---- Type category: STRING --------------------------------------------------------------

    @Test
    public void stringIsStringNotPrimitive() {
        RustMethodRegistry r = json();
        assertTrue(r.isCategory(cn(null, "String"), RustTypeCategory.STRING));
        assertFalse(r.isCategory(cn(null, "String"), RustTypeCategory.PRIMITIVE));
    }

    // ---- Type category: PRIMITIVE -----------------------------------------------------------

    @Test
    public void allPrimitivesAndBoxedAreCategory_PRIMITIVE() {
        RustMethodRegistry r = json();
        for (String name : new String[]{
                "int","Integer","long","Long","float","Float",
                "double","Double","boolean","Boolean",
                "byte","Byte","short","Short","char","Character"}) {
            assertTrue("Expected PRIMITIVE for " + name,
                    r.isCategory(cn(null, name), RustTypeCategory.PRIMITIVE));
        }
    }

    @Test
    public void primitivesAreCopy() {
        RustMethodRegistry r = json();
        assertTrue(r.isCopy(cn(null, "int")));
        assertTrue(r.isCopy(cn(null, "boolean")));
        assertFalse(r.isCopy(cn(null, "String")));
        assertFalse(r.isCopy(cn(null, "HashMap")));
    }

    // ---- Type category: VOID ----------------------------------------------------------------

    @Test
    public void voidIsVoid() {
        RustMethodRegistry r = json();
        assertTrue(r.isCategory(cn(null, "void"),        RustTypeCategory.VOID));
        assertTrue(r.isCategory(cn(null, "Void"),        RustTypeCategory.VOID));
        assertTrue(r.isCategory(cn("past.lang", "Void"), RustTypeCategory.VOID));
    }

    // ---- Type category: FUNCTION ------------------------------------------------------------

    @Test
    public void functionTypesAreFunction() {
        RustMethodRegistry r = json();
        assertTrue(r.isCategory(cn("past.lang", "Function"),          RustTypeCategory.FUNCTION));
        assertTrue(r.isCategory(cn("java.util.function", "Function"), RustTypeCategory.FUNCTION));
        assertTrue(r.isCategory(cn(null, "Function"),                 RustTypeCategory.FUNCTION));
    }

    // ---- Type category: CLASS_REF -----------------------------------------------------------

    @Test
    public void classTypesAreClassRef() {
        RustMethodRegistry r = json();
        assertTrue(r.isCategory(cn("past.lang", "Class"), RustTypeCategory.CLASS_REF));
        assertTrue(r.isCategory(cn(null, "Class"),        RustTypeCategory.CLASS_REF));
    }

    // ---- Null / unknown handling ------------------------------------------------------------

    @Test
    public void nullTypeNameReturnsFalseAndNull() {
        RustMethodRegistry r = json();
        assertFalse(r.isCategory(null, RustTypeCategory.MAP));
        assertNull(r.resolveType(null));
        assertNull(r.rustName((TypeName) null));
        assertNull(r.rustName((String) null));
    }

    @Test
    public void unknownTypeNameReturnsNull() {
        RustMethodRegistry r = json();
        assertNull(r.resolveType(cn(null, "NoSuchType")));
        assertFalse(r.isCategory(cn(null, "NoSuchType"), RustTypeCategory.MAP));
    }

    // ---- rustName lookups -------------------------------------------------------------------

    @Test
    public void rustNameForAllPrimitives() {
        RustMethodRegistry r = json();
        assertEquals("i32",  r.rustName("int"));
        assertEquals("i32",  r.rustName("Integer"));
        assertEquals("i64",  r.rustName("long"));
        assertEquals("f32",  r.rustName("float"));
        assertEquals("f64",  r.rustName("double"));
        assertEquals("bool", r.rustName("boolean"));
        assertEquals("i8",   r.rustName("byte"));
        assertEquals("i16",  r.rustName("short"));
        assertEquals("char", r.rustName("char"));
    }

    @Test
    public void rustNameForStringAndVoid() {
        RustMethodRegistry r = json();
        assertEquals("String", r.rustName("String"));
        assertEquals("()",     r.rustName("void"));
        assertEquals("()",     r.rustName("Void"));
    }

    @Test
    public void rustNameViaTypeName() {
        RustMethodRegistry r = json();
        assertEquals("i32",    r.rustName(cn(null, "int")));
        assertEquals("String", r.rustName(cn(null, "String")));
        assertNull(r.rustName(cn(null, "UnknownType")));
    }

    // ---- MAP method specs -------------------------------------------------------------------

    @Test
    public void mapGet_hasKeyBorrowAndCopied() {
        RustMethodSpec spec = json().resolveMethod(RustTypeCategory.MAP, "get");
        assertNotNull(spec);
        assertEquals("get",                              spec.rustName);
        assertFalse(spec.mutatesReceiver);
        assertEquals(ArgTreatment.KEY_BORROW,            spec.argTreatment(0));
        assertEquals(ResultTransform.COPIED,             spec.resultTransform);
        assertEquals(ChainedGetBehavior.UNWRAP_AND_CHAIN, spec.chainedGetBehavior);
        assertEquals(ReceiverTransform.NONE,             spec.receiverTransform);
    }

    @Test
    public void mapContainsKey_hasKeyBorrowNoCopied() {
        RustMethodSpec spec = json().resolveMethod(RustTypeCategory.MAP, "containsKey");
        assertNotNull(spec);
        assertEquals("contains_key",         spec.rustName);
        assertFalse(spec.mutatesReceiver);
        assertEquals(ArgTreatment.KEY_BORROW, spec.argTreatment(0));
        assertEquals(ResultTransform.NONE,    spec.resultTransform);
        assertEquals(ChainedGetBehavior.NONE, spec.chainedGetBehavior);
    }

    @Test
    public void mapPut_mutatesAndSwitchesToGetMut() {
        RustMethodSpec spec = json().resolveMethod(RustTypeCategory.MAP, "put");
        assertNotNull(spec);
        assertEquals("insert",                          spec.rustName);
        assertTrue(spec.mutatesReceiver);
        assertEquals(ArgTreatment.OWNED_STRING,          spec.argTreatment(0));
        assertEquals(ArgTreatment.VALUE_UNWRAP,          spec.argTreatment(1));
        assertEquals(ChainedGetBehavior.SWITCH_TO_GET_MUT, spec.chainedGetBehavior);
    }

    // ---- LIST method specs ------------------------------------------------------------------

    @Test
    public void listAdd_pushMutatesAndClones() {
        RustMethodSpec spec = json().resolveMethod(RustTypeCategory.LIST, "add");
        assertNotNull(spec);
        assertEquals("push",                      spec.rustName);
        assertTrue(spec.mutatesReceiver);
        assertEquals(ArgTreatment.CLONE,           spec.argTreatment(0));
        assertEquals(ReceiverTransform.AS_MUT_UNWRAP, spec.receiverTransform);
    }

    @Test
    public void listSize_lenNoMutation() {
        RustMethodSpec spec = json().resolveMethod(RustTypeCategory.LIST, "size");
        assertNotNull(spec);
        assertEquals("len", spec.rustName);
        assertFalse(spec.mutatesReceiver);
    }

    // ---- Global method specs ----------------------------------------------------------------

    @Test
    public void globalToString_toSnakeName() {
        RustMethodSpec spec = json().resolveMethodGlobal("toString");
        assertNotNull(spec);
        assertEquals("to_string", spec.rustName);
        assertFalse(spec.mutatesReceiver);
    }

    @Test
    public void globalForEach_mutatesReceiver() {
        assertTrue(json().resolveMethodGlobal("forEach").mutatesReceiver);
    }

    @Test
    public void addMutatesViaGlobalSearch() {
        RustMethodSpec spec = json().resolveMethodGlobal("add");
        assertNotNull("add must be findable via global search", spec);
        assertTrue(spec.mutatesReceiver);
    }

    @Test
    public void unknownMethodReturnsNull() {
        RustMethodRegistry r = json();
        assertNull(r.resolveMethod(RustTypeCategory.MAP, "nonexistent"));
        assertNull(r.resolveMethodGlobal("nonexistent"));
    }

    // ---- ArgTreatment varargs coverage -----------------------------------------------------

    @Test
    public void argTreatment_emptyListDefaultsToPassByValue() {
        RustMethodSpec spec = new RustMethodSpec("foo","foo",false,
                ReceiverTransform.NONE,ChainedGetBehavior.NONE,List.of(),ResultTransform.NONE);
        assertEquals(ArgTreatment.PASS_BY_VALUE, spec.argTreatment(0));
        assertEquals(ArgTreatment.PASS_BY_VALUE, spec.argTreatment(99));
    }

    @Test
    public void argTreatment_lastEntryRepeatedForExcessPositions() {
        RustMethodSpec spec = new RustMethodSpec("foo","foo",false,
                ReceiverTransform.NONE, ChainedGetBehavior.NONE,
                List.of(ArgTreatment.KEY_BORROW, ArgTreatment.VALUE_UNWRAP),
                ResultTransform.NONE);
        assertEquals(ArgTreatment.KEY_BORROW,   spec.argTreatment(0));
        assertEquals(ArgTreatment.VALUE_UNWRAP, spec.argTreatment(1));
        assertEquals(ArgTreatment.VALUE_UNWRAP, spec.argTreatment(2));
        assertEquals(ArgTreatment.VALUE_UNWRAP, spec.argTreatment(100));
    }

    // ---- Programmatic builder --------------------------------------------------------------

    @Test
    public void builderConstructsWorkingRegistry() {
        RustMethodRegistry r = RustMethodRegistry.builder()
                .type(new RustTypeSpec(Set.of("MyStruct"), RustTypeCategory.STRUCT, "MyStruct", false))
                .method(RustTypeCategory.STRUCT, new RustMethodSpec(
                        "doThing","do_thing",false,
                        ReceiverTransform.NONE,ChainedGetBehavior.NONE,
                        List.of(ArgTreatment.PASS_BY_VALUE),ResultTransform.NONE))
                .globalMethod(new RustMethodSpec(
                        "globalOp","global_op",true,
                        ReceiverTransform.NONE,ChainedGetBehavior.NONE,
                        List.of(),ResultTransform.NONE))
                .build();

        assertTrue(r.isCategory(cn(null, "MyStruct"), RustTypeCategory.STRUCT));
        assertEquals("do_thing", r.resolveMethod(RustTypeCategory.STRUCT, "doThing").rustName);
        assertTrue(r.resolveMethodGlobal("globalOp").mutatesReceiver);
    }

    @Test
    public void builderRegistryIsIsolatedFromJsonContent() {
        RustMethodRegistry r = RustMethodRegistry.builder()
                .type(new RustTypeSpec(Set.of("Foo"), RustTypeCategory.OTHER, "Foo", false))
                .build();
        // A builder-only registry must not contain entries from the JSON file
        assertFalse(r.isCategory(cn(null, "int"), RustTypeCategory.PRIMITIVE));
        assertFalse(r.isCategory(cn("past.util", "HashMap"), RustTypeCategory.MAP));
    }

    // ---- Parity: JSON vs programmatic -------------------------------------------------------

    @Test
    public void jsonAndBuilderAgreeOnContainsKeySpec() {
        RustMethodRegistry jsonReg = json();
        RustMethodRegistry builtReg = RustMethodRegistry.builder()
                .type(new RustTypeSpec(
                        Set.of("past.util.Map","past.util.HashMap","HashMap"),
                        RustTypeCategory.MAP, "HashMap", false))
                .method(RustTypeCategory.MAP, new RustMethodSpec(
                        "containsKey","contains_key",false,
                        ReceiverTransform.NONE,ChainedGetBehavior.NONE,
                        List.of(ArgTreatment.KEY_BORROW),ResultTransform.NONE))
                .build();

        RustMethodSpec fromJson    = jsonReg.resolveMethod(RustTypeCategory.MAP, "containsKey");
        RustMethodSpec fromBuilder = builtReg.resolveMethod(RustTypeCategory.MAP, "containsKey");
        assertNotNull(fromJson);
        assertNotNull(fromBuilder);
        assertEquals(fromJson.rustName,        fromBuilder.rustName);
        assertEquals(fromJson.mutatesReceiver, fromBuilder.mutatesReceiver);
        assertEquals(fromJson.argTreatment(0), fromBuilder.argTreatment(0));
        assertEquals(fromJson.resultTransform, fromBuilder.resultTransform);
    }
}
