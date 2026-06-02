@Configuration
public class ConditionalExperimentConfig {

    // 实验1：只有当配置开启时才注册
    // 改变application.yml中的experiment.enabled值，观察Bean是否被注册
    // 当experiment.enabled=true时，该Bean会被注册
    // 当experiment.enabled=false或不配置时，该Bean不会被注册
    @Bean
    @ConditionalOnProperty(name = "experiment.enabled", havingValue = "true")
    public String experimentBean() {
        return "Experiment is enabled!";
    }

    // 实验2：只有当classpath中有特定类时才注册
    // 引入或移除对应依赖，观察Bean是否被注册
    // 当classpath中有ObjectMapper时，该Bean会被注册
    // 当classpath中没有ObjectMapper时，该Bean不会被注册
    @Bean
    @ConditionalOnClass(name = "com.fasterxml.jackson.databind.ObjectMapper")
    public String jacksonAvailable() {
        return "Jackson is available!";
    }

    // 实验3：只有当容器中没有该类型Bean时才注册
    // 定义其他String类型的Bean，观察这个Bean是否被注册
    // 当容器中没有其他String类型的Bean时，该Bean会被注册
    // 当容器中已有其他String类型的Bean时，该Bean不会被注册
    @Bean
    @ConditionalOnMissingBean
    public String defaultBean() {
        return "Default bean - no other String bean exists";
    }
}