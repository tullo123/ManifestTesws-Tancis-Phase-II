package com.ManifestTeswTancis.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "CO_ORDINAL" , schema = "TANCISEXT")
public class CommonOrdinalEntity implements Serializable {
    @Id
    @Column(name = "PREFIX")
    private String prefix;
    @Column(name = "SEQUENCE_NO")
    private int sequenceNo;

}
