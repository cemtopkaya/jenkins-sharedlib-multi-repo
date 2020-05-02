package ulak.Node

class PackageVersion {
    String version
    Boolean published = false

    PackageVersion(String version, Boolean published=false){
        this.version = version
        this.published = published
    }

    @Override
    String toString(){
        return "{\n\tversion: $version,\n\tpublished: $published\n}"
    }
}