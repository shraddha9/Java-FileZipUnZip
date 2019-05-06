package com.test.company.util;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileUtils {

	public static String zipDir(String inputDir, String outputDir, String zipFileName) {
		List <String> fileList = new ArrayList<String>();
		prepareFileList(new File(inputDir), inputDir, fileList);

		byte[] buffer = new byte[1024];
		String source = new File(inputDir).getName();
		FileOutputStream fileOutputStream = null;
		ZipOutputStream zipOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(inputDir);
			zipOutputStream = new ZipOutputStream(fileOutputStream);
			FileInputStream in = null;

			for (String file: fileList) {
				ZipEntry ze = new ZipEntry(source + File.separator + file);
				zipOutputStream.putNextEntry(ze);
				try {
					in = new FileInputStream(inputDir + File.separator + file);
					int len;
					while ((len = in .read(buffer)) > 0) {
						zipOutputStream.write(buffer, 0, len);
					}
				} finally {
					in.close();
				}
			}

			zipOutputStream.closeEntry();

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zipOutputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return outputDir + zipFileName;
	}

	private static void prepareFileList(File node, String inputDir, List<String> fileList) {
		if (node.isFile()) {
			fileList.add(node.toString().substring(inputDir.length() + 1, node.toString().length()));
		}
		if (node.isDirectory()) {
			String[] childFile = node.list();
			for (String filename: childFile) {
				prepareFileList(new File(node, filename), inputDir, fileList);
			}
		}
	}

	public static void splitZipIntoSmallZip(String zipFileName, String zippedPath, String zipCopyDest2) throws IOException{
		FileInputStream fileInputSream  = new FileInputStream(zippedPath);
		ZipInputStream zipInputStream = new ZipInputStream(fileInputSream);
		ZipEntry entry = null;
		long entrySize = 0;
		ZipFile zipFile = new ZipFile(zippedPath);
		Enumeration<?> enumeration = zipFile.entries();

		String copDest = zipCopyDest2 +".zip";

		FileOutputStream fos = new FileOutputStream(new File(copDest));
		BufferedOutputStream bos = new BufferedOutputStream(fos);
		ZipOutputStream zos = new ZipOutputStream(bos);
		long currentSize = 0; 

		try {
			while ((entry = zipInputStream.getNextEntry()) != null && enumeration.hasMoreElements()) {

				ZipEntry zipEntry = (ZipEntry) enumeration.nextElement();
				entrySize = zipEntry.getSize();

				ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

				currentSize += entrySize;
				zos.putNextEntry(new ZipEntry(entry.getName()));
				byte[] buffer = new byte[8192];
				int length = 0;
				while ((length = zipInputStream.read(buffer)) > 0) {
					outputStream.write(buffer, 0, length);
				}

				byte[] unzippedFile = outputStream.toByteArray();
				zos.write(unzippedFile);
				unzippedFile = null;
				outputStream.close();
				zos.closeEntry();
			}
		} finally {
			zos.close();
		}
	}

	public static void unzipDir(String zipFilePath, String destDirectory) throws IOException {
		File destDir = new File(destDirectory);
		if (!destDir.exists()) {
			destDir.mkdir();
		}
		ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
		ZipEntry entry = zipIn.getNextEntry();
		while (entry != null) {
			String filePath = destDirectory + File.separator + entry.getName();
			if (!entry.isDirectory()) {
				extractFile(zipIn, filePath);
			} else {
				File dir = new File(filePath);
				dir.mkdir();
			}
			zipIn.closeEntry();
			entry = zipIn.getNextEntry();
		}
		zipIn.close();
	}

	private static void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
		byte[] bytesIn = new byte[4096];
		int read = 0;
		while ((read = zipIn.read(bytesIn)) != -1) {
			bos.write(bytesIn, 0, read);
		}
		bos.close();
	}

}