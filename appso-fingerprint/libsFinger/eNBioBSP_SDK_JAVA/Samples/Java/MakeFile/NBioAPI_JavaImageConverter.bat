rmdir /s /q build_ImgConverter
mkdir build_ImgConverter
javac -Xlint:deprecation -classpath ..\..\..\Lib\NBioBSPJNI.jar;..\..\..\Lib\swing-layout-1.0.3.jar ..\src\NBioAPI_JavaImageConverter.java -d .\build_ImgConverter
java -Xincgc -Xmn100m -Xms512m -Xmx512m -XX:PermSize=256m -XX:MaxPermSize=256m -classpath ./build_ImgConverter;../../../Lib/NBioBSPJNI.jar;../../../Lib/swing-layout-1.0.3.jar NBioAPI_JavaImageConverter