/*
 * This file is generated by jOOQ.
 */
package generatedEntities.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Organisation implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Long taxNumber;
    private final String name;
    private final Long checkingAccount;

    public Organisation(Organisation value) {
        this.taxNumber = value.taxNumber;
        this.name = value.name;
        this.checkingAccount = value.checkingAccount;
    }

    public Organisation(
        Long taxNumber,
        String name,
        Long checkingAccount
    ) {
        this.taxNumber = taxNumber;
        this.name = name;
        this.checkingAccount = checkingAccount;
    }

    /**
     * Getter for <code>public.organisation.tax_number</code>.
     */
    public Long getTaxNumber() {
        return this.taxNumber;
    }

    /**
     * Getter for <code>public.organisation.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for <code>public.organisation.checking_account</code>.
     */
    public Long getCheckingAccount() {
        return this.checkingAccount;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Organisation other = (Organisation) obj;
        if (this.taxNumber == null) {
            if (other.taxNumber != null)
                return false;
        }
        else if (!this.taxNumber.equals(other.taxNumber))
            return false;
        if (this.name == null) {
            if (other.name != null)
                return false;
        }
        else if (!this.name.equals(other.name))
            return false;
        if (this.checkingAccount == null) {
            if (other.checkingAccount != null)
                return false;
        }
        else if (!this.checkingAccount.equals(other.checkingAccount))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.taxNumber == null) ? 0 : this.taxNumber.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.checkingAccount == null) ? 0 : this.checkingAccount.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Organisation (");

        sb.append(taxNumber);
        sb.append(", ").append(name);
        sb.append(", ").append(checkingAccount);

        sb.append(")");
        return sb.toString();
    }
}
