package ulak.Node

import ulak.Model.* 
import ulak.Parser.* 

class NpmPackage{

    String PackageScope
    String PackageName
    String Version
    private static def Context

    String getScopedPackageName(){
        return PackageScope ? PackageScope+"/"+PackageName : PackageName
    } 

    NpmPackage(def context, String packageScope, String packageName, String version){
        this.PackageName = packageName
        this.Version = version
        this.PackageScope = packageScope
        Context = context
    }

    static NpmPackage parseFromFullName(def context, String fullName){
        String scope, name, version

        if(fullName[0]=="@"){
            scope = fullName.split("/")[0]
        }

        int idx = scope ? 1 : 0
        name = fullName.split("/")[idx]

        println "name?.split(@).size(): "+name?.split("@")
        if(name?.split("@").size()>0){
            String[] arr = name.split('@')
            name = arr[0]
            version = arr[1]
        }

        println "scope: $scope, name: $name, version: $version"
        return new NpmPackage(context, scope, name, version)
    }

    static NpmPackage parseFromPackageJson(def context, String packageJsonPath){
        println "------------------ parseFromPackageJson ---------------"
        
        println "------- packageJsonPath: $packageJsonPath"
        
        try{
            def jsn = readJSON file: packageJsonPath
            return NpmPackage.parseFromFullName(context, jsn.name+"@"+jsn.version)
        }catch(err){
            println "---*** Hata (parseFromPackageJson): $packageJsonPath işlenirken istisna oldu (Exception: $err)"   
            throw err
        }        
    }

    @Override
    String toString(){
        return "PackageScope: $PackageScope, PackageName: $PackageName, Version: $Version"
    }

    Boolean isPublished(String registry){
        String pkg = PackageScope ? PackageScope+"/"+PackageName : PackageName
        return NpmPackage.fromApi(registry, pkg, Version)
    }

    private static Boolean fromApi(String registry, String pgk, String version){
        println "----------------- isPackagePublished -------------------"
        
        try {
            // println ">>> registry: $registry , pgk: $pgk , version: $version "
            // Context.sh "curl -s http://192.168.13.33:4873/@cinar/cn-main | awk '/version.*:.*0.0.1/{count++;} END{isExist = (length(count)>0); print isExist}'"
            
            String script = "curl -s $registry/$pgk | awk '/version.*:.*$version/{count++;} END{isExist = (length(count)>0); print isExist}'"
            println "script: $script"
            String output = Context.sh (
                label: "REST sorgusuyla verdaccio kontrol ediliyor: $script",
                returnStdout: true,
                script: script
            ).trim()
            Boolean isExist = ( "1" == output)
        
            println "--->>> (isPackagePublished) >>> '$registry' Kütüğünde '$pgk@$version' versiyonu var mı: "+isExist

            return isExist
        } catch(err) {
            println "---*** Hata (isPackagePublished): istisna oldu (Exception: $err)"  
            throw err
        }

    }

    private def fromNpmView(String registry, String pgk, String version){
        npmViewScript = "npm view ${pgk}@${version} ${registry}"
        println "** Aynı versiyon kullanılmış mı kontrolü için script > npmViewScript: ${npmViewScript}"
        /* Eğer npm view aranan paketi bulamazsa Context.sh komutu 1 (Error 404) ile hata fırlatarak çıkacak!
        * Bu yüzden kod kırılmasın diye "returnStatus: true" ile Context.sh execute edilecek ve exit code okunacak.
        * Exit code 0'dan farklıysa ise yani "npm ERR! code E404" ile npm view hata fırlatarak çıkış yapmışsa bileceğiz ki; paket yok!
        * Eğer normal çıkış yapmışsa bu kez çıktıyı almak için "returnStdout: true" anahtarıyla tekrar paket sorgulanacak
        **/
        
        npmViewStatusCode = Context.sh(returnStatus: true, script: "$npmViewScript")
        if(npmViewStatusCode == 1){
            result = false
        }else {
            try {
                checkVersionPublished = Context.sh(
                    label: "$npmViewScript",
                    returnStdout: true, 
                    script: "${npmViewScript} | wc -m"
                ).trim() as Integer
                
                result = checkVersionPublished > 0
            }
            catch (err) {
                println "npm view hata fırlattı: $err"
                throw err
            }
        }
    }

    static def setNpmConfigRegistries(def ctx, Map<String,String> scopeRegistries, Boolean isGlobal=false){
        println "----------------- setNpmConfigRegistries -------------------"
        
        println "ctx: $ctx"
        
        try {
            scopeRegistries.each{
                def scope = it.key?:""
                println ">> scope: $scope"
                def key = scope ? "$scope:registry" : "registry"
                def value = it.value
                
                def script = "npm config set $key $value ${isGlobal ? '' : '--userconfig ./.npmrc'}"
                ctx.sh(
                    label: "npm config set registry : $script",
                    script: script,
                    returnStdout: false
                )
            }
        } catch(err) {
            println "---*** Hata (setNpmConfigRegistries): istisna oldu (Exception: $err)"  
            throw err
        }

    }

    def buildAngularPackage(){
        println "----------------- buildAngularPackage -----------------"
        sh "pwd"
        catchError (buildResult: "ABORTED", stageResult:"FAILURE"){
            Context.sh (
                label:"NPM Package Building ($getScopedPackageName())",
                returnStdout: false,
                script: "ng build $getScopedPackageName()"
            )
        }
    }

    def unpublish(String registry, String packageVersion=null){
        println "----------------- unpublish -----------------"

        def script = "npm unpublish ${getScopedPackageName()}@${packageVersion?:Version}  --regitry=$registry"
        def label = "Unpublish Package: $script"
        try {
            Context.sh (
                label: label,
                returnStatus: false,
                script: script
            )
        }
        catch (err) {
            println "---*** Hata (unpublish): $script çalıştırılırken istisna oldu (Exception: $err)"
        }
    }

    def publish(String registry, String packageSrcPath, Boolean force=false){
        println "----------------- publishIfNeeded -----------------"
                    
        println "NPM Package Publishing (${getScopedPackageName()})"
        Context.dir(packageSrcPath){
            try {
                def script = "npm publish  ${force ? '--force' : ''}   ${registry ? '--registry='+registry : ''}"
                def label = "Publishing: ${Context.pwd()} -- $script"
                                
                def shStatusCode = Context.sh (
                    label: label,
                    script: script,
                    returnStatus: true
                )
            }
            catch (err) {
                println "---*** Hata (publishIfNeeded): istisna oldu (Exception: $err)"
                throw err
            }
        }
    }

    static def installPackages(def ctx, String sourceFolder, String registry, String[] args=[]){
        Context = ctx
        println ">>> sourceFolder: "+sourceFolder
        Context.dir(sourceFolder){
            Boolean is_nodemodules_exits = Context.fileExists("$sourceFolder/node_modules")

            if( is_nodemodules_exits == false){
                println "*** NODE_MODULES Yok! NPM paketlerini yükleyeceğiz"
                args.push("--registry "+registry)
                String npm_install = "npm install "+args.join(" ")
                //sh "npm  --no-bin-links --cache-min Infinity install"
                Context.sh "pwd && " + npm_install
            }else{
                println "*** NODE_MODULES var ve tekrar NPM paketlerini yüklemeyelim"
            }
        }
    }

    static def npmLogin(_userName, _pass, _email="jenkins@service.com", _registry){
        println "_userName: $_userName, _pass: $_pass, _email: $_email, _registry: $_registry"
        def userName = _userName ?: "jenkins"
        def pass = _pass ?: "service"
        def email = _email ?: "jenkins@service.com"
        def registry = _registry ?: ""
        println "userName: $userName, pass: $pass, email: $email, registry: $registry"

        NpmPackage.installNpmCliLogin()

        def cikti = Context.sh (
            label: "npm-cli-login ile Login oluyoruz",
            script: "npm-cli-login -u $userName -p $pass -e $email ${registry ? '-r $registry' :  ''}",
            returnStdout: false
        )
    }

    static def installNpmCliLogin(){
        Context.sh "npm install -g npm-cli-login"
    }

}