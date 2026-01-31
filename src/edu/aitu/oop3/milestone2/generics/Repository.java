package edu.aitu.oop3.milestone2.generics;

import java.util.List;

public interface Repository <T, ID> {
    T findById(ID id);
}
