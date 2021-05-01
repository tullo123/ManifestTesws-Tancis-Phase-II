package com.ManifestTeswTancis.idEntities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class HouseBlId implements Serializable{
	private static final long serialVersionUID = 2492961508424042418L;
	private String masterBillOfLading;
	private String mrn;
	private String houseBillOfLading;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((houseBillOfLading == null) ? 0 : houseBillOfLading.hashCode());
		result = prime * result + ((masterBillOfLading == null) ? 0 : masterBillOfLading.hashCode());
		result = prime * result + ((mrn == null) ? 0 : mrn.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		HouseBlId other = (HouseBlId) obj;
		if (houseBillOfLading == null) {
			if (other.houseBillOfLading != null)
				return false;
		} else if (!houseBillOfLading.equals(other.houseBillOfLading))
			return false;
		if (masterBillOfLading == null) {
			if (other.masterBillOfLading != null)
				return false;
		} else if (!masterBillOfLading.equals(other.masterBillOfLading))
			return false;
		if (mrn == null) {
			if (other.mrn != null)
				return false;
		} else if (!mrn.equals(other.mrn))
			return false;
		return true;
	}
	
}
