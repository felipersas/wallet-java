package com.wallet.demo.modules.transfer.presentation;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigInteger;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.wallet.demo.modules.transfer.application.TransferViewDto;
import com.wallet.demo.modules.transfer.application.usecases.CreateTransferUseCase;
import com.wallet.demo.modules.transfer.application.usecases.GetTransferUseCase;
import com.wallet.demo.modules.transfer.domain.TransferId;
import com.wallet.demo.modules.transfer.domain.enums.TransferStatus;
import com.wallet.demo.shared.domain.Money;
import com.wallet.demo.shared.domain.WalletId;
import com.wallet.demo.shared.domain.enums.MoneyCurrency;

@WebMvcTest(TransferController.class)
class TransferControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CreateTransferUseCase createTransferUseCase;

  @MockitoBean
  private GetTransferUseCase getTransferUseCase;

  @Nested
  class CreateTransfer {

    @Test
    void shouldReturn200WhenTransferCreated() throws Exception {
      String sourceId = WalletId.newId().toString();
      String destinationId = WalletId.newId().toString();

      mockMvc.perform(post("/transfers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {
                "source_wallet_id": "%s",
                "destination_wallet_id": "%s",
                "amount": 500,
                "currency": "BRL"
              }
              """.formatted(sourceId, destinationId)))
          .andExpect(status().isOk());
    }

    @Test
    void shouldReturn400WhenSourceWalletIdIsBlank() throws Exception {
      mockMvc.perform(post("/transfers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {
                "source_wallet_id": "",
                "destination_wallet_id": "%s",
                "amount": 500,
                "currency": "BRL"
              }
              """.formatted(WalletId.newId())))
          .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenDestinationWalletIdIsBlank() throws Exception {
      mockMvc.perform(post("/transfers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {
                "source_wallet_id": "%s",
                "destination_wallet_id": "",
                "amount": 500,
                "currency": "BRL"
              }
              """.formatted(WalletId.newId())))
          .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenAmountIsMissing() throws Exception {
      mockMvc.perform(post("/transfers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {
                "source_wallet_id": "%s",
                "destination_wallet_id": "%s",
                "currency": "BRL"
              }
              """.formatted(WalletId.newId(), WalletId.newId())))
          .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn400WhenCurrencyIsMissing() throws Exception {
      mockMvc.perform(post("/transfers")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {
                "source_wallet_id": "%s",
                "destination_wallet_id": "%s",
                "amount": 500
              }
              """.formatted(WalletId.newId(), WalletId.newId())))
          .andExpect(status().isBadRequest());
    }
  }

  @Nested
  class GetTransfer {

    @Test
    void shouldReturn200WithTransfer() throws Exception {
      TransferId transferId = TransferId.newId();
      String sourceId = WalletId.newId().toString();
      String destinationId = WalletId.newId().toString();
      Money money = Money.create(BigInteger.valueOf(500), MoneyCurrency.BRL);
      TransferViewDto dto = new TransferViewDto(transferId.toString(), sourceId, destinationId,
          money, MoneyCurrency.BRL, TransferStatus.PENDING, null);
      when(getTransferUseCase.execute(any())).thenReturn(dto);

      mockMvc.perform(get("/transfers/{id}", transferId))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(transferId.toString()))
          .andExpect(jsonPath("$.source_wallet_id").value(sourceId))
          .andExpect(jsonPath("$.destination_wallet_id").value(destinationId))
          .andExpect(jsonPath("$.amount").value(500))
          .andExpect(jsonPath("$.currency").value("BRL"))
          .andExpect(jsonPath("$.status").value("PENDING"));
    }

    @Test
    void shouldReturn404WhenTransferNotFound() throws Exception {
      TransferId transferId = TransferId.newId();
      when(getTransferUseCase.execute(any()))
          .thenThrow(new com.wallet.demo.modules.transfer.domain.exceptions.TransferNotFoundException(transferId.toString()));

      mockMvc.perform(get("/transfers/{id}", transferId))
          .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn400WhenIdIsNotValidUUID() throws Exception {
      mockMvc.perform(get("/transfers/{id}", "not-a-uuid"))
          .andExpect(status().isBadRequest());
    }
  }
}
