package com.clean.arch.example.repository;

import com.clean.arch.example.repository.entities.ProductTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductTable, Integer> {

    @Query(value = "SELECT product FROM ProductTable product WHERE is_deleted = false")
    List<ProductTable> listProducts();

    @Query(value = "SELECT product FROM ProductTable product WHERE code = :code and is_deleted = false")
    ProductTable findProduct(int code);

    @Transactional
    @Modifying
    @Query(value = "UPDATE store.product SET is_Deleted = true WHERE code = :code", nativeQuery = true)
    void deleteProduct(int code);

    @Transactional
    @Modifying
    @Query(value = "UPDATE store.product SET name = :name, description = :description, quantity = :quantity" +
            ",price = :price, updated_date = clock_timestamp() WHERE code = :code", nativeQuery = true)
    void editProduct(int code, String name, String description, int quantity, double price);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM store.product WHERE code = :productCode and " +
            "TRIM(LOWER(\"name\")) = TRIM(LOWER(:name)) and is_Deleted = false)", nativeQuery = true)
    boolean checkIfProductAlreadyExists(int productCode, String name);
}
