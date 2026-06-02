// 查询 10 个用户，触发 11 次查询（1 + 10）
List<User> users = userRepository.findAll();
for (User user : users) {
    System.out.println(user.getOrders().size());  // N+1!
}