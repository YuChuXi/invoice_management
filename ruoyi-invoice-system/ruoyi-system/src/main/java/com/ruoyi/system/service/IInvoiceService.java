package com.ruoyi.system.service;

import com.ruoyi.system.domain.Invoice;
import java.util.List;
import java.util.Date;

/**
 * 发票管理 服务层
 * 
 * @author ruoyi
 */
public interface IInvoiceService
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
     * 删除发票信息
     * 
     * @param invoiceId 发票ID
     * @return 结果
     */
    public int deleteInvoiceById(Long invoiceId);

    /**
     * 批量删除发票信息
     * 
     * @param invoiceIds 需要删除的发票ID
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
    public int updateInvoiceStatus(Long invoiceId, String status);

    /**
     * 更新报销状态
     * 
     * @param invoiceId 发票ID
     * @param reimbursementStatus 报销状态
     * @return 结果
     */
    public int updateReimbursementStatus(Long invoiceId, String reimbursementStatus);

    /**
     * 上传发票文件
     * 
     * @param invoice 发票信息
     * @param fileBytes 文件字节数组
     * @param originalFilename 原始文件名
     * @return 结果
     */
    public int uploadInvoiceFile(Invoice invoice, byte[] fileBytes, String originalFilename);

    /**
     * 提交URL链接发票
     * 
     * @param invoice 发票信息
     * @return 结果
     */
    public int submitInvoiceUrl(Invoice invoice);

    /**
     * 重新提交发票
     * 
     * @param invoice 发票信息
     * @param fileBytes 文件字节数组
     * @param originalFilename 原始文件名
     * @return 结果
     */
    public int resubmitInvoice(Invoice invoice, byte[] fileBytes, String originalFilename);

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
        Long uploaderId,
        Date startDate,
        Date endDate,
        String status,
        String reimbursementStatus);

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
        Long adminId,
        Long uploaderId,
        Date startDate,
        Date endDate,
        String reimbursementStatus);

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
        Long uploaderId,
        Long adminId,
        Date startDate,
        Date endDate,
        String status,
        String reimbursementStatus);

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

    /**
     * 验证发票权限
     * 
     * @param invoiceId 发票ID
     * @param userId 用户ID
     * @param userRole 用户角色
     * @return 是否有权限
     */
    public boolean validateInvoicePermission(Long invoiceId, Long userId, String userRole);

    /**
     * 下载发票文件
     * 
     * @param invoiceId 发票ID
     * @return 文件信息
     */
    public byte[] downloadInvoiceFile(Long invoiceId);

    /**
     * 获取发票文件信息
     * 
     * @param invoiceId 发票ID
     * @return 文件信息
     */
    public String getInvoiceFileInfo(Long invoiceId);
}
