package com.takeaway.scoober.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Player {

	private String name;

	private Integer addedNumber;

	private Integer resultingNumber;

}
