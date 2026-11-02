#!/usr/bin/env python3
"""
Create the side-by-side Scala 3 copy of a Scala 2.13 module (see SCALA3_MIGRATION.md).

Usage (from the repository root):
    python3 scala3-migration/copy-module.py modules-core/prov-model-scala

It
  1. copies <module> to <module>-scala3 (without target/ and *.iml),
  2. rewrites the copied pom.xml:
       - artifactId X -> X-scala3, name gets " (Scala 3)"
       - property scala.binary.version=3 (so every _${scala.binary.version} dep becomes _3)
       - scala-library 2.13 -> scala3-library_3 ${scala3.version}
       - dependencies on the 2.13 Scala modules -> their -scala3 copies
       - scala-maven-plugin: scalaVersion ${scala3.version}, Scala 3 compiler args
       - maven-compiler-plugin: release ${scala3.release}
  3. registers <module>-scala3 in the parent aggregator pom, right after <module>.

The script refuses to run if the copy already exists, so it never overwrites work.
Source code is copied unchanged: fixing it for Scala 3 is a manual step.
"""
import os
import re
import shutil
import sys

# The 2.13 Scala modules (artifactIds). A -scala3 copy depends only on -scala3 copies.
SCALA_MODULES = [
    "prov-model-scala", "nlg-wrapper", "prov-scala-misc", "nlg-spec-transformer",
    "prov-nf", "prov-summarisation", "prov-signature", "prov-typemap", "prov-query",
    "prov-explanations", "prov-summary-explanations", "modules-executable-provman",
]

SCALA3_ARGS = """<args>
                        <arg>-deprecation</arg>
                        <arg>-feature</arg>
                        <arg>-no-indent</arg>
                        <arg>-release</arg>
                        <arg>${scala3.release}</arg>
                    </args>"""


def fail(msg):
    print("ERROR: " + msg, file=sys.stderr)
    sys.exit(1)


def rewrite_pom(s):
    # --- artifactId / name of the project itself (first artifactId after </parent>)
    parent_end = s.index("</parent>")
    m = re.compile(r"<artifactId>([^<]+)</artifactId>").search(s, parent_end)
    own = m.group(1)
    s = s[:m.start()] + "<artifactId>%s-scala3</artifactId>" % own + s[m.end():]
    s = re.sub(r"<name>([^<]*)</name>", lambda mm: "<name>%s (Scala 3)</name>" % mm.group(1).rstrip(), s, count=1)

    # --- property scala.binary.version = 3
    prop = "<scala.binary.version>3</scala.binary.version> <!-- Scala 3 copy -->"
    # the project-level <properties> is the one not nested in a <profile>; insert into the first
    # <properties> that occurs before <build> or <profiles>, else create one before <dependencies>.
    limit = min([i for i in (s.find("<build>"), s.find("<profiles>")) if i >= 0] or [len(s)])
    pi = s.find("<properties>")
    if 0 <= pi < limit:
        s = s[:pi] + "<properties>\n        " + prop + s[pi + len("<properties>"):]
    else:
        di = s.index("<dependencies>")
        s = s[:di] + "<properties>\n        " + prop + "\n    </properties>\n\n    " + s[di:]

    # --- scala-library -> scala3-library_3
    s, n = re.subn(
        r"<groupId>org\.scala-lang</groupId>(\s*)<artifactId>scala-library</artifactId>(\s*)<version>[^<]*</version>",
        r"<groupId>org.scala-lang</groupId>\1<artifactId>scala3-library_3</artifactId>\2<version>${scala3.version}</version>",
        s)
    if n == 0:
        # add an explicit dependency on the Scala 3 library
        di = s.index("<dependencies>") + len("<dependencies>")
        s = s[:di] + """
        <dependency>
            <groupId>org.scala-lang</groupId>
            <artifactId>scala3-library_3</artifactId>
            <version>${scala3.version}</version>
        </dependency>
""" + s[di:]

    # --- dependencies on 2.13 Scala modules -> -scala3
    for a in SCALA_MODULES:
        s = re.sub(r"<artifactId>%s</artifactId>" % re.escape(a), "<artifactId>%s-scala3</artifactId>" % a, s)
    # (the project's own artifactId may now read X-scala3-scala3)
    s = s.replace("-scala3-scala3", "-scala3")

    # --- scala-maven-plugin configuration
    pm = re.search(r"<artifactId>scala-maven-plugin</artifactId>", s)
    if not pm:
        fail("no scala-maven-plugin in pom")
    plugin_end = s.index("</plugin>", pm.end())
    block = s[pm.end():plugin_end]
    block = re.sub(r"\s*<scalaVersion>[^<]*</scalaVersion>", "", block)
    if "<args>" in block:
        block = re.sub(r"<args>.*?</args>", SCALA3_ARGS, block, flags=re.S)
    if "<configuration>" in block:
        # the plugin-level configuration is the last one in the block (after </executions>)
        ci = block.rfind("<configuration>") + len("<configuration>")
        extra = "\n                    <scalaVersion>${scala3.version}</scalaVersion>"
        if "<args>" not in block:
            extra += "\n                    " + SCALA3_ARGS
        block = block[:ci] + extra + block[ci:]
    else:
        block += """    <configuration>
                    <scalaVersion>${scala3.version}</scalaVersion>
                    %s
                </configuration>
            """ % SCALA3_ARGS
    s = s[:pm.end()] + block + s[plugin_end:]

    # --- scaladoc: the doc-jar goal of scala-maven-plugin 4.9.x passes Scala 2 scaladoc options; disable it
    s = re.sub(r"(<id>attach-javadocs</id>)",
               r"\1\n                        <phase>none</phase> <!-- scala3: doc-jar of scala-maven-plugin 4.9.x passes scala2 scaladoc options; disabled, see SCALA3_MIGRATION.md -->",
               s)

    # --- maven-compiler-plugin: release 17 (Java sources must be able to read Scala 3 class files)
    cm = re.search(r"<artifactId>maven-compiler-plugin</artifactId>", s)
    rel = """
                <configuration>
                    <release>${scala3.release}</release>
                </configuration>"""
    if cm:
        cend = s.index("</plugin>", cm.end())
        cblock = s[cm.end():cend]
        if "<release>" in cblock:
            cblock = re.sub(r"<release>[^<]*</release>", "<release>${scala3.release}</release>", cblock)
        elif "<configuration>" in cblock:
            cblock = cblock.replace("<configuration>", "<configuration>\n                    <release>${scala3.release}</release>", 1)
        else:
            cblock = rel + cblock
        s = s[:cm.end()] + cblock + s[cend:]
    else:
        bi = s.index("<plugins>", s.index("<build>")) + len("<plugins>")
        s = s[:bi] + """
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>""" + rel + """
            </plugin>
""" + s[bi:]
    return own, s


def register(module_dir):
    parent_dir, name = os.path.split(module_dir.rstrip("/"))
    parent_pom = os.path.join(parent_dir or ".", "pom.xml")
    p = open(parent_pom).read()
    line = "<module>%s</module>" % name
    new = "<module>%s-scala3</module>" % name
    if new in p:
        return parent_pom
    i = p.index(line)
    eol = p.index("\n", i)
    indent = p[p.rindex("\n", 0, i) + 1:i]
    p = p[:eol + 1] + indent + new + "\n" + p[eol + 1:]
    open(parent_pom, "w").write(p)
    return parent_pom


def main():
    if len(sys.argv) != 2:
        fail(__doc__)
    src = sys.argv[1].rstrip("/")
    dst = src + "-scala3"
    if not os.path.isfile(os.path.join(src, "pom.xml")):
        fail("%s has no pom.xml" % src)
    if os.path.exists(dst):
        fail("%s already exists; not overwriting" % dst)
    shutil.copytree(src, dst, ignore=shutil.ignore_patterns("target", "*.iml", ".idea"))
    own, pom = rewrite_pom(open(os.path.join(dst, "pom.xml")).read())
    open(os.path.join(dst, "pom.xml"), "w").write(pom)
    parent_pom = register(src)
    print("created %s (artifactId %s-scala3), registered in %s" % (dst, own, parent_pom))


if __name__ == "__main__":
    main()
