import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern
import com.cloudbees.groovy.cps.NonCPS

@NonCPS
def  Map<String, Paket> call(String file) {
    println "parseAngularJson-parseAngularJson-parseAngularJson-parseAngularJson-parseAngularJson"
    def res = [:]
    
    // def path = "${projectDir}/angular.json".replace("\\","/")
    // // def data = readFile(file: 'developer/angular.json')
    // // println("data........data....:${data}")
    // println "File.class.name: ${File.class.name}"
    // def file = new File(path)
    println "file::::::: ${file}"
    def lines = file.split("\n")
    println "lines.size()::::::: ${lines.size()}"

    for (def idx=0;idx<lines.size();idx++){
        line = lines[idx]   
        println "line:${line} - idX:${idx}"
        def reg =  ~/.*"projectType": "library",/
        if(reg.matcher(line).matches() && (idx-1>0) && (idx+1)<lines.size()){
            // println "${idx} \t Current Line: ${line} \t Prev Line: ${lines[idx-1]} \t Next Line: ${lines[idx+1]}"
            // lines[idx-1].grep(~/(@cinar.*)>?"/){ match, a -> name = a }
            // lines[idx+1].grep(~/(projects.*)>?"/){ match, a -> root = a }
            def matcher = lines[idx+1] =~ /((projects.*(?=")))/                
            
            println "root matcher[0] ${matcher[0]}"
            def root = matcher[0][0]
            println "root: ${root}"
            
            matcher = lines[idx-1] =~ ~/((@.*(?=")))/
            println "name matcher[0] ${matcher[0]}"
            def name = matcher[0][0]
            println "name: ${name}"

            
            println "WORKSPACE: ${WORKSPACE}"
            def dir = "${WORKSPACE}/developer/package.json"
            println "dirrrrrrrrrrrr: ${dir}"
            def packageJsonPath = "./developer/${root}/package.json"
            println "packageJsonPath: ${packageJsonPath}"
            // def packageJsonLines = readFile(file: packageJsonPath)
            def packageJsonLines = new File(packageJsonPath).readLines()
            println "packageJsonLines: ${packageJsonLines}"
            res.put(name, new Paket(name, root, parsePackageJson(dir)))
        }
    }

    return res
}