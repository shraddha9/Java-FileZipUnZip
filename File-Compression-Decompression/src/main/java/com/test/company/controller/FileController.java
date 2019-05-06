package com.test.company.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.test.company.model.FileCompressionRequest;
import com.test.company.model.FileDecompressionRequest;
import com.test.company.service.FileService;

@Controller
@RequestMapping(value="/file")
public class FileController {

	@Autowired private FileService fileService;

	@ResponseBody
	@RequestMapping(value="/compress", method=RequestMethod.POST,
	consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public String compressFile(final HttpServletRequest request, HttpServletResponse response,
			@RequestBody final FileCompressionRequest fileCompressionRequest) {
		fileService.compressFile(fileCompressionRequest);
		return "Files Compressed Successfully";
	}
	@ResponseBody
	@RequestMapping(value="/decompress", method=RequestMethod.POST,
	consumes=MediaType.APPLICATION_JSON_VALUE, produces=MediaType.APPLICATION_JSON_VALUE)
	public String decompressFile(final HttpServletRequest request, HttpServletResponse response,
			@RequestBody final FileDecompressionRequest fileDecompressionRequest) {
		fileService.decompressFile(fileDecompressionRequest);
		return "Files Decompressed Successfully";
	}

}
