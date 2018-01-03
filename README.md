## 概述 ##
Kepler Admin 是一款专门为使用Kepler框架的服务提供的服务监控以及管理系统。系统目前包括三大功能模块：

- 大盘监控
- 服务管理
- 主机管理

系统由**收集器**（collector）以及**控制台**（admin）组成。收集器将各个服务传过来的数据存放到mongodb，然后控制台从mongodb里读出数据进行统计以及展示。

<img src="https://raw.githubusercontent.com/Kepler-Framework/Kepler-Images/master/admin_arch.png"/>

## 部署 ##

@See[<a href="https://github.com/Kepler-Framework/Kepler-Admin/wiki/%E9%83%A8%E7%BD%B2">Admin 部署</a>]

## 服务配置 ##

@See[<a href="https://github.com/Kepler-Framework/Kepler-All/wiki/%E5%8F%82%E6%95%B0%E9%85%8D%E7%BD%AE-%E6%9C%8D%E5%8A%A1%E7%9B%91%E6%8E%A7">参数配置</a>]

## 收集器数据定义 ##
@See[<a href="https://github.com/Kepler-Framework/Kepler-Admin/wiki/%E6%94%B6%E9%9B%86%E5%99%A8%E6%95%B0%E6%8D%AE%E5%AE%9A%E4%B9%89">收集器数据定义</a>]
## 大盘监控 ##

大盘监控分别用总量，错误量，耗时三个指标来统计当前周期的服务情况以及与前一周期相比的变化情况。

- **总量**：当日该服务的调用量
- **错误量**：当日该服务出错的总量
- **耗时**：当日该服务的平均调用耗时

一个周期默认为2分钟，可通过修改收集器的参数来配置。

`com.kepler.admin.collector.transfer.DashboardHandler.interval=2`

## 服务管理 ##

为每个服务提供以下功能：

- 服务配置：可以修改该服务的**标签**以及**优先级**（0-9，优先级依次递增）。
- 服务依赖查询：展示了所有依赖该服务的**服务分组**。服务分组下包含所有属于该分组的服务以及客户端。
- 服务统计信息查询

### 服务统计信息 ###

服务统计信息包含三个指标：

- 访问量
- 耗时（ms）
- 错误量

统计的时间单位包含：

- 分钟，统计时间跨度为前240分钟到当前时间
- 小时，统计时间跨度为前24小时到当前时间
- 天，统计时间跨度为前7天到当天

蓝色标签下的图表展示的是该服务总的统计数据，橙色标签下的图表展示的是该实例的统计数据。

<img src="https://raw.githubusercontent.com/Kepler-Framework/Kepler-Images/master/service_chat.png">

## 主机管理 ##

为每个节点提供以下功能：

- 主机状态查询
	- <a href="https://github.com/Kepler-Framework/Kepler-Admin/wiki/%E6%94%B6%E9%9B%86%E5%99%A8%E6%95%B0%E6%8D%AE%E5%AE%9A%E4%B9%89#status_static">静态status</a>查询
	- <a href="https://github.com/Kepler-Framework/Kepler-Admin/wiki/%E6%94%B6%E9%9B%86%E5%99%A8%E6%95%B0%E6%8D%AE%E5%AE%9A%E4%B9%89#status_dynamic">动态status</a>查询
- 动态参数配置
	- 修改该实例的**标签**以及**优先级**（0-9，优先级依次递增）
	- 修改kepler框架参数，@See[<a href="https://github.com/Kepler-Framework/Kepler-All/wiki/%E5%8F%82%E6%95%B0%E9%85%8D%E7%BD%AE-%E5%8A%A8%E6%80%81%E5%8F%82%E6%95%B0">动态参数</a>]
	- 修改服务自定义的参数，需要该参数实现参数回调。@See[<a href="https://github.com/Kepler-Framework/Kepler-All/wiki/%E5%8F%82%E6%95%B0%E9%85%8D%E7%BD%AE-%E5%8A%A8%E6%80%81%E5%8F%82%E6%95%B0">动态参数</a>]
- 主机依赖查询
	- 该实例export的所有服务
	- 该实例import的所有服务的实例

## 更多功能 ##
# Kepler-Admin
# Kepler-Admin
