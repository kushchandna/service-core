package com.kush.lib.service.server.authorization;

import static com.google.common.base.Strings.isNullOrEmpty;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;

public final class Permission implements Serializable, Comparable<Permission> {

    private static final long serialVersionUID = 1L;

    private final String[] keys;

    public static Permission get(String... keys) {
        return new Permission(keys);
    }

    private Permission(String... keys) {
        validateKeys(keys);
        this.keys = Arrays.copyOf(keys, keys.length);
    }

    public boolean isChildOf(Permission permission) {
        return isParentChildRelationshipApplicable(permission, this);
    }

    public boolean isParentOf(Permission permission) {
        return isParentChildRelationshipApplicable(this, permission);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(keys);
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
        Permission other = (Permission) obj;
        if (!Arrays.equals(keys, other.keys)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return Arrays.toString(keys);
    }

    @Override
    public int compareTo(Permission o) {
        return o == null ? 1 : this.toString().compareTo(o.toString());
    }

    private static boolean isParentChildRelationshipApplicable(Permission parent, Permission child) {
        int parentKeysLength = parent.keys.length;
        int childKeysLength = child.keys.length;
        if (parentKeysLength >= childKeysLength) {
            return false;
        }
        String[] childKeysMatchingParentLeaf = Arrays.copyOf(child.keys, parentKeysLength);
        return Arrays.equals(parent.keys, childKeysMatchingParentLeaf);
    }

    private static void validateKeys(String... keys) {
        Objects.requireNonNull(keys, "keys");
        if (keys.length == 0) {
            throw new IllegalArgumentException("Keys can not be empty");
        }
        for (String key : keys) {
            if (isNullOrEmpty(key)) {
                throw new IllegalArgumentException("No member of key can be empty");
            }
        }
    }
}
