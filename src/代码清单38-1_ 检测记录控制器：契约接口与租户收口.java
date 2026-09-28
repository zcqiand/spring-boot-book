@RestController
public class TestRecordController implements TestRecordsApi {

  private final TestRecordService service;
  private final ConfigUserDirectory directory;

  public TestRecordController(TestRecordService service, ConfigUserDirectory directory) {
    this.service = service;
    this.directory = directory;
  }

  @Override
  public ResponseEntity<TestRecordsListTestRecords200Response> testRecordsListTestRecords(
      Integer page, Integer pageSize, String sampleId, String parameterCode) {
    // @entry M03.F03.I01
    String tenantId = InspectionCatalogController.currentTenantIdOrDefaultStatic(directory);
    var items = service.list(tenantId, sampleId);
    var body = new TestRecordsListTestRecords200Response();
    body.setItems(items);
    body.setPage(page != null ? page : Integer.valueOf(1));
    // 2026-09-16 T11 live 实证：pageSize 缺省对齐家族约定 20（nextjs oracle）。
    body.setPageSize(pageSize != null ? pageSize : Integer.valueOf(20));
    body.setTotal((long) items.size());
    return ResponseEntity.ok(body);
  }

  // ……（中略：创建、详情、更新、删除、改判五个端点，同样是「取租户、调服务、包响应」三步，见源文件）
}