package Parser

import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

//@NonCPS
def  ArrayList call(String libDirPath){
    def res = []
    
    def json = readJSON file: "${libDirPath}/package.json"
    println "------- $json -----------"
    json["peerDependencies"].each { k, v ->
        println "keyyyyyyyyyyyy $k yyyyyyyyyek"
        if(k.startsWith("@")){
            res.add(k)
        }
    }

    return res.size()>0 ? res : null;
}
