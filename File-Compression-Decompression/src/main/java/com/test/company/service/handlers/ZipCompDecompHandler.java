package com.test.company.service.handlers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

@Component
public class ZipCompDecompHandler implements CompDecompHandler {
	private final String techType = "ZIP";
	private final String zipFilePrefix = "File";

	@Override
	public boolean canHandleRequest(String techType) {
		return this.techType.equals(techType);
	}

	@Override
	public void compressFile(String inputDir, String outputDir, long compressedMaxFileSize) {
		List <String> fileList = new ArrayList<>();
		prepareFileList(new File(inputDir), inputDir, fileList);

		byte[] buffer = new byte[1024];
		FileOutputStream fileOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		try {
			createZipFile(inputDir, outputDir, fileList, buffer, fileOutputStream, zipOutputStream, "1");
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zipOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void createZipFile(String inputDir, String outputDir, List<String> fileList, byte[] buffer,
			FileOutputStream fileOutputStream, ZipOutputStream zipOutputStream, String zipFileSuffix)
					throws FileNotFoundException, IOException {
		String zipFile = outputDir + "/" + zipFilePrefix + zipFileSuffix + ".zip";
		fileOutputStream = new FileOutputStream(zipFile);
		zipOutputStream = new ZipOutputStream(fileOutputStream);
		FileInputStream fileInputStream = null;

		List<String> addedFileList = new ArrayList<String>();
		for (String file: fileList) {
			ZipEntry zipEntry = new ZipEntry(file);
			if(!isZipFileSizeExceed()) {
				addedFileList.add(file);
				zipOutputStream.putNextEntry(zipEntry);
				try {
					fileInputStream = new FileInputStream(inputDir + File.separator + file);
					int len;
					while ((len = fileInputStream .read(buffer)) > 0) {
						zipOutputStream.write(buffer, 0, len);
					}
				} finally {
					fileInputStream.close();
				}
			} else {
				fileList.removeAll(addedFileList);
				String zipUpdatedSuffix = String.valueOf(Integer.valueOf(zipFileSuffix) + 1);
				createZipFile(inputDir, outputDir, addedFileList, buffer, fileOutputStream, zipOutputStream, zipUpdatedSuffix);
			}
		}
		zipOutputStream.closeEntry();
	}

	//TODO - file size logic need to implemented
	private boolean isZipFileSizeExceed() {
		return false;
	}
	
	private void prepareFileList(File node, String inputDir, List<String> fileList) {
		if (node.isFile()) {
			fileList.add(node.toString().substring(inputDir.length() + 1, node.toString().length()));
		} else if (node.isDirectory()) {
			String[] childFile = node.list();
			for (String filename: childFile) {
				prepareFileList(new File(node, filename), inputDir, fileList);
			}
		}
	}

	@Override
	public void decompressFile(String inputDir, String outputDir) {
		long fileCount = new File(inputDir).listFiles().length;
		for(int i = 1; i <= fileCount ; i++) {
			String zipFilePath = inputDir + "/" + zipFilePrefix + i + ".zip";
			File dir = new File(outputDir);
			if(!dir.exists()) dir.mkdirs();
			FileInputStream fileInputStream;
			byte[] buffer = new byte[1024];
			try {
				fileInputStream = new FileInputStream(zipFilePath);
				ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
				ZipEntry zipEntry = zipInputStream.getNextEntry();
				while(zipEntry != null){
					String fileName = zipEntry.getName();
					File newFile = new File(outputDir + File.separator + fileName);
					new File(newFile.getParent()).mkdirs();
					FileOutputStream fos = new FileOutputStream(newFile);
					int len;
					while ((len = zipInputStream.read(buffer)) > 0) {
						fos.write(buffer, 0, len);
					}
					fos.close();
					zipInputStream.closeEntry();
					zipEntry = zipInputStream.getNextEntry();
				}
				zipInputStream.closeEntry();
				zipInputStream.close();
				fileInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}