package Model

class Paket{
    String name;
    String path;
    def dependencies;
    
    Paket(name, path, dependencies){
        this.name = name;
        this.path = path;
        this.dependencies = dependencies;
    }
}