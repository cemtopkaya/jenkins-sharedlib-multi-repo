package ulak.Parser

import ulak.Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

class PackageParser {

    @NonCPS
    def static ArrayList parseJson(String jsonPath){
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
    
    static String getPackageScopedName(String jsonPath){
        println "----------------- getPackageScopedName -----------------"

        def packageJsonPath = jsonPath
        try {
            def json = readJSON(file: packageJsonPath)
            
            String name = json.name
            echo "---*** Paketin adı > $name"
            return name
        }
        catch (err) {
            println "---*** Hata (getPackageScopedName): $packageJsonPath yolu readJSON ile okurken istisna oldu (Exception: $err)"
            throw err  
        }
    }
    static String getPackageVersion(String jsonPath){
        println "----------------- getPackageVersion -----------------"

        def packageJsonPath = jsonPath
        try {
            def json = readJSON(file: packageJsonPath)
            
            String version = json.version
            echo "---*** Paketin versiyonu > $version"
            return version
        }
        catch (err) {
            println "---*** Hata (getPackageVersion): $packageJsonPath yolu readJSON ile okurken istisna oldu (Exception: $err)"
            throw err  
        }
    }

}
