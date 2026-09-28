/** M03.F03 检测记录 controller（B9.3）。6 端点。tenant 收口走 JWT claim。 */
@RestController
public class TestRecordController implements TestRecordsApi {

  private final TestRecordService service;
  private final ConfigUserDirectory directory;

  // ……（中略：构造器与列表、创建、详情、更新、删除端点，见源文件）

  @Override
  public ResponseEntity<TestRecord> testRecordsSetVerdict(
      String id, TestRecordsSetVerdictRequest body) {
    return ResponseEntity.ok(
        service.setVerdict(
            InspectionCatalogController.currentTenantIdOrDefaultStatic(directory),
            id,
            body == null ? null : body.getVerdict()));
  }
}