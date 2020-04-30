/**
 * Parametrede verilen k?t?kte, parametredeki 
 * paket ve versiyon y?kl? m? kontrol eder
 */

//package org.hede

static def call(String registry, String pgk, String version){
    // TODO: Check if curl exists try fromApi otherwise fromNpmView
    return fromApi(registry, pgk, version)
}

def fromApi(String registry, String pgk, String version){
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

def fromNpmView(String registry, String pgk, String version){
    npmViewScript = "npm view ${pgk}@${version} ${registry}"
    echo "** Ayn? versiyon kullan?lm?? m? kontrol? i?in script > npmViewScript: ${npmViewScript}"
    /* E?er npm view aranan paketi bulamazsa sh komutu 1 (Error 404) ile hata f?rlatarak ??kacak!
    * Bu y?zden kod k?r?lmas?n diye "returnStatus: true" ile sh execute edilecek ve exit code okunacak.
    * Exit code 0'dan farkl?ysa ise yani "npm ERR! code E404" ile npm view hata f?rlatarak ??k?? yapm??sa bilece?iz ki; paket yok!
    * E?er normal ??k?? yapm??sa bu kez ??kt?y? almak i?in "returnStdout: true" anahtar?yla tekrar paket sorgulanacak
    **/
    
    npmViewStatusCode = sh(returnStatus: true, script: "$npmViewScript")
    if(npmViewStatusCode == 1){
        result = false
    }else {
        try {
            checkVersionPublished = sh(
                label: "$npmViewScript",
                returnStdout: true, 
                script: "${npmViewScript} | wc -m"
            ).trim() as Integer
            
            result = checkVersionPublished > 0
        }
        catch (err) {
            println "npm view hata f?rlatt?: $err"
        }
    }
}