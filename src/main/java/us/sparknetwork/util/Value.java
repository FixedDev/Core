package us.sparknetwork.util;

import javax.annotation.Nullable;

import lombok.Setter;

@Setter
public class Value<O> {

    O object;

    public Value(@Nullable O object) {
        this.object = object;
    }

    public Value() {

    }

    @Nullable
    public O getObject() {
        return object;
    }

    public boolean isPresent() {
        return object != null;
    }

}
