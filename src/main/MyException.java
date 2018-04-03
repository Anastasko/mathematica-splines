package main;

import java.io.FileNotFoundException;

public class MyException extends RuntimeException {

	public MyException(String string) {
		super(string);
	}

	public MyException(FileNotFoundException e) {
		super(e);
	}

}
