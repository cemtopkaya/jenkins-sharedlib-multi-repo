package ulak.Model

import ulak.Node.NpmPackage

class Paket implements Serializable{
    String name
    String path
    def dependencies
    NpmPackage npmPackage
    
    Paket(name, path, dependencies, npmPackage=null){
        this.name = name
        this.path = path
        this.dependencies = dependencies
        this.npmPackage = npmPackage
    }
}