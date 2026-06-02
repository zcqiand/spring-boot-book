@Service
public class MemoryLeakService {
    // 模拟内存泄漏：持续增长的缓存
    private final List<byte[]> memoryLeak = new ArrayList<>();

    public void allocateMemory(int mb) {
        // 每调用一次分配指定MB内存
        memoryLeak.add(new byte[1024 * 1024 * mb]);
    }

    public void clearMemory() {
        memoryLeak.clear();
        System.gc();
    }

    public int getAllocatedSize() {
        return memoryLeak.size();
    }
}