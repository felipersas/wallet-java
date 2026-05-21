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

import com.wallet.demo.modules.wallet.application.usecases.CreateWalletUseCase;
import com.wallet.demo.modules.wallet.application.usecases.GetWalletUseCase;
import com.wallet.demo.modules.wallet.domain.OwnerId;
import com.wallet.demo.modules.wallet.domain.WalletId;

@RestController
@RequestMapping("/wallets")
@Validated
public class WalletController {
  private final CreateWalletUseCase createWalletUseCase;
  private final GetWalletUseCase getWalletUseCase;

  public WalletController(CreateWalletUseCase createWalletUseCase, GetWalletUseCase getWalletUseCase) {
    this.createWalletUseCase = createWalletUseCase;
    this.getWalletUseCase = getWalletUseCase;
  }

  @PostMapping
  public ResponseEntity<WalletResponse> create(@Valid @RequestBody CreateWalletRequest request) {
    WalletResponse response = WalletMapper.toResponse(
        createWalletUseCase.execute(OwnerId.from(request.ownerId()), request.initialBalance()));
    return ResponseEntity.created(URI.create("/wallets/" + response.id())).body(response);
  }

  @GetMapping("/{id}")
  public WalletResponse getById(@PathVariable String id) {
    return WalletMapper.toResponse(getWalletUseCase.execute(WalletId.from(id)));
  }
}
