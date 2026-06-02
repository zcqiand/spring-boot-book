com.lab.management
├── common                      # 通用模块
│   ├── config                  # 配置类
│   ├── exception               # 异常定义
│   └── result                  # 统一返回结构
├── domain                      # 领域层
│   ├── equipment               # 设备管理领域
│   │   ├── model               # 设备实体
│   │   ├── repository          # 仓储接口
│   │   └── service            # 领域服务
│   ├── experiment             # 实验管理领域
│   └── report                 # 报告管理领域
├── application                # 应用层
│   ├── equipment              # 设备管理用例
│   └── dto                    # 数据传输对象
├── infrastructure             # 基础设施层
│   └── persistence            # 持久化
└── interfaces                 # 接口层
    └── controller             # REST控制器