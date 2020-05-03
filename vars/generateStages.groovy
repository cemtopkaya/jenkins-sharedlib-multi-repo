import ulak.Model.Paket
import ulak.Node.NpmPackage
import ulak.Jenkins.*
import ulak.Parser.AngularParser
import ulak.Sorter.BuildSorter
import hudson.FilePath
import com.cloudbees.groovy.cps.NonCPS
import java.util.LinkedHashMap

def call(def context, String repoDirectory, String repoUrl, String sourceBranch, String credId, String _npmRegistry){
    def ctx = context
    Map<String, Paket> libs = [:]
    String npmRegistry = _npmRegistry
    
    return node (params.AGENT_NAME){
        
        stage("Checkout"){
            dir(repoDirectory) {
                checkoutSCM(repoUrl, sourceBranch, credId)
            }
        }

        stage('Set NPM Registries'){
            echo ">>> PreRequisites >>> repoDirectory: $repoDirectory"
            dir(repoDirectory){
                PreRequisites.instAll(ctx)
                Map<String, String> ss = [
                    "":params.NPM_REGISTRY,
                    "@cinar":params.NPM_REGISTRY
                ]
                NpmPackage.setRegistries(ctx, repoDirectory, ss)
            }
        }
        
        stage("Init"){
            dir(repoDirectory) {
                NpmPackage.installPackages(ctx, repoDirectory, npmRegistry, ['--no-bin-links'])
            }
        }
        
        stage("Build"){
            echo "generated stage build-------------------"
            
            dir(repoDirectory){
                
                libs = getLibs(repoDirectory)

                println "---***** ------------ Set Dependencies ---------"
                libs.each{ entry ->
                    /**
                     * ./projects içindeki kütüphanelerin bağımlılıklarını bulalım 
                     */
                    
                    // ./projects/@kapsam/kütüp_adı yolunu olusturalım
                    def libDirPath = "$repoDirectory/$entry.value.path"
                    println "libDirPath: $libDirPath"
                    // paketin bağımlılıklarını bulalım
                    catchError(buildResult: 'SUCCESS', stageResult: 'UNSTABLE'){
                        def allDeps  = getLibDependencies(libDirPath)
                        
                        def projectLibNames = libs.keySet()
                        println "allDepss: $allDeps"
                        println "projectLibNames: $projectLibNames"
                        
                        entry.value.dependencies = allDeps.findAll { 
                            it in projectLibNames
                        }
                        
                        println "entry.value.dependencies: ${entry.value.dependencies}"
                    }
                }

                println "---***** ------------ getSortedLibraries ---------"
                // Tüm bağımlılıkları en az bağımlıdan, en çoka doğru sıralayalım
                
                catchError(buildResult: 'ABORTED', stageResult: 'FAILURE'){
                    def sortedLibs = BuildSorter.getSortedLibraries(libs)

                    sortedLibs.each { packageName ->
                        println ">>> Kütüp adı: $packageName"
                        lib = libs.get(packageName)
                        println ">>> lib: $lib"
                        String packageJsonPath = "$repoDirectory/$lib.path/package.json"
                        println "packageJsonPath:::: $packageJsonPath"
                        NpmPackage pkg = NpmPackage.parseFromPackageJson(ctx, packageJsonPath)
                        pkg.isPublished(npmRegistry)
                        lib.npmPackage = pkg
                        
                        println "lib: $lib"
                        
                        //lib.npmPackage.buildAngularPackage()
                    }
                }
            }
        }
        stage("Test"){
            
        }
        stage("Merge"){
            
        }
        stage("Deploy"){
            println "Deploy adı::: $libs"
            
            libs.each{ entry ->
                println "entry :::: "+entry
                // paket sunucusuna yükleyelim
                catchError(buildResult: 'ABORTED', stageResult: 'FAILURE'){
                
                    if(entry.value.npmPackage.Version.published && params.FORCE_TO_PUBLISH == false){ 
                        currentBuild.result = "FAILURE" 
                        error('$pkg.Version.version Versiyon numaralı sürüm mevcut, aynı versiyon numarası kullanılamaz!')
                    }else{
                        String packageSrcPath = "${repoDirectory}/${entry.value.path.replace('projects','dist')}"
                        entry.value.npmPackage.unpublish(params.NPM_REGISTRY)
                        entry.value.npmPackage.publish(params.NPM_REGISTRY, packageSrcPath, true)
                    }
                    
                    println ">>> entry   ::: $entry"
                }
            }
            
        }
		
		stage('Git Push To Origin') {
            script {
                if(params.TAG_AND_PUSH == true){
                    
                    tagName = "${env.JOB_NAME}-v${MAJOR}.${PHASE_NUMBER}.${SPRINT_NUMBER}-b${env.BUILD_NUMBER}"
                    eposta = "jenkins.servis@ulakhaberlesme.com.tr"
                    name = "Jenkins Servis"
                    
                    echo "*** Etiketlenecek ve Push edilecek. Kullanılacak etikat adı: ${tagName}"
                    
                    sshagent([params.REPO_CRED_ID]) {
                        sh "git config --local user.email '${eposta}'"
                        sh "git config --local user.name '$name'"
                        sh "git tag -fa ${tagName} -m 'git tag oldu'"
                        sh "git push origin HEAD:$REPO_TARGET_BRANCH_NAME --tags"
                    }
                    
                }
            }
        }
    }
}