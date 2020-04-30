/**
 * Parametrede verilen k?t?kte, parametredeki 
 * paket ve versiyon y?kl? m? kontrol eder
 */

def call(String registry, String pgk, String version){
    println "----------------- isPackagePublished -------------------"
    
    try {
        // println ">>> registry: $registry , pgk: $pgk , version: $version "
        // sh "curl -s http://192.168.13.33:4873/@cinar/cn-main | awk '/version.*:.*0.0.1/{count++;} END{isExist = (length(count)>0); print isExist}'"
        
        String script = "curl -s $registry/$pgk | awk '/version.*:.*$version/{count++;} END{isExist = (length(count)>0); print isExist}'"
        println "script: $script"
        String output = sh (
            label: "REST sorgusuyla verdaccio kontrol ediliyor: $script",
            returnStdout: true,
            script: script
        ).trim()
        Boolean isExist = ( "1" == output)
    
        echo "--->>> (isPackagePublished) >>> '$registry' K?t???nde '$pgk@$version' versiyonu var m?: "+isExist

        return isExist
    } catch(err) {
        echo "---*** Hata (isPackagePublished): istisna oldu (Exception: $err)"  
        throw err
    }

}