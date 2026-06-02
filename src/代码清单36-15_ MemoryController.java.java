@RestController
@RequestMapping("/api/memory")
public class MemoryController {
    private final MemoryLeakService memoryLeakService;

    public MemoryController(MemoryLeakService memoryLeakService) {
        this.memoryLeakService = memoryLeakService;
    }

    @GetMapping("/allocate/{mb}")
    public Map<String, Object> allocate(@PathVariable int mb) {
        memoryLeakService.allocateMemory(mb);
        Map<String, Object> result = new HashMap<>();
        result.put("allocated", mb);
        result.put("total_blocks", memoryLeakService.getAllocatedSize());
        return result;
    }

    @GetMapping("/clear")
    public Map<String, Object> clear() {
        memoryLeakService.clearMemory();
        Map<String, Object> result = new HashMap<>();
        result.put("status", "cleared");
        return result;
    }
}