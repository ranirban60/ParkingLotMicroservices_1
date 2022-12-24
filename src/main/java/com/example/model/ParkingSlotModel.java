package com.example.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParkingSlotModel {
    
	private int slotNumber;
	private String carRegNum;
	private String carName;
	private String carOwnerName;
	private LocalDateTime entryTime;
	private LocalDateTime exitTime;
	private Double fare;
	
	
}
