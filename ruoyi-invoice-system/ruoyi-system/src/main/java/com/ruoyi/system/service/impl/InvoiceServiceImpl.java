package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Invoice;
import com.ruoyi.system.mapper.InvoiceMapper;
import com.ruoyi.system.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 发票管理 服务层实现
 * 
 * @author ruoyi
 */
@Service
public class InvoiceServiceImpl implements IInvoiceService {

    @Autowired
    private InvoiceMapper invoiceMapper;

    /**
     * 查询发票信息
     */
    @Override
    public Invoice selectInvoiceById(Long invoiceId) {
        return invoiceMapper.selectInvoiceById(invoiceId);
    }

    /**
     * 查询发票列表
     */
    @Override
    public List<Invoice> selectInvoiceList(Invoice invoice) {
        return invoiceMapper.selectInvoiceList(invoice);
    }

    /**
     * 新增发票
     */
    @Override
    public int insertInvoice(Invoice invoice) {
        return invoiceMapper.insertInvoice(invoice);
    }

    /**
     * 修改发票
     */
    @Override
    public int updateInvoice(Invoice invoice) {
        return invoiceMapper.updateInvoice(invoice);
    }

    /**
     * 删除发票信息
     */
    @Override
    public int deleteInvoiceById(Long invoiceId) {
        return invoiceMapper.deleteInvoiceById(invoiceId);
    }

    /**
     * 批量删除发票信息
     */
    @Override
    public int deleteInvoiceByIds(Long[] invoiceIds) {
        return invoiceMapper.deleteInvoiceByIds(invoiceIds);
    }

    /**
     * 根据上传者ID查询发票列表
     */
    @Override
    public List<Invoice> selectInvoiceByUploaderId(Long uploaderId) {
        return invoiceMapper.selectInvoiceByUploaderId(uploaderId);
    }

    /**
     * 根据管理员ID查询发票列表
     */
    @Override
    public List<Invoice> selectInvoiceByAdminId(Long adminId) {
        return invoiceMapper.selectInvoiceByAdminId(adminId);
    }

    /**
     * 根据状态查询发票列表
     */
    @Override
    public List<Invoice> selectInvoiceByStatus(String status) {
        return invoiceMapper.selectInvoiceByStatus(status);
    }

    /**
     * 更新发票状态
     */
    @Override
    public int updateInvoiceStatus(Long invoiceId, String status) {
        return invoiceMapper.updateInvoiceStatus(invoiceId, status);
    }

    /**
     * 更新报销状态
     */
    @Override
    public int updateReimbursementStatus(Long invoiceId, String reimbursementStatus) {
        return invoiceMapper.updateReimbursementStatus(invoiceId, reimbursementStatus);
    }

    /**
     * 上传发票文件
     */
    @Override
    public int uploadInvoiceFile(Invoice invoice, byte[] fileBytes, String originalFilename) {
        // 这里需要实现文件上传逻辑
        // 暂时返回模拟数据
        return 1;
    }

    /**
     * 提交URL链接发票
     */
    @Override
    public int submitInvoiceUrl(Invoice invoice) {
        return invoiceMapper.insertInvoice(invoice);
    }

    /**
     * 重新提交发票
     */
    @Override
    public int resubmitInvoice(Invoice invoice, byte[] fileBytes, String originalFilename) {
        return invoiceMapper.updateInvoice(invoice);
    }

    /**
     * 根据条件筛选发票（上传者）
     */
    @Override
    public List<Invoice> selectInvoiceByUploaderFilter(Long uploaderId, Date startDate, Date endDate, String status, String reimbursementStatus) {
        return invoiceMapper.selectInvoiceByUploaderFilter(uploaderId, startDate, endDate, status, reimbursementStatus);
    }

    /**
     * 根据条件筛选发票（管理员）
     */
    @Override
    public List<Invoice> selectInvoiceByAdminFilter(Long adminId, Long uploaderId, Date startDate, Date endDate, String reimbursementStatus) {
        return invoiceMapper.selectInvoiceByAdminFilter(adminId, uploaderId, startDate, endDate, reimbursementStatus);
    }

    /**
     * 根据条件筛选发票（超级管理员）
     */
    @Override
    public List<Invoice> selectInvoiceBySuperAdminFilter(Long uploaderId, Long adminId, Date startDate, Date endDate, String status, String reimbursementStatus) {
        return invoiceMapper.selectInvoiceBySuperAdminFilter(uploaderId, adminId, startDate, endDate, status, reimbursementStatus);
    }

    /**
     * 统计发票数量
     */
    @Override
    public int countInvoice(Invoice invoice) {
        return invoiceMapper.countInvoice(invoice);
    }

    /**
     * 统计金额总和
     */
    @Override
    public Double sumInvoiceAmount(Invoice invoice) {
        return invoiceMapper.sumInvoiceAmount(invoice);
    }

    /**
     * 验证发票权限
     */
    @Override
    public boolean validateInvoicePermission(Long invoiceId, Long userId, String userRole) {
        // 这里需要实现权限验证逻辑
        // 暂时返回true
        return true;
    }

    /**
     * 下载发票文件
     */
    @Override
    public byte[] downloadInvoiceFile(Long invoiceId) {
        // 这里需要实现文件下载逻辑
        // 暂时返回空数组
        return new byte[0];
    }

    /**
     * 获取发票文件信息
     */
    @Override
    public String getInvoiceFileInfo(Long invoiceId) {
        // 这里需要实现文件信息获取逻辑
        // 暂时返回空字符串
        return "";
    }
}
