/*
 * This file is generated by jOOQ.
 */
package generatedEntities.tables.pojos;


import java.io.Serializable;


/**
 * This class is generated by jOOQ.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Integer id;
    private final String name;
    private final String password;

    public Users(Users value) {
        this.id = value.id;
        this.name = value.name;
        this.password = value.password;
    }

    public Users(
        Integer id,
        String name,
        String password
    ) {
        this.id = id;
        this.name = name;
        this.password = password;
    }

    /**
     * Getter for <code>public.users.id</code>.
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * Getter for <code>public.users.name</code>.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Getter for <code>public.users.password</code>.
     */
    public String getPassword() {
        return this.password;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final Users other = (Users) obj;
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
        if (this.password == null) {
            if (other.password != null)
                return false;
        }
        else if (!this.password.equals(other.password))
            return false;
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        result = prime * result + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + ((this.password == null) ? 0 : this.password.hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Users (");

        sb.append(id);
        sb.append(", ").append(name);
        sb.append(", ").append(password);

        sb.append(")");
        return sb.toString();
    }
}
