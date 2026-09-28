/** sample_receipts.flow_status : 8 阶段枚举 ↔ PG text。 */
@Converter(autoApply = false)
public class FlowStatusConverter implements AttributeConverter<FlowStatus, String> {

  @Override
  public String convertToDatabaseColumn(FlowStatus attribute) {
    return attribute == null ? null : attribute.getValue();
  }

  @Override
  public FlowStatus convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
    for (FlowStatus t : FlowStatus.values()) {
      if (t.getValue().equals(dbData)) return t;
    }
    throw new IllegalArgumentException("Unknown flow_status: " + dbData);
  }
}