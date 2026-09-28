// 前略：package、@impl 锚点注释、import 与类注释（2 端点与过滤维度说明），见源文件
@Service
public class SummaryService {

  /** 「ALL」特殊值 = 不按 category 过滤。 */
  public static final String CATEGORY_ALL = "ALL";

  private static final List<SummaryColumn> SUMMARY_COLUMNS =
      List.of(
          new SummaryColumn().key("commissionCode").label("委托编号"),
          new SummaryColumn().key("categoryCode").label("报告类别"),
          new SummaryColumn().key("projectName").label("工程名称"),
          new SummaryColumn().key("flowStatus").label("流程状态"),
          new SummaryColumn().key("result").label("结论"),
          new SummaryColumn().key("reportCode").label("报告编号"));