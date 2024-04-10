vue-manage-system  后台管理系统的前端项目</br></br>
src目录 是后台管理系统的服务器程序，
使用spring security 加自定义 token实现 登录校验和权限管理</br></br>

client目录  客户端服务器，主要用于制造数据，模拟注册登录发布评论点赞等等，产生内容后发送消息到消息中心统一处理</br></br>

messagecenter 消息中心，监听rabbitmq 消息队列 根据消息类型调用不同的服务</br>
</br>
rpcreview 自动审核服务 spring cloud+nacos 实现 负载均衡</br></br>


后台界面
![image](https://github.com/fengxiang1990/image-manager/assets/5101992/990f6e14-1b15-49c5-a209-a14d05ea9e5c)

![image](https://github.com/fengxiang1990/image-manager/assets/5101992/e51b0137-c886-4ff1-bac4-d0d1b1b76586)
![image](https://github.com/fengxiang1990/image-manager/assets/5101992/96bd5f3f-e3ab-48d4-bf48-13f8e9f3d3fa)
![image](https://github.com/fengxiang1990/image-manager/assets/5101992/bd27abe5-1148-4ffd-933a-c771dc2a0079)



![image](https://github.com/fengxiang1990/image-manager/assets/5101992/b6a75026-8f4f-408b-9093-a9d3891740a8)

![3156b18451c68b9c88d7a56114b798b](https://github.com/fengxiang1990/image-manager/assets/5101992/0bb9f431-2e65-4e81-a7e9-d6cffbe3ecd8)
