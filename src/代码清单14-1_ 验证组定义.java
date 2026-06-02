public interface GroupA {}  // 创建场景
public interface GroupB {}  // 更新场景

@NotNull(groups = GroupB.class)
private Long id;

@NotBlank(groups = {GroupA.class, GroupB.class})
private String name;