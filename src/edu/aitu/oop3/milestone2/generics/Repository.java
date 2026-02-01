package edu.aitu.oop3.milestone2.generics;

import java.util.List;

public interface Repository <T, ID> {
    T findById(ID id);
     // T  -> the ENTITY type
    //ID -> the type of the entity's identifier (e.g. Integer, Long)
}
