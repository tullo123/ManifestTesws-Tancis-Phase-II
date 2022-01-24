package com.ManifestTeswTancis.Entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "TANCISINT.CL_FI_GEPG")
public class BillGePGEntity implements Serializable {
    @Id
    @NotNull
    @Column(name="BILL_CUSTOMS_OFFICE_CD")
    private String billCustomsOfficeCd;

    @Column(name="BILL_YY")
    private String billYy;

	@Column(name="BILL_TYPE_CD")
    private String billTypeCd;

	@Column(name="BILL_SERIAL_NO")
    private String billSerialNumber;

	@Column(name="BILL_DG")
    private Double billDg;

	@Column(name="GEPG_CONTROL_NO")
    private String gepgControlNo;

	@Column(name="GEPG_DT")
    private Date gepgDate;

	@Column(name="FIRST_REGISTER_ID")
    private String firstRegisterId;

	@Column(name="FIRST_REGISTER_DT")
    private Date firstRegisterDate;

	@Column(name="LAST_UPDATE_ID")
    private String lastUpdateId;

	@Column(name="LAST_UPDATE_DT")
    private Date lastUpdateDate;
}
