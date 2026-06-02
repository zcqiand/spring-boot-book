@PostMapping
public String createUser(
        @Valid @RequestBody CreateUserRequest request,
        BindingResult result) {

    // 如果有校验错误，不进入业务逻辑
    if (result.hasErrors()) {
        StringBuilder errors = new StringBuilder();
        result.getFieldErrors().forEach(e ->
            errors.append(e.getField())
                  .append(": ")
                  .append(e.getDefaultMessage())
                  .append("; ")
        );
        return "校验失败: " + errors;
    }

    // 校验通过，执行业务逻辑
    return userService.create(request);
}