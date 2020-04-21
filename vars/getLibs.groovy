/**
 * Projenin kütüphanelerine ait bilgileri
 *  Lib Adı, Dizini, Bağımlılıkları
 * toplayalım
 */
import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern
// import com.cloudbees.groovy.cps.NonCPS

//@NonCPS
def  Map<String, Paket> call(String prjDirPath) {
    def res = [:]
    
    def jsn = readJSON file: "${prjDirPath}/angular.json"
    jsn["projects"].each { k, v ->
        println "---------- $k --------------"
        if(jsn["projects"][k]["projectType"]=="library"){            
            res.put(k, new Paket(k, jsn["projects"][k]["root"], []))
        }
    }
    
    res
}