package com.slava.dao.interfaces;

import java.util.Optional;

public interface ILocationDAO<E, L> {
    void saveLocation(L location);

}
