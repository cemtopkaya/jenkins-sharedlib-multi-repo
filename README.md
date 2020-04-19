Konsol üstünde çalıştırmak için

```
groovy -cp ./src test.gvy
```

classpath olarak ./src dizinini gösteriyoruz. Böylece arayacağı paketleri /src dizini içinde bulabileceğine ikna ediyoruz.
Eğer harici bir jar dahil etmek isteseydik aşağıdaki gibi eklerdik
```
-cp "./src; c:/temp/kütüp.jar; ./vars" 
```

Sonrasında gelen xxx.groovy dosyası ise bu classpath içindeki dosyaları kullanarak çalıştıracağımız xxx.groovy 
veya xxx.gvy dosyasını işaret etmek içindir.

en başta "groovy" executable dosyanın da ortam değişkenlerinden erişilebilir olmasına dikkat etmek gerek.

[Referans](https://gist.github.com/tknerr/42258e761f2a0f95a92b)

build.gradle dosyasında classpath