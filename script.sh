mvn clean package
jpackage --name "New Root Academy Manager" --input target --main-jar newrootacademymanager-1.0.0.jar --main-class com.fathi.newrootacademymanager.Main --app-version 1.0.0 --icon icon.ico --win-dir-chooser --win-menu --win-shortcut
mv "New Root Academy Manager-1.0.0.exe" dist/