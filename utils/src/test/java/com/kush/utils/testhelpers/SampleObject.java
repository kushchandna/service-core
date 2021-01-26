package com.kush.utils.testhelpers;

import java.io.Serializable;

public class SampleObject implements Serializable, Cloneable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public SampleObject clone() {
        SampleObject object = new SampleObject();
        object.setId(id);
        object.setName(name);
        return object;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (id == null ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "SampleObject " + id;
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
        SampleObject other = (SampleObject) obj;
        if (id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!id.equals(other.id)) {
            return false;
        }
        return true;
    }

    public static class Builder {

        private String id;
        private String name;

        public SampleObject build() {
            SampleObject sampleObject = new SampleObject();
            sampleObject.setId(id);
            sampleObject.setName(name);
            return sampleObject;
        }

        public SampleObject.Builder withId(String id) {
            this.id = id;
            return this;
        }

        public SampleObject.Builder withName(String name) {
            this.name = name;
            return this;
        }
    }
}
