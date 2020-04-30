/**
 * SCM'den verileri çekmek için bir ara katman olsun
 */
import java.io.*;
import groovy.io.*;
import java.util.regex.Pattern
// import com.cloudbees.groovy.cps.NonCPS

//@NonCPS
def call(_userName, _pass, _email="jenkins@service.com", _registry){
    echo "_userName: $_userName, _pass: $_pass, _email: $_email, _registry: $_registry"
    userName = _userName ?: "jenkins"
    pass = _pass ?: "service"
    email = _email ?: "jenkins@service.com"
    registry = _registry ?: params.NPM_REGISTRY.replace('--registry=','')
    echo "userName: $userName, pass: $pass, email: $email, registry: $registry"
    cikti = sh (
        label: "npm-cli-login ile Login oluyoruz",
        script: "npm-cli-login -u $userName -p $pass -e $email -r $registry",
        returnStdout: false
    )
}