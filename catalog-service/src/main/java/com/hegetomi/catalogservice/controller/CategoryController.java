package com.hegetomi.catalogservice.controller;

import com.hegetomi.catalogservice.api.CategoryControllerApi;
import com.hegetomi.catalogservice.api.model.PostApiCategoryRequest;
import com.hegetomi.catalogservice.model.Category;
import com.hegetomi.catalogservice.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;


@RestController
@RequiredArgsConstructor
public class CategoryController implements CategoryControllerApi {

    private final CategoryService categoryService;
    private final NativeWebRequest nativeWebRequest;

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return Optional.of(nativeWebRequest);
    }

    @Override
    @PreAuthorize("hasAuthority('admin')")
    public ResponseEntity<Void> postApiCategory(PostApiCategoryRequest postApiCategoryRequest) {
        Optional<Category> category = categoryService.saveNewCategory(postApiCategoryRequest.getString());

        if (category.isPresent()) {
            return ResponseEntity.status(201).build();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
}
