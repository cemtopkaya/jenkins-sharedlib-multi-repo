import java.io.*;
import groovy.io.*;
import java.util.regex.*;
import Parser.AngularParser;
import Sorter.BuildSorter; 

// def classLoader = ClassLoader.systemClassLoader
// while (classLoader.parent) {
//     classLoader = classLoader.parent
// }
// classLoader.addURL(new File("/var/lib/jenkins/workspace/egsdloen-
// logging/mysql/mysql.jar").toURL())

def projectPath = "C:/Users/cem.topkaya/git/gui_nrf_test/"
def map = AngularParser.parseAngularJson(projectPath)
//println map
res = BuildSorter.getSortedLibraries(map)
println res
