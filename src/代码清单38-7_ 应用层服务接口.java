public interface EquipmentManagementService {
    EquipmentResponse createEquipment(CreateEquipmentRequest request);
    EquipmentResponse getEquipmentById(String equipId);
    PageResult<EquipmentResponse> listEquipments(EquipmentQuery query);
    EquipmentResponse updateEquipment(UpdateEquipmentRequest request);
    void deleteEquipment(String equipId);
}