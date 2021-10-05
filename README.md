# Personal Website Backend
Check out my website at [markTakatsuka.com](https://markTakatsuka.com)  
**NOTE:** It is still under construction!

## Functions Implemented:
#### Traditional FunctionFus:
| Function | Form | Description |
| --- | --- | --- |
| Addition          | a + b | Add |
| Subtraction       | a - b | Subtract |
| Multiplication    | a * b | Multiply | 
| Division          | a / b | Divide |
| Mod (remainder)   | a % b | Modulo |
| Absolute Value    | abs( a ) | Absolute Value |

## Tech-Stack
- Springboot
- Java 11

## Run on Compute Engine
```shell
gradle clean build

gsutil cp build/libs/* gs://website-backend-builds/[version].jar
```

## Updating Certs
```shell
sudo certbot renew

cd /etc/letsencrypt/live/[cert_name]
openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name backend -CAfile chain.pem -caname root
gsutil cp keystore.p12 gs://website-backend-keyfiles
```