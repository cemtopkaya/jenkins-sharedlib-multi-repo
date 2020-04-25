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
def Map<String, Paket> call(String prjDirPath) {
    println "------------------ getLibs ---------------"
    
    def res = [:]
    String pathAngularJson = "$prjDirPath/angular.json"
    
    try{
        def jsn = readJSON file: pathAngularJson
        jsn["projects"].each { k, v ->
            println "---------- $k --------------"
            if(jsn["projects"][k]["projectType"]=="library"){            
                res.put(k, new Paket(k, jsn["projects"][k]["root"], []))
            }
        }

        return res

    }catch(err){
        println "---*** Hata (getLibs): $pathAngularJson işlenirken istisna oldu (Exception: $err)"   
    }
    
}