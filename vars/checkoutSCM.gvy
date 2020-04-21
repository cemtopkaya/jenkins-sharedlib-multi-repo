def call(String url, String branch="master", String credId){
    echo "url:${url}, branch:${branch}, credId:${credId}"
    //sh "pwd && mkdir branch && cd branch && pwd"

    subFolder = branch // Jenkinsfile olan yeri silmeyelim diye
    // git branch: branch, credentialsId: credId, url: url, relativeTargetDir: "branch"
    checkout([
        $class: 'GitSCM', 
        branches: [[name: "*/${branch}"]], 
        doGenerateSubmoduleConfigurations: false, 
        extensions: [[
            $class: 'RelativeTargetDirectory', 
            //relativeTargetDir: subFolder
        ]], 
        submoduleCfg: [], 
        userRemoteConfigs: [[
            credentialsId: credId, 
            url: url
        ]]
    ]);

}