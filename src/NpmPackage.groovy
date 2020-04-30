class NpmPackage{

    String PackageScope
    String PackageName
    String Version

    static NpmPackage parse(String fullName){
        String scope, name, version

        if(fullName[0]=="@"){
            scope = fullName.split("/")[0]
        }

        int idx = scope ? 1 : 0
        name = fullName.split("/")[idx]

        if(name?.split("@").size()>0){
            name = name.split("@")[0]
            version = name.split("@")[1]
        }

        println "scope: $scope, name: $name, version: $version"
        return new NpmPackage(scope, name, version)
    }
    
    NpmPackage(String packageScope, String packageName, String version){
        this.PackageName = packageName
        this.Version = version
        this.PackageScope = packageScope
    }

    Boolean isPublished(String registry){
        this.fromApi(registry, this.PackageName)
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
        
            echo "--->>> (isPackagePublished) >>> '$registry' Kütüğünde '$pgk@$version' versiyonu var mı: "+isExist

            return isExist
        } catch(err) {
            echo "---*** Hata (isPackagePublished): istisna oldu (Exception: $err)"  
            throw err
        }

    }

    def fromNpmView(String registry, String pgk, String version){
        npmViewScript = "npm view ${pgk}@${version} ${registry}"
        echo "** Aynı versiyon kullanılmış mı kontrolü için script > npmViewScript: ${npmViewScript}"
        /* Eğer npm view aranan paketi bulamazsa sh komutu 1 (Error 404) ile hata fırlatarak çıkacak!
        * Bu yüzden kod kırılmasın diye "returnStatus: true" ile sh execute edilecek ve exit code okunacak.
        * Exit code 0'dan farklıysa ise yani "npm ERR! code E404" ile npm view hata fırlatarak çıkış yapmışsa bileceğiz ki; paket yok!
        * Eğer normal çıkış yapmışsa bu kez çıktıyı almak için "returnStdout: true" anahtarıyla tekrar paket sorgulanacak
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
                println "npm view hata fırlattı: $err"
            }
        }
    }



}