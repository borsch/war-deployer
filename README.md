## WAR deployer

## Create binary file
You need to configure Java environment. a
    
- add `JAVA_HOME` environment variable
- add `M2_HOME` environment variable

in the source directory run `mvn clean install` from command line or terminal
in newly created directory `target` you can find `war-deployer.jar`


## Using
To deploy new version to Tomcat move `war-deployer.jar` to location, where your `war` files is located. 
By default it's `CATALINA_HOME\webapps`. You need to add configuration file `config.cfg` to same directory. 
Configuration file must have following content

    old-war-name=exploaded_folder
    new-war-url=http://example.com/path/to/download/new.war
    start-cmd=net start tomcat
    stop-cmd=net stop tomcat
    

- `old-war-name` - is a name of exploded folder. both folder and `war` file will be proceed. For example if you enter `my_website`, than
`/my_website` folder and `my_website.war` will be proceed
- `new-war-url` - URL location of new `war` file
- `stop-cmd` - this command will be executed after successful new war downloaded and before zipping process
- `start-cmd` - this command will be executed in the end of process

when you configure everything, than in the command line or terminal you can enter
`java -jar war-deployer.jar`