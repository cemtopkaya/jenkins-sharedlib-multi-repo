package Parser

import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

@NonCPS
def  ArrayList call(String fileContent){
    def res = []
    
    // def lines = new File("${jsonPath}/package.json").readLines()
    def lines = fileContent.split("\n")

    lines.eachWithIndex { line, idx ->
        def matcher = line =~ /@.*(?=":)/
        if(matcher.size()>0){
            def dependency = matcher[0]
            res.add(dependency)
        }
    }

    return res.size()>0 ? res : null;
}
