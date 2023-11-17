# logView
单机springboot应用日志查看工具，可以通过maven集成到springboot应用中
![image](https://github.com/sallaixu/logView/assets/48641847/875b376b-a3ca-4373-bed3-7dfcb9655053)
pom 中添加工具依赖
<dependency>
    <groupId>io.github.sallaixu</groupId>
    <artifactId>logView</artifactId>
    <version>1.0.1</version>
</dependency>


yml文件配置

```yml
logging:
  file:
    name: logs/test.log  #配置日志记录文件，路径名称
logview:
  enable: true #是否开启   登录地址：/logView
  loginName: sallai # web端登录用户名
  loginPassword: 1234567 #登录密码

```
