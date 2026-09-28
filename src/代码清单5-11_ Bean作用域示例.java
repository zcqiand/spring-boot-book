@Component
@Scope("prototype") // 每次注入都创建新实例
public class ProtoBean {
    // ...
}

@Component
@Scope("singleton") // 默认值，整个容器只有一个实例
public class SingletonBean {
    // ...
}