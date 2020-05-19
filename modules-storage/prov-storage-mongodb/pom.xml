<?xml version="1.0"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>org.openprovenance.prov</groupId>
        <artifactId>modules-storage</artifactId>
        <version>0.9.3</version>
    </parent>
    <artifactId>prov-storage-mongodb</artifactId>
    <name> |---- PROV-STORAGE-MONGODB</name>
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.openprovenance.prov</groupId>
            <artifactId>prov-model</artifactId>
            <version>0.9.3</version>
        </dependency>

        <dependency>
            <groupId>org.openprovenance.prov</groupId>
            <artifactId>prov-storage-api</artifactId>
            <version>0.9.3</version>
        </dependency>


        <dependency>
            <groupId>org.openprovenance.prov</groupId>
            <artifactId>prov-jsonld</artifactId>
            <version>0.9.3</version>
        </dependency>

        <dependency>
            <groupId>org.openprovenance.prov</groupId>
            <artifactId>prov-jsonld-xml</artifactId>
            <version>0.9.3</version>
        </dependency>

        <dependency>
            <groupId>org.openprovenance.prov</groupId>
            <artifactId>prov-interop-light</artifactId>
            <version>0.9.3</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson.version}</version>
            <scope>compile</scope>
        </dependency>

        <!-- to serialise with jackson in mongodb -->
        <dependency>
            <groupId>org.mongojack</groupId>
            <artifactId>mongojack</artifactId>
            <version>2.10.1</version>  <!-- 2.10.0 -->
        </dependency>

        <!-- to support uploading of csv mini templates -->
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-csv</artifactId>
            <version>1.7</version>
        </dependency>

        <!-- TESTING -->
        <dependency>
            <groupId>org.openprovenance.prov</groupId>
            <artifactId>prov-model</artifactId>
            <version>0.9.3</version>
            <classifier>tests</classifier>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.openprovenance.prov</groupId>
            <artifactId>prov-jsonld</artifactId>
            <version>0.9.3</version>
            <classifier>tests</classifier>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.openprovenance.prov</groupId>
            <artifactId>prov-jsonld-xml</artifactId>
            <version>0.9.3</version>
            <classifier>tests</classifier>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-nop</artifactId>
            <version>1.7.12</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.openprovenance.prov</groupId>
            <artifactId>prov-template-compiler</artifactId>
            <version>0.9.3</version>
            <scope>test</scope>
        </dependency>


    </dependencies>


    <build>

        <resources>
            <resource>
                <directory>src/main/resources</directory>
                <filtering>true</filtering>
            </resource>
        </resources>
        <testResources>
            <testResource>
                <directory>src/test/resources</directory>
                <filtering>true</filtering>
            </testResource>
            <testResource>
                <directory>src/test/config</directory>
                <filtering>true</filtering>
                <includes>
                    <include>log4j.xml</include>
                </includes>
            </testResource>
        </testResources>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>11</source>
                    <target>11</target>
                </configuration>
            </plugin>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-javadoc-plugin</artifactId>
                <version>3.1.1</version>
                <configuration>
                    <source>1.9</source>
                </configuration>
            </plugin>
        </plugins>


    </build>

</project>
