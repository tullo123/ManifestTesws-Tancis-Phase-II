package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AmendmentBl {
    //private List<BillOfLadingDto> additionalBls;
    private AmendedBillOfLadingDto[] amendedBls;
    //private List<DeletedBlDto> deletedBls;
    private DeletedBlDto[] deletedBls;
    private AmendedBillOfLadingDto[] additionalBls;
}
