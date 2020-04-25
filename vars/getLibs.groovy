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
    println "pathAngularJson: $pathAngularJson"
    
    def res = [:]
    String pathAngularJson = "$prjDirPath/angular.json"
    
    try{
        println ">>> angular.json var mı?"
        println fileExists pathAngularJson

        def jsn = readJSON file: pathAngularJson
        jsn["projects"].each { k, v ->
            println "---------- $k --------------"
            if(jsn["projects"][k]["projectType"]=="library"){            
                res.put(k, new Paket(k, jsn["projects"][k]["root"], []))
            }
        }
        println ":::::::.res::::::"
        println res
        println res.getClass()   
        env.RES = res
        return res

    }catch(err){
        println "---*** Hata (getLibs): $pathAngularJson işlenirken istisna oldu (Exception: $err)"   
    }
    
}