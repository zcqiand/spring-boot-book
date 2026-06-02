@GetMapping("/users/{id}")
public User getUser(@PathVariable Long id) {
    // URL: /users/123 → id = 123
    return userService.findById(id);
}

@GetMapping("/users/{userId}/posts/{postId}")
public Post getUserPost(
        @PathVariable Long userId,
        @PathVariable Long postId) {
    // URL: /users/1/posts/99 → userId=1, postId=99
    return postService.findByUserPost(userId, postId);
}