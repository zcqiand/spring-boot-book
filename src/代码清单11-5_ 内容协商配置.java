@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    // 只接受JSON，返回JSON
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Document getDocumentJson(@PathVariable Long id) {
        return documentService.findById(id);
    }

    // 只接受XML，返回XML
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE)
    public Document getDocumentXml(@PathVariable Long id) {
        return documentService.findById(id);
    }

    // 接受JSON或XML响应
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Document createDocument(@RequestBody Document doc) {
        return documentService.save(doc);
    }
}