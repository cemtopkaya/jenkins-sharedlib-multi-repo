/**
 * SCM'den verileri çekmek için bir ara katman olsun
 */
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern
// import com.cloudbees.groovy.cps.NonCPS

//@NonCPS
def call(){
    println "----------------- setNpmConfigs -------------------"
    echo "params.NPM_REGISTRY:${params.NPM_REGISTRY}"
    //sh "pwd && mkdir branch && cd branch && pwd"

    try {
        
    }catch(err){
        echo "---*** Hata (checkoutSCM): istisna oldu (Exception: $err)"  
        throw err 
    }

}