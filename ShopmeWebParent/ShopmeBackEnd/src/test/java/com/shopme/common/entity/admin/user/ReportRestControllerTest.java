package com.shopme.common.entity.admin.user;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ReportRestControllerTest {

    @Autowired
    private MockMvc mockMvc;


    @Test
    @WithMockUser(username="user1", password = "pass1", authorities = "Salesperson")
    public void testGetReportDataLast7Days() throws Exception {
       String url = "/reports/sales_by_date/last_7_days";

       mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }


    @Test
    @WithMockUser(username="user1", password = "pass1", authorities = "Salesperson")
    public void testGetReportDataLast6Months() throws Exception {
        String url = "/reports/sales_by_date/last_6_months";

        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }
    @Test
    @WithMockUser(username="user1", password = "pass1", authorities = "Salesperson")
    public void testGetReportDataByDateRange() throws Exception {

        String startDate = " 2022-09-01";
        String endDate = "2022-09-25";
        String url = "/reports/sales_by_date/"+startDate+"/"+endDate;

        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(username="user1", password = "pass1", authorities = "Salesperson")
    public void testGetReportDataByCategory() throws Exception {
        String url = "/reports/category/last_7_days";

        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    @WithMockUser(username="user1", password = "pass1", authorities = "Salesperson")
    public void testGetReportDataByProduct() throws Exception {
        String url = "/reports/product/last_7_days";

        mockMvc.perform(get(url)).andExpect(status().isOk()).andDo(print());
    }
}
