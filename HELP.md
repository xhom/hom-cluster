### hom-cluster

#### 项目介绍
>包含了所有微服务的集群，但在实际开发中可将每个子模块单独建立代码库以便于不同的项目给不同的人开发

#### 微服务依赖版本对应关系
| Spring Cloud Alibaba Version | Spring Cloud Version | Spring Boot Version |
|------------------------------|----------------------|---------------------|
| 2022.0.0.0-RC2*              | 2022.0.0             | 3.0.2               |
| 2022.0.0.0-RC1               | 2022.0.0             | 3.0.0               |
| 2021.0.5.0*                  | 2021.0.5             | 2.6.13              |
| 2021.0.4.0                   | 2021.0.4             | 2.6.11              |
| 2021.0.1.0                   | 2021.0.1             | 2.6.3               |
| 2021.1                       | 2020.0.1             | 2.4.2               |
| 2.2.10-RC1*                  | Hoxton.SR12          | 2.3.12.RELEASE      |
| 2.2.9.RELEASE                | Hoxton.SR12          | 2.3.12.RELEASE      |
| 2.2.8.RELEASE                | Hoxton.SR12          | 2.3.12.RELEASE      |
| 2.2.7.RELEASE                | Hoxton.SR12          | 2.3.12.RELEASE      |
| 2.2.6.RELEASE                | Hoxton.SR9           | 2.3.2.RELEASE       |
| 2.2.1.RELEASE                | Hoxton.SR3           | 2.2.5.RELEASE       |
| 2.2.0.RELEASE                | Hoxton.RELEASE       | 2.2.X.RELEASE       |
| 2.1.4.RELEASE                | Greenwich.SR6        | 2.1.13.RELEASE      |
| 2.1.2.RELEASE                | Greenwich            | 2.1.X.RELEASE       |
| 2.0.4.RELEASE                | Finchley             | 2.0.X.RELEASE       |
| 1.5.1.RELEASE                | Edgware              | 1.5.X.RELEASE       |

* 数据来源：[Spring Cloud Alibaba 版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E#%E7%BB%84%E4%BB%B6%E7%89%88%E6%9C%AC%E5%85%B3%E7%B3%BB)

#### Nacos鉴权（Version 2.2.2）开启
* Nacos服务默认是不需要密码就可以连接的 
* 需要修改Nacos服务conf目录 下的application.properties文件
* 具体修改项如下：
1. nacos.core.auth.enabled=true //启用密码
2. nacos.core.auth.system.type=nacos
3. nacos.core.auth.plugin.nacos.token.secret.key=随机Base64不小于32位
4. nacos.core.auth.server.identity.key=xxx
5. nacos.core.auth.server.identity.value=xxx
* 用户需要前端配置：权限控制-用户列表-创建用户，并给用户分配对应命名空间的权限（权限的动作要指定为：读写 (rw)）
* identity配置项分别作为Header头的key和value，添加到Header可绕过权限校验，可以理解为一个自定义的固定 Token