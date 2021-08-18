package com.css.work.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @NonNull
    private SimulateMode mode;

    @NonNull
    private String orderJsonPath;
}
