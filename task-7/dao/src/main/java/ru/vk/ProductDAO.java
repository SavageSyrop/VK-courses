package ru.vk;

import generatedEntities.tables.records.ProductRecord;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

import static generatedEntities.tables.Product.PRODUCT;


public class ProductDAO extends AbstractDAO<ProductRecord, Integer> {
    public ProductDAO(@NotNull Connection connection) {
        super(connection, PRODUCT, PRODUCT.ID);
    }
}
