/**
 * Parametrede verilen k?t?kte, parametredeki 
 * paket ve versiyon y?kl? m? kontrol eder
 */

def call(String registry, String pgk, String version){
    println "----------------- isPackagePublished -------------------"
    
    try {
        println ">>> registry: $registry , pgk: $pgk , version: $version "
        // sh "curl -s http://localhost:4873/@kapsam2/kutup11 | awk '/0.0.1/{count++;} END{print count=="" ? 0 : count}'"
        def count = sh (
            label: "REST sorgusuyla verdaccio kontrol ediliyor",
            returnStatus: true,
            script: "curl -s $registry/$pgk | awk '/$version/{count++;} END{print count=='' ? 0 : count}'"
        )
    
        echo "--->>> is published - Version say?s?: $count"

        return count as Integer
        }
    } catch(err) {
        echo "---*** Hata (isPackagePublished): istisna oldu (Exception: $err)"  
        throw 
    }

}