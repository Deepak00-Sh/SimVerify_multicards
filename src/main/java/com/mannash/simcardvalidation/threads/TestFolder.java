package com.mannash.simcardvalidation.threads;

import java.io.File;

public class TestFolder {

	public static void main(String[] args) {
		File file = new File ("C:\\logs\\mudit\\123\\456");
		if(!file.exists()) {
			file.mkdir();
		}
	}
}
