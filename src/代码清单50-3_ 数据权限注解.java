@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPermission {
    String resourceType() default "";  // 资源类型
    String[] allowedFields() default {}; // 允许访问的字段
    String[] deniedFields() default {}; // 禁止访问的字段
    String filterField() default "tenantId"; // 过滤字段
}