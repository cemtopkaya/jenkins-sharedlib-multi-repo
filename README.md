## CLI ÜSTÜNDEN ELLE KOŞTURMAK
Konsol üstünden groovy kod dosyamızı çalıştırmak için classpath argumanına hem sınıflarımıza erişsin diye ./src dizinini 
hem de @NonCPS annotation'ınını kullandığımız kütüphanenin jar dosyasının yolunu veriyoruz.

```
$ groovy -cp "./src;./lib/groovy-cps-1.24.jar" test.gvy
[@cinar/cn-nrf-api, @cinar/cn-nef, @cinar/cn-nssf, @cinar/cn-nrf]
```

## Gradle ÜSTÜNDE OTOMASYONLA ÇALIŞTIRMAK
az sonra...