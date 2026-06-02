// 反例：总是拼接字符串，即使日志不输出
logger.info("用户" + userName + "登录了");

// 正例：参数化日志，日志不输出时不拼接
logger.info("用户 {} 登录了", userName);