package Sorter

import Model.Paket
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern
import com.cloudbees.groovy.cps.NonCPS

class BuildSorter {

    @NonCPS
    def static getSortedLibraries(Map<String, Paket> param){
        def libs = param.keySet();
        def deps = getDependencies(param)

        def fnSort = { String a, String b ->    
                //println "\n>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"
                //println "a: $a -- b: $b"
                //println "   a.dependencies: ${param.get(a).dependencies}"
                //println "   b.dependencies: ${param.get(b).dependencies}"
                //println "   a.dep.contains(b): ${param.get(a).dependencies?.contains(b)}"
                //println "a: $a -- b: $b -- \n\t a.dependencies: ${param.get(a).dependencies}"
                if(param.get(a).dependencies == null) return -1;
                if(param.get(a).dependencies.contains(b)) return 1;
                if(param.get(b).dependencies.contains(a)) return -1;
                if(deps.contains(a)) return -1;
                // bağımlılıklarda yoksa en son derlensin
                return 1;
            }

        def sorted = libs.sort{ a,b->fnSort(a,b) }

        return sorted
    }

    @NonCPS
    def static ArrayList getDependencies(Map<String, Paket> p1){
        def libModuleNames = p1.keySet();
        def depNames = []
        p1.eachWithIndex { entry, index ->

            // println "$index Key: $entry.key \n\t Value: $entry.value \n\t Path: $entry.value.path \n\t Dependencies: $entry.value.dependencies"

            entry.value.dependencies.each{
                d-> // println "\n\t Dep Name: ${d} \n\t $depNames \n\t depNames.contains(d): ${depNames.contains(d)} \n***************"
                    if(!depNames.contains(d)) {
                        depNames.add(d)
                    }
            }
        }

        // println "---------- depNames: $depNames"

        return depNames
    }

}
