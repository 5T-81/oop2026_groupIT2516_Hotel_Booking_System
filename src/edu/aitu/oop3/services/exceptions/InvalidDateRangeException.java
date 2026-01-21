package edu.aitu.oop3.services.exceptions;

public class InvalidDateRangeException extends RuntimeException{\
    public InvalidDateRangeException(String message){
    super(message); //use constructor of the parent(RuntimeExc)
    }
}
