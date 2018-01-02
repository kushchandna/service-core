package com.kush.lib.service.api.server.id;

public class Identifier {

    public static final Identifier NULL = new Identifier(null);

    private final Object key;

    private Identifier(Object key) {
        this.key = key;
    }

    public static Identifier id(Object key) {
        return new Identifier(key);
    }

    @Override
    public int hashCode() {
        int prime = 31;
        int result = 1;
        result = prime * result + ((key == null) ? 0 : key.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Identifier other = (Identifier) obj;
        if (key == null) {
            if (other.key != null) {
                return false;
            }
        } else if (!key.equals(other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Id [" + key + "]";
    }
}
