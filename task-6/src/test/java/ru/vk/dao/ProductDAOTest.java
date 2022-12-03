package ru.vk.dao;

import generatedEntities.tables.records.OrganisationRecord;
import generatedEntities.tables.records.ProductRecord;
import generatedEntities.tables.records.ReceiptItemRecord;
import generatedEntities.tables.records.ReceiptRecord;
import org.jooq.Record2;
import org.jooq.Record4;
import org.jooq.Record5;
import org.jooq.exception.DataAccessException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

import static generatedEntities.tables.Organisation.ORGANISATION;
import static generatedEntities.tables.Receipt.RECEIPT;
import static generatedEntities.tables.ReceiptItem.RECEIPT_ITEM;
import static org.junit.jupiter.api.Assertions.*;

class ProductDAOTest extends AbstractTest {

    private final ProductDAO productDAO;

    private final ReceiptItemDAO receiptItemDAO;

    private final OrganisationDAO organisationDAO;

    private final ReceiptDAO receiptDAO;

    ProductDAOTest() {
        this.productDAO = new ProductDAO(getConnection());
        this.receiptItemDAO = new ReceiptItemDAO(getConnection());
        this.organisationDAO = new OrganisationDAO(getConnection());
        this.receiptDAO = new ReceiptDAO(getConnection());
    }


    @Test
    void getWhenProductCodeFound() {
        assertNotNull(productDAO.get(1));
    }

    @Test
    void getWhenProductCodeNotFound() {
        assertThrows(IllegalStateException.class, () -> productDAO.get(54));
    }

    @Test
    void getAll() {
        assertNotNull(productDAO.getAll());
    }

    @Test
    void deleteWhenIsReferencedByOtherEntities() {
        int code = 1;
        assertThrows(DataAccessException.class, () -> productDAO.delete(code));
        assertNotNull(productDAO.get(code));
    }

    @Test
    void deleteWhenIsNotReferencedByOtherEntities() {
        productDAO.delete(8);
        assertThrows(IllegalStateException.class, () -> productDAO.get(8));
    }

    @Test
    void update() {
        int code = 2;
        ProductRecord product = productDAO.get(code);
        String oldName = "Хлеб черный";
        assertEquals(product.getName(), oldName);
        product.setName("Хлеб шоколадный");
        productDAO.update(product);
        assertNotEquals(productDAO.get(code).getName(), oldName);
    }


    @Test
    void create() {
        List<ProductRecord> productRecords = productDAO.getAll();
        int sizeBefore = productRecords.size();
        assertThrows(IllegalStateException.class, () -> productDAO.get(sizeBefore + 1));
        ProductRecord product = new ProductRecord();
        product.setName("Хлеб особенный");
        productDAO.create(product);
        productRecords = productDAO.getAll();
        int sizeAfter = productRecords.size();
        assertTrue(sizeBefore < sizeAfter);
        assertNotNull(productDAO.get(sizeAfter));
    }


    @Test
    void getEveryDayProductStatsBetweenDates() {
        LocalDate from = LocalDate.of(2022, 11, 11);
        LocalDate to = LocalDate.of(2022, 11, 21);

        int queryTotalPrice = 0;
        int codeTotalPrice = 0;

        List<Record5<LocalDate, Integer, Integer, Integer, Integer>> queryResult = productDAO.getEveryDayProductStatsBetweenDates(from, to);

        for (Record5<LocalDate, Integer, Integer, Integer, Integer> record : queryResult) {
            LocalDate creationDate = record.get(RECEIPT.CREATION_DATE);

            Object totalPrice = record.get("total_price");
            if (totalPrice == null) {
                fail();
            } else {
                queryTotalPrice += Integer.parseInt(totalPrice.toString());
            }
            boolean isOnBorderDates = from.equals(creationDate) || creationDate.equals(to);
            boolean isBetweenDates = (from.isBefore(creationDate) && to.isAfter(creationDate));
            assertTrue(isOnBorderDates || isBetweenDates);
        }

        for (ReceiptItemRecord item : receiptItemDAO.getAll()) {
            codeTotalPrice += item.getPrice() * item.getAmount();
        }
        assertEquals(codeTotalPrice, queryTotalPrice);
    }

    @Test
    void getAveragePriceOfProductsBetweenDates() {
        LocalDate from = LocalDate.of(2022, 11, 11);
        LocalDate to = LocalDate.of(2022, 11, 21);

        Map<Integer, Double> averagePriceForEveryProduct = new TreeMap<>();
        Map<Integer, Integer> productCount = new HashMap<>();
        for (ReceiptItemRecord receiptItem : receiptItemDAO.getAll()) {
            Integer productCode = receiptItem.getProductCode();
            Double productPriceSum = averagePriceForEveryProduct.get(productCode);
            if (productPriceSum == null) {
                productPriceSum = 0D;
            }
            productPriceSum += receiptItem.getPrice() * receiptItem.getAmount();
            averagePriceForEveryProduct.put(productCode, productPriceSum);
            Integer prCount = productCount.get(productCode);
            if (prCount == null) {
                prCount = 0;
            }
            prCount += receiptItem.getAmount();
            productCount.put(productCode, prCount);
        }
        for (Map.Entry<Integer, Double> element : averagePriceForEveryProduct.entrySet()) {
            averagePriceForEveryProduct.put(element.getKey(), element.getValue() / productCount.get(element.getKey()));
        }

        List<Double> productTotalPrices = averagePriceForEveryProduct.values().stream().toList();
        List<Record2<Integer, BigDecimal>> queryResult = productDAO.getAveragePriceOfEveryProductBetweenDates(from, to);

        for (int index = 0; index < queryResult.size() - 1; index++) {
            DecimalFormat decimalFormat = new DecimalFormat("###.######");
            assertEquals(decimalFormat.format(queryResult.get(index).get("average_price")), decimalFormat.format(productTotalPrices.get(index)));
        }
    }


    @Test
    void getAllProductsBetweenDates() {
        LocalDate from = LocalDate.of(2022, 11, 11);
        LocalDate to = LocalDate.of(2022, 11, 21);
        Map<Long, Boolean> organisationHasProducts = new HashMap<>();
        for (OrganisationRecord organisation : organisationDAO.getAll()) {
            organisationHasProducts.put(organisation.getTaxNumber(), false);
        }

        Map<Long, List<Integer>> productsByOrganisation = new HashMap<>();
        for (ReceiptItemRecord receiptItem : receiptItemDAO.getAll()) {
            ReceiptRecord receipt = receiptDAO.get(receiptItem.getReceiptId());
            LocalDate creationDate = receipt.getCreationDate();
            Long organisationTaxNumber = receipt.getOrganisationTaxNumber();
            boolean isOnBorderDates = from.equals(creationDate) || creationDate.equals(to);
            boolean isBetweenDates = (from.isBefore(creationDate) && to.isAfter(creationDate));
            if (isOnBorderDates || isBetweenDates) {
                if (!productsByOrganisation.containsKey(organisationTaxNumber)) {
                    productsByOrganisation.put(organisationTaxNumber, new ArrayList<>());
                    organisationHasProducts.put(organisationTaxNumber, true);
                }
                productsByOrganisation.get(organisationTaxNumber).add(receiptItem.getProductCode());
            }
        }

        List<Record4<Integer, String, String, Long>> queryResult = productDAO.getAllProductsBetweenDates(from, to);
        for (Record4<Integer, String, String, Long> record : queryResult) {
            Integer productCode = record.get(RECEIPT_ITEM.PRODUCT_CODE);
            Long organisationTaxNumber = record.get(ORGANISATION.TAX_NUMBER);
            if (productCode == null) {
                assertFalse(productsByOrganisation.containsKey(organisationTaxNumber));
            } else {
                assertTrue(organisationHasProducts.get(organisationTaxNumber));
                assertTrue(productsByOrganisation.get(organisationTaxNumber).contains(productCode));
            }
        }
    }
}