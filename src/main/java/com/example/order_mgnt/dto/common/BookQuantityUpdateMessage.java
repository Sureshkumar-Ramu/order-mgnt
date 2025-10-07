package com.example.order_mgnt.dto.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookQuantityUpdateMessage {
    private UUID bookId;
    private int quantityToReduce;
}
