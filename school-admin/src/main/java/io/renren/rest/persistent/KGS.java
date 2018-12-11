package io.renren.rest.persistent;

public interface KGS {
    int nextId();

    Integer nextId(String nameSpace);
}
