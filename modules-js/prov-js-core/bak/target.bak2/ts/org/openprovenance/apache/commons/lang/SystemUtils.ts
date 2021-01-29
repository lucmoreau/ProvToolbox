/* Generated from Java with JSweet 3.0.0 - http://www.jsweet.org */
namespace org.openprovenance.apache.commons.lang {
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
    export class SystemUtils {
        static JAVA_VERSION_TRIM_SIZE: number = 3;

        /**
         * The prefix String for all Windows OS.
         */
        static OS_NAME_WINDOWS_PREFIX: string = "Windows";

        /**
         * The System property key for the user home directory.
         */
        static USER_HOME_KEY: string = "user.home";

        /**
         * The System property key for the user directory.
         */
        static USER_DIR_KEY: string = "user.dir";

        /**
         * The System property key for the Java IO temporary directory.
         */
        static JAVA_IO_TMPDIR_KEY: string = "java.io.tmpdir";

        /**
         * The System property key for the Java home directory.
         */
        static JAVA_HOME_KEY: string = "java.home";

        /**
         * <p>
         * The <code>awt.toolkit</code> System Property.
         * </p>
         * <p>
         * Holds a class name, on Windows XP this is <code>sun.awt.windows.WToolkit</code>.
         * </p>
         * <p>
         * <b>On platforms without a GUI, this value is <code>null</code>.</b>
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value will
         * be out of sync with that System property.
         * </p>
         * 
         * @since 2.1
         */
        public static AWT_TOOLKIT: string; public static AWT_TOOLKIT_$LI$(): string { if (SystemUtils.AWT_TOOLKIT == null) { SystemUtils.AWT_TOOLKIT = SystemUtils.getSystemProperty("awt.toolkit"); }  return SystemUtils.AWT_TOOLKIT; }

        /**
         * <p>
         * The <code>file.encoding</code> System Property.
         * </p>
         * <p>
         * File encoding, such as <code>Cp1252</code>.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.0
         * @since Java 1.2
         */
        public static FILE_ENCODING: string; public static FILE_ENCODING_$LI$(): string { if (SystemUtils.FILE_ENCODING == null) { SystemUtils.FILE_ENCODING = SystemUtils.getSystemProperty("file.encoding"); }  return SystemUtils.FILE_ENCODING; }

        /**
         * <p>
         * The <code>file.separator</code> System Property. File separator (<code>&quot;/&quot;</code> on UNIX).
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static FILE_SEPARATOR: string; public static FILE_SEPARATOR_$LI$(): string { if (SystemUtils.FILE_SEPARATOR == null) { SystemUtils.FILE_SEPARATOR = SystemUtils.getSystemProperty("file.separator"); }  return SystemUtils.FILE_SEPARATOR; }

        /**
         * <p>
         * The <code>java.awt.fonts</code> System Property.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.1
         */
        public static JAVA_AWT_FONTS: string; public static JAVA_AWT_FONTS_$LI$(): string { if (SystemUtils.JAVA_AWT_FONTS == null) { SystemUtils.JAVA_AWT_FONTS = SystemUtils.getSystemProperty("java.awt.fonts"); }  return SystemUtils.JAVA_AWT_FONTS; }

        /**
         * <p>
         * The <code>java.awt.graphicsenv</code> System Property.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.1
         */
        public static JAVA_AWT_GRAPHICSENV: string; public static JAVA_AWT_GRAPHICSENV_$LI$(): string { if (SystemUtils.JAVA_AWT_GRAPHICSENV == null) { SystemUtils.JAVA_AWT_GRAPHICSENV = SystemUtils.getSystemProperty("java.awt.graphicsenv"); }  return SystemUtils.JAVA_AWT_GRAPHICSENV; }

        /**
         * <p>
         * The <code>java.awt.headless</code> System Property.
         * The value of this property is the String <code>"true"</code> or <code>"false"</code>.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @see #isJavaAwtHeadless()
         * @since 2.1
         * @since Java 1.4
         */
        public static JAVA_AWT_HEADLESS: string; public static JAVA_AWT_HEADLESS_$LI$(): string { if (SystemUtils.JAVA_AWT_HEADLESS == null) { SystemUtils.JAVA_AWT_HEADLESS = SystemUtils.getSystemProperty("java.awt.headless"); }  return SystemUtils.JAVA_AWT_HEADLESS; }

        /**
         * <p>
         * The <code>java.awt.printerjob</code> System Property.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.1
         */
        public static JAVA_AWT_PRINTERJOB: string; public static JAVA_AWT_PRINTERJOB_$LI$(): string { if (SystemUtils.JAVA_AWT_PRINTERJOB == null) { SystemUtils.JAVA_AWT_PRINTERJOB = SystemUtils.getSystemProperty("java.awt.printerjob"); }  return SystemUtils.JAVA_AWT_PRINTERJOB; }

        /**
         * <p>
         * The <code>java.class.path</code> System Property. Java class path.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static JAVA_CLASS_PATH: string; public static JAVA_CLASS_PATH_$LI$(): string { if (SystemUtils.JAVA_CLASS_PATH == null) { SystemUtils.JAVA_CLASS_PATH = SystemUtils.getSystemProperty("java.class.path"); }  return SystemUtils.JAVA_CLASS_PATH; }

        /**
         * <p>
         * The <code>java.class.version</code> System Property. Java class format version number.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static JAVA_CLASS_VERSION: string; public static JAVA_CLASS_VERSION_$LI$(): string { if (SystemUtils.JAVA_CLASS_VERSION == null) { SystemUtils.JAVA_CLASS_VERSION = SystemUtils.getSystemProperty("java.class.version"); }  return SystemUtils.JAVA_CLASS_VERSION; }

        /**
         * <p>
         * The <code>java.compiler</code> System Property. Name of JIT compiler to use.
         * First in JDK version 1.2. Not used in Sun JDKs after 1.2.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2. Not used in Sun versions after 1.2.
         */
        public static JAVA_COMPILER: string; public static JAVA_COMPILER_$LI$(): string { if (SystemUtils.JAVA_COMPILER == null) { SystemUtils.JAVA_COMPILER = SystemUtils.getSystemProperty("java.compiler"); }  return SystemUtils.JAVA_COMPILER; }

        /**
         * <p>
         * The <code>java.endorsed.dirs</code> System Property. Path of endorsed directory or directories.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.4
         */
        public static JAVA_ENDORSED_DIRS: string; public static JAVA_ENDORSED_DIRS_$LI$(): string { if (SystemUtils.JAVA_ENDORSED_DIRS == null) { SystemUtils.JAVA_ENDORSED_DIRS = SystemUtils.getSystemProperty("java.endorsed.dirs"); }  return SystemUtils.JAVA_ENDORSED_DIRS; }

        /**
         * <p>
         * The <code>java.ext.dirs</code> System Property. Path of extension directory or directories.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.3
         */
        public static JAVA_EXT_DIRS: string; public static JAVA_EXT_DIRS_$LI$(): string { if (SystemUtils.JAVA_EXT_DIRS == null) { SystemUtils.JAVA_EXT_DIRS = SystemUtils.getSystemProperty("java.ext.dirs"); }  return SystemUtils.JAVA_EXT_DIRS; }

        /**
         * <p>
         * The <code>java.home</code> System Property. Java installation directory.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static JAVA_HOME: string; public static JAVA_HOME_$LI$(): string { if (SystemUtils.JAVA_HOME == null) { SystemUtils.JAVA_HOME = SystemUtils.getSystemProperty(SystemUtils.JAVA_HOME_KEY); }  return SystemUtils.JAVA_HOME; }

        /**
         * <p>
         * The <code>java.io.tmpdir</code> System Property. Default temp file path.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_IO_TMPDIR: string; public static JAVA_IO_TMPDIR_$LI$(): string { if (SystemUtils.JAVA_IO_TMPDIR == null) { SystemUtils.JAVA_IO_TMPDIR = SystemUtils.getSystemProperty(SystemUtils.JAVA_IO_TMPDIR_KEY); }  return SystemUtils.JAVA_IO_TMPDIR; }

        /**
         * <p>
         * The <code>java.library.path</code> System Property. List of paths to search when loading libraries.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_LIBRARY_PATH: string; public static JAVA_LIBRARY_PATH_$LI$(): string { if (SystemUtils.JAVA_LIBRARY_PATH == null) { SystemUtils.JAVA_LIBRARY_PATH = SystemUtils.getSystemProperty("java.library.path"); }  return SystemUtils.JAVA_LIBRARY_PATH; }

        /**
         * <p>
         * The <code>java.runtime.name</code> System Property. Java Runtime Environment name.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.0
         * @since Java 1.3
         */
        public static JAVA_RUNTIME_NAME: string; public static JAVA_RUNTIME_NAME_$LI$(): string { if (SystemUtils.JAVA_RUNTIME_NAME == null) { SystemUtils.JAVA_RUNTIME_NAME = SystemUtils.getSystemProperty("java.runtime.name"); }  return SystemUtils.JAVA_RUNTIME_NAME; }

        /**
         * <p>
         * The <code>java.runtime.version</code> System Property. Java Runtime Environment version.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.0
         * @since Java 1.3
         */
        public static JAVA_RUNTIME_VERSION: string; public static JAVA_RUNTIME_VERSION_$LI$(): string { if (SystemUtils.JAVA_RUNTIME_VERSION == null) { SystemUtils.JAVA_RUNTIME_VERSION = SystemUtils.getSystemProperty("java.runtime.version"); }  return SystemUtils.JAVA_RUNTIME_VERSION; }

        /**
         * <p>
         * The <code>java.specification.name</code> System Property. Java Runtime Environment specification name.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_SPECIFICATION_NAME: string; public static JAVA_SPECIFICATION_NAME_$LI$(): string { if (SystemUtils.JAVA_SPECIFICATION_NAME == null) { SystemUtils.JAVA_SPECIFICATION_NAME = SystemUtils.getSystemProperty("java.specification.name"); }  return SystemUtils.JAVA_SPECIFICATION_NAME; }

        /**
         * <p>
         * The <code>java.specification.vendor</code> System Property. Java Runtime Environment specification vendor.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_SPECIFICATION_VENDOR: string; public static JAVA_SPECIFICATION_VENDOR_$LI$(): string { if (SystemUtils.JAVA_SPECIFICATION_VENDOR == null) { SystemUtils.JAVA_SPECIFICATION_VENDOR = SystemUtils.getSystemProperty("java.specification.vendor"); }  return SystemUtils.JAVA_SPECIFICATION_VENDOR; }

        /**
         * <p>
         * The <code>java.specification.version</code> System Property. Java Runtime Environment specification version.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.3
         */
        public static JAVA_SPECIFICATION_VERSION: string; public static JAVA_SPECIFICATION_VERSION_$LI$(): string { if (SystemUtils.JAVA_SPECIFICATION_VERSION == null) { SystemUtils.JAVA_SPECIFICATION_VERSION = SystemUtils.getSystemProperty("java.specification.version"); }  return SystemUtils.JAVA_SPECIFICATION_VERSION; }

        /**
         * <p>
         * The <code>java.util.prefs.PreferencesFactory</code> System Property. A class name.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.1
         * @since Java 1.4
         */
        public static JAVA_UTIL_PREFS_PREFERENCES_FACTORY: string; public static JAVA_UTIL_PREFS_PREFERENCES_FACTORY_$LI$(): string { if (SystemUtils.JAVA_UTIL_PREFS_PREFERENCES_FACTORY == null) { SystemUtils.JAVA_UTIL_PREFS_PREFERENCES_FACTORY = SystemUtils.getSystemProperty("java.util.prefs.PreferencesFactory"); }  return SystemUtils.JAVA_UTIL_PREFS_PREFERENCES_FACTORY; }

        /**
         * <p>
         * The <code>java.vendor</code> System Property. Java vendor-specific string.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static JAVA_VENDOR: string; public static JAVA_VENDOR_$LI$(): string { if (SystemUtils.JAVA_VENDOR == null) { SystemUtils.JAVA_VENDOR = SystemUtils.getSystemProperty("java.vendor"); }  return SystemUtils.JAVA_VENDOR; }

        /**
         * <p>
         * The <code>java.vendor.url</code> System Property. Java vendor URL.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static JAVA_VENDOR_URL: string; public static JAVA_VENDOR_URL_$LI$(): string { if (SystemUtils.JAVA_VENDOR_URL == null) { SystemUtils.JAVA_VENDOR_URL = SystemUtils.getSystemProperty("java.vendor.url"); }  return SystemUtils.JAVA_VENDOR_URL; }

        /**
         * <p>
         * The <code>java.version</code> System Property. Java version number.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static JAVA_VERSION: string; public static JAVA_VERSION_$LI$(): string { if (SystemUtils.JAVA_VERSION == null) { SystemUtils.JAVA_VERSION = SystemUtils.getSystemProperty("java.version"); }  return SystemUtils.JAVA_VERSION; }

        /**
         * <p>
         * The <code>java.vm.info</code> System Property. Java Virtual Machine implementation info.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.0
         * @since Java 1.2
         */
        public static JAVA_VM_INFO: string; public static JAVA_VM_INFO_$LI$(): string { if (SystemUtils.JAVA_VM_INFO == null) { SystemUtils.JAVA_VM_INFO = SystemUtils.getSystemProperty("java.vm.info"); }  return SystemUtils.JAVA_VM_INFO; }

        /**
         * <p>
         * The <code>java.vm.name</code> System Property. Java Virtual Machine implementation name.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_VM_NAME: string; public static JAVA_VM_NAME_$LI$(): string { if (SystemUtils.JAVA_VM_NAME == null) { SystemUtils.JAVA_VM_NAME = SystemUtils.getSystemProperty("java.vm.name"); }  return SystemUtils.JAVA_VM_NAME; }

        /**
         * <p>
         * The <code>java.vm.specification.name</code> System Property. Java Virtual Machine specification name.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_VM_SPECIFICATION_NAME: string; public static JAVA_VM_SPECIFICATION_NAME_$LI$(): string { if (SystemUtils.JAVA_VM_SPECIFICATION_NAME == null) { SystemUtils.JAVA_VM_SPECIFICATION_NAME = SystemUtils.getSystemProperty("java.vm.specification.name"); }  return SystemUtils.JAVA_VM_SPECIFICATION_NAME; }

        /**
         * <p>
         * The <code>java.vm.specification.vendor</code> System Property. Java Virtual Machine specification vendor.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_VM_SPECIFICATION_VENDOR: string; public static JAVA_VM_SPECIFICATION_VENDOR_$LI$(): string { if (SystemUtils.JAVA_VM_SPECIFICATION_VENDOR == null) { SystemUtils.JAVA_VM_SPECIFICATION_VENDOR = SystemUtils.getSystemProperty("java.vm.specification.vendor"); }  return SystemUtils.JAVA_VM_SPECIFICATION_VENDOR; }

        /**
         * <p>
         * The <code>java.vm.specification.version</code> System Property. Java Virtual Machine specification version.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_VM_SPECIFICATION_VERSION: string; public static JAVA_VM_SPECIFICATION_VERSION_$LI$(): string { if (SystemUtils.JAVA_VM_SPECIFICATION_VERSION == null) { SystemUtils.JAVA_VM_SPECIFICATION_VERSION = SystemUtils.getSystemProperty("java.vm.specification.version"); }  return SystemUtils.JAVA_VM_SPECIFICATION_VERSION; }

        /**
         * <p>
         * The <code>java.vm.vendor</code> System Property. Java Virtual Machine implementation vendor.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_VM_VENDOR: string; public static JAVA_VM_VENDOR_$LI$(): string { if (SystemUtils.JAVA_VM_VENDOR == null) { SystemUtils.JAVA_VM_VENDOR = SystemUtils.getSystemProperty("java.vm.vendor"); }  return SystemUtils.JAVA_VM_VENDOR; }

        /**
         * <p>
         * The <code>java.vm.version</code> System Property. Java Virtual Machine implementation version.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.2
         */
        public static JAVA_VM_VERSION: string; public static JAVA_VM_VERSION_$LI$(): string { if (SystemUtils.JAVA_VM_VERSION == null) { SystemUtils.JAVA_VM_VERSION = SystemUtils.getSystemProperty("java.vm.version"); }  return SystemUtils.JAVA_VM_VERSION; }

        /**
         * <p>
         * The <code>line.separator</code> System Property. Line separator (<code>&quot;\n&quot;</code> on UNIX).
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static LINE_SEPARATOR: string; public static LINE_SEPARATOR_$LI$(): string { if (SystemUtils.LINE_SEPARATOR == null) { SystemUtils.LINE_SEPARATOR = SystemUtils.getSystemProperty("line.separator"); }  return SystemUtils.LINE_SEPARATOR; }

        /**
         * <p>
         * The <code>os.arch</code> System Property. Operating system architecture.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static OS_ARCH: string; public static OS_ARCH_$LI$(): string { if (SystemUtils.OS_ARCH == null) { SystemUtils.OS_ARCH = SystemUtils.getSystemProperty("os.arch"); }  return SystemUtils.OS_ARCH; }

        /**
         * <p>
         * The <code>os.name</code> System Property. Operating system name.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static OS_NAME: string; public static OS_NAME_$LI$(): string { if (SystemUtils.OS_NAME == null) { SystemUtils.OS_NAME = SystemUtils.getSystemProperty("os.name"); }  return SystemUtils.OS_NAME; }

        /**
         * <p>
         * The <code>os.version</code> System Property. Operating system version.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static OS_VERSION: string; public static OS_VERSION_$LI$(): string { if (SystemUtils.OS_VERSION == null) { SystemUtils.OS_VERSION = SystemUtils.getSystemProperty("os.version"); }  return SystemUtils.OS_VERSION; }

        /**
         * <p>
         * The <code>path.separator</code> System Property. Path separator (<code>&quot;:&quot;</code> on UNIX).
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static PATH_SEPARATOR: string; public static PATH_SEPARATOR_$LI$(): string { if (SystemUtils.PATH_SEPARATOR == null) { SystemUtils.PATH_SEPARATOR = SystemUtils.getSystemProperty("path.separator"); }  return SystemUtils.PATH_SEPARATOR; }

        /**
         * <p>
         * The <code>user.country</code> or <code>user.region</code> System Property.
         * User's country code, such as <code>GB</code>. First in
         * Java version 1.2 as <code>user.region</code>. Renamed to <code>user.country</code> in 1.4
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.0
         * @since Java 1.2
         */
        public static USER_COUNTRY: string; public static USER_COUNTRY_$LI$(): string { if (SystemUtils.USER_COUNTRY == null) { SystemUtils.USER_COUNTRY = SystemUtils.getSystemProperty("user.country") == null ? SystemUtils.getSystemProperty("user.region") : SystemUtils.getSystemProperty("user.country"); }  return SystemUtils.USER_COUNTRY; }

        /**
         * <p>
         * The <code>user.dir</code> System Property. User's current working directory.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static USER_DIR: string; public static USER_DIR_$LI$(): string { if (SystemUtils.USER_DIR == null) { SystemUtils.USER_DIR = SystemUtils.getSystemProperty(SystemUtils.USER_DIR_KEY); }  return SystemUtils.USER_DIR; }

        /**
         * <p>
         * The <code>user.home</code> System Property. User's home directory.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static USER_HOME: string; public static USER_HOME_$LI$(): string { if (SystemUtils.USER_HOME == null) { SystemUtils.USER_HOME = SystemUtils.getSystemProperty(SystemUtils.USER_HOME_KEY); }  return SystemUtils.USER_HOME; }

        /**
         * <p>
         * The <code>user.language</code> System Property. User's language code, such as <code>"en"</code>.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.0
         * @since Java 1.2
         */
        public static USER_LANGUAGE: string; public static USER_LANGUAGE_$LI$(): string { if (SystemUtils.USER_LANGUAGE == null) { SystemUtils.USER_LANGUAGE = SystemUtils.getSystemProperty("user.language"); }  return SystemUtils.USER_LANGUAGE; }

        /**
         * <p>
         * The <code>user.name</code> System Property. User's account name.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since Java 1.1
         */
        public static USER_NAME: string; public static USER_NAME_$LI$(): string { if (SystemUtils.USER_NAME == null) { SystemUtils.USER_NAME = SystemUtils.getSystemProperty("user.name"); }  return SystemUtils.USER_NAME; }

        /**
         * <p>
         * The <code>user.timezone</code> System Property. For example: <code>"America/Los_Angeles"</code>.
         * </p>
         * 
         * <p>
         * Defaults to <code>null</code> if the runtime does not have
         * security access to read this property or the property does not exist.
         * </p>
         * 
         * <p>
         * This value is initialized when the class is loaded. If {@link System#setProperty(String,String)} or
         * {@link System#setProperties(java.util.Properties)} is called after this class is loaded, the value
         * will be out of sync with that System property.
         * </p>
         * 
         * @since 2.1
         */
        public static USER_TIMEZONE: string; public static USER_TIMEZONE_$LI$(): string { if (SystemUtils.USER_TIMEZONE == null) { SystemUtils.USER_TIMEZONE = SystemUtils.getSystemProperty("user.timezone"); }  return SystemUtils.USER_TIMEZONE; }

        /**
         * <p>
         * Gets the Java version as a <code>String</code> trimming leading letters.
         * </p>
         * 
         * <p>
         * The field will return <code>null</code> if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         * 
         * @since 2.1
         */
        public static JAVA_VERSION_TRIMMED: string; public static JAVA_VERSION_TRIMMED_$LI$(): string { if (SystemUtils.JAVA_VERSION_TRIMMED == null) { SystemUtils.JAVA_VERSION_TRIMMED = SystemUtils.getJavaVersionTrimmed(); }  return SystemUtils.JAVA_VERSION_TRIMMED; }

        /**
         * <p>
         * Gets the Java version as a <code>float</code>.
         * </p>
         * 
         * <p>
         * Example return values:
         * </p>
         * <ul>
         * <li><code>1.2f</code> for Java 1.2
         * <li><code>1.31f</code> for Java 1.3.1
         * </ul>
         * 
         * <p>
         * The field will return zero if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static JAVA_VERSION_FLOAT: number; public static JAVA_VERSION_FLOAT_$LI$(): number { if (SystemUtils.JAVA_VERSION_FLOAT == null) { SystemUtils.JAVA_VERSION_FLOAT = SystemUtils.getJavaVersionAsFloat(); }  return SystemUtils.JAVA_VERSION_FLOAT; }

        /**
         * <p>
         * Gets the Java version as an <code>int</code>.
         * </p>
         * 
         * <p>
         * Example return values:
         * </p>
         * <ul>
         * <li><code>120</code> for Java 1.2
         * <li><code>131</code> for Java 1.3.1
         * </ul>
         * 
         * <p>
         * The field will return zero if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static JAVA_VERSION_INT: number; public static JAVA_VERSION_INT_$LI$(): number { if (SystemUtils.JAVA_VERSION_INT == null) { SystemUtils.JAVA_VERSION_INT = SystemUtils.getJavaVersionAsInt(); }  return SystemUtils.JAVA_VERSION_INT; }

        /**
         * <p>
         * Is <code>true</code> if this is Java version 1.1 (also 1.1.x versions).
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         */
        public static IS_JAVA_1_1: boolean; public static IS_JAVA_1_1_$LI$(): boolean { if (SystemUtils.IS_JAVA_1_1 == null) { SystemUtils.IS_JAVA_1_1 = SystemUtils.getJavaVersionMatches("1.1"); }  return SystemUtils.IS_JAVA_1_1; }

        /**
         * <p>
         * Is <code>true</code> if this is Java version 1.2 (also 1.2.x versions).
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         */
        public static IS_JAVA_1_2: boolean; public static IS_JAVA_1_2_$LI$(): boolean { if (SystemUtils.IS_JAVA_1_2 == null) { SystemUtils.IS_JAVA_1_2 = SystemUtils.getJavaVersionMatches("1.2"); }  return SystemUtils.IS_JAVA_1_2; }

        /**
         * <p>
         * Is <code>true</code> if this is Java version 1.3 (also 1.3.x versions).
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         */
        public static IS_JAVA_1_3: boolean; public static IS_JAVA_1_3_$LI$(): boolean { if (SystemUtils.IS_JAVA_1_3 == null) { SystemUtils.IS_JAVA_1_3 = SystemUtils.getJavaVersionMatches("1.3"); }  return SystemUtils.IS_JAVA_1_3; }

        /**
         * <p>
         * Is <code>true</code> if this is Java version 1.4 (also 1.4.x versions).
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         */
        public static IS_JAVA_1_4: boolean; public static IS_JAVA_1_4_$LI$(): boolean { if (SystemUtils.IS_JAVA_1_4 == null) { SystemUtils.IS_JAVA_1_4 = SystemUtils.getJavaVersionMatches("1.4"); }  return SystemUtils.IS_JAVA_1_4; }

        /**
         * <p>
         * Is <code>true</code> if this is Java version 1.5 (also 1.5.x versions).
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         */
        public static IS_JAVA_1_5: boolean; public static IS_JAVA_1_5_$LI$(): boolean { if (SystemUtils.IS_JAVA_1_5 == null) { SystemUtils.IS_JAVA_1_5 = SystemUtils.getJavaVersionMatches("1.5"); }  return SystemUtils.IS_JAVA_1_5; }

        /**
         * <p>
         * Is <code>true</code> if this is Java version 1.6 (also 1.6.x versions).
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         */
        public static IS_JAVA_1_6: boolean; public static IS_JAVA_1_6_$LI$(): boolean { if (SystemUtils.IS_JAVA_1_6 == null) { SystemUtils.IS_JAVA_1_6 = SystemUtils.getJavaVersionMatches("1.6"); }  return SystemUtils.IS_JAVA_1_6; }

        /**
         * <p>
         * Is <code>true</code> if this is Java version 1.7 (also 1.7.x versions).
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if {@link #JAVA_VERSION} is <code>null</code>.
         * </p>
         * 
         * @since 2.5
         */
        public static IS_JAVA_1_7: boolean; public static IS_JAVA_1_7_$LI$(): boolean { if (SystemUtils.IS_JAVA_1_7 == null) { SystemUtils.IS_JAVA_1_7 = SystemUtils.getJavaVersionMatches("1.7"); }  return SystemUtils.IS_JAVA_1_7; }

        /**
         * <p>
         * Is <code>true</code> if this is AIX.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_AIX: boolean; public static IS_OS_AIX_$LI$(): boolean { if (SystemUtils.IS_OS_AIX == null) { SystemUtils.IS_OS_AIX = SystemUtils.getOSMatchesName("AIX"); }  return SystemUtils.IS_OS_AIX; }

        /**
         * <p>
         * Is <code>true</code> if this is HP-UX.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_HP_UX: boolean; public static IS_OS_HP_UX_$LI$(): boolean { if (SystemUtils.IS_OS_HP_UX == null) { SystemUtils.IS_OS_HP_UX = SystemUtils.getOSMatchesName("HP-UX"); }  return SystemUtils.IS_OS_HP_UX; }

        /**
         * <p>
         * Is <code>true</code> if this is Irix.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_IRIX: boolean; public static IS_OS_IRIX_$LI$(): boolean { if (SystemUtils.IS_OS_IRIX == null) { SystemUtils.IS_OS_IRIX = SystemUtils.getOSMatchesName("Irix"); }  return SystemUtils.IS_OS_IRIX; }

        /**
         * <p>
         * Is <code>true</code> if this is Linux.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_LINUX: boolean; public static IS_OS_LINUX_$LI$(): boolean { if (SystemUtils.IS_OS_LINUX == null) { SystemUtils.IS_OS_LINUX = SystemUtils.getOSMatchesName("Linux") || SystemUtils.getOSMatchesName("LINUX"); }  return SystemUtils.IS_OS_LINUX; }

        /**
         * <p>
         * Is <code>true</code> if this is Mac.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_MAC: boolean; public static IS_OS_MAC_$LI$(): boolean { if (SystemUtils.IS_OS_MAC == null) { SystemUtils.IS_OS_MAC = SystemUtils.getOSMatchesName("Mac"); }  return SystemUtils.IS_OS_MAC; }

        /**
         * <p>
         * Is <code>true</code> if this is Mac.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_MAC_OSX: boolean; public static IS_OS_MAC_OSX_$LI$(): boolean { if (SystemUtils.IS_OS_MAC_OSX == null) { SystemUtils.IS_OS_MAC_OSX = SystemUtils.getOSMatchesName("Mac OS X"); }  return SystemUtils.IS_OS_MAC_OSX; }

        /**
         * <p>
         * Is <code>true</code> if this is OS/2.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_OS2: boolean; public static IS_OS_OS2_$LI$(): boolean { if (SystemUtils.IS_OS_OS2 == null) { SystemUtils.IS_OS_OS2 = SystemUtils.getOSMatchesName("OS/2"); }  return SystemUtils.IS_OS_OS2; }

        /**
         * <p>
         * Is <code>true</code> if this is Solaris.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_SOLARIS: boolean; public static IS_OS_SOLARIS_$LI$(): boolean { if (SystemUtils.IS_OS_SOLARIS == null) { SystemUtils.IS_OS_SOLARIS = SystemUtils.getOSMatchesName("Solaris"); }  return SystemUtils.IS_OS_SOLARIS; }

        /**
         * <p>
         * Is <code>true</code> if this is SunOS.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_SUN_OS: boolean; public static IS_OS_SUN_OS_$LI$(): boolean { if (SystemUtils.IS_OS_SUN_OS == null) { SystemUtils.IS_OS_SUN_OS = SystemUtils.getOSMatchesName("SunOS"); }  return SystemUtils.IS_OS_SUN_OS; }

        /**
         * <p>
         * Is <code>true</code> if this is a UNIX like system,
         * as in any of AIX, HP-UX, Irix, Linux, MacOSX, Solaris or SUN OS.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.1
         */
        public static IS_OS_UNIX: boolean; public static IS_OS_UNIX_$LI$(): boolean { if (SystemUtils.IS_OS_UNIX == null) { SystemUtils.IS_OS_UNIX = SystemUtils.IS_OS_AIX_$LI$() || SystemUtils.IS_OS_HP_UX_$LI$() || SystemUtils.IS_OS_IRIX_$LI$() || SystemUtils.IS_OS_LINUX_$LI$() || SystemUtils.IS_OS_MAC_OSX_$LI$() || SystemUtils.IS_OS_SOLARIS_$LI$() || SystemUtils.IS_OS_SUN_OS_$LI$(); }  return SystemUtils.IS_OS_UNIX; }

        /**
         * <p>
         * Is <code>true</code> if this is Windows.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_WINDOWS: boolean; public static IS_OS_WINDOWS_$LI$(): boolean { if (SystemUtils.IS_OS_WINDOWS == null) { SystemUtils.IS_OS_WINDOWS = SystemUtils.getOSMatchesName(SystemUtils.OS_NAME_WINDOWS_PREFIX); }  return SystemUtils.IS_OS_WINDOWS; }

        /**
         * <p>
         * Is <code>true</code> if this is Windows 2000.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_WINDOWS_2000: boolean; public static IS_OS_WINDOWS_2000_$LI$(): boolean { if (SystemUtils.IS_OS_WINDOWS_2000 == null) { SystemUtils.IS_OS_WINDOWS_2000 = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "5.0"); }  return SystemUtils.IS_OS_WINDOWS_2000; }

        /**
         * <p>
         * Is <code>true</code> if this is Windows 95.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_WINDOWS_95: boolean; public static IS_OS_WINDOWS_95_$LI$(): boolean { if (SystemUtils.IS_OS_WINDOWS_95 == null) { SystemUtils.IS_OS_WINDOWS_95 = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX + " 9", "4.0"); }  return SystemUtils.IS_OS_WINDOWS_95; }

        /**
         * <p>
         * Is <code>true</code> if this is Windows 98.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_WINDOWS_98: boolean; public static IS_OS_WINDOWS_98_$LI$(): boolean { if (SystemUtils.IS_OS_WINDOWS_98 == null) { SystemUtils.IS_OS_WINDOWS_98 = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX + " 9", "4.1"); }  return SystemUtils.IS_OS_WINDOWS_98; }

        /**
         * <p>
         * Is <code>true</code> if this is Windows ME.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_WINDOWS_ME: boolean; public static IS_OS_WINDOWS_ME_$LI$(): boolean { if (SystemUtils.IS_OS_WINDOWS_ME == null) { SystemUtils.IS_OS_WINDOWS_ME = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "4.9"); }  return SystemUtils.IS_OS_WINDOWS_ME; }

        /**
         * <p>
         * Is <code>true</code> if this is Windows NT.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_WINDOWS_NT: boolean; public static IS_OS_WINDOWS_NT_$LI$(): boolean { if (SystemUtils.IS_OS_WINDOWS_NT == null) { SystemUtils.IS_OS_WINDOWS_NT = SystemUtils.getOSMatchesName(SystemUtils.OS_NAME_WINDOWS_PREFIX + " NT"); }  return SystemUtils.IS_OS_WINDOWS_NT; }

        /**
         * <p>
         * Is <code>true</code> if this is Windows XP.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.0
         */
        public static IS_OS_WINDOWS_XP: boolean; public static IS_OS_WINDOWS_XP_$LI$(): boolean { if (SystemUtils.IS_OS_WINDOWS_XP == null) { SystemUtils.IS_OS_WINDOWS_XP = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "5.1"); }  return SystemUtils.IS_OS_WINDOWS_XP; }

        /**
         * <p>
         * Is <code>true</code> if this is Windows Vista.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.4
         */
        public static IS_OS_WINDOWS_VISTA: boolean; public static IS_OS_WINDOWS_VISTA_$LI$(): boolean { if (SystemUtils.IS_OS_WINDOWS_VISTA == null) { SystemUtils.IS_OS_WINDOWS_VISTA = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "6.0"); }  return SystemUtils.IS_OS_WINDOWS_VISTA; }

        /**
         * <p>
         * Is <code>true</code> if this is Windows 7.
         * </p>
         * 
         * <p>
         * The field will return <code>false</code> if <code>OS_NAME</code> is <code>null</code>.
         * </p>
         * 
         * @since 2.5
         */
        public static IS_OS_WINDOWS_7: boolean; public static IS_OS_WINDOWS_7_$LI$(): boolean { if (SystemUtils.IS_OS_WINDOWS_7 == null) { SystemUtils.IS_OS_WINDOWS_7 = SystemUtils.getOSMatches(SystemUtils.OS_NAME_WINDOWS_PREFIX, "6.1"); }  return SystemUtils.IS_OS_WINDOWS_7; }

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
        public static getJavaHome(): java.io.File {
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
        public static getJavaIoTmpDir(): java.io.File {
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
        public static getJavaVersion(): number {
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
        /*private*/ static getJavaVersionAsFloat(): number {
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
        /*private*/ static getJavaVersionAsInt(): number {
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
        /*private*/ static getJavaVersionMatches(versionPrefix: string): boolean {
            return SystemUtils.isJavaVersionMatch(SystemUtils.JAVA_VERSION_TRIMMED_$LI$(), versionPrefix);
        }

        /**
         * Trims the text of the java version to start with numbers.
         * 
         * @return {string} the trimmed java version
         * @private
         */
        /*private*/ static getJavaVersionTrimmed(): string {
            if (SystemUtils.JAVA_VERSION_$LI$() != null){
                for(let i: number = 0; i < SystemUtils.JAVA_VERSION_$LI$().length; i++) {{
                    const ch: string = SystemUtils.JAVA_VERSION_$LI$().charAt(i);
                    if ((c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) >= '0'.charCodeAt(0) && (c => c.charCodeAt==null?<any>c:c.charCodeAt(0))(ch) <= '9'.charCodeAt(0)){
                        return SystemUtils.JAVA_VERSION_$LI$().substring(i);
                    }
                };}
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
        /*private*/ static getOSMatches(osNamePrefix: string, osVersionPrefix: string): boolean {
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
        /*private*/ static getOSMatchesName(osNamePrefix: string): boolean {
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
        /*private*/ static getSystemProperty(property: string): string {
            try {
                return java.lang.System.getProperty(property);
            } catch(ex) {
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
        public static getUserDir(): java.io.File {
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
        public static getUserHome(): java.io.File {
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
        public static isJavaAwtHeadless(): boolean {
            return SystemUtils.JAVA_AWT_HEADLESS_$LI$() != null ? SystemUtils.JAVA_AWT_HEADLESS_$LI$() === true.toString() : false;
        }

        public static isJavaVersionAtLeast$float(requiredVersion: number): boolean {
            return SystemUtils.JAVA_VERSION_FLOAT_$LI$() >= requiredVersion;
        }

        public static isJavaVersionAtLeast$int(requiredVersion: number): boolean {
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
        public static isJavaVersionAtLeast(requiredVersion?: any): any {
            if (((typeof requiredVersion === 'number') || requiredVersion === null)) {
                return <any>org.openprovenance.apache.commons.lang.SystemUtils.isJavaVersionAtLeast$int(requiredVersion);
            } else if (((typeof requiredVersion === 'number') || requiredVersion === null)) {
                return <any>org.openprovenance.apache.commons.lang.SystemUtils.isJavaVersionAtLeast$float(requiredVersion);
            } else throw new Error('invalid overload');
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
        static isJavaVersionMatch(version: string, versionPrefix: string): boolean {
            if (version == null){
                return false;
            }
            return /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(version, versionPrefix);
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
        static isOSMatch(osName: string, osVersion: string, osNamePrefix: string, osVersionPrefix: string): boolean {
            if (osName == null || osVersion == null){
                return false;
            }
            return /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(osName, osNamePrefix) && /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(osVersion, osVersionPrefix);
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
        static isOSNameMatch(osName: string, osNamePrefix: string): boolean {
            if (osName == null){
                return false;
            }
            return /* startsWith */((str, searchString, position = 0) => str.substr(position, searchString.length) === searchString)(osName, osNamePrefix);
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
        static toJavaVersionFloat(version: string): number {
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
        static toJavaVersionInt(version: string): number {
            return SystemUtils.toVersionInt(SystemUtils.toJavaVersionIntArray$java_lang_String$int(version, SystemUtils.JAVA_VERSION_TRIM_SIZE));
        }

        static toJavaVersionIntArray$java_lang_String(version: string): number[] {
            return SystemUtils.toJavaVersionIntArray$java_lang_String$int(version, 2147483647);
        }

        public static toJavaVersionIntArray$java_lang_String$int(version: string, limit: number): number[] {
            if (version == null){
                return org.openprovenance.apache.commons.lang.ArrayUtils.EMPTY_INT_ARRAY_$LI$();
            }
            const strings: string[] = org.openprovenance.apache.commons.lang.StringUtils.split$java_lang_String$java_lang_String(version, "._- ");
            let ints: number[] = (s => { let a=[]; while(s-->0) a.push(0); return a; })(Math.min(limit, strings.length));
            let j: number = 0;
            for(let i: number = 0; i < strings.length && j < limit; i++) {{
                const s: string = strings[i];
                if (s.length > 0){
                    try {
                        ints[j] = /* parseInt */parseInt(s);
                        j++;
                    } catch(e) {
                    }
                }
            };}
            if (ints.length > j){
                const newInts: number[] = (s => { let a=[]; while(s-->0) a.push(0); return a; })(j);
                /* arraycopy */((srcPts, srcOff, dstPts, dstOff, size) => { if(srcPts !== dstPts || dstOff >= srcOff + size) { while (--size >= 0) dstPts[dstOff++] = srcPts[srcOff++];} else { let tmp = srcPts.slice(srcOff, srcOff + size); for (let i = 0; i < size; i++) dstPts[dstOff++] = tmp[i]; }})(ints, 0, newInts, 0, j);
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
        public static toJavaVersionIntArray(version?: any, limit?: any): any {
            if (((typeof version === 'string') || version === null) && ((typeof limit === 'number') || limit === null)) {
                return <any>org.openprovenance.apache.commons.lang.SystemUtils.toJavaVersionIntArray$java_lang_String$int(version, limit);
            } else if (((typeof version === 'string') || version === null) && limit === undefined) {
                return <any>org.openprovenance.apache.commons.lang.SystemUtils.toJavaVersionIntArray$java_lang_String(version);
            } else throw new Error('invalid overload');
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
        /*private*/ static toVersionFloat(javaVersions: number[]): number {
            if (javaVersions == null || javaVersions.length === 0){
                return 0.0;
            }
            if (javaVersions.length === 1){
                return javaVersions[0];
            }
            const builder: { str: string, toString: Function } = { str: "", toString: function() { return this.str; } };
            /* append */(sb => { sb.str += <any>javaVersions[0]; return sb; })(builder);
            /* append */(sb => { sb.str += <any>'.'; return sb; })(builder);
            for(let i: number = 1; i < javaVersions.length; i++) {{
                /* append */(sb => { sb.str += <any>javaVersions[i]; return sb; })(builder);
            };}
            try {
                return /* parseFloat */parseFloat(/* toString */builder.str);
            } catch(ex) {
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
        /*private*/ static toVersionInt(javaVersions: number[]): number {
            if (javaVersions == null){
                return 0;
            }
            let intVersion: number = 0;
            const len: number = javaVersions.length;
            if (len >= 1){
                intVersion = javaVersions[0] * 100;
            }
            if (len >= 2){
                intVersion += javaVersions[1] * 10;
            }
            if (len >= 3){
                intVersion += javaVersions[2];
            }
            return intVersion;
        }

        public constructor() {
        }
    }
    SystemUtils["__class"] = "org.openprovenance.apache.commons.lang.SystemUtils";

}

