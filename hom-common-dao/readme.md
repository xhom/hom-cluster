#### 使用说明
* 宿主项目定义：引用了当前模块的项目
* 配置文件必须放在resources/config目录下，不然宿主项目不能读取到这些配置
* 在宿主项目的启动类上需添加@MapperScan("hom.cluster.common.dao.mapper")才能扫描到此模块定义的Mapper
* 在宿主项目的启动类上需要指定scanBasePackages并包含当前模块的路径，不然Bean不会注入容器