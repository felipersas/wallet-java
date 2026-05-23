# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
./mvnw clean install                  # Build + run all tests
./mvnw test                           # Run all tests
./mvnw test -pl . -Dtest=WalletTest   # Run a single test class
./mvnw spring-boot:run                # Start the app (H2 in-memory DB)
```

HTTP test requests are in `requests/` (wallet.http, transfer.http) for use with IDE REST clients.

## Architecture

Spring Boot 4.0.6 / Java 17 / Maven. Dual database: H2 (default/test), PostgreSQL (postgres profile).

### Architectural Decisions

- **JPA annotations on domain entities** — for simplicity, `@Entity`, `@Table`, `@Column`, `@Embeddable` annotations are placed directly on domain entities and value objects rather than maintaining separate infrastructure JPA classes. This couples domain to persistence but eliminates mapping boilerplate in a small project.

### Modular DDD Structure

Each bounded context lives under `modules/{module}/` with four layers:

| Layer | Package | Purpose |
|-------|---------|---------|
| **Presentation** | `presentation/` | Controllers, request/response DTOs, mappers |
| **Application** | `application/` | Use cases (injected directly into controllers), view DTOs, facades |
| **Domain** | `domain/` | Entities, value objects, domain services, interfaces (ports), exceptions |
| **Infrastructure** | `infrastructure/` | Repository implementations, cross-module adapter implementations |

**Current modules:** `wallet`, `transfer`

### Shared Kernel (`shared/`)

- `shared.domain` — `BaseId` (abstract UUID wrapper), `Money` (amount + currency), `WalletId`
- `shared.domain.enums` — `MoneyCurrency`
- `shared.domain.exceptions` — `DomainException` (abstract, with `ErrorType` enum: NOT_FOUND, CONFLICT, RULE_VIOLATION)
- `shared.presentation` — `ApiExceptionHandler` maps `DomainException.ErrorType` to HTTP status, handles validation errors

### Key Patterns

- **Value objects** extend `BaseId`, use static factories (`OwnerId.from(string)`, `WalletId.newId()`)
- **Entities** use private constructors with static factory methods (`Wallet.open()`, `Transfer.create()`)
- **Cross-module dependencies** go through domain interfaces (ports). E.g., transfer module defines `WalletCreditor`, `WalletDebtor`, `WalletExistenceChecker` — implemented in wallet's infrastructure layer
- **Domain exceptions** extend `DomainException` with an `ErrorType`; the global exception handler auto-maps them to HTTP status codes
- **Use cases** are `@Component`/`@Service` classes injected directly into controllers (no application service facade)
- **Transfer execution** is `@Async` with programmatic transactions via `TransactionTemplate`

### Testing Conventions

- JUnit 5 with `@Nested` inner classes to group related test cases
- Controller tests use `@WebMvcTest` + `@MockitoBean` + MockMvc
- Domain/infrastructure tests are unit tests with Mockito where needed

## CodeGraph

This project has CodeGraph initialized. Use `codegraph_*` tools for faster symbol lookup and code exploration instead of grep/find.
