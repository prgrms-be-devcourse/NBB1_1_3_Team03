package com.sscanner.team.trashcan.controller;


import com.sscanner.team.global.common.response.ApiResponse;
import com.sscanner.team.trashcan.requestDto.RegisterTrashcanRequestDto;
import com.sscanner.team.trashcan.responseDto.TrashcanResponseDto;
import com.sscanner.team.trashcan.service.TrashcanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/trashcan")
public class TrashcanApiController {

    private final TrashcanService trashcanService;

    @PostMapping()
    public ApiResponse<TrashcanResponseDto> registerTrashcan(@RequestBody @Valid RegisterTrashcanRequestDto requestDto){

        TrashcanResponseDto responseDto = trashcanService.registerTrashcan(requestDto);

        return ApiResponse.ok(201, responseDto, "쓰레기통 등록 성공");
    }

    @GetMapping("/{trashcanId}")
    public ApiResponse<TrashcanResponseDto> getTrashcanById(@PathVariable Long trashcanId){

        TrashcanResponseDto responseDto = trashcanService.getTrashcanById(trashcanId);

        return ApiResponse.ok(200, responseDto, "쓰레기통 조회 성공");
    }

}
