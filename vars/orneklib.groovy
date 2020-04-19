import Parser.AngularParser;
import Sorter.BuildSorter; 

import com.cloudbees.groovy.cps.NonCPS

@NonCPS
def call(String projectPath) {
    // Any valid steps can be called from this code, just like in other
    // Scripted Pipeline
    println "Merhaba, Cengoooo::: $projectPath"
    
    def map = AngularParser.parseAngularJson(projectPath)
    //println map
    res = BuildSorter.getSortedLibraries(map)
    println res

}