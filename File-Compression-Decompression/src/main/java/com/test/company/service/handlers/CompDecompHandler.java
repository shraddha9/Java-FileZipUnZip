package com.test.company.service.handlers;

public interface CompDecompHandler {

	boolean canHandleRequest(String techType);

	void compressFile(String inputDir, String outputDir, long compressedMaxFileSize);

	void decompressFile(String inputDir, String outputDir); 

}
