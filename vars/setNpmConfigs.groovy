/**
 * SCM'den verileri çekmek için bir ara katman olsun
 */
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern
// import com.cloudbees.groovy.cps.NonCPS

//@NonCPS
def call(Map<String,String> scopeRegistries){
    println "----------------- setNpmConfigs -------------------"
    echo "params.NPM_REGISTRY:${params.NPM_REGISTRY}"
    //sh "pwd && mkdir branch && cd branch && pwd"

    try {
        scopeRegistries.each{
            def scope = it.key?:""
            echo "scope: $scope"
            if(scope){
                scope+=":"
            }
            echo "scope: $scope"
            script = "npm config set ${scope}registry ${it.value} --userconfig ./.npmrc"
            echo "script: $script"
        }
    }catch(err){
        echo "---*** Hata (checkoutSCM): istisna oldu (Exception: $err)"  
        throw err 
    }

}