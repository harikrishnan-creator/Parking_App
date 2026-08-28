package com.parking.app.service;

import com.parking.app.dto.BillingDTO;

public interface BillingService {

    BillingDTO getBill(String tokenNumber);

    Double calculateBill(Long parkedMinutes);

}
