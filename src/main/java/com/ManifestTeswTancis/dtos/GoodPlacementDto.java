package com.ManifestTeswTancis.dtos;

public class GoodPlacementDto {
	private String containerNo;
	
	public GoodPlacementDto() {
		
	}

	public String getContainerNo() {
		return containerNo;
	}

	public void setContainerNo(String containerNo) {
		this.containerNo = containerNo;
	}

	@Override
	public String toString() {
		return "GoodPlacementDto [containerNo=" + containerNo + "]";
	}
	
}
