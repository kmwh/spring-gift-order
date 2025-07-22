package gift.option.controller;

import gift.option.dto.OptionRequestDto;
import gift.option.dto.OptionResponseDto;
import gift.option.service.OptionService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products/{product_id}/options")
public class OptionController {
    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @PostMapping
    public ResponseEntity<OptionResponseDto> create(
        @PathVariable("product_id") Long productId,
        @Valid @RequestBody OptionRequestDto requestDto
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(optionService.create(productId, requestDto));
    }

    @GetMapping
    public ResponseEntity<List<OptionResponseDto>> findAllByProductId(
        @PathVariable("product_id") Long productId
    ) {
        return ResponseEntity.ok(optionService.findAllByProductId(productId));
    }

    @GetMapping("/{option_id}")
    public ResponseEntity<OptionResponseDto> findByProductIdAndOptionId(
        @PathVariable("product_id") Long productId,
        @PathVariable("option_id") Long optionId
    ) {
        return ResponseEntity.ok(optionService.findByProductIdAndOptionId(productId, optionId));
    }

    @PutMapping("/{option_id}")
    public ResponseEntity<OptionResponseDto> update(
        @PathVariable("product_id") Long productId,
        @PathVariable("option_id") Long optionId,
        @Valid @RequestBody OptionRequestDto requestDto
    ) {
        return ResponseEntity.ok(optionService.update(productId, optionId, requestDto));
    }

    @DeleteMapping("/{option_id}")
    public ResponseEntity<Void> delete(
        @PathVariable("product_id") Long productId,
        @PathVariable("option_id") Long optionId
    ) {
        optionService.delete(productId, optionId);
        return ResponseEntity.noContent()
            .build();
    }
}
