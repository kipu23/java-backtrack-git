
TODO: consolidate!


#Using git SHA number in java program

For using the own git SHA number in a java program, this information should be written into the manifest file of the jar. To do this, the 'buildnumber-maven-plugin' maven plugins should be used, and configued properly in the pom file:

    <pluginManagement>
		...
        <plugin>
          <artifactId>maven-jar-plugin</artifactId>
          <version>3.0.2</version>
                <configuration>
					<finalName>${project.artifactId}-${project.version}</finalName> 
                    <archive>
                        <manifest>
							<addDefaultImplementationEntries>true</addDefaultImplementationEntries>
							<addClasspath>true</addClasspath>
							<mainClass>com.krisz.helloworld.HelloWorld</mainClass>
                        </manifest>
						<manifestEntries>
							<Git-SHA>${buildNumber}</Git-SHA>
						</manifestEntries>
                    </archive>
                </configuration>
        </plugin>
		<plugin>
			<groupId>org.codehaus.mojo</groupId>
			<artifactId>buildnumber-maven-plugin</artifactId>
			<version>1.4</version>
			<executions>
				<execution>
					<phase>validate</phase>
					<goals>
						<goal>create</goal>
					</goals>
				</execution>
			</executions>
			<configuration>
				<doCheck>false</doCheck>
				<doUpdate>false</doUpdate>
			</configuration>
		</plugin>
		...
      </plugins>
    </pluginManagement>
    <plugins>
		...
		<plugin>
			<groupId>org.codehaus.mojo</groupId>
			<artifactId>buildnumber-maven-plugin</artifactId>
		</plugin>
		...
    </plugins>

In the above example, between the manifestEntries tags of the maven-jar-plugin, we specify a custom main attribute, called 'Git-SHA'. This can be named anyway, but should be remembered, because the git SHA number value will be written with this key into the manifest file, and this attribute will be used in the java code as well.

	
The repository url should be present in the pom file as well:

	<scm>
		<url>scm:git:https://github.conti.de/AutoITMA-Middleware/TEST.git</url>
		<connection>scm:git:https://github.conti.de/AutoITMA-Middleware/TEST.git</connection>
		<developerConnection>scm:git:https://github.conti.de/AutoITMA-Middleware/TEST.git</developerConnection>
	</scm>




Now, with a correctly set up pom file, we can build the jar file as usual, with the following command:

>mvn clean install

The manifest file (META-INF/MANIFEST.MF) inside the created .jar file will contain a main attribute called GIT-SHA:

Manifest-Version: 1.0
Created-By: Apache Maven 3.6.3
Built-By: uic87769
Build-Jdk: 11
Implementation-Title: helloworld
Implementation-Version: 1.0
Implementation-Vendor-Id: com.krisz
Main-Class: com.krisz.helloworld.HelloWorld
Git-SHA: dc0c63f314663d8f685a35f1185ce3447f1583fe

To access this information during runtime within the java program, we should use the resources class:

        try {
			Enumeration<URL> resources = HelloWorld.class.getClassLoader().getResources("META-INF/MANIFEST.MF");
			while (resources.hasMoreElements()) {
				Manifest manifest = new Manifest(resources.nextElement().openStream());
				Attributes attr = manifest.getMainAttributes();
				String shaNumber = attr.getValue("Git-SHA");
				System.out.println("Git SHA number: " +  shaNumber);
			}
		} catch (IOException E) {
        	System.err.println("Error: Manifest file not found!");
		}

Working example can be found here:
https://github.conti.de/AutoITMA-Middleware/TEST/tree/gh-pages/java-helloworld



# usage

mvn clean compile assembly:single

java -jar target/java-backtrack-git-1.0-jar-with-dependencies.jar
