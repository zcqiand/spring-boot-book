// 有状态机的代码示例
public void submitForReview(Long taskId) {
    InspectionTask task = taskRepository.findById(taskId);
    transitionGuard.validateTransition(task, InspectionStatus.PENDING_REVIEW);
    task.transitionTo(InspectionStatus.PENDING_REVIEW);
    taskRepository.save(task);
}