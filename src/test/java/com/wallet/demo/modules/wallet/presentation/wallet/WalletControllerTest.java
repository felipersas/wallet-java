package com.wallet.demo.modules.wallet.presentation.wallet;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigInteger;
import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.wallet.demo.modules.wallet.application.WalletViewDto;
import com.wallet.demo.modules.wallet.application.usecases.CreateWalletUseCase;
import com.wallet.demo.modules.wallet.application.usecases.GetWalletUseCase;
import com.wallet.demo.modules.wallet.domain.exceptions.DuplicateOwnerWalletException;
import com.wallet.demo.modules.wallet.domain.exceptions.WalletNotFoundException;
import com.wallet.demo.shared.domain.WalletId;

@WebMvcTest(WalletController.class)
class WalletControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private CreateWalletUseCase createWalletUseCase;

  @MockitoBean
  private GetWalletUseCase getWalletUseCase;

  @Nested
  class CreateWallet {

    @Test
    void shouldReturn201WithCreatedWallet() throws Exception {
      UUID ownerUuid = UUID.randomUUID();
      WalletId walletId = WalletId.newId();
      when(createWalletUseCase.execute(any(), eq(BigInteger.valueOf(500))))
          .thenReturn(new WalletViewDto(walletId.toString(), ownerUuid.toString(), BigInteger.valueOf(500)));

      mockMvc.perform(post("/wallets")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {"owner_id": "%s", "initial_balance": 500}
              """.formatted(ownerUuid)))
          .andExpect(status().isCreated())
          .andExpect(header().string("Location", "/wallets/" + walletId))
          .andExpect(jsonPath("$.id").value(walletId.toString()))
          .andExpect(jsonPath("$.owner_id").value(ownerUuid.toString()))
          .andExpect(jsonPath("$.balance").value(500));
    }

    @Test
    void shouldReturn400WhenOwnerIdIsBlank() throws Exception {
      mockMvc.perform(post("/wallets")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {"owner_id": "", "initial_balance": 500}
              """))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("Validation failed"))
          .andExpect(jsonPath("$.errors[?(@.field == 'ownerId')]").exists());
    }

    @Test
    void shouldReturn400WhenOwnerIdIsMissing() throws Exception {
      mockMvc.perform(post("/wallets")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {"initial_balance": 500}
              """))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void shouldReturn400WhenBalanceIsNegative() throws Exception {
      UUID ownerUuid = UUID.randomUUID();
      mockMvc.perform(post("/wallets")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {"owner_id": "%s", "initial_balance": -100}
              """.formatted(ownerUuid)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void shouldReturn400WhenBalanceIsNull() throws Exception {
      UUID ownerUuid = UUID.randomUUID();
      mockMvc.perform(post("/wallets")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {"owner_id": "%s"}
              """.formatted(ownerUuid)))
          .andExpect(status().isBadRequest())
          .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void shouldReturn409WhenDuplicateOwner() throws Exception {
      UUID ownerUuid = UUID.randomUUID();
      when(createWalletUseCase.execute(any(), any()))
          .thenThrow(new DuplicateOwnerWalletException(ownerUuid.toString()));

      mockMvc.perform(post("/wallets")
          .contentType(MediaType.APPLICATION_JSON)
          .content("""
              {"owner_id": "%s", "initial_balance": 0}
              """.formatted(ownerUuid)))
          .andExpect(status().isConflict())
          .andExpect(jsonPath("$.message").value("Wallet already exists for owner: " + ownerUuid));
    }
  }

  @Nested
  class GetWallet {

    @Test
    void shouldReturn200WithWallet() throws Exception {
      UUID ownerUuid = UUID.randomUUID();
      WalletId walletId = WalletId.newId();
      when(getWalletUseCase.execute(any()))
          .thenReturn(new WalletViewDto(walletId.toString(), ownerUuid.toString(), BigInteger.valueOf(300)));

      mockMvc.perform(get("/wallets/{id}", walletId))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.id").value(walletId.toString()))
          .andExpect(jsonPath("$.owner_id").value(ownerUuid.toString()))
          .andExpect(jsonPath("$.balance").value(300));
    }

    @Test
    void shouldReturn404WhenWalletNotFound() throws Exception {
      WalletId walletId = WalletId.newId();
      when(getWalletUseCase.execute(any()))
          .thenThrow(new WalletNotFoundException(walletId.toString()));

      mockMvc.perform(get("/wallets/{id}", walletId))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.message").value("Wallet not found: " + walletId));
    }

    @Test
    void shouldReturn400WhenIdIsNotValidUUID() throws Exception {
      mockMvc.perform(get("/wallets/{id}", "not-a-uuid"))
          .andExpect(status().isBadRequest());
    }
  }
}
