# Empires
## Development Setup
### Installing Java for Ubuntu

First you will need to update the package index using:

`sudo apt update`

You can check if Java is already installed using:

`java -version`

Execute the following command to install OpenJDK:

`sudo apt install default-jre`

To install the Java Development Kit (JDK) in addition to the JRE in order to compile and run specific Java-based software install the JDK, execute the following command:

`sudo apt install default-jdk`

### Dowloading and Running Build Tools

Make a directory for the buildtools to store the files using:

`mkdir buildtools`
 
Change directory into the buildtools directly created using:

`cd buildtools`

Pull the lates version of the buildtools using:

`curl -o BuildTools.jar https://hub.spigotmc.org/jenkins/job/BuildTools/lastSuccessfulBuild/artifact/target/BuildTools.jar`

Build the lates release of the build tools using:

`java -jar BuildTools.jar`

### Creating and running the Server

If you just completed the pervious section, cd out of the directory. 

`cd ..`

Make a new directory for the server to store the files using:

`mkdir minecraft_server`

Copy the .jar file created from the build tools using:

`cp spigot*.jar ../minecraft_server`

Create a bash file to start the server using:

`echo "#!/bin/sh" > start.bash`

Then copy the code into the file using a text editor.

`echo "java -Xms2G -Xmx2G -jar spigot*.jar nogui" >> start.bash`

make the file exicutable using:

`chmod +x start.bash`

You can the run the server using:

`./start.bash`

If this is the first time the server is launched it will populate the folder with several files. Remember to change the eula.txt file and set the agreement to true before the server will fully launch.
