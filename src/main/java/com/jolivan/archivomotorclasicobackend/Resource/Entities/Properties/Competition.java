package com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
public class Competition extends Property {
    @NonNull
    String name;

}
