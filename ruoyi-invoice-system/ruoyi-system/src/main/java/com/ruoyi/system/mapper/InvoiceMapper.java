package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Invoice;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Date;

/**
 * 发票管理 数据层
 * 
 * @author ruoyi
 */
public interface InvoiceMapper
{
    /**
     * 查询发票信息
     * 
     * @param invoiceId 发票ID
     * @return 发票信息
     */
    public Invoice selectInvoiceById(Long invoiceId);

    /**
     * 查询发票列表
     * 
     * @param invoice 发票信息
     * @return 发票集合
     */
    public List<Invoice> selectInvoiceList(Invoice invoice);

    /**
     * 新增发票
     * 
     * @param invoice 发票信息
     * @return 结果
     */
    public int insertInvoice(Invoice invoice);

    /**
     * 修改发票
     * 
     * @param invoice 发票信息
     * @return 结果
     */
    public int updateInvoice(Invoice invoice);

    /**
     * 删除发票
     * 
     * @param invoiceId 发票ID
     * @return 结果
     */
    public int deleteInvoiceById(Long invoiceId);

    /**
     * 批量删除发票
     * 
     * @param invoiceIds 需要删除的数据ID
     * @return 结果
     */
    public int deleteInvoiceByIds(Long[] invoiceIds);

    /**
     * 根据上传者ID查询发票列表
     * 
     * @param uploaderId 上传者ID
     * @return 发票集合
     */
    public List<Invoice> selectInvoiceByUploaderId(Long uploaderId);

    /**
     * 根据管理员ID查询发票列表
     * 
     * @param adminId 管理员ID
     * @return 发票集合
     */
    public List<Invoice> selectInvoiceByAdminId(Long adminId);

    /**
     * 根据状态查询发票列表
     * 
     * @param status 状态
     * @return 发票集合
     */
    public List<Invoice> selectInvoiceByStatus(String status);

    /**
     * 更新发票状态
     * 
     * @param invoiceId 发票ID
     * @param status 状态
     * @return 结果
     */
    public int updateInvoiceStatus(@Param("invoiceId") Long invoiceId, @Param("status") String status);

    /**
     * 更新报销状态
     * 
     * @param invoiceId 发票ID
     * @param reimbursementStatus 报销状态
     * @return 结果
     */
    public int updateReimbursementStatus(@Param("invoiceId") Long invoiceId, @Param("reimbursementStatus") String reimbursementStatus);

    /**
     * 根据条件筛选发票（上传者）
     * 
     * @param uploaderId 上传者ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param status 状态
     * @param reimbursementStatus 报销状态
     * @return 发票集合
     */
    public List<Invoice> selectInvoiceByUploaderFilter(
        @Param("uploaderId") Long uploaderId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("status") String status,
        @Param("reimbursementStatus") String reimbursementStatus);

    /**
     * 根据条件筛选发票（管理员）
     * 
     * @param adminId 管理员ID
     * @param uploaderId 上传者ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param reimbursementStatus 报销状态
     * @return 发票集合
     */
    public List<Invoice> selectInvoiceByAdminFilter(
        @Param("adminId") Long adminId,
        @Param("uploaderId") Long uploaderId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("reimbursementStatus") String reimbursementStatus);

    /**
     * 根据条件筛选发票（超级管理员）
     * 
     * @param uploaderId 上传者ID
     * @param adminId 管理员ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param status 状态
     * @param reimbursementStatus 报销状态
     * @return 发票集合
     */
    public List<Invoice> selectInvoiceBySuperAdminFilter(
        @Param("uploaderId") Long uploaderId,
        @Param("adminId") Long adminId,
        @Param("startDate") Date startDate,
        @Param("endDate") Date endDate,
        @Param("status") String status,
        @Param("reimbursementStatus") String reimbursementStatus);

    /**
     * 统计发票数量
     * 
     * @param invoice 查询条件
     * @return 发票数量
     */
    public int countInvoice(Invoice invoice);

    /**
     * 统计金额总和
     * 
     * @param invoice 查询条件
     * @return 金额总和
     */
    public Double sumInvoiceAmount(Invoice invoice);
}
