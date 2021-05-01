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
public class ManifestNotice {
private String messageReferenceNumber;
private String call_id;
private String mrn;
private String approvalStatus;
private String approvalDt;
private List<ManifestNoticeBl> bls;
}
