// 违反 SRP：这个类有两个变化原因
public class EquipmentService {
    public void saveEquipment(Equipment e) { /* ... */ }
    public void deleteEquipment(Long id) { /* ... */ }
    public void exportToExcel() { /* ... */ }  // 导出逻辑不应该在这里
    public void generatePDF() { /* ... */ }     // PDF生成不应该在这里
}

// 符合 SRP：职责分离
public class EquipmentService {
    public void saveEquipment(Equipment e) { /* ... */ }
    public void deleteEquipment(Long id) { /* ... */ }
}

public class EquipmentExportService {
    public void exportToExcel(List<Equipment> equipment) { /* ... */ }
    public void generatePDF(List<Equipment> equipment) { /* ... */ }
}