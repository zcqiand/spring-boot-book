// 前略：package 行、其后一行 @impl 锚点注释、import 区，见源文件
/** 把 {@link AuditTimestampInterceptor} 挂进 Hibernate SessionFactory（显式注册，不靠隐式探测）。 */
@Configuration
public class HibernateAuditConfig {

  @Bean
  public AuditTimestampInterceptor auditTimestampInterceptor() {
    return new AuditTimestampInterceptor();
  }

  @Bean
  public HibernatePropertiesCustomizer auditTimestampCustomizer(
      AuditTimestampInterceptor interceptor) {
    return properties -> properties.put(AvailableSettings.INTERCEPTOR, interceptor);
  }
}