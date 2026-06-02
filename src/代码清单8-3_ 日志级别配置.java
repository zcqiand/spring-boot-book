// 反例——永远不要这样写
logger.info("用户" + user.getName() + "的订单" + order.getId() + "已创建");

// 正例——使用占位符
logger.info("用户 {} 的订单 {} 已创建", user.getName(), order.getId());

// 多参数也没问题
logger.debug("参数: a={}, b={}, c={}", a, b, c);