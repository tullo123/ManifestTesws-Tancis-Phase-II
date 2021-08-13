package com.ManifestTeswTancis.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ManifestAmendmentDto implements Serializable {
   private String communicationAgreedId;
   private String amendmentReference;
   private String amendDate;
   private String mrn;
   private String voyageNumber;
   private String amendType;
   private Bl bl;
   List<Containers> containers;
}
