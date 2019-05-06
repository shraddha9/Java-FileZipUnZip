package com.test.company.service;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.test.company.model.FileCompressionRequest;
import com.test.company.model.FileDecompressionRequest;
import com.test.company.service.handlers.CompDecompHandler;

@Service
public class FileServiceProvider implements FileService {

	@Autowired private List<CompDecompHandler> compDecompHandlers;

	@Override
	public void compressFile(FileCompressionRequest request) {
		CompDecompHandler compDecompHandler = getCompDecompHandler(request.getCompressionTech());
		compDecompHandler.compressFile(request.getInputDir(), request.getOutputDir(), request.getMaxCompressedFileSize());
	}

	@Override
	public void decompressFile(FileDecompressionRequest request) {
		CompDecompHandler compDecompHandler = getCompDecompHandler(request.getCompressionTech());
		compDecompHandler.decompressFile(request.getInputDir(), request.getOutputDir());
	}

	private CompDecompHandler getCompDecompHandler(String techType) {
		for (CompDecompHandler compDecompHandler : compDecompHandlers) {
			if (compDecompHandler.canHandleRequest(techType)) {
				return compDecompHandler;
			}
		}
		throw new RuntimeException("Invalid compression decompression teqnique");
	}

}
