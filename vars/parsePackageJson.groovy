package Parser

import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

//@NonCPS
def  ArrayList call(String absolutePackageJsonPath){
    def res = []
    
    def json = readJSON file:absolutePackageJsonPath
    json["peerDependencies"].each { key, value ->
        if(key.startsWith("@")){
            res.add(key)
        }
    }

    return res.size()>0 ? res : null;
}
