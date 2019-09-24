package com.laurynas.banktransfer.api.model;

import io.micronaut.http.hateoas.Link;

public class ObjectRef {

    private Long id;

    private Link link;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }
}
