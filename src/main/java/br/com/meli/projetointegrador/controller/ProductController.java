package br.com.meli.projetointegrador.controller;

import br.com.meli.projetointegrador.model.dto.ProductDTO;
import br.com.meli.projetointegrador.model.entity.Product;
import br.com.meli.projetointegrador.model.repository.ProductRepository;
import br.com.meli.projetointegrador.model.service.ProductService;
import br.com.meli.projetointegrador.util.SectionCategory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    private final ProductRepository productRepository;


    public ProductController(ProductService productService, ProductRepository productRepository) {
        this.productService = productService;
        this.productRepository = productRepository;
    }


    @GetMapping(value = "/list") // Chamada do endpoint: /list?sectionCategory=FF ou FS ou RF ou um que nao existe.
    public ResponseEntity<List<ProductDTO>> getProductByCategory(@RequestParam("sectionCategory") String category) {
        if (category.equals(SectionCategory.FS.toString()) |
                category.equals(SectionCategory.FF.toString()) |
                category.equals(SectionCategory.RF.toString())) {
            List<ProductDTO> productList = productService.listProdutcByCategory(SectionCategory.valueOf(category));
            if (!productList.isEmpty()) {
                return ResponseEntity.ok(productList);
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = "/products")
    public ResponseEntity<List<ProductDTO>> getlistProductBylist() {
        List<Product> products = productRepository.findAll();
        if (!products.isEmpty()) {
                return ResponseEntity.ok(productService.converteProductlist(products));
            } else {

        } return ResponseEntity.notFound().build();
    }
}


