// 没有状态机的代码示例
public void submitForReview(Long taskId) {
    InspectionTask task = taskRepository.findById(taskId);
    if (task == null) throw new IllegalArgumentException("任务不存在");
    if (task.getStatus() != "IN_PROGRESS") throw new IllegalStateException("只有进行中的任务才能提交审核");
    if (task.getAssigneeId() == null) throw new IllegalStateException("任务未指派");
    if (task.getItems().isEmpty()) throw new IllegalStateException("任务没有检测项目");
    // ... 更多校验
    task.setStatus("PENDING_REVIEW");
    taskRepository.save(task);
}