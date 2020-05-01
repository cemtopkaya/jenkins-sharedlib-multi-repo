package ulak.Parser

import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

class AngularParser {

//    @NonCPS
    def static Map<String, Paket> parseAngularJson(String file){
        println "parseAngularJson-parseAngularJson-parseAngularJson-parseAngularJson-parseAngularJson"
        def res = [:]
        
        // def path = "${projectDir}/angular.json".replace("\\","/")
        // // def data = readFile(file: 'developer/angular.json')
        // // println("data........data....:${data}")
        // println "File.class.name: ${File.class.name}"
        // def file = new File(path)
        println "file::::::: ${file}"
        def lines = file.split("\r\n")
        println "lines.size()::::::: ${lines.size()}"

        for (def idx=0;idx<lines.size();idx++){
            def line = lines[idx]   
            println "line:${line} - idX:${idx}"
            def reg =  ~/.*"projectType": "library",/
            if(reg.matcher(line).matches() && (idx-1>0) && (idx+1)<lines.size()){
                // println "${idx} \t Current Line: ${line} \t Prev Line: ${lines[idx-1]} \t Next Line: ${lines[idx+1]}"
                // lines[idx-1].grep(~/(@cinar.*)>?"/){ match, a -> name = a }
                // lines[idx+1].grep(~/(projects.*)>?"/){ match, a -> root = a }
                def matcher = lines[idx+1] =~ /((projects.*(?=")))/                
                def root = matcher[0][0]
                
                matcher = lines[idx-1] =~ ~/((@cinar.*(?=")))/
                def name = matcher[0][0]

                
                // println "WORKSPACE: ${env.WORKSPACE}"
                // sh "pwd"
                // def dir = "${env.WORKSPACE}/developer/package.json"
                // println "dirrrrrrrrrrrr: ${dir}"
                // res.put(name, new Paket(name, root, PackageParser.parseJson(dir)))
            }
        }

        return res
    }

}
