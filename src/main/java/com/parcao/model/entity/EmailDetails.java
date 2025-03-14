package com.parcao.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailDetails {
    private String msgBody;
    private String subject;
    private String attachment;
    private String typeRecipient;
}


