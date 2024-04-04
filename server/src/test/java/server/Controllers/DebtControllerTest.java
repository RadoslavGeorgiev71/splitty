//package server.Controllers;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.web.servlet.MockMvc;
//import server.Services.DebtService;
//import server.api.DebtController;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@WebMvcTest(DebtController.class)
//public class DebtControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private DebtService debtService;
//
//    @Test
//    public void getAllDebtsTest() throws Exception {
//        mockMvc.perform(get("/api/debts"))
//                .andExpect(status().isOk());
//    }
//
//    // Add more tests for other endpoints
//}