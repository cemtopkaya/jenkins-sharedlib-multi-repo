package Parser

import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

def  ArrayList call(String jsonPath){
    def res = []
    
    def lines = new File(jsonPath).readLines()

    lines.eachWithIndex { line, idx ->
        def matcher = line =~ /@cinar.*(?=":)/
        if(matcher.size()>0){
            def dependency = matcher[0]
            res.add(dependency)
        }
    }

    return res.size()>0 ? res : null;
}
