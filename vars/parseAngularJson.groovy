import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern
import com.cloudbees.groovy.cps.NonCPS

@NonCPS
def  Map<String, Paket> call(String prjDirPath) {
    println "parseAngularJson-parseAngularJson-parseAngularJson-parseAngularJson-parseAngularJson"
    def res = [:]
    
    def jsn = readJSON file: "${prjDirPath}/angular.json"
    println "---------- jsn --------------"
    println jsn
    return null
    def file = readFile file: "${prjDirPath}/angular.json"
    def lines = file.split("\n")
    println "lines.size()::::::: ${lines.size()}"

    for (def idx=0;idx<lines.size();idx++){
        line = lines[idx]   
        // println "line:${line} - idX:${idx}"
        def reg =  ~/.*"projectType": "library",/
        if(reg.matcher(line).matches() && (idx-1>0) && (idx+1)<lines.size()){
            // println "${idx} \t Current Line: ${line} \t Prev Line: ${lines[idx-1]} \t Next Line: ${lines[idx+1]}"
            // lines[idx-1].grep(~/(@cinar.*)>?"/){ match, a -> name = a }
            // lines[idx+1].grep(~/(projects.*)>?"/){ match, a -> root = a }
            def matcher = lines[idx+1] =~ /((projects.*(?=")))/
            def root = matcher[0][0]
            
            matcher = lines[idx-1] =~ ~/((@.*(?=")))/
            def name = matcher[0][0]

            println "name: ${name}, root: ${root}"
            res.put(name, new Paket(name, root, []))
        }
    }

    return res
}