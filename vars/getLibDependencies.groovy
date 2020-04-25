/**
 * ./projects/@kapsam/kütüp_adı/package.json 
 * içindeki bağımılıkları bulalım
 */
package Parser

import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern

// import com.cloudbees.groovy.cps.NonCPS
//@NonCPS
def  ArrayList call(String libDirPath){
    println "------------------ getLibDependencies ---------------"
    
    def res = []    
    String packageJson ="$libDirPath/package.json"
    try{
        
        def json = readJSON file: packageJson
        json["peerDependencies"].each { k, v ->
            if(k.startsWith("@")){
                res.add(k)
            }
        }

        return res.size()>0 ? res : null;    
    }catch(err){
        println "---*** Hata (getLibDependencies): $packageJson işlenirken istisna oldu (Exception: $err)"   
    }
}
