package com.hegetomi.catalogservice.mapper;

import com.hegetomi.catalogservice.api.model.GetApiProduct200ResponseInner;
import com.hegetomi.catalogservice.api.model.PostApiProductRequest;
import com.hegetomi.catalogservice.model.Category;
import com.hegetomi.catalogservice.model.Product;
import com.hegetomi.catalogservice.repository.CategoryRepository;
import com.hegetomi.catalogservice.repository.ProductRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    CategoryRepository categoryRepository;

    @Mapping(source = "category", target = "category")
    public abstract Product dtoToProduct(PostApiProductRequest postApiProductRequest);

    protected Category categoryNameToObject(String categoryName) {
        return categoryRepository.findByName(categoryName).orElse(null);
    }

    @Mapping(source = "category.name", target = "category")
    public abstract GetApiProduct200ResponseInner productToDto(Product product);

}
