package com.hegetomi.catalogservice.service;

import com.hegetomi.catalogservice.api.model.GetApiProductHistoryId200ResponseInner;
import com.hegetomi.catalogservice.model.Product;
import com.hegetomi.catalogservice.repository.ProductRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.DefaultRevisionEntity;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    @PersistenceContext
    private final EntityManager entityManager;


    public Optional<Product> save(Product dtoToProduct) {
        return Optional.of(productRepository.save(dtoToProduct));
    }


    public Optional<Product> modifyById(Long id, Product dtoToProduct) {
        Optional<Product> productOptional = productRepository.findById(id);
        if (productOptional.isPresent()) {
            dtoToProduct.setId(id);
            return Optional.of(productRepository.save(dtoToProduct));
        }
        return Optional.empty();
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findAll(Predicate predicate, Pageable pageable) {
        List<Product> all = productRepository.findAll(predicate,pageable).stream().toList();
        List<Long> ids = all.stream().map(Product::getId).toList();
        all = productRepository.findAllWithCategory(ids);
        return all;
    }

    @Transactional
    @Cacheable("productPriceHistory")
    @SuppressWarnings("unchecked")
    public List<GetApiProductHistoryId200ResponseInner> getProductPriceHistory(Long id){
        return AuditReaderFactory.get(entityManager)
                .createQuery()
                .forRevisionsOfEntity(Product.class, false, true)
                .add(AuditEntity.property("id").eq(id))
                .getResultList()
                .stream().map(obj -> {
                    Object[] objArray = (Object[]) obj;
                    DefaultRevisionEntity revEntity = (DefaultRevisionEntity) objArray[1];
                    Product currProduct = (Product) objArray[0];
                    GetApiProductHistoryId200ResponseInner currHistory = new GetApiProductHistoryId200ResponseInner();
                    currHistory.setPrice(currProduct.getPrice());
                    currHistory.setDateColon(revEntity.getRevisionDate().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDateTime());
                    return currHistory;
                }).toList();
    }


}
