class PreRequisites {
    static def Context

    PreRequisites(def context){
        PreRequisites.Context = context

        try {
            is_node_installed = Context.sh(
                label: "NODE Yüklü mü?",
                returnStdout: true, 
                script: "whereis node | grep ' ' -ic"
            ).trim() 
        }
        catch (exception) { PreRequisites.installNodeJs() }

        // try {
        //     angular_cli_version =  sh(
        //         label: "Angular CLI Yüklü mü?",
        //         returnStdout: true, 
        //         script: "ng --version | awk '/8.3/{count=0; count++} END{print count == 0 ? 0 : count}'"
        //         // script: "ng --version"
        //     )

        //     // yüklü ancak 8 versiyonu değil
        //     if(angular_cli_version == "0"){ fnInstallAngularCli() }
        // }
        // catch (err) { PreRequisites.installAngularCli() }
    }

    static def installNodeJs(){
        try {
            Context.sh(
                label: "NodeJs Yükleniyor",
                returnStdout: false, 
                script: "sudo apt-get update                                           \
                            && apt-get upgrade -y                                      \
                            && curl -sL https://deb.nodesource.com/setup_12.x | bash - \
                            && apt-get install -y nodejs                               \
                            && node --version                                          \
                            && npm --version"
            )
        }
        catch (err) {
            println " --** Hata (NodeJs) : $err" 
            Context.currentBuild.result = 'FAILURE'
        }
    }

    static def installAngularCli(){
        try {
            Context.sh(
                label: "Angular CLI Yükleniyor",
                returnStdout: false, 
                script: "npm install -g @angular/cli@8.3.23"
            )
        }
        catch (err) {
            println " --** Hata (Angular CLI) : $err" 
            Context.currentBuild.result = 'FAILURE'
        }
    }
}