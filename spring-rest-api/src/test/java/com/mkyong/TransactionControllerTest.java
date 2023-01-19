package com.mkyong;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//@WebMvcTest(TransactionController.class)
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TransactionControllerTest {

    private static final ObjectMapper om = new ObjectMapper();

    private static final DateTimeFormatter df = new DateTimeFormatterBuilder()
            .parseCaseInsensitive()
            .appendPattern("dd/MMM/yyyy")
            .toFormatter(Locale.ENGLISH);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionRepository mockRepository;

    @Before
    public void init() {
        Transaction transaction = new Transaction(LocalDate.parse("01/Nov/2020", df), "Morrisons", "card", new BigDecimal("10.40"), "Groceries");
        when(mockRepository.findById(1L)).thenReturn(Optional.of(transaction));
    }

    @Test
    public void find_allTransaction_OK() throws Exception {

        List<Transaction> transaction = Arrays.asList(
                new Transaction(LocalDate.parse("01/Nov/2020", df), "Morrisons", "card", new BigDecimal("10.40"), "Groceries"));

        when(mockRepository.findAll()).thenReturn(transaction);

        mockMvc.perform(get("/transaction/2020"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].vendor", is("Morrisons")))
                .andExpect(jsonPath("$[0].type", is("card")))
                .andExpect(jsonPath("$[0].price", is(10.40)))
                .andExpect(jsonPath("$[0].category", is("Groceries")));

        verify(mockRepository, times(1)).findAll();
    }

    @Test
    public void find_bookIdNotFound_404() throws Exception {
        mockMvc.perform(get("/testing")).andExpect(status().isNotFound());
    }

    @Test
    public void find_bookIdNotFound_405() throws Exception {
        mockMvc.perform(get("/transaction")).andExpect(status().is4xxClientError());
    }

    private static void printJSON(Object object) {
        String result;
        try {
            result = om.writerWithDefaultPrettyPrinter().writeValueAsString(object);
            System.out.println(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
