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
public class AmendmentContainer {
    private List<ContainerDto> amendedContainers;
    //private ContainerDto[] amendedContainers;
    private List<ContainerDto> additionalContainers;
    //private ContainerDto[] additionalContainers;
    private List<DeletedContainerDto> deletedContainers;
   // private DeletedContainerDto[] deletedContainers;
}
