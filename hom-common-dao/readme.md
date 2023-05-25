#### 使用说明
* 宿主项目定义：引用了当前模块的项目
* 配置文件必须放在resources/config目录下，不然宿主项目不能读取到这些配置
* 在宿主项目的启动类上需添加@MapperScan("hom.cluster.common.dao.mapper")才能扫描到此模块定义的Mapper
* 在宿主项目的启动类上需要指定scanBasePackages并包含当前模块的路径，不然Bean不会注入容器
* [《SpringBoot读取配置文件的顺序》](https://wenku.baidu.com/view/d2d770a280d049649b6648d7c1c708a1284a0abc.html?_wkts_=1685026112881&bdQuery=springboot%E4%BC%98%E5%85%88%E8%AF%BB%E5%8F%96%E4%BE%9D%E8%B5%96%E9%A1%B9%E7%9B%AE%E7%9A%84%E9%85%8D%E7%BD%AE%E6%96%87%E4%BB%B6)