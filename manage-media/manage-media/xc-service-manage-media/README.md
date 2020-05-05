oauth_client_details表：client_id:XcWebApp,client_secret:XcWebApp
xc_user表：username：itcast,password:111111


资管理授权配置
1.1 配置公钥
    公钥拷贝到 publickey.txt文件中，将此文件拷贝到资源服务工程的classpath下
    1.1.1 生成公钥私钥
        1.1.1.1、生成密钥证书
            下边命令生成密钥证书，采用RSA 算法每个证书包含公钥和私钥
            keytool -genkeypair -alias xckey -keyalg RSA -keypass xuecheng -keystore xc.keystore -storepass
            xuechengkeystore
            Keytool 是一个java提供的证书管理工具
            -alias：密钥的别名
            -keyalg：使用的hash算法
            -keypass：密钥的访问密码
            -keystore：密钥库文件名，xc.keystore保存了生成的证书
            -storepass：密钥库的访问密码
            查询证书信息：
            keytool -list -keystore xc.keystore
            删除别名
            keytool -delete -alias xckey -keystore xc.keystore
        1.1.1.2、导出公钥
            openssl是一个加解密工具包，这里使用openssl来导出公钥信息。
            安装 openssl：http://slproweb.com/products/Win32OpenSSL.html
            安装资料目录下的Win64OpenSSL-1_1_0g.exe
            配置openssl的path环境变量，本教程配置在D:\OpenSSL-Win64\bin
            cmd进入xc.keystore文件所在目录执行如下命令：
                keytool ‐list ‐rfc ‐‐keystore xc.keystore | openssl x509 ‐inform pem ‐pubkey
            将生成的公钥拷贝到文本文件中，合并为一行
1.2 添加依赖
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-oauth2</artifactId>
    <version>2.1.5.RELEASE</version>
</dependency>
1.3 将ResourceServerConfig拷贝到config包下