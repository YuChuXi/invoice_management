package com.ruoyi.system.controller;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.Invoice;
import com.ruoyi.system.service.IInvoiceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

/**
 * InvoiceController 测试类
 */
public class InvoiceControllerTest {

    private MockMvc mockMvc;

    @Mock
    private IInvoiceService invoiceService;

    @InjectMocks
    private InvoiceController invoiceController;

    private Invoice testInvoice;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(invoiceController).build();

        // 创建测试发票对象
        testInvoice = new Invoice();
        testInvoice.setInvoiceId(1L);
        testInvoice.setInvoiceNumber("INV20241115001");
        testInvoice.setSupplierName("测试供应商");
        testInvoice.setPurchaseDate(new Date());
        testInvoice.setAmount(1000.00);
        testInvoice.setRemarks("测试发票备注");
        testInvoice.setStatus("pending");
        testInvoice.setReimbursementStatus("unclaimed");
        testInvoice.setUploaderId(1L);
        testInvoice.setAdminId(2L);
    }
    
    @Test
    public void testGetInvoiceDetail() throws Exception {
        when(invoiceService.selectInvoiceById(1L)).thenReturn(testInvoice);

        mockMvc.perform(get("/system/invoice/detail/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.invoiceNumber").value("INV20241115001"))
                .andExpect(jsonPath("$.data.supplierName").value("测试供应商"));
    }

    @Test
    public void testUpdateInvoiceStatus() throws Exception {
        when(invoiceService.updateInvoiceStatus(1L, "approved")).thenReturn(1);

        mockMvc.perform(post("/system/invoice/updateStatus")
                .param("invoiceId", "1")
                .param("status", "approved"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));
    }

    @Test
    public void testUpdateReimbursementStatus() throws Exception {
        when(invoiceService.updateReimbursementStatus(1L, "reimbursed")).thenReturn(1);

        mockMvc.perform(post("/system/invoice/updateReimbursementStatus")
                .param("invoiceId", "1")
                .param("reimbursementStatus", "reimbursed"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.msg").value("操作成功"));
    }

    @Test
    public void testValidatePermission() throws Exception {
        when(invoiceService.validateInvoicePermission(1L, 1L, "uploader")).thenReturn(true);

        mockMvc.perform(post("/system/invoice/validatePermission")
                .param("invoiceId", "1")
                .param("userId", "1")
                .param("userRole", "uploader"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.hasPermission").value(true));
    }
}
