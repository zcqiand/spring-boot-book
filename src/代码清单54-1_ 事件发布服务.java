@Service
@RequiredArgsConstructor
public class DomainEventPublisher {

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    public void publishTaskCreated(Task task) {
        TaskCreatedEvent event = new TaskCreatedEvent(
            task.getId(),
            task.getTaskNo(),
            task.getLabId(),
            task.getCreatedBy()
        );
        eventPublisher.publishEvent(event);
    }

    public void publishTaskCompleted(Task task) {
        TaskCompletedEvent event = new TaskCompletedEvent(
            task.getId(),
            task.getTaskNo(),
            task.getCompletedAt()
        );
        eventPublisher.publishEvent(event);
    }
}