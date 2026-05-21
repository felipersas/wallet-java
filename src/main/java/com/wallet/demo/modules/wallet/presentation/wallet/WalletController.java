package com.wallet.demo.modules.wallet.presentation.wallet;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

import com.wallet.demo.modules.wallet.application.WalletApplicationService;
import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.WalletId;

@RestController
@RequestMapping("/wallets")
@Validated
public class WalletController {
  private final WalletApplicationService walletApplicationService;

  public WalletController(WalletApplicationService walletApplicationService) {
    this.walletApplicationService = walletApplicationService;
  }

  @PostMapping
  public ResponseEntity<WalletResponse> create(@Valid @RequestBody CreateWalletRequest request) {
    WalletResponse response = WalletMapper.toResponse(
        walletApplicationService.createWallet(OwnerId.from(request.ownerId()), request.initialBalance()));
    return ResponseEntity.created(URI.create("/wallets/" + response.id())).body(response);
  }

  @GetMapping("/{id}")
  public WalletResponse getById(@PathVariable String id) {
    return WalletMapper.toResponse(walletApplicationService.getWallet(WalletId.from(id)));
  }
}
