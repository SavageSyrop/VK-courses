package ru.vk;

import com.google.inject.Inject;

import generatedEntities.tables.pojos.Product;
import org.jetbrains.annotations.NotNull;
import org.jooq.DSLContext;
import java.util.List;
import static generatedEntities.tables.Product.PRODUCT;


public class ProductDAO {
    private final DSLContext context;

    @Inject
    public ProductDAO(DSLContext context) {
        this.context = context;
    }

    public List<Product> getAll() {
        return context.selectFrom(PRODUCT).fetchInto(Product.class);
    }

    public void create(@NotNull Product product) {
        if (context.executeInsert(context.newRecord(PRODUCT, product)) == 0) {
            throw new IllegalStateException("Error during insertion");
        }
    }

    public void deleteProductsByName(@NotNull String productName) {
        if (context.delete(PRODUCT).where(PRODUCT.NAME.eq(productName)).execute() == 0) {
            throw new IllegalStateException("Product with name equal to " + productName + " not found");
        }
    }

    public List<Product> getAllProductsByCompanyName(@NotNull String companyName) {
        return context.selectFrom(PRODUCT).where(PRODUCT.COMPANY_NAME.eq(companyName)).fetchInto(Product.class);
    }
}
