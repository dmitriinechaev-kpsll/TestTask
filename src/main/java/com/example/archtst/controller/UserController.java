package com.example.archtst.controller;

import com.example.archtst.constant.Urls;
import com.example.archtst.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RequestMapping(Urls.BASE_URL)
public interface UserController {

    @Operation(summary = "Создать пользователя")
    @PostMapping
    ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO userDTO);

    @Operation(summary = "Поиск пользователей")
    @PostMapping(Urls.SEARCH)
    ResponseEntity<Page<UserResponseDTO>> searchUsers(
            @RequestBody UserSearchRequestDTO request
    );

    @Operation(summary = "Получить всех пользователей")
    @GetMapping
    ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @ParameterObject
            @PageableDefault(page = 0, size = 10, sort = "id")
            Pageable pageable
    );

    @Operation(summary = "Получить пользователя по ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно найден"),
            // content = @Content убирает генерацию странных схем для ошибки 404
            @ApiResponse(responseCode = "404", description = "Пользователь с таким ID не найден")
    })
    @GetMapping(Urls.BY_ID)
    ResponseEntity<UserResponseDTO> getUserById(@PathVariable UUID id);

    @Operation(summary = "Удалить пользователя по ID")
    @ApiResponses(value = {
            // content = @Content - это та самая магия, которая уберет черный квадрат со "string"
            @ApiResponse(responseCode = "204", description = "Пользователь успешно удален", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    @DeleteMapping(Urls.BY_ID)
    ResponseEntity<Void> deleteUser(@PathVariable UUID id);

    //    @GetMapping(Urls.STATS)
    //ResponseEntity<Map<String, Object>> getStats();

    // Добавление адреса пользователю
    @Operation(summary = "Добавить новый адрес пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Адрес успешно добавлен"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации данных адреса", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    })
    @PostMapping(Urls.ADDRESSES)
    ResponseEntity<AddressResponseDTO> addAddress(
            @Parameter(description = "Уникальный идентификатор (UUID) пользователя", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,
            @Valid @RequestBody AddressRequestDTO request);

    @Operation(summary = "Получить адреса пользователя (с пагинацией)")
    @GetMapping(Urls.ADDRESSES) // Предполагаю, что тут маппинг вроде "/{id}/addresses"
    ResponseEntity<Page<AddressResponseDTO>> getUserAddresses(
            @Parameter(description = "ID пользователя", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,

            @ParameterObject
            @PageableDefault(size = 5, sort = "createdAt") // По умолчанию отдаем по 5 штук, новые сверху (если есть поле createdAt)
            Pageable pageable
    );

    @Operation(summary = "Удалить адрес пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Адрес успешно удален", content = @Content),
            @ApiResponse(responseCode = "404", description = "Пользователь или адрес не найден", content = @Content)
    })
    // URL будет выглядеть примерно так: /api/v1/users/{id}/addresses/{addressId}
    @DeleteMapping(Urls.ADDRESSES + "/{addressId}")
    ResponseEntity<Void> removeAddress(
            @Parameter(description = "ID пользователя", example = "123e4567-e89b-12d3-a456-426614174000")
            @PathVariable UUID id,

            @Parameter(description = "ID адреса для удаления", example = "987fcdeb-51a2-43d7-9012-3456789abcde")
            @PathVariable UUID addressId
    );

    @Operation(summary = "Назначить роль пользователю")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль успешно назначена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Ошибка валидации или роль не существует в БД")
    })
    @PostMapping(Urls.ROLES)
    ResponseEntity<UserResponseDTO> addRole(
            @PathVariable UUID id,
            @Valid @RequestBody RoleRequestDTO request
    );

    @Operation(summary = "Забрать роль у пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Роль успешно удалена"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден"),
            @ApiResponse(responseCode = "400", description = "Роль не существует в БД")
    })
    @DeleteMapping(Urls.ROLES + "/{roleName}")
    ResponseEntity<UserResponseDTO> removeRole(
            @PathVariable UUID id,
            @PathVariable String roleName
    );

}