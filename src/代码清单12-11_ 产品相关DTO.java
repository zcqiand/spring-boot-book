package com.xrtech.chapter12.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;

public class CreateProductRequest {

    @NotBlank(message = "产品名称不能为空")
    @Size(max = 100, message = "产品名称最多100字符")
    private String name;

    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.01", message = "价格至少0.01")
    private BigDecimal price;

    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负")
    private Integer stock;

    @Size(max = 50, message = "分类最多50字符")
    private String category;

    private List<String> tags;

    // getter和setter
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}

public class CreateProductWithSpecRequest {

    @NotBlank(message = "产品名称不能为空")
    private String name;

    private ProductSpec spec;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public ProductSpec getSpec() { return spec; }
    public void setSpec(ProductSpec spec) { this.spec = spec; }
}

public class ProductSpec {
    private String weight;
    private String size;
    private String color;

    public String getWeight() { return weight; }
    public void setWeight(String weight) { this.weight = weight; }
    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
}

class Product {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer stock;
    private String category;
    private List<String> tags;
    private ProductSpec spec;
    private String createTime;

    // getter和setter
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
    public ProductSpec getSpec() { return spec; }
    public void setSpec(ProductSpec spec) { this.spec = spec; }
    public String getCreateTime() { return createTime; }
    public void setCreateTime(String createTime) { this.createTime = createTime; }
}