package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.enums.ESectionCategory;
import br.com.meli.projetointegrador.model.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Jhony Zuim / Lucas Pereira / Edemilson Nobre / Rafael Vicente
 * @version 1.0.0
 * @since 15/10/2021
 * Camada de controller responsavel pela regra de negocio relacionada ao productController
 */

@RestController
@RequestMapping("/api/v1/fresh-products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/list") // Chamada do endpoint: /list?sectionCategory=FF ou FS ou RF ou um que nao existe.
    public ResponseEntity<List<ProductDTO>> getProductByCategory(@RequestParam("sectionCategory") String category){
        if (category.equals(ESectionCategory.FS.toString()) |
                category.equals(ESectionCategory.FF.toString()) |
                category.equals(ESectionCategory.RF.toString())) {
            List<ProductDTO> productList = productService.listProdutcByCategory(category);
            if(!productList.isEmpty()){
                return ResponseEntity.ok(productList);
            }
        }
    return ResponseEntity.badRequest().build();
    }

}