@Service
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public RoleService(RoleRepository roleRepository, UserRepository userRepository) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
    }

    public Role createRole(String roleName, String description) {
        if (roleRepository.existsByRoleName(roleName)) {
            throw new IllegalArgumentException("角色已存在：" + roleName);
        }
        return roleRepository.save(Role.builder().roleName(roleName).description(description).build());
    }

    @Transactional(readOnly = true)
    public Optional<Role> findById(Long id) {
        return roleRepository.findById(id);
    }

    public User grantRoleToUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        user.getRoles().add(role);
        role.getUsers().add(user);
        return userRepository.save(user);
    }

    public User revokeRoleFromUser(Long userId, Long roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"));
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("角色不存在"));
        user.getRoles().remove(role);
        role.getUsers().remove(user);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean userHasRole(Long userId, String roleName) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("用户不存在"))
                .getRoles().stream().anyMatch(r -> r.getRoleName().equals(roleName));
    }
}