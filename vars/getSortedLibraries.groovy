import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern

import com.cloudbees.groovy.cps.NonCPS

@NonCPS
def ArrayList call(Map<String, Paket> param){
    println "------------ getSortedLibraries ---------"

    def libs = param.keySet();
    def deps = getInternalDependencies(param)

    def fnSort = { String a, String b ->    
            //println "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
            //println "a: $a -- b: $b"
            //println "   a.dependencies: ${param.get(a).dependencies}"
            //println "   b.dependencies: ${param.get(b).dependencies}"
            //println "   a.dep.contains(b): ${param.get(a).dependencies?.contains(b)}"
            //println "a: $a -- b: $b -- \n\t a.dependencies: ${param.get(a).dependencies}"

            if(param.get(a).dependencies == null) return -1
            if(param.get(a).dependencies.contains(b)) return 1
            if(param.get(b).dependencies.contains(a)) return -1
            if(deps.contains(a)) return -1
            // bağımlılıklarda yoksa en son derlensin
            return 1
        }

    def sorted = libs.sort{ a,b->fnSort(a,b) }

    return sorted
}

@NonCPS
def ArrayList getInternalDependencies(Map<String, Paket> collection){


    def libModuleNames = collection.keySet();
    def depNames = []

    try {
        for (entry in collection) {
            println "Key: $entry.key \n\t Value: $entry.value \n\t\t Path: $entry.value.path \n\t\t Dependencies: $entry.value.dependencies"
            for (d in entry.value.dependencies) {
                if(d.startsWith(entry.key.split("/")[0]) && !depNames.contains(d)) {
                    depNames.add(d)
                }
            }
        }

        println "---*** depNames: $depNames"
        return depNames

    }catch(err){
        println "---*** Hata (getInternalDependencies): istisna oldu (Exception: $err)"   
    }
}
