package main;

import java.io.FileNotFoundException;

@SuppressWarnings("serial")
public class MyException extends RuntimeException {

	public MyException(String s) {
		super(s);
	}

	public MyException(FileNotFoundException e) {
		super(e);
	}

}
