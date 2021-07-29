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
public class ManifestAmendmentDto {
    private String callId;
    private String amendmentReference;
    private String mrn;
    private String voyageNumber;
    private String declarantTin;
    List <DeletedBlDto> deletedBls;
    List <AdditionalBls> additionalBls;
    List <AmendedBls> amendedBls;
    List<AdditionalContainer>additionalContainers;
    List<ContainerDtoAmend> amendedContainers;
    List<DeletedContainerDto>deletedContainers;
    List<AmendedBillOfLadingDto> consignments;


}
