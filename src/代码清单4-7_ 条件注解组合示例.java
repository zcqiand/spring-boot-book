@Bean
@ConditionalOnProperty(name = "feature.x.enabled", havingValue = "true")
@ConditionalOnClass(name = "com.example.FeatureX")
@ConditionalOnMissingBean(FeatureX.class)
public FeatureX featureX() {
    return new FeatureX();
}
// 该Bean只有在以下三个条件同时满足时才会注册：
// 1. 配置属性 feature.x.enabled=true
// 2. classpath中存在 com.example.FeatureX 类
// 3. 容器中没有其他FeatureX类型的Bean
//
// 条件判断顺序：OnProperty -> OnClass -> OnMissingBean
// 任何一个条件不满足，该Bean就不会被注册
//
// 这种组合设计确保了：
// - 功能可以通过配置开关
// - 功能依赖某个类存在
// - 用户可以覆盖默认实现
//
// 注意：组合条件是AND关系，所有条件都必须满足Bean才会被注册
// 如果需要OR关系，需要创建多个Bean，每个使用不同的条件注解
//
// 这种设计允许精细控制Bean的注册条件，是自动配置的核心机制