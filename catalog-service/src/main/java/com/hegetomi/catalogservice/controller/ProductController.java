package com.hegetomi.catalogservice.controller;

import com.hegetomi.catalogservice.api.ProductControllerApi;
import com.hegetomi.catalogservice.api.model.GetApiProduct200ResponseInner;
import com.hegetomi.catalogservice.api.model.GetApiProductHistoryId200ResponseInner;
import com.hegetomi.catalogservice.api.model.PostApiProductRequest;
import com.hegetomi.catalogservice.mapper.ProductMapper;
import com.hegetomi.catalogservice.model.Product;
import com.hegetomi.catalogservice.model.QProduct;
import com.hegetomi.catalogservice.service.ProductService;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.data.web.PageableArgumentResolver;
import org.springframework.data.web.querydsl.QuerydslPredicateArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductControllerApi {

    private final NativeWebRequest nativeWebRequest;
    private final ProductService productService;
    private final ProductMapper productMapper;
    private final PageableArgumentResolver pageableArgumentResolver;
    private final QuerydslPredicateArgumentResolver querydslPredicateArgumentResolver;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Void> postApiProduct(PostApiProductRequest postApiProductRequest) {
        Optional<Product> save = productService.save(productMapper.dtoToProduct(postApiProductRequest));
        if (save.isPresent()) {
            return ResponseEntity.ok().build();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Void> deleteApiProductId(Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Void> putApiProductId(Long id, PostApiProductRequest postApiProductRequest) {
        Optional<Product> product = productService.modifyById(id, productMapper.dtoToProduct(postApiProductRequest));
        if (product.isPresent()) {
            return ResponseEntity.ok().build();
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<List<GetApiProduct200ResponseInner>> getApiProduct(Long page, Long size, String sort,
                                                                             String productName, String productCategoryName,
                                                                             Long priceFrom, Long priceTo) {
        Predicate predicate = predicateCreator(productName, productCategoryName, priceFrom, priceTo);
        Pageable pageable = getPageable();
        return ResponseEntity.ok(productService.findAll(predicate, pageable).stream().map(productMapper::productToDto).toList());
    }

    private Pageable getPageable() {
        Method method = null;
        try {
            method = this.getClass().getMethod("configPageable", Pageable.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        MethodParameter methodParameter = new MethodParameter(method, 0);
        return pageableArgumentResolver.resolveArgument(methodParameter, null, nativeWebRequest, null);
    }

    public void configPageable(Pageable pageable) {
    }

    public void configurePredicate(@QuerydslPredicate(root = Product.class) Predicate predicate) {
    }

    private Predicate createPredicate(String configurePredicate) {
        Method method = null;
        try {
            method = this.getClass().getMethod(configurePredicate, Predicate.class);

            MethodParameter methodParameter = new MethodParameter(method, 0);
            return (Predicate) querydslPredicateArgumentResolver.resolveArgument(methodParameter, null, nativeWebRequest, null);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private Predicate predicateCreator(String productName,
                                       String productCategory, Long priceFrom, Long priceTo) {
        BooleanBuilder bb = new BooleanBuilder();

        if (productName != null) {
            bb.and(QProduct.product.name.containsIgnoreCase(productName));
        }
        if (productCategory != null) {
            bb.and(QProduct.product.category.name.startsWithIgnoreCase(productCategory));
        }
        if (priceFrom != null && priceTo != null) {
            bb.and(QProduct.product.price.between(priceFrom, priceTo));
        } else if (priceFrom != null) {
            bb.and(QProduct.product.price.goe(priceFrom));
        } else if (priceTo != null) {
            bb.and(QProduct.product.price.loe(priceTo));
        }
        return bb;
    }

    @Override
    public ResponseEntity<List<GetApiProductHistoryId200ResponseInner>> getApiProductHistoryId(Long id) {
        return ResponseEntity.ok(productService.getProductPriceHistory(id));
    }
}
