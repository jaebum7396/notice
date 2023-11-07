package com.notice.controller;

import io.swagger.annotations.Api;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Api(tags = "NoticeController")
@Tag(name = "NoticeController", description = "공지사항 서비스 제공")
@RestController
@RequiredArgsConstructor
public class NoticeController {

}
