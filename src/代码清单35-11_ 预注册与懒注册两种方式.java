// 预注册：在构造方法中预先注册所有Meter
public PreRegisteredService(MeterRegistry registry) {
    this.counter = Counter.builder("business.pre.registered").register(registry);
}

// 懒注册：首次使用时注册，适合指标数量不确定的场景
public void doSomething(String taskType) {
    Counter.builder("business.lazy.registered")
            .tag("task_type", taskType)
            .register(registry)
            .increment();
}