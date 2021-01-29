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
                    /**
                     * <p>
                     * SystemUtils instances should NOT be constructed in standard programming. Instead, the class should be used as
                     * <code>SystemUtils.FILE_SEPARATOR</code>.
                     * </p>
                     *
                     * <p>
                     * This constructor is public to permit tools that require a JavaBean instance to operate.
                     * </p>
                     * @class
                     * @author Apache Software Foundation
                     */
                    class SystemUtils {
                        constructor() {
                        }
                        static AWT_TOOLKIT_$LI$() { if (SystemUtils.AWT_TOOLKIT == null) {
                            SystemUtils.AWT_TOOLKIT = SystemUtils.getSystemProperty("awt.toolkit");
                        } return SystemUtils.AWT_TOOLKIT; }
                        static FILE_ENCODING_$LI$() { if (SystemUtils.FILE_ENCODING == null) {
                            SystemUtils.FILE_ENCODING = SystemUtils.getSystemProperty("file.encoding");
                        } return SystemUtils.FILE_ENCODING; }
                        static FILE_SEPARATOR_$LI$() { if (SystemUtils.FILE_SEPARATOR == null) {
                            SystemUtils.FILE_SEPARATOR = SystemUtils.getSystemProperty("file.separator");
                        } return SystemUtils.FILE_SEPARATOR; }
                        static JAVA_AWT_FONTS_$LI$() { if (SystemUtils.JAVA_AWT_FONTS == null) {
                            SystemUtils.JAVA_AWT_FONTS = SystemUtils.getSystemProperty("java.awt.fonts");
                        } return SystemUtils.JAVA_AWT_FONTS; }
                        static JAVA_AWT_GRAPHICSENV_$LI$() { if (SystemUtils.JAVA_AWT_GRAPHICSENV == null) {
                            SystemUtils.JAVA_AWT_GRAPHICSENV = SystemUtils.getSystemProperty("java.awt.graphicsenv");
                        } return SystemUtils.JAVA_AWT_GRAPHICSENV; }
                        static JAVA_AWT_HEADLESS_$LI$() { if (SystemUtils.JAVA_AWT_HEADLESS == null) {
                            SystemUtils.JAVA_AWT_HEADLESS = SystemUtils.getSystemProperty("java.awt.headless");
                        } return SystemUtils.JAVA_AWT_HEADLESS; }
                        static JAVA_AWT_PRINTERJOB_$LI$() { if (SystemUtils.JAVA_AWT_PRINTERJOB == null) {
                            SystemUtils.JAVA_AWT_PRINTERJOB = SystemUtils.getSystemProperty("java.awt.printerjob");
                        } return SystemUtils.JAVA_AWT_PRINTERJOB; }
                        static JAVA_CLASS_PATH_$LI$() { if (SystemUtils.JAVA_CLASS_PATH == null) {
                            SystemUtils.JAVA_CLASS_PATH = SystemUtils.getSystemProperty("java.class.path");
                        } return SystemUtils.JAVA_CLASS_PATH; }
                        static JAVA_CLASS_VERSION_$LI$() { if (SystemUtils.JAVA_CLASS_VERSION == null) {
                            SystemUtils.JAVA_CLASS_VERSION = SystemUtils.getSystemProperty("java.class.version");
                        } return SystemUtils.JAVA_CLASS_VERSION; }
                        static JAVA_COMPILER_$LI$() { if (SystemUtils.JAVA_COMPILER == null) {
                            SystemUtils.JAVA_COMPILER = SystemUtils.getSystemProperty("java.compiler");
                        } return SystemUtils.JAVA_COMPILER; }
                        static JAVA_ENDORSED_DIRS_$LI$() { if (SystemUtils.JAVA_ENDORSED_DIRS == null) {
                            SystemUtils.JAVA_ENDORSED_DIRS = SystemUtils.getSystemProperty("java.endorsed.dirs");
                        } return SystemUtils.JAVA_ENDORSED_DIRS; }
                        static JAVA_EXT_DIRS_$LI$() { if (SystemUtils.JAVA_EXT_DIRS == null) {
                            SystemUtils.JAVA_EXT_DIRS = SystemUtils.getSystemProperty("java.ext.dirs");
                        } return SystemUtils.JAVA_EXT_DIRS; }
                        static JAVA_HOME_$LI$() { if (SystemUtils.JAVA_HOME == null) {
                            SystemUtils.JAVA_HOME = SystemUtils.getSystemProperty(SystemUtils.JAVA_HOME_KEY);
                        } return SystemUtils.JAVA_HOME; }
                        static JAVA_IO_TMPDIR_$LI$() { if (SystemUtils.JAVA_IO_TMPDIR == null) {
                            SystemUtils.JAVA_IO_TMPDIR = SystemUtils.getSystemProperty(SystemUtils.JAVA_IO_TMPDIR_KEY);
                        } return SystemUtils.JAVA_IO_TMPDIR; }
                        static JAVA_LIBRARY_PATH_$LI$() { if (SystemUtils.JAVA_LIBRARY_PATH == null) {
                            SystemUtils.JAVA_LIBRARY_PATH = SystemUtils.getSystemProperty("java.library.path");
                        } return SystemUtils.JAVA_LIBRARY_PATH; }
                        static JAVA_RUNTIME_NAME_$LI$() { if (SystemUtils.JAVA_RUNTIME_NAME == null) {
                            SystemUtils.JAVA_RUNTIME_NAME = SystemUtils.getSystemProperty("java.runtime.name");
                        } return SystemUtils.JAVA_RUNTIME_NAME; }
                        static JAVA_RUNTIME_VERSION_$LI$() { if (SystemUtils.JAVA_RUNTIME_VERSION == null) {
                            SystemUtils.JAVA_RUNTIME_VERSION = SystemUtils.getSystemProperty("java.runtime.version");
                        } return SystemUtils.JAVA_RUNTIME_VERSION; }
                        static JAVA_SPECIFICATION_NAME_$LI$() { if (SystemUtils.JAVA_SPECIFICATION_NAME == null) {
                            SystemUtils.JAVA_SPECIFICATION_NAME = SystemUtils.getSystemProperty("java.specification.name");
                        } return SystemUtils.JAVA_SPECIFICATION_NAME; }
                        static JAVA_SPECIFICATION_VENDOR_$LI$() { if (SystemUtils.JAVA_SPECIFICATION_VENDOR == null) {
                            SystemUtils.JAVA_SPECIFICATION_VENDOR = SystemUtils.getSystemProperty("java.specification.vendor");
                        } return SystemUtils.JAVA_SPECIFICATION_VENDOR; }
                        static JAVA_SPECIFICATION_VERSION_$LI$() { if (SystemUtils.JAVA_SPECIFICATION_VERSION == null) {
                            SystemUtils.JAVA_SPECIFICATION_VERSION = SystemUtils.getSystemProperty("java.specification.version");
                        } return SystemUtils.JAVA_SPECIFICATION_VERSION; }
                        static JAVA_UTIL_PREFS_PREFERENCES_FACTORY_$LI$() { if (SystemUtils.JAVA_UTIL_PREFS_PREFERENCES_FACTORY == null) {
                            SystemUtils.JAVA_UTIL_PREFS_PREFERENCES_FACTORY = SystemUtils.getSystemProperty("java.util.prefs.PreferencesFactory");
                        } return SystemUtils.JAVA_UTIL_PREFS_PREFERENCES_FACTORY; }
                        static JAVA_VENDOR_$LI$() { if (SystemUtils.JAVA_VENDOR == null) {
                            SystemUtils.JAVA_VENDOR = SystemUtils.getSystemProperty("java.vendor");
                        } return SystemUtils.JAVA_VENDOR; }
                        static JAVA_VENDOR_URL_$LI$() { if (SystemUtils.JAVA_VENDOR_URL == null) {
                            SystemUtils.JAVA_VENDOR_URL = SystemUtils.getSystemProperty("java.vendor.url");
                        } return SystemUtils.JAVA_VENDOR_URL; }
                        static JAVA_VERSION_$LI$() { if (SystemUtils.JAVA_VERSION == null) {
                            SystemUtils.JAVA_VERSION = SystemUtils.getSystemProperty("java.version");
                        } return SystemUtils.JAVA_VERSION; }
                        static JAVA_VM_INFO_$LI$() { if (SystemUtils.JAVA_VM_INFO == null) {
                            SystemUtils.JAVA_VM_INFO = SystemUtils.getSystemProperty("java.vm.info");
                        } return SystemUtils.JAVA_VM_INFO; }
                        static JAVA_VM_NAME_$LI$() { if (SystemUtils.JAVA_VM_NAME == null) {
                            SystemUtils.JAVA_VM_NAME = SystemUtils.getSystemProperty("java.vm.name");
                        } return SystemUtils.JAVA_VM_NAME; }
                        static JAVA_VM_SPECIFICATION_NAME_$LI$() { if (SystemUtils.JAVA_VM_SPECIFICATION_NAME == null) {
                            SystemUtils.JAVA_VM_SPECIFICATION_NAME = SystemUtils.getSystemProperty("java.vm.specification.name");
                        } return SystemUtils.JAVA_VM_SPECIFICATION_NAME; }
                        static JAVA_VM_SPECIFICATION_VENDOR_$LI$() { if (SystemUtils.JAVA_VM_SPECIFICATION_VENDOR == null) {
                            SystemUtils.JAVA_VM_SPECIFICATION_VENDOR = SystemUtils.getSystemProperty("java.vm.specification.vendor");
                        } return SystemUtils.JAVA_VM_SPECIFICATION_VENDOR; }
                        static JAVA_VM_SPECIFICATION_VERSION_$LI$() { if (SystemUtils.JAVA_VM_SPECIFICATION_VERSION == null) {
                            SystemUtils.JAVA_VM_SPECIFICATION_VERSION = SystemUtils.getSystemProperty("java.vm.specification.version");
                        } return SystemUtils.JAVA_VM_SPECIFICATION_VERSION; }
                        static JAVA_VM_VENDOR_$LI$() { if (SystemUtils.JAVA_VM_VENDOR == null) {
                            SystemUtils.JAVA_VM_VENDOR = SystemUtils.getSystemProperty("java.vm.vendor");
                        } return SystemUtils.JAVA_VM_VENDOR; }
                        static JAVA_VM_VERSION_$LI$() { if (SystemUtils.JAVA_VM_VERSION == null) {
                            SystemUtils.JAVA_VM_VERSION = SystemUtils.getSystemProperty("java.vm.version");
                        } return SystemUtils.JAVA_VM_VERSION; }
                        static LINE_SEPARATOR_$LI$() { if (SystemUtils.LINE_SEPARATOR == null) {
                            SystemUtils.LINE_SEPARATOR = SystemUtils.getSystemProperty("line.separator");
                        } return SystemUtils.LINE_SEPARATOR; }
                        static OS_ARCH_$LI$() { if (SystemUtils.OS_ARCH == null) {
                            SystemUtils.OS_ARCH = SystemUtils.getSystemProperty("os.arch");
                        } return SystemUtils.OS_ARCH; }
                        static OS_NAME_$LI$() { if (SystemUtils.OS_NAME == null) {
                            SystemUtils.OS_NAME = SystemUtils.getSystemProperty("os.name");
                        } return SystemUtils.OS_NAME; }
                        static OS_VERSION_$LI$() { if (SystemUtils.OS_VERSION == null) {
                            SystemUtils.OS_VERSION = SystemUtils.getSystemProperty("os.version");
                        } return SystemUtils.OS_VERSION; }
                        static PATH_SEPARATOR_$LI$() { if (SystemUtils.PATH_SEPARATOR == null) {
                            SystemUtils.PATH_SEPARATOR = SystemUtils.getSystemProperty("path.separator");
                        } return SystemUtils.PATH_SEPARATOR; }
                        static USER_COUNTRY_$LI$() { if (SystemUtils.USER_COUNTRY == null) {
                            SystemUtils.USER_COUNTRY = SystemUtils.getSystemProperty("user.country") == null ? SystemUtils.getSystemProperty("user.region") : SystemUtils.getSystemProperty("user.country");
                        } return SystemUtils.USER_COUNTRY; }
                        static USER_DIR_$LI$() { if (SystemUtils.USER_DIR == null) {
                            SystemUtils.USER_DIR = SystemUtils.getSystemProperty(SystemUtils.USER_DIR_KEY);
                        } return SystemUtils.USER_DIR; }
                        static USER_HOME_$LI$() { if (SystemUtils.USER_HOME == null) {
                            SystemUtils.USER_HOME = SystemUtils.getSystemProperty(SystemUtils.USER_HOME_KEY);
                        } return SystemUtils.USER_HOME; }
                        static USER_LANGUAGE_$LI$() { if (SystemUtils.USER_LANGUAGE == null) {
                            SystemUtils.USER_LANGUAGE = SystemUtils.getSystemProperty("user.language");
                        } return SystemUtils.USER_LANGUAGE; }
                        static USER_NAME_$LI$() { if (SystemUtils.USER_NAME == null) {
                            SystemUtils.USER_NAME = SystemUtils.getSystemProperty("user.name");
                        } return SystemUtils.USER_NAME; }
                        static USER_TIMEZONE_$LI$() { if (SystemUtils.USER_TIMEZONE == null) {
                            SystemUtils.USER_TIMEZONE = SystemUtils.getSystemProperty("user.timezone");
                        } return SystemUtils.USER_TIMEZONE; }
                        static JAVA_VERSION_TRIMMED_$LI$() { if (SystemUtils.JAVA_VERSION_TRIMMED == null) {
                            SystemUtils.JAVA_VERSION_TRIMMED = SystemUtils.getJavaVersionTrimmed();
                        } return SystemUtils.JAVA_VERSION_TRIMMED; }
                        static JAVA_VERSION_FLOAT_$LI$() { if (SystemUtils.JAVA_VERSION_FLOAT == null) {
                            SystemUtils.JAVA_VERSION_FLOAT = SystemUtils.getJavaVersionAsFloat();
                        } return SystemUtils.JAVA_VERSION_FLOAT; }
                        static JAVA_VERSION_INT_$LI$() { if (SystemUtils.JAVA_VERSION_INT == null) {
                            SystemUtils.JAVA_VERSION_INT = SystemUtils.getJavaVersionAsInt();
                        } return SystemUtils.JAVA_VERSION_INT; }
                        static IS_JAVA_1_1_$LI$() { if (SystemUtils.IS_JAVA_1_1 == null) {
                            SystemUtils.IS_JAVA_1_1 = SystemUtils.getJavaVersionMatches("1.1");
                        } return SystemUtils.IS_JAVA_1_1; }
                        static IS_JAVA_1_2_$LI$() { if (SystemUtils.IS_JAVA_1_2 == null) {
                            SystemUtils.IS_JAVA_1_2 = SystemUtils.getJavaVersionMatches("1.2");
                        } return SystemUtils.IS_JAVA_1_2; }
                        static IS_JAVA_1_3_$LI$() { if (SystemUtils.IS_JAVA_1_3 == null) {
                            SystemUtils.IS_JAVA_1_3 = SystemUtils.getJavaVersionMatches("1.3");
                        } return SystemUtils.IS_JAVA_1_3; }
                        static IS_JAVA_1_4_$LI$() { if (SystemUtils.IS_JAVA_1_4 == null) {
                            SystemUtils.IS_JAVA_1_4 = SystemUtils.getJavaVersionMatches("1.4");
                        } return SystemUtils.IS_JAVA_1_4; }
                        static IS_JAVA_1_5_$LI$() { if (SystemUtils.IS_JAVA_1_5 == null) {
                            SystemUtils.IS_JAVA_1_5 = SystemUtils.getJavaVersionMatches("1.5");
                        } return SystemUtils.IS_JAVA_1_5; }
                        static IS_JAVA_1_6_$LI$() { if (SystemUtils.IS_JAVA_1_6 == null) {
                            SystemUtils.IS_JAVA_1_6 = SystemUtils.getJavaVersionMatches("1.6");
                        } return SystemUtils.IS_JAVA_1_6; }
                        static IS_JAVA_1_7_$LI$() { if (SystemUtils.IS_JAVA_1_7 == null) {
                            SystemUtils.IS_JAVA_1_7 = SystemUtils.getJavaVersionMatches("1.7");
                        } return SystemUtils.IS_JAVA_1_7; }
                        static IS_OS_AIX_$LI$() { if (SystemUtils.IS_OS_AIX == null) {
                            SystemUtils.IS_OS_AIX = SystemUtils.getOSMatchesName("AIX");
                        } return SystemUtils.IS_OS_AIX; }
                        static IS_OS_HP_UX_$LI$() { if (SystemUtils.IS_OS_HP_UX == null) {
                            SystemUtils.IS_OS_HP_UX = SystemUtils.getOSMatchesName("HP-UX");
                        } return SystemUtils.IS_OS_HP_UX; }
                        static IS_OS_IRIX_$LI$() { if (SystemUtils.IS_OS_IRIX == null) {
                            SystemUtils.IS_OS_IRIX = SystemUtils.getOSMatchesName("Irix");
                        } return SystemUtils.IS_OS_IRIX; }
                        static IS_OS_LINUX_$LI$() { if (SystemUtils.IS_OS_LINUX == null) {
                            SystemUtils.IS_OS_LINUX = SystemUtils.getOSMatchesName("Linux") || SystemUtils.getOSMatchesName("LINUX");
                        } return SystemUtils.IS_OS_LINUX; }
                        static IS_OS_MAC_$LI$() { if (SystemUtils.IS_OS_MAC == null) {
                            SystemUtils.IS_OS_MAC = SystemUtils.getOSMatchesName("Mac");
                        } return SystemUtils.IS_OS_MAC; }
                        static IS_OS_MAC_OSX_$LI$() { if (SystemUtils.IS_OS_MAC_OSX == null) {
                            SystemUtils.IS_OS_MAC_OSX = SystemUtils.getOSMatchesName("Mac OS X");
                        } return SystemUtils.IS_OS_MAC_OSX; }
                        static IS_OS_OS2_$LI$() { if (SystemUtils.IS_OS_OS2 == null) {
                            SystemUtils.IS_OS_OS2 = SystemUtils.getOSMatchesName("OS/2");
                        } return SystemUtils.IS_OS_OS2; }
                        static IS_OS_SOLARIS_$LI$() { if (SystemUtils.IS_OS_SOLARIS == null) {
                            SystemUtils.IS_OS_SOLARIS = SystemUtils.getOSMatchesName("Solaris");
                        } return SystemUtils.IS_OS_SOLARIS; }
                        static IS_OS_SUN_OS_$LI$() { if (SystemUtils.IS_OS_SUN_OS == null) {
                            SystemUtils.IS_OS_SUN_OS = SystemUtils.getOSMatchesName("SunOS");
                        } return SystemUtils.IS_OS_SUN_OS; }
                        static IS_OS_UNIX_$LI$() { if (SystemUtils.IS_OS_UNIX == null) {
                            SystemUtils.IS_OS_UNIX = SystemUtils.IS_OS_AIX_$LI$() || SystemUtils.IS_OS_HP_UX_$LI$() || SystemUtils.IS_OS_IRIX_$LI$() || SystemUtils.IS_OS_LINUX_$LI$() || SystemUtils.IS_OS_MAC_OSX_$LI$() || SystemUtils.IS_OS_SOLARIS_$LI$() || SystemUtils.IS_OS_SUN_OS_$LI$();
                        } return SystemUtils.IS_OS_UNIX; }
                        static IS_OS_WINDOWS_$LI$() { if (SystemUtils.IS_OS_WINDOWS == null) {
                            SystemUtils.IS_OS_WINDOWS = SystemUtils.getOSMatchesName(SystemUtils.OS_NAME_WINDOWS_PREFIX);
                        } return SystemUtils.IS_OS_WINDOWS; }
                        static IS_OS_WINDOWS_2000_$LI$() { if (SystemUtils.IS_OS_WINDOWS_2000 == null) {
                            SystemUtils.IS_OS_WINDOWS_2000 = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "5.0");
                        } return SystemUtils.IS_OS_WINDOWS_2000; }
                        static IS_OS_WINDOWS_95_$LI$() { if (SystemUtils.IS_OS_WINDOWS_95 == null) {
                            SystemUtils.IS_OS_WINDOWS_95 = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX + " 9", "4.0");
                        } return SystemUtils.IS_OS_WINDOWS_95; }
                        static IS_OS_WINDOWS_98_$LI$() { if (SystemUtils.IS_OS_WINDOWS_98 == null) {
                            SystemUtils.IS_OS_WINDOWS_98 = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX + " 9", "4.1");
                        } return SystemUtils.IS_OS_WINDOWS_98; }
                        static IS_OS_WINDOWS_ME_$LI$() { if (SystemUtils.IS_OS_WINDOWS_ME == null) {
                            SystemUtils.IS_OS_WINDOWS_ME = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "4.9");
                        } return SystemUtils.IS_OS_WINDOWS_ME; }
                        static IS_OS_WINDOWS_NT_$LI$() { if (SystemUtils.IS_OS_WINDOWS_NT == null) {
                            SystemUtils.IS_OS_WINDOWS_NT = SystemUtils.getOSMatchesName(SystemUtils.OS_NAME_WINDOWS_PREFIX + " NT");
                        } return SystemUtils.IS_OS_WINDOWS_NT; }
                        static IS_OS_WINDOWS_XP_$LI$() { if (SystemUtils.IS_OS_WINDOWS_XP == null) {
                            SystemUtils.IS_OS_WINDOWS_XP = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "5.1");
                        } return SystemUtils.IS_OS_WINDOWS_XP; }
                        static IS_OS_WINDOWS_VISTA_$LI$() { if (SystemUtils.IS_OS_WINDOWS_VISTA == null) {
                            SystemUtils.IS_OS_WINDOWS_VISTA = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "6.0");
                        } return SystemUtils.IS_OS_WINDOWS_VISTA; }
                        static IS_OS_WINDOWS_7_$LI$() { if (SystemUtils.IS_OS_WINDOWS_7 == null) {
                            SystemUtils.IS_OS_WINDOWS_7 = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "6.1");
                        } return SystemUtils.IS_OS_WINDOWS_7; }
                        /**
                         * <p>
                         * Gets the Java home directory as a <code>File</code>.
                         * </p>
                         *
                         * @return {java.io.File} a directory
                         * @throws SecurityException if a security manager exists and its
                         * <code>checkPropertyAccess</code> method doesn't allow access to the specified system property.
                         * @see System#getProperty(String)
                         * @since 2.1
                         */
                        static getJavaHome() {
                            return new java.io.File(java.lang.System.getProperty(SystemUtils.JAVA_HOME_KEY));
                        }
                        /**
                         * <p>
                         * Gets the Java IO temporary directory as a <code>File</code>.
                         * </p>
                         *
                         * @return {java.io.File} a directory
                         * @throws SecurityException if a security manager exists and its
                         * <code>checkPropertyAccess</code> method doesn't allow access to the specified system
                         * property.
                         * @see System#getProperty(String)
                         * @since 2.1
                         */
                        static getJavaIoTmpDir() {
                            return new java.io.File(java.lang.System.getProperty(SystemUtils.JAVA_IO_TMPDIR_KEY));
                        }
                        /**
                         * <p>Gets the Java version number as a <code>float</code>.</p>
                         *
                         * <p>Example return values:</p>
                         * <ul>
                         * <li><code>1.2f</code> for JDK 1.2
                         * <li><code>1.31f</code> for JDK 1.3.1
                         * </ul>
                         *
                         * @return {number} the version, for example 1.31f for JDK 1.3.1
                         * @deprecated Use {@link #JAVA_VERSION_FLOAT} instead.
                         * Method will be removed in Commons Lang 3.0.
                         */
                        static getJavaVersion() {
                            return SystemUtils.JAVA_VERSION_FLOAT_$LI$();
                        }
                        /**
                         * <p>
                         * Gets the Java version number as a <code>float</code>.
                         * </p>
                         *
                         * <p>
                         * Example return values:
                         * </p>
                         * <ul>
                         * <li><code>1.2f</code> for Java 1.2</li>
                         * <li><code>1.31f</code> for Java 1.3.1</li>
                         * <li><code>1.6f</code> for Java 1.6.0_20</li>
                         * </ul>
                         *
                         * <p>
                         * Patch releases are not reported.
                         * </p>
                         *
                         * @return {number} the version, for example 1.31f for Java 1.3.1
                         * @private
                         */
                        /*private*/ static getJavaVersionAsFloat() {
                            return SystemUtils.toVersionFloat(SystemUtils.toJavaVersionIntArray$java_lang_String$int(SystemUtils.JAVA_VERSION_$LI$(), SystemUtils.JAVA_VERSION_TRIM_SIZE));
                        }
                        /**
                         * <p>
                         * Gets the Java version number as an <code>int</code>.
                         * </p>
                         *
                         * <p>
                         * Example return values:
                         * </p>
                         * <ul>
                         * <li><code>120</code> for Java 1.2</li>
                         * <li><code>131</code> for Java 1.3.1</li>
                         * <li><code>160</code> for Java 1.6.0_20</li>
                         * </ul>
                         *
                         * <p>
                         * Patch releases are not reported.
                         * </p>
                         *
                         * @return {number} the version, for example 131 for Java 1.3.1
                         * @private
                         */
                        /*private*/ static getJavaVersionAsInt() {
                            return SystemUtils.toVersionInt(SystemUtils.toJavaVersionIntArray$java_lang_String$int(SystemUtils.JAVA_VERSION_$LI$(), SystemUtils.JAVA_VERSION_TRIM_SIZE));
                        }
                        /**
                         * <p>
                         * Decides if the Java version matches.
                         * </p>
                         *
                         * @param {string} versionPrefix
                         * the prefix for the java version
                         * @return {boolean} true if matches, or false if not or can't determine
                         * @private
                         */
                        /*private*/ static getJavaVersionMatches(versionPrefix) {
                            return SystemUtils.isJavaVersionMatch(SystemUtils.JAVA_VERSION_TRIMMED_$LI$(), versionPrefix);
                        }
                        /**
                         * Trims the text of the java version to start with numbers.
                         *
                         * @return {string} the trimmed java version
                         * @private
                         */
                        /*private*/ static getJavaVersionTrimmed() {
                            if (SystemUtils.JAVA_VERSION_$LI$() != null) {
                                for (let i = 0; i < SystemUtils.JAVA_VERSION_$LI$().length; i++) {
                                    {
                                        const ch = SystemUtils.JAVA_VERSION_$LI$().charAt(i);
                                        if ((c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) >= '0'.charCodeAt(0) && (c => c.charCodeAt == null ? c : c.charCodeAt(0))(ch) <= '9'.charCodeAt(0)) {
                                            return SystemUtils.JAVA_VERSION_$LI$().substring(i);
                                        }
                                    }
                                    ;
                                }
                            }
                            return null;
                        }
                        /**
                         * Decides if the operating system matches.
                         *
                         * @param {string} osNamePrefix
                         * the prefix for the os name
                         * @param {string} osVersionPrefix
                         * the prefix for the version
                         * @return {boolean} true if matches, or false if not or can't determine
                         * @private
                         */
                        /*private*/ static getOSMatches(osNamePrefix, osVersionPrefix) {
                            return SystemUtils.isOSMatch(SystemUtils.OS_NAME_$LI$(), SystemUtils.OS_VERSION_$LI$(), osNamePrefix, osVersionPrefix);
                        }
                        /**
                         * Decides if the operating system matches.
                         *
                         * @param {string} osNamePrefix
                         * the prefix for the os name
                         * @return {boolean} true if matches, or false if not or can't determine
                         * @private
                         */
                        /*private*/ static getOSMatchesName(osNamePrefix) {
                            return SystemUtils.isOSNameMatch(SystemUtils.OS_NAME_$LI$(), osNamePrefix);
                        }
                        /**
                         * <p>
                         * Gets a System property, defaulting to <code>null</code> if the property cannot be read.
                         * </p>
                         *
                         * <p>
                         * If a <code>SecurityException</code> is caught, the return value is <code>null</code> and a message is written to
                         * <code>System.err</code>.
                         * </p>
                         *
                         * @param {string} property
                         * the system property name
                         * @return {string} the system property value or <code>null</code> if a security problem occurs
                         * @private
                         */
                        /*private*/ static getSystemProperty(property) {
                            try {
                                return java.lang.System.getProperty(property);
                            }
                            catch (ex) {
                                console.error("Caught a SecurityException reading the system property \'" + property + "\'; the SystemUtils property value will default to null.");
                                return null;
                            }
                        }
                        /**
                         * <p>
                         * Gets the user directory as a <code>File</code>.
                         * </p>
                         *
                         * @return {java.io.File} a directory
                         * @throws SecurityException if a security manager exists and its
                         * <code>checkPropertyAccess</code> method doesn't allow access to the specified system property.
                         * @see System#getProperty(String)
                         * @since 2.1
                         */
                        static getUserDir() {
                            return new java.io.File(java.lang.System.getProperty(SystemUtils.USER_DIR_KEY));
                        }
                        /**
                         * <p>
                         * Gets the user home directory as a <code>File</code>.
                         * </p>
                         *
                         * @return {java.io.File} a directory
                         * @throws SecurityException if a security manager exists and its
                         * <code>checkPropertyAccess</code> method doesn't allow access to the specified system property.
                         * @see System#getProperty(String)
                         * @since 2.1
                         */
                        static getUserHome() {
                            return new java.io.File(java.lang.System.getProperty(SystemUtils.USER_HOME_KEY));
                        }
                        /**
                         * Returns whether the {@link #JAVA_AWT_HEADLESS} value is <code>true</code>.
                         *
                         * @return {boolean} <code>true</code> if <code>JAVA_AWT_HEADLESS</code> is <code>"true"</code>, <code>false</code> otherwise.
                         *
                         * @see #JAVA_AWT_HEADLESS
                         * @since 2.1
                         * @since Java 1.4
                         */
                        static isJavaAwtHeadless() {
                            return SystemUtils.JAVA_AWT_HEADLESS_$LI$() != null ? SystemUtils.JAVA_AWT_HEADLESS_$LI$() === true.toString() : false;
                        }
                        static isJavaVersionAtLeast$float(requiredVersion) {
                            return SystemUtils.JAVA_VERSION_FLOAT_$LI$() >= requiredVersion;
                        }
                        static isJavaVersionAtLeast$int(requiredVersion) {
                            return SystemUtils.JAVA_VERSION_INT_$LI$() >= requiredVersion;
                        }
                        /**
                         * <p>
                         * Is the Java version at least the requested version.
                         * </p>
                         *
                         * <p>
                         * Example input:
                         * </p>
                         * <ul>
                         * <li><code>120</code> to test for Java 1.2 or greater</li>
                         * <li><code>131</code> to test for Java 1.3.1 or greater</li>
                         * </ul>
                         *
                         * @param {number} requiredVersion
                         * the required version, for example 131
                         * @return {boolean} <code>true</code> if the actual version is equal or greater than the required version
                         * @since 2.0
                         */
                        static isJavaVersionAtLeast(requiredVersion) {
                            if (((typeof requiredVersion === 'number') || requiredVersion === null)) {
                                return org.openprovenance.apache.commons.lang.SystemUtils.isJavaVersionAtLeast$int(requiredVersion);
                            }
                            else if (((typeof requiredVersion === 'number') || requiredVersion === null)) {
                                return org.openprovenance.apache.commons.lang.SystemUtils.isJavaVersionAtLeast$float(requiredVersion);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>
                         * Decides if the Java version matches.
                         * </p>
                         * <p>
                         * This method is package private instead of private to support unit test invocation.
                         * </p>
                         *
                         * @param {string} version
                         * the actual Java version
                         * @param {string} versionPrefix
                         * the prefix for the expected Java version
                         * @return {boolean} true if matches, or false if not or can't determine
                         */
                        static isJavaVersionMatch(version, versionPrefix) {
                            if (version == null) {
                                return false;
                            }
                            return /* startsWith */ ((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(version, versionPrefix);
                        }
                        /**
                         * Decides if the operating system matches.
                         * <p>
                         * This method is package private instead of private to support unit test invocation.
                         * </p>
                         *
                         * @param {string} osName
                         * the actual OS name
                         * @param {string} osVersion
                         * the actual OS version
                         * @param {string} osNamePrefix
                         * the prefix for the expected OS name
                         * @param {string} osVersionPrefix
                         * the prefix for the expected OS version
                         * @return {boolean} true if matches, or false if not or can't determine
                         */
                        static isOSMatch(osName, osVersion, osNamePrefix, osVersionPrefix) {
                            if (osName == null || osVersion == null) {
                                return false;
                            }
                            return /* startsWith */ ((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(osName, osNamePrefix) && /* startsWith */ ((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(osVersion, osVersionPrefix);
                        }
                        /**
                         * Decides if the operating system matches.
                         * <p>
                         * This method is package private instead of private to support unit test invocation.
                         * </p>
                         *
                         * @param {string} osName
                         * the actual OS name
                         * @param {string} osNamePrefix
                         * the prefix for the expected OS name
                         * @return {boolean} true if matches, or false if not or can't determine
                         */
                        static isOSNameMatch(osName, osNamePrefix) {
                            if (osName == null) {
                                return false;
                            }
                            return /* startsWith */ ((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(osName, osNamePrefix);
                        }
                        /**
                         * <p>
                         * Converts the given Java version string to a <code>float</code>.
                         * </p>
                         *
                         * <p>
                         * Example return values:
                         * </p>
                         * <ul>
                         * <li><code>1.2f</code> for Java 1.2</li>
                         * <li><code>1.31f</code> for Java 1.3.1</li>
                         * <li><code>1.6f</code> for Java 1.6.0_20</li>
                         * </ul>
                         *
                         * <p>
                         * Patch releases are not reported.
                         * </p>
                         * <p>
                         * This method is package private instead of private to support unit test invocation.
                         * </p>
                         *
                         * @param {string} version The string version
                         * @return {number} the version, for example 1.31f for Java 1.3.1
                         */
                        static toJavaVersionFloat(version) {
                            return SystemUtils.toVersionFloat(SystemUtils.toJavaVersionIntArray$java_lang_String$int(version, SystemUtils.JAVA_VERSION_TRIM_SIZE));
                        }
                        /**
                         * <p>
                         * Converts the given Java version string to an <code>int</code>.
                         * </p>
                         *
                         * <p>
                         * Example return values:
                         * </p>
                         * <ul>
                         * <li><code>120</code> for Java 1.2</li>
                         * <li><code>131</code> for Java 1.3.1</li>
                         * <li><code>160</code> for Java 1.6.0_20</li>
                         * </ul>
                         *
                         * <p>
                         * Patch releases are not reported.
                         * </p>
                         * <p>
                         * This method is package private instead of private to support unit test invocation.
                         * </p>
                         *
                         * @param {string} version The string version
                         * @return {number} the version, for example 131 for Java 1.3.1
                         */
                        static toJavaVersionInt(version) {
                            return SystemUtils.toVersionInt(SystemUtils.toJavaVersionIntArray$java_lang_String$int(version, SystemUtils.JAVA_VERSION_TRIM_SIZE));
                        }
                        static toJavaVersionIntArray$java_lang_String(version) {
                            return SystemUtils.toJavaVersionIntArray$java_lang_String$int(version, 2147483647);
                        }
                        static toJavaVersionIntArray$java_lang_String$int(version, limit) {
                            if (version == null) {
                                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_INT_ARRAY_$LI$();
                            }
                            const strings = org.openprovenance.apache.commons.lang.StringUtils.split$java_lang_String$java_lang_String(version, "._- ");
                            let ints = (s => { let a = []; while (s-- > 0)
                                a.push(0); return a; })(Math.min(limit, strings.length));
                            let j = 0;
                            for (let i = 0; i < strings.length && j < limit; i++) {
                                {
                                    const s = strings[i];
                                    if (s.length > 0) {
                                        try {
                                            ints[j] = /* parseInt */ parseInt(s);
                                            j++;
                                        }
                                        catch (e) {
                                        }
                                    }
                                }
                                ;
                            }
                            if (ints.length > j) {
                                const newInts = (s => { let a = []; while (s-- > 0)
                                    a.push(0); return a; })(j);
                                /* arraycopy */ ((srcPts, srcOff, dstPts, dstOff, size) => { if (srcPts !== dstPts || dstOff >= srcOff + size) {
                                    while (--size >= 0)
                                        dstPts[dstOff++] = srcPts[srcOff++];
                                }
                                else {
                                    let tmp = srcPts.slice(srcOff, srcOff + size);
                                    for (let i = 0; i < size; i++)
                                        dstPts[dstOff++] = tmp[i];
                                } })(ints, 0, newInts, 0, j);
                                ints = newInts;
                            }
                            return ints;
                        }
                        /**
                         * <p>
                         * Converts the given Java version string to an <code>int[]</code> of maximum size <code>limit</code>.
                         * </p>
                         *
                         * <p>
                         * Example return values:
                         * </p>
                         * <ul>
                         * <li><code>[1, 2, 0]</code> for Java 1.2</li>
                         * <li><code>[1, 3, 1]</code> for Java 1.3.1</li>
                         * <li><code>[1, 5, 0, 21]</code> for Java 1.5.0_21</li>
                         * </ul>
                         *
                         * @param {string} version The string version
                         * @param {number} limit version limit
                         * @return {int[]} the version, for example [1, 5, 0, 21] for Java 1.5.0_21
                         * @private
                         */
                        static toJavaVersionIntArray(version, limit) {
                            if (((typeof version === 'string') || version === null) && ((typeof limit === 'number') || limit === null)) {
                                return org.openprovenance.apache.commons.lang.SystemUtils.toJavaVersionIntArray$java_lang_String$int(version, limit);
                            }
                            else if (((typeof version === 'string') || version === null) && limit === undefined) {
                                return org.openprovenance.apache.commons.lang.SystemUtils.toJavaVersionIntArray$java_lang_String(version);
                            }
                            else
                                throw new Error('invalid overload');
                        }
                        /**
                         * <p>
                         * Converts given the Java version array to a <code>float</code>.
                         * </p>
                         *
                         * <p>
                         * Example return values:
                         * </p>
                         * <ul>
                         * <li><code>1.2f</code> for Java 1.2</li>
                         * <li><code>1.31f</code> for Java 1.3.1</li>
                         * <li><code>1.6f</code> for Java 1.6.0_20</li>
                         * </ul>
                         *
                         * <p>
                         * Patch releases are not reported.
                         * </p>
                         *
                         * @param {int[]} javaVersions The version numbers
                         * @return {number} the version, for example 1.31f for Java 1.3.1
                         * @private
                         */
                        /*private*/ static toVersionFloat(javaVersions) {
                            if (javaVersions == null || javaVersions.length === 0) {
                                return 0.0;
                            }
                            if (javaVersions.length === 1) {
                                return javaVersions[0];
                            }
                            const builder = { str: "", toString: function () { return this.str; } };
                            /* append */ (sb => { sb.str += javaVersions[0]; return sb; })(builder);
                            /* append */ (sb => { sb.str += '.'; return sb; })(builder);
                            for (let i = 1; i < javaVersions.length; i++) {
                                {
                                    /* append */ (sb => { sb.str += javaVersions[i]; return sb; })(builder);
                                }
                                ;
                            }
                            try {
                                return /* parseFloat */ parseFloat(/* toString */ builder.str);
                            }
                            catch (ex) {
                                return 0.0;
                            }
                        }
                        /**
                         * <p>
                         * Converts given the Java version array to an <code>int</code>.
                         * </p>
                         *
                         * <p>
                         * Example return values:
                         * </p>
                         * <ul>
                         * <li><code>120</code> for Java 1.2</li>
                         * <li><code>131</code> for Java 1.3.1</li>
                         * <li><code>160</code> for Java 1.6.0_20</li>
                         * </ul>
                         *
                         * <p>
                         * Patch releases are not reported.
                         * </p>
                         *
                         * @param {int[]} javaVersions The version numbers
                         * @return {number} the version, for example 1.31f for Java 1.3.1
                         * @private
                         */
                        /*private*/ static toVersionInt(javaVersions) {
                            if (javaVersions == null) {
                                return 0;
                            }
                            let intVersion = 0;
                            const len = javaVersions.length;
                            if (len >= 1) {
                                intVersion = javaVersions[0] * 100;
                            }
                            if (len >= 2) {
                                intVersion += javaVersions[1] * 10;
                            }
                            if (len >= 3) {
                                intVersion += javaVersions[2];
                            }
                            return intVersion;
                        }
                    }
                    SystemUtils.JAVA_VERSION_TRIM_SIZE = 3;
                    /**
                     * The prefix String for all Windows OS.
                     */
                    SystemUtils.OS_NAME_WINDOWS_PREFIX = "Windows";
                    /**
                     * The System property key for the user home directory.
                     */
                    SystemUtils.USER_HOME_KEY = "user.home";
                    /**
                     * The System property key for the user directory.
                     */
                    SystemUtils.USER_DIR_KEY = "user.dir";
                    /**
                     * The System property key for the Java IO temporary directory.
                     */
                    SystemUtils.JAVA_IO_TMPDIR_KEY = "java.io.tmpdir";
                    /**
                     * The System property key for the Java home directory.
                     */
                    SystemUtils.JAVA_HOME_KEY = "java.home";
                    lang.SystemUtils = SystemUtils;
                    SystemUtils["__class"] = "org.openprovenance.apache.commons.lang.SystemUtils";
                })(lang = commons.lang || (commons.lang = {}));
            })(commons = apache.commons || (apache.commons = {}));
        })(apache = openprovenance.apache || (openprovenance.apache = {}));
    })(openprovenance = org.openprovenance || (org.openprovenance = {}));
})(org || (org = {}));
