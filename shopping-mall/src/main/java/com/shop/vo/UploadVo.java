package com.shop.vo;

import org.springframework.web.multipart.MultipartFile;

public class UploadVo {

	private MultipartFile multipartFile;

	private String tableName;

	private String excelTitle;

	private String[] batchImportColumn;

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	
	public MultipartFile getMultipartFile() {
		return multipartFile;
	}

	public void setMultipartFile(MultipartFile multipartFile) {
		this.multipartFile = multipartFile;
	}

	public String getExcelTitle() {
		return excelTitle;
	}

	public void setExcelTitle(String excelTitle) {
		this.excelTitle = excelTitle;
	}

	public String[] getBatchImportColumn() {
		return batchImportColumn;
	}

	public void setBatchImportColumn(String[] batchImportColumn) {
		this.batchImportColumn = batchImportColumn;
	}

}
