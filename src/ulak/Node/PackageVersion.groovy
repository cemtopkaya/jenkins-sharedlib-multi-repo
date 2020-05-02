package ulak.Node
import com.cloudbees.groovy.cps.NonCPS

class PackageVersion {
    String version
    Boolean published = false

    PackageVersion(String version, Boolean published=false){
        this.version = version
        this.published = published
    }

    @NonCPS
    @Override
    String toString(){
        return "{\n\tversion: $version,\n\tpublished: $published\n}"
    }
}