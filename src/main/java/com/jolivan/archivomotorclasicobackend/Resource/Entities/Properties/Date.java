package com.jolivan.archivomotorclasicobackend.Resource.Entities.Properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Date extends Property{
    ZonedDateTime date;
}
