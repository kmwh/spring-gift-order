package gift.product.controller;

import gift.option.dto.OptionRequestDto;
import gift.option.service.OptionService;
import gift.product.dto.CreateProductRequestDto;
import gift.product.dto.ProductResponseDto;
import gift.product.dto.UpdateProductRequestDto;
import gift.product.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/products")
public class ProductAdminController {
    private final ProductService productService;
    private final OptionService optionService;

    public ProductAdminController(ProductService productService, OptionService optionService) {
        this.productService = productService;
        this.optionService = optionService;
    }

    @GetMapping
    public String list(
        Model model,
        @PageableDefault(size = 20) Pageable pageable
    ) {
        model.addAttribute("products", productService.findAll(pageable));
        return "products/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("productId", null);
        model.addAttribute("product", CreateProductRequestDto.from());
        return "products/create_form";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        ProductResponseDto productResponseDto = productService.findById(id);
        model.addAttribute("product", productResponseDto);
        return "products/update_form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute CreateProductRequestDto requestDto) {
        productService.create(requestDto);
        return "redirect:/admin/products";
    }

    @PutMapping("/{id}")
    public String update(
        @PathVariable Long id,
        @Valid @ModelAttribute UpdateProductRequestDto requestDto
    ) {
        productService.update(id, requestDto);
        return "redirect:/admin/products";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
        productService.delete(id);
        return "redirect:/admin/products";
    }

    @PostMapping("/{id}/options")
    public String createOption(
        @PathVariable Long id,
        @Valid @ModelAttribute OptionRequestDto requestDto
    ) {
        optionService.create(id, requestDto);
        return "redirect:/admin/products/" + id + "/edit";
    }

    @DeleteMapping("/{product_id}/options/{option_id}")
    public String deleteOption(
        @PathVariable("product_id") Long productId,
        @PathVariable("option_id") Long optionId
    ) {
        optionService.delete(productId, optionId);
        return "redirect:/admin/products/" + productId + "/edit";
    }
}


