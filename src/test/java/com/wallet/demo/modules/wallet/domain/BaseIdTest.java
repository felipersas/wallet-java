package com.wallet.demo.modules.wallet.domain;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.wallet.demo.shared.domain.WalletId;

class BaseIdTest {

  @Nested
  class OwnerIdTests {

    @Test
    void shouldCreateFromUUID() {
      UUID uuid = UUID.randomUUID();
      OwnerId ownerId = new OwnerId(uuid);

      assertEquals(uuid, ownerId.value());
    }

    @Test
    void shouldCreateFromNewId() {
      OwnerId id = OwnerId.newId();

      assertNotNull(id);
      assertNotNull(id.value());
    }

    @Test
    void shouldParseFromString() {
      UUID uuid = UUID.randomUUID();
      OwnerId ownerId = OwnerId.from(uuid.toString());

      assertEquals(uuid, ownerId.value());
    }

    @Test
    void shouldRejectNullUUID() {
      assertThrows(NullPointerException.class, () -> new OwnerId(null));
    }

    @Test
    void shouldRejectInvalidString() {
      assertThrows(IllegalArgumentException.class, () -> OwnerId.from("not-a-uuid"));
    }
  }

  @Nested
  class WalletIdTests {

    @Test
    void shouldCreateFromUUID() {
      UUID uuid = UUID.randomUUID();
      WalletId walletId = new WalletId(uuid);

      assertEquals(uuid, walletId.value());
    }

    @Test
    void shouldCreateFromNewId() {
      WalletId id = WalletId.newId();

      assertNotNull(id);
      assertNotNull(id.value());
    }

    @Test
    void shouldRejectNullUUID() {
      assertThrows(NullPointerException.class, () -> new WalletId(null));
    }
  }

  @Nested
  class Equality {

    @Test
    void sameValueShouldBeEqual() {
      UUID uuid = UUID.randomUUID();
      OwnerId a = new OwnerId(uuid);
      OwnerId b = new OwnerId(uuid);

      assertEquals(a, b);
      assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    void differentValuesShouldNotBeEqual() {
      OwnerId a = OwnerId.newId();
      OwnerId b = OwnerId.newId();

      assertNotEquals(a, b);
    }

    @Test
    void differentTypesShouldNotBeEqual() {
      UUID uuid = UUID.randomUUID();
      OwnerId ownerId = new OwnerId(uuid);
      WalletId walletId = new WalletId(uuid);

      assertNotEquals(ownerId, walletId);
    }

    @Test
    void shouldNotEqualToNull() {
      OwnerId id = OwnerId.newId();

      assertNotEquals(null, id);
    }

    @Test
    void shouldNotEqualUnrelatedType() {
      OwnerId id = OwnerId.newId();

      assertNotEquals("not-an-id", id);
    }

    @Test
    void shouldEqualItself() {
      OwnerId id = OwnerId.newId();

      assertEquals(id, id);
    }
  }

  @Nested
  class ToString {

    @Test
    void shouldReturnUUIDString() {
      UUID uuid = UUID.randomUUID();
      OwnerId ownerId = new OwnerId(uuid);

      assertEquals(uuid.toString(), ownerId.toString());
    }
  }
}
