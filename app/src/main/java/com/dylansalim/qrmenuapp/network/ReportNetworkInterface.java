package com.dylansalim.qrmenuapp.network;

import com.dylansalim.qrmenuapp.models.dto.Result;
import com.dylansalim.qrmenuapp.models.dto.ReportDto;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ReportNetworkInterface {

    @POST("/report/submit-report")
    Observable<Result<String>> submitReport(@Body ReportDto reportDto);
}
