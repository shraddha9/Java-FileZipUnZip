package com.test.company.service.handlers;

import org.springframework.stereotype.Component;

@Component
public class RarCompDecompHandler implements CompDecompHandler {
	private final String techType = "RAR";

	@Override
	public boolean canHandleRequest(String techType) {
		return this.techType.equals(techType);
	}

	@Override
	public void compressFile(String inputDir, String outputDir, long compressedMaxFileSize) {

	}

	@Override
	public void decompressFile(String inputDir, String outputDir) {

	}

}
