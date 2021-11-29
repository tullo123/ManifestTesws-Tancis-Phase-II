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
public class GoodItemsId implements Serializable {
    private String mrn;
    private String masterBillOfLading;
    private String goodsItemNo;
}
