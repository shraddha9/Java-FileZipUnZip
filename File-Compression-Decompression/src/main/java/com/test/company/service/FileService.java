package com.test.company.service;

import com.test.company.model.FileCompressionRequest;
import com.test.company.model.FileDecompressionRequest;

public interface FileService {

	void compressFile(FileCompressionRequest request);

	void decompressFile(FileDecompressionRequest request); 

}
