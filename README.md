# Empires
## Development Setup
### Installing Java for Ubuntu

First you will need to update the package index using:
`sudo apt update`

You can check if Java is already installed using:
'java -version'

Execute the following command to install OpenJDK:
'sudo apt install default-jre'

To install the Java Development Kit (JDK) in addition to the JRE in order to compile and run specific Java-based software install the JDK, execute the following command:
'sudo apt install default-jdk'

### Dowloading and Running Build Tools

Make a directory for the buildtools to store the files using:
'mkdir buildtools'
 
Change directory into the buildtools directly created using:
'cd buildtools'

Pull the lates version of the buildtools using:
'curl -o BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar'

Build the lates release of the build tools using:
'java -jar BuildTools.jar'
