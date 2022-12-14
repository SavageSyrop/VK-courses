/*
 * This file is generated by jOOQ.
 */
package generatedEntities.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Product implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final String name;
    private final String companyName;
    private final Integer amount;

    public Product(Product value) {
        this.id = value.id;
        this.name = value.name;
        this.companyName = value.companyName;
        this.amount = value.amount;
    }

    public Product(
        Integer id,
        String name,
        String companyName,
        Integer amount
    ) {
        this.id = id;
        this.name = name;
        this.companyName = companyName;
        this.amount = amount;
    }

    /**
     * Getter for <code>public.product.id</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Getter for <code>public.product.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for <code>public.product.company_name</code>.
     */
    public String getCompanyName() {
        return this.companyName;
    }

    /**
     * Getter for <code>public.product.amount</code>.
     */
    public Integer getAmount() {
        return this.amount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Product other = (Product) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        }
        else if (!this.id.equals(other.id))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.companyName == null) {
            if (other.companyName != null)
                return false;
        }
        else if (!this.companyName.equals(other.companyName))
            return false;
        if (this.amount == null) {
            if (other.amount != null)
                return false;
        }
        else if (!this.amount.equals(other.amount))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.companyName == null) ? 0 : this.companyName.hashCode());
        result = prime * result + ((this.amount == null) ? 0 : this.amount.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Product (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(companyName);
        sb.append(", ").append(amount);

        sb.append(")");
        return sb.toString();
    }
}