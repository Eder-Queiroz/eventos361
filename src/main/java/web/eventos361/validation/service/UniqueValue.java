package web.eventos361.validation.service;

public interface UniqueValue {
	boolean isValueUnique(Object value, String fieldName) throws UnsupportedOperationException;
}
