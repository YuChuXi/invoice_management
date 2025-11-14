package com.ruoyi.system.controller;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.Invoice;
import com.ruoyi.system.service.IInvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

/**
 * 发票管理Controller
 * 
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/invoice")
public class InvoiceController extends BaseController
{
    private String prefix = "system/invoice";

    @Autowired
    private IInvoiceService invoiceService;

    @GetMapping()
    public String invoice()
    {
        return prefix + "/invoice";
    }

    /**
     * 查询发票列表
     */
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(Invoice invoice)
    {
        startPage();
        List<Invoice> list = invoiceService.selectInvoiceList(invoice);
        return getDataTable(list);
    }

    /**
     * 导出发票列表
     */
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(Invoice invoice)
    {
        List<Invoice> list = invoiceService.selectInvoiceList(invoice);
        ExcelUtil<Invoice> util = new ExcelUtil<Invoice>(Invoice.class);
        return AjaxResult.success("导出成功").put("data", util.exportExcel(list, "发票数据"));
    }

    /**
     * 新增发票
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存发票
     */
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(Invoice invoice)
    {
        return toAjax(invoiceService.insertInvoice(invoice));
    }

    /**
     * 修改发票
     */
    @GetMapping("/edit/{invoiceId}")
    public String edit(@PathVariable("invoiceId") Long invoiceId, ModelMap mmap)
    {
        Invoice invoice = invoiceService.selectInvoiceById(invoiceId);
        mmap.put("invoice", invoice);
        return prefix + "/edit";
    }

    /**
     * 修改保存发票
     */
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(Invoice invoice)
    {
        return toAjax(invoiceService.updateInvoice(invoice));
    }

    /**
     * 删除发票
     */
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        String[] strIds = convertToStrArray(ids);
        Long[] longIds = new Long[strIds.length];
        for (int i = 0; i < strIds.length; i++) {
            longIds[i] = Long.parseLong(strIds[i]);
        }
        return toAjax(invoiceService.deleteInvoiceByIds(longIds));
    }

    /**
     * 上传发票文件
     */
    @PostMapping("/upload")
    @ResponseBody
    public AjaxResult uploadInvoice(@RequestParam("invoiceFile") MultipartFile file,
                                   Invoice invoice)
    {
        try {
            if (file.isEmpty()) {
                return error("请选择要上传的发票文件");
            }

            byte[] fileBytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            
            int result = invoiceService.uploadInvoiceFile(invoice, fileBytes, originalFilename);
            return toAjax(result);
        } catch (Exception e) {
            return error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 提交URL链接发票
     */
    @PostMapping("/submitUrl")
    @ResponseBody
    public AjaxResult submitInvoiceUrl(Invoice invoice)
    {
        try {
            int result = invoiceService.submitInvoiceUrl(invoice);
            return toAjax(result);
        } catch (Exception e) {
            return error("提交失败：" + e.getMessage());
        }
    }

    /**
     * 重新提交发票
     */
    @PostMapping("/resubmit")
    @ResponseBody
    public AjaxResult resubmitInvoice(@RequestParam("invoiceFile") MultipartFile file,
                                     Invoice invoice)
    {
        try {
            if (file.isEmpty()) {
                return error("请选择要重新上传的发票文件");
            }

            byte[] fileBytes = file.getBytes();
            String originalFilename = file.getOriginalFilename();
            
            int result = invoiceService.resubmitInvoice(invoice, fileBytes, originalFilename);
            return toAjax(result);
        } catch (Exception e) {
            return error("重新提交失败：" + e.getMessage());
        }
    }

    /**
     * 更新发票状态
     */
    @PostMapping("/updateStatus")
    @ResponseBody
    public AjaxResult updateInvoiceStatus(@RequestParam Long invoiceId,
                                        @RequestParam String status)
    {
        return toAjax(invoiceService.updateInvoiceStatus(invoiceId, status));
    }

    /**
     * 更新报销状态
     */
    @PostMapping("/updateReimbursementStatus")
    @ResponseBody
    public AjaxResult updateReimbursementStatus(@RequestParam Long invoiceId,
                                              @RequestParam String reimbursementStatus)
    {
        return toAjax(invoiceService.updateReimbursementStatus(invoiceId, reimbursementStatus));
    }

    /**
     * 获取发票详情
     */
    @GetMapping("/detail/{invoiceId}")
    @ResponseBody
    public AjaxResult getInvoiceDetail(@PathVariable Long invoiceId)
    {
        Invoice invoice = invoiceService.selectInvoiceById(invoiceId);
        return success(invoice);
    }

    /**
     * 下载发票文件
     */
    @GetMapping("/download/{invoiceId}")
    @ResponseBody
    public AjaxResult downloadInvoice(@PathVariable Long invoiceId)
    {
        try {
            byte[] fileBytes = invoiceService.downloadInvoiceFile(invoiceId);
            String fileInfo = invoiceService.getInvoiceFileInfo(invoiceId);
            return success().put("fileData", fileBytes).put("fileInfo", fileInfo);
        } catch (Exception e) {
            return error("下载失败：" + e.getMessage());
        }
    }

    /**
     * 上传者筛选发票
     */
    @PostMapping("/uploaderFilter")
    @ResponseBody
    public TableDataInfo uploaderFilter(@RequestParam Long uploaderId,
                                       @RequestParam(required = false) Date startDate,
                                       @RequestParam(required = false) Date endDate,
                                       @RequestParam(required = false) String status,
                                       @RequestParam(required = false) String reimbursementStatus)
    {
        List<Invoice> list = invoiceService.selectInvoiceByUploaderFilter(
            uploaderId, startDate, endDate, status, reimbursementStatus);
        return getDataTable(list);
    }

    /**
     * 管理员筛选发票
     */
    @PostMapping("/adminFilter")
    @ResponseBody
    public TableDataInfo adminFilter(@RequestParam Long adminId,
                                    @RequestParam(required = false) Long uploaderId,
                                    @RequestParam(required = false) Date startDate,
                                    @RequestParam(required = false) Date endDate,
                                    @RequestParam(required = false) String reimbursementStatus)
    {
        List<Invoice> list = invoiceService.selectInvoiceByAdminFilter(
            adminId, uploaderId, startDate, endDate, reimbursementStatus);
        return getDataTable(list);
    }

    /**
     * 超级管理员筛选发票
     */
    @PostMapping("/superAdminFilter")
    @ResponseBody
    public TableDataInfo superAdminFilter(@RequestParam(required = false) Long uploaderId,
                                         @RequestParam(required = false) Long adminId,
                                         @RequestParam(required = false) Date startDate,
                                         @RequestParam(required = false) Date endDate,
                                         @RequestParam(required = false) String status,
                                         @RequestParam(required = false) String reimbursementStatus)
    {
        List<Invoice> list = invoiceService.selectInvoiceBySuperAdminFilter(
            uploaderId, adminId, startDate, endDate, status, reimbursementStatus);
        return getDataTable(list);
    }

    /**
     * 验证发票权限
     */
    @PostMapping("/validatePermission")
    @ResponseBody
    public AjaxResult validatePermission(@RequestParam Long invoiceId,
                                       @RequestParam Long userId,
                                       @RequestParam String userRole)
    {
        boolean hasPermission = invoiceService.validateInvoicePermission(invoiceId, userId, userRole);
        return success().put("hasPermission", hasPermission);
    }
}
