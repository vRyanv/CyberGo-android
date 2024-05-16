package com.tech.cybercars.data.remote.user.statistic;

import com.tech.cybercars.data.remote.base.BaseResponse;

public class StatisticResponse extends BaseResponse {
    public Quantity trip_quantity_of_vehicle;
    public Quantity revenue_of_vehicle;
    public static class Quantity{
        public int moto;
        public int car;
        public int truck;
    }
}
