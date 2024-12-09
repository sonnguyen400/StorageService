package com.sonnguyen.storageservice.viewmodel;

import com.sonnguyen.storageservice.constant.FileAccessType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FileInfoPostVm(
        @NotNull FileAccessType accessType,
        @NotBlank String owner
) {
}
