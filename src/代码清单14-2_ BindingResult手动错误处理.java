@PostMapping("/register")
public ResponseEntity<?> register(@Valid @RequestBody UserForm form, BindingResult result) {
    if (result.hasErrors()) {
        // 手动收集错误而非抛异常
        return ResponseEntity.badRequest().body(
            result.getFieldErrors().stream()
                .collect(Collectors.toMap(
                    FieldError::getField,
                    FieldError::getDefaultMessage
                ))
        );
    }
    // 验证通过，继续业务逻辑
}