@Documented
@Constraint(validatedBy = YourValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface YourConstraint {
    String message() default "默认错误消息";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}