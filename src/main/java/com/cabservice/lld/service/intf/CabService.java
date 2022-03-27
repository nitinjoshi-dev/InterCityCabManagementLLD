package com.cabservice.lld.service.intf;

import com.cabservice.lld.constant.CabState;
import com.cabservice.lld.model.*;
import com.phonepe.test.model.*;
import com.cabservice.lld.model.dto.CabSnapshot;

import java.util.List;

public interface CabService {
    Cab registerCab(Cab cab);
    CabRiderMapping assignRiderToCab(Cab cab, Rider rider);
    CabCityMapping changeCabLocation(Cab cab, City city);
    CabStateMapping changeCabState(Cab cab, CabState cabState);
    List<CabSnapshot> getAllCabSnapshot();
    List<CabStateMapping> getCabStateHistory(Cab cab);
}
