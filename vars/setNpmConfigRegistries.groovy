/**
 * npm registry ayarlar?n? yapar ve .npmrc dosyas?na yazar
 * Parametre olarak Map<String,String> alacak:
 *   Map<String, String> ss = [
 *                          "":npmRegistry,
 *                          "@cinar":npmRegistry
 *                      ]
 */

def call(Map<String,String> scopeRegistries){
    println "----------------- setNpmConfigRegistries -------------------"
    
    try {
        scopeRegistries.each{
            def scope = it.key?:""
            echo "scope: $scope"
            if(scope){
                scope+=":"
            }
            script = "npm config set ${scope}registry ${it.value} --userconfig ./.npmrc"
            sh(
                label: "npm config set registry....",
                script: script,
                returnStdout: false
            )
        }
    } catch(err) {
        echo "---*** Hata (setNpmConfigRegistries): istisna oldu (Exception: $err)"  
        throw 
    }

}