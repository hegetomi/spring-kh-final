package com.hegetomi.catalogservice.service;

import com.hegetomi.catalogservice.model.Category;
import com.hegetomi.catalogservice.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;


    public Optional<Category> saveNewCategory(String string) {
        Optional<Category> categoryOptional = categoryRepository.findByName(string);
        if (categoryOptional.isEmpty()) {
            Category category = new Category();
            category.setName(string);
            return Optional.of(categoryRepository.save(category));
        }
        return Optional.empty();

    }
}
