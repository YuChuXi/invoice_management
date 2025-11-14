package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 发票对象 invoice
 * 
 * @author ruoyi
 */
public class Invoice extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 发票ID */
    private Long invoiceId;

    /** 发票号码 */
    private String invoiceNumber;

    /** 供应商名称 */
    private String supplierName;

    /** 购买日期 */
    private Date purchaseDate;

    /** 金额 */
    private Double amount;

    /** 税率 */
    private Double taxRate;

    /** 税额 */
    private Double taxAmount;

    /** 总金额 */
    private Double totalAmount;

    /** 发票类型 */
    private String invoiceType;

    /** 发票状态（pending, approved, rejected） */
    private String status;

    /** 报销状态（unclaimed, claimed, reimbursed） */
    private String reimbursementStatus;

    /** 上传者ID */
    private Long uploaderId;

    /** 管理员ID */
    private Long adminId;

    /** 文件数据 */
    private byte[] fileData;

    /** 文件名 */
    private String fileName;

    /** 文件大小 */
    private Long fileSize;

    /** 文件类型 */
    private String fileType;

    /** 发票URL */
    private String invoiceUrl;

    /** 拒绝原因 */
    private String rejectionReason;

    /** 备注 */
    private String remarks;

    public void setInvoiceId(Long invoiceId) 
    {
        this.invoiceId = invoiceId;
    }

    public Long getInvoiceId() 
    {
        return invoiceId;
    }
    public void setInvoiceNumber(String invoiceNumber) 
    {
        this.invoiceNumber = invoiceNumber;
    }

    public String getInvoiceNumber() 
    {
        return invoiceNumber;
    }
    public void setSupplierName(String supplierName) 
    {
        this.supplierName = supplierName;
    }

    public String getSupplierName() 
    {
        return supplierName;
    }
    public void setPurchaseDate(Date purchaseDate) 
    {
        this.purchaseDate = purchaseDate;
    }

    public Date getPurchaseDate() 
    {
        return purchaseDate;
    }
    public void setAmount(Double amount) 
    {
        this.amount = amount;
    }

    public Double getAmount() 
    {
        return amount;
    }
    public void setTaxRate(Double taxRate) 
    {
        this.taxRate = taxRate;
    }

    public Double getTaxRate() 
    {
        return taxRate;
    }
    public void setTaxAmount(Double taxAmount) 
    {
        this.taxAmount = taxAmount;
    }

    public Double getTaxAmount() 
    {
        return taxAmount;
    }
    public void setTotalAmount(Double totalAmount) 
    {
        this.totalAmount = totalAmount;
    }

    public Double getTotalAmount() 
    {
        return totalAmount;
    }
    public void setInvoiceType(String invoiceType) 
    {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceType() 
    {
        return invoiceType;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setReimbursementStatus(String reimbursementStatus) 
    {
        this.reimbursementStatus = reimbursementStatus;
    }

    public String getReimbursementStatus() 
    {
        return reimbursementStatus;
    }
    public void setUploaderId(Long uploaderId) 
    {
        this.uploaderId = uploaderId;
    }

    public Long getUploaderId() 
    {
        return uploaderId;
    }
    public void setAdminId(Long adminId) 
    {
        this.adminId = adminId;
    }

    public Long getAdminId() 
    {
        return adminId;
    }
    public void setFileData(byte[] fileData) 
    {
        this.fileData = fileData;
    }

    public byte[] getFileData() 
    {
        return fileData;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setFileSize(Long fileSize) 
    {
        this.fileSize = fileSize;
    }

    public Long getFileSize() 
    {
        return fileSize;
    }
    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }
    public void setInvoiceUrl(String invoiceUrl) 
    {
        this.invoiceUrl = invoiceUrl;
    }

    public String getInvoiceUrl() 
    {
        return invoiceUrl;
    }
    public void setRejectionReason(String rejectionReason) 
    {
        this.rejectionReason = rejectionReason;
    }

    public String getRejectionReason() 
    {
        return rejectionReason;
    }
    public void setRemarks(String remarks) 
    {
        this.remarks = remarks;
    }

    public String getRemarks() 
    {
        return remarks;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("invoiceId", getInvoiceId())
            .append("invoiceNumber", getInvoiceNumber())
            .append("supplierName", getSupplierName())
            .append("purchaseDate", getPurchaseDate())
            .append("amount", getAmount())
            .append("taxRate", getTaxRate())
            .append("taxAmount", getTaxAmount())
            .append("totalAmount", getTotalAmount())
            .append("invoiceType", getInvoiceType())
            .append("status", getStatus())
            .append("reimbursementStatus", getReimbursementStatus())
            .append("uploaderId", getUploaderId())
            .append("adminId", getAdminId())
            .append("fileName", getFileName())
            .append("fileSize", getFileSize())
            .append("fileType", getFileType())
            .append("invoiceUrl", getInvoiceUrl())
            .append("rejectionReason", getRejectionReason())
            .append("remarks", getRemarks())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
