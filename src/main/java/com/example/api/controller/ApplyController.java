package com.example.api.controller;

import com.example.api.exception.*;
import com.example.api.repository.CouponCountRepository;
import com.example.api.service.ApplyService;
import com.example.api.service.CurrUserSessionService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Controller
public class ApplyController {

    private final ApplyService applyService;
    private final CurrUserSessionService currUserSessionService;

    @Autowired
    public ApplyController(ApplyService applyService, CurrUserSessionService currUserSessionService) {
        this.applyService = applyService;
        this.currUserSessionService = currUserSessionService;
    }

    @PostMapping("/issue-coupon")
    public ResponseEntity<String> applyCoupon(HttpSession session) {
        try {
            String sessionId = "curr_user_session"; // Set to the appropriate session ID or key
            Long userId = currUserSessionService.getSession(sessionId);

//            Long userId = (Long) session.getAttribute("userId"); //임의로 지정한 userID

            // 세션에서 사용자 ID가 없는 경우
            if (userId == null) {
                return ResponseEntity.status(400).body("사용자 ID가 세션에 없습니다");
            }

            applyService.apply(userId);
            return ResponseEntity.ok().body("쿠폰 발급 성공");
        } catch (DuplicateCouponException e) {
            System.out.println("error in duplicated userID");
            return ResponseEntity.ok().body("중복 쿠폰 발급 오류");
        } catch (CouponLimitExceededException e) {
            System.out.println("coupon issuance limit exceeded");
            return ResponseEntity.ok().body("쿠폰 초과 발급 오류");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("쿠폰 발급 실패");
        }
    }

}
