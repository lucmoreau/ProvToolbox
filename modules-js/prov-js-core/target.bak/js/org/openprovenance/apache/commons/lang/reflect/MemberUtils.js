/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
var org;
(function (org) {
    var openprovenance;
    (function (openprovenance) {
        var apache;
        (function (apache) {
            var commons;
            (function (commons) {
                var lang;
                (function (lang) {
                    var reflect;
                    (function (reflect) {
                        /**
                         * Contains common code for working with Methods/Constructors, extracted and
                         * refactored from <code>MethodUtils</code> when it was imported from Commons
                         * BeanUtils.
                         *
                         * @author Apache Software Foundation
                         * @author Steve Cohen
                         * @author Matt Benson
                         * @since 2.5
                         * @version $Id: MemberUtils.java 1057013 2011-01-09 20:04:16Z niallp $
                         * @class
                         */
                        class MemberUtils {
                            static __static_initialize() { if (!MemberUtils.__static_initialized) {
                                MemberUtils.__static_initialized = true;
                                MemberUtils.__static_initializer_0();
                            } }
                            static ACCESS_TEST_$LI$() { MemberUtils.__static_initialize(); if (MemberUtils.ACCESS_TEST == null) {
                                MemberUtils.ACCESS_TEST = java.lang.reflect.Modifier.PUBLIC | java.lang.reflect.Modifier.PROTECTED | java.lang.reflect.Modifier.PRIVATE;
                            } return MemberUtils.ACCESS_TEST; }
                            static IS_SYNTHETIC_$LI$() { MemberUtils.__static_initialize(); return MemberUtils.IS_SYNTHETIC; }
                            static __static_initializer_0() {
                                let isSynthetic = null;
                                if (org.openprovenance.apache.commons.lang.SystemUtils.isJavaVersionAtLeast(1.5)) {
                                    try {
                                        isSynthetic = /* getMethod */ ((c, p) => { if (c.prototype.hasOwnProperty(p) && typeof c.prototype[p] == 'function')
                                            return { owner: c, name: p, fn: c.prototype[p] };
                                        else
                                            return null; })("java.lang.reflect.Member", "isSynthetic");
                                    }
                                    catch (e) {
                                    }
                                }
                                MemberUtils.IS_SYNTHETIC = isSynthetic;
                            }
                            static ORDERED_PRIMITIVE_TYPES_$LI$() { MemberUtils.__static_initialize(); if (MemberUtils.ORDERED_PRIMITIVE_TYPES == null) {
                                MemberUtils.ORDERED_PRIMITIVE_TYPES = [javaemul.internal.ByteHelper.TYPE, javaemul.internal.ShortHelper.TYPE, javaemul.internal.CharacterHelper.TYPE, javaemul.internal.IntegerHelper.TYPE, javaemul.internal.LongHelper.TYPE, javaemul.internal.FloatHelper.TYPE, javaemul.internal.DoubleHelper.TYPE];
                            } return MemberUtils.ORDERED_PRIMITIVE_TYPES; }
                            /**
                             * XXX Default access superclass workaround
                             *
                             * When a public class has a default access superclass with public members,
                             * these members are accessible. Calling them from compiled code works fine.
                             * Unfortunately, on some JVMs, using reflection to invoke these members
                             * seems to (wrongly) to prevent access even when the modifer is public.
                             * Calling setAccessible(true) solves the problem but will only work from
                             * sufficiently privileged code. Better workarounds would be gratefully
                             * accepted.
                             * @param {java.lang.reflect.AccessibleObject} o the AccessibleObject to set as accessible
                             */
                            static setAccessibleWorkaround(o) {
                                if (o == null || o.isAccessible()) {
                                    return;
                                }
                                const m = o;
                                if (java.lang.reflect.Modifier.isPublic(m.getModifiers()) && MemberUtils.isPackageAccess(m.getDeclaringClass().getModifiers())) {
                                    try {
                                        o.setAccessible(true);
                                    }
                                    catch (e) {
                                    }
                                }
                            }
                            /**
                             * Learn whether a given set of modifiers implies package access.
                             * @param {number} modifiers to test
                             * @return {boolean} true unless package/protected/private modifier detected
                             */
                            static isPackageAccess(modifiers) {
                                return (modifiers & MemberUtils.ACCESS_TEST_$LI$()) === 0;
                            }
                            /**
                             * Check a Member for basic accessibility.
                             * @param {*} m Member to check
                             * @return {boolean} true if <code>m</code> is accessible
                             */
                            static isAccessible(m) {
                                return m != null && java.lang.reflect.Modifier.isPublic(m.getModifiers()) && !MemberUtils.isSynthetic(m);
                            }
                            /**
                             * Try to learn whether a given member, on JDK >= 1.5, is synthetic.
                             * @param {*} m Member to check
                             * @return {boolean} true if <code>m</code> was introduced by the compiler.
                             */
                            static isSynthetic(m) {
                                if (MemberUtils.IS_SYNTHETIC_$LI$() != null) {
                                    try {
                                        return /* booleanValue */ MemberUtils.IS_SYNTHETIC_$LI$().fn.apply(m, [null]);
                                    }
                                    catch (e) {
                                    }
                                }
                                return false;
                            }
                            /**
                             * Compare the relative fitness of two sets of parameter types in terms of
                             * matching a third set of runtime parameter types, such that a list ordered
                             * by the results of the comparison would return the best match first
                             * (least).
                             *
                             * @param {java.lang.Class[]} left the "left" parameter set
                             * @param {java.lang.Class[]} right the "right" parameter set
                             * @param {java.lang.Class[]} actual the runtime parameter types to match against
                             * <code>left</code>/<code>right</code>
                             * @return {number} int consistent with <code>compare</code> semantics
                             */
                            static compareParameterTypes(left, right, actual) {
                                const leftCost = MemberUtils.getTotalTransformationCost(actual, left);
                                const rightCost = MemberUtils.getTotalTransformationCost(actual, right);
                                return leftCost < rightCost ? -1 : rightCost < leftCost ? 1 : 0;
                            }
                            /**
                             * Returns the sum of the object transformation cost for each class in the
                             * source argument list.
                             * @param {java.lang.Class[]} srcArgs The source arguments
                             * @param {java.lang.Class[]} destArgs The destination arguments
                             * @return {number} The total transformation cost
                             * @private
                             */
                            /*private*/ static getTotalTransformationCost(srcArgs, destArgs) {
                                let totalCost = 0.0;
                                for (let i = 0; i < srcArgs.length; i++) {
                                    {
                                        let srcClass;
                                        let destClass;
                                        srcClass = srcArgs[i];
                                        destClass = destArgs[i];
                                        totalCost += MemberUtils.getObjectTransformationCost(srcClass, destClass);
                                    }
                                    ;
                                }
                                return totalCost;
                            }
                            /**
                             * Gets the number of steps required needed to turn the source class into
                             * the destination class. This represents the number of steps in the object
                             * hierarchy graph.
                             * @param {*} srcClass The source class
                             * @param {*} destClass The destination class
                             * @return {number} The cost of transforming an object
                             * @private
                             */
                            /*private*/ static getObjectTransformationCost(srcClass, destClass) {
                                if ( /* isPrimitive */(destClass === '__erasedPrimitiveType__')) {
                                    return MemberUtils.getPrimitivePromotionCost(srcClass, destClass);
                                }
                                let cost = 0.0;
                                while ((srcClass != null && !((o1, o2) => o1 && o1.equals ? o1.equals(o2) : o1 === o2)(destClass, srcClass))) {
                                    {
                                        if (destClass.isInterface() && org.openprovenance.apache.commons.lang.ClassUtils.isAssignable$java_lang_Class$java_lang_Class(srcClass, destClass)) {
                                            cost += 0.25;
                                            break;
                                        }
                                        cost++;
                                        srcClass = srcClass.getSuperclass();
                                    }
                                }
                                ;
                                if (srcClass == null) {
                                    cost += 1.5;
                                }
                                return cost;
                            }
                            /**
                             * Get the number of steps required to promote a primitive number to another
                             * type.
                             * @param {*} srcClass the (primitive) source class
                             * @param {*} destClass the (primitive) destination class
                             * @return {number} The cost of promoting the primitive
                             * @private
                             */
                            /*private*/ static getPrimitivePromotionCost(srcClass, destClass) {
                                let cost = 0.0;
                                let cls = srcClass;
                                if (!(cls === '__erasedPrimitiveType__')) {
                                    cost += 0.1;
                                    cls = org.openprovenance.apache.commons.lang.ClassUtils.wrapperToPrimitive(cls);
                                }
                                for (let i = 0; cls !== destClass && i < MemberUtils.ORDERED_PRIMITIVE_TYPES_$LI$().length; i++) {
                                    {
                                        if (cls === MemberUtils.ORDERED_PRIMITIVE_TYPES_$LI$()[i]) {
                                            cost += 0.1;
                                            if (i < MemberUtils.ORDERED_PRIMITIVE_TYPES_$LI$().length - 1) {
                                                cls = MemberUtils.ORDERED_PRIMITIVE_TYPES_$LI$()[i + 1];
                                            }
                                        }
                                    }
                                    ;
                                }
                                return cost;
                            }
                        }
                        MemberUtils.__static_initialized = false;
                        reflect.MemberUtils = MemberUtils;
                        MemberUtils["__class"] = "org.openprovenance.apache.commons.lang.reflect.MemberUtils";
                    })(reflect = lang.reflect || (lang.reflect = {}));
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
org.openprovenance.apache.commons.lang.reflect.MemberUtils.__static_initialize();
