package com.test.company.model;

import java.io.Serializable;

public class FileCompressionRequest implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String inputDir;
	
	private String outputDir;
	
	private long maxCompressedFileSize;
	
	private String compressionTech;

	public String getInputDir() {
		return inputDir;
	}

	public void setInputDir(String inputDir) {
		this.inputDir = inputDir;
	}

	public String getOutputDir() {
		return outputDir;
	}

	public void setOutputDir(String outputDir) {
		this.outputDir = outputDir;
	}

	public long getMaxCompressedFileSize() {
		return maxCompressedFileSize;
	}

	public void setMaxCompressedFileSize(long maxCompressedFileSize) {
		this.maxCompressedFileSize = maxCompressedFileSize;
	}

	public String getCompressionTech() {
		return compressionTech;
	}

	public void setCompressionTech(String compressionTech) {
		this.compressionTech = compressionTech;
	}

}
