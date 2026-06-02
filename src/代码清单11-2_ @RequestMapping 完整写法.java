@RestController
@RequestMapping("/api/products")
public class ProductController {

    // 完整写法：method + path + consumes + produces
    @RequestMapping(
        value = "/create",
        method = RequestMethod.POST,
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE
    )
    public Product createProduct(@RequestBody Product product) {
        return productService.save(product);
    }
}